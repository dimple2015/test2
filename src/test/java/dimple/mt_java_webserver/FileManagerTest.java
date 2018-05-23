
package dimple.mt_java_webserver;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mt_java_webserver.MainApp;

import io.restassured.RestAssured;

/**
 * This class will test unsupported http request method
 */
public class FileManagerTest
{

    @Before
    public void setup()
    {
        MainApp.startServer(4444);
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 4444;
    }

    @Test
    public void testWithIncorrectHttpMethod()
            throws Exception
    {
        try
        {
            Map<String, String> queryParamMap = new HashMap<>();
            String fullPath = File.separator + "tmp" + File.separator + "sample.txt";
            queryParamMap.put("filenamewithpath", fullPath);
            given().urlEncodingEnabled(false).queryParams(queryParamMap).expect().statusCode(405).and()
                    .body(containsString("Unsupported Request Method. Only GET, DELETE and POST are supported")).when()
                    .put("/v1/file");
        }
        finally
        {
            RestAssured.reset();
        }
    }

    @After
    public void tearDown()
    {
        MainApp.stopServer();
    }

}
