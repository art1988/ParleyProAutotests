package utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Util class that saves useful data within one single test.
 * May be useful to retrieve 'Contract title', 'Current tab handle', etc. from another test-class within one single test.
 */
public class Cache
{
    private static Cache CACHE_INSTANCE = new Cache(); // singleton instance

    private static Map<String, String> hashTable = new HashMap<>();

    private static String KEY_CONTRACT_TITLE               = "KEY_CONTRACT_TITLE";
    private static String KEY_CURRENT_TAB_HANDLE           = "KEY_CURRENT_TAB_HANDLE";
    private static String KEY_TAB_1_HANDLE                 = "KEY_TAB_1_HANDLE";
    private static String KEY_EMAIL                        = "KEY_EMAIL";
    private static String KEY_ADDITIONAL_COUNTERPARTY_USER = "KEY_ADDITIONAL_COUNTERPARTY_USER";
    private static String KEY_FILE                         = "KEY_FILE";

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

    public void setEmail(String email)
    {
        hashTable.put(KEY_EMAIL, email);
    }

    public String getCachedEmail()
    {
        return hashTable.get(KEY_EMAIL);
    }

    public void setAdditionalCounterpartyUser(String user)
    {
        hashTable.put(KEY_ADDITIONAL_COUNTERPARTY_USER, user);
    }

    public String getCachedAdditionalCounterpartyUser()
    {
        return hashTable.get(KEY_ADDITIONAL_COUNTERPARTY_USER);
    }

    public void setFile(String fileName)
    {
        hashTable.put(KEY_FILE, fileName);
    }

    public String getCachedFile()
    {
        return hashTable.get(KEY_FILE);
    }

    public static Cache getInstance()
    {
        return CACHE_INSTANCE;
    }
}
