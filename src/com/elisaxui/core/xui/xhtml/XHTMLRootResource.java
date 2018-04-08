package com.elisaxui.core.xui.xhtml;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.target.AFTER_BODY;
import com.elisaxui.core.xui.xhtml.target.BODY;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;


public class XHTMLRootResource extends XHTMLPart {

	
	@xTarget(CONTENT.class)
	@xResource
	public XMLElement xGetContent() {

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		LocalDateTime dateBuild = XUIFactoryXHtml.changeMgr.dateBuild;
		
		String formatDateTimeBuild =   dateBuild.format(formatter);
		String formatDateTimeNow = now.format(formatter);

		
		List<XMLElement> body = getListElementFromTarget(BODY.class);
		List<XMLElement> afterBody = getListElementFromTarget(AFTER_BODY.class);
		List<XMLElement> header = getListElementFromTarget(HEADER.class);
		
		Collections.sort(header, new XHTMLRoot.XMLElementComparator() );
		Collections.sort(body, new XHTMLRoot.XMLElementComparator() );
		Collections.sort(afterBody, new XHTMLRoot.XMLElementComparator() );
		
		return xListNode(header, body, afterBody );
	}

	// + "<head>\n" + getPart(null, null, 0, HtmlPart.HEADER) +
	// // " <title>Default Page Title</title>\n"+
	// // " <link rel=\"shortcut icon\" href=\"favicon.ico\">\n"+
	// // " <link rel=\"icon\" href=\"favicon.ico\">\n"+
	// // " <link rel=\"stylesheet\" type=\"text/css\"
	// // href=\"styles.css\">\n"+
	// // " <script type=\"text/javascript\"
	// //
	// src=\"//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>\n"+
	// // "<!--[if lt IE 9]>\n"+
	// // " <script
	// //
	// src=\"http://html5shiv.googlecode.com/svn/trunk/html5.js\"></script>\n"+

}
