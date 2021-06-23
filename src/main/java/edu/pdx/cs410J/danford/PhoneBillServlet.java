package edu.pdx.cs410J.danford;

import com.google.common.annotations.VisibleForTesting;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static edu.pdx.cs410J.danford.PhoneBillURLParameters.*;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>PhoneBill</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple dictionary of words
 * and their definitions.
 */
public class PhoneBillServlet extends HttpServlet
{

    private final Map<String, PhoneBill> PhoneBills = new HashMap<>();

    /**
     * Handles an HTTP GET request from a client by writing the definition of the
     * word specified in the "word" HTTP parameter to the HTTP response.  If the
     * "word" parameter is not specified, all of the entries in the dictionary
     * are written to the HTTP response.
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setContentType( "text/plain" );


        String customer = getParameter( CUSTOMER_PARAMETER, request );
        if (customer == null) {
            missingRequiredParameter(response, CUSTOMER_PARAMETER);
            return;
        }

        PhoneBill bill = getPhoneBill(customer);
        if(bill == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        String startDateAndTime = getParameter(START_DATE_AND_TIME_PARAMETER, request);
        String endDateAndTime = getParameter(END_DATE_AND_TIME_PARAMETER, request);

        //Catch for missing field for search
        if((startDateAndTime == null && endDateAndTime != null) || (startDateAndTime != null && endDateAndTime == null)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

        PhoneBillTextDumper dumper = new PhoneBillTextDumper(response.getWriter());

        if(startDateAndTime != null && endDateAndTime != null) {
            try {
                PhoneBill searchResultBill = bill.billSearch(bill, makeDateObjectFromString(startDateAndTime), makeDateObjectFromString(endDateAndTime));
                if(searchResultBill.calls.isEmpty()) { response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED); }
                dumper.dump(searchResultBill);
            } catch (ParseException e) {
                response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED);
            }
        }
        else {
            dumper.dump(bill);
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Handles an HTTP POST request by storing the dictionary entry for the
     * "word" and "definition" request parameters.  It writes the dictionary
     * entry to the HTTP response.
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setContentType( "text/plain" );

        String customer = getParameter(CUSTOMER_PARAMETER, request );
        if (customer == null) {
            missingRequiredParameter(response, CUSTOMER_PARAMETER);
            return;
        }

        String caller = getParameter(CALLER_NUMBER_PARAMETER, request );
        if ( caller == null) {
            missingRequiredParameter(response, CALLER_NUMBER_PARAMETER );
            return;
        }

        String callee = getParameter(CALLEE_NUMBER_PARAMETER, request);
        if (callee == null) {
            missingRequiredParameter(response, CALLEE_NUMBER_PARAMETER);
            return;
        }

        String startDateAndTime = getParameter(START_DATE_AND_TIME_PARAMETER, request);
        if (startDateAndTime == null) {
            missingRequiredParameter(response, START_DATE_AND_TIME_PARAMETER);
            return;
        }

        String endDateAndTime = getParameter(END_DATE_AND_TIME_PARAMETER, request);
        if (endDateAndTime == null) {
            missingRequiredParameter(response, END_DATE_AND_TIME_PARAMETER);
            return;
        }

        PhoneBill bill = getPhoneBill(customer);
        if(bill == null) {
            bill = new PhoneBill(customer);
            try {
                bill.addPhoneCall(new PhoneCall(customer, caller, callee, startDateAndTime, endDateAndTime));
            } catch (ParseException e) {
                response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Cannot create new PhoneCall in servlet");
            }
        } else {
            try {
                bill.addPhoneCall(new PhoneCall(customer, caller, callee, startDateAndTime, endDateAndTime));
            } catch (ParseException e) {
                response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Cannot create new PhoneCall in servlet");
            }
        }

        this.PhoneBills.put(customer, bill);

        response.setStatus( HttpServletResponse.SC_OK);
    }

    /**
     * Writes an error message about a missing parameter to the HTTP response.
     *
     *
     */
    private void missingRequiredParameter( HttpServletResponse response, String parameterName )
        throws IOException
    {
        String message = String.format("Missing required parameter: %s", parameterName);
        response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
    }

    /**
     * Returns the value of the HTTP request parameter with the given name.
     *
     * @return <code>null</code> if the value of the parameter is
     *         <code>null</code> or is the empty string
     */
    private String getParameter(String name, HttpServletRequest request) {
      String value = request.getParameter(name);
      if (value == null || "".equals(value)) {
        return null;

      } else {
        return value;
      }
    }

    /**
     * This makes a Date object from a string that holds Date info
     * Date, time ampm
     * @param stringDate A string that holds date info
     * @return A date object from the date info
     * @throws ParseException true
     */
    public Date makeDateObjectFromString(String stringDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy, hh:mm a");
        Date date = formatter.parse(stringDate);
        return date;
    }

    /**
     * Gets a phoneBill from the map inside the servlet
     * @param customer The customer whose date we are getting
     * @return The customer's phonebill
     */
    @VisibleForTesting
    PhoneBill getPhoneBill(String customer) {
        return this.PhoneBills.get(customer);
    }

    /**
     *This adds a phonebill to the map in the servlet.
     * @param bill The bill being added.
     */
    @VisibleForTesting
    void addPhoneBill(PhoneBill bill) {
        this.PhoneBills.put(bill.getCustomer(), bill);
    }

}
