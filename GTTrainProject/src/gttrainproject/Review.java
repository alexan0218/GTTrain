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
public class Review {
    private String rating;
    private String comment;
    
    public Review(String rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }
    
    public void setRating(String rate) {
        rating = rate;
    }
    
    public void setComment(String cmt) {
        comment = cmt;
    }
    
    public String getRating() {
        return rating;
    }
    
    public String getComment() {
        return comment;
    }
    
}
