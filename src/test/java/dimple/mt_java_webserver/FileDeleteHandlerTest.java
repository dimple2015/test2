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
 * 
 * This class tests the deletion of a file
 */
public class FileDeleteHandlerTest
{
    @Before
    public void setup()
    {
        MainApp.startServer(4444);
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 4444;
    }

    @Test
    public void testDeleteOfNonExistentFile()
    {
        try
        {
            Map<String, String> queryParamMap = new HashMap<>();
            String fullPath = File.separator + "tmp" + File.separator + "somerandomfile.txt";
            queryParamMap.put("filenamewithpath", fullPath);
            given().urlEncodingEnabled(false).queryParams(queryParamMap).expect().statusCode(400).and()
                    .body(containsString("The specified file does not exist")).when().delete("/v1/file");
        }
        finally
        {
            RestAssured.reset();
        }
    }

    @Test
    public void testDeleteWithMissingQueryParam()
    {
        try
        {
            String fullPath = File.separator + "tmp" + File.separator + "somerandomfile.txt";
            given().urlEncodingEnabled(false).expect().statusCode(400).and()
                    .body(containsString("The filenamewithpath query parameter is missing")).when().delete("/v1/file");
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
