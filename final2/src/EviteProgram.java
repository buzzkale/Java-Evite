import java.util.HashMap;
import java.util.*;

/**
* This class is everything that the user sees. It takes data from all the controller classes to run smoothly.
* 
* Author: Katelyn Le (coffee)
* kble@usc.edu
* 
*/

public class EviteProgram {
    // instance variables
    private UserDB users;
    private PartyDB parties;
    private GuestDB guests;
    private Savior savior;
    private String userFile;
    private String partyFile;
    private String guestFile;

    // initialize DBs
    public EviteProgram() {
        users = new UserDB();
        parties = new PartyDB();
        guests = new GuestDB();
        savior = new Savior();
        userFile = "C:\\Users\\katel\\IdeaProjects\\final2\\src\\users";
        partyFile = "C:\\Users\\katel\\IdeaProjects\\final2\\src\\parties";
        guestFile = "C:\\Users\\katel\\IdeaProjects\\final2\\src\\guests";
    }

    // welcome
    public void welcome() {
        System.out.println("Welcome to Katelyn Le's final Evite project, LEvite.");
    }

    // print main menu
    public MainMenu menu() {
        savior.printFancy("Main Menu");
        System.out.println("Please select a menu option by number.");
        MainMenu.printMenu();
        MainMenu choice = MainMenu.getOptionFromIndex(savior.inputInt("> "));

        return choice;
    }

    // run through menu options
    public void run(MainMenu choice) {
        // cases
        switch(choice) {
            case CREATE_ACCOUNT:
                savior.printFancy("Create a new account");
                savior.print("To create an account, please fill out the following fields.");

                String fName = savior.inputWord("First name: ");
                String lName = savior.inputWord("Last name: ");
                String email = savior.inputWord("Email: ");

                // double-checking that email isn't already in system
                while (users.checkExistingEmail(email)) {
                    savior.print("This email is already registered with an account in our " +
                            "system. Please use another one.");
                    email = savior.inputWord("Email: ");
                }

                String password = savior.inputWord("Password: ");
                users.newUser(fName, lName, email, password);
                savior.print("Account created successfully. Thank you for choosing LEvite.");

                // return to menu
                run(menu());
                break;
            case LOGIN:
                savior.printFancy("Login");
                String emailAttempt = savior.inputWord("Email: ");
                String passAttempt = savior.inputWord("Password: ");
                boolean login = users.login(emailAttempt, passAttempt);

                // login validation
                if (!login) {
                    savior.print("Incorrect email and password combination.");
                    savior.print("Please select an option.");
                    savior.print("0: Retry login");
                    savior.print("1: Create new account");
                    int loginFail = savior.inputInt("> ", 0, 1);
                    if (loginFail == 0) {
                        run(MainMenu.LOGIN);
                    }
                    else {
                        run(MainMenu.CREATE_ACCOUNT);
                    }
                    break;
                }

                // login success, run user
                User u = users.getUser(emailAttempt);
                u.welcomeMessage();
                userRun(u, userMenu());

                // return to menu
                run(menu());
                break;
            case RSVP:
                savior.printFancy("Guest RSVP");
                savior.print("Welcome! Let's party.");
                String code = savior.inputWord("Please enter the unique party code you received in your email: ");
                // checking to see if the code is valid
                while (!parties.checkCode(code)) {
                    savior.print("Incorrect code. Please try again.");
                    code = savior.inputWord("> ");
                }
                Party p = parties.getPartyFromCode(code);
                savior.print("To RSVP, please fill out the following fields. If you need to change the details of " +
                        "your RSVP later on, just simply fill out this form again.");
                String gfName = savior.inputLine("First name: ");
                String glName = savior.inputLine("Last name: ");
                String gEmail = savior.inputWord("Email: ");
                savior.print("Will you be attending '" + p.getUser().getfName() + "' hosted by " +
                        parties.getPartyFromCode(code).getUser().getEmail() + "?");
                int confirmation = savior.inputInt("0: Yes\n1: No", 0, 1);
                if (confirmation == 0) {
                    guestRun(code, p.getType(), gfName, glName, gEmail, "Will attend");
                }
                else {
                    guestRun(code, p.getType(), gfName, glName, gEmail, "Will not attend");
                    savior.print("We're sorry you can't make it! You will be missed.");
                }
                // return to menu
                run(menu());
                break;
            case QUIT:
                break;
        }
    }

    // print user menu
    public UserMenu userMenu() {
        savior.printFancy("User Menu");
        System.out.println("Please select a menu option by number.");
        UserMenu.printMenu();
        UserMenu choice = UserMenu.getOptionFromIndex(savior.inputInt("> "));

        return choice;
    }

    // run through user menu
    public void userRun(User u, UserMenu choice) {
        // cases
        switch(choice) {
            case NEW_PARTY:
                savior.printFancy("Create a new party");
                savior.print("To create a new party, please fill out all of the following fields.");
                PartyType.printMenu();
                PartyType type = PartyType.getOptionFromIndex(savior.inputInt("Please select an option: ",
                        0, 5));
                switch(type) {
                    case DINNER, BIRTHDAY:
                        savior.print(type.toString());
                        // default map
                        Map<String, Integer> restrictions = new HashMap<>();
                        restrictions.put("default", Integer.valueOf(0));
                        // input party info
                        String location = savior.inputLine("Location: ");
                        String date = savior.inputLine("Date and time: ");
                        String title = savior.inputLine("Title: ");
                        String desc = savior.inputLine("Description (to be read by invitees): ");
                        String code = savior.inputWord("Unique access code (no spaces): ");
                        while (code.contains(" ") || parties.checkCode(code)) {
                            savior.print("This code is unavailable. Please try another one.");
                            code = savior.inputWord("> ");
                        }
                        // make dinner party
                        parties.newParty(u, location, type, title, desc, date, code, restrictions);
                        savior.print(parties.getPartyFromCode(code).toString());
                        savior.print("Please confirm if this information is correct: ");
                        // confirm that info is correct
                        int confirmation = savior.inputInt("0: Yes\n1: No", 0, 1);
                        if (confirmation == 0) {
                            savior.print("Great! Thanks for celebrating with us.");
                        } else {
                            userRun(u, UserMenu.EDIT_PARTY);
                            break;
                        }

                        // return to menu
                        userRun(u, userMenu());
                        break;
                    case POTLUCK, HOLIDAY:
                        savior.print(type.toString());
                        // default map
                        ArrayList<String> allDishes = new ArrayList<>();
                        allDishes.add("Host's dish");
                        // input party info
                        location = savior.inputLine("Location: ");
                        date = savior.inputLine("Date and time: ");
                        title = savior.inputLine("Title: ");
                        desc = savior.inputLine("Description (to be read by invitees): ");
                        code = savior.inputWord("Unique access code (no spaces): ");
                        while (code.contains(" ") || parties.checkCode(code)) {
                            savior.print("This code is unavailable. Please try another one.");
                            code = savior.inputWord("> ");
                        }
                        // make potluck party
                        parties.newParty(u, location, type, title, desc, date, code, allDishes);
                        savior.print(parties.getPartyFromCode(code).toString());
                        savior.print("Please confirm if this information is correct: ");
                        // confirm that info is correct
                        confirmation = savior.inputInt("0: Yes\n1: No", 0, 1);
                        if (confirmation == 0) {
                            savior.print("Great! Thanks for celebrating with us.");
                        } else {
                            userRun(u, UserMenu.EDIT_PARTY);
                            break;
                        }

                        // return to menu
                        userRun(u, userMenu());
                        break;
                    case WEDDING, REUNION:
                        savior.print(type.toString());
                        // initialize maps
                        Map<AssignedGuest, ArrayList<String>> friends = new HashMap<>();
                        ArrayList<String> friendList = new ArrayList<>();
                        friendList.add("lover");
                        Map<AssignedGuest, ArrayList<String>> enemies = new HashMap<>();
                        ArrayList<String> enemyList = new ArrayList<>();
                        enemyList.add("evil mother in law");
                        // input party info
                        location = savior.inputLine("Location: ");
                        date = savior.inputLine("Date and time: ");
                        title = savior.inputLine("Title: ");
                        desc = savior.inputLine("Description (to be read by invitees): ");
                        code = savior.inputWord("Unique access code (no spaces): ");
                        while (code.contains(" ") || parties.checkCode(code)) {
                            savior.print("This code is unavailable. Please try another one.");
                            code = savior.inputWord("> ");
                        }
                        // initializing guest, populating Map for file purposes
                        AssignedGuest g = new AssignedGuest(code, type, "default", "default", "default",
                                "Will attend", friendList, enemyList);
                        friends.put(g, friendList);
                        enemies.put(g, enemyList);
                        guests.addGuest(g);
                        parties.getAllGuests().addGuest(g);
                        // make wedding party
                        parties.newParty(u, location, type, title, desc, date, code, friends, enemies);
                        savior.print(parties.getPartyFromCode(code).toString());
                        savior.print("Please confirm if this information is correct: ");
                        // confirm that info is correct
                        confirmation = savior.inputInt("0: Yes\n1: No", 0, 1);
                        if (confirmation == 0) {
                            savior.print("Great! Thanks for celebrating with us.");
                        } else {
                            userRun(u, UserMenu.EDIT_PARTY);
                            break;
                        }

                        // return to menu
                        userRun(u, userMenu());
                        break;
                }
                break;
            case VIEW_PARTIES:
                savior.printFancy("Viewing all parties");
                savior.print("Created parties:");
                parties.printUserParties(u.getEmail());
                String select = savior.inputWord("Select a party to view in more detail, or enter \"q\" to " +
                        "quit.");
                while (!select.equalsIgnoreCase("q")) {
                    System.out.println(parties.getPartiesByEmail(u.getEmail()).get(Integer.parseInt(select)));
                    select = savior.inputWord("Select a party to view in more detail, or enter \"q\" to " +
                            "quit.");
                }

                // return to menu
                userRun(u, userMenu());
                break;
            case EDIT_PARTY:
                savior.printFancy("Edit parties");
                parties.printUserParties(u.getEmail());
                System.out.println(parties.getPartiesByEmail(u.getEmail()).size() + ": " + "Cancel");
                int selection = savior.inputInt("Please select which party you would like to edit: ");
                if (selection == parties.getPartiesByEmail(u.getEmail()).size()) {
                    break;
                }
                partyRun(u, selection);

                // return to menu
                userRun(u, userMenu());
                break;
            case VIEW_GUESTS:
                savior.printFancy("View guest status");
                parties.printUserParties(u.getEmail());
                select = savior.inputWord("Select a party to view in more detail, or enter \"q\" to " +
                        "quit.");
                while (!select.equalsIgnoreCase("q")) {
                    ArrayList<Guest> guestList =
                            guests.getGuestsByCode(parties.getPartiesByEmail(u.getEmail()).get(Integer.parseInt(select)).getCode());
                    for (Guest g : guestList) {
                        savior.print(g.confirmationString());
                    }
                    select = savior.inputWord("Select a party to view in more detail, or enter \"q\" to " +
                            "quit.");
                }

                // return to menu
                userRun(u, userMenu());
                break;
            case CHANGE_PASS:
                savior.printFancy("Change your password");
                savior.print("To change your password, please fill out the following fields.");
                String attempt = savior.inputWord("Current password: ");
                String newPass = savior.inputWord("New password: ");
                // incorrect attempt
                if (!u.changePassword(attempt, newPass)) {
                    savior.print("Incorrect current password. Would you like to try again?");
                    int confirmation = savior.inputInt("0: Yes\n1: No", 0, 1);
                    while (confirmation == 0) {
                        attempt = savior.inputWord("Current password: ");
                        newPass = savior.inputWord("New password: ");
                        if (!u.changePassword(attempt, newPass)) {
                            savior.print("Incorrect current password. Would you like to try again?");
                            confirmation = savior.inputInt("0: Yes\n1: No", 0, 1);
                        }
                    }
                } else {
                    savior.print("Your password has been updated.");
                }

                // return to menu
                userRun(u, userMenu());
                break;
            case QUIT:
                break;
        }
    }

    // guest run
    public void guestRun(String code, PartyType type, String fName, String lName, String email, String confirmation) {
        if (confirmation.equalsIgnoreCase("will attend")) {
            switch(type) {
                case DINNER, BIRTHDAY:
                    // initializing allergy arraylist and Dinner
                    ArrayList<String> allergies = new ArrayList<>();
                    savior.print("Do you have any dietary restrictions?");
                    int yesno = savior.inputInt("0: Yes\n1: No", 0, 1);
                    if (yesno == 0) {
                        while (yesno == 0) {
                            String allergy = savior.inputLine("Please enter one restriction: ");
                            allergies.add(allergy);
                            ((Dinner) parties.getPartyFromCode(code)).addRestrictionToDinner(allergy);
                            savior.print("Do you have any other food allergies?");
                            yesno = savior.inputInt("0: Yes\n1: No", 0, 1);
                        }
                    } else {
                        allergies.add("none");
                    }
                    // creating FoodGuest
                    Guest g = new FoodGuest(code, type, fName, lName, email, confirmation, allergies);
                    guests.addGuest(g);
                    g.welcomeMessage();
                    break;
                case POTLUCK, HOLIDAY:
                    // initialize Potluck
                    String items = savior.inputLine("Please enter the item(s) you are bringing: ");
                    g = new PotLuckGuest(code, type, fName, lName, email, confirmation, items);
                    ((Potluck) parties.getPartyFromCode(code)).addDishes(items);
                    guests.addGuest(g);
                    g.welcomeMessage();
                    break;
                case WEDDING, REUNION:
                    // initializing friend and enemy arraylist
                    ArrayList<String> friends = new ArrayList<>();
                    ArrayList<String> enemies = new ArrayList<>();
                    // friends
                    savior.print("Would you like to sit next to/near anybody in particular?");
                    yesno = savior.inputInt("0: Yes\n1: No", 0, 1);
                    if (yesno == 0) {
                        while (yesno == 0) {
                            String friend = savior.inputLine("Please enter one person: ");
                            friends.add(friend);
                            savior.print("Are there any others you would like to sit next to/near?");
                            yesno = savior.inputInt("0: Yes\n1: No", 0, 1);
                        }
                    } else {
                        friends.add("none");
                    }
                    // enemies
                    savior.print("Would you like to be seated further from anybody in particular?");
                    yesno = savior.inputInt("0: Yes\n1: No", 0, 1);
                    if (yesno == 0) {
                        while (yesno == 0) {
                            String enemy = savior.inputLine("Please enter one person: ");
                            enemies.add(enemy);
                            savior.print("Are there any others you would prefer not to sit near?");
                            yesno = savior.inputInt("0: Yes\n1: No", 0, 1);
                        }
                    } else {
                        enemies.add("none");
                    }
                    // creating AssignedGuest
                    g = new AssignedGuest(code, type, fName, lName, email, confirmation, friends, enemies);
                    parties.removeDefaults(code, "default");
                    guests.removeGuest("default");
                    ((AssignedSeating) parties.getPartyFromCode(code)).populateFriends((AssignedGuest) g);
                    ((AssignedSeating) parties.getPartyFromCode(code)).populateEnemies((AssignedGuest) g);
                    guests.addGuest(g);
                    g.welcomeMessage();
                    break;
            }
        }
    }

    // party run, for editing parties
    public void partyRun(User u, int selection) {
        Party p = parties.getPartiesByEmail(u.getEmail()).get(selection);
        System.out.println(p);
        int choice;
        do {
            savior.print("Please select an option.");
            savior.print("0: Edit location");
            savior.print("1: Edit title");
            savior.print("2: Edit description");
            savior.print("3: Edit note");
            savior.print("4: Exit");
            choice = savior.inputInt("> ", 0, 4);
            if (choice == 0) {
                p.setLocation(savior.inputLine("Enter new location: "));
            }
            else if (choice == 1) {
                p.setTitle(savior.inputLine("Enter new title: "));
            }
            else if (choice == 2) {
                p.setDescription(savior.inputLine("Enter new description: "));
            }
            else if (choice == 3) {
                p.setNote(savior.inputLine("Enter new note: "));
            }
        } while (choice != 4);
        System.out.println(p);
        savior.inputInt("Is this information correct?");
        int confirmation = savior.inputInt("0: Yes\n1: No", 0, 1);
        if (confirmation == 1) {
            partyRun(u, selection);
        } else {
            savior.print("Great! Returning you to home.");
        }
    }

    // read everything in
    public void initialize() {
        users.initialize(userFile);
        parties.initialize(partyFile);
        guests.initialize(guestFile);
    }

    // save everything
    public void saveAll() {
        users.saveAll(userFile);
        parties.saveAll(partyFile);
        guests.saveAll(guestFile);
    }

    // main :)
    public static void main(String[] args) {
        EviteProgram p = new EviteProgram();
        p.initialize();
        p.welcome();
        MainMenu option = p.menu();
        p.run(option);
        p.saveAll();
    }
}