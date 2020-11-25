package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import model.User;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

public class DeleteUser
{
    private SelenideElement title = $(".modal-body-title");
    private SelenideElement deleteUserButton = $(".button.btn-common.btn.btn-danger");


    private static Logger logger = Logger.getLogger(DeleteUser.class);

    public DeleteUser(User user)
    {
        title.waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Are you sure to delete " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")"));
    }

    public void clickDelete()
    {
        deleteUserButton.click();

        logger.info("DELETE USER button was clicked");

        // wait until dialog will disappear
        title.waitUntil(Condition.disappear, 6_000);
    }
}
