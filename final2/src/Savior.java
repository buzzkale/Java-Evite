import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.time.*;


/**
 * This class is meant to serve ITP 265 students as a help for getting input and error checking on input, but
 * may also be used as a general purpose class to store methods which may be needed across lots of projects.
 *
 * @author Kendra Walther and TEA class
 * @version Fall 2022
 */
public class Savior{
    private Scanner sc; //declare

    public Savior(){
        sc = new Scanner(System.in); // initialize
    }
    /**
     * Prompt the user and read one word of text as a String
     * @param prompt: the question to ask the user
     * @return: a one word String - if the user enters multiple words, all other input until the return will be discarded.
     */
    public String inputWord(String prompt) {
        System.out.print(prompt + "\n>");
        String word = sc.next();
        sc.nextLine(); // remove any "garbage" (like extra whitespace or the return key) after the one word that is read in
        return word;
    }
    /**
     * Prompt the user and read one line of text as a String
     * @param prompt: the question to ask the user
     * @return: a line of user input (including spaces, until they hit enter)
     */
    public String inputLine(String prompt) {
        System.out.print(prompt + "\n>");
        return sc.nextLine();
    }

    /**
     * Prompt the user and read one word of text as a String, returns a String that matches
     * one allowed matching words passed in as parameters (case sensitive)
     * @param prompt: the question to ask the user
     * @param match1: a word the input is allowed to be
     * @param match2: a word the input is allowed to be
     * @param match3: a word the input is allowed to be
     * @return: a one word  String that matches one of the three allowed words (case-sensitive)
     */
    public String inputWord(String prompt, String match1, String match2, String match3) {
        String word = inputWord(prompt);
        //TODO: finish this method, checking for matching words
        while (! (word.equalsIgnoreCase(match1) || word.equalsIgnoreCase(match2) || word.equalsIgnoreCase(match3))){
            System.out.printf("%s did not match the allowed choices: %s or %s or %s\n", word, match1, match2, match3);
            word = inputWord(prompt);
        }
        return word;
    }


    /**
     * Prompt the user and read an int, clearing whitespace or the enter after the number
     * @param prompt: the question to ask the user
     * @return: an int
     */
    public int inputInt(String prompt) {
        int num = 0;
        System.out.println (prompt);
        while(!sc.hasNextInt()){
            String garbage = sc.nextLine();
            System.out.println(garbage + " was not an int. Please enter a whole number");
        }
        num = sc.nextInt();
        sc.nextLine();//clear
        return num;
    }

    /**
     * Prompt the user and read an int between (inclusive) of minValue and maxValue, clearing whitespace or the enter after the number
     * @param prompt: the question to ask the user
     * @return: an int between minValue and maxValue
     */
    public int inputInt(String prompt, int minValue, int maxValue) {
        // get an int -- DRY principle don't repeat yourself
        int num = inputInt(prompt);
        while(num < minValue || num > maxValue){ // while number is bad
            // get new number
            System.out.println(num + " was not valid. please choose " + minValue + "  -  " + maxValue);
            num = inputInt(prompt);
        }
        // return good number
        return num;
    }

    /**
     * Prompt the user and read a floating point number, clearing whitespace or the enter after the number
     * @param prompt: the question to ask the user
     * @return: a double value
     */
    public double inputDouble(String prompt) {
        double num = 0;
        System.out.println (prompt);
        while(!sc.hasNextDouble()){
            String garbage = sc.nextLine();
            System.out.println(garbage + " was not a double. Please enter a floating point number");
        }
        num = sc.nextDouble();
        sc.nextLine();//clear
        return num;
    }

    /**
     * Prompt the user and read a floating point number between (inclusive) of min and max,
     * clearing whitespace or the enter after the number
     * @param prompt: the question to ask the user
     * @return: a double value
     */
    public double inputDouble(String prompt, double minValue, double maxValue) {
        // get an double -- DRY principle don't repeat yourself
        double num = inputDouble(prompt);
        while(num < minValue || num > maxValue){ // while number is bad
            // get new number
            System.out.println(num + " was not valid. please choose " + minValue + "  -  " + maxValue);
            num = inputDouble(prompt);
        }
        // return good number
        return num;
    }

    /**
     * Prompt the user and read a boolean value, clearing whitespace or the enter after the number
     * @param prompt: the question to ask the user
     * @return: a boolean value
     */
    public boolean inputBoolean(String prompt) {
        System.out.println(prompt);
        String currentVal = sc.next();
        while (!currentVal.equalsIgnoreCase("true") && !currentVal.equalsIgnoreCase("false")) {
            System.out.println(currentVal + " was not a valid input. " + prompt);
            currentVal = sc.next();
        }
        sc.nextLine();
        return currentVal.equalsIgnoreCase("true");
    }

    /**
     * Prompt the user enter yes or no (will match y/yes and n/no any case) and return true for yes and false for no.
     * @param prompt: the question to ask the user
     * @return: a boolean value
     */
    public boolean inputYesNo(String prompt) {

        String yesNo = inputWord(prompt).toLowerCase();

        while ( !  (yesNo.equals("yes") ||  yesNo.equals("y") ||
                yesNo.equals("no") ||  yesNo.equals("n"))) {
            System.out.println("Please give a yes or no answer");
            yesNo = inputWord(prompt).toLowerCase();
        }
        return yesNo.startsWith("y");

    }


    /**
     *Prompt the user what kind of date to enter, then prompt for each value in order to create the date
     * @return: A LocalDate object
     */
    public LocalDate inputDate(String prompt) {
        System.out.println(prompt);
        int year = inputInt("Enter year: ", 1900, 2050);
        int month = inputInt("enter month: ", 1, 12);
        // TODO to be smarter, look at how many days in month...
        int day = inputInt("Enter day:", 1, 31);
        return LocalDate.of(year, month, day);
    }
    /**
     * A shortcut to System.out.println
     * @param msg: the line to be output
     * @return: none
     */
    public void print(String msg){
        System.out.println(msg);
    }
    /**
     * A shortcut to System.out.println which will surround the message with some stars to make it stand out.
     * @param msg: the line to be output
     * @return: none
     */
    public void printFancy(String msg){
        System.out.println("********************************");
        System.out.println(msg);
        System.out.println("********************************");
    }

    /**
     * Reads from a file (given) and makes an ArrayList, where each item is a String that holds one line of the file.
     * @param fileName
     * @return a list with each (non-empty) line of the file as an item
     */
    public ArrayList<String> readFile(String fileName){
        ArrayList<String> data = new ArrayList<>();
        boolean finished = false;

        while(!finished) {
            try (FileInputStream fis = new FileInputStream(fileName);
                 Scanner scan = new Scanner(fis)) {
                while (scan.hasNextLine()) {
                    String line = scan.nextLine();
                    if (!line.isEmpty()) { //skip empty lines
                        data.add(line);
                    }
                }
                finished = true;
            }
            catch (FileNotFoundException e) {
                finished = false; // something went wrong!
                System.err.println("File not found: " + fileName);
                fileName = this.inputWord("Please enter a different fileName to try again");
            } // will loop again on new file
            catch (IOException e) {
                System.err.println("An IOException occurred. Can't recover, ending program. ");
                e.printStackTrace();
                System.exit(1); // exit the program completely.
            }
        }
        return data;
    }

    // turn ArrayList into CSV file string
    public String listToCSVString(ArrayList<String> list) {
        // iteration limit
        int size = list.size() - 1;

        String fileString = "";
        for (int i = 0; i < size; i++) {
            fileString += list.get(i) + ",";
        }
        fileString += list.get(size);
        return fileString;
    }

    // turn ArrayList into file string with delimiter of choice
    public String listToFileString(String delimiter, ArrayList<String> list) {
        // iteration limit
        int size = list.size() - 1;

        String fileString = "";
        for (int i = 0; i < size; i++) {
            fileString += list.get(i) + delimiter;
        }
        fileString += list.get(size);
        return fileString;
    }
}