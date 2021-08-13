package tests.formatting.at164;

import constants.Const;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import java.io.IOException;

public class CleanDownloadDir
{
    @Test
    public void cleanDownloadsDir() throws IOException
    {
        FileUtils.deleteDirectory(Const.DOWNLOAD_DIR);
    }
}
