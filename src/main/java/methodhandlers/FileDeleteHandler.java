package methodhandlers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * 
 * This class will handle deletion of files
 */
public class FileDeleteHandler
        implements HttpHandler
{

    /*
     * (non-Javadoc)
     * @see com.sun.net.httpserver.HttpHandler#handle(com.sun.net.httpserver.HttpExchange)
     */
    @Override
    public void handle(HttpExchange exchange)
            throws IOException
    {
        Headers responseHeaders = exchange.getResponseHeaders();
        OutputStream outStream = exchange.getResponseBody();
        String response = "";
        String query = exchange.getRequestURI().getQuery();
        if ( query != null )
        {
            String params[] = query.split("=");
            if ( params[0].equalsIgnoreCase("filenamewithpath") )
            {

                String fileNameWithPath = params[1];
                Path pathval = Paths.get(fileNameWithPath);
                if ( !Files.exists(pathval) )
                {
                    // file does not exist
                    response = "The specified file does not exist";
                    sendResponse(response, HttpURLConnection.HTTP_BAD_REQUEST, outStream, exchange, responseHeaders);
                }
                else
                {
                    Files.delete(pathval);
                    sendResponse(response, HttpURLConnection.HTTP_NO_CONTENT, outStream, exchange, responseHeaders);
                }
            }
        }
        else
        {
            // missing query param
            response = "The filenamewithpath query parameter is missing";
            sendResponse(response, HttpURLConnection.HTTP_BAD_REQUEST, outStream, exchange, responseHeaders);
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
    private void sendResponse(String response, int returnCode, OutputStream os, HttpExchange he, Headers responseHeader)
            throws IOException
    {
        responseHeader.add("Content-Type", "text/plain");
        he.sendResponseHeaders(returnCode, response.length());
        os.write(response.getBytes());
    }

}
