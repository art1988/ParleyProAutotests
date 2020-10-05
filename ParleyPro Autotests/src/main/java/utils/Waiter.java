package utils;

import com.codeborne.selenide.Selenide;

/**
 * Util class that have useful wait methods
 */
public class Waiter
{
    /**
     * Smart wait. Poll every 500 milliseconds of the given jQuerySelector until it is visible
     * @param jQuerySelector like $('.document__body-content')
     */
    public static void smartWaitUntilVisible(String jQuerySelector)
    {
        while ( true )
        {
            boolean isElementVisible = Selenide.executeJavaScript("return " + jQuerySelector + ".is(':visible')");

            if( isElementVisible )
            {
                break;
            }
            else
            {
                try
                {
                    Thread.sleep(500);
                    continue;
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
