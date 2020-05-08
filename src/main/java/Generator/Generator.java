package Generator;

import DataManager.DataManager;
import Parser.Parser;
import SwaggerObjects.Service;
import SwaggerObjects.ServiceParameter;
import SwaggerObjects.ServiceResponse;
import enums.enums;

import javax.naming.ServiceUnavailableException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Generator {
    //Generator class instance for glorious code generation stuff. Neat.
    private static Generator generator_instance = null;
    private static ArrayList<Service> parsedServices = null;
    //This tells the Generator class to which folder it should output its generated .java files.
    private static Path rootOutputLocation = Paths.get("Generated");
    //Current java file under construction.
    private static String currentJavaFile = null;

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
        //TODO:Remove the line below is you found it in final version. setParser Services MUST be called from user class.
        setParsedServices(new File("D:\\Workspace\\easyresttest\\src\\main\\sample-data\\petshop-swagger.json"));
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
        createNewJavaFile("TestCases", enums.testCaseType.GetURLAssertStatusCode);
        String methodName = null, method = null, requestContentType = null, responseContentType = null;
        ArrayList<ServiceParameter> serviceParameters = null;
        ServiceResponse response = null;
        for (Service currentService : parsedServices) {
            //Get the current parameters from service object.
            methodName = removeSpecialChars(currentService.getEndPointPath());
            if(methodName != null)
                writeToJavaVariable("testcaseName", methodName);
            else
                System.out.println("Methgod name is null. Skipping.");
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
     * @throws FileNotFoundException
     * @param varName String
     * @param data String
     */
    private void writeToJavaVariable(String varName, String data) throws FileNotFoundException, IOException{
        if(currentJavaFile == null)
            throw new FileNotFoundException("Java file not found.");
        String contentOfCurrentFile = new String(Files.readAllBytes(Paths.get(currentJavaFile)), StandardCharsets.UTF_8);
        contentOfCurrentFile = contentOfCurrentFile.replaceAll("%" + varName, data);
        //TODO:Underconstuction!!!
    }

    /**
     * Create a new java file for the new Test cases. Location is defaulted to Generated folder.
     * @throws IOException
     *
     * @return void
     */
    private void createNewJavaFile(String name, enums.testCaseType testCaseType) throws IOException {
        currentJavaFile = rootOutputLocation.toString() + "/" + name + ".java";
        File javaFile = new File(currentJavaFile);
        javaFile.createNewFile();

        //TODO:Do not use Buffered reader. It shall be fixed in next commit.
        //Fill the content with chosen template.
        FileReader fr = new FileReader(Paths.get("resources").toAbsolutePath().toString() + "/" + testCaseType.toString() + ".re");
        BufferedReader br = new BufferedReader(fr);
        FileWriter fw = new FileWriter(javaFile.getAbsolutePath(), true);
        String s;
        while (( s = br.readLine()) != null) { // read a line
            fw.write(s); // write to output file
            fw.write(System.getProperty( "line.separator" ));
            fw.flush();
        }
        fw.write(System.getProperty( "line.separator" ));
        br.close();
        fw.close();
        System.out.println("File Copied at ->" + currentJavaFile);
    }

    /**
     * Get string and remove and special chars and return regulated text as String
     *
     * @param text
     * @return
     */
    private String removeSpecialChars(String text) {
        return text.replaceAll("[^a-zA-Z0-9\\s+]", "");
    }
}
