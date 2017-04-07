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
import javafx.stage.Stage;
import java.sql.*;
import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Jihai
 */
public class LoginPageController implements Initializable {
    
    GlobalSetting setting = GlobalSetting.getInstance();
    
    @FXML
    public Button register;
    @FXML
    public Button login;
    @FXML
    public Label warning;
    @FXML
    public TextField userName;
    @FXML
    public PasswordField password;
    
    @FXML
    public void register_button_action(ActionEvent e) throws IOException {
        System.out.println("registering a new account");
        Parent register_page_parent = FXMLLoader.load(getClass().getResource("RegisterPage.fxml"));
        Scene register_page_scene = new Scene(register_page_parent);
        Stage register_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        register_page_stage.setScene(register_page_scene);
        register_page_stage.show();
    }
    
    @FXML
    public void login_button_action(ActionEvent e) throws IOException {
        Boolean error = false;
        Boolean isCustomer = false;
        Connection con = null;
        if (!userName.getText().equals("") && !password.getText().equals("")) {
            
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_58", "cs4400_Team_58", "QxXNykgB");
                if(!con.isClosed()){
                    System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
                }
                //do real work here
                Statement stmt = con.createStatement();
                ResultSet st = stmt.executeQuery("SELECT USERNAME from USER");
                ArrayList<String> names = new ArrayList<String>();
                while (st.next()) {
                    String n = st.getString("USERNAME");
                    names.add(n);
                }
                System.out.println(names);
                if (names.contains(userName.getText())) {
                    //check if password matches
                    System.out.println("user " + userName.getText() + "is found!");
                    st = stmt.executeQuery("SELECT u1.PASSWORD from USER u1 WHERE '" + userName.getText() + "' = u1.USERNAME");
                    String pword = "";
                    if (st.next()) {
                        pword = st.getString("PASSWORD");
                        System.out.println("pword = " + pword); 
                    }
                
                    if (password.getText().equals(pword)) {
                        //check if it's user
                        st = stmt.executeQuery("SELECT USERNAME from CUSTOMER");
                        ArrayList<String> customers = new ArrayList<String>();
                        while (st.next()) {
                            String n = st.getString("USERNAME");
                            customers.add(n);
                        }
                        System.out.println(customers);
                    
                        if (customers.contains(userName.getText())) {
                            isCustomer = true;
                        } else {
                            isCustomer = false;
                        }
                        setting.setCurrentUser(userName.getText(), password.getText(), isCustomer);
                    } else {
                        error = true;
                        //inform user that password doesn't match
                        warning.setText("Password doesn't match!");
                    }
                } else {
                    error = true;
                    //inform user that username doesn't exist
                    warning.setText("User name doesn't exist!");
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

            if (!error && isCustomer) {
                System.out.println("logging in as a customer");
                Parent cChoose_page_parent = FXMLLoader.load(getClass().getResource("customerChoosePage.fxml"));
                Scene cChoose_page_scene = new Scene(cChoose_page_parent);
                Stage cChoose_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                cChoose_page_stage.setScene(cChoose_page_scene);
                cChoose_page_stage.show();
            } else if (!error && !isCustomer) {
                //TODO: login as a manager
                System.out.println("logging in as a manager");
                Parent mChoose_page_parent = FXMLLoader.load(getClass().getResource("managerChoosePage.fxml"));
                Scene mChoose_page_scene = new Scene(mChoose_page_parent);
                Stage mChoose_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                mChoose_page_stage.setScene(mChoose_page_scene);
                mChoose_page_stage.show();
            } else {
                //inform unknown error happens
                System.out.println("weird thing happened");
            }
        } else if (userName.getText().equals("")) {
            warning.setText("User name can't be empty!");
        } else if (password.getText().equals("")) {
            warning.setText("Password can't be empty!");  
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
