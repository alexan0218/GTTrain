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
public class ViewTrainPageOneController implements Initializable {
    
    GlobalSetting setting = GlobalSetting.getInstance();
    
    @FXML
    public Button search;
    @FXML
    public Button back;
    @FXML
    public TextField trainNum;
    @FXML
    public Label warning;
    
    @FXML
    public void back_button_action(ActionEvent e) throws IOException {
        System.out.println("logging in as a customer");
        Parent cChoose_page_parent = FXMLLoader.load(getClass().getResource("customerChoosePage.fxml"));
        Scene cChoose_page_scene = new Scene(cChoose_page_parent);
        Stage cChoose_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        cChoose_page_stage.setScene(cChoose_page_scene);
        cChoose_page_stage.show();
    }
    
    @FXML
    public void search_button_action(ActionEvent e) throws IOException {
        
        if (!trainNum.getText().equals("")) {
            Connection con = null;
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_58", "cs4400_Team_58", "QxXNykgB");
                if(!con.isClosed()){
                    System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
                }
                //do real work here
                Statement stmt = con.createStatement();
                ResultSet st = stmt.executeQuery("SELECT TRAIN_NUMBER from TRAINROUTE");
                ArrayList<String> trains = new ArrayList<String>();
                while (st.next()) {
                    String n = st.getString("TRAIN_NUMBER");
                    trains.add(n);
                }

                if (trains.contains(trainNum.getText())) {
                    setting.setReviewingTrainNum(trainNum.getText());

                    System.out.println("searching a train schedule");
                    Parent viewTrainPageTwo_page_parent = FXMLLoader.load(getClass().getResource("viewTrainPageTwo.fxml"));
                    Scene viewTrainPageTwo_page_scene = new Scene(viewTrainPageTwo_page_parent);
                    Stage viewTrainPageTwo_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    viewTrainPageTwo_page_stage.setScene(viewTrainPageTwo_page_scene);
                    viewTrainPageTwo_page_stage.show();
                } else {
                    warning.setText("Cannot find the train.");
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
            warning.setText("Please input a Train number.");
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
