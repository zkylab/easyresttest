package SwaggerObjects;

/**
 *
 * @param in the place of parameter, ex. path, formData
 * @param type type of parameter, ex. integer, file, string
 */
public class ServiceParameter {
    private String name;
    private boolean isRequired;
    private String type;
    private String in;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }



}
