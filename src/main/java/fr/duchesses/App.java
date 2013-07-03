package fr.duchesses;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class App {

    public static void main(String[] args) throws Exception {
        String webappDirLocation = "src/main/webapp/";

        Server server = new Server(Integer.valueOf(System.getenv("PORT")));
        WebAppContext root = new WebAppContext();

        root.setContextPath("/");
        root.setDescriptor(webappDirLocation + "/WEB-INF/web.xml");
        root.setResourceBase(webappDirLocation);

        root.setParentLoaderPriority(true);

        server.setHandler(root);

        server.start();
        server.join();
    }
}
