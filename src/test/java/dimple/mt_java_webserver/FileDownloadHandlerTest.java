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
 * Test class for testing download of files
 */
public class FileDownloadHandlerTest
{

    @Before
    public void setup()
    {
        MainApp.startServer(4444);
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 4444;
    }

    @Test
    public void testDownloadFileWithNonExistentFile()
            throws Exception
    {
        try
        {
            Map<String, String> queryParamMap = new HashMap<>();
            String fullPath = File.separator + "tmp" + File.separator + "nonexistent.txt";
            queryParamMap.put("filenamewithpath", fullPath);
            given().urlEncodingEnabled(false).queryParams(queryParamMap).expect().statusCode(400).and()
                    .body(containsString("File does not exist")).when().get("/v1/file");

        }
        finally
        {
            RestAssured.reset();
        }
    }

    @Test
    public void testDownloadFileWithInvalidRequestParameter()
            throws Exception
    {
        try
        {
            Map<String, String> queryParamMap = new HashMap<>();
            String fullPath = File.separator + "tmp" + File.separator + "samplea.txt";
            queryParamMap.put("filename", fullPath);
            given().urlEncodingEnabled(false).queryParams(queryParamMap).expect().statusCode(400).and()
                    .body(containsString("Invalid request parameter")).when().get("/v1/file");

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
