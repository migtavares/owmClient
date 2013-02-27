package org.bitpipeline.lib.owm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

abstract class AbstractOwmResponse {
	static private final String JSON_COD = "cod";
	static private final String JSON_MESSAGE = "message";
	static protected final String JSON_CALCTIME = "calctime";
	static private final String JSON_CALCTIME_TOTAL = "total";
	static protected final String JSON_LIST = "list";


	private final int code;
	private final String message;
	private final float calctime;

	public AbstractOwmResponse (JSONObject json) {
		this.code = json.optInt (AbstractOwmResponse.JSON_COD, Integer.MIN_VALUE);
		this.message = json.optString (AbstractOwmResponse.JSON_MESSAGE);
		String calcTimeStr = json.optString (AbstractOwmResponse.JSON_CALCTIME);
		float calcTimeTotal = Float.NaN;
		if (calcTimeStr.length () > 0) {
			try {
				calcTimeTotal = Float.valueOf (calcTimeStr); 
			} catch (NumberFormatException nfe) { // So.. it's not a number.. let's see if we can still find it's value.
				String totalCalcTimeStr = AbstractOwmResponse.getValueStrFromCalcTimePart (calcTimeStr, AbstractOwmResponse.JSON_CALCTIME_TOTAL);
				if (totalCalcTimeStr != null) {
					try {
						calcTimeTotal = Float.valueOf (totalCalcTimeStr);
					} catch (NumberFormatException nfe2) {
						calcTimeTotal = Float.NaN;
					}
				}
			}
		}
		this.calctime = calcTimeTotal;
	}

	public boolean hasCode () {
		return this.code != Integer.MIN_VALUE;
	}
	public int getCode () {
		return this.code;
	}

	public boolean hasMessage () {
		return this.message != null && this.message.length () > 0;
	}
	public String getMessage () {
		return this.message;
	}

	public boolean hasCalcTime () {
		return !Double.isNaN (this.calctime);
	}
	public double getCalcTime () {
		return this.calctime;
	}

	static String getValueStrFromCalcTimePart (final String calcTimeStr, final String part) {
		Pattern keyValuePattern = Pattern.compile (part + "\\s*=\\s*([\\d\\.]*)");
		Matcher matcher = keyValuePattern.matcher (calcTimeStr);
		if (matcher.find () && matcher.groupCount () == 1) {
			return matcher.group (1);
		}
		return null;
	}

	static float getValueFromCalcTimeStr (final String calcTimeStr, final String part) {
		if (calcTimeStr == null || calcTimeStr.length () == 0)
			return Float.NaN;
		float value = Float.NaN;
		String valueStr = AbstractOwmResponse.getValueStrFromCalcTimePart (calcTimeStr, part);
		if (valueStr != null) {
			try {
				value = Float.valueOf (valueStr);
			} catch (NumberFormatException nfe) {
				value = Float.NaN;
			}
		}
		return value;
	}


}
