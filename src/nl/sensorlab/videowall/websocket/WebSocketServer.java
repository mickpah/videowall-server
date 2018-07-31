package nl.sensorlab.videowall.websocket;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class WebSocketServer {
	
	private static Server server = null;

	public static void start(int port) {
		
		if (server != null) {
			System.err.println("Server already started");
			return;
		}

		server = new Server();
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(port);
		server.addConnector(connector);

		// Setup the basic application "context" for this application at "/"
		// This is also known as the handler tree (in jetty speak)
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);

		// Add a websocket to a specific path spec
		ServletHolder holderEvents = new ServletHolder("ws-events", Servlet.class);
		context.addServlet(holderEvents, "/*");

		try {
			server.start();
		} catch (Throwable t) {
			t.printStackTrace(System.err);
		}
	}
	
	public static void stop() {
		
		if (server != null) {
			try {
				server.join();
			} catch (InterruptedException e) {
				e.printStackTrace(System.err);
			}
		}
		
	}
}