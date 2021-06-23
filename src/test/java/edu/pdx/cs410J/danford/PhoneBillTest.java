package edu.pdx.cs410J.danford;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * This is the PhoneBill test suite.
 */

public class PhoneBillTest {

    //These are the testing members
    PhoneBill bill = new PhoneBill("Hot Soccermom");
    PhoneCall call = new PhoneCall("Hot Soccermom", "111-222-3333", "444-555-6666", "6/26/1985", "4:20", "PM", "06/26/1985",
            "4:30", "PM");

    public PhoneBillTest() throws ParseException {
    }


    /**
     * This tests the addPhoneCall method for accuracy.
     */
    @Test
    public void testAddPhoneCallPassWithoutError() throws ParseException {
        PhoneBill bill = new PhoneBill();
        bill.addPhoneCall(call);
    }

    /**
     * This tests the getCustomer method for accuracy.
     */
    @Test
    public void testGetCustomer() {
        bill.addPhoneCall(call);
        bill.customer = call.theCustomer;
        Assert.assertEquals(bill.getCustomer(), "Hot Soccermom");
    }

    /**
     * This tests the getPhoneCalls method for accuracy.
     * This isn't great. When I try to assert other things it doesn't work well. I will look for clarification.
     * Now I think because it's an arraylist and not a collection. I'm really just reverting to C so I should investigate.
     * It will need to be fixed in the next version when I change to collection
     */

    @Test
    public void testGetPhoneCalls() {
        bill.addPhoneCall(call);
        PhoneBill phoneBillTest = new PhoneBill();
        phoneBillTest.addPhoneCall(call);
        Assert.assertEquals(bill.getPhoneCalls(), phoneBillTest.getPhoneCalls());

        if(!bill.calls.contains(call)) {
            System.err.println("Phone bill does not contain phone calls");
        }
    }

    @Test
    public void billSearchWorksIfItExists() throws ParseException {
        bill.addPhoneCall(call);
        PhoneBill searchResult = bill.billSearch(bill, call.makeDateObjectFromString("6/26/85, 4:22 pm"), call.makeDateObjectFromString("7/30/20, 4:40 pm"));
        assertThat(searchResult.getCustomer(), containsString("Hot Soccermom"));
    }

    @Test
    public void billSearchReturnsABillWithoutCallsIfNotFound() throws ParseException {
        bill.addPhoneCall(call);
        PhoneBill searchResult = bill.billSearch(bill, call.makeDateObjectFromString("6/27/85, 4:22 pm"), call.makeDateObjectFromString("7/30/20, 4:40 pm"));
        assert(searchResult.getPhoneCalls().isEmpty());
    }
}