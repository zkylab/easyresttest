import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class MainTest {
    public static void main(String[] args) {
        // hello easy rest test
        given().urlEncodingEnabled(true)
                .param("username", "user@site.com")
                .param("password", "Pas54321")

                .contentType(ContentType.fromContentType("aa"))
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .post("/login")
                .then().statusCode(200);

    }
}
