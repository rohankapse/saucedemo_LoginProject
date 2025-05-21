package appModule;

import java.time.Duration;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.github.bonigarcia.wdm.WebDriverManager;

public class base_class {

    public static WebDriver driver;
    public static ExtentReports extent;
    public static ExtentSparkReporter spark;
    public static ExtentTest test;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
    }

    @BeforeTest
    public void startReport() {
        spark = new ExtentSparkReporter(System.getProperty("user.dir") + "/test-output/Navigate_Test.html");

        extent = new ExtentReports();
        extent.attachReporter(spark);

        spark.config().setDocumentTitle("Navigate Site Test Automation Report");
        spark.config().setReportName("Navigate Site Automation Report");
        spark.config().setTheme(Theme.DARK);

        extent.setSystemInfo("Tester", "QA Engineer");
        extent.setSystemInfo("Environment", "Testing");
        extent.setSystemInfo("Project", "SauceDemo Navigation");
    }

    @Test(priority = 1)
    public void navigateToSite() {
        test = extent.createTest("Navigate to SauceDemo");

        try {
            driver.get("https://www.saucedemo.com/");
            test.pass("Navigated to https://www.saucedemo.com/");
            test.info("Page Title: " + driver.getTitle());

            Thread.sleep(2000); // Wait 2 seconds to observe browser before next action

        } catch (Exception e) {
            test.fail("Navigation Test failed due to: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void loginTest() {
        test = extent.createTest("Login into SauceDemo");

        try {
            driver.get("https://www.saucedemo.com/");

            WebElement username = driver.findElement(By.id("user-name"));
            username.sendKeys("standard_user");

            WebElement pass = driver.findElement(By.id("password"));
            pass.sendKeys("secret_sauce"); // Corrected password

            test.pass("Entered username and password");
            
            
            WebElement login_btn = driver.findElement(By.id("login-button"));
            login_btn.click();
            
            Thread.sleep(100);
            
            Alert alert=driver.switchTo().alert();
            
            alert.accept();
            
            Thread.sleep(100);
            
       
            test.pass("Clicked on login button");
            
            
            String expTitle="Swag Labs";
            
            String pageTitle=driver.getTitle();
       
            if(pageTitle.equals(expTitle))
            {
            	test.pass("Test is passed" +pageTitle);
            	
            }
            else
            	
            {
            	test.fail("Page Title is Not Found");
            }
            
            test.pass("Login Test Passed");

            Thread.sleep(3000); // Wait 3 seconds after login

        } catch (Exception e) {
            test.fail("Login Test failed: " + e.getMessage());
        }
    }

    @AfterTest
    public void endReport() {
        extent.flush();
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
          driver.quit();
        }
    }
}
