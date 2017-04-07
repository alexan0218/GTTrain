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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Jihai
 */
public class CustomerChoosePageController implements Initializable {
    GlobalSetting setting = GlobalSetting.getInstance();

    @FXML
    public Button logout;
    @FXML
    public Button button_viewTrainSchedule;
    @FXML
    public Button button_addStudentInfo;
    @FXML
    public Button button_make_reservation;
    @FXML
    public Button button_update_reservation;
    @FXML
    public Button button_cancel_reservation;
    @FXML
    public Button button_view_review;
    @FXML
    public Button button_give_review;
    
    
    @FXML
    public void logout_button_action(ActionEvent e) throws IOException {
        
        System.out.println("backing to loggin page(from customer choose page)");
        Parent login_page_parent = FXMLLoader.load(getClass().getResource("loginPage.fxml"));
        Scene login_page_scene = new Scene(login_page_parent);
        Stage login_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        login_page_stage.setScene(login_page_scene);
        login_page_stage.show();
    }
    
    @FXML
    public void viewSchedule_button_action(ActionEvent e) throws IOException {
        
        System.out.println("going to view train schedule page");
        Parent viewTrainPageOne_page_parent = FXMLLoader.load(getClass().getResource("viewTrainPageOne.fxml"));
        Scene viewTrainPageOne_page_scene = new Scene(viewTrainPageOne_page_parent);
        Stage viewTrainPageOne_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        viewTrainPageOne_page_stage.setScene(viewTrainPageOne_page_scene);
        viewTrainPageOne_page_stage.show();
    }
    
    @FXML
    public void addStudentInfo_button_action(ActionEvent e) throws IOException {
        System.out.println("going to add student info page");
        Parent addSchool_page_parent = FXMLLoader.load(getClass().getResource("addSchoolPage.fxml"));
        Scene addSchool_page_scene = new Scene(addSchool_page_parent);
        Stage addSchool_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        addSchool_page_stage.setScene(addSchool_page_scene);
        addSchool_page_stage.show();
    }
    @FXML
    public void makeReservation_button_action(ActionEvent e) throws IOException {
        System.out.println("going to search train info page");
        Parent makeReservation_page_parent = FXMLLoader.load(getClass().getResource("reservePageOne.fxml"));
        Scene makeReservation_page_scene = new Scene(makeReservation_page_parent);
        Stage makeReservation_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        makeReservation_page_stage.setScene(makeReservation_page_scene);
        makeReservation_page_stage.show();
    } 
    @FXML
    public void updateReservation_button_action(ActionEvent e) throws IOException {
        System.out.println("going to update revervation page");
        Parent updateReservation_page_parent = FXMLLoader.load(getClass().getResource("updatePageOne.fxml"));
        Scene updateReservation_page_scene = new Scene(updateReservation_page_parent);
        Stage updateReservation_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        updateReservation_page_stage.setScene(updateReservation_page_scene);
        updateReservation_page_stage.show();
    }
    
    @FXML
    public void cancelReservation_button_action(ActionEvent e) throws IOException {
        
        System.out.println("going to cancellation page");
        Parent cancelPageOne_page_parent = FXMLLoader.load(getClass().getResource("cancelPageOne.fxml"));
        Scene cancelPageOne_page_scene = new Scene(cancelPageOne_page_parent);
        Stage cancelPageOne_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        cancelPageOne_page_stage.setScene(cancelPageOne_page_scene);
        cancelPageOne_page_stage.show();
    }
    
    @FXML
    public void viewReview_button_action(ActionEvent e) throws IOException {      
        System.out.println("viewing review");
        Parent reviewPageOne_page_parent = FXMLLoader.load(getClass().getResource("reviewPageOne.fxml"));
        Scene reviewPageOne_page_scene = new Scene(reviewPageOne_page_parent);
        Stage reviewPageOne_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        reviewPageOne_page_stage.setScene(reviewPageOne_page_scene);
        reviewPageOne_page_stage.show();
    }
    @FXML
    public void giveReview_button_action(ActionEvent e) throws IOException {      
        System.out.println("giving review");
        Parent comment_page_parent = FXMLLoader.load(getClass().getResource("commentPage.fxml"));
        Scene comment_page_scene = new Scene(comment_page_parent);
        Stage comment_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        comment_page_stage.setScene(comment_page_scene);
        comment_page_stage.show();
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
                ResultSet st = stmt.executeQuery("SELECT IS_STUDENT FROM CUSTOMER WHERE USERNAME = '" + setting.getUsername() +"'");
                
                
                while (st.next()) {
                    if (st.getBoolean("IS_STUDENT")) {
                        setting.setStudent();
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
        
        
        
        
        setting.clearLocalReservations();
    }    
    
}
