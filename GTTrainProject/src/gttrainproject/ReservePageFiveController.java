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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Jihai,Yichen
 */
public class ReservePageFiveController implements Initializable {
    GlobalSetting setting = GlobalSetting.getInstance();
    
    @FXML
    public Button button_add_submit;
    @FXML
    public Button button_delete_submit;
    @FXML
    public Label warning;
    @FXML
    public TextField name;
    @FXML
    public TextField cardNum;
    @FXML
    public TextField cvv;
    @FXML
    public DatePicker datePicker = new DatePicker();
    @FXML
    public ChoiceBox<String> removingCards;
    @FXML
    public Button back;
    
    
    @FXML
    public void add_submit_button_action(ActionEvent e) throws IOException { 
        //date picker 需要点一下才把值带入
        if(!name.getText().equals("") && !cardNum.getText().equals("") && !cvv.getText().equals("") && datePicker.getValue() != null && cardNum.getText().length()== 16 && cvv.getText().length() == 3) {
            LocalDate ld = datePicker.getValue();
            Instant instant = ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            java.util.Date date = Date.from(instant);
            Connection con = null;
            try {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_58", "cs4400_Team_58", "QxXNykgB");
                    if(!con.isClosed()){
                        System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
                    }

                    //get all stations
                    PreparedStatement stmt = con.prepareStatement("insert into PAYMENTINFO "
                                       + " (USERNAME, CARD_NUMBER, CVV, EXP_DATE, NAME_ON_CARD)"
                                       + " values (?, ?, ?, ?, ?)");
                    stmt.setString(1, setting.getUsername());
                    stmt.setString(2, cardNum.getText());
                    stmt.setString(3, cvv.getText());
                    stmt.setDate(4, new java.sql.Date(date.getTime()));  
                    stmt.setString(5, name.getText());
                    stmt.execute();
                    System.out.println("going to make reservation page");
                    Parent reserveFour_page_parent = FXMLLoader.load(getClass().getResource("reservePageFour.fxml"));
                    Scene reserveFour_page_scene = new Scene(reserveFour_page_parent);
                    Stage reserveFour_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    reserveFour_page_stage.setScene(reserveFour_page_scene);
                    reserveFour_page_stage.show();

            } catch (Exception ec) {
                    warning.setText("Exception: card already exist");

            } finally {
                    try {
                        if(con != null) {
                            con.close();
                        }
                    } catch(SQLException ec) {}
            }
        } else if (name.getText().equals("")) {
            warning.setText("Name on card cannot be empty.");
        } else if (cardNum.getText().equals("")) {
            warning.setText("Card number cannot be empty.");
        } else if (cvv.getText().equals("")) {
            warning.setText("CVV cannot be empty.");
        } else if (datePicker.getValue() == null) {
            warning.setText("Exp date cannot be empty.");
        } else if (cardNum.getText().length() != 16) {
            warning.setText("Card Number should be a 16 digits number.");
        } else if (cvv.getText().length() != 3) {
            warning.setText("CVV number should be a 3 digits number.");
        } else {
            warning.setText("Unknown error occurs.");
        }
        
    }
    @FXML
    public void delete_submit_button_action(ActionEvent e) throws IOException {    
        Connection con = null;
        ArrayList<String> c = new ArrayList<String>();
        ArrayList<Integer> b = new ArrayList<>();
        ArrayList<Date> d = new ArrayList<Date>(); 
        ArrayList<Date> d2 = new ArrayList<Date>(); 
        try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_58", "cs4400_Team_58", "QxXNykgB");
                if(!con.isClosed()){
                    System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
                }
                
                //get all stations
                
                Statement checking = con.createStatement();
                ResultSet rs = checking.executeQuery("SELECT CARD_NUMBER, DEPATURE_DATE, IS_CANCELLED FROM RESERVATION NATURAL JOIN RESERVES");
                while (rs.next()) {
                    String card = rs.getString("CARD_NUMBER");
                    System.out.println(card + "card !=======================================================");
                    Date dat = rs.getDate("DEPATURE_DATE");
                    System.out.println(dat + "date !=======================================================");
                    int boo = rs.getInt("IS_CANCELLED");
                    b.add(boo);
                    c.add(card);
                    d.add(dat);
                }

                for (int i = 0; i < c.size(); i++) {
                    if (c.get(i).equals(removingCards.getValue()) && b.get(i) == 0) {
                        d2.add(d.get(i));
                    }
                }
                
                boolean canRemove = true;
                for (int j = 0; j < d2.size(); j++) {
                    if (d2.get(j).after(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())) || d2.get(j).equals(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()))) {
                        //System.out.println("can not remove !======================================");
                        canRemove = false;
                        break;
                    }
                }
                //System.out.println(canRemove + "====================");
                if (canRemove) {
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate("DELETE FROM PAYMENTINFO WHERE CARD_NUMBER = '" + removingCards.getValue() +"'");
                    System.out.println("going to make reservations page");
                    Parent reserveFour_page_parent = FXMLLoader.load(getClass().getResource("reservePageFour.fxml"));
                    Scene reserveFour_page_scene = new Scene(reserveFour_page_parent);
                    Stage reserveFour_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    reserveFour_page_stage.setScene(reserveFour_page_scene);
                    reserveFour_page_stage.show();
                } else {
                    warning.setText("Cannot remove card, since it is being used in a transaction which has not ended yet.");
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
        
    }
    
    @FXML
    public void back_button_action(ActionEvent e) throws IOException {    
        Parent reserveFour_page_parent = FXMLLoader.load(getClass().getResource("reservePageFour.fxml"));
        Scene reserveFour_page_scene = new Scene(reserveFour_page_parent);
        Stage reserveFour_page_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        reserveFour_page_stage.setScene(reserveFour_page_scene);
        reserveFour_page_stage.show();
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        datePicker.setValue(LocalDate.now());
        
        final Callback<DatePicker, DateCell> dayCellFactory = 
            new Callback<DatePicker, DateCell>() {
                @Override
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);
                           
                            if (item.isBefore(
                                    LocalDate.now())
                                ) {
                                    
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                            }   
                    }
                };
            }
        };
        
        datePicker.setDayCellFactory(dayCellFactory);
        
        Connection con = null;
        ArrayList<String> cards = new ArrayList<String>();
        try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_58", "cs4400_Team_58", "QxXNykgB");
                if(!con.isClosed()){
                    System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
                }
                
                //get all stations
                Statement stmt = con.createStatement();
                ResultSet st = stmt.executeQuery("SELECT CARD_NUMBER FROM PAYMENTINFO WHERE USERNAME = '" + setting.getUsername() +"'");
                
                while (st.next()) {
                    String cardNumber = st.getString("CARD_NUMBER");
                    cards.add(cardNumber);
                }
                
                System.out.println(cards);
        } catch (Exception ec) {
                System.err.println("Exception: " + ec.getMessage());
        } finally {
                try {
                    if(con != null) {
                        con.close();
                    }
                } catch(SQLException ec) {}
        }

        if (cards.size() == 0) {
            removingCards.setItems(FXCollections.observableArrayList("no cards available"));
            removingCards.setValue("no cards available");
        } else {
            removingCards.setItems(FXCollections.observableArrayList(cards));
            removingCards.setValue(cards.get(0));
        }
        

        
    }    
    
}
