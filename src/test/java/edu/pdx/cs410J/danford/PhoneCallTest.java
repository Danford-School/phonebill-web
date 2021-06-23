package edu.pdx.cs410J.danford;

import org.junit.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Unit tests for the {@link PhoneCall} class.
 *
 * You'll need to update these unit tests as you build out you program.
 */
public class PhoneCallTest {

    /**
     * This test is to ensure that the simple constructor initializes everything to null
     */
    @Test
    public void theSimpleConstructorNullsEverything() {
        PhoneCall call = new PhoneCall();
        assertThat(call.theCustomer, is(nullValue()));
        assertThat(call.theCustomersNumber, is(nullValue()));
        assertThat(call.isCallingThisNumber, is(nullValue()));
    }

    /**
     * This tests to ensure that the full constructor initializes everything correctly.
     */
    @Test
    public void theFullConstructorShouldWork() throws ParseException {
        PhoneCall call = new PhoneCall("Tommy Middle-D", "111-222-3333", "444-555-6666", "5/29/1984", "4:20", "PM", "05/29/1984",
                "4:30", "PM");
        assertThat(call.theCustomer, containsString("Tommy Middle-D"));
        assertThat(call.theCustomersNumber, containsString("111-222-3333"));
        assertThat(call.isCallingThisNumber, containsString("444-555-6666"));
    }

    @Test
    public void dateObjectsShouldMakeDateObjects2() throws ParseException {
        String stringDate = "05/29/84, 4:30 pm";
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy, hh:mm a");
        Date test = formatter.parse(stringDate);
        PhoneCall call = new PhoneCall("Tommy Middle-D", "111-222-3333", "444-555-6666", "5/29/1984", "4:20", "PM", "05/29/1984",
                "4:30", "PM");
        assertThat(test, equalTo(call.dateFormattedEnd));
    }

    @Test
    public void testFormatAndStringADate() throws ParseException {
        PhoneCall call = new PhoneCall("Tommy Middle-D", "111-222-3333", "444-555-6666", "5/29/1984", "4:20", "PM", "05/29/1984",
                "4:30", "PM");
        assertThat(call.startTimeAndDate, containsString("5/29/1984, 4:20 pm"));
    }

    @Test
    public void testPrettyOutput() throws ParseException {
        PhoneCall call = new PhoneCall("Tommy Middle-D", "111-222-3333", "444-555-6666", "5/29/1984", "4:20", "PM", "05/29/1984",
                "4:30", "PM");
        assertThat(call.phoneCallToOutput(), containsString("Tommy Middle-D at 111-222-3333 called 444-555-6666 starting on 5/29/1984, 4:20 pm, and ending on 05/29/1984, 4:30 pm."));
    }

    @Test
    public void checkAMPMPass() {
        String pass = "am";
        PhoneCall.checkAMPM(pass);
    }

    @Test (expected = IllegalArgumentException.class)
    public void checkAMPMFail() {
        String fail = "NO";
        PhoneCall.checkAMPM(fail);
    }

    /**
     * This tests the error handling of a bad phone number and it should fail.
     */
    @Test(expected = IllegalArgumentException.class)
    public void phoneNumbersAreNotTacos() throws ParseException {
        PhoneCall call = new PhoneCall("Tommy Middle-D", "ImA-big-Taco", "Ihe-art-taco", "5/29/1984", "4:20", "pm", "05/29/1984",
                "4:30", "pm");
    }

    /**
     * This tests to ensure that dates are not letters. It should fail.
     */
    @Test(expected = IllegalArgumentException.class)
    public void youDontTellTimeWithLetters() throws ParseException {
        PhoneCall call = new PhoneCall("Tommy Middle-D", "111-222-3333", "444-555-6666", "5/29/1984", "A:BC", "PM", "05/29/1984",
                "CD:EF", "PM");
    }

    /**
     * This tests to ensure that the phone numbers cannot have letters. Should fail.
     */
    @Test(expected = IllegalArgumentException.class)
    public void datesAreNotTheAlamo() throws ParseException {
        PhoneCall call = new PhoneCall("Tommy Middle-D", "111-222-3333", "444-555-6666", "Th/eA/lamo", "4:20", "PM", "A/l/amoe",
                "4:30", "PM");
    }

    /**
     * This tests the getCaller method to ensure accuracy
     */
    @Test
    public void testGetCaller() throws ParseException {
        PhoneCall call = new PhoneCall("Tommy Middle-D", "111-222-3333", "444-555-6666", "5/29/1984", "4:20", "PM", "05/29/1984",
                "4:30", "PM");
        assertThat(call.getCaller(), containsString("111-222-3333"));
    }

    /**
     * this tests the getCallee method to ensure accuracy
     */
    @Test
    public void testGetCallee() throws ParseException {
        PhoneCall call = new PhoneCall("Tommy Middle-D", "111-222-3333", "444-555-6666", "5/29/1984", "4:20", "PM", "05/29/1984",
                "4:30", "PM");
        assertThat(call.getCallee(), containsString("444-555-6666"));
    }

    /**
     * This tests to the GetStartTime method to ensure accuracy.
     */
    @Test
    public void testGetStartTimeString() throws ParseException {
        PhoneCall call = new PhoneCall("Tommy Middle-D", "111-222-3333", "444-555-6666", "5/29/1984", "4:20", "PM", "05/29/1984",
                "4:30", "PM");
        assertThat(call.getStartTimeString(), containsString("5/29/1984, 4:20 pm"));
    }

    /**
     * This tests the getEndTime method to ensure accuracy.
     */
    @Test
    public void testGetEndTimeString() throws ParseException {
        PhoneCall call = new PhoneCall("Tommy Middle-D", "111-222-3333", "444-555-6666", "5/29/1984", "4:20", "PM", "05/29/1984",
                "4:30", "PM");
        assertThat(call.getEndTimeString(), containsString("05/29/1984, 4:30 pm"));
    }

    @Test
    public void dateObjectsShouldMakeDateObjects() throws ParseException {
        PhoneCall call = new PhoneCall("Tommy Middle-D", "111-222-3333", "444-555-6666", "5/29/84", "4:20", "PM", "05/29/84",
                "4:30", "PM");
        assertThat(call.endTimeAndDate, containsString("05/29/84, 4:30 pm"));
    }

    @Test
    public void phoneCallToOutputShouldBeReadable() throws ParseException {
        PhoneCall call = new PhoneCall("Tommy Middle-D", "111-222-3333", "444-555-6666", "5/29/84", "4:20", "PM", "05/29/84",
                "4:30", "PM");
        assertThat(call.phoneCallToOutput(), containsString("Tommy Middle-D at 111-222-3333 called 444-555-6666 starting on 5/29/84, 4:20 pm, and ending on 05/29/84, 4:30 pm"));
    }

    @Test
    public void checkAMPMShouldReturnLowerCase() throws ParseException {
        PhoneCall call = new PhoneCall("Tommy Middle-D", "111-222-3333", "444-555-6666", "5/29/84", "4:20", "PM", "05/29/84",
                "4:30", "PM");
        assertThat(call.startTimeAndDate, containsString("pm"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void XXIsNotTheSameAsAMOrPM() throws ParseException {
        PhoneCall call = new PhoneCall("Tommy Middle-D", "111-222-3333", "444-555-6666", "5/29/84", "4:20", "PM", "05/29/84",
                "4:30", "XX");
    }
}
