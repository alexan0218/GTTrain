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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Jihai
 */
public class ViewTrainPageTwoController implements Initializable {
    GlobalSetting setting = GlobalSetting.getInstance();
    ArrayList<Trainroute> stops = new ArrayList<Trainroute>();
    
    @FXML
    private Button back_to_page_One;
    @FXML
    private TableView table;
    @FXML
    private TableColumn trainCol;
    @FXML
    private TableColumn depCol;
    @FXML
    private TableColumn arrCol;
    @FXML
    private TableColumn stationCol;
    
    @FXML
    public void backToPageOne_button_action(ActionEvent e) throws IOException {
        System.out.println("logging in as a customer");
        Parent viewTrainPageOne_page_parent = FXMLLoader.load(getClass().getResource("viewTrainPageOne.fxml"));
        Scene viewTrainPageOne_page_scene = new Scene(viewTrainPageOne_page_parent);
        Stage viewTrainPageOne_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        viewTrainPageOne_page_stage.setScene(viewTrainPageOne_page_scene);
        viewTrainPageOne_page_stage.show();
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Connection con = null;
        try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_58", "cs4400_Team_58", "QxXNykgB");
                if(!con.isClosed()){
                    System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
                }
                
                //get all stations
                Statement stmt = con.createStatement();
                ResultSet st = stmt.executeQuery("SELECT * FROM STOP WHERE TRAIN_NUMBER = '" + setting.getReviewingTrainNum() + "'" + "ORDER BY ARRIVAL_TIME, DEPARTURE_TIME" + "");
                while (st.next()) {
                    String trainNum = st.getString("TRAIN_NUMBER");
                    String arrivalTime = " ";
                    String departTime = " ";
                    String station = st.getString("NAME");
                    
                    try {
                        Time arrT = st.getTime("ARRIVAL_TIME");
                        arrivalTime = st.getTime("ARRIVAL_TIME").toString();
                    } catch (NullPointerException e) {
                        arrivalTime = " ";
                    }
                    try {
                        Time depT = st.getTime("DEPARTURE_TIME");
                        departTime = st.getTime("DEPARTURE_TIME").toString();
                    } catch (NullPointerException e) {
                        departTime = " ";
                    }
                    
                    
                    Trainroute thisStop = new Trainroute(trainNum, arrivalTime, departTime, station);
                    
                    
                    //TODO:sort
                    stops.add(thisStop);
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
        ObservableList data = FXCollections.observableList(stops);
        table.setItems(data);
        trainCol.setCellValueFactory(new PropertyValueFactory<Trainroute, String>("trainNum"));
        arrCol.setCellValueFactory(new PropertyValueFactory<Trainroute, String>("arrivalTime"));
        depCol.setCellValueFactory(new PropertyValueFactory<Trainroute, String>("departTime"));
        stationCol.setCellValueFactory(new PropertyValueFactory<Trainroute, String>("station"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        
        
    }
}

