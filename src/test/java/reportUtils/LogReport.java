package reportUtils;

import com.aventstack.extentreports.ExtentTest;
import org.testng.Assert;

public class LogReport{
    protected ExtentTest logStep = null;

    public void logStep(String description) {
        logStep = ExtTest.getTest().createNode(description);
    }

    public void logException(Throwable e){
        ExtentTest logStep = ExtTest.getTest().createNode("Exception:");
        logStep.warning(utils.Utilities.getStackTrace(e.getStackTrace()));
        Assert.fail("Exception occurs when executing test case: ", e);
    }
}
