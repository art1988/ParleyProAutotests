package pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.testng.Assert;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Page that appears after clicking 'SIGN IN' button from email that allows to create new account
 */
public class CreateParleyProAccountPage
{
    private SelenideElement firstNameField        = $("#first-name");
    private SelenideElement lastNameField         = $("#last-name");
    private SelenideElement passwordField         = $("#password");
    private SelenideElement confirmPasswordField  = $("#confirm-password");

    private SelenideElement createAndSignInButton = $(".button.btn-common.btn.btn-primary.btn-block");

    private static Logger logger = Logger.getLogger(CreateParleyProAccountPage.class);


    public CreateParleyProAccountPage()
    {
        $(".auth__title").shouldBe(Condition.visible).shouldHave(Condition.exactText("Create Parley Pro account"));
        Assert.assertEquals($(".auth__head").waitUntil(Condition.visible, 20_000).attr("href"), "http://parleypro.com/");

        String backgroundImage = $(".auth__head").getCssValue("background-image");
        Assert.assertTrue(backgroundImage.endsWith("images/cc8124f8be69a02e7221cfaabe5a0ef1.svg\")"));

        $$("input").shouldHave(CollectionCondition.size(4));
    }

    private void clearField(SelenideElement input)
    {
        for( int i = 0; i < 50; i++ )
        {
            input.sendKeys(Keys.BACK_SPACE);
        }
    }

    public void setFirstName(String firstName)
    {
        clearField(firstNameField);
        firstNameField.sendKeys(firstName);
    }

    public void setLastName(String lastName)
    {
        clearField(lastNameField);
        lastNameField.sendKeys(lastName);
    }

    public void setPassword(String password)
    {
        clearField(passwordField);
        passwordField.sendKeys(password);
    }

    public void setConfirmPassword(String confirmPassword)
    {
        clearField(confirmPasswordField);
        confirmPasswordField.sendKeys(confirmPassword);
    }

    public OpenedContract clickCreateAndSignIn()
    {
        createAndSignInButton.click();

        logger.info("CREATE AND SIGN IN button was clicked");

        return new OpenedContract();
    }

    /**
     * Use this method if you know that empty page will be opened (no previous contracts will be opened).
     * @param isBlank just marker. Has no meaning
     * @return
     */
    public OpenedContract clickCreateAndSignIn(boolean isBlank)
    {
        createAndSignInButton.click();

        logger.info("CREATE AND SIGN IN button was clicked");

        return new OpenedContract(isBlank);
    }
}
