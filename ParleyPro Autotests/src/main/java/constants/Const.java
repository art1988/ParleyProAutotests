package constants;

import model.User;

import java.io.File;

public class Const
{
    // Users
    public static final User   PREDEFINED_USER_CN_ROLE       = new User("autotest_cn fn", "ln", "arthur.khasanov+autotestcn@parleypro.com", "Parley650!");
    public static final User   PREDEFINED_APPROVER_USER_1    = new User("Approval_User_1", "", "arthur.khasanov+approval1@parleypro.com", "Parley650!");
    public static final User   PREDEFINED_APPROVER_USER_2    = new User("Approval_User_2", "", "arthur.khasanov+approval2@parleypro.com", "Parley650!");
    public static final User   PREDEFINED_INTERNAL_USER_1    = new User("Internal user1", "Internal user1 last name", "arthur.khasanov+team1@parleypro.com", "Parley650!");
    public static final User   PREDEFINED_INTERNAL_USER_2    = new User("Internal user2", "Internal user2 last name", "arthur.khasanov+team2@parleypro.com", "Parley650!");
    public static final User   PREDEFINED_CCN                = new User("CCN AT fn", "CCN AT ln", "arthur.khasanov+ccn_at@parleypro.com", "Parley650!");
    public static final User   PREDEFINED_REQUESTER          = new User("Requester_User", "ln", "arthur.khasanov+requester@parleypro.com", "Parley650!");

    public static final User   USER_GREG                     = new User("Greg", "Smith", "arthur.khasanov+greg@parleypro.com", "Parley650!");
    public static final User   USER_MARY                     = new User("Mary", "Jones", "arthur.khasanov+mary@parleypro.com", "Parley650!");

    /** Predefined user with all roles */
    public static final User   USER_FELIX                    = new User("Felix", "Wilson", "arthur.khasanov+felix@parleypro.com", "Parley650!");

    public static final User   EVHEN_AT50_USER               = new User("Parley", "Pro", "yevhen.uvin+at50@parleypro.com", "Parley650!");
    public static final User   TRACKCHANGES_USER             = new User("Arthur", "Trackchanges", "arthur.khasanov+trackchanges@parleypro.com", "Parley650!");

    public static final User   Aaron_Aaronson                = new User("Aaron", "Aaronson", "arthur.khasanov+aaron.aaronson@parleypro.com", "Parley650!");
    public static final User   Bartholomew_Aaronson          = new User("Bartholomew", "Aaronson", "arthur.khasanov+bartholomew.aaronson@parleypro.com", "Parley650!");
    public static final User   Bartholomew_Bronson           = new User("Bartholomew", "Bronson", "arthur.khasanov+bartholomew.bronson@parleypro.com", "Parley650!");

    public static final User   POPOVERS_MYTEAM_CN            = new User("autotest_cn fn", "ln", "arthur.khasanov+popoverscn@parleypro.com", "Parley650!");
    public static final User   POPOVERS_CCN                  = new User("POP CCN fn", "POP CCN ln", "arthur.khasanov+popoversccn@parleypro.com", "Parley650!");
    public static final User   POPOVERS_VIEWER               = new User("Viewer fn", "ln", "arthur.khasanov+popovers-viewer@parleypro.com", "Parley650!");

    public static final User   VIEWER_PLUS_USER1             = new User("vp1", "ln", "arthur.khasanov+vp1@parleypro.com", "Parley650!");

    // Document samples
    public static final File   DOCUMENT_LIFECYCLE_SAMPLE     = new File(System.getProperty("user.dir") + "/Documents/pramata.docx");
    public static final File   DOCUMENT_DISCUSSIONS_SAMPLE   = new File(System.getProperty("user.dir") + "/Documents/AT-14.docx");
    public static final File   DOCUMENT_FORMATTING_SAMPLE    = new File(System.getProperty("user.dir") + "/Documents/Formatting.docx");
    public static final File   DOCUMENT_NUMBERED_LIST_SAMPLE = new File(System.getProperty("user.dir") + "/Documents/NumberedList.docx");
    public static final File   DOCUMENT_TABLE_SAMPLE         = new File(System.getProperty("user.dir") + "/Documents/tabletest_AT-160.docx");

    public static final File   DOCUMENT_NUMBERED_WITH_SUBLISTS = new File(System.getProperty("user.dir") + "/Documents/NumberedWithMultipleSubs.docx");

    public static final File   DOCUMENT_CLASSIC_AT40         = new File(System.getProperty("user.dir") + "/Documents/AT-40.docx");
    public static final File   DOCUMENT_CLASSIC_AT40_2       = new File(System.getProperty("user.dir") + "/Documents/AT-40_2.docx");

    public static final File   TEMPLATE_AT48                 = new File(System.getProperty("user.dir") + "/Documents/Template_AT48.docx");
    public static final File   TEMPLATE_AT58                 = new File(System.getProperty("user.dir") + "/Documents/nurix_date_problem.docx");
    public static final File   REGRESSION_DOC_AT70           = new File(System.getProperty("user.dir") + "/Documents/Numbered_manual testing MAIN short.docx");
    public static final File   REGRESSION_TEMPLATE_AT46      = new File(System.getProperty("user.dir") + "/Documents/Template_Regression_AT_46.docx");
    public static final File   REGRESSION_DOC_AT75_V1        = new File(System.getProperty("user.dir") + "/Documents/MSW_v1.docx");
    public static final File   REGRESSION_DOC_AT75_V2        = new File(System.getProperty("user.dir") + "/Documents/MSW_v2.docx");

    public static final File   REGRESSION_IMG_DOC            = new File(System.getProperty("user.dir") + "/Documents/imgDocs/image.docx");
    public static final File   REGRESSION_TEXT_AND_IMG_DOC   = new File(System.getProperty("user.dir") + "/Documents/imgDocs/text_and_image.docx");
    public static final File   REGRESSION_FLOATING_IMG_DOC   = new File(System.getProperty("user.dir") + "/Documents/imgDocs/Floating_image.docx");
    public static final File   REGRESSION_FLOATING_TEXT_DOC  = new File(System.getProperty("user.dir") + "/Documents/imgDocs/Floating_text_box.docx");

    public static final File   TEMPLATE_AT77                 = new File(System.getProperty("user.dir") + "/Documents/Template_AT-77_dummy.docx");
    public static final File   TEMPLATE_AT86                 = new File(System.getProperty("user.dir") + "/Documents/Template_AT-86_text_cut_off.docx");
    public static final File   TEMPLATE_AT90_SILENT_ERROR    = new File(System.getProperty("user.dir") + "/Documents/Template_silent_error_AT-90.docx");
    public static final File   TEMPLATE_AT135                = new File(System.getProperty("user.dir") + "/Documents/AT-135_Template_identical.docx");
    public static final File   TEMPLATE_AT164                = new File(System.getProperty("user.dir") + "/Documents/Template_AT-164-Manufacturing_Agreement.docx");
    public static final File   TEMPLATE_TO_UPLOAD_OVER_AT164 = new File(System.getProperty("user.dir") + "/Documents/SimpleTemplate_Word_formatting_AT-164.docx");
    public static final File   TEMPLATE_AT213                = new File(System.getProperty("user.dir") + "/Documents/AT_213_Supply Agreement_DEBUG_.docx");

    public static final File   REGRESSION_DOC_AT83_BDOC1     = new File(System.getProperty("user.dir") + "/Documents/bdoc1.docx");
    public static final File   REGRESSION_DOC_AT83_BDOC2     = new File(System.getProperty("user.dir") + "/Documents/bdoc2.docx");
    public static final File   REGRESSION_DOC_AT83_BDOC3     = new File(System.getProperty("user.dir") + "/Documents/bdoc3.docx");

    public static final File   REGRESSION_DOC_AT141           = new File(System.getProperty("user.dir") + "/Documents/dummyAT141.docx");
    public static final File   REGRESSION_TEMPLATE_DOCX_AT161 = new File(System.getProperty("user.dir") + "/Documents/TemplateDOCXCapital_AT-161.DOCX");
    public static final File   REGRESSION_DOC_AT163           = new File(System.getProperty("user.dir") + "/Documents/CentusAcornsAgreement2021.docx");

    public static final File   DOC_PARAGRAPH_CLONING_AT89_V1 = new File(System.getProperty("user.dir") + "/Documents/ParagraphClonning_AT89.docx");
    public static final File   DOC_PARAGRAPH_CLONING_AT89_V2 = new File(System.getProperty("user.dir") + "/Documents/ParagraphClonning_AT89_with_responses.docx");

    public static final File   CONTRACT_DATA_CSV_ORIGINAL_EXECUTED = new File(System.getProperty("user.dir") + "/Documents/executed-metadata_original.csv");
    public static final File   CONTRACT_DATA_CSV_ORIGINAL_ACTIVE   = new File(System.getProperty("user.dir") + "/Documents/active-metadata_original.csv");

    public static final File   CLASSIC_AT92_V1               = new File(System.getProperty("user.dir") + "/Documents/Manufacturing_Agreement_draft_3.docx");
    public static final File   CLASSIC_AT92_V2               = new File(System.getProperty("user.dir") + "/Documents/Manufacturing_Agreement_draft_2.docx");

    public static final File   CLASSIC_AT_119_V1             = new File(System.getProperty("user.dir") + "/Documents/PAR-13996_V1.docx");
    public static final File   CLASSIC_AT_119_V2             = new File(System.getProperty("user.dir") + "/Documents/PAR-13996_V2.docx");

    public static final File   CLASSIC_AT_167_V1             = new File(System.getProperty("user.dir") + "/Documents/Quotient_DCSA_QuotientEdits_14703_AT_167.docx");
    public static final File   CLASSIC_AT_167_V2             = new File(System.getProperty("user.dir") + "/Documents/Quotient_DCSA_CBv2_14703_AT_167.docx");

    public static final File   TRACK_CHANGES_AT110_V1        = new File(System.getProperty("user.dir") + "/Documents/AT110/Acorns Engagement Letter_party_changes_v1.docx");
    public static final File   TRACK_CHANGES_AT110_V2        = new File(System.getProperty("user.dir") + "/Documents/AT110/V5 Acorns Engagement Letter_v2.docx");
    public static final File   TRACK_CHANGES_AT110_V3        = new File(System.getProperty("user.dir") + "/Documents/AT110/V6 Acorns Engagement Letter-CNM_v3.docx");
    public static final File   TRACK_CHANGES_AT110_V4        = new File(System.getProperty("user.dir") + "/Documents/AT110/V6 Acorns Engagement Letter-CNM_v4.docx");

    public static final File   TRACK_CHANGES_CLASSIC_AT206    = new File(System.getProperty("user.dir") + "/Documents/AT_206_eGain Master Agreement LM edits July_9.docx");
    public static final File   TRACK_CHANGES_CLASSIC_AT206_V2 = new File(System.getProperty("user.dir") + "/Documents/AT_206_eGain Master Agreement LM edits July 9 (SS07132021) Updated v2.docx");

    public static final File   DOCUMENT_AT65_GLUE            = new File(System.getProperty("user.dir") + "/Documents/Dynatrace_Vendor_Addendum.docx");
    public static final File   DOCUMENT_AT65_GLUE_V2         = new File(System.getProperty("user.dir") + "/Documents/Dynatrace_Vendor_Addendum_v2.docx");

    public static final File   DOC_MICROVENTION_AT139_ONE    = new File(System.getProperty("user.dir") + "/Documents/ForPAR-14078_AT139.docx");
    public static final File   DOC_MICROVENTION_AT139_TWO    = new File(System.getProperty("user.dir") + "/Documents/AT_139_14078.docx");

    public static final File   DOC_AT166_ONE                 = new File(System.getProperty("user.dir") + "/Documents/AT-166_Manufacturing Agreement_1.docx");
    public static final File   DOC_AT166_TWO                 = new File(System.getProperty("user.dir") + "/Documents/AT-166_Manufacturing Agreement_2.docx");

    public static final File   DOC_AT148_ONE                 = new File(System.getProperty("user.dir") + "/Documents/Manufacturing_Agreement_AT148_1.docx");
    public static final File   DOC_AT148_TWO                 = new File(System.getProperty("user.dir") + "/Documents/Manufacturing_Agreement_AT148_2.docx");

    public static final File   IMG_JPEG_SAMPLE               = new File(System.getProperty("user.dir") + "/Documents/IMG_JPEG.jpeg");

    public static final File   DOC_1_AT185                   = new File(System.getProperty("user.dir") + "/Documents/AT185-Manufacturing Agreement-redlines-D1.docx");
    public static final File   DOC_2_AT185                   = new File(System.getProperty("user.dir") + "/Documents/AT185-Lists-D2.docx");

    public static final File   DOC_FORMATTING_AT190          = new File(System.getProperty("user.dir") + "/Documents/AT_190-SoW_CASS_Hybrid-Platform_2022_from Patrick.docx");
    public static final File   DOC_AT199_DOWNLOAD            = new File(System.getProperty("user.dir") + "/Documents/D&B Hoovers - Order Agreement for.docx");

    public static final File   AVATAR_IMG_SAMPLE             = new File(System.getProperty("user.dir") + "/Documents/avatar_user_test.png");

    public static final File   DOC_AT211                     = new File(System.getProperty("user.dir") + "/Documents/MSA - Ray Fortin v2a.docx");

    // Directories
    // Directory of docs from clients. Used in Classic
    public static final File   CLIENT_DOCS_DIR               = new File(System.getProperty("user.dir") + "/Documents/classicClientDocs");
    // Directory of pdf documents. Used in Basics
    public static final File   PDF_DOCS_DIR                  = new File(System.getProperty("user.dir") + "/Documents/pdfDocs");

    public static final File   ATTACHMENTS_DOCS_DIR          = new File(System.getProperty("user.dir") + "/Documents/attachmentDocs");
    public static final File   BAD_ATTACHMENTS_DOCS_DIR      = new File(System.getProperty("user.dir") + "/Documents/badAttachments");

    public static final File   DOWNLOAD_DIR                  = new File(System.getProperty("user.dir") + "/Downloads");
    // Dir for screenshots of failed tests
    public static final File   SCREENSHOTS_DIR               = new File(System.getProperty("user.dir") + "/ScreenshotsOfFailedTests");


    private Const() {}
}
