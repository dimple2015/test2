package methodhandlers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * This class forwards the requests to /v1/file context path to the appropriate handlers
 * 
 */
public class FileManager
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
        String requestMethod = exchange.getRequestMethod();
        switch (requestMethod)
        {
            case "POST":
                new FileUploadHandler().handle(exchange);
                break;
            case "GET":
                new FileDownloadHandler().handle(exchange);
                break;
            case "DELETE":
                new FileDeleteHandler().handle(exchange);
                break;
            default:
                String response = "Unsupported Request Method. Only GET, DELETE and POST are supported";
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, response.length());
                OutputStream outStream = exchange.getResponseBody();
                outStream.write(response.getBytes());
                outStream.close();
        }
    }

}
