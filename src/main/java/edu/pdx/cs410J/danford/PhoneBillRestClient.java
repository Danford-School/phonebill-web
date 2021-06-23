package edu.pdx.cs410J.danford;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.web.HttpRequestHelper;
import edu.pdx.cs410J.ParserException;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_OK;
import static edu.pdx.cs410J.danford.PhoneBillURLParameters.*;

/**
 * A helper class for accessing the rest client.  Note that this class provides
 * an example of how to make gets and posts to a URL.  You'll need to change it
 * to do something other than just send dictionary entries.
 */
public class PhoneBillRestClient extends HttpRequestHelper
{
    private static final String WEB_APP = "phonebill";
    private static final String SERVLET = "calls";

    @VisibleForTesting
    protected final String url;


    /**
     * Creates a client to the Phone Bil REST service running on the given host and port
     * @param hostName The name of the host
     * @param port The port
     */
    public PhoneBillRestClient( String hostName, int port )
    {
        this.url = String.format( "http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET );
    }

    /**
     * Returns all dictionary entries from the server
     */
    public String getPhoneBill(String customer) throws IOException {
      Response response = get(this.url, Map.of(CUSTOMER_PARAMETER, customer));
      throwExceptionIfNotOkayHttpStatus(response);
      return response.getContent();
    }

    /**
     * This searches for a customer's phonebill for phonecalls that occurred during a given time.
     * It does this by using the get method with a larger map.
     * @param customer the customer being seached for
     * @param fromThisTime the beginning of the time period
     * @param toThisTime the end of the time period.
     * @return Returns a pretty-printed string of any phone calls from the customer that are within the time frame
     * @throws IOException Maybe
     */
    public String searchPhoneBill(String customer, String fromThisTime, String toThisTime) throws IOException {
        Response response = get(this.url, Map.of(CUSTOMER_PARAMETER, customer, START_DATE_AND_TIME_PARAMETER, fromThisTime, END_DATE_AND_TIME_PARAMETER, toThisTime));
        throwExceptionIfNotOkayHttpStatus(response);
        return response.getContent();
    }

    /**
     * This adds a phone call to an existing or new phone bill.
     * @param customer the customer who is making calls
     * @param call the call in question
     * @throws IOException Throws them everywhere
     */
    public void addPhoneCall(String customer, PhoneCall call) throws IOException {
        Response response = postToMyURL(Map.of(CUSTOMER_PARAMETER, customer, CALLER_NUMBER_PARAMETER,
                call.theCustomersNumber, CALLEE_NUMBER_PARAMETER, call.isCallingThisNumber, START_DATE_AND_TIME_PARAMETER,
                call.startTimeAndDate, END_DATE_AND_TIME_PARAMETER, call.endTimeAndDate), call.theCustomer);
        throwExceptionIfNotOkayHttpStatus(response);
    }

    /**
     * helper function for http?
     * @param calls a map of strings
     * @param customer the customer who is making calls
     * @return a http response
     * @throws IOException True
     */
    @VisibleForTesting
    Response postToMyURL(Map<String, String> calls, String customer) throws IOException {
      return post(this.url, calls);
    }

    /**
     * This throws an exception if the response is not ok. Otherwise returns the response
     * @param response the http response that is received from the server.
     * @return the response from the server.
     */
    @VisibleForTesting
    protected Response throwExceptionIfNotOkayHttpStatus(Response response) throws IOException {
      int code = response.getCode();
      if (code != HTTP_OK) {
          String problem = String.format("There was a code %d error!", code);
          System.out.println(problem);
          throw new IOException();
      }
      return response;
    }
}
