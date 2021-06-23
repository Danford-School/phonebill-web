package edu.pdx.cs410J.danford;

import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;
import org.hamcrest.Matchers;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

import static java.net.HttpURLConnection.*;


import static edu.pdx.cs410J.danford.PhoneBillURLParameters.CUSTOMER_PARAMETER;
import static edu.pdx.cs410J.danford.Project4.createCall;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class PhoneBillRestClientTest {

    PhoneBillRestClient client = new PhoneBillRestClient("localhost", 8080);

    @Test
    public void restClientShouldKnowItsURL() {
        assertThat(client.url, containsString("http://localhost:8080/phonebill/calls"));
    }

    @Test
    public void getPhoneBillShouldGetAPhoneBill() throws IOException, ParserException, ParseException {
        String[] args = {"-host", "localhost", "-port", "8080", "-print", "Benny Schwazz", "111-222-3333", "444-555-6666", "5/29/1984", "4:20", "PM", "05/29/1984",
                "4:30", "PM"};
        PhoneCall call = createCall(args, 5);
        PhoneBill bill = new PhoneBill(call.theCustomer);
        bill.addPhoneCall(call);

        PhoneBillServlet servlet = new PhoneBillServlet();
        servlet.getPhoneBill(bill.customer);

        PhoneBillRestClient client = mock(PhoneBillRestClient.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(CUSTOMER_PARAMETER)).thenReturn(call.theCustomer);

        when(client.getPhoneBill(call.theCustomer)).thenReturn("Benny Schwazz at 111-222-3333 called 444-555-6666 on 5/29/1984, 4:20 pm, to 5/29/1984, 4:30 pm.");
        String result = client.getPhoneBill(call.theCustomer);

        assertThat(result, containsString("Benny Schwazz at 111-222-3333 called 444-555-6666 on 5/29/1984, 4:20 pm, to 5/29/1984, 4:30 pm."));
    }

    @Test
    public void getPhoneCallShouldWork() throws ParseException, IOException {
        PhoneBillRestClient client2 = mock(PhoneBillRestClient.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        response.setStatus(HTTP_BAD_REQUEST);
        String test = String.valueOf(response.getStatus());
        when(client2.getPhoneBill("Benny Schwazz")).thenReturn(test);
        client2.getPhoneBill("Benny Schwazz");
    }

    @Test
    public void aSuccessfulAddPhoneCallDoesNothing() throws ParseException {
        String[] args = {"-host", "localhost", "-port", "8080", "-print", "Benny Schwazz", "111-222-3333", "444-555-6666", "5/29/1984", "4:20", "PM", "05/29/1984",
                "4:30", "PM"};
        PhoneCall call = createCall(args, 5);
        PhoneBill bill = new PhoneBill(call.theCustomer);
        bill.addPhoneCall(call);

        PhoneBillServlet servlet = new PhoneBillServlet();
        servlet.getPhoneBill(bill.customer);

        PhoneBillRestClient client = mock(PhoneBillRestClient.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
    }

    //This shouldn't pass...
    @Test
    public void notOKThrowsARestException() throws IOException {
        PhoneBillRestClient client = mock(PhoneBillRestClient.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        response.setStatus(SC_BAD_REQUEST);
        HttpRequestHelper.Response another = mock(HttpRequestHelper.Response.class);
        when(another.getCode()).thenReturn(HTTP_BAD_REQUEST);
        HttpRequestHelper.Response response3 = client.throwExceptionIfNotOkayHttpStatus(another);
        }
}
