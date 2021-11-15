package tests.basics.at188;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Description;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.OpenedContract;
import pages.subelements.CKEditorActive;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class PerformMentioning
{
    private CKEditorActive ckEditorInstance;
    private OpenedContract openedContract;


    @Test(priority = 1)
    @Description("This test verifies that after typing @ popup with users is visible.")
    public void mayInvokePopupByTypingAt()
    {
        openedContract = new OpenedContract();

        ckEditorInstance = openedContract.clickByParagraph("Paragraph 1");

        ckEditorInstance.getCommentInstance().sendKeys("@");

        SelenideElement popup = $$(".atwho-view").filterBy(Condition.visible).shouldHaveSize(1).first();
        popup.findAll("ul li").shouldHave(CollectionCondition.sizeGreaterThan(3));
        Screenshoter.makeScreenshot();

        ckEditorInstance.clickCancel();
    }

    @Test(priority = 2)
    @Description("This test mentions two users (Felix, Mary) and creates discussion.")
    public void mentionTwoUsers() throws InterruptedException
    {
        ckEditorInstance = openedContract.clickByParagraph("Paragraph 2");

        ckEditorInstance.getCommentInstance().sendKeys("@");
        SelenideElement popup = $$(".atwho-view").filterBy(Condition.visible).shouldHaveSize(1).first();
        popup.findAll("ul li").filterBy(Condition.text("Felix")).first().click();
        Thread.sleep(1_000);

        ckEditorInstance.getCommentInstance().sendKeys("@");
        popup = $$(".atwho-view").filterBy(Condition.visible).shouldHaveSize(1).first();
        popup.findAll("ul li").filterBy(Condition.text("Mary")).first().click();
        Thread.sleep(1_000);

        ckEditorInstance.setComment(" two users were mentioned...").clickPost();
        $(".notification-stack").shouldHave(Condition.text(" has been successfully created."));


    }
}
