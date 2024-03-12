/**
* This class creates Guest objects and implements Welcome.
 * It contains all the basic Guest information such as the party they belong to and basic contact info.
* 
* Author: Katelyn Le (coffee)
* kble@usc.edu
* 
*/

public abstract class Guest implements Welcome {
    // instance variables
    private String code;
    private PartyType type;
    private String fName;
    private String lName;
    private String email;
    private String confirmation;
    private Savior savior;

    // constructor
    Guest(String code, PartyType type, String fName, String lName, String email, String confirmation) {
        this.code = code;
        this.type = type;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.confirmation = confirmation;
        this.savior = new Savior();
    }

    // toString
    public String toString() {
        return this.fName + " " + this.lName + "(" + this.email + ")";
    }

    // confirmation string
    public String confirmationString() {
        return this.fName + " " + this.lName + "(" + this.email + "): " + confirmation;
    }

    @Override
    public void welcomeMessage() {
        System.out.println("Welcome to the party, " + this.fName + "! We can't wait to see you.");
    }

    // file string
    public abstract String getFileString();

    // code accessor
    public String getCode() {
        return this.code;
    }

    // type accessor
    public PartyType getType() {
        return this.type;
    }

    // fName accessor
    public String getfName() {
        return this.fName;
    }

    // lName accessor
    public String getlName() {
        return this.lName;
    }

    // email accessor
    public String getEmail() {
        return this.email;
    }

    // confirmation accessor
    public String getConfirmation() {
        return this.confirmation;
    }
}
