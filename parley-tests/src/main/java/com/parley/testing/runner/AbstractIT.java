package com.parley.testing.runner;

import com.parley.testing.configuration.TestsConfiguration;
import com.parley.testing.pages.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.*;


@TestExecutionListeners(inheritListeners = false, listeners = DependencyInjectionTestExecutionListener.class)
@ContextConfiguration(classes = TestsConfiguration.class)
public class AbstractIT extends AbstractTestNGSpringContextTests implements ITest {

    private String testInstanceName = "";

    @Autowired
    protected PageFactory pageFactory;

    @Override
    public String getTestName() {
        return testInstanceName;
    }

    public void setTestName(String testInstanceName) {
        this.testInstanceName = testInstanceName;
    }

    public void handleSkipMessage(String skipMessage){
        if(skipMessage != null) throw new SkipException(skipMessage);
    }

    public static void report(String stepText, String stepDetails) {
        Reporter.log("<b>" + stepText + "</b>" + "&nbsp" + stepDetails + "<p>", true);
    }
    public static void report(String stepText) {
        Reporter.log(stepText + "<p>", true);
    }

}
