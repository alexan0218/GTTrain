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
 * @author Jihai
 */
public class CancelPageOneController implements Initializable {

    GlobalSetting setting = GlobalSetting.getInstance();
    
    @FXML
    public Button back_on_page_one;   
    @FXML
    public Button search;
    @FXML
    public TextField reservationID;
    @FXML
    public Label warning;
    
    @FXML
    public void search_button_action(ActionEvent e) throws IOException {
        
        if (!reservationID.getText().equals("")) {
            Connection con = null;
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_58", "cs4400_Team_58", "QxXNykgB");
                if(!con.isClosed()){
                    System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
                }

                //get all reservations
                Statement stmt = con.createStatement();
                ResultSet st = stmt.executeQuery("SELECT RESERVATION_ID, IS_CANCELLED, USERNAME from RESERVATION");
                ArrayList<String> reservations = new ArrayList<String>();
                while (st.next()) {
                    String n = st.getString("RESERVATION_ID");
                    if (st.getInt("IS_CANCELLED") != 1  && st.getString("USERNAME").equals(setting.getUsername())) {
                        reservations.add(n);
                    }
                }

                if (reservations.contains(reservationID.getText())) {
                    setting.setCurrentReservationID(reservationID.getText());
                    System.out.println("going to train search page");
                    Parent cancelPageTwo_page_parent = FXMLLoader.load(getClass().getResource("cancelPageTwo.fxml"));
                    Scene cancelPageTwo_page_scene = new Scene(cancelPageTwo_page_parent);
                    Stage cancelPageTwo_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    cancelPageTwo_page_stage.setScene(cancelPageTwo_page_scene);
                    cancelPageTwo_page_stage.show();
                } else {
                    warning.setText("Cannot find the reservation.");
                }
            } catch (Exception ec) {
                   System.out.println(ec.getMessage());

            } finally {
                    try {
                        if(con != null) {
                            con.close();
                        }
                    } catch(SQLException ec) {}
            }
        } else {
            warning.setText("Please input a reservation ID");
        }
        
    }
    
    @FXML
    public void back_button_action(ActionEvent e) throws IOException {
        System.out.println("back to customer choose page");
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
