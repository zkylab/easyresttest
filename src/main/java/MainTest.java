import Asserter.AssertionParser;
import DataManager.DataManager;
import CodeGenerator.CodeGenerator;
import Parser.Parser;
import SwaggerObjects.Service;

import java.io.File;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
@SuppressWarnings("unchecked")
public class MainTest {
    public static void main(String[] args) throws Exception{
        // hello easy rest test
        File swaggerFile = new File("D:\\Workspace\\easyresttest\\src\\main\\sample-data\\petshop-swagger.json");

        Parser parser = Parser.getInstance();
        parser.setJSON(swaggerFile);
        parser.runParser();

        ArrayList<Service> services = DataManager.getInstance().getServices();
        System.out.println(services);

        AssertionParser gen = AssertionParser.getInstance();
        gen.runAssertionGenerator();

        CodeGenerator generator = CodeGenerator.getInstance();
        generator.setParsedServices(new File("D:\\Workspace\\easyresttest\\src\\main\\sample-data\\petshop-swagger.json"));
        generator.runGenerator("test", DataManager.getInstance().getHost());

        String host = DataManager.getInstance().getHost();
        System.out.println(host);
    }
}