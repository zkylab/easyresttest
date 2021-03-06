package Parser;

import DataManager.DataManager;
import SwaggerObjects.Service;
import SwaggerObjects.ServiceParameter;
import SwaggerObjects.ServiceResponse;
import SwaggerObjects.ServiceScheme;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

//import sun.reflect.generics.tree.Tree;


import java.io.File;
import java.io.IOException;
import java.util.*;

public class Parser {
    private static Parser swaggerParser_instance = null;
    private ArrayList<Service> services;
    private String hostAddress;
    private File jsonFile;
    private ObjectMapper objectMapper;

    private Parser(){

    }

    /**
     * In order to get Parser's instance, statically call this method
     * @return Parser, Swagger Parser Instance.
     */
    public static Parser getInstance(){
           if(swaggerParser_instance == null)
               return swaggerParser_instance = new Parser();
           return swaggerParser_instance;
    }

    /**
     * Set your swagger json file. It can be overwritten when you set more than one. 
     * @param File, swagger.json file
     */
    public void setJSON(File file){
        jsonFile = file;
    }

    /**
     * Parse swagger json into Java structures such as hashmap, arraylist.
     * Parsed object will be used by code generator.
     */
    //TODO: make this method private, hide from user.
    public void runParser(){
        HashMap<String, Object>  swaggerRoot = parseSwaggerRaw();
        HashMap<String,Object> servicesRaw = (HashMap<String, Object>) swaggerRoot.get("paths");
        ArrayList<Service> services = parseServices(servicesRaw);
        DataManager.getInstance().setServices(services);
        String host = parseHost(swaggerRoot);
        DataManager.getInstance().setHost(host);
    }

    public String parseHost(HashMap<String, Object>  root){
        return root.get("host").toString();
    }

    private ArrayList<Service> parseServices(HashMap<String, Object>  root){
        ArrayList<Service> services = new ArrayList<Service>();
        System.out.println(root);
        for (Map.Entry<String, Object> serviceEntry : root.entrySet()) {
            String serviceName = serviceEntry.getKey();
            HashMap<String, Object> serviceData = (HashMap) serviceEntry.getValue();
            Service service = new Service();
            service.setEndPointPath(serviceName);
            for (Map.Entry<String, Object> serviceDataEntry : serviceData.entrySet()) {
                String method = serviceDataEntry.getKey();
                service.setMethod(method);
                HashMap<String, Object> serviceInnerData = (HashMap) serviceDataEntry.getValue();
                ArrayList<HashMap<String, Object>> parametersList = (ArrayList) serviceInnerData.get("parameters");
                ArrayList<ServiceParameter> parameters = new ArrayList<>();
                service.setServiceParameters(parameters);
                for (HashMap<String, Object> parameter: parametersList){
                    String parameterName = (String) parameter.get("name");
                    boolean isRequired = (boolean) parameter.get("required");
                    String type = (String) parameter.get("type");
                    String where = (String) parameter.get("in");
                    ServiceParameter serviceParameter = new ServiceParameter();
                    serviceParameter.setName(parameterName);
                    serviceParameter.setRequired(isRequired);
                    serviceParameter.setType(type);
                    serviceParameter.setIn(where);
                    parameters.add(serviceParameter);
                }
                HashMap<String,Object> responseRaw = (HashMap<String, Object>) serviceInnerData.get("responses");
                ServiceResponse serviceResponse = parseServiceResponse(responseRaw);
                service.setResponse(serviceResponse);

                HashMap<String, Object>  swaggerRoot = parseSwaggerRaw();
                HashMap<String,Object> test = new HashMap<>();
                if(((HashMap)responseRaw.get("200"))!= null){
                    if((((HashMap)(HashMap)((HashMap)responseRaw.get("200")).get("schema")).get("$ref")) != null){
                        String schemaKey = (((HashMap)(HashMap)((HashMap)responseRaw.get("200")).get("schema")).get("$ref")).toString();
                        schemaKey = schemaKey.substring(schemaKey.lastIndexOf("/")+1);
                        ServiceScheme schema = new ServiceScheme();
                        schema.setType(((HashMap)((HashMap)swaggerRoot.get("definitions")).get(schemaKey)).get("type").toString());
                        TreeMap<String,String> tree = new TreeMap<>();
                        tree.putAll((HashMap)((HashMap)((HashMap)swaggerRoot.get("definitions")).get(schemaKey)).get("properties"));
                        schema.setProperties(tree);
                        serviceResponse.setSchema(schema);
                    }
                }
            }
            services.add(service);
            }

        return services;
        }

        private ServiceResponse parseServiceResponse(HashMap<String,Object> responseRaw){
            ServiceResponse serviceResponse = new ServiceResponse();
            for (Map.Entry<String, Object> responseDataEntry : responseRaw.entrySet()) {
                if(!responseDataEntry.getKey().equals("default")){
                    int statusCode = Integer.parseInt(responseDataEntry.getKey());
                    serviceResponse.setStatusCode(statusCode);
                }
                HashMap<String,Object> responseInnerData = (HashMap<String, Object>) responseDataEntry.getValue();
                HashMap<String,Object> responseSchema = (HashMap<String, Object>) responseInnerData.get("schema");
                if(responseSchema != null){
                    String ref = (String) responseSchema.get("$ref");
                    serviceResponse.setRefDefinition(ref);
                }

            }
            return serviceResponse;
        }

    private HashMap<String, Object> parseSwaggerRaw(){
        if(jsonFile != null) {
            try {
                return new ObjectMapper().readValue(jsonFile, HashMap.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
