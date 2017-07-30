package com.elisaxui.core.xui.xhtml.js.datadriven;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.js.value.JSInt;
import com.elisaxui.core.xui.xhtml.js.value.JSon;

public interface JSDataCtx extends JSClass {

	JSInt idxBegin = null;
	JSInt idxEnd = null;
	JSInt currentIdx = null;
	JSon currentRow = null;
	
	default Object initCurrent (Object idx, Object row )
	{
		return set(currentIdx, idx)
				.set(currentRow, row)
				;
		
	}
}
