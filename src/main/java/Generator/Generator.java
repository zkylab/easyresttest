package Generator;

import DataManager.DataManager;
import Parser.Parser;
import SwaggerObjects.Service;
import SwaggerObjects.ServiceParameter;
import SwaggerObjects.ServiceResponse;
import enums.enums;
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
    private static Path rootOutputLocation = Paths.get("Generated");
    //Current java file under construction.
    private static String currentJavaFile = null;
    //Method template. Defaulted to basic GetURL template.
    private static enums.testCaseType currentTestCaseType = enums.testCaseType.GetURLAssertStatusCode;

    /**
     * Get the generator instance.
     *
     * @return void
     */
    public static Generator getInstance() {
        if(generator_instance == null)
            generator_instance = new Generator();
        return generator_instance;
    }

    /**
     * Run the code generator after location of the parsed Services are set.
     *
     * @throws ServiceUnavailableException
     *
     * @return void
     */
    public void runGenerator() throws Exception {
        if(parsedServices == null)
            throw new ServiceUnavailableException("Parser Service is null.");
        generate();
    }

    /**
     * Generate the testcases.
     *
     * @throws Exception
     * @return ArrayList<File>
     */
    private ArrayList<File> generate() throws Exception {
        createNewJavaFile("TestCases");
        String methodName = null, method = null;
        ContentType requestContentType = null, responseContentType = null;
        ArrayList<ServiceParameter> serviceParameters = null;
        ServiceResponse response = null;
        if(currentJavaFile == null)
            throw new FileNotFoundException("Java file not found.");
        for (Service currentService : parsedServices) {
            //Copy the template test function from the template files.
            String currentMethod = appendTestFunction(new File(Paths.get("resources").toAbsolutePath().toString() + "/" + currentTestCaseType.toString() + ".re"));
            //Get the current parameters from service object.
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
            insertServiceParameters(serviceParameters, currentMethod);
            response = currentService.getResponse();
            if(response != null)
                currentMethod = writeToJavaVariable("statusCode", Integer.toString(response.getStatusCode()), currentMethod);
            else
                System.out.println("Status code from response is null. Skipping.");
            currentMethod += "\n\n";
            appendToJavaFile(currentJavaFile, currentMethod);
        }
        return null;
    }

    /**
     * Set the parsed services from The Parsed class.
     *
     * @param parsedFile
     */
    public void setParsedServices(File parsedFile) {
        Parser parser = Parser.getInstance();
        parser.setJSON(parsedFile);
        parser.runParser();
        parsedServices = DataManager.getInstance().getServices();
    }

    /**
     * Write everything found inside service parameters array.
     *
     * @param serviceParameters ArrayList<ServiceParameter>
     * @param currentMethod String
     * @throws IOException
     */
    private void insertServiceParameters(ArrayList<ServiceParameter> serviceParameters, String currentMethod) throws IOException {
        if(serviceParameters == null) {
            writeToJavaVariable("parameters", "", currentMethod);
            return;
        }
        for (ServiceParameter currentPar: serviceParameters) {

        }
    }

    /**
     * Set the template function file to use.
     *
     */
    public void setTemplate(enums.testCaseType testCaseType) {
        if(testCaseType != null)
            currentTestCaseType = testCaseType;
        System.out.println("Template : " + testCaseType.toString() + " setted.");
    }

    /**
     * Set the custom location for output folder if needed.
     *
     * @param location File
     */
    public void setCustomOutputFolder(File location) {
        rootOutputLocation = location.toPath();
    }

    /**
     * //Open the current java file and write the parameters found from parsed service.
     *
     * @throws IOException
     * @param varName String
     * @param data String
     */
    private String writeToJavaVariable(String varName, String data, String contentOfCurrentFile) throws IOException{
        contentOfCurrentFile = contentOfCurrentFile.replaceAll("%" + varName, data);
        return contentOfCurrentFile;
    }

    /**
     * For each parsed object, go to the template file and copy everthing inside to current java file.
     *
     * @param fileToRead String
     *
     * @return String
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
     * @param text
     * @return String
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
        currentJavaFile = rootOutputLocation.toString() + "/" + name + ".java";
        File javaFile = new File(currentJavaFile);
        javaFile.createNewFile();

        System.out.println("File created at ->" + currentJavaFile);
    }

    /**
     * Append the given string to Output file.
     *
     * @param fileToWrite File
     * @param data String
     *
     * @throws IOException
     */
    private void appendToJavaFile(String fileToWrite, String data) throws IOException {
        Files.write(Paths.get(fileToWrite), data.getBytes(), StandardOpenOption.APPEND);
    }
}
