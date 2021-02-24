package constants;

public enum SideBarItems
{
    PRIORITY_DASHBOARD("a[href*='#/priority-dashboard']"),
    IN_PROGRESS_CONTRACTS("a[href*='#/contracts?filter=active']"),
    EXECUTED_CONTRACTS("a[href*='contracts-executed?filter=executed']"),
    DASHBOARD("a[href*='#/dashboard']"),
    TEMPLATES("a[href*='#/templates']"),
    ADMINISTRATION("a[href*='#/admin/usermanagement']"),
    USER_GUIDE("a[href^='http://help.parleypro.com/']");

    private String locator;

    SideBarItems(String locator)
    {
        this.locator = locator;
    }

    public String getLocator()
    {
        return locator;
    }
}
