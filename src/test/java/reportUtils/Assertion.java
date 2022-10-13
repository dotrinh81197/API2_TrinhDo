package reportUtils;

import com.aventstack.extentreports.Status;
import org.testng.Assert;

import static com.aventstack.extentreports.Status.WARNING;

public class Assertion extends LogReport{

    public void assertEquals(int actual, int expected, String description) {
        logStep = ExtTest.getTest().createNode(description);
        try {
            if (expected == actual) {
                logStep.log(Status.PASS, String.format("Actual Result: %s<br/>expected Result: %s", actual, expected));
            } else {
                logStep.log(Status.FAIL, String.format("Actual Result: %s<br/>expected Result: %s", actual, expected));
                Assert.fail(String.format("Actual Result: %s<br/>expected Result: %s", actual, expected));
            }
        } catch (Exception e) {
            logStep.log(WARNING, "assertEquals error");
            logException(e);
        }
    }

    public void assertEquals(Object actual, String expected, String description) {
        logStep = ExtTest.getTest().createNode(description);
        try {
            if (actual.toString().equals(expected)) {
                logStep.log(Status.PASS, String.format("Actual Result: %s<br/>expected Result: %s", actual, expected));
            } else {
                logStep.log(Status.FAIL, String.format("Actual Result: %s<br/>expected Result: %s", actual, expected));
            }
        } catch (Exception e) {
            logStep.log(WARNING, "assertEquals error");
            logException(e);
        }
    }

    public void assertNotNull(Object o, String description) {
        logStep = ExtTest.getTest().createNode(description);
        try {
            if (o != null) {
                logStep.log(Status.PASS, String.format("Actual Result is not null:<br/> %s", o));
            } else {
                logStep.log(Status.FAIL, String.format("Actual Result is null"));
            }
        } catch (Exception e) {
            logStep.log(WARNING, "assertNotNull error");
            logException(e);
        }
    }

}