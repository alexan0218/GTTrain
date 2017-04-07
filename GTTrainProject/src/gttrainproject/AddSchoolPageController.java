/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gttrainproject;

import java.io.IOException;
import java.net.URL;
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
import java.sql.*;

/**
 * FXML Controller class
 *
 * @author Jihai
 */
public class AddSchoolPageController implements Initializable {
    GlobalSetting setting = GlobalSetting.getInstance();

    @FXML
    public Button back;
    @FXML
    public Button submit;
    @FXML
    public Label warning;
    @FXML
    public TextField email;
    
    @FXML
    public void back_button_action(ActionEvent e) throws IOException {
        System.out.println("backing to customer choose page");
        Parent cChoose_page_parent = FXMLLoader.load(getClass().getResource("customerChoosePage.fxml"));
        Scene cChoose_page_scene = new Scene(cChoose_page_parent);
        Stage cChoose_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        cChoose_page_stage.setScene(cChoose_page_scene);
        cChoose_page_stage.show();
    }
    
    @FXML
    public void submit_button_action(ActionEvent e) throws IOException {
        //TODO: something should be added to database
        Connection con = null;
        if (email.getText().contains("@") && email.getText().contains(".edu")) {
            Boolean error  = false;
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_58", "cs4400_Team_58", "QxXNykgB");
                if(!con.isClosed()){
                    System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
                }
                //do real work here
                String username = setting.getUsername();
                
                PreparedStatement stmt = con.prepareStatement("UPDATE CUSTOMER "
                       + "SET IS_STUDENT = " + true
                       + " WHERE USERNAME = '" + username + "'");
                stmt.execute();
                //setting.setStudent();
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
                //GOTO previous page
                System.out.println("backing to customer choose page");
                Parent cChoose_page_parent = FXMLLoader.load(getClass().getResource("customerChoosePage.fxml"));
                Scene cChoose_page_scene = new Scene(cChoose_page_parent);
                Stage cChoose_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                cChoose_page_stage.setScene(cChoose_page_scene);
                cChoose_page_stage.show();
            }
            
        } else {
            warning.setText("Pleas provide a valid Email address.");
        }
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
