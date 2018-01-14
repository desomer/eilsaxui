package com.elisaxui.component.toolkit.datadriven;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;

public interface JSDataCtx extends JSClass {

	JSInt idxBegin = null;
	JSInt idxEnd = null;
	JSInt currentIdx = null;
	JSon currentRow = null;
	
	default Object initCurrent (Object idx, Object row )
	{
		return _set(currentIdx, idx)
				._set(currentRow, row)
				;
		
	}
}
