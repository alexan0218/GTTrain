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
import java.sql.Time;
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
public class ReservePageTwoController implements Initializable {
    GlobalSetting setting = GlobalSetting.getInstance();
    float selectedPrice = 0;
    Boolean priceChosen = false;
    String selectedTrainNumber = null;
    String selectedClass = null;
    String selectedDuration = null;
    ArrayList<departureSelection> selections = new ArrayList<departureSelection>();
    
    @FXML
    public Button button_bag_extra;
    @FXML
    public Button button_back_searchTrain;
    @FXML
    public TableView tableView = new TableView();
    @FXML
    public TableColumn trainCol;
    @FXML
    public TableColumn timeCol;
    @FXML
    public TableColumn firstClassCol;
    @FXML
    public TableColumn secondClassCol;
    @FXML
    public Label warning;
    
    
    @FXML
    public void bag_extra_button_action(ActionEvent e) throws IOException { 
        if (selectedPrice != 0 && priceChosen && selectedTrainNumber != null && selectedClass != null && selections.size() > 0) {
            Reservation res = setting.getCurrentReservation();
            res.setTrainNumber(selectedTrainNumber);
            res.setClass(selectedClass);
            res.setTicketPrice(selectedPrice);
            res.setDuration(selectedDuration);
            setting.setCurrentReservation(res);
            
            System.out.println(res);
            
            System.out.println("going to bag & extra page");
            Parent reserveThree_page_parent = FXMLLoader.load(getClass().getResource("reservePageThree.fxml"));
            Scene reserveThree_page_scene = new Scene(reserveThree_page_parent);
            Stage reserveThree_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            reserveThree_page_stage.setScene(reserveThree_page_scene);
            reserveThree_page_stage.show();
        }else {
            //inform user of error occured or price not chosen properly
            if (selections.size() == 0) {
                //change to warning label afterwards
                warning.setText("no route found. Please head back and search other routes.");
                //System.out.println("no route found. Please head back and search other routes.");
            } else if (selectedTrainNumber == null || selectedClass == null || !priceChosen) {
                //change to warning label afterwards
                warning.setText("please choose a price to continue");
                //System.out.println("please choose a price to continue");
            } else {
                //change to warning label afterwards
                warning.setText("connect error occured!");
                //System.out.println("connect error occured!");
            }
            
            
        }
        
        
        
        
    }
    @FXML
    public void back_searchTrain_button_action(ActionEvent e) throws IOException {  
        
        System.out.println("going BACK to search train page");
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
        
        
        Reservation res = setting.getCurrentReservation();
        String departureStation = res.getDepartureStation();
        String arrivalStation = res.getArrivalStation();
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
                
                ResultSet st = stmt.executeQuery("SELECT s1.TRAIN_NUMBER, s1.DEPARTURE_TIME, s2.ARRIVAL_TIME " +
                        "FROM STOP s1 JOIN STOP s2 " +
                        "ON s1.TRAIN_NUMBER = s2.TRAIN_NUMBER " +
                        "AND s1.NAME = '" + departureStation + "' " +
                        "AND s2.NAME = '" + arrivalStation + "' ");
                System.out.println(departureStation + ", " + arrivalStation + ", ");
                
                while (st.next()) {
                    String trainNumber = st.getString("TRAIN_NUMBER");
                    Time departTime = st.getTime("DEPARTURE_TIME");
                    Time arrivalTime = st.getTime("ARRIVAL_TIME");
                    System.out.println(trainNumber + ", " + departTime + ", " + arrivalTime);
                    
                    //make sure it's not destination or starting station
                    if (departTime != null && arrivalTime != null) {
                        //get corresponding prices
                        Statement stmt2 = con.createStatement();
                        ResultSet st2 = stmt2.executeQuery("SELECT t1.1ST_CLASS_PRICE, t1.2ND_CLASS_PRICE " +
                            "FROM STOP s1 JOIN TRAINROUTE t1 " +
                            "ON t1.TRAIN_NUMBER = '" + trainNumber + "' ");
                    
                        if (st2.next()) {
                            float firstClassPrice = st2.getFloat("1ST_CLASS_PRICE");
                            float secondClassPrice = st2.getFloat("2ND_CLASS_PRICE");
                            selections.add(new departureSelection(trainNumber, 
                                departTime.toString() + " to " + arrivalTime.toString(), 
                                firstClassPrice, secondClassPrice));
                        }
                    }
                    
                    
                }
                
                if (selections.size() == 0) {
                    //change to warning label afterwards
                    System.out.println("no route found. Please head back and search other routes.");
                }
                
                System.out.println(selections);
        } catch (Exception ec) {
                System.err.println("Exception: Here !!" + ec.getMessage());
                error = true;
        } finally {
                try {
                    if(con != null) {
                        con.close();
                    }
                } catch(SQLException ec) {}
        }
        
        
        //populate the tableview
        ObservableList data = FXCollections.observableList(selections);
        tableView.setItems(data);
        trainCol.setCellValueFactory(new PropertyValueFactory("trainNumber"));
        timeCol.setCellValueFactory(new PropertyValueFactory("duration"));
        firstClassCol.setCellValueFactory(new PropertyValueFactory("firstClassPrice"));
        secondClassCol.setCellValueFactory(new PropertyValueFactory("secondClassPrice"));

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableView.getSelectionModel().setCellSelectionEnabled(true);
        final ObservableList<TablePosition> selectedCells = tableView.getSelectionModel().getSelectedCells();
        selectedCells.addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(Change change) {
                for (TablePosition pos : selectedCells) {
                    if (pos.getTableColumn().getText().equals("1st Class Price") ||
                            pos.getTableColumn().getText().equals("2nd Class Price")) {
                        priceChosen = true;
                        selectedPrice = (float) pos.getTableColumn().getCellData(pos.getRow());
                        if (pos.getTableColumn().getText().equals("1st Class Price")) {
                            selectedClass = "First";
                        } else {
                            selectedClass = "Second";
                        }
                        selectedTrainNumber = (String) trainCol.getCellData(pos.getRow());
                        selectedDuration = (String) timeCol.getCellData(pos.getRow());
                    }else {
                        priceChosen = false;
                        
                        //inform user to choose a price instead of something else
                    }
                    
                    
                    System.out.println("Cell content: "+pos.getTableColumn().getCellData(pos.getRow()));
                }
            };
        });
        
        
    }    
    
    
    
}
