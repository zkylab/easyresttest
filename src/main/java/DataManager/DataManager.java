package DataManager;

import AssertionObjects.AssertionSet;
import SwaggerObjects.Service;

import java.util.ArrayList;

public class DataManager {
    private static DataManager dataManager_instance = null;
    private ArrayList<Service> services;
    private String host;
    private ArrayList<AssertionSet> asserts = null;
    private DataManager() {

    }

    public static DataManager getInstance() {
        if (dataManager_instance == null)
            return dataManager_instance = new DataManager();
        return dataManager_instance;
    }

    public ArrayList<AssertionSet> getAsserts() {
        return asserts;
    }

    public void setAsserts(ArrayList<AssertionSet> asserts) {
        this.asserts = asserts;
    }

    public ArrayList<Service> getServices() {
        return services;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
    }

    public void setHost(String host){
        this.host = host;
    }

    public String getHost(){
        return host;
    }

}
