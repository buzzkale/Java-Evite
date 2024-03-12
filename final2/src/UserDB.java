import java.io.*;
import java.util.*;

/**
* This class creates the UserDB, which reads and writes User objects from and to files. It contains the User parser.
* 
* Author: Katelyn Le (coffee)
* kble@usc.edu
* 
*/

public class UserDB {
    // instance variables
    private Map<String, User> allUsers;

    // initialize allUsers
    public UserDB() {
        allUsers = new HashMap<>();
    }

    // add new User
    public void newUser(String fName, String lName, String email, String password) {
        User u = new User(fName, lName, email, password);
        allUsers.put(u.getEmail(), u);
    }

    public void addUser(User u) {
        allUsers.put(u.getEmail(), u);
    }

    // get all User objects in list
    public List<User> getUserList() {
        Collection<User> usersCollection = allUsers.values();

        return new ArrayList<>(usersCollection);
    }

    // parsing : taken from UserDatabase.java in A11
    public User parseUsers(String line) {
        User u = null;
        // make User object if all data is filled in
        try {
            Scanner sc = new Scanner(line);
            sc.useDelimiter("\t");
            String fName = sc.next();
            String lName = sc.next();
            String email = sc.next();
            String password = sc.next();

            u = new User(fName, lName, email, password);
        }
        // catch block for if User cannot be created
        catch (Exception e){
            System.err.println("Error while parsing line: " + line);
        }
        return u;
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
                User u = parseUsers(line);
                if (u != null) {
                    addUser(u);
                }
            }
        // catch block for FileNotFoundException; print a message that file couldn't be read and start with an empty Map database
        } catch (FileNotFoundException e) {
            System.err.println("File not found; try using the terminal. Creating a new empty Map database.");
            allUsers = new HashMap<>();
        // I/O exception; print a message that file couldn't be read and start with an empty Map database
        } catch (IOException e) {
            System.err.println("File access unsuccessful; try using the terminal. Creating a new empty Map database.");
            allUsers = new HashMap<>();
        }
    }

    // writes to a new .txt file : taken from UserDatabase.java in A11
    public void saveAll(String fileName){
        //write to file
        File f = new File(fileName);
        // write each line
        try(PrintWriter out = new PrintWriter(f)){
            for (User u: getUserList()){
                out.println(u.getFileString());
            }
        // catch block if file name cannot be located
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // check for existing email
    public boolean checkExistingEmail(String email) {
        Set<String> emails = allUsers.keySet();
        if (emails.contains(email)) {
            return true;
        }
        else {
            return false;
        }
    }

    // login attempt
    public boolean login(String email, String password) {
        Set<String> emails = allUsers.keySet();
        if (emails.contains(email)) {
            if (allUsers.get(email).checkPassword(password)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    // get specific User object
    public User getUser(String email) {
        return allUsers.get(email);
    }
}
