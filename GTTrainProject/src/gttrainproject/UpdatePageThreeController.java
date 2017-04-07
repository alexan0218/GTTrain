/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gttrainproject;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Jihai,Yichen
 */
public class UpdatePageThreeController implements Initializable {
    GlobalSetting setting = GlobalSetting.getInstance();
    Boolean canSubmit = false;
    final static float UPDATE_FEE = 50;
    float updatedCost = 0;
    Reservation updatedRes;
    
    @FXML
    public Button button_functionality;
    @FXML
    public Button button_back;
    @FXML
    public Label warning;
    @FXML
    public DatePicker datePicker = new DatePicker();
    @FXML
    public Label totalCostLabel;
    
    @FXML
    public TableView tableView = new TableView();
    
    @FXML
    public TableColumn trainCol;
    @FXML
    public TableColumn timeCol;
    @FXML
    public TableColumn departCol;
    @FXML
    public TableColumn arriveCol;
    @FXML
    public TableColumn classCol;
    @FXML
    public TableColumn priceCol;
    @FXML
    public TableColumn baggageNumCol;
    @FXML
    public TableColumn nameCol;
    
    @FXML
    public TableView tableView2 = new TableView();
    
    @FXML
    public TableColumn trainCol2;
    @FXML
    public TableColumn timeCol2;
    @FXML
    public TableColumn departCol2;
    @FXML
    public TableColumn arriveCol2;
    @FXML
    public TableColumn classCol2;
    @FXML
    public TableColumn priceCol2;
    @FXML
    public TableColumn baggageNumCol2;
    @FXML
    public TableColumn nameCol2;
    
    @FXML
    public void searchAvailablity(ActionEvent e) throws IOException {
        //check availabity
        Reservation currentRes = setting.getCurrentReservation();
        Date currentDate = currentRes.getDepartureDate();
        if (currentDate.toLocalDate().getDayOfYear() - LocalDate.now().getDayOfYear() >= 1) {
            updatedCost = setting.getCurrentTotalCost();
            
            warning.setText("You can update with this date! Please submit your changes.");
            canSubmit = true;
            //populate updated reservation
            updatedRes = new Reservation(currentRes.getDepartureStation(), currentRes.getArrivalStation(), java.sql.Date.valueOf(datePicker.getValue()));
            System.out.println(java.sql.Date.valueOf(datePicker.getValue()));
            updatedRes.setTrainNumber(currentRes.getTrainNumber());
            updatedRes.setClass(currentRes.getTrainClass());
            updatedRes.setPassengerName(currentRes.getPassengerName());
            updatedRes.setBaggageNumber(currentRes.getBaggageNumber());
            updatedRes.setTicketPrice(currentRes.getTicketPrice());
                    
            //populate updated reservation
            ObservableList data = FXCollections.observableArrayList(updatedRes);
            tableView2.setItems(data);
        
            trainCol2.setCellValueFactory(new PropertyValueFactory("trainNumber"));
            timeCol2.setCellValueFactory(new PropertyValueFactory("date"));
            departCol2.setCellValueFactory(new PropertyValueFactory("departureStation"));
            arriveCol2.setCellValueFactory(new PropertyValueFactory("arrivalStation"));
            classCol2.setCellValueFactory(new PropertyValueFactory("trainClass"));
            priceCol2.setCellValueFactory(new PropertyValueFactory("ticketPrice"));
            baggageNumCol2.setCellValueFactory(new PropertyValueFactory("baggageNumber"));
            nameCol2.setCellValueFactory(new PropertyValueFactory("passengerName"));

            tableView2.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            
            //update total cost
            updatedCost = setting.getCurrentTotalCost() + UPDATE_FEE;
            totalCostLabel.setText("$" + Float.toString(updatedCost));
        } else {
            warning.setText("You can only update at least one day before train departs.");
        }
    }
    
    @FXML
    public void functionality_button_action(ActionEvent e) throws IOException {  
        
        
        if (canSubmit) {
            Connection con = null;
            Boolean error = false;
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_58", "cs4400_Team_58", "QxXNykgB");
                if(!con.isClosed()){
                    System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
                }
                //update reservation
                PreparedStatement stmt2 = con.prepareStatement("UPDATE RESERVATION "
                                   + "SET TOTAL_COST=" + updatedCost
                                   + " WHERE RESERVATION_ID='" + setting.getCurrentReservationID() + "' ");
                stmt2.execute();
                
                //update reserves
                System.out.println(updatedRes.getDepartureDate());
                PreparedStatement stmt = con.prepareStatement("UPDATE RESERVES "
                                   + "SET DEPATURE_DATE='" + updatedRes.getDepartureDate() + "'"
                                   + " WHERE RESERVATION_ID='" + setting.getCurrentReservationID() + "' "
                                   + "AND TRAIN_NUMBER='" + updatedRes.getTrainNumber() + "' ");
                stmt.execute();
                
            } catch (Exception ec) {
                System.err.println("Exception: " + ec.getMessage());
                error = true;
            } finally {
                try {
                    if(con != null) {
                        con.close();
                    }
                } catch(SQLException ec) {}
            }
            
            if (!error) {
                System.out.println("going to functionality page");
                Parent functionality_page_parent = FXMLLoader.load(getClass().getResource("customerChoosePage.fxml"));
                Scene functionality_page_scene = new Scene(functionality_page_parent);
                Stage functionality_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                functionality_page_stage.setScene(functionality_page_scene);
                functionality_page_stage.show();
            }
            
            
        } else {
            warning.setText("Please choose a date to update!");
        }
        
    }
    @FXML
    public void back_button_action(ActionEvent e) throws IOException {        
        System.out.println("going BACK to update reservation two page");
        Parent updateReserveTwo_page_parent = FXMLLoader.load(getClass().getResource("updatePageTwo.fxml"));
        Scene updateReserveTwo_page_scene = new Scene(updateReserveTwo_page_parent);
        Stage updateReserveTwo_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        updateReserveTwo_page_stage.setScene(updateReserveTwo_page_scene);
        updateReserveTwo_page_stage.show();
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        datePicker.setValue(LocalDate.now());
        
        final Callback<DatePicker, DateCell> dayCellFactory = 
            new Callback<DatePicker, DateCell>() {
                @Override
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);
                           
                            if (item.isBefore(
                                    LocalDate.now())
                                ) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                            }   
                    }
                };
            }
        };
        
        datePicker.setDayCellFactory(dayCellFactory);
        
        // TODO
        //populate current reservation
        ObservableList data = FXCollections.observableArrayList(setting.getCurrentReservation());
        tableView.setItems(data);
        
        trainCol.setCellValueFactory(new PropertyValueFactory("trainNumber"));
        timeCol.setCellValueFactory(new PropertyValueFactory("date"));
        departCol.setCellValueFactory(new PropertyValueFactory("departureStation"));
        arriveCol.setCellValueFactory(new PropertyValueFactory("arrivalStation"));
        classCol.setCellValueFactory(new PropertyValueFactory("trainClass"));
        priceCol.setCellValueFactory(new PropertyValueFactory("ticketPrice"));
        baggageNumCol.setCellValueFactory(new PropertyValueFactory("baggageNumber"));
        nameCol.setCellValueFactory(new PropertyValueFactory("passengerName"));

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        
        
    }    
    
}
