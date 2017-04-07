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
public class RegisterPageController implements Initializable {
    
    @FXML
    public Button back;
    @FXML
    public Button create;
    @FXML
    public Label warning;
    @FXML
    public TextField userName;
    @FXML
    public TextField emailAddress;
    @FXML
    public PasswordField password;
    @FXML
    public PasswordField checkPassword;
    
    
    @FXML
    public void back_button_action(ActionEvent e) throws IOException {
        System.out.println("backing to loggin page");
        Parent login_page_parent = FXMLLoader.load(getClass().getResource("loginPage.fxml"));
        Scene login_page_scene = new Scene(login_page_parent);
        Stage login_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        login_page_stage.setScene(login_page_scene);
        login_page_stage.show();
    }
     @FXML
    public void create_button_action(ActionEvent e) throws IOException {
        //TODO: should add a new user to database;
        Connection con = null;
        if (!userName.getText().equals("") && emailAddress.getText().contains("@") && !password.getText().equals("") &&  password.getText().equals(checkPassword.getText())) {
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_58", "cs4400_Team_58", "QxXNykgB");
                if(!con.isClosed()){
                    System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
                }
                //do real work here
                Statement stmt = con.createStatement();
                ResultSet st = stmt.executeQuery("SELECT USERNAME FROM USER");
                //ResultSet st2 = stmt.executeQuery("SELECT EMAIL FROM CUSTOMER");
                ArrayList<String> names = new ArrayList<String>();
                
                while (st.next()) {
                    String n = st.getString("USERNAME");
                    names.add(n);
                }
               
                if (!names.contains(userName.getText())) {
                    st = stmt.executeQuery("SELECT EMAIL FROM CUSTOMER");
                    ArrayList<String> emails = new ArrayList<String>();
                    while(st.next()) {
                        String n = st.getString("EMAIL");
                        emails.add(n);
                    }
                    if (!emails.contains(emailAddress.getText())) {
                        PreparedStatement stmt3 = con.prepareStatement("insert into USER "
                                   + " (USERNAME, PASSWORD)"
                                   + " values (?,?)");
                        stmt3.setString(1, userName.getText());
                        stmt3.setString(2, password.getText());
                        stmt3.execute();
                        
                        PreparedStatement stmt2 = con.prepareStatement("insert into CUSTOMER "
                                   + " (USERNAME, EMAIL, IS_STUDENT )"
                                   + " values (?, ?, ?)");
                        stmt2.setString(1, userName.getText());
                        stmt2.setString(2, emailAddress.getText());
                        stmt2.setBoolean(3, false);
                        stmt2.execute();

                        
                        //goto next page
                        System.out.println("backing to loggin page(user created!)");
                        Parent login_page_parent = FXMLLoader.load(getClass().getResource("loginPage.fxml"));
                        Scene login_page_scene = new Scene(login_page_parent);
                        Stage login_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                        login_page_stage.setScene(login_page_scene);
                        login_page_stage.show();
                    } else {
                        warning.setText("Email address already exists.");
                    }
                } else {    
                    warning.setText("User name already exists.");
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

        } else {
            //inform user of exception
            if (userName.getText().equals("")) {
                warning.setText("User name can't be empty!");
            } else if (!emailAddress.getText().contains("@")) {
                warning.setText("Email address invalid!");
            } else if (password.getText().equals("")) {
                warning.setText("Password can't be empty!");
            } else if (!password.getText().equals(checkPassword.getText())) {
                warning.setText("Two passwords must match!");
            }
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
