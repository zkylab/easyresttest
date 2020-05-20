package AssertionObjects;

import enums.Enums;

public class AssertionSet {
    private String type;
    private String operator;
    private Enums.asserterPropType prop;
    private String value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Enums.asserterPropType getProp() {
        return prop;
    }

    public void setProp(Enums.asserterPropType prop) {
        this.prop = prop;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
