package edu.pdx.cs410J.danford;

import edu.pdx.cs410J.AbstractPhoneBill;

import java.util.*;

/**
 * This is the PhoneBill class. It has a TreeSet to hold phonecalls.
 */
public class PhoneBill extends AbstractPhoneBill<PhoneCall>{
    public String customer;
    public Collection<PhoneCall> calls = new TreeSet<>(new Sorter());

    /**
     * constructor with a customer
     * @param customer the customer whose phone bill is being created
     */
    public PhoneBill(String customer){
        this.customer = customer;
    }

    /**
     * empty constructor
     */
    public PhoneBill() { this.customer = null; }

    /**
     * This adds a phone call.
     * @param call the call that gets added
     */
    @Override
    public void addPhoneCall(PhoneCall call) {
        this.calls.add(call);
    }

    /**
     * This returns the customer's name. I should made the member private
     * @return returns the name of the customer.
     */
    @Override
    public String getCustomer() {
        return this.customer;
    }

    /**
     * this gets a list of phonecalls.
     * At the moment it prints out all of the phonecalls.
     * @return returns the list of phone calls.
     */
    @Override
    public Collection<PhoneCall> getPhoneCalls() { return this.calls; }

    /**
     * This searches phonebill for phonecalls that occurred during the given time period.
     * @param bill The bill being searched
     * @param fromThisTime The beginning time in Date format
     * @param toThisTime The ending time in Date format
     * @return A new phonebill with phonecalls that fit the criteria.
     * Will return a bill without calls if no calls match.
     */
    public PhoneBill billSearch(PhoneBill bill, Date fromThisTime, Date toThisTime) {
        PhoneBill returnMe = new PhoneBill(bill.customer);
        for (PhoneCall call : bill.calls) {
            if (call.dateFormattedStart.after(fromThisTime) && call.dateFormattedStart.before(toThisTime)) {
                returnMe.addPhoneCall(call);
            }
        }
        return returnMe;
    }
}
