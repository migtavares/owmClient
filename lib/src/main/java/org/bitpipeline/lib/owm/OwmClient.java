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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

/** Implements a synchronous HTTP client to the Open Weather Map service described
 * in http://openweathermap.org/wiki/API/JSON_API
 * @author mtavares */
public class OwmClient {
	static private final String APPID_HEADER = "x-api-key";

	static public enum HistoryType {
		UNKNOWN,
		TICK, HOUR, DAY 
	}

	private String baseOwmUrl = "http://api.openweathermap.org/data/2.1/";
	private String owmAPPID = null;

	private HttpClient httpClient;

	public OwmClient () {
		this.httpClient = new DefaultHttpClient ();
	}

	public OwmClient (HttpClient httpClient) {
		if (httpClient == null)
			throw new IllegalArgumentException ("Can't construct a OwmClient with a null HttpClient");
		this.httpClient = httpClient;
	}

	/** */
	public void setAPPID (String appid) {
		this.owmAPPID = appid;
	}

	/** Find current weather around a geographic point
	 * @param lat is the latitude of the geographic point of interest (North/South coordinate)
	 * @param lon is the longitude of the geographic point of interest (East/West coordinate)
	 * @param cnt is the requested number of weather stations to retrieve (the
	 * 	actual answer might be less than the requested).
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	public WeatherStatusResponse currentWeatherAroundPoint (float lat, float lon, int cnt) throws IOException, JSONException { //, boolean cluster, OwmClient.Lang lang) {
		String subUrl = String.format (Locale.ROOT, "find/station?lat=%f&lon=%f&cnt=%d&cluster=yes",
				Float.valueOf (lat), Float.valueOf (lon), Integer.valueOf (cnt));
		JSONObject response = doQuery (subUrl);
		return new WeatherStatusResponse (response);
	}

	/** Find current weather around a city coordinates
	 * @param lat is the latitude of the geographic point of interest (North/South coordinate)
	 * @param lon is the longitude of the geographic point of interest (East/West coordinate)
	 * @param cnt is the requested number of weather stations to retrieve (the
	 * 	actual answer might be less than the requested).
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	public WeatherStatusResponse currentWeatherAtCity (float lat, float lon, int cnt) throws IOException, JSONException { //, boolean cluster, OwmClient.Lang lang) {
		String subUrl = String.format (Locale.ROOT, "find/city?lat=%f&lon=%f&cnt=%d&cluster=yes",
				Float.valueOf (lat), Float.valueOf (lon), Integer.valueOf (cnt));
		JSONObject response = doQuery (subUrl);
		return new WeatherStatusResponse (response);
	}

	/** Find current weather within a bounding box
	 * @param northLat is the latitude of the geographic top left point of the bounding box
	 * @param westLon is the longitude of the geographic top left point of the bounding box
	 * @param southLat is the latitude of the geographic bottom right point of the bounding box
	 * @param eastLon is the longitude of the geographic bottom right point of the bounding box
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	public WeatherStatusResponse currentWeatherInBoundingBox (float northLat, float westLon, float southLat, float eastLon) throws IOException, JSONException { //, boolean cluster, OwmClient.Lang lang) {
		String subUrl = String.format (Locale.ROOT, "find/station?bbox=%f,%f,%f,%f&cluster=yes",
				Float.valueOf (northLat), Float.valueOf (westLon),
				Float.valueOf (southLat), Float.valueOf (eastLon));
		JSONObject response = doQuery (subUrl);
		return new WeatherStatusResponse (response);
	}

	/** Find current city weather within a bounding box
	 * @param northLat is the latitude of the geographic top left point of the bounding box
	 * @param westLon is the longitude of the geographic top left point of the bounding box
	 * @param southLat is the latitude of the geographic bottom right point of the bounding box
	 * @param eastLon is the longitude of the geographic bottom right point of the bounding box
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	public WeatherStatusResponse currentWeatherAtCityBoundingBox (float northLat, float westLon, float southLat, float eastLon) throws IOException, JSONException { //, boolean cluster, OwmClient.Lang lang) {
		String subUrl = String.format (Locale.ROOT, "find/city?bbox=%f,%f,%f,%f&cluster=yes",
				Float.valueOf (northLat), Float.valueOf (westLon),
				Float.valueOf (southLat), Float.valueOf (eastLon));
		JSONObject response = doQuery (subUrl);
		return new WeatherStatusResponse (response);
	}

	/** Find current weather within a circle
	 * @param lat is the latitude of the geographic center of the circle (North/South coordinate)
	 * @param lon is the longitude of the geographic center of the circle (East/West coordinate)
	 * @param radius is the radius of the circle (in kilometres)
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	public WeatherStatusResponse currentWeatherInCircle (float lat, float lon, float radius) throws IOException, JSONException { //, boolean cluster, OwmClient.Lang lang) {
		String subUrl = String.format (Locale.ROOT, "find/station?lat=%f&lon=%f&radius=%f&cluster=yes",
				Float.valueOf (lat), Float.valueOf (lon), Float.valueOf (radius));
		JSONObject response = doQuery (subUrl);
		return new WeatherStatusResponse (response);
	}

	/** Find current city weather within a circle
	 * @param lat is the latitude of the geographic center of the circle (North/South coordinate)
	 * @param lon is the longitude of the geographic center of the circle (East/West coordinate)
	 * @param radius is the radius of the circle (in kilometres)
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	public WeatherStatusResponse currentWeatherAtCityCircle (float lat, float lon, float radius) throws IOException, JSONException {
		String subUrl = String.format (Locale.ROOT, "find/city?lat=%f&lon=%f&radius=%f&cluster=yes",
				Float.valueOf (lat), Float.valueOf (lon), Float.valueOf (radius));
		JSONObject response = doQuery (subUrl);
		return new WeatherStatusResponse (response);
	}

	/** Find current city weather
	 * @param cityId is the ID of the city
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	public StatusWeatherData currentWeatherAtCity (int cityId) throws IOException, JSONException {
		String subUrl = String.format (Locale.ROOT, "weather/city/%d?type=json", Integer.valueOf (cityId));
		JSONObject response = doQuery (subUrl);
		return new StatusWeatherData (response);
	}

	/** Find current station weather report
	 * @param stationId is the ID of the station
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	public StatusWeatherData currentWeatherAtStation (int stationId) throws IOException, JSONException {
		String subUrl = String.format (Locale.ROOT, "weather/station/%d?type=json", Integer.valueOf (stationId));
		JSONObject response = doQuery (subUrl);
		return new StatusWeatherData (response);
	}

	/** Find current city weather
	 * @param cityName is the name of the city
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	public WeatherStatusResponse currentWeatherAtCity (String cityName) throws IOException, JSONException {
		String subUrl = String.format (Locale.ROOT, "find/name?q=%s", cityName);
		JSONObject response = doQuery (subUrl);
		return new WeatherStatusResponse (response);
	}

	/** Find current city weather
	 * @param cityName is the name of the city
	 * @param countryCode is the two letter country code
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	public WeatherStatusResponse currentWeatherAtCity (String cityName, String countryCode) throws IOException, JSONException {
		String subUrl = String.format (Locale.ROOT, "find/name?q=%s,%s", cityName, countryCode.toUpperCase ());
		JSONObject response = doQuery (subUrl);
		return new WeatherStatusResponse (response);
	}

	/** Get the weather forecast for a city
	 * @param cityId is the ID of the city
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	public WeatherForecastResponse forecastWeatherAtCity (int cityId) throws JSONException, IOException {
		String subUrl = String.format (Locale.ROOT, "forecast/city/%d?type=json&units=metric", cityId);
		JSONObject response = doQuery (subUrl);
		return new WeatherForecastResponse (response);
	}

	/** Get the weather forecast for a city
	 * @param cityName is the Name of the city
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	public WeatherForecastResponse forecastWeatherAtCity (String cityName) throws JSONException, IOException {
		String subUrl = String.format (Locale.ROOT, "forecast/city?q=%s&type=json&units=metric", cityName);
		JSONObject response = doQuery (subUrl);
		return new WeatherForecastResponse (response);
	}

	/** Get the weather history of a city.
	 * @param cityId is the OWM city ID
	 * @param type is the history type (frequency) to use. */
	public WeatherHistoryCityResponse historyWeatherAtCity (int cityId, HistoryType type) throws JSONException, IOException {
		if (type == HistoryType.UNKNOWN)
			throw new IllegalArgumentException("Can't do a historic request for unknown type of history.");
		String subUrl = String.format (Locale.ROOT, "history/city/%d?type=%s", cityId, type);
		JSONObject response = doQuery (subUrl);
		return new WeatherHistoryCityResponse (response);
	}

	/** Get the weather history of a city.
	 * @param stationId is the OWM station ID
	 * @param type is the history type (frequency) to use. */
	public WeatherHistoryStationResponse historyWeatherAtStation (int stationId, HistoryType type) throws JSONException, IOException {
		if (type == HistoryType.UNKNOWN)
			throw new IllegalArgumentException("Can't do a historic request for unknown type of history.");
		String subUrl = String.format (Locale.ROOT, "history/station/%d?type=%s", stationId, type);
		JSONObject response = doQuery (subUrl);
		return new WeatherHistoryStationResponse (response);
	}

	private JSONObject doQuery (String subUrl) throws JSONException, IOException {
		String responseBody = null;
		HttpGet httpget = new HttpGet (this.baseOwmUrl + subUrl);
		if (this.owmAPPID != null) {
			httpget.addHeader (OwmClient.APPID_HEADER, this.owmAPPID);
		}

		HttpResponse response = this.httpClient.execute (httpget);
		InputStream contentStream = null;
		try {
			StatusLine statusLine = response.getStatusLine ();
			if (statusLine == null) {
				throw new IOException (
						String.format ("Unable to get a response from OWM server"));
			}
			int statusCode = statusLine.getStatusCode ();
			if (statusCode < 200 && statusCode >= 300) {
				throw new IOException (
						String.format ("OWM server responded with status code %d: %s", statusCode, statusLine));
			}
			/* Read the response content */
			HttpEntity responseEntity = response.getEntity ();
			contentStream = responseEntity.getContent ();
			Reader isReader = new InputStreamReader (contentStream);
			int contentSize = (int) responseEntity.getContentLength ();
			if (contentSize < 0)
				contentSize = 8*1024;
			StringWriter strWriter = new StringWriter (contentSize);
			char[] buffer = new char[8*1024];
			int n = 0;
			while ((n = isReader.read(buffer)) != -1) {
					strWriter.write(buffer, 0, n);
			}
			responseBody = strWriter.toString ();
			contentStream.close ();
		} catch (IOException e) {
			throw e;
		} catch (RuntimeException re) {
			httpget.abort ();
			throw re;
		} finally {
			if (contentStream != null)
				contentStream.close ();
		}
		return new JSONObject (responseBody);
	}
}
