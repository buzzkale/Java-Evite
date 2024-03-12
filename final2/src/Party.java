import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

/**
* This class creates Party objects. It contains basic info such as who is hosting and basic details.
* 
* Author: Katelyn Le (coffee)
* kble@usc.edu
* 
*/

public abstract class Party {
    // instance variables
    protected User u;
    private String location;
    private PartyType type;
    private String title;
    private String description;
    private String note;
    private String code;
    private Savior savior;

    // constructor
    Party(User u, String location, PartyType type, String title, String description, String note, String code) {
        this.u = u;
        this.location = location;
        this.type = type;
        this.title = title;
        this.description = description;
        this.note = note;
        this.code = code;
        this.savior = new Savior();
    }

    // print information
    public String toString() {
        return "Title: " + this.title + "\nLocation: " + this.location +"\nDate: " + this.note +  "\nType: " +
                this.type + "\nDescription: " + this.description + "\nGuest access code: " + this.code;
    }

    // get file string
    public abstract String getFileString();

    // get User
    public User getUser() {
        return this.u;
    }

    // get host email
    public String getHostEmail() {
        return u.getEmail();
    }

    // accessors
    public String getLocation() {
        return location;
    }

    public PartyType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getNote() {
        return note;
    }

    public String getCode() {
        return code;
    }

    // mutators
    public void setLocation(String location) {
        this.location = location;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
