import java.io.*;
import java.util.*;

/**
* This class creates AssignedGuest objects and extends Guest. It also implements Welcome.
 * Additional instance variables:
 * ArrayList<String> to store friends
 * ArrayList<String> to store enemies
* 
* Author: Katelyn Le (coffee)
* kble@usc.edu
* 
*/

public class AssignedGuest extends Guest implements Welcome {
    // instance variable
    private ArrayList<String> friends;
    private ArrayList<String> enemies;
    private Savior savior;

    // constructor
    AssignedGuest(String code, PartyType type, String fName, String lName, String email, String confirmation, ArrayList<String>
            friends, ArrayList<String> enemies) {
        super(code, type, fName, lName, email, confirmation);
        this.friends = friends;
        this.enemies = enemies;
        savior = new Savior();
    }

    // friends accessor
    public ArrayList<String> getFriends() {
        return friends;
    }

    // enemies accessor
    public ArrayList<String> getEnemies() {
        return enemies;
    }

    @Override
    public String getFileString() {
        return this.getCode() + "#" + PartyType.getIndexFromOption(this.getType()) + "#" + this.getfName() + "#" +
                this.getlName() + "#" + this.getEmail() + "#" + this.getConfirmation() + "#" +
                savior.listToFileString("%", friends) + "#" + savior.listToFileString("%", enemies);
    }
}

