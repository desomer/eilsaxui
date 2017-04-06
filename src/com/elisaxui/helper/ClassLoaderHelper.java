package com.elisaxui.helper;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;

public class ClassLoaderHelper {
	
	public static Iterable<FileEntry> listFilesRelativeToClass(Class<?> clazz, String subdirectory) throws IOException {
	    CodeSource src = clazz.getProtectionDomain().getCodeSource();
	    if (src == null) {
	        return null;
	    }
	    URL classpathEntry = src.getLocation();
	    ArrayList<FileEntry> list = new ArrayList<FileEntry>();
	    getSubDirectory(list, subdirectory, classpathEntry);
	    return list;
	}

	private static void getSubDirectory(ArrayList<FileEntry> list, String subdirectory, URL classpathEntry) throws IOException {
		

		try {
	        // Check if we're loaded from a folder
	        File file = new File(new File(classpathEntry.toURI()), subdirectory);
	        if (file.isDirectory()) {
	        	fileEntriesFor(list, file.listFiles(), subdirectory , classpathEntry);
//	        	for (FileEntry fileEntry : ret) {
//	        		if (fileEntry.name.startsWith("/")) {
//	        			 getSubDirectory(list, subdirectory+fileEntry.name,classpathEntry);
//				}
//	        }
	       }
	    } catch (URISyntaxException e) {
	        // Should never happen, because we know classpathentry is valid
	        throw new RuntimeException(e);
	    }

//	    // We're not in a folder, so we must be in a jar or similar
//	    subdirectory = subdirectory.replace(File.separatorChar, '/');
//	    if (!subdirectory.endsWith("/")) {
//	        subdirectory = subdirectory + "/";
//	    }

//	    ZipInputStream jarStream = new ZipInputStream(classpathEntry.openStream());
//	    ZipEntry zipEntry;
//	    while ((zipEntry = jarStream.getNextEntry()) != null) {
//	        if (isChild(subdirectory, zipEntry.getName())) {
//	            String basename = zipEntry.getName().substring(subdirectory.length());
//	            int indexOfSlash = basename.indexOf('/');
//	            if (indexOfSlash < 0 || indexOfSlash == basename.length() - 1) {
//	                list.add(new FileEntry(basename));
//	            }
//	        }
//	    }
	   // return list;
	}

//	private static boolean isChild(String parent, String name) {
//	    return name.startsWith(parent);
//	}

	public static ArrayList<FileEntry> fileEntriesFor(ArrayList<FileEntry> list, File[] files, String subdirectory, URL classpathEntry) throws IOException {
	    for (File file : files) {
	        String filename = file.getName();
	        if (file.isDirectory()) {
	            filename = "/"+filename;
	            getSubDirectory(list, subdirectory+filename, classpathEntry);
	        }
	        list.add(new FileEntry(file));
	    }
	    return list;
	}
	
	public static class FileEntry {
		 public final File file;

		    public FileEntry(File file) {
		        this.file = file;
		    }
		}
}
