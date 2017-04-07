/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gttrainproject;

/**
 *
 * @author YICHEN
 */
public class PopularPageReport {
    int month;
    String trainNum;
    int numOfReserve;
    
    public PopularPageReport(int month, String trainNum, int numOfReserve) {
        this.month = month;
        this.trainNum = trainNum;
        this.numOfReserve = numOfReserve;  
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public void setTrainNum(String trainNum) {
        this.trainNum = trainNum;
    }
    public void setNumOfReserve(int numOfReserve) {
        this.numOfReserve = numOfReserve;
    }
    public int getMonth() {
        return month;
    }
    public String getTrainNum() {
        return trainNum;
    }
    public int getNumOfReserve() {
        return numOfReserve;
    }
    public String tostring() {
        return month + " " + trainNum + " " + numOfReserve;
    }
}
