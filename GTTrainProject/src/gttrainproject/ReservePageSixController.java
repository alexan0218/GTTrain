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
 * @author Jihai,Yichen
 */
public class ReservePageSixController implements Initializable {
    GlobalSetting setting = GlobalSetting.getInstance();
    @FXML
    public Button button_back;
    @FXML
    public Label resIDLabel;
    @FXML
    public Label warning;
    
    @FXML
    public void back_button_action(ActionEvent e) throws IOException {        
        System.out.println("going BACK to functionality page");
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
        String id = setting.getCurrentReservationID();
        if (id != null) {
            resIDLabel.setText(id);
        }
    }    
    
}
