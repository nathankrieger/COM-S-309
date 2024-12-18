package com.example.frontend.models;

public class Comment {

    private int ID;
    private String Text;
    private int UserID;

    public Comment(int id, int userID, String text) {
        this.ID = id;
        this.UserID = userID;
        this.Text = text;
    }

    public int getID() {
        return ID;
    }

    public int getUserID() {
        return UserID;
    }

    public String getText() {
        return Text;
    }

    public void setUserID(int id) {
        this.UserID = id;
    }

    public void setText(String text) {
        this.Text = text;
    }



}
