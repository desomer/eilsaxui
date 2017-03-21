package com.elisaxui.core.xui;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class XUILaucher {

	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);

		HandlerCollection myhandlers = new HandlerCollection(true);
		
		ServletContextHandler restHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		restHandler.setContextPath("/rest");

		ServletHolder jerseyServlet = restHandler.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
		jerseyServlet.setInitOrder(0);
		jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", XUIFactoryXHtml.class.getCanonicalName());
		myhandlers.addHandler(restHandler);
		
		ContextHandler basicHandler = new ContextHandler();
		basicHandler.setContextPath("/basic");
		//basicHandler.setResourceBase(".");
		basicHandler.setHandler(new XUIFactoryBasic());
	//	basicHandler.setClassLoader(Thread.currentThread().getContextClassLoader());
		myhandlers.addHandler(basicHandler);

		server.setHandler(myhandlers);
		
		server.start();
		server.join();
	}

}
