package com.elisaxui.core.xui;


import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.URL;
import java.security.CodeSource;
import java.security.Security;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.conscrypt.OpenSSLProvider;
import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.http2.HTTP2Cipher;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
import org.eclipse.jetty.rewrite.handler.RedirectPatternRule;
import org.eclipse.jetty.rewrite.handler.RewriteHandler;
import org.eclipse.jetty.rewrite.handler.RewritePatternRule;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.ForwardedRequestCustomizer;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
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
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.StdErrLog;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

import com.elisaxui.AppConfig;
import com.elisaxui.core.helper.JSExecutorHelper;
import com.elisaxui.core.xui.config.WatchDir;
import com.elisaxui.core.xui.config.XHTMLAppScanner;
import com.elisaxui.core.xui.rest.XUIRestApp;


public class XUILaucher {

    
	public static void main(String[] args) throws Exception {
		
		JSExecutorHelper.initGlobal();
		
//		StdErrLog log =new StdErrLog();
//		log.setLevel(StdErrLog.LEVEL_DEBUG);
//		
//		Log.setLog(log);

		System.out.println("start " + System.getProperty("sun.java.command"));
		
	    RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
	    List<String> arguments = runtimeMXBean.getInputArguments();
	    for (String string : arguments) {
		    System.out.println(string);
		}

		Server server = new Server();
//		server.setDumpAfterStart(true);
		
	    System.out.println("start jetty server " + server.getVersion());
        //*******************************************************************/
		RewriteHandler rewrite = new RewriteHandler();
		initRewriting(rewrite);

		/*********************************************************************/
		ServletContextHandler restHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
		restHandler.setContextPath("/rest");
		initJersey(restHandler);

		/*********************************************************************/
		ContextHandler basicHandler = new ContextHandler();
		basicHandler.setContextPath("/asset");
		initXUIFactoryBasic(basicHandler);

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
	//	myhandlers2.addHandler(movedHandlerToHttps);   // redirection to https
		myhandlers2.addHandler(rewrite);
			
		/******************************** HTTPS **************************************/
		HttpConfiguration configHttps = getHttpsConfiguration();

		Security.addProvider(new OpenSSLProvider());

		
		SslContextFactory sslContextFactory = new SslContextFactory();
		sslContextFactory.setKeyStorePath("C:\\Users\\gauth\\git\\eilsaxui\\\\keystore.jks");
//		sslContextFactory.setKeyStorePath("C:\\Users\\Bureau\\git\\eilsaxui\\keystore.jks"); // (EmbeddedServer.class.getResource("/keystore.jks").toExternalForm()
		sslContextFactory.setKeyStorePassword("123456");
		sslContextFactory.setKeyManagerPassword("123456");
//		sslContextFactory.setProvider("Conscrypt");
		
		sslContextFactory.setCipherComparator(HTTP2Cipher.COMPARATOR);
	    sslContextFactory.setUseCipherSuitesOrder(true);
	    
//        sslContextFactory.setIncludeProtocols("TLSv1.2");
//        sslContextFactory.setIncludeCipherSuites("TLS_RSA_WITH_AES_256_CBC_SHA");
//	    sslContextFactory.setExcludeCipherSuites("SSL_RSA_WITH_3DES_EDE_CBC_SHA", "SSL_DHE_RSA_WITH_DES_CBC_SHA", "SSL_DHE_DSS_WITH_DES_CBC_SHA");
//	    sslContextFactory.addExcludeProtocols("SSLv3");
//	    sslContextFactory.setExcludeProtocols("");
//	    sslContextFactory.setIncludeProtocols("SSL", "SSLv2", "SSLv2Hello", "SSLv3");
	    
//	    System.out.println("A = "+   Arrays.toString( sslContextFactory.getExcludeCipherSuites())); 
//	    System.out.println("B = "+   Arrays.toString( sslContextFactory.getIncludeCipherSuites())); 
//	    
//	    System.out.println("getExcludeProtocols = "+ Arrays.toString( sslContextFactory.getExcludeProtocols())); 
//	    System.out.println("getIncludeProtocols = "+   Arrays.toString( sslContextFactory.getIncludeProtocols())); 
	    
		HttpConnectionFactory https1 = new HttpConnectionFactory(configHttps);
	    HTTP2ServerConnectionFactory https2 = new HTTP2ServerConnectionFactory(configHttps);
	        	    
	    ALPNServerConnectionFactory alpn = new ALPNServerConnectionFactory();
	    alpn.setDefaultProtocol(https2.getProtocol()); // sets default protocol to HTTP 2
	    
	    SslConnectionFactory ssl = new SslConnectionFactory(sslContextFactory, alpn.getProtocol());  
	    // alpn.getProtocol();

		ServerConnector httpsConnector = new ServerConnector(server,
				ssl,
				alpn,
				https2,
				https1
				);
		httpsConnector.setName("secured"); // named connector
		httpsConnector.setPort(9998);

		/********************************* HTTP    ****************************************/
		HttpConfiguration configHttp = getHttpConfiguration();
		HttpConnectionFactory http1 = new HttpConnectionFactory(configHttp);
		
		ServerConnector httpconnector = new ServerConnector(server, http1);
		httpconnector.setName("unsecured"); // named connector
		httpconnector.setPort(8080);
		
		/***********************************************************************************/
		
		server.setHandler(myhandlers2);   // les handlers 
		server.setConnectors(new Connector[] { httpconnector, httpsConnector });   // les connectors

		/*******************************************************************/
		
		XHTMLAppScanner.getMapXHTMLPart(XUIFactoryXHtml.changeMgr);
		
	    CodeSource src = XUILaucher.class.getProtectionDomain().getCodeSource();
	    URL classpathEntry = src.getLocation();
		WatchDir.start(classpathEntry.getFile()+"com");
		
		/***************************************************/		
		server.start();
		server.join();
		
	}


	/**
	 * @param basicHandler
	 */
	private static void initXUIFactoryBasic(ContextHandler basicHandler) {
		// basicHandler.setResourceBase(".");
		basicHandler.setHandler(new XUIFactoryBasic());
	}


	/**
	 * @param restHandler
	 */
	private static void initJersey(ServletContextHandler restHandler) {

		ServletHolder jerseyServlet = new ServletHolder(new ServletContainer(resourceConfig()));
		restHandler.addServlet(jerseyServlet, "/*");
		jerseyServlet.setInitOrder(0);
//		jerseyServlet.setInitParameter("jersey.config.server.provider.classnames",	XUIFactoryXHtml.class.getCanonicalName());
//		jerseyServlet.setInitParameter("jersey.config.server.provider.packages", AppConfig.getRestPackage());
		
	}

	private static ResourceConfig resourceConfig() {
	    // manually injecting dependencies (clock) to Jersey resource classes
		ResourceConfig config = new ResourceConfig();
		
		config.register(XUIFactoryXHtml.class);
		config.packages(AppConfig.getRestPackage());
	//	config.forApplication(null);
		
	// config.register(new LoggingFeature(Logger.getLogger(this.class.getName()), Level.INFO, LoggingFeature.Verbosity.PAYLOAD_TEXT, 2048));
		
		final Map<String, Object> properties = new HashMap<String, Object>();
      //  properties.put(ServerProperties.TRACING, "ALL");
		
		config.addProperties(properties);
		
		return config;
	}
	
	
	/**
	 * @param rewrite
	 */
	private static void initRewriting(RewriteHandler rewrite) {
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
	}

	private static HttpConfiguration getHttpConfiguration() {
	    HttpConfiguration config = new HttpConfiguration();
//	    config.setOutputBufferSize(32768);
	    config.setSendXPoweredBy(false);
	    config.setSendServerVersion(false);
	    return config;
	}
	
	
	private static HttpConfiguration getHttpsConfiguration() {
	    HttpConfiguration config = new HttpConfiguration();
	    config.setSecureScheme("https");
	    config.setSecurePort(9998);
//	    config.setOutputBufferSize(32768);
	    config.setSendXPoweredBy(false);
	    config.setSendServerVersion(false);
	    config.addCustomizer(new SecureRequestCustomizer());
	    config.addCustomizer(new ForwardedRequestCustomizer());
	    return config;
	}
	
}
