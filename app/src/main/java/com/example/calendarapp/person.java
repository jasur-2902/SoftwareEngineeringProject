// Your package name can be different depending 
// on your project name 
package com.example.calendarapp;

public class person
{
    // Variable to store data corresponding
    // to firstname keyword in database 
    private String username;

    // Variable to store data corresponding 
    // to lastname keyword in database 
    private String status;

    // Variable to store data corresponding 
    // to age keyword in database 
    private String imageURL;

    // Mandatory empty constructor 
    // for use of FirebaseUI 
    public person() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}