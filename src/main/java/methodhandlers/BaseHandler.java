package methodhandlers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * 
 * Handles the "/" context path
 */
public class BaseHandler
        implements HttpHandler
{

    /*
     * (non-Javadoc)
     * @see com.sun.net.httpserver.HttpHandler#handle(com.sun.net.httpserver.HttpExchange)
     */
    public void handle(HttpExchange exchange)
            throws IOException
    {
        String response = "The web server is up and running";
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
        OutputStream outStream = exchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }

}
