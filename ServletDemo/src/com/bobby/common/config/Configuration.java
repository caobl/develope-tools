package com.bobby.common.config;

import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @Title Configuration.java
 * @Package com.bobby.common.config
 * @Description 单例的系统配置信息访问类
 * @author 曹保利
 * @email caobl_work@126.com
 * @date 2013年11月19日 下午12:28:25
 * @version V1.0
 */
public final class Configuration {

    private static Log logger = LogFactory.getLog(Configuration.class);
    private Properties properties = new Properties();

    private static final Configuration config = new Configuration();
    private static final String DEFAULT_CONFIG_FILE = "config.properties";

    private Configuration() {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            properties.load(loader.getResourceAsStream(DEFAULT_CONFIG_FILE));
        } catch (Exception ex) {
            logger.error("Can't read the properties file. " + ex.getMessage());
        }
    }

    /**
     * 使用单例模式，返回配置类实例.
     *
     * @return Configuration
     */
    public static Configuration getInstance() {
        return config;
    }

    /**
     * 获取指定Key的配置项的值.
     *
     * @param key
     *            配置项键值.
     * @return 配置项的值
     */
    public String getValue(String key) {
        return properties.getProperty(key);
    }
}
