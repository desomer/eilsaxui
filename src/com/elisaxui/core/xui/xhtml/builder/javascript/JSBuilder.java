package com.elisaxui.core.xui.xhtml.builder.javascript;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;

public class JSBuilder extends Element {

	public void setNameOfProxy(String prefix, Object inst, Object name) {
		aInvocationHandler mh = (aInvocationHandler) Proxy.getInvocationHandler(inst);
		mh.name = prefix + name;
	}

	public JSClassImpl createClass(Class<? extends JSClass> cl, Object proxy) throws IllegalAccessException {
		JSClassImpl ImplClass = XUIFactoryXHtml.getXMLFile().getClassImpl(JSBuilder.this, cl);
		if (!ImplClass.isInitialized()) {


			ImplClass.setInitialized(true);
		}
		return ImplClass;
	}

	abstract class aInvocationHandler implements InvocationHandler {
		public Object proxy;
		public Object name;
		public Class<? extends JSClass> interfac;
		public JSContent content; // pas utiliser => la class qui implemente par
		// defaut l'interface
	}

	public String getId(Method method, Object[] args) {
		StringBuilder buf = new StringBuilder();
		buf.append(method.getName());
		buf.append(".");
		buf.append(args == null ? 0 : args.length);
		return buf.toString();
	}

	public final <E extends JSClass> E getProxy(final Class<? extends JSClass> cl) {

		class MyInvocationHandler extends aInvocationHandler {

			public MyInvocationHandler(Class<? extends JSClass> interfac) {
				this.interfac = interfac;
			}

			@SuppressWarnings("static-access")
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

				if (method.getName().equals("toString")) {
					if (name == null)
						return "???";
					return name.toString();
				}

				String id = getId(method, args);
				JSClassImpl implcl = XUIFactoryXHtml.getXMLFile().getClassImpl(JSBuilder.this, cl);
				boolean isMthInClass = implcl.listDistinctFct.containsKey(id);

				if (!isMthInClass) {
					if (method.isDefault()) {
						content = null; // creation d'un
						implcl.listDistinctFct.put(id, id);
						final Class<?> declaringClass = method.getDeclaringClass();

						Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class
								.getDeclaredConstructor(Class.class, int.class);
						constructor.setAccessible(true);

						Parameter[] param = method.getParameters();
						Object[] p = new Object[param.length];

						if (args != null) {
							for (int i = 0; i < args.length; i++) {
								p[i] = param[i].getName();
							}
						}

						// appel la fct par defaut de l'interface
						Object ret = constructor.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE)
								.unreflectSpecial(method, declaringClass).bindTo(proxy).invokeWithArguments(p);

						if (ret instanceof JSContent) {
							// ajout de la function durant l'appel
							JSFunction fct = createJSFunction().setName(method.getName()).setParam(p)
									.setCode(((JSContent) ret));

							JSClassImpl ImplClass = createClass(cl, proxy);
							ImplClass.addFunction(fct);

						} else
							System.out.println("ret =" + ret);
					} else {
						if (content == null)
							content = createJSContent(); // creer le contenu

						// appel l'implementation si JSInterface
						return method.invoke(content, args);
					}
				}

				return toStringFctCall(method, args);
			}

			private String toStringFctCall(Method method, Object[] args) {
				StringBuilder buf = new StringBuilder();
				buf.append(name);
				buf.append(".");
				buf.append(method.getName());
				buf.append("(");

				int i = 0;
				if (args != null) {
					for (Object p : args) {
						if (i > 0)
							buf.append(", ");
						buf.append(p);
						i++;
					}
				}
				buf.append(")");
				return buf.toString();
			}
		}

		MyInvocationHandler ih = new MyInvocationHandler(cl);

		Object proxy = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { cl }, ih);
		ih.proxy = proxy;
		return (E) proxy;
	}

	public JSBuilder(Object name, Object[] inner) {
		super(name, inner);
	}

	public JSClassImpl createJSClass() {
		return new JSClassImpl(this);
	}

	public JSFunction createJSFunction() {
		return new JSFunction(this);
	}

	public JSContent createJSContent() {
		return new JSContent(this);
	}

	public static final class JSNewLine {
	};

	public static final class JSElem {
		public Object value;
	}
}