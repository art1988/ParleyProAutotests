package utils;

/**
 * Util class that saves Contract title within one single test.
 * May be useful to retrieve 'Contract title' from another test-class within one single test.
 */
public class Cache
{
    public static Cache CACHE_INSTANCE = new Cache(); // singleton instance

    private static String cachedContractTitle;

    private Cache() {}


    public void setContractTitle(String contractTitle)
    {
        cachedContractTitle = contractTitle;
    }

    public String getCachedContractTitle()
    {
        return cachedContractTitle;
    }

    public static Cache getInstance()
    {
        return CACHE_INSTANCE;
    }
}
