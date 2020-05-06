package SwaggerObjects;

import io.restassured.http.ContentType;

import java.util.ArrayList;
import java.util.TreeMap;

public class Service {
    private String endPointPath;
    private ContentType requestContentType;
    private ContentType responseContentType;
    private ArrayList<ServiceParameter> serviceParameters;

    /**
     * a nested class to encapsulate service data
     * @param in the place of parameter, ex. path, formData
     * @param type type of parameter, ex. integer, file, string
     */
    class ServiceParameter {
    private String name;
    private boolean isRequired;
    private String type;
    private String in;
}
    private class ServiceResponse{
        private int statusCode;
        private String refDefinition;
        private ServiceResponseSchema schema;
    }

    /**
     * @param properties to store response parameters with name and type
     *                   ex. id ==> integer, name ==> string
     */
    private class ServiceResponseSchema{
        private String type;
        private TreeMap<String,String> properties;
        
    }


}
