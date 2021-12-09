package forms.profile;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DisplayName
{
    private SelenideElement firstNameField = $("input[data-label='First name']");
    private SelenideElement lastNameField  = $("input[data-label='Last name']");

    private Logger logger = Logger.getLogger(DisplayName.class);


    public DisplayName()
    {
        $(".modal-body div[class*='styles__rename_title']").shouldBe(Condition.visible).shouldHave(Condition.exactText("Display name"));
        $$(".modal-body button").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("CANCEL", "APPLY"));
    }

    public DisplayName setFirstName(String firstName)
    {
        firstNameField.clear();
        firstNameField.sendKeys(firstName);

        return this;
    }

    public DisplayName setLastName(String lastName)
    {
        lastNameField.clear();
        lastNameField.sendKeys(lastName);

        return this;
    }

    public void clickApply()
    {
        $$(".modal-body button").filterBy(Condition.exactText("APPLY")).first().shouldBe(Condition.visible, Condition.enabled).click();

        logger.info("APPLY button was clicked");

        $(".modal-body").should(Condition.disappear);
    }
}
