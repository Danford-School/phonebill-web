package edu.pdx.cs410J.danford;

import java.util.Comparator;

/**
 * This is the sorter class. It only exists to sort stuff using Comparator. I don't quite understand the output,
 * but it does seem to work.
 */
public class Sorter implements Comparator<PhoneCall> {

    /**
     * This compares two objects for the Comparator interface.
     * It first compares start time
     * It second compares phone numbers.
     * I don't know how to make it error out when they are identical.
     * @param o First PhoneCall
     * @param t1 Second PhoneCall
     * @return An int that is the result. It's odd.
     */
    @Override
    public int compare(PhoneCall o, PhoneCall t1) {
        int result = o.startTimeAndDate.compareTo(t1.startTimeAndDate);
        if(result == 0) {
            result = o.theCustomersNumber.compareTo(t1.theCustomersNumber);
        }
        return result;
    }
}
