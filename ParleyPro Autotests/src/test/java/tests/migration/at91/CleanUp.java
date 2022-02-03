package tests.migration.at91;

import constants.Const;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import java.io.IOException;


public class CleanUp
{
    @Test
    public void clearDownloadFolder() throws IOException
    {
        FileUtils.deleteDirectory(Const.DOWNLOAD_DIR);
    }
}
