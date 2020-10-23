package utils;

import com.codeborne.selenide.testng.ScreenShooter;
import org.testng.ITestResult;

/**
 * Screenshot listener on failed tests.
 * Triggers every time test fails, make screenshot and attaches it for Allure report.
 */
public class ScreenShotOnFailListener extends ScreenShooter
{
    @Override
    public void onTestFailure(ITestResult result)
    {
        super.onTestFailure(result);

        // Attach this screenshot for Allure report
        Screenshoter.makeScreenshot();
    }
}
