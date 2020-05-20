package enums;

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
        value,
    }
}