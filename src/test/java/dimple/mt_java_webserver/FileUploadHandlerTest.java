package dimple.mt_java_webserver;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mt_java_webserver.MainApp;

import io.restassured.RestAssured;

/**
 * 
 * This class contains unit test for upload of files
 */
public class FileUploadHandlerTest
{
    /**
     * Initial setup
     */
    @Before
    public void setup()
    {
        MainApp.startServer(4444);
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 4444;
    }

    @Test
    public void testUploadFileWithMissingFilePathParam()
            throws Exception
    {
        try
        {
            File initialFile = new File("src/test/resources/sampletextfile.txt");
            InputStream targetStream = new FileInputStream(initialFile);
            final byte[] bytes = IOUtils.toByteArray(targetStream);
            Map<String, String> queryParamMap = new HashMap<>();
            queryParamMap.put("filename", "samplea.txt");
            given().urlEncodingEnabled(false).queryParams(queryParamMap).body(bytes).expect().statusCode(400).and()
                    .body(containsString("Missing filepath parameter")).when().post("/v1/file");

        }
        finally
        {
            RestAssured.reset();
        }
    }

    @Test
    public void testUploadFileWithMissingFileNameParam()
            throws Exception
    {
        try
        {
            File initialFile = new File("src/test/resources/sampletextfile.txt");
            InputStream targetStream = new FileInputStream(initialFile);
            final byte[] bytes = IOUtils.toByteArray(targetStream);
            Map<String, String> queryParamMap = new HashMap<>();
            queryParamMap.put("filepath", "/tmp");
            given().urlEncodingEnabled(false).queryParams(queryParamMap).body(bytes).expect().statusCode(400).and()
                    .body(containsString("Missing filename parameter")).when().post("/v1/file");
        }
        finally
        {
            RestAssured.reset();
        }
    }

    @Test
    public void testUploadFileWithMissingAllParams()
            throws Exception
    {
        try
        {
            File initialFile = new File("src/test/resources/sampletextfile.txt");
            InputStream targetStream = new FileInputStream(initialFile);
            final byte[] bytes = IOUtils.toByteArray(targetStream);
            given().urlEncodingEnabled(false).body(bytes).expect().statusCode(400).and()
                    .body(containsString("The filename and filepath query parameters are missing")).when()
                    .post("/v1/file");
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
