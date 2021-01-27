package constants;

import model.User;

import java.io.File;

public class Const
{
    // Tenants
    public static final String QA_TENANT_URL                 = "https://qa-autotests.parleypro.net/master/index.html";
    public static final String PROD_TENANT_URL               = "https://qa-autotests.parleypro.com";
    public static final String AT50_TENANT_URL               = "https://at50.parleypro.net/master/index.html";
    public static final String RC_TENANT_URL                 = "https://qa-autotests.parleypro.net/rc/index.html";

    // Users
    public static final User   PREDEFINED_USER_CN_ROLE       = new User("autotest_cn fn", "ln", "arthur.khasanov+autotestcn@parleypro.com", "Parley650!");
    public static final User   PREDEFINED_APPROVER_USER_1    = new User("Approval_User_1", "", "arthur.khasanov+approval1@parleypro.com", "Parley650!");
    public static final User   PREDEFINED_APPROVER_USER_2    = new User("Approval_User_2", "", "arthur.khasanov+approval2@parleypro.com", "Parley650!");
    public static final User   PREDEFINED_INTERNAL_USER_1    = new User("Internal user1", "Internal user1 last name", "arthur.khasanov+team1@parleypro.com", "Parley650!");
    public static final User   PREDEFINED_INTERNAL_USER_2    = new User("Internal user2", "Internal user2 last name", "arthur.khasanov+team2@parleypro.com", "Parley650!");

    public static final User   USER_GREG                     = new User("Greg", "Smith", "arthur.khasanov+greg@parleypro.com", "Parley650!");
    public static final User   USER_MARY                     = new User("Mary", "Jones", "arthur.khasanov+mary@parleypro.com", "Parley650!");
    public static final User   USER_FELIX                    = new User("Felix", "Wilson", "arthur.khasanov+felix@parleypro.com", "Parley650!");

    public static final User   EVHEN_AT50_USER               = new User("Parley", "Pro", "yevhen.uvin+at50@parleypro.com", "Parley650!");

    // Document samples
    public static final File   DOCUMENT_LIFECYCLE_SAMPLE     = new File(System.getProperty("user.dir") + "/Documents/pramata.docx");
    public static final File   DOCUMENT_DISCUSSIONS_SAMPLE   = new File(System.getProperty("user.dir") + "/Documents/AT-14.docx");
    public static final File   DOCUMENT_FORMATTING_SAMPLE    = new File(System.getProperty("user.dir") + "/Documents/Formatting.docx");
    public static final File   DOCUMENT_NUMBERED_LIST_SAMPLE = new File(System.getProperty("user.dir") + "/Documents/NumberedList.docx");

    public static final File   DOCUMENT_NUMBERED_WITH_SUBLISTS = new File(System.getProperty("user.dir") + "/Documents/NumberedWithMultipleSubs.docx");

    public static final File   DOCUMENT_CLASSIC_AT40         = new File(System.getProperty("user.dir") + "/Documents/AT-40.docx");
    public static final File   DOCUMENT_CLASSIC_AT40_2       = new File(System.getProperty("user.dir") + "/Documents/AT-40_2.docx");

    public static final File   TEMPLATE_AT48                 = new File(System.getProperty("user.dir") + "/Documents/Template_AT48.docx");
    public static final File   REGRESSION_DOC_AT70           = new File(System.getProperty("user.dir") + "/Documents/Numbered_manual testing MAIN short.docx");
    public static final File   REGRESSION_TEMPLATE_AT46      = new File(System.getProperty("user.dir") + "/Documents/Template_Regression_AT_46.docx");

    // Directories
    // Directory of docs from clients. Used in Classic
    public static final File   CLIENT_DOCS_DIR               = new File(System.getProperty("user.dir") + "/Documents/classicClientDocs");
    // Directory of pdf documents. Used in Basics
    public static final File   PDF_DOCS_DIR                  = new File(System.getProperty("user.dir") + "/Documents/pdfDocs");

    public static final File   ATTACHMENTS_DOCS_DIR          = new File(System.getProperty("user.dir") + "/Documents/attachmentDocs");

    public static final File   DOWNLOAD_DIR                  = new File(System.getProperty("user.dir") + "/Downloads");
    // Dir for screenshots of failed tests
    public static final File   SCREENSHOTS_DIR               = new File(System.getProperty("user.dir") + "/ScreenshotsOfFailedTests");


    private Const() {}
}
