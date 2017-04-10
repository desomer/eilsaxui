package com.elisaxui.core.xui.xhtml.js.datadriven;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;

public interface JSDataCtx extends JSClass {

	String idxBegin = null;
	String idxEnd = null;
	String currentIdx = null;
	String currentRow = null;
	
	default Object initCurrent (Object idx, Object row )
	{
		return set(this.currentIdx, idx)
				.set(this.currentRow, row)
				;
		
	}
}
