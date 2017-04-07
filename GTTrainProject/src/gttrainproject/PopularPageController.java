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
import java.time.LocalDate;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author YICHEN
 */
public class PopularPageController implements Initializable {
    ArrayList<PopularPageReport> popular = new ArrayList<PopularPageReport>();
    @FXML
    private Button back;
    @FXML
    private TableView table;
    @FXML
    private TableColumn monthCol;
    @FXML
    private TableColumn trainNumCol;
    @FXML
    private TableColumn NumOfReserveCol;
    @FXML
    private Label warning;
    
    
    @FXML
    public void back_button_action(ActionEvent e) throws IOException {        
        System.out.println("back to manager choose page");
        Parent mChoose_page_parent = FXMLLoader.load(getClass().getResource("managerChoosePage.fxml"));
        Scene mChoose_page_scene = new Scene(mChoose_page_parent);
        Stage mChoose_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        mChoose_page_stage.setScene(mChoose_page_scene);
        mChoose_page_stage.show();
    }
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        int thisMonth = LocalDate.now().getMonthValue();
        System.out.println("popular route reports for top 3 most popular train");
        Connection con = null;
        try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_58", "cs4400_Team_58", "QxXNykgB");
                if(!con.isClosed()){
                    System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
                }
                
                //get all stations
                for (int i = 0; i < 3; i++) {
                    thisMonth -= 1;
                    Statement stmt = con.createStatement();
                    ResultSet st = stmt.executeQuery("SELECT M, TRAIN_NUMBER, NumReservation "
                                                    + "FROM (SELECT M, TRAIN_NUMBER, COUNT(*) AS NumReservation " 
                                                          + "FROM (SELECT month(DEPATURE_DATE) AS M, TRAIN_NUMBER "
                                                                + "FROM (RESERVATION NATURAL JOIN RESERVES) "
                                                                + "WHERE IS_CANCELLED = 0) AS K GROUP BY M, TRAIN_NUMBER "
                                                    + "ORDER BY M , NumReservation DESC ) AS ResCount "
                                                    + "WHERE M = " + thisMonth
                                                    + " LIMIT 3");
                    while (st.next()) {
                        int m = st.getInt("M");
                        String t = st.getString("TRAIN_NUMBER");
                        int n = st.getInt("NumReservation");
                        popular.add(new PopularPageReport(m, t, n));
                    }
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
        ObservableList data = FXCollections.observableList(popular);
        table.setItems(data);
        monthCol.setCellValueFactory(new PropertyValueFactory("month"));
        trainNumCol.setCellValueFactory(new PropertyValueFactory("trainNum"));
        NumOfReserveCol.setCellValueFactory(new PropertyValueFactory("numOfReserve"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //table.getSelectionModel().setCellSelectionEnabled(true);
                 
    
}
}
