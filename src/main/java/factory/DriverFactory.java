package factory;

import constants.FileLocationConstants;
import exceptions.BrowserExceptions;
import exceptions.EnvironmentExceptions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.log4testng.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class DriverFactory {


    private static ThreadLocal<WebDriver> threadLocal = new ThreadLocal<WebDriver>();
    public static Logger log = Logger.getLogger(DriverFactory.class);


    public WebDriver initBrowser(Properties prop) {

        String browser = prop.getProperty("browser");
        OptionsFactory optionsFactory = new OptionsFactory(prop);

        switch (browser.trim().toLowerCase()) {

            case "chrome":
                threadLocal.set(new ChromeDriver(optionsFactory.getChromeOptions()));
                break;
            case "firefox":
                threadLocal.set( new FirefoxDriver(optionsFactory.getFirefoxOptions()));
                break;
            case "edge":
                threadLocal.set(new EdgeDriver(optionsFactory.getEdgeOptions()));
                break;
            default:
                throw new BrowserExceptions("Invalid Browser");

        }
        getDriver().manage().window().maximize();
        getDriver().manage().deleteAllCookies();
        getDriver().get(prop.getProperty("url"));
        return getDriver();
    }



    public static WebDriver getDriver(){
        return threadLocal.get();
    }

    public static void removeDriver(){
        threadLocal.remove();
    }


    public Properties initProperties() {

       String envName =  System.getProperty("env");
       FileInputStream fileInputStream;

       if(envName==null){
           try {
               fileInputStream = new FileInputStream(FileLocationConstants.DEFAULT_CONFIG_FILE_PATH);
           } catch (FileNotFoundException e) {
               throw new RuntimeException(e);
           }
       }
       else {
           switch(envName){
               case "qa":
                   try {
                       fileInputStream = new FileInputStream(FileLocationConstants.QA_CONFIG_FILE_PATH);
                   } catch (FileNotFoundException ex) {
                       throw new RuntimeException(ex);
                   }
                   break;
               case "dev":
                   try {
                       fileInputStream = new FileInputStream(FileLocationConstants.DEV_CONFIG_FILE_PATH);
                   } catch (FileNotFoundException ex) {
                       throw new RuntimeException(ex);
                   }
                   break;
               case "stage":
                   try {
                       fileInputStream = new FileInputStream(FileLocationConstants.STAGE_CONFIG_FILE_PATH);
                   } catch (FileNotFoundException ex) {
                       throw new RuntimeException(ex);
                   }
                   break;
               case "uat":
                   try {
                       fileInputStream = new FileInputStream(FileLocationConstants.UAT_CONFIG_FILE_PATH);
                   } catch (FileNotFoundException ex) {
                       throw new RuntimeException(ex);
                   }
                   break;
               default:
                   throw new EnvironmentExceptions("Invalid Environment");
           }
           }


        Properties prop = new Properties();

        try {
            prop.load(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return prop;
    }



    public static File getScreenshotFile(){
        return  ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);

    }

    public static byte[] getScreenshotBytes(){
        return  ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);

    }
    public static String getScreenshotBase64(){
        return  ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BASE64);

    }
}
