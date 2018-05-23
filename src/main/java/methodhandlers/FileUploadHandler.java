package methodhandlers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * 
 * This class handles the POST request to upload files
 */
public class FileUploadHandler
        implements HttpHandler
{

    private final static String FILENAME_PARAM = "filename";
    private final static String FILEPATH_PARAM = "filepath";

    /*
     * (non-Javadoc)
     * @see com.sun.net.httpserver.HttpHandler#handle(com.sun.net.httpserver.HttpExchange)
     */
    public void handle(HttpExchange exchange)
            throws IOException
    {
        OutputStream outStream = exchange.getResponseBody();
        Headers responseHeaders = exchange.getResponseHeaders();
        Headers headers = exchange.getRequestHeaders();
        int contentLen = 0;
        String query = exchange.getRequestURI().getQuery();
        if ( query != null )
        {
            String params[] = query.split("&");
            Map<String, String> queryMap = new HashMap<String, String>();
            String fileName = null;
            String filePath = null;
            for (String queryparam : params)
            {
                String[] pairs = queryparam.split("=");
                queryMap.put(pairs[0], pairs[1]);
            }
            if ( !queryMap.containsKey(FILENAME_PARAM) )
            {
                String response = "Missing filename parameter";
                sendInvalidResponse(response, HttpURLConnection.HTTP_BAD_REQUEST, outStream, exchange, responseHeaders);
            }
            else
            {
                fileName = queryMap.get(FILENAME_PARAM);
            }
            if ( !queryMap.containsKey(FILEPATH_PARAM) )
            {
                String response = "Missing filepath parameter";
                sendInvalidResponse(response, HttpURLConnection.HTTP_BAD_REQUEST, outStream, exchange, responseHeaders);
            }
            else
            {
                filePath = queryMap.get(FILEPATH_PARAM);
            }
            if ( headers.getFirst("Content-Length") != null )
            {
                contentLen = Integer.parseInt(headers.getFirst("Content-Length"));
                byte[] fileContents = new byte[contentLen];
                InputStream inStream = exchange.getRequestBody();
                inStream.read(fileContents);
                responseHeaders.add("Content-Type", "text/plain");
                Path pathVal = Paths.get(filePath + File.separator + fileName);
                Files.write(pathVal, fileContents);
                String response = "Sucessfully wrote bytes to file";
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
                outStream.write(response.getBytes());
                inStream.close();
            }
        }
        else
        {
            String response = "The filename and filepath query parameters are missing";
            sendInvalidResponse(response, HttpURLConnection.HTTP_BAD_REQUEST, outStream, exchange, responseHeaders);
        }
        outStream.close();
    }

    /**
     * 
     * @param response - the response written to the output stream
     * @param returnCode - the http status code
     * @param os - the output stream
     * @param he - http exchange
     * @param responseHeader - the response header
     * @throws IOException - The IO exception
     */
    private void sendInvalidResponse(String response, int returnCode, OutputStream os, HttpExchange he,
            Headers responseHeader)
            throws IOException
    {
        responseHeader.add("Content-Type", "text/plain");
        he.sendResponseHeaders(returnCode, response.length());
        os.write(response.getBytes());
    }

}
