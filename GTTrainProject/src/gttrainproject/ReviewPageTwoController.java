/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gttrainproject;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import java.sql.*;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Jihai
 */
public class ReviewPageTwoController implements Initializable {
    GlobalSetting setting = GlobalSetting.getInstance();
    ArrayList<Review> reviews = new ArrayList<Review>();
    
    @FXML
    private Button back_on_page_two;
    @FXML
    private Button button_new_search;
    
    @FXML
    private TableView table;
    @FXML
    private TableColumn rateCol;
    @FXML
    private TableColumn commentCol;
    @FXML
    public Label warning;
    
    @FXML
    public void newSearch_button_action(ActionEvent e) throws IOException {
        System.out.println("starting a new search");
        Parent reviewPageOne_page_parent = FXMLLoader.load(getClass().getResource("reviewPageOne.fxml"));
        Scene reviewPageOne_page_scene = new Scene(reviewPageOne_page_parent);
        Stage reviewPageOne_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        reviewPageOne_page_stage.setScene(reviewPageOne_page_scene);
        reviewPageOne_page_stage.show();
    }
    
    @FXML
    public void back_button_action(ActionEvent e) throws IOException {
        System.out.println("back to functionality");
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
        System.out.println("current train num:" + setting.getReviewingTrainNum());
        Connection con = null;
        try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_58", "cs4400_Team_58", "QxXNykgB");
                if(!con.isClosed()){
                    System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
                }
                
                //get all stations
                Statement stmt = con.createStatement();
                ResultSet st = stmt.executeQuery("SELECT RATING, COMMENT FROM REVIEW WHERE TRAIN_NUMBER = '" + setting.getReviewingTrainNum() + "'");
                while (st.next()) {
                    String rating = st.getString("RATING");
                    String comment = st.getString("COMMENT");
                    reviews.add(new Review(rating, comment));
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
        ObservableList data = FXCollections.observableList(reviews);
        table.setItems(data);
        rateCol.setCellValueFactory(new PropertyValueFactory("rating"));
        commentCol.setCellValueFactory(new PropertyValueFactory("comment"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //table.getSelectionModel().setCellSelectionEnabled(true);
        
    }    
    
    
    
    
    
}
