package com.elisaxui.core.notification;

public class MgrErrorNotificafion {

	private static final ThreadLocal<MgrErrorNotificafion> threadLocalMgrErrorNotificafion = new ThreadLocal<MgrErrorNotificafion>();
	StringBuilder bufError = new StringBuilder();

	public static void doError(CharSequence msg, Throwable ex) {
		MgrErrorNotificafion mgrError = getMgrError();	

		System.out.println(msg);
		if (ex!=null)
			ex.printStackTrace();
		mgrError.bufError.append(msg+"\n");
	}

	private static MgrErrorNotificafion getMgrError() {
		MgrErrorNotificafion mgrError = threadLocalMgrErrorNotificafion.get();
		if (mgrError == null) {
			mgrError = new MgrErrorNotificafion();
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
