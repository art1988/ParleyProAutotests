package tests.templates.at77;

import constants.Const;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.EditTemplatePage;
import pages.TemplatesPage;

public class CreateTemplateAndPasteText
{
    private String textToPaste = "\\n" +
            "<<[contract.getCustomFields().get(“Borrower5”)]>><</if>><<if[contract.getCustomFields().get(\"Borrower6\")!=null]>>\\n" +
            "\\n" +
            "\\n" +
            "<<[contract.getCustomFields().get(“Borrower6”)]>><</if>><<if[contract.getCustomFields().get(\"Borrower7\")!=null]>>\\n" +
            "\\n" +
            "\\n" +
            "<<[contract.getCustomFields().get(“Borrower7”)]>><</if>><<if[contract.getCustomFields().get(\"Borrower8\")!=null]>>\\n" +
            "\\n" +
            "\\n" +
            "<<[contract.getCustomFields().get(“Borrower8”)]>><</if>><<if[contract.getCustomFields().get(\"Borrower9\")!=null]>>\\n" +
            "\\n" +
            "\\n" +
            "<<[contract.getCustomFields().get(“Borrower9”)]>><</if>><<if[contract.getCustomFields().get(\"Borrower10\")!=null]>>\\n" +
            "\\n" +
            "\\n" +
            "<<[contract.getCustomFields().get(“Borrower10”)]>><</if>><<if[contract.getCustomFields().get(\"Borrower11\")!=null]>>\\n" +
            "\\n" +
            "\\n" +
            "<<[contract.getCustomFields().get(“Borrower11”)]>><</if>><<if[contract.getCustomFields().get(\"Borrower12\")!=null]>>\\n" +
            "\\n" +
            "\\n" +
            "<<[contract.getCustomFields().get(“Borrower12”)]>><</if>><<if[contract.getCustomFields().get(\"Borrower13\")!=null]>>\\n" +
            "\\n" +
            "\\n" +
            "<<[contract.getCustomFields().get(“Borrower13”)]>><</if>><<if[contract.getCustomFields().get(\"Borrower14\")!=null]>>\\n" +
            "\\n" +
            "\\n" +
            "<<[contract.getCustomFields().get(“Borrower14”)]>><</if>><<if[contract.getCustomFields().get(\"Borrower15\")!=null]>>\\n" +
            "\\n" +
            "\\n" +
            "\\n" +
            "<<[contract.getCustomFields().get(“Borrower15”)]>><</if>>\\n";

    @Test(priority = 1)
    public void uploadDocAsTemplateAndPasteText() throws InterruptedException
    {
        TemplatesPage templatesPage = new DashboardPage().getSideBar().clickTemplates(true);

        templatesPage.clickUploadTemplatesButton(Const.TEMPLATE_AT77 );

        EditTemplatePage editTemplatePage = templatesPage.selectTemplate("Template_AT-77_dummy");

        editTemplatePage.addText(textToPaste);
    }
}
