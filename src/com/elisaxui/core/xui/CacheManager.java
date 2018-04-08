/**
 * 
 */
package com.elisaxui.core.xui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.mapdb.Atomic.Var;

import com.elisaxui.core.helper.log.CoreLogger;

/**
 * @author gauth
 *
 */
public class CacheManager {
	private static boolean enableCache = true;
	
	private static Map<String, ArrayList<String>> listeVersionId = null; 
	private static HashMap<String, String> resourceCacheMem = new HashMap<>() ;

	/*************************************************************************/
	private static DB db = null;
	public static HTreeMap<String, String> resourceDB = null;
	private static  Var<Object> listeDico= null;
	private static  Var<Object> lastDateFile= null;
	/**************************************************************************/

	public static final long getLastDate() {
		Long r = (Long) lastDateFile.get();
		if (r==null)
		{
			lastDateFile.set(System.currentTimeMillis());
			r = (Long) lastDateFile.get();
		}
		return r;
	}
	
	/**
	 * @param lastDate the lastDate to set
	 */
	public static final void setLastDate(long lastDate) {
		lastDateFile.set(lastDate);
		db.commit();
	}
	
	static {	
		initDB();
	}

	@SuppressWarnings("unchecked")
	private static void initDB() {
		String home = System.getProperty("user.home");
		String pathdb = home+ File.separator+"fileMapdb.db";
		
		CoreLogger.getLogger(1).info(()->"start db at "+pathdb);
		
		db = DBMaker.fileDB(pathdb).checksumHeaderBypass().closeOnJvmShutdown().make();	
		resourceDB = db.hashMap("mapFileHtml",Serializer.STRING,Serializer.STRING).createOrOpen();
		listeDico = db.atomicVar("pageDico").createOrOpen();
		lastDateFile = db.atomicVar("lastDate").createOrOpen();
		
		listeVersionId =  (Map<String, ArrayList<String>>) listeDico.get();
		if (listeVersionId==null)
			listeVersionId = new HashMap<>();
	}
	/************************************************/
	String id = null;
	String cachetype = null;   // es5
	
	/**
	 * @param id
	 * @param cachetype
	 */
	public CacheManager(String id, String cachetype) {
		super();
		this.id = id;
		this.cachetype = cachetype;
	}

	/**********************************************/
	private String idCache = null;   // id + date fichier+ type
	private String idCacheDicoVersionning = null;  // id + type
	String idCacheDB; 
	String result = null;
	/************************************************/
	
	public void getVersion(boolean noCache, int version)
	{

		if (enableCache)
		{
			idCacheDicoVersionning = id+cachetype;
			idCache = id+XUIFactoryXHtml.changeMgr.lastOlderFile+cachetype;
			
			if (!noCache)
			{
				result = getVersionDB(version);
			}	
		}
	}
	
	public String getVersionDB(int version)
	{
		if (idCache==null)
			return null;
		
		int verDB = version;
		String htmlInCacheOriginal = null;
		if (version==0)
		{
			if (result==null)
				// recherche en base
				result = resourceCacheMem.computeIfAbsent(idCache, k-> resourceDB.get(k));
			
			htmlInCacheOriginal = result;
			verDB = -1;  // compare par rapport au dernier
		}
		else
			htmlInCacheOriginal= resourceDB.get(idCache);
		
		idCacheDB = idCache;
		ArrayList<String> version1 = listeVersionId.get(idCacheDicoVersionning);
		if (version1!=null &&  -verDB<version1.size())
			idCacheDB = version1.get(version1.size()+verDB-1);
		
		String htmlInCacheDB = resourceDB.get(idCacheDB);
		
		// initialize 
		if (htmlInCacheDB!=null && htmlInCacheOriginal!=null) {
			XUIFactoryXHtml.changeMgr.initLineDiff(htmlInCacheDB ,  htmlInCacheOriginal);
		}
		
		if (version==0)
		{
			idCacheDB = idCache;
			return result;
		}
		else
		{
			return htmlInCacheDB; 	
		}
	}
	
	public void storeResultInDb() {
		if (idCache==null)
			return;

		CoreLogger.getLogger(1).info(()->" add cache "+idCache);
		
		ArrayList<String> version1 = listeVersionId.get(idCacheDicoVersionning);
		if (version1==null)
		{
			version1=new ArrayList<>();
			listeVersionId.put(idCacheDicoVersionning, version1);
		}
		version1.add(idCache);
		
		resourceDB.put(idCache, result);
		listeDico.set(listeVersionId);
		db.commit();
		
		resourceCacheMem.put(idCache, result);
	}
}
