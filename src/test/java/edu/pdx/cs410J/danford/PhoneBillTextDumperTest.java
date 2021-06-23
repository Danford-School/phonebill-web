package edu.pdx.cs410J.danford;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.rmi.NoSuchObjectException;
import java.text.ParseException;
import java.util.Scanner;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;



public class PhoneBillTextDumperTest {

    PrintWriter writer = new PrintWriter(System.out);
    PhoneBillTextDumper dumper = new PhoneBillTextDumper(writer);

    @Test (expected = NoSuchObjectException.class)
    public void dumpingAnEmptyPhoneBillGetsExitStatus1() throws ParseException, IOException {
        PhoneBill bill = new PhoneBill();
        dumper.dump(bill);
    }

    @Test
    public void dumpingAPhoneBillGivesAReadableOutput() throws ParseException, IOException {
        PhoneBill bill = new PhoneBill("Hot Soccermom");
        PhoneCall call = new PhoneCall("Hot Soccermom", "111-222-3333", "444-555-6666", "6/26/1985", "4:20", "PM", "06/26/1985",
                "4:30", "PM");
        PhoneCall call2 = new PhoneCall("Hot Soccermom", "111-222-3333", "666-777-8888", "6/27/1985", "4:20", "PM", "06/27/1985",
                "4:30", "PM");
        bill.addPhoneCall(call);
        bill.addPhoneCall(call2);
        dumper.dump(bill);
    }
}
