package com.elisaxui.core.notification;

import java.util.logging.Level;

import com.elisaxui.core.helper.log.CoreLogger;

public class ErrorNotificafionMgr {

	private static final ThreadLocal<ErrorNotificafionMgr> threadLocalMgrErrorNotificafion = new ThreadLocal<ErrorNotificafionMgr>();
	StringBuilder bufError = new StringBuilder();

	public static void doError(CharSequence msg, Throwable ex) {
		ErrorNotificafionMgr mgrError = getMgrError();	

		CoreLogger.getLogger(2).log(Level.SEVERE, msg.toString(),  ex);

		mgrError.bufError.append(msg+"\n");
	}

	private static ErrorNotificafionMgr getMgrError() {
		ErrorNotificafionMgr mgrError = threadLocalMgrErrorNotificafion.get();
		if (mgrError == null) {
			mgrError = new ErrorNotificafionMgr();
			threadLocalMgrErrorNotificafion.set(mgrError);
		}
		return mgrError;
	}
	
	public static StringBuilder getBufferErrorMessage()
	{
		return getMgrError().bufError;
	}

	
	public static boolean hasErrorMessage()
	{
		return getMgrError().bufError.length()>0;
	}
}
