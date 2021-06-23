package edu.pdx.cs410J.danford;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static edu.pdx.cs410J.danford.PhoneBillURLParameters.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * A unit test for the {@link PhoneBillServlet}.  It uses mockito to
 * provide mock http requests and responses.
 */
public class PhoneBillServletTest {

  @Test
  public void requestWithNoCustomerReturnMissingParameter() throws ServletException, IOException {
    PhoneBillServlet servlet = new PhoneBillServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    servlet.doGet(request, response);

    //needs a verify*************
    //verify(response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED, Messages.missingRequiredParameter(CUSTOMER_PARAMETER));
  }

  @Test (expected = FileNotFoundException.class)
  public void requestCustomerWithNoPhoneBillReturnsNotFound() throws ServletException, IOException {
    PhoneBillServlet servlet = new PhoneBillServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    String customerName = "Dave";
    when(request.getParameter(CUSTOMER_PARAMETER)).thenReturn(customerName);

    HttpServletResponse response = mock(HttpServletResponse.class);

    servlet.doGet(request, response);

    //needs a verify
    verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "No phone bill for customer null");
  }

  @Test
  public void cannotPostWithoutCallerNumber() throws ServletException, IOException {
    PhoneBillServlet servlet = new PhoneBillServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    String customerName = "Dave";
    when(request.getParameter(CUSTOMER_PARAMETER)).thenReturn(customerName);

    HttpServletResponse response = mock(HttpServletResponse.class);

    servlet.doPost(request, response);

    verify(response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "Missing required parameter: callerNumber");
  }

  @Test
  public void cannotPostWithoutCalleeNumber() throws ServletException, IOException {
    PhoneBillServlet servlet = new PhoneBillServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    String customerName = "Dave";
    when(request.getParameter(CUSTOMER_PARAMETER)).thenReturn(customerName);
    String callerNum = "111-222-3333";
    when(request.getParameter(CALLER_NUMBER_PARAMETER)).thenReturn(callerNum);

    HttpServletResponse response = mock(HttpServletResponse.class);

    servlet.doPost(request, response);

    verify(response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "Missing required parameter: calleeNumber");
  }

  @Test
  public void cannotPostWithoutStartDateAndTime() throws ServletException, IOException {
    PhoneBillServlet servlet = new PhoneBillServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    String customerName = "Dave";
    when(request.getParameter(CUSTOMER_PARAMETER)).thenReturn(customerName);
    String callerNum = "111-222-3333";
    when(request.getParameter(CALLER_NUMBER_PARAMETER)).thenReturn(callerNum);
    String calleeNum = "444-555-6666";
    when(request.getParameter(CALLEE_NUMBER_PARAMETER)).thenReturn(calleeNum);

    HttpServletResponse response = mock(HttpServletResponse.class);

    servlet.doPost(request, response);

    verify(response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "Missing required parameter: startDateAndTime");
  }

  @Test
  public void cannotPostWithoutEndDateAndTime() throws ServletException, IOException {
    PhoneBillServlet servlet = new PhoneBillServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    String customerName = "Dave";
    when(request.getParameter(CUSTOMER_PARAMETER)).thenReturn(customerName);
    String callerNum = "111-222-3333";
    when(request.getParameter(CALLER_NUMBER_PARAMETER)).thenReturn(callerNum);
    String calleeNum = "444-555-6666";
    when(request.getParameter(CALLEE_NUMBER_PARAMETER)).thenReturn(calleeNum);
    String startDateAndTime = "7/7/77";
    when(request.getParameter(START_DATE_AND_TIME_PARAMETER)).thenReturn(startDateAndTime);

    HttpServletResponse response = mock(HttpServletResponse.class);

    servlet.doPost(request, response);

    verify(response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "Missing required parameter: endDateAndTime");
  }

  @Test
  public void dateObjectsShouldMakeDateObjects() throws ParseException {
    String stringDate = "05/29/84, 4:30 pm";
    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy, hh:mm a");
    Date test = formatter.parse(stringDate);
    PhoneCall call = new PhoneCall("Tommy Middle-D", "111-222-3333", "444-555-6666", "5/29/1984", "4:20", "PM", "05/29/1984",
            "4:30", "PM");
    assertThat(test, equalTo(call.dateFormattedEnd));
  }

  @Test
  public void addANewPhoneCall() throws ServletException, IOException {
    PhoneBillServlet servlet = new PhoneBillServlet();

    String customer = "Sware";
    String callerPhoneNumber = "111-222-3333";
    String isCalling = "444-555-6666";
    String startTime = "6/26/1985, 4:20 pm";
    String endTime = "6/26/1985, 4:30 pm";

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(CUSTOMER_PARAMETER)).thenReturn(customer);
    when(request.getParameter(CALLER_NUMBER_PARAMETER)).thenReturn(callerPhoneNumber);
    when(request.getParameter(CALLEE_NUMBER_PARAMETER)).thenReturn(isCalling);
    when(request.getParameter(START_DATE_AND_TIME_PARAMETER)).thenReturn(startTime);
    when(request.getParameter(END_DATE_AND_TIME_PARAMETER)).thenReturn(endTime);

    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);

    servlet.doPost(request, response);

    verify(pw, times(0)).println(Mockito.any(String.class));
    verify(response).setStatus(HttpServletResponse.SC_OK);

    PhoneBill phoneBill = servlet.getPhoneBill(customer);
    assertThat(phoneBill, notNullValue());
    assertThat(phoneBill.getCustomer(), equalTo(customer));

    PhoneCall phoneCall = phoneBill.getPhoneCalls().iterator().next();
    assertThat(phoneCall.getCaller(), equalTo(callerPhoneNumber));
  }

  @Test
  public void requestingExistingPhoneBillDumpsItToPrintWriter() throws IOException, ServletException, ParseException {
    String customer = "Tommy Middle-D";
    PhoneCall call = new PhoneCall("Tommy Middle-D", "111-222-3333", "444-555-6666", "5/29/1984", "4:20", "PM", "05/29/1984",
            "4:30", "PM");

    PhoneBill bill = new PhoneBill(customer);
    bill.addPhoneCall(call);

    PhoneBillServlet servlet = new PhoneBillServlet();
    servlet.addPhoneBill(bill);

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(CUSTOMER_PARAMETER)).thenReturn(customer);

    HttpServletResponse response = mock(HttpServletResponse.class);
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);

    verify(response).setStatus(HttpServletResponse.SC_OK);

    String textPhoneBill = sw.toString();
    assertThat(textPhoneBill, containsString(customer));
    assertThat(textPhoneBill, containsString(call.theCustomersNumber));
  }

}
