/**
 * Copyright 2012 
 *         J. Miguel P. Tavares <mtavares@bitpipeline.eu>
 *         BitPipeline
 */
package org.bitpipeline.lib.owm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/** Implements a synchronous HTTP client to the Open Weather Map service described
 * in http://openweathermap.org/wiki/API/JSON_API
 * @author mtavares */
public class OwmClient {
	static enum Lang {
		RU, EN, DE, FR, ES, IT
	}

	private String baseOwmUrl = "http://api.openweathermap.org/data/2.1/";
	private int retriesPerRequest = 3;

	private HttpClient httpClient;

	public OwmClient () {
		this.httpClient = new HttpClient ();
	}

	/** Find current weather around a point 
	 * @throws JSONException 
	 * @throws IOException */
	public List<WeatherData> currentWeatherAroundPoint (float lat, float lon, int cnt) throws IOException, JSONException { //, boolean cluster, OwmClient.Lang lang) {
		String subUrl = String.format ("find/station?lat=%f&lon=%f&cnt=%d",
				Float.valueOf (lat), Float.valueOf (lon), Integer.valueOf (cnt));
		JSONObject response = doQuery (subUrl);
		return weatherDataListFromJSon (response);
	}

	private List<WeatherData> weatherDataListFromJSon (JSONObject json) throws JSONException {
		JSONArray stationList = json.getJSONArray ("list");
		List<WeatherData> weatherDataList = new ArrayList<WeatherData> (stationList.length ()); 
		for (int i = 0; i < stationList.length (); i++) {
			try {
				weatherDataList.add (
						new WeatherData (stationList.getJSONObject (i)));
			} catch (JSONException jsonE) {
				// ignore this one.
			}
		}
		return weatherDataList;
	}

	private JSONObject doQuery (String subUrl) throws JSONException, IOException {
		String responseBody = null;
		GetMethod httpget = new GetMethod (this.baseOwmUrl + subUrl);
		httpget.getParams ().setParameter (HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler (this.retriesPerRequest, false));

		try {
			this.httpClient.executeMethod (httpget);
			responseBody = httpget.getResponseBodyAsString ();
		} catch (IOException e) {
			throw e;
		} finally {
			httpget.releaseConnection ();
		}
		return new JSONObject (responseBody);
	}
}
