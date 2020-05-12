package Generator;

import DataManager.DataManager;
import Parser.Parser;
import SwaggerObjects.Service;
import SwaggerObjects.ServiceParameter;
import SwaggerObjects.ServiceResponse;
import enums.Enums;
import io.restassured.http.ContentType;

import javax.naming.ServiceUnavailableException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class Generator {
    //Generator class instance for glorious code generation stuff. Neat.
    private static Generator generator_instance = null;
    //Parsed services from Parser class.
    private static ArrayList<Service> parsedServices = null;
    //This tells the Generator class to which folder it should output its generated .java files.
    private static Path rootOutputLocation = Paths.get("Generated").toAbsolutePath();
    //Current java file under construction.
    private static String currentJavaFile = null;
    //Method template. Defaulted to basic GetURL template.
    private static Enums.testCaseType currentTestCaseType = Enums.testCaseType.GetURLAssertStatusCode;

    //DELETE THIS WHEN HOSTNAME STARTS TO BE PROVIDED FROM PARSER!!11!!cCc
    private static String hostname = "https://test.com";

    /**
     * Get the generator instance.
     *
     */
    public static Generator getInstance() {
        if(generator_instance == null)
            generator_instance = new Generator();
        return generator_instance;
    }

    /**
     * Run the code generator after location of the parsed Services are set.
     *
     * @param fileName for java file name.
     *
     * @throws Exception
     *
     */
    public void runGenerator(String fileName) throws Exception {
        if(parsedServices == null)
            throw new ServiceUnavailableException("Parser Service is null.");
        generate(fileName);
    }

    /**
     * Generate the testcases.
     *
     * @param fileName for java file name. Normally passed from runGenerator function.
     * @throws Exception
     */
    private void generate(String fileName) throws Exception {
        createNewJavaFile(fileName);
        String methodName, method;
        ContentType requestContentType, responseContentType;
        ArrayList<ServiceParameter> serviceParameters;
        ServiceResponse response;
        if(currentJavaFile == null)
            throw new FileNotFoundException("Java file not found.");
        for (Service currentService : parsedServices) {
            //Copy the template test function from the template files.
            String currentMethod = appendTestFunction(new File(Paths.get("resources").toAbsolutePath().toString() + "/" + currentTestCaseType.toString() + ".re"));

            methodName = removeSpecialChars(currentService.getEndPointPath());
            if(methodName != null)
                currentMethod = writeToJavaVariable("testcaseName", methodName, currentMethod);
            else
                System.out.println("Method name is null. Skipping.");

            requestContentType = currentService.getRequestContentType();
            if(requestContentType != null)
                currentMethod = writeToJavaVariable("requestContentType", requestContentType.toString(), currentMethod);
            else
                System.out.println("RequestContentType is null. Skipping.");

            responseContentType = currentService.getResponseContentType();
            if(responseContentType != null)
                currentMethod = writeToJavaVariable("responseContentType", responseContentType.toString(), currentMethod);
            else
                System.out.println("ResponseContentType is null. Skipping.");

            serviceParameters = currentService.getServiceParameters();
            //Get into service parameters no matter what, since it needs to delete the var string even if no parameters are given.
            currentMethod = insertServiceParameters(serviceParameters, currentService.getEndPointPath(), currentMethod);

            response = currentService.getResponse();
            if(response != null)
                currentMethod = writeToJavaVariable("statusCode", Integer.toString(response.getStatusCode()), currentMethod);
            else
                System.out.println("Status code from response is null. Skipping.");


            currentMethod += "\n\n";
            currentMethod = currentMethod.replaceAll("%parameters", "");
            currentMethod = currentMethod.replaceAll("%testURL", hostname);

            System.out.println("Function constructed. Writing to java file...");
            appendToJavaFile(currentJavaFile, currentMethod);
            System.out.println("Java file " + fileName + " appended.");
        }
        System.out.println("Parsed methods generated.");
    }

    /**
     * Set the parsed services from The Parsed class.
     *
     * @param parsedFile for getting parsed services from Parser submodule.
     */
    public void setParsedServices(File parsedFile) {
        Parser parser = Parser.getInstance();
        parser.setJSON(parsedFile);
        parser.runParser();
        parsedServices = DataManager.getInstance().getServices();
    }

    /**
     * Take current method string and pass it through after inserting service parameters found in service object.
     *
     * @param serviceParameters A ServiceParameter types arraylist for iterating current rest parameter inputs.
     * @param currentMethod String
     * @throws IOException
     * @return currentMethod String
     */
    private String insertServiceParameters(ArrayList<ServiceParameter> serviceParameters, String endpointPath, String currentMethod) throws IOException {
        if(serviceParameters == null) {
            currentMethod = writeToJavaVariable("parameters", "", currentMethod);
            return currentMethod;
        }
        for (ServiceParameter currentPar: serviceParameters) {
            if(currentPar.getType() == null)
                continue;
           switch (currentPar.getIn()) {
               case "path":
                   currentMethod = writeToJavaVariable("testURL", (hostname + endpointPath).replaceAll(currentPar.getName(), "DATA"), currentMethod);
                   break;
               case "formData":
                   currentMethod = writeToJavaVariable("parameters", "param(\"" + currentPar.getName() + "\", DATA). \n %parameters", currentMethod);
                   break;
               case "query":
                   currentMethod = writeToJavaVariable("parameters", "queryParam(\"" + currentPar.getName() + "\", DATA). \n %parameters", currentMethod);
                   break;
           }
        }
        return currentMethod;
    }

    /**
     * Set the template function file to use.
     *
     * @param testCaseType for getting the template type.
     */
    public void setTemplate(Enums.testCaseType testCaseType) {
        if(testCaseType != null)
            currentTestCaseType = testCaseType;
        System.out.println("Template : " + testCaseType.toString() + " setted.");
    }

    /**
     * Set the custom location for output folder if needed.
     *
     * @param location File input for setting the location of custom folder. Note that custom folder is defaulted to Generated folder.
     */
    public void setCustomOutputFolder(File location) {
        rootOutputLocation = location.toPath();
    }

    /**
     * //Open the current java file and write the parameters found from parsed service.
     *
     * @throws IOException
     * @param varName for variable field to replace in template file.
     * @param data for replacing the parameter field to text given.
     * @return returns string with wanted parameters inserted into current string that is being processed.
     */
    private String writeToJavaVariable(String varName, String data, String contentOfCurrentFile) throws IOException{
        contentOfCurrentFile = contentOfCurrentFile.replaceAll("%" + varName, data);
        return contentOfCurrentFile;
    }

    /**
     * For each parsed object, go to the template file and copy everthing inside to current java file.
     *
     * @param filetoRead for getting the template file text.
     *
     * @return A brand new string to process parameters and other wanted fields.
     * @throws IOException
     */
    private String appendTestFunction(File filetoRead) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(filetoRead.getAbsolutePath()));
        return new String(encoded, StandardCharsets.UTF_8);
    }

    //------------------------------------------------------------------------------------------------------------------------
    //--------------------------------BASIC UTIL FUNCTIONS--------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Get string and remove and special chars and return regulated text as String
     *
     * @param text for getting a filth ridden text
     * @return return a spotless clean string.
     */
    private String removeSpecialChars(String text) {
        return text.replaceAll("[^a-zA-Z0-9\\s+]", "");
    }

    /**
     * Create a new java file for the new Test cases. Location is defaulted to Generated folder.
     * @throws IOException
     *
     * @return void
     */
    private void createNewJavaFile(String name) throws IOException {
        if(!Files.exists(rootOutputLocation))
            Files.createDirectory(rootOutputLocation);
        currentJavaFile = rootOutputLocation.toString() + "/" + name + ".java";
        File javaFile = new File(currentJavaFile);
        javaFile.createNewFile();

        System.out.println("File created at ->" + currentJavaFile);
    }

    /**
     * Append the given string to Output file.
     *
     * @param fileToWrite to get output file path.
     * @param data data to write into output file path.
     *
     * @throws IOException
     */
    private void appendToJavaFile(String fileToWrite, String data) throws IOException {
        Files.write(Paths.get(fileToWrite), data.getBytes(), StandardOpenOption.APPEND);
    }
}