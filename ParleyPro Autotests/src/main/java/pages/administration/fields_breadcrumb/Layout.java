package pages.administration.fields_breadcrumb;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class Layout
{
    public Layout()
    {
        $(".admin-fields-layout__description").shouldBe(Condition.visible)
                .shouldHave(Condition.exactText("Drag and resize the cells to change layout"));

        $$(".admin-fields__title").shouldHave(CollectionCondition.size(3))
                .shouldHave(CollectionCondition.exactTexts("Summary", "Post-execution", "Contract Request"));
    }
}
