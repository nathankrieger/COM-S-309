package com.example.frontend.models;

import java.util.ArrayList;

public class User {

    private int ID;

    private boolean IsActive;

    private String Username;

    private String Email;

    private String Password;

    private ArrayList<Bookmark> Bookmarks;

    public User(String username, String password) {
        this.Username = username;
        this.Password = password;
    }

    public void setUserID(int id) { this.ID = id; }

    public String getUsername() {
        return Username;
    }

    public String getEmail() {
        return Username + "@gmail.com";
    } // TODO

    public int getUserID() {
        return ID;
    }

    public ArrayList<Bookmark> getBookmarks() { return Bookmarks; }

    public void setBookmarks(ArrayList<Bookmark> bookmarks) { this.Bookmarks = bookmarks; }
}
