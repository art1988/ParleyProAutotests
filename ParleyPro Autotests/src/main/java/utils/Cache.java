package utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Util class that saves useful data within one single test.
 * May be useful to retrieve 'Contract title', 'Current tab handle' from another test-class within one single test.
 */
public class Cache
{
    private static Cache CACHE_INSTANCE = new Cache(); // singleton instance

    private static Map<String, String> hashTable = new HashMap<>();

    private static String KEY_CONTRACT_TITLE     = "KEY_CONTRACT_TITLE";
    private static String KEY_CURRENT_TAB_HANDLE = "KEY_CURRENT_TAB_HANDLE";
    private static String KEY_TAB_1_HANDLE       = "KEY_TAB_1_HANDLE";

    private Cache() {}

    public void setContractTitle(String contractTitle)
    {
        hashTable.put(KEY_CONTRACT_TITLE, contractTitle);
    }

    public String getCachedContractTitle()
    {
        return hashTable.get(KEY_CONTRACT_TITLE);
    }

    public void setCurrentTabHandle(String currentTabHandle)
    {
        hashTable.put(KEY_CURRENT_TAB_HANDLE, currentTabHandle);
    }

    public String getCachedCurrentTabHandle()
    {
        return hashTable.get(KEY_CURRENT_TAB_HANDLE);
    }

    public void setTab1Handle(String tab1Handle)
    {
        hashTable.put(KEY_TAB_1_HANDLE, tab1Handle);
    }

    public String getCachedTab1Handle()
    {
        return hashTable.get(KEY_TAB_1_HANDLE);
    }

    public static Cache getInstance()
    {
        return CACHE_INSTANCE;
    }
}
