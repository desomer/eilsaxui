package com.elisaxui.core.xui;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

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

		HandlerCollection myhandlers = new HandlerCollection(true);

		ServletContextHandler restHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		restHandler.setContextPath("/rest");

		ServletHolder jerseyServlet = restHandler.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
		jerseyServlet.setInitOrder(0);
		jerseyServlet.setInitParameter("jersey.config.server.provider.classnames",
				XUIFactoryXHtml.class.getCanonicalName());
		myhandlers.addHandler(restHandler);

		ContextHandler basicHandler = new ContextHandler();
		basicHandler.setContextPath("/basic");
		// basicHandler.setResourceBase(".");
		basicHandler.setHandler(new XUIFactoryBasic());
		// basicHandler.setClassLoader(Thread.currentThread().getContextClassLoader());
		myhandlers.addHandler(basicHandler);

		server.setHandler(myhandlers);

		server.start();
		server.join();
	}

}
