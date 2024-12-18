package com.example.frontend.models;

public class Bookmark {


    private int ID;
    private int MovieID;

    private String Name;

    private int UserID;

    public Bookmark(int id, int movieID, String name, int userID) {

        this.ID = id;
        this.MovieID = movieID;
        this.Name = name;
        this.UserID = userID;

    }

    // Getter and Setter for ID
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    // Getter and Setter for MovieID
    public int getMovieID() {
        return MovieID;
    }

    public void setMovieID(int movieID) {
        this.MovieID = movieID;
    }

    // Getter and Setter for Name
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    // Getter and Setter for UserID
    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        this.UserID = userID;
    }
}
