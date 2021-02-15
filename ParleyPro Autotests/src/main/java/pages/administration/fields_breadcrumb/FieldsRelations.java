package pages.administration.fields_breadcrumb;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import forms.add.AddNewParentField;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FieldsRelations
{
    public FieldsRelations()
    {
        $$(".admin-fields__title").shouldHave(CollectionCondition.size(3))
                .shouldHave(CollectionCondition.exactTexts("Summary", "Contract Request"));
    }

    /*
    public AddNewParentField addNewParentField()
    {
        Selenide.executeJavaScript("");
    }*/
}
