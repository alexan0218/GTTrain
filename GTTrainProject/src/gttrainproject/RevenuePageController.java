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
public class RevenuePageController implements Initializable {
    ArrayList<RevenueReport> revenues = new ArrayList<RevenueReport>();
    @FXML
    private Button back;
    @FXML
    private TableView table;
    @FXML
    private TableColumn monthCol;
    @FXML
    private TableColumn revenueCol;
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
        System.out.println("revenue reports for previous three months");
        Connection con = null;
        try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_58", "cs4400_Team_58", "QxXNykgB");
                if(!con.isClosed()){
                    System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
                }
                
                //get all stations
                Statement stmt = con.createStatement();
                ResultSet st = stmt.executeQuery("SELECT M, SUM(TOTAL) AS TOTAL FROM (SELECT month(DEPATURE_DATE) AS M, SUM(TOTAL_COST) AS TOTAL FROM (RESERVATION NATURAL JOIN RESERVES AS K) GROUP BY RESERVATION_ID, M) AS L GROUP BY M LIMIT 3");
                while (st.next()) {
                    String m = st.getString("M");
                    String r = st.getString("TOTAL");
                    revenues.add(new RevenueReport(m, r));
                    System.out.println(m + "================="  + r );
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
        ObservableList data = FXCollections.observableList(revenues);
        table.setItems(data);
        monthCol.setCellValueFactory(new PropertyValueFactory("month"));
        revenueCol.setCellValueFactory(new PropertyValueFactory("revenue"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //table.getSelectionModel().setCellSelectionEnabled(true);
        
    }    
  }    
   
