package dimple.mt_java_webserver;

import static io.restassured.RestAssured.given;

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
 * This class will test upload of a file followed by the download of the same file and then a delete
 */
public class IntegrationTest

{
    @Before
    public void setup()
    {
        MainApp.startServer(4444);
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 4444;
    }

    @Test
    public void testUploadDownloadAndDeleteOfFile()
            throws Exception
    {
        try
        {
            File initialFile = new File("src/test/resources/sampletextfile.txt");
            InputStream targetStream = new FileInputStream(initialFile);
            final byte[] bytes = IOUtils.toByteArray(targetStream);
            Map<String, String> queryParamMap = new HashMap<>();
            queryParamMap.put("filename", "samplea.txt");
            queryParamMap.put("filepath", "/tmp");
            given().queryParams(queryParamMap).body(bytes).expect().statusCode(200).when().post("/v1/file");

            // now download the file
            Map<String, String> getParams = new HashMap<>();
            String fullPath = File.separator + "tmp" + File.separator + "samplea.txt";
            getParams.put("filenamewithpath", fullPath);
            given().urlEncodingEnabled(false).queryParams(getParams).expect().statusCode(200).when().get("/v1/file");

            // next delete the file
            given().urlEncodingEnabled(false).queryParams(getParams).expect().statusCode(204).when().delete("/v1/file");
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
