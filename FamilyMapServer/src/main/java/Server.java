import java.io.*;
import java.net.*;

import Handler.*;
import com.sun.net.httpserver.*;

/*
	The Server class is the "main" class for the server (i.e., it contains the 
		"main" method for the server program).
	When the server runs, all command-line arguments are passed in to Server.main.
	For this server, the only command-line argument is the port number on which 
		the server should accept incoming client connections.
*/
public class Server {
	private static final int MAX_WAITING_CONNECTIONS = 12;
	private HttpServer server;

	private void run(String portNumber) {

		System.out.println("Initializing HTTP Server");
		try {
			server = HttpServer.create(
						new InetSocketAddress(Integer.parseInt(portNumber)),
						MAX_WAITING_CONNECTIONS);
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("IOException in Server.java");
			return;
		}

		server.setExecutor(null);

		System.out.println("Creating contexts");

		server.createContext("/user/register", new Register());
		server.createContext("/user/login", new Login());
		server.createContext("/clear", new Clear());
        server.createContext("/fill", new Fill());
		server.createContext("/load", new Load());
		server.createContext("/person/", new PersonID());
		server.createContext("/person", new Person());
		server.createContext("/event/", new EventID());
		server.createContext("/event", new Event());

		server.createContext("/", new FileHandler());

		System.out.println("Starting server");
		server.start();
		System.out.println("Server started");
	}

	public static void main(String[] args) {		
		String portNumber = args[0];
		new Server().run(portNumber);
	}
}

