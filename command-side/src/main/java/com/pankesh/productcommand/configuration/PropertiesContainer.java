package com.pankesh.productcommand.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Holds structured configuration properties from spring boot and flattens them
 * into a {@link Properties} instance.
 */
public class PropertiesContainer extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    /**
     * Converts the nested structure into a flattened properties instance.
     *
     * @return The flattened contents as a {@link Properties} object.
     */
    public Properties getProperties() {
        Properties properties = new Properties();
        flattenProperties(properties, this);
        return properties;
    }

    private void flattenProperties(Properties properties, Map<String, Object> values) {
        flattenProperties(properties, values, null);
    }

    private void flattenProperties(Properties properties, Map<String, Object> map, String previousKey) {
        for (Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            String newKey = key;
            if (previousKey != null) {
                newKey = previousKey + "." + key;
            }
            if (map.get(key) instanceof Map<?, ?>) {
                @SuppressWarnings("unchecked")
                Map<String, Object> newMap = (Map<String, Object>) map.get(key);
                flattenProperties(properties, newMap, newKey);
            } else {
                properties.setProperty(newKey, map.get(key).toString());
            }
        }
    }
}
