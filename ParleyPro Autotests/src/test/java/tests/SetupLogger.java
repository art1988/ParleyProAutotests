package tests;

import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.BeforeSuite;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SetupLogger
{
    @BeforeSuite
    public static void setupLog4j()
    {
        System.out.println("Setting up log4j...");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hhmmss");
        System.setProperty("current_date", dateFormat.format(new Date()));

        PropertyConfigurator.configure(System.getProperty("user.dir") + "/src/main/resources/log4j.properties");
    }
}
