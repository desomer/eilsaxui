package com.elisaxui.core.xui;


import java.util.ArrayList;
import java.util.List;

public class XUIHtmlBuilder implements IHtmlBuilder {

	StringBuilder buf; 
	public List<Tag> listTag = new ArrayList<>();
	
	
	public XUIHtmlBuilder(StringBuilder buf) {
		super();
		this.buf = buf;
	}

	
	public Tag getTag()
	{
		Tag t = new Tag();
		return t;
	}

	@Override
	public XUIHtmlBuilder toHtml(XUIHtmlBuilder buf) {
		this.buf = buf.buf;
		
		for (Tag tag : listTag) {
			tag.toHtml(buf);	
		}
		
		return buf;
	}
	

	private void append(Object v)
	{
		buf.append(v);
	}
	
	public static class Tag implements IHtmlBuilder
	{
		public Object name;
		public List<Attr> listAttr = new ArrayList<>();
		public List<Tag> listTag = new ArrayList<>();

		@Override
		public XUIHtmlBuilder toHtml(XUIHtmlBuilder buf) {
			buf.append("<"+ name);
			
			for (Attr attr : listAttr) {
				attr.toHtml(buf);	
			}
			buf.append(">");
			
			for (Tag tag : listTag) {
				tag.toHtml(buf);	
			}
			
			buf.append("</"+ name+">");
			return buf;
		}
		
	}
	
	
	public static class Attr implements IHtmlBuilder
	{
		Object name;
		Object value;
		
		@Override
		public XUIHtmlBuilder toHtml(XUIHtmlBuilder buf) {
			buf.append(name);
			buf.append("=");
			buf.append(value);
			return buf;
		}
		
		
	}

}
