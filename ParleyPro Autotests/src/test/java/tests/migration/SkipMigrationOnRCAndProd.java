package tests.migration;

import org.apache.log4j.Logger;
import org.testng.SkipException;
import org.testng.annotations.Test;
import utils.LoginBase;

public class SkipMigrationOnRCAndProd
{
    private static Logger logger = Logger.getLogger(SkipMigrationOnRCAndProd.class);

    @Test
    public void skipMigrationOnRCAndPROD()
    {
        if(LoginBase.isRc() || LoginBase.isProd())
        {
            logger.warn("Do not run Migration suite on RC/PROD ! Skipping !");
            throw new SkipException("Do not run Migration suite on RC/PROD ! Skipping !");
        }
    }
}
