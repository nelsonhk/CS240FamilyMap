package Handler;

import DataAccess.DataAccessException;
import Result.ClearResult;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;

public class Clear extends BaseHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            boolean success = false;

            try {
                // Only allow POST requests for this operation.
                if (exchange.getRequestMethod().equalsIgnoreCase("post")) {

                    Service.Clear service = new Service.Clear();
                    ClearResult result = service.clear();

                    if (result.isSuccess()) {
                        Gson gson = new Gson();
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        OutputStream resBody = exchange.getResponseBody();
                        writeString(gson.toJson(result), resBody);
                        resBody.close();
                        success = true;
                    }
                }

                if (!success) {
                    // The HTTP request was invalid somehow, so we return a "bad request"
                    // status code to the client.
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                    // We are not sending a response body, so close the response body
                    // output stream, indicating that the response is complete.
                    exchange.getResponseBody().close();
                }
            }
            catch (IOException | DataAccessException e) {
                // Some kind of internal error has occurred inside the server (not the
                // client's fault), so we return an "internal server error" status code
                // to the client.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);

                // We are not sending a response body, so close the response body
                // output stream, indicating that the response is complete.
                exchange.getResponseBody().close();

                // Display/log the stack trace
                e.printStackTrace();
            }
        }

}
