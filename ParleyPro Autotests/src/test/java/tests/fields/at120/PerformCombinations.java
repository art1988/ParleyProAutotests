package tests.fields.at120;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import forms.ContractInformation;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class PerformCombinations
{
    private ContractInformation contractInformation;

    @BeforeMethod
    public void openContractInformation()
    {
        contractInformation = new DashboardPage().getSideBar()
                                                 .clickInProgressContracts(true)
                                                 .clickNewContractButton();
    }

    @AfterMethod()
    public void closeContractInformation()
    {
        contractInformation.clickCancel();
    }

    @Test(priority = 1)
    public void cat2type3()
    {
        contractInformation.setContractCategory("category2");
        contractInformation.setContractType("type3");
        $(".modal-content .spinner").waitUntil(Condition.disappear, 7_000);

        for( int n = 1; n <= 7; n++ )
        {
            if( n == 7 )
            {
                Assert.assertTrue(Selenide.executeJavaScript("return $('label:contains(\"Field" + n + "\")').length === 1"), "Looks like that Field7 is NOT on the form !!!");
                Assert.assertEquals(Selenide.executeJavaScript("return $('label:contains(\"Field" + n + "\")').next().prop(\"tagName\")"), "TEXTAREA", "Field7 is not <textarea> !!!");
                continue;
            }

            Assert.assertFalse(Selenide.executeJavaScript("return $('label:contains(\"Field" + n + "\")').length === 1"), "Looks like that Field" + n + " IS on form, but shouldn't !!!");
        }

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void cat1type3()
    {
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type3");
        $(".modal-content .spinner").waitUntil(Condition.disappear, 7_000);

        for( int n = 1; n <= 7; n++ )
        {
            if( n == 1 || n == 2 || n == 7 )
            {
                Assert.assertTrue(Selenide.executeJavaScript("return $('label:contains(\"Field" + n + "\")').length === 1"), "Looks like that Field" + n + " is NOT on the form !!!");
                continue;
            }

            Assert.assertFalse(Selenide.executeJavaScript("return $('label:contains(\"Field" + n + "\")').length === 1"), "Looks like that Field" + n + " IS on form, but shouldn't !!!");
        }

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    public void cat2type1()
    {
        contractInformation.setContractCategory("category2");
        contractInformation.setContractType("type1");
        $(".modal-content .spinner").waitUntil(Condition.disappear, 7_000);

        for( int n = 1; n <= 7; n++ )
        {
            if( n == 1 || n == 3 || n == 6 || n == 7 )
            {
                Assert.assertTrue(Selenide.executeJavaScript("return $('label:contains(\"Field" + n + "\")').length === 1"), "Looks like that Field" + n + " is NOT on the form !!!");
                continue;
            }

            Assert.assertFalse(Selenide.executeJavaScript("return $('label:contains(\"Field" + n + "\")').length === 1"), "Looks like that Field" + n + " IS on form, but shouldn't !!!");
        }

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 4)
    public void cat2type2()
    {
        contractInformation.setContractCategory("category2");
        contractInformation.setContractType("type2");
        $(".modal-content .spinner").waitUntil(Condition.disappear, 7_000);

        for( int n = 1; n <= 7; n++ )
        {
            if( n == 4 || n == 6 || n == 7 )
            {
                Assert.assertTrue(Selenide.executeJavaScript("return $('label:contains(\"Field" + n + "\")').length === 1"), "Looks like that Field" + n + " is NOT on the form !!!");
                continue;
            }
            else if( n == 5 )
            {
                Assert.assertTrue(Selenide.executeJavaScript("return $('.radio-group__label:contains(\"Field5\")').length === 1"), "Looks like that Field" + n + " is NOT on the form !!!");
                Assert.assertEquals(Selenide.executeJavaScript("return $('.radio-group__label:contains(\"Field5\")').next().text()"), "radio 1radio 2", "There are no radio options !!!");
                continue;
            }

            Assert.assertFalse(Selenide.executeJavaScript("return $('label:contains(\"Field" + n + "\")').length === 1"), "Looks like that Field" + n + " IS on form, but shouldn't !!!");
        }

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 5)
    public void cat1type1()
    {
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        $(".modal-content .spinner").waitUntil(Condition.disappear, 7_000);

        for( int n = 1; n <= 7; n++ )
        {
            if( n == 4 || n == 5 )
            {
                Assert.assertFalse(Selenide.executeJavaScript("return $('label:contains(\"Field" + n + "\")').length === 1"), "Looks like that Field" + n + " IS on form, but shouldn't !!!");
                continue;
            }

            Assert.assertTrue(Selenide.executeJavaScript("return $('label:contains(\"Field" + n + "\")').length === 1"), "Looks like that Field" + n + " is NOT on the form !!!");
        }

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 6)
    public void cat1type2()
    {
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type2");
        $(".modal-content .spinner").waitUntil(Condition.disappear, 7_000);

        for( int n = 1; n <= 7; n++ )
        {
            if( n == 3 )
            {
                Assert.assertFalse(Selenide.executeJavaScript("return $('label:contains(\"Field" + n + "\")').length === 1"), "Looks like that Field" + n + " IS on form, but shouldn't !!!");
                continue;
            }
            else if( n == 5 )
            {
                Assert.assertTrue(Selenide.executeJavaScript("return $('.radio-group__label:contains(\"Field5\")').length === 1"), "Looks like that Field" + n + " is NOT on the form !!!");
                Assert.assertEquals(Selenide.executeJavaScript("return $('.radio-group__label:contains(\"Field5\")').next().text()"), "radio 1radio 2", "There are no radio options !!!");
                continue;
            }

            Assert.assertTrue(Selenide.executeJavaScript("return $('label:contains(\"Field" + n + "\")').length === 1"), "Looks like that Field" + n + " is NOT on the form !!!");
        }

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 7)
    public void cat1type1type2()
    {
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType(new String[]{"type1", "type2"});
        $(".modal-content .spinner").waitUntil(Condition.disappear, 7_000);

        for( int n = 1; n <= 7; n++ )
        {
            if( n == 5 )
            {
                Assert.assertTrue(Selenide.executeJavaScript("return $('.radio-group__label:contains(\"Field5\")').length === 1"), "Looks like that Field" + n + " is NOT on the form !!!");
                Assert.assertEquals(Selenide.executeJavaScript("return $('.radio-group__label:contains(\"Field5\")').next().text()"), "radio 1radio 2", "There are no radio options !!!");
                continue;
            }

            Assert.assertTrue(Selenide.executeJavaScript("return $('label:contains(\"Field" + n + "\")').length === 1"), "Looks like that Field" + n + " is NOT on the form !!!");
        }

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 8)
    public void cat2type1type2()
    {
        contractInformation.setContractCategory("category2");
        contractInformation.setContractType(new String[]{"type1", "type2"});
        $(".modal-content .spinner").waitUntil(Condition.disappear, 7_000);

        for( int n = 1; n <= 7; n++ )
        {
            if( n == 2 )
            {
                Assert.assertFalse(Selenide.executeJavaScript("return $('label:contains(\"Field" + n + "\")').length === 1"), "Looks like that Field" + n + " IS on form, but shouldn't !!!");
                continue;
            }

            if( n == 5 )
            {
                Assert.assertTrue(Selenide.executeJavaScript("return $('.radio-group__label:contains(\"Field5\")').length === 1"), "Looks like that Field" + n + " is NOT on the form !!!");
                Assert.assertEquals(Selenide.executeJavaScript("return $('.radio-group__label:contains(\"Field5\")').next().text()"), "radio 1radio 2", "There are no radio options !!!");
                continue;
            }

            Assert.assertTrue(Selenide.executeJavaScript("return $('label:contains(\"Field" + n + "\")').length === 1"), "Looks like that Field" + n + " is NOT on the form !!!");
        }

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 9)
    public void cat2type1type2type3()
    {
        contractInformation.setContractCategory("category2");
        contractInformation.setContractType(new String[]{"type1", "type2", "type3"});
        $(".modal-content .spinner").waitUntil(Condition.disappear, 7_000);

        for( int n = 1; n <= 7; n++ )
        {
            if( n == 2 )
            {
                Assert.assertFalse(Selenide.executeJavaScript("return $('label:contains(\"Field" + n + "\")').length === 1"), "Looks like that Field" + n + " IS on form, but shouldn't !!!");
                continue;
            }

            if( n == 5 )
            {
                Assert.assertTrue(Selenide.executeJavaScript("return $('.radio-group__label:contains(\"Field5\")').length === 1"), "Looks like that Field" + n + " is NOT on the form !!!");
                Assert.assertEquals(Selenide.executeJavaScript("return $('.radio-group__label:contains(\"Field5\")').next().text()"), "radio 1radio 2", "There are no radio options !!!");
                continue;
            }

            Assert.assertTrue(Selenide.executeJavaScript("return $('label:contains(\"Field" + n + "\")').length === 1"), "Looks like that Field" + n + " is NOT on the form !!!");
        }

        Screenshoter.makeScreenshot();
    }
}
