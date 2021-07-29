package tests;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;

public class LoginBase {
    public static final String PROP_TENANT_URL = "tenant_url";
    public static final String PROP_POPOVERS_TENANT_URL = "popovers_tenant_url";

    protected static boolean isRC,
                             isPROD;

    protected static Logger logger = Logger.getLogger(LoginBase.class);
    private static String tenantUrl;
    protected java.util.Properties property;

    public LoginBase() {
        init();
    }

    private void init() {
        final String env = System.getenv("ENV");

        String configName = "config";
        if(StringUtils.isNotBlank(env)) {
            logger.info("Use env: " + env);
            configName = configName + "-" + env.toLowerCase();
        }

        final String filePath = "src/main/resources/" + configName + ".properties";

        FileInputStream fis;
        property = new java.util.Properties();

        try {
            logger.info("Load property file from: " + filePath);

            fis = new FileInputStream(filePath);
            property.load(fis);
        } catch (IOException e) {
            logger.error("Can't find file " + filePath);
        }

        if(env == null) return; // master by default
        switch (env)
        {
            case "rc":   isRC   = true; break;
            case "prod": isPROD = true; break;
        }
    }

    public String getTenantUrl() {
        if(tenantUrl != null) {
            return tenantUrl;
        }

        final String envTenantUrl = System.getenv("TENANT_URL");
        if(StringUtils.isNotBlank(envTenantUrl)) {
            logger.info("Use custom tenant_url: " + envTenantUrl);

            tenantUrl = envTenantUrl;

            return tenantUrl;
        }

        String propTenantUrl = property.getProperty(PROP_TENANT_URL);

        logger.info("Use tenant_url: " + propTenantUrl);

        tenantUrl = propTenantUrl;

        return tenantUrl;
    }

    public String getPopoversTenantUrl() {
        return property.getProperty(PROP_POPOVERS_TENANT_URL);
    }
}
