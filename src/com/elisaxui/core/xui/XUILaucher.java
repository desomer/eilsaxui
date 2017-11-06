package com.elisaxui.core.xui;


import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.http2.HTTP2Cipher;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
import org.eclipse.jetty.rewrite.handler.RedirectPatternRule;
import org.eclipse.jetty.rewrite.handler.RewriteHandler;
import org.eclipse.jetty.rewrite.handler.RewritePatternRule;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.NegotiatingServerConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.MovedContextHandler;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import com.elisaxui.AppConfig;


public class XUILaucher {

	
//    private static ConstraintSecurityHandler buildConstraintSecurityHandler() {
//        // this configures jetty to require HTTPS for all requests
//        Constraint constraint = new Constraint();
//        constraint.setDataConstraint(Constraint.DC_CONFIDENTIAL);
//        ConstraintMapping mapping = new ConstraintMapping();
//        mapping.setPathSpec("/*");
//        mapping.setConstraint(constraint);
//        ConstraintSecurityHandler security = new ConstraintSecurityHandler();
//        security.setConstraintMappings(Collections.singletonList(mapping));
//        return security;
//    }
//	
//    private static ContextHandler buildStaticResourceHandler() {
//        // if you want the static content to serve off a url like
//        // localhost:8080/files/.... then put 'files' in the constructor
//        // to the ContextHandler
//        ContextHandler ch = new ContextHandler("/");
//        ResourceHandler rh = new ResourceHandler();
//        rh.setWelcomeFiles(new String[]{"index.html"});
//        rh.setResourceBase(".");
//        ch.setHandler(rh);
//        return ch;
//    }
    
	public static void main(String[] args) throws Exception {

		Server server = new Server();
		
        //*******************************************************************/
		RewriteHandler rewrite = new RewriteHandler();
		rewrite.setRewriteRequestURI(true);
		rewrite.setRewritePathInfo(false);
		rewrite.setOriginalPathAttribute("requestedPath");

		String[] redirectArray = { "", "/main", "/manager" };
		for (String redirect : redirectArray) {
			RedirectPatternRule rule = new RedirectPatternRule();
			rule.setTerminating(true);
			rule.setPattern(redirect);
			rule.setLocation("/index.html");    // redirection 302
			rewrite.addRule(rule);
		}

		RewritePatternRule oldToNew = new RewritePatternRule();
//		oldToNew.setPattern("/fr/*");
//		oldToNew.setReplacement("/rest/page/fr/fra/id/");   // redirection interne
//		oldToNew.setTerminating(true);
//		oldToNew.setHandling(false);
//		rewrite.addRule(oldToNew);
		
		oldToNew = new RewritePatternRule();
		oldToNew.setPattern("/index.html");
		oldToNew.setReplacement("/rest/page/fr/fra/id/main");   // redirection interne
		oldToNew.setTerminating(true);
		oldToNew.setHandling(false);
		rewrite.addRule(oldToNew);
		
		oldToNew = new RewritePatternRule();
		oldToNew.setPattern("/shop.html");
		oldToNew.setReplacement("/rest/page/fr/fra/id/shop");   // redirection interne
		oldToNew.setTerminating(true);
		oldToNew.setHandling(false);
		rewrite.addRule(oldToNew);
		
		oldToNew = new RewritePatternRule();
		oldToNew.setPattern("/sw.js");
		oldToNew.setReplacement("/rest/js/sw.js");   // redirection interne
		oldToNew.setTerminating(true);
		oldToNew.setHandling(false);
		rewrite.addRule(oldToNew);
		
		oldToNew = new RewritePatternRule();
		oldToNew.setPattern("/.well-known/acme-challenge/*");
		oldToNew.setReplacement("/rest/page/challenge/");    // redirection interne
		oldToNew.setTerminating(true);
		oldToNew.setHandling(false);
		rewrite.addRule(oldToNew);

		/*********************************************************************/
		ServletContextHandler restHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
		restHandler.setContextPath("/rest");

		ServletHolder jerseyServlet = restHandler.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
		jerseyServlet.setInitOrder(0);
		jerseyServlet.setInitParameter("jersey.config.server.provider.classnames",
				XUIFactoryXHtml.class.getCanonicalName());
		jerseyServlet.setInitParameter("jersey.config.server.provider.packages", AppConfig.getRestPackage());

		/*********************************************************************/
		ContextHandler basicHandler = new ContextHandler();
		basicHandler.setContextPath("/asset");
		// basicHandler.setResourceBase(".");
		basicHandler.setHandler(new XUIFactoryBasic());

		/*********************************************************************/
		GzipHandler gzipHolder = new GzipHandler();
		gzipHolder.setIncludedMimeTypes("text/html", "text/plain", "text/xml",
				"text/css", "application/javascript", "text/javascript", "application/json");

		HandlerCollection myhandlers = new HandlerCollection(true);
		myhandlers.addHandler(restHandler);
		myhandlers.addHandler(basicHandler);
		gzipHolder.setHandler(myhandlers);
		rewrite.setHandler(gzipHolder);

		
        MovedContextHandler movedHandlerToHttps = new MovedContextHandler();
        movedHandlerToHttps.setNewContextURL("https://www.elisys-dyslexie.com");    // force en https
        movedHandlerToHttps.setContextPath("/*");
        movedHandlerToHttps.setPermanent(true);
        movedHandlerToHttps.setDiscardPathInfo(false);
        movedHandlerToHttps.setDiscardQuery(false);
        movedHandlerToHttps.setVirtualHosts(new String[]{"@unsecured"});   // uniquement en http
		
		HandlerCollection myhandlers2 = new HandlerCollection(true);
		myhandlers2.addHandler(movedHandlerToHttps);   // redirection to https
	//	myhandlers2.addHandler(new SecuredRedirectHandler());   // interdit le non secure
		myhandlers2.addHandler(rewrite);
		

      //  movedHandler.setVirtualHosts(new String[]{"elisys-dyslexie.com"});  //Note that this will also redirect example.com to www.example.com:

//        ConstraintSecurityHandler securityHandlerAllHttps = buildConstraintSecurityHandler();
//        securityHandlerAllHttps.setHandler(rewrite);
//        
//        ConstraintSecurityHandler securityHandlerHttp = buildConstraintSecurityHandler();
//        ContextHandler ch  = buildStaticResourceHandler();
//        securityHandlerHttp.setHandler(ch);   // a ajouter

		
		/******************************** HTTPS **************************************/
		HttpConfiguration config = getHttpConfiguration();

		HttpConnectionFactory https1 = new HttpConnectionFactory(config);
	    HTTP2ServerConnectionFactory https2 = new HTTP2ServerConnectionFactory(config);
	    
	    NegotiatingServerConnectionFactory.checkProtocolNegotiationAvailable();
	    	    
	    ALPNServerConnectionFactory alpn = new ALPNServerConnectionFactory();
	    alpn.setDefaultProtocol(https1.getProtocol()); // sets default protocol to HTTP 1.1

		
		SslContextFactory sslContextFactory = new SslContextFactory();
		sslContextFactory.setKeyStorePath("C:\\Users\\gauth\\git\\eilsaxui\\\\keystore.jks");
//		sslContextFactory.setKeyStorePath("C:\\Users\\Bureau\\git\\eilsaxui\\keystore.jks"); // (EmbeddedServer.class.getResource("/keystore.jks").toExternalForm()
		sslContextFactory.setKeyStorePassword("123456");
		sslContextFactory.setKeyManagerPassword("123456");
		
		sslContextFactory.setCipherComparator(HTTP2Cipher.COMPARATOR);
	    sslContextFactory.setUseCipherSuitesOrder(true);
		
	    SslConnectionFactory ssl = new SslConnectionFactory(sslContextFactory, alpn.getProtocol());

		ServerConnector sslConnector = new ServerConnector(server,
				ssl,
				alpn,
				https2,
				https1
				);
		sslConnector.setPort(9998);

		/********************************* HTTP    ****************************************/
		
		ServerConnector httpconnector = new ServerConnector(server);
		httpconnector.setName("unsecured"); // named connector
		httpconnector.setPort(8080);
		
		/***********************************************************************************/
		
		server.setHandler(myhandlers2);   // les handlers 
		
		server.setConnectors(new Connector[] { httpconnector, sslConnector });   // les connectors

		server.start();
		server.join();
	}

	
	private static HttpConfiguration getHttpConfiguration() {
	    HttpConfiguration config = new HttpConfiguration();
	    config.setSecureScheme("https");
	    config.setSecurePort(9998);
	    config.setSendXPoweredBy(false);
	    config.setSendServerVersion(false);
	    config.addCustomizer(new SecureRequestCustomizer());
	    return config;
	}
	
}
