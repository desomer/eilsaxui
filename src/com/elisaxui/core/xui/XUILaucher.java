package com.elisaxui.core.xui;

import org.eclipse.jetty.rewrite.handler.RedirectPatternRule;
import org.eclipse.jetty.rewrite.handler.RewriteHandler;
import org.eclipse.jetty.rewrite.handler.RewritePatternRule;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;

//import com.github.leonardoxh.gzipfilter.GZIPFilter;

public class XUILaucher {

	public static void main(String[] args) throws Exception {


		Server server = new Server();
		
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(8080);

		RewriteHandler rewrite = new RewriteHandler();
		rewrite.setRewriteRequestURI(true);
		rewrite.setRewritePathInfo(false);
		rewrite.setOriginalPathAttribute("requestedPath");
		  
		String[] redirectArray = {"", "/main", "/manager"};
		for (String redirect : redirectArray) {
		    RedirectPatternRule rule = new RedirectPatternRule();
		    rule.setTerminating(true);
		    rule.setPattern(redirect);
		    rule.setLocation("/rest/page/fr/fra/id/standard");
		    rewrite.addRule(rule);
		}
		
		RewritePatternRule oldToNew = new RewritePatternRule();
		oldToNew.setPattern("/fr/*");
		oldToNew.setReplacement("/rest/page/fr/fra/id/");
		oldToNew.setTerminating(true);
		oldToNew.setHandling(false);
		rewrite.addRule(oldToNew);   
		
		
		/*********************************************************************/
		ServletContextHandler restHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
		restHandler.setContextPath("/rest");
		
		ServletHolder jerseyServlet = restHandler.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
		jerseyServlet.setInitOrder(0);
		jerseyServlet.setInitParameter("jersey.config.server.provider.classnames",	XUIFactoryXHtml.class.getCanonicalName());
		jerseyServlet.setInitParameter("jersey.config.server.provider.packages", "com.elisaxui.srv");
		


		/*********************************************************************/
		ContextHandler basicHandler = new ContextHandler();
		basicHandler.setContextPath("/asset");
	//	basicHandler.setResourceBase(".");
		basicHandler.setHandler(new XUIFactoryBasic());
		// basicHandler.setClassLoader(Thread.currentThread().getContextClassLoader());

		/*********************************************************************/	
	    GzipHandler gzipHolder = new GzipHandler();
	    gzipHolder.setIncludedMimeTypes("text/html", "text/plain", "text/xml", 
	            "text/css", "application/javascript", "text/javascript", "application/json");
	    
		HandlerCollection myhandlers = new HandlerCollection(true);
		myhandlers.addHandler(restHandler);
		myhandlers.addHandler(basicHandler);
		gzipHolder.setHandler(myhandlers);
		
		rewrite.setHandler(gzipHolder);
		
		server.setHandler(rewrite);
		
		
		 HttpConfiguration https = new HttpConfiguration();
		  https.addCustomizer(new SecureRequestCustomizer());
		  
		  SslContextFactory sslContextFactory = new SslContextFactory();
		 sslContextFactory.setKeyStorePath("C:\\Users\\Bureau\\git\\eilsaxui\\keystore.jks");    //(EmbeddedServer.class.getResource("/keystore.jks").toExternalForm()
 		 sslContextFactory.setKeyStorePassword("123456");
		 sslContextFactory.setKeyManagerPassword("123456");
		 ServerConnector sslConnector = new ServerConnector(server,
		         new SslConnectionFactory(sslContextFactory, "http/1.1"),
		         new HttpConnectionFactory(https));
		 sslConnector.setPort(9998);

		 server.setConnectors(new Connector[] { connector, sslConnector });
		 
		server.start();
		server.join();
	}

}
