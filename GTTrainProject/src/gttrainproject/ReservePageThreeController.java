/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gttrainproject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Jihai,Yichen
 */
public class ReservePageThreeController implements Initializable {
    GlobalSetting setting = GlobalSetting.getInstance();
    
    String pName = null;
    int baggageNumber = 0;
    
    @FXML
    public Button button_next_pay;
    @FXML
    public Button button_back_select_departure;
    @FXML
    public ChoiceBox<String> baggageNum;
    @FXML
    public TextField passengerName;
    @FXML
    public Label warning;
    
    @FXML
    public void next_buy_button_action(ActionEvent e) throws IOException {
        
        
        baggageNumber = Integer.parseInt(baggageNum.getValue());
        pName = passengerName.getText();
        // baggegeNUmber can be zero
        if (pName != null && !pName.equals("")) {
            Reservation res = setting.getCurrentReservation();
            res.setBaggageNumber(baggageNumber);
            res.setPassengerName(pName);
            res.setFunctionLabel("remove");
            setting.setCurrentReservation(res);
            
            System.out.println(res);
            
            setting.addCurrentReservationToLocalReservations();
            
            System.out.println("going to pay page");
            Parent reserveFour_page_parent = FXMLLoader.load(getClass().getResource("reservePageFour.fxml"));
            Scene reserveFour_page_scene = new Scene(reserveFour_page_parent);
            Stage reserveFour_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            reserveFour_page_stage.setScene(reserveFour_page_scene);
            reserveFour_page_stage.show();
        } else {

            if (pName.equals("")) {
                warning.setText("put your name in to continue.");
                //System.out.println("put your name in to continue.");
            } else {
                warning.setText("error occured!");
                //System.out.println("error occured!");
            }

        }
        
        
    }
    @FXML
    public void back_select_departure_button_action(ActionEvent e) throws IOException {
        
        System.out.println("going BACK to select departure page");
        Parent reserveTwo_page_parent = FXMLLoader.load(getClass().getResource("reservePageTwo.fxml"));
        Scene reserveTwo_page_scene = new Scene(reserveTwo_page_parent);
        Stage reserveTwo_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        reserveTwo_page_stage.setScene(reserveTwo_page_scene);
        reserveTwo_page_stage.show();
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        baggageNum.setItems(FXCollections.observableArrayList("0", "1", "2", "3", "4"));
        baggageNum.setValue("0");
    }    
    
}
