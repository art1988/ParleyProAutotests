package constants;

public enum AcceptTypes
{
    INSERT("Are you sure you want to insert this text?", "The text will be inserted in the document and the discussion will be closed"),
    DELETE("Are you sure you want to delete this text?", "The text will be deleted from the document and the discussion will be closed"),
    ACCEPT("Are you sure you want to accept this text?", "The text will replace the current text, and the discussion will be closed");

    private String formTitle;
    private String formMessage;


    AcceptTypes(String formTitle, String formMessage)
    {
        this.formTitle = formTitle;
        this.formMessage = formMessage;
    }

    public String getTitle()
    {
        return formTitle;
    }

    public String getMessage()
    {
        return formMessage;
    }
}
