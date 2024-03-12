import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * This class creates the GuestDB, which reads and writes Guest objects from and to files. It contains all the
 * specific Guest parsers.
 *
 * author: Katelyn Le
 * kble@usc.edu
 *
 */

public class GuestDB {
    // instance variables
    private Map<String, ArrayList<Guest>> allGuests; // string is unique party code
    private Savior savior;

    // initialize map
    public GuestDB() {
        allGuests = new HashMap<>();
        this.savior = new Savior();
    }

    // allGuests accessor
    public Map<String, ArrayList<Guest>> getAllGuests() {
        return this.allGuests;
    }

    // get guest from email
    public Guest getGuestFromEmail(String email) {
        Guest guest = null;
        // getting list of arraylist of guests
        Collection<ArrayList<Guest>> guestCollection = allGuests.values();
        List<ArrayList<Guest>> guestLists = new ArrayList<>(guestCollection);
        for (ArrayList<Guest> guestList : guestLists) {
            for (Guest g : guestList) {
                if (g.getEmail().equals(email)) {
                    guest = g;
                }
            }
        }
        return guest;
    }

    // get guest by party code
    public ArrayList<Guest> getGuestsByCode(String code) {
        return allGuests.get(code);
    }

    public Set<String> getAllKeys() {
        return allGuests.keySet();
    }

    // remove guest
    public void removeGuest(String email) {
        Guest g = getGuestFromEmail(email);
        if (getGuestFromEmail(email) != null) {
            getGuestsByCode(g.getCode()).remove(g);
        }
    }

    // add guest from file to db
    public void addGuest(Guest g) {
        // check to see if party code key exists
        if (getAllKeys() != null && getAllKeys().contains(g.getCode())) {
            allGuests.get(g.getCode()).add(g);
            allGuests.put(g.getCode(), allGuests.get(g.getCode()));
        } else {
            ArrayList<Guest> guestList = new ArrayList<>();
            guestList.add(g);
            allGuests.put(g.getCode(), guestList);
        }
    }

    // getting list of guests
    public ArrayList<Guest> getGuestList() {
        Collection<ArrayList<Guest>> guestCollection = allGuests.values();
        List<ArrayList<Guest>> guestList = new ArrayList<>(guestCollection);
        ArrayList<Guest> guests = new ArrayList<>();
        // putting guestList into guests
        for (ArrayList<Guest> list : guestList) {
            for (Guest g : list) {
                guests.add(g);
            }
        }
        return guests;
    }

    // write guests to file
    // writes to a new .txt file : taken from UserDatabase.java in A11
    public void saveAll(String fileName){
        //write to file
        File f = new File(fileName);
        // write each line
        try(PrintWriter out = new PrintWriter(f)){
            for (Guest g: getGuestList()){
                out.println(g.getFileString());
            }
        // catch block if file name cannot be located
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // parsing guest lines
    public Guest parseGuests(String line) {
        Guest g = null;
        // make User object if all data is filled in
        try {
            Scanner sc = new Scanner(line);
            sc.useDelimiter("#");
            String code = sc.next();
            // getting PartyType from index
            PartyType type = PartyType.getEnumFromDesc(sc.next());
            String fName = sc.next();
            String lName = sc.next();
            String email = sc.next();
            String confirmation = sc.next();
            // create new guest based on party type
            switch(type) {
                case DINNER, BIRTHDAY:
                    // parsing allergy list into arraylist
                    String allergiesString = sc.next();
                    ArrayList<String> allergies = new ArrayList<>();
                    Scanner din = new Scanner(allergiesString);
                    din.useDelimiter("%");
                    while (din.hasNext()) {
                        String allergy = din.next();
                        allergies.add(allergy);
                    }

                    // creating FoodGuest
                    g = new FoodGuest(code, type, fName, lName, email, confirmation, allergies);
                    break;
                case POTLUCK, HOLIDAY:
                    // adding String itemsBringing for potluck
                    String itemsBringing = sc.next();

                    // creating PotLuckGuest
                    g = new PotLuckGuest(code, type, fName, lName, email, confirmation, itemsBringing);
                    break;
                case WEDDING, REUNION:
                    // parsing friends list into arraylist
                    String friends = sc.next(); // friend%friend
                    ArrayList<String> friendList = new ArrayList<>();
                    Scanner f = new Scanner(friends);
                    f.useDelimiter("%");
                    while (f.hasNext()) {
                        String friend = f.next();
                        friendList.add(friend);
                    }
                    // parsing enemies list into arraylist
                    String enemies = sc.next();
                    ArrayList<String> enemyList = new ArrayList<>();
                    Scanner e = new Scanner(enemies);
                    e.useDelimiter("%");
                    while (e.hasNext()) {
                        String enemy = e.next();
                        enemyList.add(enemy);
                    }

                    // creating AssignedGuest
                    g = new AssignedGuest(code, type, fName, lName, email, confirmation, friendList, enemyList);
                    break;
            }
        }
        // catch block for if User cannot be created
        catch (Exception e){
            System.err.println("Error while parsing line: " + line);
        }
        return g;
    }

    // reads file, creates User objects from file : taken from UserDatabase.java in A11
    public void initialize(String fileName) {
        // read in users from a file
        // try with resources
        try (FileInputStream fis = new FileInputStream(fileName);
             Scanner sc = new Scanner(fis)){
            // parsing file, assuming fis works
            while (sc.hasNext()) {
                String line = sc.nextLine();
                Guest g = parseGuests(line);
                if (g != null) {
                    addGuest(g);
                }
            }
            // catch block for FileNotFoundException; print a message that file couldn't be read and start with an empty Map database
        } catch (FileNotFoundException e) {
            System.err.println("File not found; try using the terminal. Creating a new empty Map database.");
            allGuests = new HashMap<>();
            // I/O exception; print a message that file couldn't be read and start with an empty Map database
        } catch (IOException e) {
            System.err.println("File access unsuccessful; try using the terminal. Creating a new empty Map database.");
            allGuests = new HashMap<>();
        }
    }
}
