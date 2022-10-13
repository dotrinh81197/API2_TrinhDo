package reportUtils;


import com.aventstack.extentreports.ExtentTest;

public class ExtTest {
    public static ThreadLocal<ExtentTest> extentTestThreadSafe = new ThreadLocal<ExtentTest>();

    public static synchronized ExtentTest getTest() {
        return extentTestThreadSafe.get();
    }

    public static void setTest(ExtentTest tst) {
        extentTestThreadSafe.set(tst);
    }
}
