package com.parley.testing.listener;

import com.parley.testing.aws.AwsService;
import com.parley.testing.context.ApplicationContextHolder;
import com.parley.testing.utils.ZipUtils;
import org.apache.maven.surefire.shade.common.org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class UITestListener implements ITestListener {

    @Autowired
    private ApplicationContextHolder applicationContext;

    private AwsService awsService;
    private WebDriver driver;
    private String currentDir = System.getProperty("user.dir");

    public UITestListener() {
        System.out.println();
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {

    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {

    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        String methodName = iTestResult.getMethod().getMethodName();
        driver = (WebDriver) applicationContext.getBean(WebDriver.class);
        takeScreenShot(methodName, driver);

    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(ITestContext iTestContext) {

    }

    @Override
    public void onFinish(ITestContext iTestContext) {

    }

    private void takeScreenShot(String methodName, WebDriver driver) {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            String basePath = currentDir + File.separator + "target" + File.separator;
            String folderPath = basePath + "screenshots";
            FileUtils.copyFile(scrFile, new File(folderPath + File.separator + methodName + ".png"));
            System.out.println("***Placed screen shot in " + folderPath + " ***");
            Boolean isSentToAws = Boolean.valueOf(System.getProperty("sent.to.aws"));
            if (isSentToAws) {
                String zipFolder = basePath + "zip";
                String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                String zipName = zipFolder +  File.separator + "screenshots" + date + ".zip";
                ZipUtils.zipFolder(folderPath, zipFolder, zipName);
                awsService = (AwsService) applicationContext.getBean(AwsService.class);
                awsService.uploadDocumentToS3(zipName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
