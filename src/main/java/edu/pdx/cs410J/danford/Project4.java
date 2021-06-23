package edu.pdx.cs410J.danford;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.ParserException;

import java.io.*;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


/**
 * The main class that parses the command line and communicates with the
 * Phone Bill server using REST.
 */
public class Project4 {

    /**
     * This doesn't do much. doAllTheThings() is what you are afger
     * @param args the command line arguments
     * @throws IOException Yes it does
     * @throws ParserException Sometimes
     */
    public static void main(String... args) throws IOException, ParseException {

        if(args.length == 0) {
            System.out.println("There were no arguments!");
            System.exit(1);
        }

        doAllTheThings(args);

        System.exit(0);
    }

    /**
     * this makes error messages?
     * @param message A message?
     */
    @VisibleForTesting
    protected static void error( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);

        System.exit(1);
    }

    /**
     * This replaces main for testing purposes, but it was less effective with this project.
     * @param args The command line arguments.
     * @throws IOException When IO doesn't work
     * @throws ParserException  When parsing doesn't work
     */
    public static void doAllTheThings(String[] args) throws IOException, ParseException {

        String customer = null;

        /* Arguments to help my brain:
        Options:
        0. -README: shows readme and exits
        1 & 2 [option] -host hostname
        3 & 4 [option] -port port #
        13 [option] -print
        5-12 -search(5) customerName(6) startDate(7) startTime(8) StartAMPM(9) endDate(10) endTime(11) endAMPM(12)
        OR
        5-6 -search(5) customerName(6)
        --------------
        Required fields:
        1. Customer's name (string)

        Maybe Required fields:
        2. Customer's number
        3. Callee's number
        4 & 5 & 6. start Date, time, AM/PM
        7 & 8 & 9. end date, time, AM/PM

        We will not be adding when we are searching. So with adding it is up to 14, minimum 13
        When we are searching it is either 12 or 6
         */


        //Check for README first
        checkForReadme(args);

        //This checks for -print and exits if true
        Boolean shouldPrint = shouldIPrint(args);
        Boolean shouldSearch = checkSearch(args);
        String portString = getPort(args);
        String hostName = getHost(args);

        if (hostName == null) {
            System.err.println("Must provide host name!");

        } else if ( portString == null) {
            System.err.println("Must provide a port number!");
        }

        //The total argument length is already checked as is -README. In this case the program has not errored out
        // and the count is within range. So if shouldPrint has not been selected and there is no file name then
        //there is something weird in the arguments.
        int check = 0;
        if(shouldPrint) { check++; }
        if(portString != null) { check +=2; }
        if(hostName != null) { check +=2; }
        if(shouldSearch) {check++; }

        if(args.length <= check) {
            System.out.println("Missing command line arguments.");
            System.exit(1);
        }

        int port = 0;
        try {
            port = Integer.parseInt( portString );

        } catch (NumberFormatException ex) {
            System.err.println(String.format("Portnumber %s must be an integer!%n", portString));
            System.exit(1);
        }
        if(port == 0 ) {
            System.out.println("You must give a port number!");
            System.exit(1);
        }

        PhoneBillRestClient client = new PhoneBillRestClient(hostName, port);

        try {
             if (args.length == check +1) {
                 try {
                     System.out.println(client.getPhoneBill(args[check]));
                 } catch ( IOException ex) {
                     System.exit(1);
                 }
             } else if (args.length == check + 3){
                 System.out.println(client.searchPhoneBill(args[check], args[++check], args[++check]));

             } else if (args.length == check + 9){
                PhoneCall call = createCall(args, check);
                client.addPhoneCall(call.theCustomer, call);
                 //If -print was selected, this prints
                 if(shouldPrint) {
                     System.out.println(call.phoneCallToOutput());
                 }
            }

        } catch (IOException | ParseException ex) {
            error("While contacting server: " + ex);
            return;
        }
    }

    /**
     * This method populates the PhoneCall argument that it is given. It takes the command line arguments, a blank
     * Phonecall, and the count from the checkArgsLength which does some checking and returns the count of arguments.
     * This count is modulo 9 to get where we start populating the call.
     * My Reasoning:
     * if there are 9, then it is just the arguments, and if they are incorrect the input checks will error them out
     * If there are 10-14 then move down appropriately with modulo
     * If there are 15, it would include -README so it shouldn't get here.
     * If there was a random option, it should be caught before this is called.
     * @param args The command line arguments
     * @param check The
     */
    public static PhoneCall createCall(String[] args, int check) throws ParseException {
        PhoneCall call = new PhoneCall();
        int i = check;
        call.theCustomer = args[i];
        call.theCustomersNumber = phoneNumberCheck(args[++i]);
        call.isCallingThisNumber = phoneNumberCheck(args[++i]);
        String thisIsTheStartDate = dateChecker(args[++i]);
        String thisIsTheStartTime = timeChecker(args[++i]);
        String startTimeAMPM = checkAMPM(args[++i]);
        String thisIsTheEndDate = dateChecker(args[++i]);
        String thisIsTheEndTime = timeChecker(args[++i]);
        String endTimeAMPM = checkAMPM(args[++i]);
        call.startTimeAndDate = call.fakeDateString(thisIsTheStartDate, thisIsTheStartTime,
                startTimeAMPM);
        call.endTimeAndDate = call.fakeDateString(thisIsTheEndDate, thisIsTheEndTime,
                endTimeAMPM);
        call.dateFormattedStart = call.makeDateObjectFromString(call.startTimeAndDate);
        call.dateFormattedEnd = call.makeDateObjectFromString(call.endTimeAndDate);
        checkTimeOrder(call.dateFormattedStart, call.dateFormattedEnd);

        return call;
    }



    public static void checkTimeOrder(Date start, Date end) {
            if(end.before(start) || start.after(end)) {
            throw new IllegalArgumentException("The beginning must happen before the end!");
        }
    }

    /**
     * this gets the argument after the -port option. Hopefully it is a number. That is checked later.
     * @param args The command line arguments
     * @return Returns the string of whatever is afte the -port argument.
     */
    public static String getPort(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("-port")) {
                return args[++i];
            }
        }
        return null;
    }

    /**
     * Gets the string of whatever is after -host option.
     * @param args command line arguments
     * @return the string of whatever is after the -host option
     */
    public static String getHost(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("-host")) {
                return args[++i];
            }
        }
        return null;
    }

    /**
     * Checks to see if the phonebill database should be searched. Sends back a true if so. ALso checks to ensure
     * that there are more arguments after the option.
     * @param args command line arguments
     * @return boolean dependant on if the -search option is selected.
     */
    public static Boolean checkSearch(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("-search")) {
                if(args[++i] == null) {
                    System.err.println("Must provide search criteria");
                    System.exit(1);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * This checks to see if -print option was selected. If so it changes the shouldPrint boolean to true.
     * @param args the command line arguments
     * @return Returns the shouldPrint boolean
     */
    public static boolean shouldIPrint(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("-print")) {
                return true;
            }
        }
        return false;
    }

    /**
     * This just checks for the README flag.
     * If it is there it prints the README and exits the program.
     * @param args The command line arguments
     */
    public static void checkForReadme(String[] args) {
        for (String arg : args) {
            if (arg.contains("-README")) {
                printReadMe();
                System.exit(0);
            }
        }
    }


    /**
     * This checks to ensure the phone number is in the correct format using regex. If it fails it throws
     * an IllegalArgumentException. Phone numbers are hard, so it really returns any phone number.
     * @param aPhoneNumber a sting that is a phone number in the format ###-###-####
     * @return returns the phone number if it matches the regex
     */
    public static String phoneNumberCheck(String aPhoneNumber) {
        if (!aPhoneNumber.matches("\\d{3}-\\d{3}-\\d{4}")) {
            System.err.println("\nThe phone number is not in the correct format.\nPlease use ###-###-####\n");
            throw new IllegalArgumentException();
        }
        return aPhoneNumber;
    }

    /**
     * This checks the date to ensure it matches the format of #/#/#### or ##/#/#### or #/##/#### or ##/##/####
     * @param date this is a string of the date
     * @return this returns the date on success.
     */
    public static String dateChecker(String date) {
        if(!date.matches("^[0-3]?[0-9]/[0-3]?[0-9]/(?:[0-9]{2})?[0-9]{2}$")) {
            System.err.println("\nThe date is not valid.\nPlease use the form mm/dd/yyyy, m/dd/yyyy, mm/d/yyyy, or m/d/yyyy\n");
            throw new IllegalArgumentException();
        }
        return date;
    }

    /**
     * This just checks to ensure that AM or PM has been selected.
     * @param AMPM The AM or PM field of a call
     * @return Returns the string in lower case if it is correct
     */
    public static String checkAMPM(String AMPM) {
        if(AMPM.contains("AM") | AMPM.contains("am") | AMPM.contains("PM") | AMPM.contains("pm")) {
            return AMPM.toLowerCase();
        } else {
            System.err.println("AM or PM required (Case insensitive)");
            throw new IllegalArgumentException();
        }
    }

    /**
     * This takes a time String and checks the format.
     * @param time This is the time string that was extracted based on placement.
     * @return  This returns the time as long as it is in the correct format.
     */
    public static String timeChecker(String time) {
        if (!time.matches("(1[012]|[1-9]):[0-5][0-9]")) {
            System.err.println("\nThe time is not valid.\nPlease use the form hh:mm or h:mm in 12-hour format\n");
            throw new IllegalArgumentException();
        }
        return time;
    }

    /**
     * This prints the README file if that option is selected
     */
    public static void printReadMe() {

        try {
            File theReadme = new File("README.txt");
            Scanner reader = new Scanner(theReadme);
            while(reader.hasNextLine()) {
                String line = reader.nextLine();
                System.out.println(line);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("The README was not found! :(");
        }
    }
}