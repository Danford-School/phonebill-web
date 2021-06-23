package edu.pdx.cs410J.danford;

import edu.pdx.cs410J.ParserException;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;

import static edu.pdx.cs410J.danford.PhoneBillURLParameters.CUSTOMER_PARAMETER;
import static edu.pdx.cs410J.danford.Project4.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class Project4Test {
    @Test
    public void testDoAllTheThings() throws IOException, ParserException, ServletException, ParseException {
        String[] args = {"-host", "localhost", "-port", "8080", "Benny Schwazz", "211-222-3333", "444-555-6666", "5/29/1984", "4:20", "PM", "05/29/84",
                "4:30", "PM"};
        PhoneCall call = createCall(args, 4);
        PhoneBill bill = new PhoneBill(call.theCustomer);
        bill.addPhoneCall(call);

        PhoneBillServlet servlet = new PhoneBillServlet();
        servlet.addPhoneBill(bill);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(CUSTOMER_PARAMETER)).thenReturn(call.theCustomer);

        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        when(response.getWriter()).thenReturn(pw);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);

        String textPhoneBill = sw.toString();
        assertThat(textPhoneBill, Matchers.containsString(call.theCustomer));
        assertThat(textPhoneBill, Matchers.containsString(call.theCustomersNumber));
    }

    /**
     * This tests the doAllTheThings method again, but with options to ensure that I am not mathing badly later on.
     */
    @Test
    public void phoneBillSearchReturnsAResult() throws IOException, ServletException, ParseException {
        String[] args = {"-host", "localhost", "-port", "8080", "-print", "Benny Schwazz", "111-222-3333", "444-555-6666", "5/29/1984", "4:20", "PM", "05/29/1984",
                "4:30", "PM"};
        PhoneCall call = createCall(args, 5);
        PhoneBill bill = new PhoneBill(call.theCustomer);
        bill.addPhoneCall(call);

        PhoneBillServlet servlet = new PhoneBillServlet();
        servlet.addPhoneBill(bill);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(CUSTOMER_PARAMETER)).thenReturn(call.theCustomer);

        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        when(response.getWriter()).thenReturn(pw);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);

        String textPhoneBill = sw.toString();
        assertThat(textPhoneBill, Matchers.containsString(call.theCustomer));
        assertThat(textPhoneBill, Matchers.containsString(call.theCustomersNumber));
    }

    /*
    @Test
    public void testSearchAName() throws IOException, ParserException {
        String[] args = {"-host", "localhost", "-port", "8080", "-search", "Benny Schwazz"};

        Project4.doAllTheThings(args);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        String result = new String(output.toByteArray());
        System.out.println(result);
    }

    @Test (expected = IllegalArgumentException.class)
    public void portMustBeANumberNotATacoFail() throws IOException, ParserException {
        String[] args = {"-host", "localhost", "-port", "Taco", "-search", "Benny Schwazz", "5/29/1984, 4:22 pm", "5/29/1984, 4:30 pm"};

        doAllTheThings(args);
    }


     */


    /**
     * This checks the createCall function for accuracy.
     */
    @Test
    public void checkCreateCall() throws ParseException {
        String[] args = {"Benny Schwazz", "111-222-3333", "444-555-6666", "5/29/1984", "4:20", "PM", "05/29/1984",
                "4:30", "PM"};
        int count = 0;
        PhoneCall call = Project4.createCall(args, count);
        assertThat(call.theCustomer, containsString("Benny Schwazz"));
        assertThat(call.theCustomersNumber, containsString("111-222-3333"));
        assertThat(call.isCallingThisNumber, containsString("444-555-6666"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void cannotGoBackwardsInTime() throws ParseException {
        String[] args = {"Benny Schwazz", "111-222-3333", "444-555-6666", "5/29/1985", "4:20", "PM", "05/29/1984",
                "4:30", "PM"};
        int count = 0;
        PhoneCall call = Project4.createCall(args, count);
    }


    //This works, but I am not sure how to catch the console output.
    @Test
    public void doesReadMeWork() {
        Project4.printReadMe();
        String output = System.out.toString();
    }

    /**
     * This tests to ensure that dates are not letters. It should fail.
     */
    @Test(expected = IllegalArgumentException.class)
    public void youDontTellTimeWithLetters() throws ParseException {
        PhoneCall call = new PhoneCall("Tommy Middle-D", "111-222-3333", "444-555-6666", "5/29/1984", "A:BC", "PM", "05/29/1984",
                "CD:EF", "PM");
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

    @Test(expected = IllegalArgumentException.class)
    public void phoneNumbersAreNotTacos() throws ParseException {
        PhoneCall call = new PhoneCall("Tommy Middle-D", "ImA-big-Taco", "Ihe-art-taco", "5/29/1984", "4:20", "pm", "05/29/1984",
                "4:30", "pm");
    }


    /**
     * This tests to ensure that the phone numbers cannot have letters. Should fail.
     */
    @Test(expected = IllegalArgumentException.class)
    public void datesAreNotTheAlamo() throws ParseException {
        PhoneCall call = new PhoneCall("Tommy Middle-D", "111-222-3333", "444-555-6666", "Th/eA/lamo", "4:20", "PM", "A/l/amoe",
                "4:30", "PM");
    }

    @Test
    public void checkPrintBoolean() {
        String[] args = {"duck", "duck", "duck", "-print", "duck", "goose!"};
        Boolean test = shouldIPrint(args);
        assertEquals(true, (boolean) test);
    }

    @Test
    public void checkSearchBoolean() {
        String[] args = {"duck", "duck", "duck", "-search", "duck", "goose!"};
        Boolean test = checkSearch(args);
        assertEquals(true, (boolean) test);
    }

    @Test
    public void checkGrabPort() {
        String[] args = {"duck", "duck", "duck", "-port", "I'm A Port!", "duck", "goose!"};
        String test = getPort(args);
        assertEquals(test, "I'm A Port!");
    }
    @Test
    public void checkGrabHost() {
        String[] args = {"duck", "duck", "duck", "-host", "I'm A Host!", "duck", "goose!"};
        String test = getHost(args);
        assertEquals(test, "I'm A Host!");
    }
}
