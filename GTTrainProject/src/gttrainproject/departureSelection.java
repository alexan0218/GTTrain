/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gttrainproject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleFloatProperty;

/**
 *
 * @author CinaSMY
 */
public class departureSelection {
    private SimpleStringProperty trainNumber;
    private SimpleStringProperty duration;
    private SimpleFloatProperty firstClassPrice;
    private SimpleFloatProperty secondClassPrice;
    
    public departureSelection (String tNum, String dura, float firstC, float secondC) {
        trainNumber = new SimpleStringProperty(tNum);
        duration = new SimpleStringProperty(dura);
        firstClassPrice = new SimpleFloatProperty(firstC);
        secondClassPrice = new SimpleFloatProperty(secondC);
    }
    
    public String getTrainNumber() {
        return trainNumber.get();
    }
    
    public void setTrainNumber(String t) {
        trainNumber.set(t);
    }
    
    public String getDuration() {
        return duration.get();
    }
    
    public void setDuration(String d) {
        duration.set(d);
    }
    
    public float getFirstClassPrice() {
        return firstClassPrice.get();
    }
    
    public void setFirstClassPrice(float f) {
        firstClassPrice.set(f);
    }
    
    public float getSecondClassPrice() {
        return secondClassPrice.get();
    }
    
    public void setSecondClassPrice(float s) {
        secondClassPrice.set(s);
    }
    
    public String toString() {
        return (trainNumber.get() + ", " + duration.get() + ", " + firstClassPrice.get() + ", " + secondClassPrice.get());
    }
}
