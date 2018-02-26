/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.json;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.json.JsonNumber;
import javax.json.JsonValue;

public class JsonNumberImpl implements JsonNumber {

	 String value;
	 
	 /**
	 * @param value
	 */
	public JsonNumberImpl(String value) {
		super();
		this.value = value;
	}


	@Override
	 public String toString() {
		 return value;
	 }


	@Override
	public ValueType getValueType() {
		return JsonValue.ValueType.NUMBER;
	}


	@Override
	public boolean isIntegral() {
		return false;
	}


	@Override
	public int intValue() {
		return 0;
	}


	@Override
	public int intValueExact() {
		return 0;
	}


	@Override
	public long longValue() {
		return 0;
	}


	@Override
	public long longValueExact() {
		return 0;
	}


	@Override
	public BigInteger bigIntegerValue() {
		return null;
	}


	@Override
	public BigInteger bigIntegerValueExact() {
		return null;
	}


	@Override
	public double doubleValue() {
		return 0;
	}


	@Override
	public BigDecimal bigDecimalValue() {
		return null;
	}

}