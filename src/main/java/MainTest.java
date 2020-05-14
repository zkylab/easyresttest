import DataManager.DataManager;
import Parser.Parser;
import SwaggerObjects.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
@SuppressWarnings("unchecked")
public class MainTest {
    public static void main(String[] args) throws IOException {
        // hello easy rest test
        File swaggerFile = new File("../swagger-module/src/main/sample-data/petshop-swagger.json");

        Parser parser = Parser.getInstance();
        parser.setJSON(swaggerFile);
        parser.runParser();

        ArrayList<Service> services = DataManager.getInstance().getServices();
        System.out.println(services);


        String host = DataManager.getInstance().getHost();
        System.out.println(host);
    }
}
