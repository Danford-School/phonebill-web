package edu.pdx.cs410J.danford;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.text.ParseException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Tests the {@link Project4} class by invoking its main method with various arguments
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Project4IT extends InvokeMainTestCase {
    private static final String HOSTNAME = "localhost";
    private static final String PORT = System.getProperty("http.port", "8080");


    @Test
    public void test1NoCommandLineArguments() {
        MainMethodResult result = invokeMain( Project4.class );
        assertThat(result.getExitCode(), equalTo(1));
    }



    @Test
    public void test2EmptyServer() {
        MainMethodResult result = invokeMain( Project4.class, HOSTNAME, PORT );
        assertThat(result.getExitCode(), equalTo(1));

    }

    @Test
    public void portMustBeANumberNotATacoFail() throws IOException, ParseException {
        MainMethodResult result = invokeMain(Project4.class, "-host", "localhost", "-port", "8080", "Benny Schwazz", "5/29/1984", "4:22", "pm", "5/29/1984", "4:30", "pm");
        assertThat(result.getExitCode(), equalTo(0));
    }

/*
    @Test(expected = PhoneBillRestException.class)
    public void test3NoDefinitionsThrowsAppointmentBookRestException() throws Throwable {
        String word = "WORD";
        try {
            invokeMain(Project4.class, HOSTNAME, PORT, word);

        } catch (UncaughtExceptionInMain ex) {
            throw ex.getCause();
        }
    }



    @Test
    public void test4AddDefinition() {
        String word = "WORD";
        String definition = "DEFINITION";

        MainMethodResult result = invokeMain( Project4.class, "-host", HOSTNAME, "-port", PORT, word);
        assertThat(result.getTextWrittenToStandardError(), result.getExitCode(), equalTo(0));
        String out = result.getTextWrittenToStandardOut();
        assertThat(out, out, containsString(word + " " + definition));

        result = invokeMain( Project4.class, "-host", HOSTNAME, "-port", PORT, word );
        out = result.getTextWrittenToStandardOut();
        assertThat(out, out, containsString(word));

 */
/*
        result = invokeMain( Project4.class, "-host", HOSTNAME, "-port", PORT );
        out = result.getTextWrittenToStandardOut();

 //       assertThat(out, out, containsString(Messages.formatDictionaryEntry(word, definition)));
 */

//    }

}