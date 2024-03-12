import java.io.*;
import java.util.*;

/**
 * This class puts together all the Dinner objects and extends Party.
 * Additional instance variables:
 * Map<String, Integer> restrictions to store how many of which dietary restrictions there are.
 *
 * Author: Katelyn Le (coffee)
 * kble@usc.edu
 *
 */

public class Dinner extends Party {
    // instance variables
    private Map<String, Integer> restrictions;

    // full constructor
    Dinner(User u, String location, PartyType type, String title, String description, String note, String
            code, Map<String, Integer> restrictions) {
        super(u, location, type, title, description, note, code);
        this.restrictions = restrictions;
    }

    // restrictions accessor
    public Map<String, Integer> getRestrictions() {
        return restrictions;
    }

    // add restriction with scanner
    public void addRestrictionToDinner(String allergy) {
        // if key already exists, then add 1 to count
        if (restrictions.keySet().contains(allergy)) {
            int i = restrictions.get(allergy).intValue();
            i += 1;
            restrictions.replace(allergy, Integer.valueOf(i));
            System.out.println("restriction added successfully");
        } else {
            if (restrictions.keySet().contains(allergy)) {
                restrictions.remove("default");
            }
            restrictions.put(allergy, Integer.valueOf(1));
            System.out.println("restriction added successfully");
        }
    }

    // turning instance variable into parsable string
    public String mapToString() {
        // number of pairs in map for iteration
        int size = restrictions.size() - 1;
        // getting list of counts (of people with a certain restriction)
        Collection<Integer> countsCollection = restrictions.values();
        List<Integer> counts = new ArrayList(countsCollection);
        // getting list of keys
        Set<String> keysSet = restrictions.keySet();
        List<String> keys = new ArrayList<>(keysSet);
        String fileString = "";
        if (size > 1) {
            for (int i = 0; i < size; i++) {
            fileString += keys.get(i) + "@" + counts.get(i).toString() + "@";
            }
        }
        if (restrictions.size() > 0) {
            fileString += keys.get(size) + "@" + counts.get(size).toString();
        }
        // example of file string:
        // Vegetarian@3@Gluten-free@2
        return fileString;
    }

    @Override
    public String getFileString() {
        return this.u.getEmail() + "\t" + this.getLocation() + "\t" + PartyType.getIndexFromOption(this.getType()) +
                "\t" + this.getTitle() + "\t" + this.getDescription() + "\t" + this.getNote() + "\t" + this.getCode() +
                "\t" + mapToString();
    }
}
