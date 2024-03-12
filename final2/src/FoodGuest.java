import java.util.ArrayList;

/**
 * This class creates FoodGuest objects and extends Guest. It also implements Welcome.
 *
 * Additional instance variables: ArrayList<String> allergies to store what dietary restrictions the Guest has.
 *
 * author: Katelyn Le
 * kble@usc.edu
 *
 */

public class FoodGuest extends Guest implements Welcome{
    // instance variables
    private ArrayList<String> allergies;
    private Savior savior;

    // constructor
    FoodGuest(String code, PartyType type, String fName, String lName, String email, String confirmation, ArrayList<String> allergies) {
        super(code, type, fName, lName, email, confirmation);
        this.allergies = allergies;
        savior = new Savior();
    }

    @Override
    public String getFileString() {
        return this.getCode() + "#" + PartyType.getIndexFromOption(this.getType()) + "#" + this.getfName() + "#" +
                this.getlName() + "#" + this.getEmail() + "#" + this.getConfirmation() + "#" +
                savior.listToFileString("%", allergies);
    }

    // accessor
    public ArrayList<String> getAllergies() {
        return this.allergies;
    }
}
