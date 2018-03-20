package com.elisaxui.core.xui;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.ws.rs.core.HttpHeaders;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.elisaxui.core.helper.URLLoader;
import com.elisaxui.core.helper.log.CoreLogger;
import com.elisaxui.core.xui.xhtml.builder.javascript.utility.JSMinifier;

public class XUIFactoryBasic extends AbstractHandler {

	static HashMap<String, String> cache = new HashMap<String, String>();
	
	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		
		String url = request.getParameter("url");
		
		String content = cache.get(url);
		
		if (content==null) {
			
			if (url==null)
			{
				CoreLogger.getLogger(1).log(Level.SEVERE, () -> "pb param url " + request);
				baseRequest.setHandled(true);
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			
			StringBuilder js = URLLoader.loadTextFromUrl(url);
			
			if (js.length()>0)
			{
				content = JSMinifier.doMinifyJS(js.toString()).toString();	
				cache.put(url, content);
			}
		}
		
		if (content==null) {
			baseRequest.setHandled(true);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else
		{
			response.setHeader(HttpHeaders.CACHE_CONTROL, "max-age=315360000, public");
			response.setHeader(HttpHeaders.CONTENT_ENCODING,"gzip");
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/javascript;charset=utf-8");   
			
	        GZIPOutputStream gzip = new GZIPOutputStream(response.getOutputStream());
	        gzip.write(content.getBytes("UTF-8"));
	        gzip.flush();
	        gzip.close();
			
			baseRequest.setHandled(true);
			response.setStatus(HttpServletResponse.SC_OK);
		}
	}


//	public static class GZIPHttpServletResponseWrapper extends HttpServletResponseWrapper {
//
//	    private ServletResponseGZIPOutputStream gzipStream;
//	    private ServletOutputStream outputStream;
//	    private PrintWriter printWriter;
//
//	    public GZIPHttpServletResponseWrapper(HttpServletResponse response) throws IOException {
//	        super(response);
//	        response.addHeader(HttpHeaders.CONTENT_ENCODING, "gzip");
//	    }
//
//	    public void finish() throws IOException {
//	        if (printWriter != null) {
//	            printWriter.close();
//	        }
//	        if (outputStream != null) {
//	            outputStream.close();
//	        }
//	        if (gzipStream != null) {
//	            gzipStream.close();
//	        }
//	    }
//
//	    @Override
//	    public void flushBuffer() throws IOException {
//	        if (printWriter != null) {
//	            printWriter.flush();
//	        }
//	        if (outputStream != null) {
//	            outputStream.flush();
//	        }
//	        super.flushBuffer();
//	    }
//
//	    @Override
//	    public ServletOutputStream getOutputStream() throws IOException {
//	        if (printWriter != null) {
//	            throw new IllegalStateException("printWriter already defined");
//	        }
//	        if (outputStream == null) {
//	            initGzip();
//	            outputStream = gzipStream;
//	        }
//	        return outputStream;
//	    }
//
//	    @Override
//	    public PrintWriter getWriter() throws IOException {
//	        if (outputStream != null) {
//	            throw new IllegalStateException("printWriter already defined");
//	        }
//	        if (printWriter == null) {
//	            initGzip();
//	            printWriter = new PrintWriter(new OutputStreamWriter(gzipStream, getResponse().getCharacterEncoding()));
//	        }
//	        return printWriter;
//	    }
//
//	    @Override
//	    public void setContentLength(int len) {
//	    }
//
//	    private void initGzip() throws IOException {
//	        gzipStream = new ServletResponseGZIPOutputStream(getResponse().getOutputStream());
//	    }
//
//	}
//
//	public static class ServletResponseGZIPOutputStream extends ServletOutputStream {
//
//	    GZIPOutputStream gzipStream;
//	    final AtomicBoolean open = new AtomicBoolean(true);
//	    OutputStream output;
//
//	    public ServletResponseGZIPOutputStream(OutputStream output) throws IOException {
//	        this.output = output;
//	        gzipStream = new GZIPOutputStream(output);
//	    }
//
//	    @Override
//	    public void close() throws IOException {
//	        if (open.compareAndSet(true, false)) {
//	            gzipStream.close();
//	        }
//	    }
//
//	    @Override
//	    public void flush() throws IOException {
//	        gzipStream.flush();
//	    }
//
//	    @Override
//	    public void write(byte[] b) throws IOException {
//	        write(b, 0, b.length);
//	    }
//
//	    @Override
//	    public void write(byte[] b, int off, int len) throws IOException {
//	        if (!open.get()) {
//	            throw new IOException("Stream closed!");
//	        }
//	        gzipStream.write(b, off, len);
//	    }
//
//	    @Override
//	    public void write(int b) throws IOException {
//	        if (!open.get()) {
//	            throw new IOException("Stream closed!");
//	        }
//	        gzipStream.write(b);
//	    }
//
//		/* (non-Javadoc)
//		 * @see javax.servlet.ServletOutputStream#isReady()
//		 */
//		@Override
//		public boolean isReady() {
//			// TODO Auto-generated method stub
//			return true;
//		}
//
//		/* (non-Javadoc)
//		 * @see javax.servlet.ServletOutputStream#setWriteListener(javax.servlet.WriteListener)
//		 */
//		@Override
//		public void setWriteListener(WriteListener arg0) {
//			// TODO Auto-generated method stub
//		
//		}
//
//	}
//	
}
