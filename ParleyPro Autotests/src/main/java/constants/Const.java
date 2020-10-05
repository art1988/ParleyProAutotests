package constants;

import model.User;

import java.io.File;

public class Const
{
    public static final String QA_TENANT_URL           = "http://qa-autotests.parleypro.net/master/index.html";
    public static final User   PREDEFINED_USER_CN_ROLE = new User("autotest_cn fn", "ln", "arthur.khasanov+autotestcn@parleypro.com", "Parley650!");
    public static final File   CONTRACT_SAMPLE         = new File(System.getProperty("user.dir") + "\\Contracts\\pramata.docx");
    public static final File   DOWNLOAD_DIR            = new File(System.getProperty("user.dir") + "\\Downloads");


    private Const() {}
}
