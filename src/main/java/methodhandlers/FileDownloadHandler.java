package methodhandlers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * This class handles the download of a file
 * 
 */
public class FileDownloadHandler
        implements HttpHandler
{

    /*
     * (non-Javadoc)
     * @see com.sun.net.httpserver.HttpHandler#handle(com.sun.net.httpserver.HttpExchange)
     */
    public void handle(HttpExchange exchange)
            throws IOException
    {
        OutputStream outStream = exchange.getResponseBody();
        Headers responseHeader = exchange.getResponseHeaders();
        String query = exchange.getRequestURI().getQuery();
        if ( query != null )
        {
            String params[] = query.split("=");
            String response = null;
            if ( params[0].equalsIgnoreCase("filenamewithpath") )
            {
                String fileName = params[1];
                File file = new File(fileName);
                if ( file.exists() )
                {
                    byte[] bytearray = new byte[(int) file.length()];
                    FileInputStream fis = new FileInputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    bis.read(bytearray, 0, bytearray.length);

                    String contentType = determineContentType(fileName);
                    if ( !contentType.equalsIgnoreCase("Unsupported file format") )
                    {
                        responseHeader.add("Content-Type", contentType);
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, bytearray.length);
                        outStream.write(bytearray);
                        bis.close();
                    }
                    else
                    {
                        response = "The file with specified extension is not supported";
                        sendResponse(response, HttpURLConnection.HTTP_BAD_REQUEST, outStream, exchange, responseHeader,
                                "text/plain");
                    }
                }
                else
                {
                    response = "File does not exist";
                    sendResponse(response, HttpURLConnection.HTTP_BAD_REQUEST, outStream, exchange, responseHeader,
                            "text/plain");
                }
            }
            else
            {
                response = "Invalid request parameter";
                sendResponse(response, HttpURLConnection.HTTP_BAD_REQUEST, outStream, exchange, responseHeader,
                        "text/plain");
            }
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
    private void sendResponse(String response, int returnCode, OutputStream os, HttpExchange he, Headers responseHeader,
            String contentType)
            throws IOException
    {
        responseHeader.add("Content-Type", contentType);
        he.sendResponseHeaders(returnCode, response.length());
        os.write(response.getBytes());
    }

    /**
     * This method determines the content type for the response based on the file extension
     * 
     * @param fileName - the name of the file
     * @return the content type
     */
    private String determineContentType(String fileName)
    {
        if ( fileName.endsWith(".txt") ) return "text/plain";
        if ( fileName.endsWith(".xml") ) return "text/xml";
        if ( fileName.endsWith(".json") ) return "application/json";
        return "Unsupported file format";
    }

}
