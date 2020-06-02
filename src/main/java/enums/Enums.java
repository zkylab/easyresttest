package enums;

import java.util.NoSuchElementException;

public class Enums {

    /**
     * Enum for types of template that resides in resources folder.
     * Warning: If a new template is added to folder, you also should add it here to the enum below for recognition.
     *
     */
    public enum testCaseType {
        GetURLAssertStatusCode,
    }

    /**
     * Enum for types of container files used for constructing a valid rest assured java file.
     * Warning: If a new template is added to folder, you also should add it here to the enum below for recognition.
     *
     */
    public enum testClassType {
        GeneralRestAssuredContainer,
    }

    /**
     * Enum for types of assertion properties for Asserter module. Asserter gets the property to assert from here.
     * Warning: If a new property is added to asserter json file, you also should add it here to the enum below for recognition.
     *
     */
    public enum asserterPropType {
        length,
        isnull,
        equals,
        contains,
    }

    public enum LogicalOperator {
        LESS("<") {
            @Override public boolean apply(int left, int right) {
                return left < right;
            }
        },
        EQUAL("==") {
            @Override public boolean apply(int left, int right) {
                return left == right;
            }
        },
        LESSOREQUAL("<=") {
            @Override public boolean apply(int left, int right) {
                return left <= right;
            }
        },
        MORE(">") {
            @Override public boolean apply(int left, int right) {
                return left > right;
            }
        },
        MOREOREQUAL("<") {
            @Override public boolean apply(int left, int right) {
                return left >= right;
            }
        },
        NOTEQUAL("!=") {
            @Override public boolean apply(int left, int right) {
                return left != right;
            }
        };

        private final String operator;
        private LogicalOperator(String operator) {
            this.operator = operator;
        }

        public static LogicalOperator parseOperator(String operator) {
            for (LogicalOperator op : values()) {
                if (op.operator.equals(operator)) return op;
            }
            throw new NoSuchElementException(String.format("Unknown operator [%s]", operator));
        }

        public abstract boolean apply(int left, int right);
    }
}