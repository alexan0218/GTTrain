/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gttrainproject;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Jihai,Yichen
 */
public class UpdatePageTwoController implements Initializable {
    GlobalSetting setting = GlobalSetting.getInstance();
    ArrayList<Reservation> reservations = new ArrayList<Reservation>();
    int selectedIndex = -1;
    
    @FXML
    public Button button_update_reservation_three;
    @FXML
    public Button button_back;
    @FXML
    public Label warning;
    @FXML
    public TableView tableView = new TableView();
    
    @FXML
    public TableColumn selectCol;
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
    public void update_reservation_three_button_action(ActionEvent e) throws IOException {  
        if (selectedIndex < 0) {
            //inform user to click remove to delete certain reservation
            warning.setText("Click select button to select one reservation.");
        } else {
            setting.setCurrentReservation(reservations.get(selectedIndex));
            
            System.out.println("going to update reservaation three page");
            Parent updateReserveThree_page_parent = FXMLLoader.load(getClass().getResource("updatePageThree.fxml"));
            Scene updateReserveThree_page_scene = new Scene(updateReserveThree_page_parent);
            Stage updateReserveThree_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            updateReserveThree_page_stage.setScene(updateReserveThree_page_scene);
            updateReserveThree_page_stage.show();
        }
        
        
    }
    @FXML
    public void back_button_action(ActionEvent e) throws IOException {        
        System.out.println("going BACK to functionality page");
        Parent updateReserveOne_page_parent = FXMLLoader.load(getClass().getResource("updatePageOne.fxml"));
        Scene updateReserveOne_page_scene = new Scene(updateReserveOne_page_parent);
        Stage updateReserveOne_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        updateReserveOne_page_stage.setScene(updateReserveOne_page_scene);
        updateReserveOne_page_stage.show();
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //get all reservations
        String resID = setting.getCurrentReservationID();
        Connection con = null;
        Boolean error = false;
        try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_58", "cs4400_Team_58", "QxXNykgB");
                if(!con.isClosed()){
                    System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
                }
                
                //get all stations
                Statement stmt = con.createStatement();
                ResultSet st = stmt.executeQuery("SELECT * FROM RESERVES WHERE RESERVATION_ID = '" + resID +"'");
                
                
                while (st.next()) {
                    Reservation r = new Reservation(st.getString("DEPARTS_FROM"), st.getString("ARRIVES_AT"), st.getDate("DEPATURE_DATE"));
                    r.setTrainNumber(st.getString("TRAIN_NUMBER"));
                    r.setClass(st.getString("CLASS"));
                    r.setPassengerName(st.getString("PASSENGER_NAME"));
                    r.setBaggageNumber(st.getInt("NUMBER_OF_BAGGAGE"));
                    r.setFunctionLabel("select");
                    //get ticket price
                    //get corresponding prices
                    Statement stmt2 = con.createStatement();
                    ResultSet st2 = stmt2.executeQuery("SELECT t1.1ST_CLASS_PRICE, t1.2ND_CLASS_PRICE " +
                        "FROM TRAINROUTE t1 " +
                        "WHERE t1.TRAIN_NUMBER = '" + r.getTrainNumber() + "' ");
                    
                    if (st2.next()) {
                        if (r.getClass().equals("First")) {
                            r.setTicketPrice(st2.getFloat("1ST_CLASS_PRICE"));
                        } else {
                            r.setTicketPrice(st2.getFloat("2ND_CLASS_PRICE"));
                        }
                    }
                    
                    reservations.add(r);
                }
                
                System.out.println(reservations);
                if (reservations.size() == 0) {
                    warning.setText("No reservation record for this id!");
                }
                
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
            //populate the tableView
            ObservableList data = FXCollections.observableList(reservations);
            tableView.setItems(data);
        
            selectCol.setCellValueFactory(new PropertyValueFactory("functionLabel"));
            trainCol.setCellValueFactory(new PropertyValueFactory("trainNumber"));
            timeCol.setCellValueFactory(new PropertyValueFactory("date"));
            departCol.setCellValueFactory(new PropertyValueFactory("departureStation"));
            arriveCol.setCellValueFactory(new PropertyValueFactory("arrivalStation"));
            classCol.setCellValueFactory(new PropertyValueFactory("trainClass"));
            priceCol.setCellValueFactory(new PropertyValueFactory("ticketPrice"));
            baggageNumCol.setCellValueFactory(new PropertyValueFactory("baggageNumber"));
            nameCol.setCellValueFactory(new PropertyValueFactory("passengerName"));

            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            tableView.getSelectionModel().setCellSelectionEnabled(true);
            final ObservableList<TablePosition> selectedCells = tableView.getSelectionModel().getSelectedCells();
            selectedCells.addListener(new ListChangeListener<TablePosition>() {
                @Override
                public void onChanged(ListChangeListener.Change change) {
                    for (TablePosition pos : selectedCells) {
                        if (pos.getTableColumn().getText().equals("Select")) {
                            //deal with remove
                            selectedIndex = pos.getRow();
                            System.out.println("selected " + selectedIndex);
                            warning.setText("");
                        }else {
                            //inform user to click remove to delete certain reservation
                            warning.setText("Click select button to select one reservation.");
                        }
                    
                    
                        System.out.println("Cell content: "+pos.getTableColumn().getCellData(pos.getRow()));
                    }
                };
            });
        }
        
    }    
    
}
