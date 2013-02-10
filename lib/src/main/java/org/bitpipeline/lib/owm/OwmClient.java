/**
 * Copyright 2013 J. Miguel P. Tavares
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.bitpipeline.lib.owm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/** Implements a synchronous HTTP client to the Open Weather Map service described
 * in http://openweathermap.org/wiki/API/JSON_API
 * @author mtavares */
public class OwmClient {
	private String baseOwmUrl = "http://api.openweathermap.org/data/2.1/";
	private int retriesPerRequest = 3;

	private HttpClient httpClient;
	private LogInterface log;

	public OwmClient () {
		this.httpClient = new HttpClient ();
		this.log = new SysErrLog ();
	}

	public OwmClient (HttpClient httpClient, LogInterface log) {
		if (httpClient == null)
			throw new IllegalArgumentException ("Can construct a OwmClient with a null HttpClient");
		if (log == null)
			throw new IllegalArgumentException ("Can construct a OwmClient with a null LogInterface");
		this.httpClient = httpClient;
		this.log = log;
	}

	/** Find current weather around a geographic point
	 * @param lat is the latitude of the geographic point of interest (North/South coordinate)
	 * @param lon is the longitude of the geographic point of interest (East/West coordinate)
	 * @param cnt is the requested number of weather stations to retrieve (the
	 * 	actual answer might be less than the requested).
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	public List<WeatherData> currentWeatherAroundPoint (float lat, float lon, int cnt) throws IOException, JSONException { //, boolean cluster, OwmClient.Lang lang) {
		String subUrl = String.format ("find/station?lat=%f&lon=%f&cnt=%d&cluster=yes",
				Float.valueOf (lat), Float.valueOf (lon), Integer.valueOf (cnt));
		JSONObject response = doQuery (subUrl);
		return weatherDataListFromJSon (response);
	}

	/** Find current weather around a city coordinated
	 * @param lat is the latitude of the geographic point of interest (North/South coordinate)
	 * @param lon is the longitude of the geographic point of interest (East/West coordinate)
	 * @param cnt is the requested number of weather stations to retrieve (the
	 * 	actual answer might be less than the requested).
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	public List<WeatherData> currentWeatherAtCity (float lat, float lon, int cnt) throws IOException, JSONException { //, boolean cluster, OwmClient.Lang lang) {
		String subUrl = String.format ("find/city?lat=%f&lon=%f&cnt=%d&cluster=yes",
				Float.valueOf (lat), Float.valueOf (lon), Integer.valueOf (cnt));
		JSONObject response = doQuery (subUrl);
		return weatherDataListFromJSon (response);
	}

	/** Find current weather within a bounding box
	 * @param northLat is the latitude of the geographic top left point of the bounding box
	 * @param westLon is the longitude of the geographic top left point of the bounding box
	 * @param southLat is the latitude of the geographic bottom right point of the bounding box
	 * @param eastLon is the longitude of the geographic bottom right point of the bounding box
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	public List<WeatherData> currentWeatherInBoundingBox (float northLat, float westLon, float southLat, float eastLon) throws IOException, JSONException { //, boolean cluster, OwmClient.Lang lang) {
		String subUrl = String.format ("find/station?bbox=%f,%f,%f,%f&cluster=yes",
				Float.valueOf (northLat), Float.valueOf (westLon),
				Float.valueOf (southLat), Float.valueOf (eastLon));
		JSONObject response = doQuery (subUrl);
		return weatherDataListFromJSon (response);
	}

	/** Find current city weather within a bounding box
	 * @param northLat is the latitude of the geographic top left point of the bounding box
	 * @param westLon is the longitude of the geographic top left point of the bounding box
	 * @param southLat is the latitude of the geographic bottom right point of the bounding box
	 * @param eastLon is the longitude of the geographic bottom right point of the bounding box
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	public List<WeatherData> currentWeatherAtCityBoundingBox (float northLat, float westLon, float southLat, float eastLon) throws IOException, JSONException { //, boolean cluster, OwmClient.Lang lang) {
		String subUrl = String.format ("find/city?bbox=%f,%f,%f,%f&cluster=yes",
				Float.valueOf (northLat), Float.valueOf (westLon),
				Float.valueOf (southLat), Float.valueOf (eastLon));
		JSONObject response = doQuery (subUrl);
		return weatherDataListFromJSon (response);
	}

	/** Find current weather within a circle
	 * @param lat is the latitude of the geographic center of the circle (North/South coordinate)
	 * @param lon is the longitude of the geographic center of the circle (East/West coordinate)
	 * @param radius is the radius of the circle (in kilometres)
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	public List<WeatherData> currentWeatherInCircle (float lat, float lon, float radius) throws IOException, JSONException { //, boolean cluster, OwmClient.Lang lang) {
		String subUrl = String.format ("find/station?lat=%f&lon=%f&radius=%f&cluster=yes",
				Float.valueOf (lat), Float.valueOf (lon), Float.valueOf (radius));
		JSONObject response = doQuery (subUrl);
		return weatherDataListFromJSon (response);
	}

	/** Find current city weather within a circle
	 * @param lat is the latitude of the geographic center of the circle (North/South coordinate)
	 * @param lon is the longitude of the geographic center of the circle (East/West coordinate)
	 * @param radius is the radius of the circle (in kilometres)
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	public List<WeatherData> currentWeatherAtCityCircle (float lat, float lon, float radius) throws IOException, JSONException {
		String subUrl = String.format ("find/city?lat=%f&lon=%f&radius=%f&cluster=yes",
				Float.valueOf (lat), Float.valueOf (lon), Float.valueOf (radius));
		JSONObject response = doQuery (subUrl);
		return weatherDataListFromJSon (response);
	}

	/** Find current city weather
	 * @param cityId is the ID of the city
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	public WeatherData currentWeatherAtCity (int cityId) throws IOException, JSONException {
		String subUrl = String.format ("weather/city/%d?type=json", Integer.valueOf (cityId));
		JSONObject response = doQuery (subUrl);
		return new WeatherData (response);
	}

	/** Find current city weather
	 * @param cityName is the name of the city
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	public List<WeatherData> currentWeatherAtCity (String cityName) throws IOException, JSONException {
		String subUrl = String.format ("find/name?q=%s", cityName);
		JSONObject response = doQuery (subUrl);
		return weatherDataListFromJSon (response);
	}

	/** Find current city weather
	 * @param cityName is the name of the city
	 * @param countryCode is the two letter country code
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	public List<WeatherData> currentWeatherAtCity (String cityName, String countryCode) throws IOException, JSONException {
		String subUrl = String.format ("find/name?q=%s,%s", cityName, countryCode.toUpperCase ());
		JSONObject response = doQuery (subUrl);
		return weatherDataListFromJSon (response);
	}

	/** Get the weather forecast for a city
	 * @param cityId is the ID of the city
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	public List<ForecastWeatherData> forecastWeatherAtCity (int cityId) throws JSONException, IOException {
		String subUrl = String.format ("forecast/city/%d", cityId);
		JSONObject response = doQuery (subUrl);
		return forecastWeatherListFromJSON (response);
	}

	private List<WeatherData> weatherDataListFromJSon (JSONObject json) throws JSONException {
		JSONArray stationList = json.getJSONArray ("list");
		List<WeatherData> weatherDataList = new ArrayList<WeatherData> (stationList.length ()); 
		for (int i = 0; i < stationList.length (); i++) {
			try {
				weatherDataList.add (
						new WeatherData (stationList.getJSONObject (i)));
			} catch (JSONException jsonE) {
				this.log.w (jsonE, "Error when parsing a weather data");
				this.log.d (stationList.getJSONObject (i).toString (4));
			}
		}
		return weatherDataList;
	}

	private List<ForecastWeatherData> forecastWeatherListFromJSON (JSONObject json) throws JSONException {
		JSONArray owmForecasts = json.getJSONArray ("list");
		List<ForecastWeatherData> forecastList = new ArrayList<ForecastWeatherData> (owmForecasts.length ()); 
		for (int i = 0; i < owmForecasts.length (); i++) {
			try {
				forecastList.add (
						new ForecastWeatherData (owmForecasts.getJSONObject (i)));
			} catch (JSONException jsonE) {
				this.log.w (jsonE, "Error when parsing a weather forecast");
				this.log.d (owmForecasts.getJSONObject (i).toString (4));
			}
		}
		return forecastList;
	}

	private JSONObject doQuery (String subUrl) throws JSONException, IOException {
		String responseBody = null;
		GetMethod httpget = new GetMethod (this.baseOwmUrl + subUrl);
		this.log.d ("Query: %s", httpget.getURI ().toString ());
		httpget.getParams ().setParameter (HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler (this.retriesPerRequest, false));

		try {
			int statusCode = this.httpClient.executeMethod (httpget);
			if (statusCode != HttpStatus.SC_OK) {
				throw new IOException (
						String.format ("OWM server responded with status code %d: %s", statusCode, httpget.getStatusLine ()));
			}
			responseBody = httpget.getResponseBodyAsString (1024*48);
		} catch (IOException e) {
			throw e;
		} finally {
			httpget.releaseConnection ();
		}
		return new JSONObject (responseBody);
	}
}
