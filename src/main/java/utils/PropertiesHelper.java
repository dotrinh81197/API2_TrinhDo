package utils;

import common.Constants;
import data.Key;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesHelper {

    public static String getProperty(Key key) {
        Properties props = new Properties();
        InputStream input;
        try {
            input = new FileInputStream(Constants.PROPERTIES_FILE);
            props.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return props.getProperty(key.getText());
    }

}
