import java.util.*;

/**
* This class creates AssignedSeating objects and extends Party.
 * Additional instance variables:
 * Map<AssignedGuest, ArrayList<String>> friends to store a Guest object's liked people
 * Map<AssignedGuest, ArrayList<String>> friends to store a Guest object's disliked people
*
* Author: Katelyn Le (coffee)
* kble@usc.edu
* 
*/

public class AssignedSeating extends Party {
    // instance variables
    private Map<AssignedGuest, ArrayList<String>> friends;
    private Map<AssignedGuest, ArrayList<String>> enemies;
    private Savior savior;

    // constructor
    AssignedSeating(User u, String location, PartyType type, String title, String description, String note,
                    String code, Map<AssignedGuest, ArrayList<String>> friends,
                    Map<AssignedGuest, ArrayList<String>> enemies) {
        super(u, location, type, title, description, note, code);
        this.friends = friends;
        this.enemies = enemies;
        savior = new Savior();
    }

    // friends accessor
    public Map<AssignedGuest, ArrayList<String>> getFriends() {
        return friends;
    }

    // enemies accessor
    public Map<AssignedGuest, ArrayList<String>> getEnemies() {
        return enemies;
    }

    // adding guests and their friend preferences
    public void populateFriends(AssignedGuest g) {
        friends.put(g, g.getFriends());
    }

    // adding guests and their enemies
    public void populateEnemies(AssignedGuest g) {
        enemies.put(g, g.getEnemies());
    }

    // turn friend map into file string
    public String friendsFileString() {
        String friendsFileString = "";
        // turning keys into iterable list
        Set<AssignedGuest> guestsSet = friends.keySet();
        List<AssignedGuest> guests = new ArrayList<>(guestsSet);

        // looping through list of guests' friends lists
        for (int i = 0; i < guests.size() - 1; i++) {
            friendsFileString += guests.get(i).getEmail() + "#";
            ArrayList<String> friendList = friends.get(guests.get(i));
            friendsFileString += savior.listToFileString("#", friendList) + "%";
        }
        friendsFileString += guests.get(guests.size() - 1).getEmail() + "#";
        friendsFileString += savior.listToFileString("#", friends.get(guests.get(guests.size() - 1)));

        return friendsFileString;
    }

    // turn enemy map into file string
    public String enemiesFileString() {
        String enemiesFileString = "";
        // turning keys into iterable list
        Set<AssignedGuest> guestsSet = enemies.keySet();
        List<AssignedGuest> guests = new ArrayList<>(guestsSet);

        // looping through list of guests' friends lists
        for (int i = 0; i < guests.size() - 1; i++) {
            enemiesFileString += guests.get(i).getEmail() + "#";
            ArrayList<String> enemyList = enemies.get(guests.get(i));
            enemiesFileString += savior.listToFileString("#", enemyList) + "%";
        }
        enemiesFileString += guests.get(guests.size() - 1).getEmail() + "#";
        enemiesFileString += savior.listToFileString("#", enemies.get(guests.get(guests.size() - 1)));
        // file string example:
        // kble@usc.edu#Zigi Kaiser#Maddie Ta%zabrina@usc.edu#Kayla#Tiffany
        return enemiesFileString;
    }

    @Override
    public String getFileString() {
        return u.getEmail() + "\t" + getLocation() + "\t" + PartyType.getIndexFromOption(getType()) + "\t" + getTitle()
                + "\t" + getDescription() + "\t" + getNote() + "\t" + getCode() + "\t" + friendsFileString() + "\t" +
                enemiesFileString();
    }
}
