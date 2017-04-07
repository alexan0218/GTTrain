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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Jihai,Yichen
 */
public class ReservePageFourController implements Initializable {
    GlobalSetting setting = GlobalSetting.getInstance();
    ArrayList<String> cardNumbers = new ArrayList<String>();
    ArrayList<String> cardNumbersLastFour = new ArrayList<String>();
    Boolean adding = setting.getIsAdding();
    float totalCost = 0;
    final static float BAGGAGE_EXTRA_PRICE = 30;
    final static float STUDENT_DISCOUNT = 80;
    
    @FXML
    public Button button_confirmation;
    @FXML
    public Button button_back_bag_extra;
    @FXML
    public Button button_card_info;
    @FXML
    public Button button_back_search_train;
    @FXML
    public Label warning;
    @FXML
    public ChoiceBox cards;
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
    public TableColumn removedCol;
    
    @FXML
    public void confirmation_button_action(ActionEvent e) throws IOException { 
        
        if (cardNumbers.size() > 0 && setting.getLocalReservations().size() > 0) {
            
            //get all reservation IDs from DB
            ArrayList<String> resIDs = new ArrayList<String>();
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
                ResultSet st = stmt.executeQuery("SELECT r.RESERVATION_ID FROM RESERVATION r");
                
                
                while (st.next()) {
                    String resID = st.getString("RESERVATION_ID");
                    resIDs.add(resID);
                }
                
                System.out.println(resIDs);
                
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
            
            //locally generate ID and check that it's unique from DB ones
            Random rand = new Random();
            String randomID = Integer.toString(rand.nextInt((99999 - 10000) + 1) + 10000);
            while (resIDs.contains(randomID)) {
                randomID = Integer.toString(rand.nextInt((99999 - 10000) + 1) + 10000);
            }
            

            //upload reservation as a whole
            try {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_58", "cs4400_Team_58", "QxXNykgB");
                    if(!con.isClosed()){
                        System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
                    }
                    //do real work here
                    PreparedStatement stmt = con.prepareStatement("insert into RESERVATION "
                                   + " (RESERVATION_ID, IS_CANCELLED, USERNAME, CARD_NUMBER, TOTAL_COST)"
                                   + " values (?, ?, ?, ?, ?)");
                    stmt.setString(1, randomID);
                    stmt.setBoolean(2, false);
                    stmt.setString(3, setting.getUsername());
                    stmt.setString(4, LastFourToCompleteNumber((String)cards.getValue()));
                    stmt.setFloat(5, totalCost);
                    stmt.execute();
               
                
            } catch (Exception ec) {
                    System.err.println("Exception: " + ec.getMessage());
            } finally {
                    try {
                        if(con != null) {
                            con.close();
                        }
                    } catch(SQLException ec) {}
            }
            
            //upload all reservations
            for (Reservation res : setting.getLocalReservations()) {
                try {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_58", "cs4400_Team_58", "QxXNykgB");
                    if(!con.isClosed()){
                        System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
                    }
                    //do real work here
                    PreparedStatement stmt = con.prepareStatement("insert into RESERVES "
                                   + " (RESERVATION_ID, TRAIN_NUMBER, CLASS, DEPATURE_DATE, PASSENGER_NAME, NUMBER_OF_BAGGAGE, DEPARTS_FROM, ARRIVES_AT)"
                                   + " values (?, ?, ?, ?, ?, ?, ?, ?)");
                    stmt.setString(1, randomID);
                    stmt.setString(2, res.getTrainNumber());
                    stmt.setString(3, res.getTrainClass());
                    stmt.setDate(4, res.getDepartureDate());
                    stmt.setString(5, res.getPassengerName());
                    stmt.setInt(6, res.getBaggageNumber());
                    stmt.setString(7, res.getDepartureLocation());
                    stmt.setString(8, res.getArrivalLocation());
                    stmt.execute();
               
                
                } catch (Exception ec) {
                    System.err.println("Exception: " + ec.getMessage());
                } finally {
                    try {
                        if(con != null) {
                            con.close();
                        }
                    } catch(SQLException ec) {}
                }
                
            }
           
            
            if (!error) {
                setting.setCurrentReservationID(randomID);
                
                System.out.println("going to confirmation page");
                Parent reserveSix_page_parent = FXMLLoader.load(getClass().getResource("reservePageSix.fxml"));
                Scene reserveSix_page_scene = new Scene(reserveSix_page_parent);
                Stage reserveSix_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                reserveSix_page_stage.setScene(reserveSix_page_scene);
                reserveSix_page_stage.show();
            } else {
                warning.setText("DB error occured.");
            }
            
        } else {
            if (setting.getLocalReservations().size() <= 0) {
                warning.setText("No reservations available. Please add new reservation again.");
            } else if (cardNumbers.size() == 0) {
                warning.setText("Please add a credit/debit card to continue!");
            } else {
                warning.setText("Unknown error occured.");
            }
        }
        
        
        
    }
    @FXML
    public void back_bag_extra_button_action(ActionEvent e) throws IOException {  
        setting.removeRecentlyAddedReservation();
        System.out.println(setting.getLocalReservations());
        System.out.println("going BACK to search train page");
        Parent reserveThree_page_parent = FXMLLoader.load(getClass().getResource("reservePageThree.fxml"));
        Scene reserveThree_page_scene = new Scene(reserveThree_page_parent);
        Stage reserveThree_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        reserveThree_page_stage.setScene(reserveThree_page_scene);
        reserveThree_page_stage.show();
    }
    @FXML
    public void card_info_button_action(ActionEvent e) throws IOException {  
        System.out.println("going to card info page");
        Parent reserveFive_page_parent = FXMLLoader.load(getClass().getResource("reservePageFive.fxml"));
        Scene reserveFive_page_scene = new Scene(reserveFive_page_parent);
        Stage reserveFive_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        reserveFive_page_stage.setScene(reserveFive_page_scene);
        reserveFive_page_stage.show();
    }
    @FXML
    public void back_search_train_button_action(ActionEvent e) throws IOException { 
        setting.setIsAdding(true);
        System.out.println("going to search train page");
        Parent reserveOne_page_parent = FXMLLoader.load(getClass().getResource("reservePageOne.fxml"));
        Scene reserveOne_page_scene = new Scene(reserveOne_page_parent);
        Stage reserveOne_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        reserveOne_page_stage.setScene(reserveOne_page_scene);
        reserveOne_page_stage.show();
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // TODO
        System.out.println(setting.getLocalReservations());
        //populate the tableview
        ObservableList data = FXCollections.observableList(setting.getLocalReservations());
        tableView.setItems(data);
        trainCol.setCellValueFactory(new PropertyValueFactory("trainNumber"));
        timeCol.setCellValueFactory(new PropertyValueFactory("duration"));
        departCol.setCellValueFactory(new PropertyValueFactory("departureStation"));
        arriveCol.setCellValueFactory(new PropertyValueFactory("arrivalStation"));
        classCol.setCellValueFactory(new PropertyValueFactory("trainClass"));
        priceCol.setCellValueFactory(new PropertyValueFactory("ticketPrice"));
        baggageNumCol.setCellValueFactory(new PropertyValueFactory("baggageNumber"));
        nameCol.setCellValueFactory(new PropertyValueFactory("passengerName"));
        removedCol.setCellValueFactory(new PropertyValueFactory("functionLabel"));

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableView.getSelectionModel().setCellSelectionEnabled(true);
        final ObservableList<TablePosition> selectedCells = tableView.getSelectionModel().getSelectedCells();
        selectedCells.addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(ListChangeListener.Change change) {
                for (TablePosition pos : selectedCells) {
                    if (pos.getTableColumn().getText().equals("Remove")) {
                        //deal with remove
                        setting.removeReservationAt(pos.getRow());
                        ObservableList data = FXCollections.observableList(setting.getLocalReservations());
                        tableView.setItems(data);
                        
                        calculateCost();
                        
                    }else {
                        //inform user to click remove to delete certain reservation
                        warning.setText("Click remove button to remove one reservation.");
                    }
                    
                    
                    System.out.println("Cell content: "+pos.getTableColumn().getCellData(pos.getRow()));
                }
            };
        });
        
        
        //get card information
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
                ResultSet st = stmt.executeQuery("SELECT p.CARD_NUMBER FROM PAYMENTINFO p WHERE p.USERNAME = '" + setting.getUsername() +"'");
                
                
                while (st.next()) {
                    String cardNumber = st.getString("CARD_NUMBER");
                    cardNumbers.add(cardNumber);
                    cardNumbersLastFour.add(cardNumber.substring(cardNumber.length() - 4, cardNumber.length()));
                }
                
                System.out.println(cardNumbers);
                if (cardNumbers.size() == 0) {
                    warning.setText("Please add a credit/debit card to continue!");
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
            if (cardNumbers.size() == 0) {
                cards.setItems(FXCollections.observableArrayList("no card available"));
                cards.setValue("no card available");
            } else {
                cards.setItems(FXCollections.observableArrayList(cardNumbersLastFour));
                cards.setValue(cardNumbersLastFour.get(0));
            }
        }
        
        calculateCost();
        
    } 
    
    private void calculateCost() {
        //calculate cost
        //ticket price and baggage price
        totalCost = 0;
        for (Reservation r : setting.getLocalReservations()) {
            totalCost += r.getTicketPrice();
            System.out.println(totalCost);
            int baggageNum = r.getBaggageNumber();
            if (baggageNum > 2) {
                totalCost += (baggageNum - 2) * BAGGAGE_EXTRA_PRICE;
            }
            System.out.println(baggageNum);
            System.out.println(totalCost);
        }
        //discount
        if (setting.isStudent()) {
            totalCost *= STUDENT_DISCOUNT / 100;
        }
        System.out.println(totalCost);
        totalCostLabel.setText(Float.toString(totalCost));
    }
    
    private String LastFourToCompleteNumber(String lastFour) {
        String result = null;
        for (String number : cardNumbers) {
            if (number.substring(number.length() - 4, number.length()).equals(lastFour)) {
                return number;
            }
        }
        return result;
    }
    
}
