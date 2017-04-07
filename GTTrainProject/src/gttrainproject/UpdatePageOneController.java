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
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Jihai,Yichen
 */
public class UpdatePageOneController implements Initializable {
    GlobalSetting setting = GlobalSetting.getInstance();
    Boolean isCanceled = false;
    @FXML
    public Button button_update_reservation_two;
    @FXML
    public Button button_back;
    @FXML
    public Label warning;
    @FXML
    public TextField resIDLabel;
    
    @FXML
    public void update_reservation_two_button_action(ActionEvent e) throws IOException { 
        
        //check if reservation id is valid first
        Connection con = null;
        Boolean error = false;
        Boolean nameMatched = false;
        try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_58", "cs4400_Team_58", "QxXNykgB");
                if(!con.isClosed()){
                    System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
                }
                
                //get all stations
                Statement stmt = con.createStatement();
                ResultSet st = stmt.executeQuery("SELECT USERNAME, TOTAL_COST, IS_CANCELLED FROM RESERVATION WHERE RESERVATION_ID = '" + resIDLabel.getText() +"'");
                
                
                if (st.next()) {
                    if (st.getString("USERNAME").equals(setting.getUsername())) {
                        nameMatched = true;
                        setting.setCurrentTotalCost(st.getFloat("TOTAL_COST"));
                        isCanceled = st.getBoolean("IS_CANCELLED");
                        if (isCanceled) {
                            warning.setText("Reservation has been canceled.");
                        }
                    } else {
                        //not matching username
                        warning.setText("The username doesn't match yours.");
                    }
                } else {
                    //no matching ID
                    warning.setText("No matching reservation found.");
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
        
        if (!error && nameMatched && !isCanceled) {
            setting.setCurrentReservationID(resIDLabel.getText());
            System.out.println("going to update reservaation two page");
            Parent updateReserveTwo_page_parent = FXMLLoader.load(getClass().getResource("updatePageTwo.fxml"));
            Scene updateReserveTwo_page_scene = new Scene(updateReserveTwo_page_parent);
            Stage updateReserveTwo_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            updateReserveTwo_page_stage.setScene(updateReserveTwo_page_scene);
            updateReserveTwo_page_stage.show();
        }
        
    }
    @FXML
    public void back_button_action(ActionEvent e) throws IOException {        
        System.out.println("going BACK to functionality page");
        Parent cChoose_page_parent = FXMLLoader.load(getClass().getResource("customerChoosePage.fxml"));
        Scene cChoose_page_scene = new Scene(cChoose_page_parent);
        Stage cChoose_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        cChoose_page_stage.setScene(cChoose_page_scene);
        cChoose_page_stage.show();
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
