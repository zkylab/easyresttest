package SwaggerObjects;

import java.util.TreeMap;

/**
 * @param properties to store response parameters with name and type
 *                   ex. id ==> integer, name ==> string
 */
public class ServiceScheme{
    private String type;
    private TreeMap<String,String> properties;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TreeMap<String, String> getProperties() {
        return properties;
    }

    public void setProperties(TreeMap<String, String> properties) {
        this.properties = properties;
    }
}


