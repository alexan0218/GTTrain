/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gttrainproject;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Jihai,Yichen
 */
public class ReservePageOneController implements Initializable {
    GlobalSetting setting = GlobalSetting.getInstance();
    Boolean adding = setting.getIsAdding();
    @FXML
    public Button button_select_departure;
    @FXML
    public Button back;
    @FXML
    public ChoiceBox<String> departureStations;
    @FXML
    public ChoiceBox<String> arrivalStations;
    @FXML
    public DatePicker datePicker = new DatePicker();
    @FXML
    public Label warning;
    
    @FXML
    public void back_button_action(ActionEvent e) throws IOException {
        
        if (!adding) {
            setting.setIsAdding(false);
            System.out.println("logging in as a customer");
            Parent cChoose_page_parent = FXMLLoader.load(getClass().getResource("customerChoosePage.fxml"));
            Scene cChoose_page_scene = new Scene(cChoose_page_parent);
            Stage cChoose_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            cChoose_page_stage.setScene(cChoose_page_scene);
            cChoose_page_stage.show();
        } else {
            setting.setIsAdding(false);
            System.out.println("logging in as a customer");
            Parent rp_page_parent = FXMLLoader.load(getClass().getResource("reservePageFour.fxml"));
            Scene rp_page_scene = new Scene(rp_page_parent);
            Stage rp_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            rp_page_stage.setScene(rp_page_scene);
            rp_page_stage.show();
        }
    }
    
    @FXML
    public void select_departure_button_action(ActionEvent e) throws IOException { 
        LocalDate ld = datePicker.getValue();
        Instant instant = ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        java.util.Date date = Date.from(instant);
        if (departureStations.getValue() != arrivalStations.getValue()) {
            Reservation res = new Reservation(departureStations.getValue().substring(departureStations.getValue().indexOf("(")+1,departureStations.getValue().indexOf(")")), arrivalStations.getValue().substring(arrivalStations.getValue().indexOf("(")+1,arrivalStations.getValue().indexOf(")")), new java.sql.Date(date.getTime()));
            res.setDepartureLocation(departureStations.getValue().substring(0, departureStations.getValue().indexOf("(")));
            res.setArrivalLocation(arrivalStations.getValue().substring(0, arrivalStations.getValue().indexOf("(")));
            
            setting.setCurrentReservation(res);

            System.out.println(res.getDepartureDate());
            System.out.println("going to Select Departure page");
            Parent reserveTwo_page_parent = FXMLLoader.load(getClass().getResource("reservePageTwo.fxml"));
            Scene reserveTwo_page_scene = new Scene(reserveTwo_page_parent);
            Stage reserveTwo_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            reserveTwo_page_stage.setScene(reserveTwo_page_scene);
            reserveTwo_page_stage.show();
        } else {
            warning.setText("Departure station and Arrival station should be different.");
        }
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
        Connection con = null;
        Boolean error = false;
        ArrayList<String> stations = new ArrayList<String>();
        try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_58", "cs4400_Team_58", "QxXNykgB");
                if(!con.isClosed()){
                    System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
                }
                
                //get all stations
                Statement stmt = con.createStatement();
                ResultSet st = stmt.executeQuery("SELECT * FROM STATION");
                
                
                while (st.next()) {
                    String stationName = st.getString("NAME");
                    String stationLocation = st.getString("LOCATION");
                    stations.add(stationLocation + "(" + stationName + ")");
                }
                
                System.out.println(stations);
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
            departureStations.setItems(FXCollections.observableArrayList(stations));
            departureStations.setValue(stations.get(0));
            arrivalStations.setItems(FXCollections.observableArrayList(stations));
            arrivalStations.setValue(stations.get(stations.size() - 1));
        }
        
    }    
    
}
