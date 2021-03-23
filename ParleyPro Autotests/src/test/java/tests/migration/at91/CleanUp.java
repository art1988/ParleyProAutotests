package tests.migration.at91;

import constants.Const;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.ScreenShotOnFailListener;

import java.io.IOException;

@Listeners({ScreenShotOnFailListener.class})
public class CleanUp
{
    @Test
    public void clearDownloadFolder() throws IOException
    {
        FileUtils.deleteDirectory(Const.DOWNLOAD_DIR);
    }
}
