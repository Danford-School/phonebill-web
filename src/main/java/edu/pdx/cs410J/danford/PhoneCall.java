package edu.pdx.cs410J.danford;

import edu.pdx.cs410J.AbstractPhoneCall;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhoneCall extends AbstractPhoneCall{


    /**
     * This is the PhoneCall class. It extends the abstract version.
     * It has many members.
     */

    //These are the members that hold all of the pertinent information about the phone call.
    //I should probably make them private. Or if protected is a thing in Java
    String theCustomer;
    String theCustomersNumber;
    String isCallingThisNumber;
    String startTimeAndDate;
    String endTimeAndDate;
    Date dateFormattedStart;
    Date dateFormattedEnd;

    /**
     * This is the phone call constructor for when you have all of the information. This should be the primary, probably.
     * All fields except the name go through a check.
     * @param Customer The customer's name
     * @param caller The customer's phone number
     * @param callee The number the customer is calling
     * @param startDate The date of when the phone call begins
     * @param startTime The time of when the phone call begins
     * @param startTimeAMPM this is the AM/PM associated with the startTime
     * @param endDate The date of when the phone call ends
     * @param endTime The time of when the phone call ends
     * @param endTimeAMPM The AM/PM associated with the end time
     */
    public PhoneCall(String Customer, String caller, String callee, String startDate, String startTime, String startTimeAMPM, String endDate, String endTime, String endTimeAMPM) throws ParseException {
        this.theCustomer = Customer;
        this.theCustomersNumber = phoneNumberCheck(caller);
        this.isCallingThisNumber = phoneNumberCheck(callee);
        this.startTimeAndDate = fakeDateString(dateChecker(startDate), timeChecker(startTime), checkAMPM(startTimeAMPM));
        this.endTimeAndDate = fakeDateString(dateChecker(endDate), timeChecker(endTime), checkAMPM(endTimeAMPM));
        this.dateFormattedStart = makeDateObjectFromString(this.startTimeAndDate);
        this.dateFormattedEnd = makeDateObjectFromString(this.endTimeAndDate);
    }

    public PhoneCall(String caller, String callerNumber, String callee, String startTimeAndDate, String endTimeAndDate) throws ParseException {
        this.theCustomer = caller;
        this.theCustomersNumber = callerNumber;
        this.isCallingThisNumber = callee;
        this.startTimeAndDate = startTimeAndDate;
        this.endTimeAndDate = endTimeAndDate;
        this.dateFormattedStart = makeDateObjectFromString(this.startTimeAndDate);
        this.dateFormattedEnd = makeDateObjectFromString(this.endTimeAndDate);
    }

    /**
     * This is the empty phone call constructor. It initializes all of the fields to null
     */
    public PhoneCall() {
        this.theCustomer = null;
        this.theCustomersNumber = null;
        this.isCallingThisNumber = null;
        this.startTimeAndDate = null;
        this.endTimeAndDate = null;
        this.dateFormattedStart = null;
        this.dateFormattedEnd = null;
    }

    /**
     * This returns the number of the customer that is calling
     * @return This is the phone number of the customer
     */
    @Override
    public String getCaller() {
        return this.theCustomersNumber;
    }

    /**
     * This returns the number that the customer is calling.
     * @return The is the number the customer is calling
     */
    @Override
    public String getCallee() {
        return this.isCallingThisNumber;
    }

    /**
     * This returns the start time, date and am/pm of the phone call.
     * @return Returns "startDate, startTime am/pm
     */
    @Override
    public String getStartTimeString() {
        return this.startTimeAndDate;
    }

    /**
     * This returns the end date, time am/pm.
     * @return Returns startDate, startTime am/pm
     */
    @Override
    public String getEndTimeString() {
        return this.endTimeAndDate;
    }

    /**
     * This makes a Date object from a string that contains the date, time and ampm in that order.
     * @param stringDate The string of the date and time
     * @return A date object of the date and time
     * @throws ParseException True.
     */
    public Date makeDateObjectFromString(String stringDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy, hh:mm a");
        Date date = formatter.parse(stringDate);
        return date;
    }

    /**
     * I had some issues with making a Date object, so this is my temporary work-around. Sorry, I'll fix it for the next
     * iteration of the program. This does have the correct output.
     * @param date Date of call in question
     * @param time Time of call in question
     * @param AMPM am/pm of the time of the call in question
     * @return a string with all of the above as a string.
     */
    public String fakeDateString(String date, String time, String AMPM) {
        return date.concat(", ").concat(time).concat(" ").concat(AMPM);
    }

    /**
     * This checks to ensure the phone number is in the correct format using regex.
     * It returns the phone number or an IllegalArgumentException
     * @param aPhoneNumber A string of the phone number that is being tested
     * @return This returns the phone number on success.
     */
    public static String phoneNumberCheck(String aPhoneNumber) {
        if (!aPhoneNumber.matches("\\d{3}-\\d{3}-\\d{4}")) {
            System.out.println("\nThe phone number is not in the correct format.\nPlease use ###-###-####\n");
            throw new IllegalArgumentException();
        }
        return aPhoneNumber;
    }

    /**
     * This checks to ensure the date is in the correct format using regex.
     * It returns the number if it is acceptable, or an IllegalArgumentException
     * @param date A string of the date being checked
     * @return Returns the phone number if it passes
     */
    public static String dateChecker(String date) {
        if(!date.matches("^[0-3]?[0-9]/[0-3]?[0-9]/(?:[0-9]{2})?[0-9]{2}$")) {
            System.out.println("\nThe date is not valid.\nPlease use the form mm/dd/yyyy, m/dd/yyyy, mm/d/yyyy, or m/d/yyyy\n");
            throw new IllegalArgumentException();
        }
        return date;
    }

    /**
     * This checks to ensure the time is in the correct format using regex
     * It returns the date or an IllegalArgumentException()
     * @param time A string of the time that is being checked
     * @return Returns the time if it passes the test.
     */
    public static String timeChecker(String time) {
        if (!time.matches("(1[012]|[1-9]):[0-5][0-9]")) {
            System.out.println("\nThe time is not valid.\nPlease use the form hh:mm or h:mm\n");
            throw new IllegalArgumentException();
        }
        return time;
    }

    /**
     * This takes a PhoneCall and makes it readable. This is for Project 3 and I just made it because I was here.
     * @return This is the readable string from PhoneCall.
     */
    public String phoneCallToOutput() {
        return this.theCustomer + " at " + this.theCustomersNumber + " called " + this.isCallingThisNumber +
                " starting on " + this.startTimeAndDate +
                ", and ending on " + this.endTimeAndDate + ".";
    }

    /**
     * This just checks to ensure that AM or PM has been selected.
     * @param AMPM The AM or PM field of a call
     * @return Returns the string if it is correct
     */
    public static String checkAMPM(String AMPM) {
        if(AMPM.contains("AM") | AMPM.contains("am") | AMPM.contains("PM") | AMPM.contains("pm")) {
            return AMPM.toLowerCase();
        } else {
            System.err.println("AM or PM required (Case insensitive)");
            throw new IllegalArgumentException();
        }
    }
}
