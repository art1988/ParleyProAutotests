package forms.profile;

import com.codeborne.selenide.Condition;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CropImage
{
    private static Logger logger = Logger.getLogger(CropImage.class);


    public CropImage()
    {
        $(".modal-body div[class*='styles__title']").shouldBe(Condition.visible).shouldHave(Condition.exactText("Crop image"));
    }

    public void clickApply()
    {
        $$(".modal-body button").findBy(Condition.exactText("APPLY")).shouldBe(Condition.visible, Condition.enabled).click();

        logger.info("APPLY button was clicked");

        $(".modal-body").should(Condition.disappear);
    }
}
