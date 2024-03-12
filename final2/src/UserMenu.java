/**
 * This enum contains all the UserMenu options. It has a method that gets the enum value from its index.
 *
 * author: Katelyn Le
 * kble@usc.edu
 *
 */

public enum UserMenu {
    NEW_PARTY("Create a new party"),
    VIEW_PARTIES("View all your current parties, current and past"),
    EDIT_PARTY("Change the details of an existing party"),
    VIEW_GUESTS("View guest status"),
    CHANGE_PASS("Update your password"),
    QUIT("Log out");

    // variable
    private String desc;

    // constructor
    UserMenu(String desc) {
        this.desc = desc;
    }

    // print menu
    public static void printMenu() {
        int optionNum = 0;
        for (UserMenu options : UserMenu.values()) {
            System.out.println(optionNum + ": " + options);
            optionNum += 1;
        }
    }

    // to string
    public String toString() {
        return this.desc;
    }

    // turn number into UserMenu option
    public static UserMenu getOptionFromIndex(int num) {
        return UserMenu.values()[num];
    }
}
