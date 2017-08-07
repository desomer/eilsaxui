package com.elisaxui.core.xui;

import org.eclipse.jetty.rewrite.handler.RedirectPatternRule;
import org.eclipse.jetty.rewrite.handler.RewriteHandler;
import org.eclipse.jetty.rewrite.handler.RewritePatternRule;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

//import com.github.leonardoxh.gzipfilter.GZIPFilter;

public class XUILaucher {

	// public interface A {
	// String amplify(Number a);
	// Number getA();
	// String toString();
	//
	// default String zzz() {
	// System.out.println("Sleeping: zzzz");
	// return "42";
	// };
	// }

	public static void main(String[] args) throws Exception {

		// A a = (A) Proxy.newProxyInstance(XUILaucher.class.getClassLoader(),
		// new Class[] { A.class },
		// (proxy, method, argsm) -> {
		//
		// if(method.isDefault()) {
		// final Class<?> declaringClass = method.getDeclaringClass();
		// Constructor<MethodHandles.Lookup> constructor =
		// MethodHandles.Lookup.class.getDeclaredConstructor(Class.class,
		// int.class);
		// constructor.setAccessible(true);
		// return
		// constructor.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE)
		// .unreflectSpecial(method, declaringClass)
		// .bindTo(proxy)
		// .invokeWithArguments(args);
		// }
		//
		// System.out.println("Proxying: " + method.getName() + " " +
		// Arrays.toString(argsm));
		// return "Success";
		// }
		// );
		//
		// a.amplify(0);
		// a.zzz();

		Server server = new Server(8080);

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

		server.start();
		server.join();
	}

}
