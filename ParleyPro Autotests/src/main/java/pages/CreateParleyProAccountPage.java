package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.testng.Assert;

import static com.codeborne.selenide.Selenide.$;

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
        $(".auth__title").waitUntil(Condition.visible, 20_000).shouldHave(Condition.exactText("Create Parley Pro account"));
        Assert.assertEquals($(".auth__head").waitUntil(Condition.visible, 20_000).attr("href"), "http://parleypro.com/");

        String backgroundImage = $(".auth__head").getCssValue("background-image");
        System.out.println("backgroundImage = " + backgroundImage);
        Assert.assertTrue(backgroundImage.endsWith("images/cc8124f8be69a02e7221cfaabe5a0ef1.svg\")"));
                                                 // images/cc8124f8be69a02e7221cfaabe5a0ef1.svg
    }

    public void setFirstName(String firstName)
    {
        firstNameField.sendKeys(firstName);
    }

    public void setLastName(String lastName)
    {
        lastNameField.sendKeys(lastName);
    }

    public void setPassword(String password)
    {
        passwordField.sendKeys(password);
    }

    public void setConfirmPassword(String confirmPassword)
    {
        confirmPasswordField.sendKeys(confirmPassword);
    }

    public OpenedContract clickCreateAndSignIn()
    {
        createAndSignInButton.click();

        logger.info("CREATE AND SIGN IN button was clicked");

        return new OpenedContract();
    }
}
