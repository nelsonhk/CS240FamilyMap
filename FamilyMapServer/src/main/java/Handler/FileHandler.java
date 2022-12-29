package Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
            String filePath;
            String urlPath = exchange.getRequestURI().toString();
            if (urlPath == null || urlPath.equals("/")) {
                filePath = "web/index.html";
            } else {
                filePath = "web" + urlPath;
            }

            File file = new File(filePath);
            OutputStream resBody = exchange.getResponseBody();
            if (!file.exists()) {
                File file404 = new File("web/HTML/404.html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                Files.copy(file404.toPath(), resBody);

            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                Files.copy(file.toPath(), resBody);
            }
            resBody.close();
        }
    }

}
