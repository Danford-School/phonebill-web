package edu.pdx.cs410J.danford;

import edu.pdx.cs410J.PhoneBillDumper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.NoSuchObjectException;

/**
 * This class dumps a phonebill to the writer that it is given.
 */
public class PhoneBillTextDumper implements PhoneBillDumper<PhoneBill> {
    private final PrintWriter writer;

    /**
     * Constructor where you give it the writer for output.
     * @param writer The writer that will take output
     */
    PhoneBillTextDumper(PrintWriter writer) {
        this.writer = writer;
    }

    /**
     * This dumps a phone bill in pretty format to whatever the writer is.
     * @param bill The phonebill that gets dumped.
     * @throws IOException It does.
     */
    @Override
    public void dump(PhoneBill bill) throws IOException {
        if(bill == null) { throw new FileNotFoundException("There was an error, bill not found!"); }
        if(bill.getCustomer() == null) {
            throw new NoSuchObjectException("There is no bill to dump!");
        }
        this.writer.println(bill.getCustomer() + ":");

        for(PhoneCall call : bill.getPhoneCalls()) {
            this.writer.println(call.theCustomer + " at " + call.theCustomersNumber + " is calling "
                    + call.isCallingThisNumber + " starting on " + call.startTimeAndDate + ", and ending on " +
                    call.endTimeAndDate + ".\n");
        }
    }
}
