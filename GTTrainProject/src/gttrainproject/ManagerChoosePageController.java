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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Jihai
 */
public class ManagerChoosePageController implements Initializable {
    
    public Button logout;
    public Button button_view_revenue;
    public Button button_view_popular;
    
    public Button back_from_revenue;
    public Button back_from_popular;
    @FXML
    private Label warning;
    
    
    //functionality page methods
    public void logout_button_action(ActionEvent e) throws IOException {        
        System.out.println("backing to loggin page(from manager choose page)");
        Parent login_page_parent = FXMLLoader.load(getClass().getResource("loginPage.fxml"));
        Scene login_page_scene = new Scene(login_page_parent);
        Stage login_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        login_page_stage.setScene(login_page_scene);
        login_page_stage.show();
    }
    public void revenue_button_action(ActionEvent e) throws IOException {        
        System.out.println("going to view revenue page");
        Parent revenue_page_parent = FXMLLoader.load(getClass().getResource("revenuePage.fxml"));
        Scene revenue_page_scene = new Scene(revenue_page_parent);
        Stage revenue_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        revenue_page_stage.setScene(revenue_page_scene);
        revenue_page_stage.show();
    }
    public void popular_button_action(ActionEvent e) throws IOException {        
        System.out.println("going to view popular page");
        Parent popular_page_parent = FXMLLoader.load(getClass().getResource("popularPage.fxml"));
        Scene popular_page_scene = new Scene(popular_page_parent);
        Stage popular_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        popular_page_stage.setScene(popular_page_scene);
        popular_page_stage.show();
    }
    
    
    


    //revenue and popular page actions
    @FXML
    public void back_button_action(ActionEvent e) throws IOException {        
        System.out.println("back to manager choose page from popular page");
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
    }    
    
}
