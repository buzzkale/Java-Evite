/**
 * This enum lists all the Party types. It contains additional methods to get the enum value from an index.
 *
 * Author: Katelyn Le (coffee)
 * kble@usc.edu
 *
 */

public enum PartyType {
    BIRTHDAY("Birthday"),
    REUNION("Reunion"),
    DINNER("Dinner party"),
    POTLUCK("Potluck meal"),
    WEDDING("Wedding"),
    HOLIDAY("Holiday celebration");

    // variable
    private String choice;

    // constructor
    PartyType(String choice) {
        this.choice = choice;
    }

    // to string
    public String toString() {
        return this.choice;
    }

    // print types
    public static void printMenu() {
        int optionNum = 0;
        for (PartyType options : PartyType.values()) {
            System.out.println(optionNum + ": " + options);
            optionNum += 1;
        }
    }

    // turn number into PartyType option
    public static PartyType getOptionFromIndex(int num) {
        return PartyType.values()[num];
    }

    // get index number string from PartyType
    public static String getIndexFromOption(PartyType type) {
        String index = null;
        for (int i = 0; i < PartyType.values().length; i++) {
            if (type == PartyType.values()[i]) {
                index = Integer.toString(i);
            }
        }
        return index;
    }

    // get enum value from string index
    public static PartyType getEnumFromDesc(String index) {
        int typeIndex = Integer.valueOf(index);
        PartyType type = PartyType.getOptionFromIndex(typeIndex);
        return type;
    }
}
