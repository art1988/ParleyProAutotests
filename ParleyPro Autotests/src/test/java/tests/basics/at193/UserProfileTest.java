package tests.basics.at193;

import com.codeborne.selenide.Condition;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.Profile;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class UserProfileTest
{
    private Profile profilePage;
    private static Logger logger = Logger.getLogger(UserProfileTest.class);


    @Test(priority = 1)
    @Description("This test changes user's First name and Last name and checks that changes are applied.")
    public void changeNameTest() throws InterruptedException
    {
        profilePage = new DashboardPage().getSideBar().clickProfile();

        logger.info("Making sure that prev. name is visible...");
        Assert.assertEquals(profilePage.getUsername(), "autotest_cn fn ln", "User name is not displayed !!!");

        profilePage.clickEditName()
                   .setFirstName("Piotr")
                   .setLastName("Skolka")
                   .clickApply();
        Thread.sleep(1_000);

        logger.info("Making sure that new name was applied...");
        Assert.assertEquals(profilePage.getUsername(), "Piotr Skolka", "User name has not been changed !!!");

        logger.info("Making sure avatar initials were changed too...");
        Assert.assertEquals($("div[class*='styles__avatar'] > span").getText(), "PS", "Avatar initials has not been changed !!!");

        Screenshoter.makeScreenshot();

        logger.info("Changing username back to initial state...");
        profilePage.clickEditName()
                   .setFirstName("autotest_cn fn")
                   .setLastName("ln")
                   .clickApply();
        Thread.sleep(1_000);

        Assert.assertEquals(profilePage.getUsername(), "autotest_cn fn ln", "User name has not been changed !!!");
        Assert.assertEquals($("div[class*='styles__avatar'] > span").getText(), "AL", "Avatar initials has not been changed !!!");
    }

    @Test(priority = 2)
    @Description("This test uploads user's avatar, checks that image was applied and removes it, so that user's initials are visible again.")
    public void uploadUserAvatarTest() throws InterruptedException
    {
        logger.info("Uploading user's avatar...");
        profilePage.uploadAvatar(Const.AVATAR_IMG_SAMPLE).clickApply();

        $(".spinner").should(Condition.disappear);

        $("div[class*='styles__avatar'] img").shouldBe(Condition.visible);
        Assert.assertTrue($("div[class*='styles__avatar'] img").isImage(), "Avatar has not been loaded !!!");

        logger.info("Check that in left bottom corner image was also applied...");
        $(".page-menu span img").shouldBe(Condition.visible);
        Assert.assertTrue($(".page-menu span img").isImage(), "Avatar has not been applied in left bottom corner !!!");

        Screenshoter.makeScreenshot();

        profilePage.removeAvatar();

        logger.info("Check that initials are back...");
        $("div[class*='styles__avatar'] > span").shouldHave(Condition.exactText("AL"));
        Assert.assertEquals(profilePage.getUsername(), "autotest_cn fn ln", "User name has not been changed !!!");
    }
}
