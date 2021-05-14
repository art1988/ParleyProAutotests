package constants;

public enum FieldType
{
    CHECKBOX("Checkbox"),
    DATE("Date"),
    MULTI_SELECT("Multi Select"),
    NUMERIC("Numeric"),
    DECIMAL("Decimal"),
    RADIO_BUTTON("Radio Button"),
    SELECT("Select"),
    TEXT("Text"),
    TEXT_AREA("Textarea"),
    CURRENCY("Currency");

    private String type;

    FieldType(String type)
    {
        this.type = type;
    }

    public String getFieldType()
    {
        return type;
    }
}
