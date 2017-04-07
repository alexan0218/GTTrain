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
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import static java.time.temporal.ChronoUnit.DAYS;


/**
 * FXML Controller class
 *
 * @author Jihai
 */
public class CancelPageTwoController implements Initializable {
    GlobalSetting setting = GlobalSetting.getInstance();
    //String price;
    LocalDate now = LocalDate.now();
    LocalDate earliestDate;
    Boolean isFirst = true;
    Double refundMoney = 0.0;
    Double totalcost = 0.0;
    Double newcost = 0.0;
    
    @FXML
    private Button back_on_page_two;
    @FXML
    private Button submit;
    @FXML
    private Label warning;
    @FXML 
    private TableView table;
    @FXML
    private TableColumn trainCol;
    @FXML
    private TableColumn departDateCol;
    @FXML
    private TableColumn departStationCol;
    @FXML
    private TableColumn arrivalStationCol;
    @FXML
    private TableColumn classCol;
    @FXML
    private TableColumn bagCol;
    @FXML
    private TableColumn nameCol;
    @FXML
    private Label totalCost;
    @FXML
    private Label cancelDate;
    @FXML
    private Label refund;
    
    
    @FXML
    public void submit_button_action(ActionEvent e) throws IOException {
        //do sth
        if (submit.getText().equals("Backing to functionality")){
            System.out.println("back to customer choose page");
            Parent cChoose_page_parent = FXMLLoader.load(getClass().getResource("customerChoosePage.fxml"));
            Scene cChoose_page_scene = new Scene(cChoose_page_parent);
            Stage cChoose_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            cChoose_page_stage.setScene(cChoose_page_scene);
            cChoose_page_stage.show();
        } 
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_58", "cs4400_Team_58", "QxXNykgB");
            if(!con.isClosed()){
                System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
            }

            //get all reservations
            PreparedStatement stmt2 = con.prepareStatement("UPDATE RESERVATION SET TOTAL_COST = " + newcost + " WHERE RESERVATION_ID = '" + setting.getCurrentReservationID() + "'");
            stmt2.execute();
            System.out.println(newcost + "===============================================================================================");
            PreparedStatement stmt = con.prepareStatement("UPDATE RESERVATION SET IS_CANCELLED = 1 WHERE RESERVATION_ID = '" + setting.getCurrentReservationID() + "'");
            stmt.execute();
           
            Parent cChoose_page_parent = FXMLLoader.load(getClass().getResource("customerChoosePage.fxml"));
            Scene cChoose_page_scene = new Scene(cChoose_page_parent);
            Stage cChoose_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            cChoose_page_stage.setScene(cChoose_page_scene);
            cChoose_page_stage.show();
        } catch (Exception ec) {
               System.out.println(ec.getMessage());

        } finally {
                try {
                    if(con != null) {
                        con.close();
                    }
                } catch(SQLException ec) {}
        }
    }
    
    @FXML
    public void backOnPageTwo_button_action(ActionEvent e) throws IOException {
        System.out.println("back to previous page");
        Parent cancelPageOne_page_parent = FXMLLoader.load(getClass().getResource("cancelPageOne.fxml"));
        Scene cancelPageOne_page_scene = new Scene(cancelPageOne_page_parent);
        Stage cancelPageOne_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        cancelPageOne_page_stage.setScene(cancelPageOne_page_scene);
        cancelPageOne_page_stage.show();
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Connection con = null;
        ArrayList<Reservation> reservations = new ArrayList<>();
        
        try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_58", "cs4400_Team_58", "QxXNykgB");
                if(!con.isClosed()){
                    System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
                }
                
                //get all stations
                Statement stmt = con.createStatement();
                ResultSet st = stmt.executeQuery("SELECT * FROM RESERVES WHERE RESERVATION_ID = '" + setting.getCurrentReservationID() + "'" + " ORDER BY DEPATURE_DATE");
                while (st.next()) {
                    
                    String trainNum = st.getString("TRAIN_NUMBER");
                    String cls = st.getString("CLASS");
                    java.sql.Date departDate = st.getDate("DEPATURE_DATE");
                    if (isFirst) {
                        earliestDate = departDate.toLocalDate();
                        isFirst = false;
                    }
                    String passengerName = st.getString("PASSENGER_NAME");
                    int bagNum = st.getInt("NUMBER_OF_BAGGAGE");
                    String departfrom = st.getString("DEPARTS_FROM");
                    String arrivesat = st.getString("ARRIVES_AT");
                    
                    Reservation cursor = new Reservation(departfrom, arrivesat, departDate);
                    cursor.setTrainNumber(trainNum);
                    cursor.setClass(cls);
                    cursor.setPassengerName(passengerName);
                    cursor.setBaggageNumber(bagNum);
                    
                    System.out.println(cursor.toString());
                    System.out.println(cursor.getDepartureStation());
                    //TODO:sort
                    reservations.add(cursor);
                }
                ResultSet st2 = stmt.executeQuery("SELECT TOTAL_COST FROM RESERVATION WHERE RESERVATION_ID = '" + setting.getCurrentReservationID() + "'");
                while(st2.next()) {
                   totalcost = st2.getDouble("TOTAL_COST");
                }
        } catch (Exception ec) {
                System.err.println("Exception: " + ec.getMessage());
        } finally {
                try {
                    if(con != null) {
                        con.close();
                    }
                } catch(SQLException ec) {}
        }
        //populate the tableview
        ObservableList data = FXCollections.observableList(reservations);
        table.setItems(data);
        trainCol.setCellValueFactory(new PropertyValueFactory("trainNumber"));
        departDateCol.setCellValueFactory(new PropertyValueFactory("date"));
        departStationCol.setCellValueFactory(new PropertyValueFactory("departureStation"));
        arrivalStationCol.setCellValueFactory(new PropertyValueFactory("arrivalStation"));
        classCol.setCellValueFactory(new PropertyValueFactory("trainClass"));
        bagCol.setCellValueFactory(new PropertyValueFactory("baggageNumber"));
        nameCol.setCellValueFactory(new PropertyValueFactory("passengerName"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        totalCost.setText(totalcost.toString());
        cancelDate.setText(now.toString());
        //refund 
        int daysBetween = (int) DAYS.between(now, earliestDate); 
        if (daysBetween > 7) {
            refundMoney = totalcost * 0.8 - 50;
            if (refundMoney >= 0) {
                refund.setText(refundMoney.toString());
                
            } else {
                refundMoney = 0.0;
                refund.setText(refundMoney.toString());
            }
            newcost = totalcost - refundMoney;
            //update
        } else if (daysBetween >= 1) {
            refundMoney = totalcost * 0.5 - 50;
            if (refundMoney >= 0) {
                refund.setText(refundMoney.toString());
            } else {
                refundMoney = 0.0;
                refund.setText(refundMoney.toString());
            }
            newcost = totalcost - refundMoney;
            //update
        } else {
            warning.setText("You can not cancel this reservation.");
            submit.setText("Backing to functionality");
        }
    }    

    
    
}
