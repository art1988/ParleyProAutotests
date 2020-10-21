package constants;

import model.User;

import java.io.File;

public class Const
{
    public static final String QA_TENANT_URL                = "http://qa-autotests.parleypro.net/master/index.html";
    public static final User   PREDEFINED_USER_CN_ROLE      = new User("autotest_cn fn", "ln", "arthur.khasanov+autotestcn@parleypro.com", "Parley650!");

    public static final File   CONTRACT_LIFECYCLE_SAMPLE     = new File(System.getProperty("user.dir") + "/Contracts/pramata.docx");
    public static final File   CONTRACT_DISCUSSIONS_SAMPLE   = new File(System.getProperty("user.dir") + "/Contracts/AT-14.docx");
    public static final File   CONTRACT_FORMATTING_SAMPLE    = new File(System.getProperty("user.dir") + "/Contracts/Formatting.docx");
    public static final File   CONTRACT_NUMBERED_LIST_SAMPLE = new File(System.getProperty("user.dir") + "/Contracts/NumberedList.docx");

    public static final File   DOWNLOAD_DIR                 = new File(System.getProperty("user.dir") + "/Downloads");
    public static final File   SCREENSHOTS_DIR              = new File(System.getProperty("user.dir") + "/Screenshots");


    private Const() {}
}
