import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * This class creates the PartyDB, which reads and writes Party objects from and to files. It contains all the
 * specific Party parsers.
 *
 * author: Katelyn Le
 * kble@usc.edu
 *
 */

public class PartyDB {
    // instance variables
    private Map<String, ArrayList<Party>> allParties; // key: user email
    private GuestDB allGuests;
    private UserDB allUsers;
    private Savior savior;

    // initialize Party arraylist
    public PartyDB() {
        allParties = new HashMap<>();
        allGuests = new GuestDB();
        allUsers = new UserDB();
        allUsers.initialize("C:\\Users\\katel\\IdeaProjects\\final2\\src\\users");
        allGuests.initialize("C:\\Users\\katel\\IdeaProjects\\final2\\src\\guests");
        this.savior = new Savior();
    }

    // allParties accessor
    public Map<String, ArrayList<Party>> getAllParties() {
        return this.allParties;
    }

    // allGuests accessor
    public GuestDB getAllGuests() {
        return allGuests;
    }

    // get parties by user email
    public ArrayList<Party> getPartiesByEmail(String email) {
        return allParties.get(email);
    }

    // print user parties
    public void printUserParties(String email) {
        ArrayList<Party> userParties = getPartiesByEmail(email);
        if (userParties != null) {
            int counter = 0;
            for (Party p : userParties) {
                savior.print(counter + ": " + p.getTitle());
                counter += 1;
            }
        } else {
            savior.print("No parties created yet.");
        }
    }

    // check if guest's code is valid or if code already exists
    public boolean checkCode(String code) {
        boolean check = false;
        // loop through parties to find match
        for (Party p : getPartyList()) {
            if (p.getCode().equals(code)) {
                check = true;
            }
        }
        return check;
    }

    // get Party from code
    public Party getPartyFromCode(String code) {
        Party party = null;
        for (Party p : getPartyList()) {
            if (p.getCode().equals(code)) {
                party = p;
            }
        }
        return party;
    }

    // turn map into list of Party objects
    public ArrayList<Party> getPartyList() {
        ArrayList<Party> parties = new ArrayList<>();
        Collection<ArrayList<Party>> partyCollection = allParties.values();
        List<ArrayList<Party>> partyList = new ArrayList<>(partyCollection);
        for (ArrayList<Party> partyLists : partyList) {
            for (Party p : partyLists) {
                parties.add(p);
            }
        }
        return parties;
    }

    // adding Party from file
    public void addParty(Party p) {
        Set<String> emailSet = allParties.keySet();
        if (emailSet.contains(p.getHostEmail())) {
            allParties.get(p.getHostEmail()).add(p);
        }
        else {
            ArrayList<Party> parties = new ArrayList<>();
            parties.add(p);
            allParties.put(p.getHostEmail(), parties);
        }
    }

    // creating and adding new Potluck
    public void newParty(User u, String location, PartyType type, String title, String description, String
                         note, String code, ArrayList<String> allDishes) {
        Potluck p = new Potluck(u, location, type, title, description, note, code, allDishes);
        addParty(p);
    }

    // creating and adding new Dinner
    public void newParty(User u, String location, PartyType type, String title, String description, String
                         note, String code, Map<String, Integer> restrictions) {
        Dinner p = new Dinner(u, location, type, title, description, note, code, restrictions);
        addParty(p);
    }

    // creating and adding new AssignedSeating
    public void newParty(User u, String location, PartyType type, String title, String description, String
                         note, String code, Map<AssignedGuest, ArrayList<String>>
                         friends, Map<AssignedGuest, ArrayList<String>> enemies) {
        AssignedSeating p = new AssignedSeating(u, location, type, title, description, note, code, friends, enemies);
        addParty(p);
    }

    // deleting a party
    public void deleteParty(User u, int i) {
        allParties.get(u.getEmail()).remove(i);
    }

    // parsing Party objects from file line
    public Party parseParties(String line) {
        Party p = null;
        // make User object if all data is filled in
        try {
            Scanner sc = new Scanner(line);
            sc.useDelimiter("\t");
            String hostEmail = sc.next();
            User u = allUsers.getUser(hostEmail);
            String location = sc.next();
            // getting PartyType from index
            int typeIndex = Integer.valueOf(sc.next());
            PartyType type = PartyType.getOptionFromIndex(typeIndex);
            String title = sc.next();
            String desc = sc.next();
            String note = sc.next();
            String code = sc.next();
            // switch block depending on party type
            switch (type) {
                case DINNER, BIRTHDAY:
                    // parsing map of dietary restrictions (which and how many it applies to)
                    Map<String, Integer> restrictions = new HashMap<>();
                    restrictions.put("default", Integer.valueOf(0));
                    String mapString = sc.next(); // default@0
                    // additional layer for percentage
                    Scanner din = new Scanner(mapString);
                    din.useDelimiter("@");
                    while (din.hasNext()) {
                        String key = din.next();
                        if (!key.equals("default") && !key.equals("0")) {
                            addRestrictionToDinner(din, key, restrictions);
                        }
                    }

                    // creating Dinner
                    p = new Dinner(u, location, type, title, desc, note, code, restrictions);
                    break;
                case POTLUCK, HOLIDAY:
                    // parsing arraylist of String dishes
                    ArrayList<String> dishes = new ArrayList<>();
                    String dishesString = sc.next();
                    Scanner pot = new Scanner(dishesString);
                    pot.useDelimiter("%");
                    while (pot.hasNext()) {
                        String dish = pot.next();
                        dishes.add(dish);
                    }

                    // creating Potluck
                    p = new Potluck(u, location, type, title, desc, note, code, dishes);
                    break;
                case WEDDING, REUNION:
                    Map<AssignedGuest, ArrayList<String>> friends = new HashMap<>();
                    Map<AssignedGuest, ArrayList<String>> enemies = new HashMap<>();

                    // parsing friends
                    String friendsString = sc.next(); // default#lover
                    String enemiesString = sc.next(); // default#evil mother in law


                    if (friendsString.contains("default") &&
                            enemiesString.contains("default")) { // true
                        ArrayList<String> friendList = new ArrayList<>();
                        friendList.add("lover");
                        ArrayList<String> enemyList = new ArrayList<>();
                        enemyList.add("evil mother in law");
                        AssignedGuest g = new AssignedGuest(code, PartyType.WEDDING, "default",
                                "default", "default", "default", friendList, enemyList);
                        allGuests.addGuest(g);
                        friends.put(g, friendList);
                        enemies.put(g, enemyList);
                        p = new AssignedSeating(u, location, type, title, desc, note, code, friends, enemies);
                        System.out.println(((AssignedSeating) p).enemiesFileString());
                        System.out.println(((AssignedSeating) p).friendsFileString());
                    // if no default value
                    } else {
                        // creating and populating AssignedSeating
                        p = new AssignedSeating(u, location, type, title, desc, note, code, friends, enemies);
                        for (Guest g : allGuests.getGuestsByCode(code)) {
                            ((AssignedSeating) p).populateFriends((AssignedGuest) g);
                            ((AssignedSeating) p).populateEnemies((AssignedGuest) g);
                        }
                    }
                    break;
            }
        }
        // catch block for if User cannot be created
        catch (Exception e){
            System.err.println("Error while parsing line: " + line);
        }
        return p;
    }

    // removing defaults from AssignedSeating instance variables
    public void removeDefaults(String code, String email) {
        AssignedSeating p = (AssignedSeating) getPartyFromCode(code);
        Guest g = allGuests.getGuestFromEmail(email);
        if (g != null) {
            if (p.getFriends().keySet().contains(g)) {
                p.getFriends().remove(g);
            }
            if (p.getEnemies().keySet().contains(g)) {
                p.getEnemies().remove(g);
            }
        }
    }

    public Guest parseFriendsEnemies(Map<AssignedGuest, ArrayList<String>> map, String line) {
        Scanner inlineScan = new Scanner(line); // kble@usc.edu#Zigi Kaiser
        inlineScan.useDelimiter("#");
        String email = inlineScan.next();
        ArrayList<String> personList = new ArrayList<>();
        while (inlineScan.hasNext()) { // scanning individual friends/enemies
            String person = inlineScan.next();
            personList.add(person);
        }

        AssignedGuest g = (AssignedGuest) allGuests.getGuestFromEmail(email);
        map.put(g, personList);
        return g;
    }

    // add restriction with scanner
    public void addRestrictionToDinner(Scanner din, String key, Map<String, Integer> restrictions) {
        // if key already exists, then add 1 to count
        if (restrictions.keySet().contains(key)) {
            int i = restrictions.get(key).intValue();
            i += 1;
            restrictions.replace(key, Integer.valueOf(i));
        } else {
            if (restrictions.containsKey("default")) {
                restrictions.remove("default");
            }
            Integer count = Integer.valueOf(din.next());
            restrictions.put(key, count);
        }
    }

    // writes to a new .txt file : taken from UserDatabase.java in A11
    public void saveAll(String fileName){
        //write to file
        File f = new File(fileName);
        // write each line
        try(PrintWriter out = new PrintWriter(f)){
            for (Party p: getPartyList()){
                out.println(p.getFileString());
            }
            // catch block if file name cannot be located
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
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
                Party p = parseParties(line);
                if (p != null) {
                    addParty(p);
                }
            }
            // catch block for FileNotFoundException; print a message that file couldn't be read and start with an empty Map database
        } catch (FileNotFoundException e) {
            System.err.println("File not found; try using the terminal. Creating a new empty Map database.");
            allParties = new HashMap<>();
            // I/O exception; print a message that file couldn't be read and start with an empty Map database
        } catch (IOException e) {
            System.err.println("File access unsuccessful; try using the terminal. Creating a new empty Map database.");
            allParties = new HashMap<>();
        }
    }
}
