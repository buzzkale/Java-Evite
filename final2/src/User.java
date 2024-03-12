import java.io.*;
import java.util.*;

/**
* This class creates User objects. It contains the basic account info.
* 
* Author: Katelyn Le (coffee)
* kble@usc.edu
* 
*/

public class User implements Welcome {
    // instance variables
    private String fName;
    private String lName;
    private String email;
    private String password;

    // full constructor
    User(String fName, String lName, String email, String password) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.password = password;
    }

    // fName accessor
    public String getfName() {
        return this.fName;
    }

    // lName accessor
    public String getlName() {
        return this.lName;
    }

    // email accessor
    public String getEmail() {
        return this.email;
    }

    // check password
    public boolean checkPassword(String attempt) {
        if (attempt.equals(this.password)) {
            return true;
        }
        else {
            return false;
        }
    }

    // change password
    public boolean changePassword(String attempt, String newPass) {
        if (attempt.equals(this.password)) {
            this.password = newPass;
            return true;
        }
        else {
            return false;
        }
    }

    // file string accessor
    public String getFileString() {
        return this.fName + "\t" + this.lName + "\t" + this.email + "\t" + this.password;
    }

    @Override
    public void welcomeMessage() {
        System.out.println("Weclome, " + this.fName + "! Let's throw a rager.");
    }
}
