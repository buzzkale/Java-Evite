import java.io.*;
import java.util.*;

/**
 * This class puts together all the Potluck objects and extends Party.
 * Additional parameters:
 * ArrayList<String> allDishes to store what things people are bringing.
 *
 * Author: Katelyn Le (coffee)
 * kble@usc.edu
 *
 */

public class Potluck extends Party{
    // instance variables
    private ArrayList<String> allDishes;
    private Savior savior;

    // constructor
    Potluck(User u, String location, PartyType type, String title, String description, String note, String code,
            ArrayList<String> allDishes) {
        super(u, location, type, title, description, note, code);
        this.allDishes = allDishes;
        savior = new Savior();
    }

    public ArrayList<String> getAllDishes() {
        return this.allDishes;
    }

    public void addDishes(String dishes) {
        allDishes.add(dishes);
    }

    @Override
    public String getFileString() {
        return this.u.getEmail() + "\t" + this.getLocation() + "\t" + PartyType.getIndexFromOption(getType()) + "\t" +
                this.getTitle() + "\t" + this.getDescription() + "\t" + this.getNote() + "\t" + this.getCode() +
                "\t" + savior.listToFileString("%", allDishes);
    }
}
