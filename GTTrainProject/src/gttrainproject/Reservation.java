/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gttrainproject;

import java.sql.Date;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author CinaSMY, Yichen
 */
public class Reservation {
    private SimpleStringProperty trainNumber;
    private SimpleStringProperty trainClass;
    private Date departureDate;
    private SimpleStringProperty date;
    private SimpleStringProperty duration;
    private SimpleStringProperty passengerName;
    private SimpleIntegerProperty baggageNumber;
    private SimpleStringProperty departureStation;
    private String departureLocation;
    private SimpleStringProperty arrivalStation;
    private String arrivalLocation;
    private SimpleFloatProperty ticketPrice;
    private SimpleStringProperty functionLabel;
    
    public Reservation(String departFrom, String arriveAt, Date date) {
        departureStation = new SimpleStringProperty(departFrom);
        arrivalStation = new SimpleStringProperty(arriveAt);
        departureDate = date;
        this.date = new SimpleStringProperty(departureDate.toString());
        trainNumber = new SimpleStringProperty(null);
        trainClass = new SimpleStringProperty(null);
        duration = new SimpleStringProperty(null);
        passengerName = new SimpleStringProperty(null);
        baggageNumber = new SimpleIntegerProperty(0);
        ticketPrice = new SimpleFloatProperty(0);
    }
    
    public void setFunctionLabel(String function) {
        functionLabel = new SimpleStringProperty(function);
    }
    
    public void setTrainNumber(String number) {
        trainNumber = new SimpleStringProperty(number);
    }
    
    public void setClass(String tclass) {
        trainClass = new SimpleStringProperty(tclass);
    }
    
    public void setBaggageNumber(int number) {
        baggageNumber = new SimpleIntegerProperty(number);
    }
    
    public void setPassengerName(String name) {
        passengerName = new SimpleStringProperty(name);
    }
    
    public void setTicketPrice(Float price) {
        ticketPrice = new SimpleFloatProperty(price);
    }
    
    public void setDuration(String dura) {
        duration = new SimpleStringProperty(dura);
    }
    
    public void setDepartureLocation(String d) {
        departureLocation = d;
    }
    
    public void setArrivalLocation(String a) {
        arrivalLocation = a;
    }
    
    //Getter Methods
    public String getFunctionLabel() {
        return functionLabel.get();
    }
    
    public String getTrainNumber() {
        return trainNumber.get();
    }
    
    public String getTrainClass() {
        return trainClass.get();
    }
    
    public Date getDepartureDate() {
        return departureDate;
    }
    
    public String getDate() {
        return date.get();
    }
    
    public String getPassengerName() {
        return passengerName.get();
    }
    
    public int getBaggageNumber() {
        return baggageNumber.get();
    }
    
    public String getDepartureStation() {
        return departureStation.get();
    }
    
    public String getArrivalStation() {
        return arrivalStation.get();
    }
    
    public Float getTicketPrice() {
        return ticketPrice.get();
    }
    
    public String getDuration() {
        return duration.get();
    }
    
    public String getDepartureLocation() {
        return departureLocation;
    }
    
    public String getArrivalLocation() {
        return arrivalLocation;
    }
    
    public String toString() {
        return trainNumber.get() + ", " + trainClass.get() + ", " + departureDate + ", " + passengerName.get() + ", " + baggageNumber.get() + ", " + departureStation.get() + ", " + arrivalStation.get();
    }
}
