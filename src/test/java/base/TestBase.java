package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import common.Constants;
import org.apache.tools.ant.util.LinkedHashtable;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import reportUtils.ExtTest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Hashtable;

import static com.aventstack.extentreports.Status.SKIP;
import static common.Constants.OUTPUT_PATH;
import static common.Constants.PROJECT_PATH;
import static common.Constants.TEST_DATA_JSON;

public class TestBase {

    public ExtentTest logSuite;
    protected  String stepDescription;
    private String testCaseName;

    @BeforeSuite
    public void beforeSuite() throws IOException {


//
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String reportFilePath = OUTPUT_PATH + "/report-" + df.format(now) + ".html";

        Constants.REPORT = new ExtentReports();
        Constants.SPARK = new ExtentSparkReporter(reportFilePath);
        Constants.SPARK.loadXMLConfig(PROJECT_PATH + "/extent-config.xml");

        Constants.REPORT.attachReporter(Constants.SPARK);
    }

    @BeforeClass
    public synchronized void beforeClass() {
        testCaseName = this.getClass().getSimpleName();
    }

    @BeforeMethod
    public synchronized void beforeMethod(Object[] data) {

        if (data != null && data.length > 0) {
            // Get test data for test case
            Hashtable<String, String> dataTest = (Hashtable<String, String>) data[0];

            //Initialize log suite
            logSuite = Constants.REPORT.createTest(dataTest.get("TestDataPurpose"));

            // Initial logMethod
            ExtTest.setTest(logSuite.createNode(testCaseName, dataTest.get("TestDataPurpose")));

        } else {
            logSuite = Constants.REPORT.createTest(this.testCaseName);
            logSuite.log(SKIP, "This test case has no data to run");
        }
    }

    @AfterSuite(alwaysRun = true)
    public void stopServer() {
        Constants.REPORT.flush();
    }

    @DataProvider
    public Object[][] getDataForTest() {
        String DataFilePath = TEST_DATA_JSON + this.getClass().getPackage().getName().replace(".", "/") + "/data.json";
        Object[][] data = getData(testCaseName, DataFilePath);
        return data;
    }

    public static Object[][] jsonArrayToObjectArray(JsonArray jsonArray) {

        Object[][] data = new Object[0][1];
        int index = 0;
        Gson gson = new Gson();

        if (jsonArray.size() > 0) {
            data = new Object[jsonArray.size()][1];
            for (Object obj : jsonArray) {
                Hashtable<String, Object> hashTable = new LinkedHashtable();
                data[index][0] = gson.fromJson((JsonElement) obj, hashTable.getClass());
                index++;
            }
        }
        return data;
    }

    public static Object[][] getData(String testName, String dataFilePath) {
        //Read json file data using Gson library
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(dataFilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        JsonObject jsonObject = new Gson().fromJson(br, JsonObject.class);

        //Get test data for the specific test case
        JsonArray jsonArray = jsonObject.getAsJsonArray(testName);
        Object[][] data = jsonArrayToObjectArray(jsonArray);
        return data;
    }

}
