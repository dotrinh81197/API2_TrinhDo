package common;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class Constants {
    public static ExtentReports REPORT;
    public static ExtentSparkReporter SPARK;
    public static final String PROJECT_PATH = System.getProperty("user.dir");
    public static String OUTPUT_PATH = PROJECT_PATH.concat("/output/");
    public static final String TEST_DATA_JSON = PROJECT_PATH.concat("/src/test/java/");
    public static final String PROPERTIES_FILE = "src/main/resources/config.properties";
    public static final String BASE_URI = "https://classroom.googleapis.com";
    public static String REFRESH_TOKEN;


}
