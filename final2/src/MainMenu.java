/**
 * This enum contains all the Main Menu options. It has a method that gets the enum value from its index.
 *
 * author: Katelyn Le
 * kble@usc.edu
 *
 */

public enum MainMenu {
    CREATE_ACCOUNT("Create account"),
    RSVP("RSVP to an event"),
    LOGIN("Login to your account"),
    QUIT("Quit");

    // instance variables
    private String choice;

    // constructor
    MainMenu(String choice) {
        this.choice = choice;
    }

    // print menu
    public static void printMenu() {
        int optionNum = 0;
        for (MainMenu options : MainMenu.values()) {
            System.out.println(optionNum + ": " + options);
            optionNum += 1;
        }
    }

    // to string
    public String toString() {
        return this.choice;
    }

    // turn number into MainMenu option
    public static MainMenu getOptionFromIndex(int num) {
        return MainMenu.values()[num];
    }
}
