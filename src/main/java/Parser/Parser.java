package Parser;

import SwaggerObjects.Service;

import java.util.ArrayList;

public class Parser {
    private static Parser swaggerParser_instance = null;
    private ArrayList<Service> services;
    private String hostAddress;

    private Parser(){

    }

    public static Parser getInstance(){
           if(swaggerParser_instance == null)
               return swaggerParser_instance = new Parser();
           return swaggerParser_instance;
    }

    public void setJSON(){

    }

}
