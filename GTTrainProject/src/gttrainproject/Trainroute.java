/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gttrainproject;

/**
 *
 * @author Jihai
 */
public class Trainroute {
    
    private String trainNum;
    private String departTime;
    private String arrivalTime;
    private String station;
    
    public Trainroute(String trainNum, String arrivalTime, String departTime, String station) {
        this.trainNum = trainNum;
        this.arrivalTime = arrivalTime;
        this.departTime = departTime;
        this.station = station;
    }
    
    public void setTrainNum(String trainNum) {
        this.trainNum = trainNum;
    }
    
    public void setDepartTime(String departTime) {
        this.departTime = departTime;
    }
    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    public void setStation(String station) {
        this.station = station;
    }
    
    public String getTrainNum() {
        return trainNum;
    }
    
    public String getDepartTime() {
        return departTime;
    }
    public String getArrivalTime() {
        return arrivalTime;
    }
    public String getStation() {
        return station;
    }
    @Override
    public String toString() {
        return trainNum + " " + arrivalTime + " " + departTime + " " + station;
    }
    
}
