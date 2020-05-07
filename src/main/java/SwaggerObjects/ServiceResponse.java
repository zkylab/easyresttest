package SwaggerObjects;

public class ServiceResponse{
    private int statusCode;
    private String refDefinition;
    private ServiceScheme schema;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getRefDefinition() {
        return refDefinition;
    }

    public void setRefDefinition(String refDefinition) {
        this.refDefinition = refDefinition;
    }

    public ServiceScheme getSchema() {
        return schema;
    }

    public void setSchema(ServiceScheme schema) {
        this.schema = schema;
    }
}
