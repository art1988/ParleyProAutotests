package com.parley.testing.pages.impl.dashboard;

import com.parley.testing.pages.AbstractPage;
import org.codehaus.plexus.util.ExceptionUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.parley.testing.utils.AsyncAssert.waitForSuccess;
import static java.lang.String.format;
import static junit.framework.Assert.assertTrue;
import static org.testng.Assert.fail;

public class AbstractDashboardPage extends AbstractPage {

    public static final int TRY_TIMES = 40;
    public static final int TRY_DELAY = 3000;
    public static final By USER_LOGO = By.id("page-menu-account");
    public static final By LOGOUT = By.xpath("//a[text()='Logout']");
    private By iconXpath;

    public AbstractDashboardPage(WebDriver driverProvider, By iconXpath) {
        super(driverProvider);
        if (iconXpath == null) {
            throw new RuntimeException("[iconXpath] parameter is null");
        }
        this.iconXpath = iconXpath;
    }

    @Override
    public void checkCurrentPage() throws Throwable {
        try {
            waitForSuccess(TRY_TIMES, TRY_DELAY, () -> assertTrue(findElement(iconXpath).isDisplayed()));
        } catch (AssertionError e) {
            fail("Page is not Found!");
        } catch (Throwable e) {
            fail(format("Got an unexpected exception: '%1$s' with stack trace: %2$s",
                    e, ExceptionUtils.getFullStackTrace(e)));
        }
    }


    public void checkPageIconNotExists() throws Throwable {
        checkElementDoesNotExist(iconXpath);
    }

    public void moveToPage(){
        move(iconXpath);
    }

    public void logout() throws InterruptedException {
        Thread.sleep(6000);
        WebDriverWait wait2 = new WebDriverWait(getDriver(), 10);
        wait2.until(ExpectedConditions.elementToBeClickable(USER_LOGO));
        findElement(USER_LOGO).click();
        findElement(LOGOUT).click();
    }
}
