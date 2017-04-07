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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.*;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
/**
 * FXML Controller class
 *
 * @author Jihai
 */
public class CommentPageController implements Initializable {
    GlobalSetting setting = GlobalSetting.getInstance();
    
    @FXML
    public Button Back;
    @FXML
    public Button submit;
    @FXML
    public Label warning;
    @FXML
    public TextField trainNum;
    @FXML
    public ChoiceBox<String> rating;
    @FXML
    public TextArea comment;
    
    
    
    @FXML
    public void back_button_action(ActionEvent e) throws IOException {
        System.out.println("back to main page");
        Parent cChoose_page_parent = FXMLLoader.load(getClass().getResource("customerChoosePage.fxml"));
        Scene cChoose_page_scene = new Scene(cChoose_page_parent);
        Stage cChoose_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        cChoose_page_stage.setScene(cChoose_page_scene);
        cChoose_page_stage.show();
    }
    
    @FXML
    public void submit_button_action(ActionEvent e) throws IOException {
        
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_58", "cs4400_Team_58", "QxXNykgB");
            if(!con.isClosed()){
                System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
            }
            //do real work here
            String username = setting.getUsername();

            Statement stmt = con.createStatement();
            ResultSet st = stmt.executeQuery("SELECT TRAIN_NUMBER from TRAINROUTE");
            ArrayList<String> trains = new ArrayList<String>();
            while (st.next()) {
                String n = st.getString("TRAIN_NUMBER");
                System.out.println(n);
                trains.add(n);
            }
            if (trains.contains(trainNum.getText())){ 
                PreparedStatement stmt2 = con.prepareStatement("insert into REVIEW "
                          + " (USERNAME, TRAIN_NUMBER, COMMENT, RATING)"
                          + " values (?, ?, ?, ?)");
                stmt2.setString(1, username);
                stmt2.setString(2, trainNum.getText());
                stmt2.setString(3, comment.getText());
                stmt2.setString(4, rating.getValue());
                stmt2.execute();
                System.out.println("submiting and going to main page");
                //goto next page;
                Parent cChoose_page_parent = FXMLLoader.load(getClass().getResource("customerChoosePage.fxml"));
                Scene cChoose_page_scene = new Scene(cChoose_page_parent);
                Stage cChoose_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                cChoose_page_stage.setScene(cChoose_page_scene);
                cChoose_page_stage.show();
            } else {
                //label warning
                warning.setText("Train number invalid, please try again.");
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
        //Goto next page
        
        
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        rating.setItems(FXCollections.observableArrayList("Very Good", "Good", "Neutral", "Bad", "Very Bad"));
        rating.getSelectionModel().selectFirst();
    }    
    
}
