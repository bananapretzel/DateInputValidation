import java.nio.charset.StandardCharsets;
import java.util.*;


/**
 * A simple program to check whether a date entered is between the years 1753 and 3000.
 *
 * @author Casey Cotton
 */
public class Dates {

    /** Container for day. */
    private int day = -1;
    /** Container for year. */
    private int year = -1;
    /** Container for month represented as an integer. */
    private int monthInt = -1;
    /** The separator used. */
    private char separator = '*';
    /** An array of the input divided into separate elements by the separator used. */
    private String[] split = new String[2];
    /** Container for the user's input */
    private String input;
    /** An array for the months of the Gregorian calendar represented in their first three letters. */
    private static final String[] MONTH = {"", "jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct",
            "nov", "dec"};

    /**
     * Start point of the program.The program will only take " ", "-" or "/" as a separator and only one type can be
     * used per date.
     * Valid input is: dd or d or 0d
     * mm or m or 0m
     * yy or yyyy
     *
     * @param args command line arguments are not used.
     */
    public static void main(String[] args) {

        Dates date = new Dates();
        Scanner scan = new Scanner(System.in, StandardCharsets.UTF_8);
        System.out.println("Enter a date:");


        while (scan.hasNext()) {
            // sets the fields to empty/default values.
            date.setDefault();


            date.input = scan.nextLine();
            if ((!date.input.equals(date.input.trim()))) {
                error(date.input,19);
                continue;
            }
            date.input = date.input.trim();
            // checks whole input if there is a space used


            // checks to see if correct separator is used.
            if (!date.validateSeparator()) {
                continue;
            }

            //splits the input with the corresponding separator.
            date.split = date.input.split(String.valueOf(date.separator));

            // checks to see if the seperated string using the separator consists of three elements.
            if (!date.isThreeValues(date.input)) {
                error(date.input, 2);
                continue;
            }

            // a check for leading zeroes for the day value.
            if (date.split[0].length() >= 3) {
                error(date.input,12);
                continue;
            }

            // checks if the day and year can be parsed as integers.
            if (!date.parseDayAndYearAsInt()) {
                error(date.input, 4);
                continue;
            }

            // a check to see if the year is in the correct yy or yyyy format.
            if (!(date.split[2].length() == 2 || date.split[2].length() == 4)) {
                error(date.input, 14);
                continue;
            }



            /* checks if the month and day entered is in an acceptable format and also checks for leap years, giving
               February an increased day if necessary. */
            if (!date.monthChecker(date.split[1]) || !date.dayAndLeapYearChecker(date.day, date.monthInt, date.year)) {
                continue;
            }

            // finally checks the year range and accommodates if yy or yyyy is used.
            if (!date.validateDateRange()) {
                error(date.input, 5);
            }
        }
    }

    /**
     * Checks if s is three characters long, if it is passes the method will try to parse s as an integer. If it can't,
     * it will check s to see if it is a valid three letter abbreviation.
     *
     * @param s the month string in the split variable which should be split[1]
     * @return true if s passes all the checks, else false.
     */
    private boolean monthChecker(String s) {
        if (s.length() > 3) {
            error(input, 11);
            return false;
        }
        try {
           if (s.length() == 3) {
               Integer.parseInt(s);
               error(input, 13);
               return false;
           }

        } catch (NumberFormatException ignored) {
        }
        try {
            if (Integer.parseInt(s) > 0 && Integer.parseInt(s) <= 12) {
                monthInt = Integer.parseInt(split[1]);
            }
        } catch (NumberFormatException e) {
            if (!(isMonthCaseCorrect(split[1]))) {
                error(input,6);
                return false;
            }
            String monthLower = split[1].toLowerCase();
            for (int i = 1; i < MONTH.length; i++) {
                if (MONTH[i].equals(monthLower)) {
                    monthInt = i;
                    break;
                }
            }
        }
        if (monthInt == -1) {
            error(input, 6);
            return false;
        }
        return true;
    }

    /**
     * The method will check if the day is between 0 and 31, if it is it will then check if the month's day is capped
     * at 30 or 31. If the month is identified as February a long-winded check is made to see if the year is a leap year
     * and whether february needs to be capped at 28 or 29 days.
     *
     * @return true if the day is acceptable given the month and year, else false.
     */
    private boolean dayAndLeapYearChecker(int day, int monthInt, int year) {
        boolean leapYear = false;

        if (day > 31 || day < 1) {
            error(input, 7);
            return false;
        }
        if (monthInt == 9 || monthInt == 4 || monthInt == 6 || monthInt == 11) {
            if (day > 30) {
                error(input, 8);
                return false;
            }
        } else if (monthInt == 2) {
            if (year > 1582) {
                if (year % 4 == 0) {
                    if (!(year % 100 == 0)) {
                        leapYear = true;
                    } else if (year % 400 == 0) {
                        leapYear = true;
                    }
                }
            }
            if (leapYear) {
                if (day > 29) {
                    error(input, 9);
                    return false;
                }
            } else if (day > 28) {
                error(input, 10);
                return false;
            }
        }
        return true;
    }

    /**
     * Method used to hold all the currently identifiable errors due to user input.
     *
     * @param errorCode the integer represents which statement to print out.
     */
    private static void error(String s, int errorCode) {
        switch (errorCode) {
            case 1:
                System.out.println(s + " - INVALID: There was a problem identifying the separators used.");
                break;
            case 2:
                System.out.println(s + " - INVALID: Only three values seperated by the same \"-\", \"/\", " +
                        "or \" \" are allowed.");
                break;
            case 3:
                System.out.println(s + " - INVALID: year must be either two or four digits.");
                break;
            case 4:
                System.out.println(s + " - INVALID: Either the day or year could not be parsed as integers. " +
                        "Please check your input.");
                break;
            case 5:
                System.out.println(s + " - INVALID: Year out of range.");
                break;
            case 6:
                System.out.println(s + " - INVALID: Please check the month entered.");
                break;
            case 7:
                System.out.println(s + " - INVALID: The day value needs to be between 1 and 31.");
                break;
            case 8:
                System.out.println(s + " - INVALID: The day is beyond the limit of the specified month.");
                break;
            case 9:
                System.out.println(s + " - INVALID: Impossible day due to leap year.");
                break;
            case 10:
                System.out.println(s + " - INVALID: Day value is out of range for February.");
                break;
            case 11:
                System.out.println(s + " - INVALID: Please check if you have entered the month in the correct " +
                        "format.");
                break;
            case 12:
                System.out.println(s + " - INVALID: d, 0d or dd are the only acceptable formats for the day value.");
                break;
            case 13:
                System.out.println(s + " - INVALID: m, 0m or mm are the only acceptable formats for the month value.");
                break;
            case 14:
                System.out.println(s + " - INVALID: yy yyyy are the only acceptable formats for the year value.");
                break;
            case 15:
                System.out.println(s + " - INVALID: There are too many values entered.");
                break;
            case 16:
                System.out.println(s + " - INVALID: There are too few values entered.");
                break;
            case 17:
                System.out.println(s + " - INVALID: Only one unique separator is allowed per date entered. " +
                        "Please check your input.");
                break;
            case 18:
                System.out.println(s + " - INVALID: You need two separators for a correct date format. " +
                        "Please check your input.");
                break;
            case 19:
                System.out.println((s + " - INVALID: Please check your input. leading or trailing white spaces were detected."));
        }
    }

    /**
     * Sets the object's variables to their default values.
     *
     */
    private void setDefault() {
        day = -1;
        monthInt = -1;
        year = -1;
        separator = '*';
        split = null;
    }

    /**
     * A method used to check if the separator used is within the correct format.
     *
     * @return true if a valid separator is used otherwise, false.
     */
    private boolean validateSeparator() {
        int numSeparators = 0;
        int numValues = 0;
        String[] primitiveSplit;
        List<String> split = new ArrayList<>();
        List<Character> separatorArray = new ArrayList<>();
        separatorArray.add('/');
        separatorArray.add(' ');
        separatorArray.add('-');

        /* checks to see which separator was used by comparing it to the separator array. */
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == separatorArray.get(0) || input.charAt(i) == separatorArray.get(1) ||
                    input.charAt(i) == separatorArray.get(2)) {
                separator = input.charAt(i);
                /* removes the first recognised separator from the array so now the array can be used to check if more
                than one valid separator was used.
                 */
                separatorArray.remove(Character.valueOf(separator));
                break;
            }
        }
        for (Character s : input.toCharArray()) {
            if (separatorArray.contains(s)) {
                error(input,17);
                return false;
            }
        }

        // checks how many separators were used, anything that is not 2 is rejected
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == separator) {
                numSeparators++;
            }
        }
       primitiveSplit = input.split("[ -/]");
        Collections.addAll(split, primitiveSplit);
        while (split.contains("")) {
            split.remove("");
        }
        numValues = primitiveSplit.length;
        if (split.size() > 3) {
            if (numSeparators > 2 && numValues <= 3) {
                error(input, 18);
            } else {
                error(input, 15);
            }
            return false;
        } else if (split.size() < 3) {
            error(input,16);
            return false;
        }
        if (numSeparators != 2) {
            // if there are two unique separators used then return false
            for (char c : input.toCharArray()) {
                if (separatorArray.contains(c)) {
                    error(input, 17);
                    break;
                }
            }
            error(input,18);
            return false;
        }


        /* if a separator isn't found then output error. */
        if (separator == '*') {
            error(input, 1);
            return false;
        }
        return true;
    }

    /**
     * Checks to see if the year value is between the year 1753 and 3000. It will print out the date in dd mmm yyyy
     * format if true.
     *
     * @return true if the year is between the years of 1753 and 3000 otherwise, false.
     */
    private boolean validateDateRange() {
        /* checks if year is 2 digits. */
        if (split[2].length() == 2) {
            if (year >= 50) {
                year = 1900 + Integer.parseInt(split[2]);
            } else {
                year = 2000 + Integer.parseInt(split[2]);
            }
        }

        /* checks if year is between specified range or not. */
        if (year <= 3000 && year >= 1753) {
            System.out.println("SUCCESS: " + String.format("%02d", day) + " " +
                    MONTH[monthInt].substring(0, 1).toUpperCase() +
                    MONTH[monthInt].substring(1) + " " + year);
            return true;
        }
        return false;
    }

    /**
     * A simple check to see if the seperated input is three elements long or not.
     *
     * @return true if date.split contains three elements, else false.
     */
    private boolean isThreeValues(String s) {
        int count;
        List<String> split = new ArrayList<>();
        String[] primitiveSplit = s.split("[ -/]");
        Collections.addAll(split, primitiveSplit);
        split.remove("");
        /* checks whether three values have been entered. */
        count = split.size();
        return count == 3;
    }

    /**
     * Helper method to check if the day and year input from the user can be converted to integers. Adds them to the day
     * and year variable if it can.
     * @return true if parsable as integers else, false.
     */
    private boolean parseDayAndYearAsInt() {
        /* checks if day and year are numbers by inserting them into int variables */
        try {
            day = Integer.parseInt(split[0]);
            year = Integer.parseInt(split[2]);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * A method which takes in a 3 character string and determines whether its format is all uppercase, all lowercase
     * or the first character uppercase and the rest lowercase.
     * @param s the string to be checked
     * @return true if it conforms, else false.
     */
    private boolean isMonthCaseCorrect(String s) {

        // if the last char is uppercase then they have to all be uppercase or else fail
        if (s.charAt(2) >= 65 && s.charAt(2) <= 90) {
            boolean isFirstUpper = s.charAt(0) >= 65 && s.charAt(0) <= 90;
            boolean isSecondUpper = s.charAt(1) >= 65 && s.charAt(1) <= 90;
            return isFirstUpper && isSecondUpper;
            // else if first char is lowercase then they all have to be lower or else fail
        } else if (s.charAt(0) >= 97 && s.charAt(0) <= 122){
            boolean isFirstLower = s.charAt(1) >= 97 && s.charAt(1) <= 122;
            boolean isSecondLower = s.charAt(2) >= 97 && s.charAt(2) <= 122;
            return isFirstLower && isSecondLower;
            // else check if first letter is upper and rest is lower
        } else return s.charAt(0) >= 65 && s.charAt(0) <= 90 && s.charAt(1) >= 97 && s.charAt(1) <= 122 &&
                s.charAt(2) >= 97 && s.charAt(2) <= 122;
    }
}