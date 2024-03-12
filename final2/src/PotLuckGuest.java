/**
 * This class creates PotLuckGuest objects and extends Guest. It also implements Welcome.
 *
 * Additional instance variables:
 * String itemsBringing to store what things the Guest will bring to the party.
 *
 * author: Katelyn Le
 * kble@usc.edu
 *
 */

public class PotLuckGuest extends Guest implements Welcome{
    // instance variables
    private String itemsBringing;

    // constructor
    PotLuckGuest(String code, PartyType type, String fName, String lName, String email, String confirmation, String itemsBringing) {
        super(code, type, fName, lName, email, confirmation);
        this.itemsBringing = itemsBringing;
    }

    @Override
    public String getFileString() {
        return this.getCode() + "#" + PartyType.getIndexFromOption(this.getType()) + "#" + this.getfName() + "#" +
                this.getlName() + "#" + this.getEmail() + "#" + this.getConfirmation() + "#" + this.itemsBringing;
    }
}
