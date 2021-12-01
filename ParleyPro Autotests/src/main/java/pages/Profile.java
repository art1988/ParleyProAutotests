package pages;

import com.codeborne.selenide.Condition;
import forms.profile.CropImage;
import forms.profile.DisplayName;
import org.apache.log4j.Logger;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class Profile
{
    private static Logger logger = Logger.getLogger(Profile.class);


    public Profile()
    {
        $(".page-head").shouldBe(Condition.visible).shouldHave(Condition.exactText("My Profile"));
        $("div[class^='styles__avatar']").shouldBe(Condition.visible);
        $$("div[class^='styles__container'] div").filterBy(Condition.text("My roles")).first().shouldBe(Condition.visible);
    }

    /**
     * Get username label string as 'First name' + 'First name'
     * @return
     */
    public String getUsername()
    {
        return $("div[class*='styles__username']").text();
    }

    public DisplayName clickEditName()
    {
        $("div[class*='styles__username']").parent().find("button").click();

        logger.info("Clicked by edit button of name");

        return new DisplayName();
    }

    /**
     * Upload avatar for user
     * @param avatarImage accepts only images files
     */
    public CropImage uploadAvatar(File avatarImage) throws InterruptedException
    {
        // hover image
        $("div[class*='styles__avatar']").hover();
        Thread.sleep(500);

        $("button input[type='file']").uploadFile(avatarImage);

        return new CropImage();
    }
}
