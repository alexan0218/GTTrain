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
public class RevenueReport {
    String month;
    String revenue;
    
    public RevenueReport(String month, String revenue) {
        this.month = month;
        this.revenue = revenue;
    }
    
    public void setMonth(String month) {
        this.month = month; 
    }
    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }
    public String getMonth() {
        return month;
    }
    public String getRevenue() {
        return revenue;
    }
    public String toString() {
        return month + " " + revenue;
    }
}
