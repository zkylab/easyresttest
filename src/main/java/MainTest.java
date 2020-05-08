import DataManager.DataManager;
import Generator.Generator;
import Parser.Parser;
import SwaggerObjects.Service;

import javax.naming.ServiceUnavailableException;
import java.io.File;
import java.io.IOException;
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

//        Generator generator = Generator.getInstance();
//        generator.setParsedServices(new File("D:\\Workspace\\easyresttest\\src\\main\\sample-data\\petshop-swagger.json"));
//        generator.runGenerator();
    }
}
