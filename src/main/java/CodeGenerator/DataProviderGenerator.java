package CodeGenerator;

import DataGenerator.DataGenerator;
import SwaggerObjects.Service;
import SwaggerObjects.ServiceParameter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class DataProviderGenerator {
    private String dataJsonPath = "data_out.json";
    private String dataProviderName = "dataProvider";
    private int dataProviderCount;
    private ArrayList<String> methodSignatures = new ArrayList<>();
    public ArrayList<String> getMethods(String filePath){
        ArrayList<String> methods = new ArrayList<>();
        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            String method = "";
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(data.contains("@Test")){
                    methods.add(method);
                    method = "";
                }
                else{
                    method += data+"\n";
                }
            }
            method = method.substring(0,method.lastIndexOf("}"));
            methods.add(method);
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return methods;
    }

    public ArrayList<String> updateMethods(ArrayList<String> methods, ArrayList<ArrayList<ServiceParameter>> serviceParameters){
        ArrayList<String> newMethods = new ArrayList<>();
        int dataProviderCount = 0;
        int parameterCount = 0;

        for(String method : methods){
            ArrayList<ServiceParameter> parameters = serviceParameters.get(parameterCount);
            String methodSignature = "";
            int paramNameCounter = 0;
            for(ServiceParameter parameter : parameters){
                String type = "";
                if(parameter.getType() == null)
                    continue;

                if(parameter.getType().equals("integer"))
                    type = "int";
                else if(parameter.getType().equals("array"))
                    type = "Object[]";
                else
                    type = capitilaze(parameter.getType());

                if(paramNameCounter+1 == parameters.size())
                    methodSignature += type + " p"+paramNameCounter;
                else
                    methodSignature += type + " p"+paramNameCounter + ", ";
                paramNameCounter++;
            }
            methodSignatures.add(methodSignature);
            method = method.replaceAll("%methodParameter",methodSignature);
            for(int i=0; i<paramNameCounter; i++){
                if(method.contains("DATA")){
                    method = method.replaceFirst("DATA","p"+i);
                }
            }
            if(!methodSignature.equals("")){
                dataProviderCount++;
                method = "\t@Test(dataProvider=\""+ dataProviderName+dataProviderCount+"\")" +"\n"+method;

            }
            else
                method = "\t@Test\n"+method;

            parameterCount++;
            newMethods.add(method);

        }
        this.dataProviderCount = dataProviderCount;
        return newMethods;
    }

    public String updateImports(String imports){
        imports = imports.replaceAll("import org.junit.Test;","");
        imports = imports.substring(0,imports.indexOf("public")) + "import org.testng.annotations.Test;\nimport org.testng.annotations.DataProvider;\nimport java.io.File;\n" + imports.substring(imports.indexOf("public"));
        return imports;
    }

    public ArrayList<ArrayList<ServiceParameter>> copyServiceParameters(ArrayList<ArrayList<ServiceParameter>> serviceParameters){
        ArrayList<ArrayList<ServiceParameter>> temp = new ArrayList<>(serviceParameters.size());

        for(ArrayList<ServiceParameter> parameters : serviceParameters){
            ArrayList<ServiceParameter> temp2 = new ArrayList<>(parameters.size());
            for(ServiceParameter parameter : parameters){
                ServiceParameter temp3 = new ServiceParameter();
                temp3.setName(parameter.getName());
                temp3.setIn(parameter.getIn());
                temp3.setRequired(parameter.isRequired());
                temp3.setType(parameter.getType());
                temp2.add(temp3);
            }
            temp.add(temp2);
        }

        return temp;
    }

    public Object getData(String type, JSONArray jsonArray){
        Object dataToReturn = "DATA NOT FOUND";

        for(Object data : jsonArray){
            JSONObject jsonData = (JSONObject)data;
            System.out.println(jsonData);
            if(jsonData.get("type").toString().equalsIgnoreCase(type)){
                dataToReturn = jsonData.get("value");
            }
        }

        return dataToReturn;
    }

    public String getDataProviders(ArrayList<ArrayList<ServiceParameter>> serviceParameters){
        ArrayList<ArrayList<ServiceParameter>> tempServiceParameters = copyServiceParameters(serviceParameters);
        for(int i=0; i<tempServiceParameters.size();){
            ArrayList<ServiceParameter> parameters = tempServiceParameters.get(i);
            if(parameters.size() == 0){
                tempServiceParameters.remove(parameters);
            }
            else if(parameters.size() == 1){
                ServiceParameter parameter = parameters.get(0);
                if(parameter.getType() == null || parameter.getType().equals("null")){
                    tempServiceParameters.remove(parameters);
                }
                else{
                    i++;
                }
            }
            else{
                i++;
            }
        }

        String output = "";
        String dataProvider = "";
        for(int i=1; i<=dataProviderCount; i++){
            dataProvider = "\t@DataProvider(name = \"";
            dataProvider += dataProviderName+i;
            dataProvider += "\")\n";

            String temp = getAllDataSet(tempServiceParameters,i);


            dataProvider += "\tpublic Object[][] " + dataProviderName+i + "(){\n";
            dataProvider += "\treturn new Object[][] {" + temp + "};\n";
            dataProvider += "\t}";
            output += dataProvider + "\n\n";
        }



        return output;
    }

    public String getAllDataSet(ArrayList<ArrayList<ServiceParameter>> tempServiceParameters,int index){
        String temp = "";
        ArrayList<ServiceParameter> parameters = tempServiceParameters.get(index-1);
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(dataJsonPath));
            JSONArray jsonArray = (JSONArray) obj;
            for (int i = 0; i < jsonArray.size(); i++) {
                if (i != 0)
                    temp += ",{";
                else
                    temp += "{";

                JSONArray jsonInnerArray = (JSONArray) jsonArray.get(i);
                for (int j = 0; j < parameters.size(); j++) {
                    ServiceParameter parameter = parameters.get(j);
                    if (parameter.getType().equalsIgnoreCase("array")) {
                        temp += "{\"Apple\", \"Banana\", \"Orange\", \"Grapes\"},";
                    } else if (parameter.getType().equalsIgnoreCase("file")) {
                        temp += "new File(\"" + dataJsonPath + "\"),";
                    } else if (parameter.getType().equalsIgnoreCase("string") ||
                            parameter.getType().equalsIgnoreCase("character")) {
                        temp += "\"" + getData(parameter.getType(), jsonInnerArray) + "\",";
                    } else {
                        temp += "" + getData(parameter.getType(), jsonInnerArray) + ",";
                    }

                    if (j + 1 == parameters.size() && temp.charAt(temp.length() - 1) == ',')
                        temp = temp.substring(0, temp.length() - 1);
                }
                temp += "}";
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return temp;
    }

    public void addDataProvider(String filePath, ArrayList<ArrayList<ServiceParameter>> serviceParameters){
        ArrayList<String> methods = getMethods(filePath);

        String imports = methods.get(0);
        methods.remove(0);

        methods = updateMethods(methods,serviceParameters);

        imports = updateImports(imports);


        String dataProvider = getDataProviders(serviceParameters);

        String output = imports;
        for(String method : methods)
            output += method;
        output += "\n"+dataProvider;
        output += "\n}";

        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(output);
            writer.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    private static String capitilaze(String s){
        String s1 = s.substring(0,1).toUpperCase();
        String sTitle = s1 + s.substring(1);
        return sTitle;
    }
}
