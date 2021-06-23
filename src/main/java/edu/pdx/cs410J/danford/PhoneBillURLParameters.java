package edu.pdx.cs410J.danford;

/**
 * This hold parameters to be used in http-ing
 */
public class PhoneBillURLParameters {

    static final String CUSTOMER_PARAMETER = "customer";

    static final String CALLER_NUMBER_PARAMETER = "callerNumber";

    static final String CALLEE_NUMBER_PARAMETER = "calleeNumber";

    static final String START_DATE_AND_TIME_PARAMETER = "startDateAndTime";

    static final String END_DATE_AND_TIME_PARAMETER = "endDateAndTime";

    /**
     * This just returns the customer parameter. It's so tests will pass for this class.
     * @return "customer"
     */
    public String getACustomerParameter() {
        return CUSTOMER_PARAMETER;
    }
}
