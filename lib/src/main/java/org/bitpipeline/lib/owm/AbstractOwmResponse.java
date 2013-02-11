package org.bitpipeline.lib.owm;

import org.json.JSONObject;

abstract class AbstractOwmResponse {
	static private final String JSON_COD = "cod";
	static private final String JSON_MESSAGE = "message";
	static private final String JSON_CALCTIME = "calctime";

	private final int code;
	private final String message;
	private final double calctime;

	public AbstractOwmResponse (JSONObject json) {
		this.code = json.optInt (AbstractOwmResponse.JSON_COD, Integer.MIN_VALUE);
		this.message = json.optString (AbstractOwmResponse.JSON_MESSAGE);
		this.calctime = json.optDouble (AbstractOwmResponse.JSON_CALCTIME, Double.NaN);
	}

	public boolean hasCode () {
		return this.code != Integer.MIN_VALUE;
	}
	public int getCode () {
		return this.code;
	}

	public boolean hasMessage () {
		return this.message != null && !this.message.isEmpty ();
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
}
