package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import forms.CompleteSign;
import forms.SignContract;
import forms.StartNegotiation;
import forms.StartReview;
import org.apache.log4j.Logger;
import org.testng.Assert;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;

public class OpenedContract
{
    private SelenideElement contractName = $(".contract-header__name");


    private static Logger logger = Logger.getLogger(OpenedContract.class);

    public OpenedContract()
    {
        Assert.assertTrue( isInit() );
    }

    private boolean isInit()
    {
        $(".contract-header__status").shouldBe(Condition.visible);

        return ( contractName.isDisplayed() );
    }

    public StartReview switchDocumentToReview()
    {
        // hover over DRAFT
        StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true});");
        jsCode.append("$('.document__header-info .lifecycle')[0].dispatchEvent(event);");

        Selenide.executeJavaScript(jsCode.toString());

        Waiter.smartWaitUntilVisible("$('.document__header-info .lifecycle').find(\"div:contains('REVIEW')\")");

        Selenide.executeJavaScript("$('.document__header-info .lifecycle').find(\"div:contains('REVIEW')\").click()");

        logger.info("REVIEW was clicked");

        return new StartReview(contractName.text());
    }

    public StartNegotiation switchDocumentToNegotiate()
    {
        // hover over DRAFT
        StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true});");
        jsCode.append("$('.document__header-info .lifecycle')[0].dispatchEvent(event);");

        Selenide.executeJavaScript(jsCode.toString());

        Waiter.smartWaitUntilVisible("$('.document__header-info .lifecycle').find(\"div:contains('NEGOTIATE')\")");

        Selenide.executeJavaScript("$('.document__header-info .lifecycle').find(\"div:contains('NEGOTIATE')\").click()");

        logger.info("NEGOTIATE was clicked");

        return new StartNegotiation(contractName.text());
    }

    public SignContract switchDocumentToSign()
    {
        // hover over DRAFT
        StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true});");
        jsCode.append("$('.document__header-info .lifecycle')[0].dispatchEvent(event);");

        Selenide.executeJavaScript(jsCode.toString());

        Waiter.smartWaitUntilVisible("$('.document__header-info .lifecycle').find(\"div:contains('SIGN')\")");

        Selenide.executeJavaScript("$('.document__header-info .lifecycle').find(\"div:contains('SIGN')\").click()");

        logger.info("SIGN was clicked");

        return new SignContract(contractName.text());
    }

    public CompleteSign clickCompleteSign()
    {
        Selenide.executeJavaScript("$('.document__title button[spinnersize=\"xs\"]').click()");

        logger.info("COMPLETE SIGN was clicked");

        return new CompleteSign(contractName.text());
    }
}
