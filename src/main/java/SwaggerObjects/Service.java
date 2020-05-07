package SwaggerObjects;

import io.restassured.http.ContentType;

import java.util.ArrayList;

public class Service {
    private String endPointPath;
    private String method;
    private ContentType requestContentType;
    private ContentType responseContentType;
    private ArrayList<ServiceParameter> serviceParameters;
    private ServiceResponse response;

    public ServiceResponse getResponse() {
        return response;
    }

    public void setResponse(ServiceResponse response) {
        this.response = response;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getEndPointPath() {
        return endPointPath;
    }

    public void setEndPointPath(String endPointPath) {
        this.endPointPath = endPointPath;
    }

    public ContentType getRequestContentType() {
        return requestContentType;
    }

    public void setRequestContentType(ContentType requestContentType) {
        this.requestContentType = requestContentType;
    }

    public ContentType getResponseContentType() {
        return responseContentType;
    }

    public void setResponseContentType(ContentType responseContentType) {
        this.responseContentType = responseContentType;
    }

    public ArrayList<ServiceParameter> getServiceParameters() {
        return serviceParameters;
    }

    public void setServiceParameters(ArrayList<ServiceParameter> serviceParameters) {
        this.serviceParameters = serviceParameters;
    }
}
