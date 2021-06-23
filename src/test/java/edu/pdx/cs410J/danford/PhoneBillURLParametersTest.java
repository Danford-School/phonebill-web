package edu.pdx.cs410J.danford;

import org.junit.Test;
import org.junit.Assert;

public class PhoneBillURLParametersTest {

    @Test
    public void theParametersShouldReturnTheCorrectParameter() {
        Assert.assertEquals(PhoneBillURLParameters.CUSTOMER_PARAMETER, "customer");
        Assert.assertEquals(PhoneBillURLParameters.CALLER_NUMBER_PARAMETER, "callerNumber");
        Assert.assertEquals(PhoneBillURLParameters.CALLEE_NUMBER_PARAMETER, "calleeNumber");
        Assert.assertEquals(PhoneBillURLParameters.START_DATE_AND_TIME_PARAMETER, "startDateAndTime");
        Assert.assertEquals(PhoneBillURLParameters.END_DATE_AND_TIME_PARAMETER, "endDateAndTime");
    }

    @Test
    public void checkUseLessFunctionForTestingCoverage() {
        PhoneBillURLParameters parameters = new PhoneBillURLParameters();
        Assert.assertEquals(parameters.getACustomerParameter(), "customer");
    }
}
