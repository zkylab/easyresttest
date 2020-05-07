package DataManager;

import SwaggerObjects.Service;

import java.util.ArrayList;

public class DataManager {
    private static DataManager dataManager_instance = null;
    private ArrayList<Service> services;

    private DataManager() {

    }

    public static DataManager getInstance() {
        if (dataManager_instance == null)
            return dataManager_instance = new DataManager();
        return dataManager_instance;
    }

    public ArrayList<Service> getServices() {
        return services;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
    }
}
