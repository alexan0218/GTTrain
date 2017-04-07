/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gttrainproject;

import java.util.ArrayList;

/**
 *
 * @author CinaSMY
 */
public class GlobalSetting {
    private String currentUsername;
    private String currentPassword;
    private Boolean isCustomer;
    private Boolean isStudent = false;
    private Reservation currentReservation;
    private String reviewingTrainNum;
    private String currentReservationID = null;
    private Boolean isAdding = false;
    private float currentTotalCost = -1;
    
    private ArrayList<Reservation> localReservations = new ArrayList<Reservation>();
    private final static GlobalSetting instance = new GlobalSetting();
    

    
    public static GlobalSetting getInstance() {
        return instance;
    }
    
    public void setIsAdding(Boolean tf) {
        isAdding = tf;
    }
    public void setCurrentUser(String un, String pw, Boolean isCus) {
        currentUsername = un;
        currentPassword = pw;
        isCustomer = isCus;
    }
    
    public Boolean getIsAdding() {
        return isAdding;
    }
    
    public String getUsername() {
        return currentUsername;
    }
    
    public void setStudent() {
        isStudent = true;
    }
    
    public Boolean isStudent() {
        return isStudent;
    }
    
    public void setCurrentReservationID(String randomID) {
        currentReservationID = randomID;
    }
    
    public void setCurrentReservation(Reservation r) {
        currentReservation = r;
    }
    
    public void setReviewingTrainNum(String train) {
        reviewingTrainNum = train;
    }
    
    public void setCurrentTotalCost(float cost) {
        currentTotalCost = cost;
    }
    
    public String getCurrentReservationID() {
        return currentReservationID;
    }
    
    public Reservation getCurrentReservation() {
        return currentReservation;
    }
    
    public Reservation getUpdatedReservation() {
        return currentReservation;
    }
    
    public ArrayList<Reservation> getLocalReservations() {
        return localReservations;
    }
    
    public String getReviewingTrainNum(){
        return reviewingTrainNum;
    }
    
    public float getCurrentTotalCost() {
        return currentTotalCost;
    }
    
    public void addCurrentReservationToLocalReservations() {
        localReservations.add(currentReservation);
    }
    
    public void removeReservationAt(int index) {
        localReservations.remove(index);
    }
    
    public void removeRecentlyAddedReservation() {
        if (localReservations.size() > 0) {
            localReservations.remove(localReservations.size() - 1);
        }
    }
    
    public void clearLocalReservations() {
        currentReservation = null;
        localReservations.clear();
        currentTotalCost = 0;
    }
    
}
