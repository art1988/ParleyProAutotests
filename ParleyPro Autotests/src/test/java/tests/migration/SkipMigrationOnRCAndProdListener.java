package tests.migration;

import org.apache.log4j.Logger;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;
import utils.LoginBase;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class SkipMigrationOnRCAndProdListener implements IAnnotationTransformer
{
    private static Logger logger = Logger.getLogger(SkipMigrationOnRCAndProdListener.class);


    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod)
    {
        if(LoginBase.isRc() || LoginBase.isProd())
        {
            logger.warn("Do not run Migration suite on RC/PROD ! Skipping !");
            annotation.setEnabled(false);
        }
    }
}
