/**
 * Copyright 2012 J. Miguel P. Tavares
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.bitpipeline.lib.owm.WeatherForecastResponse.City;
import org.json.JSONException;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * 
 * @author mtavares */
public class OwmClientTest {

	private HttpClient createHttpClientThatRespondsWith (final String response) throws HttpException, IOException {
		HttpClient mockHttpClient = mock (HttpClient.class);
		when (mockHttpClient.executeMethod (any (HttpMethod.class))).then (new Answer<Integer>() {
			@Override
			public Integer answer (InvocationOnMock invocation) throws Throwable {
				GetMethod getMethod = (GetMethod) invocation.getArguments ()[0];
				Method setResponseStream = HttpMethodBase.class.getDeclaredMethod ("setResponseStream", InputStream.class);
				setResponseStream.setAccessible (true);
				setResponseStream.invoke (getMethod, new ByteArrayInputStream (response.getBytes ()));
				return HttpStatus.SC_OK;
			}
		});
		return mockHttpClient;
	}

	private static void assertWeatherData (WeatherData weatherData) {
		assertNotNull (weatherData);
		assertNotNull (weatherData.getName ());
		assertFalse (weatherData.getId () == Long.MIN_VALUE);
		assertFalse (weatherData.getDateTime () == Long.MIN_VALUE);
		assertTrue (weatherData.hasMain ());
	}

	private static void assertWeatherDataList (List<WeatherData> weatherDataList, int maxData) {
		assertNotNull (weatherDataList);
		assertTrue (weatherDataList.size () <= maxData);
		for (WeatherData weatherData : weatherDataList) {
			OwmClientTest.assertWeatherData (weatherData);
		}
	}

	private static void assertForecastWeatherData (ForecastWeatherData forecast) {
		assertNotNull (forecast);
		assertFalse (forecast.getDateTime () == Long.MIN_VALUE);
		assertFalse (forecast.getCalcDateTime () == Long.MIN_VALUE);
		assertTrue (forecast.hasMain ());
	}

	private static void assertForecastWeatherDataList (List<ForecastWeatherData> forecasts, int maxData) {
		assertNotNull (forecasts);
		assertTrue (forecasts.size () <= maxData);
		for (ForecastWeatherData weatherData : forecasts) {
			OwmClientTest.assertForecastWeatherData (weatherData);
		}
	}

	@Test
	public void testAPPIDHeaderRequest () throws IOException, JSONException {
		final String appid = UUID.randomUUID ().toString ();
		HttpClient mockHttpClient = mock (HttpClient.class);
		when (mockHttpClient.executeMethod (any (HttpMethod.class))).then (new Answer<Integer>() {
			@Override
			public Integer answer (InvocationOnMock invocation) throws Throwable {
				GetMethod getMethod = (GetMethod) invocation.getArguments ()[0];

				// Check the request
				assertEquals (appid, getMethod.getRequestHeader ("x-api-key").getValue ());

				// return a valid response
				Method setResponseStream = HttpMethodBase.class.getDeclaredMethod ("setResponseStream", InputStream.class);
				setResponseStream.setAccessible (true);
				setResponseStream.invoke (getMethod, new ByteArrayInputStream (TestData.CURRENT_WEATHER_AROUND_CITY_COORD.getBytes ()));
				return HttpStatus.SC_OK;
			}
		});
		OwmClient owm = new OwmClient (mockHttpClient);
		owm.setAPPID (appid);
		owm.currentWeatherAtCity (55f, 37f, 10);
	}

	@Test
	public void testCurrentWeatherAroundPoint () throws HttpException, IOException, JSONException {
		HttpClient mockHttpClient = createHttpClientThatRespondsWith (TestData.CURRENT_WEATHER_AROUND_POINT);
		OwmClient owm = new OwmClient (mockHttpClient);
		WeatherStatusResponse currentWeather = owm.currentWeatherAroundPoint (55f, 37f, 10);
		assertTrue (currentWeather.hasWeatherStatus ());
		OwmClientTest.assertWeatherDataList (currentWeather.getWeatherStatus (), 10);
	}

	@Test
	public void testCurrentWeatherAroundCity () throws HttpException, IOException, JSONException {
		HttpClient mockHttpClient = createHttpClientThatRespondsWith (TestData.CURRENT_WEATHER_AROUND_CITY_COORD);
		OwmClient owm = new OwmClient (mockHttpClient);
		WeatherStatusResponse currentWeather = owm.currentWeatherAtCity (55f, 37f, 10);
		assertTrue (currentWeather.hasWeatherStatus ());
		OwmClientTest.assertWeatherDataList (currentWeather.getWeatherStatus (), 10);
	}

	@Test
	public void testCurrentWeatherInBoundingBox () throws HttpException, IOException, JSONException {
		HttpClient mockHttpClient = createHttpClientThatRespondsWith (TestData.CURRENT_WEATHER_IN_BBOX);
		OwmClient owm = new OwmClient (mockHttpClient);
		WeatherStatusResponse currentWeather = owm.currentWeatherInBoundingBox (12f, 32f, 15f, 37f);
		assertTrue (currentWeather.hasWeatherStatus ());
		OwmClientTest.assertWeatherDataList (currentWeather.getWeatherStatus (), Integer.MAX_VALUE);
	}

	@Test
	public void testCurrentWeatherAtCityBoundingBox () throws HttpException, IOException, JSONException {
		HttpClient mockHttpClient = createHttpClientThatRespondsWith (TestData.CURRENT_WEATHER_IN_CITY_BBOX);
		OwmClient owm = new OwmClient (mockHttpClient);
		WeatherStatusResponse currentWeather = owm.currentWeatherAtCityBoundingBox (12f, 32f, 15f, 37f);
		assertTrue (currentWeather.hasWeatherStatus ());
		OwmClientTest.assertWeatherDataList (currentWeather.getWeatherStatus (), Integer.MAX_VALUE);
	}

	@Test
	public void testCurrentWeatherInCircle () throws HttpException, IOException, JSONException {
		HttpClient mockHttpClient = createHttpClientThatRespondsWith (TestData.CURRENT_WEATHER_IN_CIRCLE);
		OwmClient owm = new OwmClient (mockHttpClient);
		WeatherStatusResponse currentWeather = owm.currentWeatherInCircle (55.5f, 37.5f, 40f);
		assertTrue (currentWeather.hasWeatherStatus ());
		OwmClientTest.assertWeatherDataList (currentWeather.getWeatherStatus (), Integer.MAX_VALUE);
	}

	@Test
	public void testCurrentWeatherAtCityInCircle () throws HttpException, IOException, JSONException {
		HttpClient mockHttpClient = createHttpClientThatRespondsWith (TestData.CURRENT_WEATHER_IN_CIRCLE);
		OwmClient owm = new OwmClient (mockHttpClient);
		WeatherStatusResponse currentWeather = owm.currentWeatherAtCityCircle (55.5f, 37.5f, 40f);
		assertTrue (currentWeather.hasWeatherStatus ());
		OwmClientTest.assertWeatherDataList (currentWeather.getWeatherStatus (), Integer.MAX_VALUE);
	}

	@Test
	public void testCurrentWeatherAtCityId () throws HttpException, IOException, JSONException {
		HttpClient mockHttpClient = createHttpClientThatRespondsWith (TestData.CURRENT_WEATHER_AT_CITY_ID_MOSKOW);
		OwmClient owm = new OwmClient (mockHttpClient);
		WeatherData weatherData = owm.currentWeatherAtCity (524901);
		assertEquals ("Moscow", weatherData.getName ());
		assertEquals (524901, weatherData.getId ());
		OwmClientTest.assertWeatherData (weatherData);
	}

	@Test
	public void testCurrentWeatherAtCityName () throws HttpException, IOException, JSONException {
		HttpClient mockHttpClient = createHttpClientThatRespondsWith (TestData.CURRENT_WEATHER_AT_CITY_NAME_LONDON);
		OwmClient owm = new OwmClient (mockHttpClient);
		WeatherStatusResponse currentWeather = owm.currentWeatherAtCity ("london");
		assertTrue (currentWeather.hasWeatherStatus ());
		OwmClientTest.assertWeatherDataList (currentWeather.getWeatherStatus (), Integer.MAX_VALUE);
		for (WeatherData weather : currentWeather.getWeatherStatus ()) {
			assertTrue ("london".equalsIgnoreCase (weather.getName ()));
		}
	}

	@Test
	public void testCurrentWeatherAtCityNameWithCountryCode () throws HttpException, IOException, JSONException {
		HttpClient mockHttpClient = createHttpClientThatRespondsWith (TestData.CURRENT_WEATHER_AT_CITY_NAME_LONDON_COUNTRY_CODE_UK);
		OwmClient owm = new OwmClient (mockHttpClient);
		WeatherStatusResponse currentWeather = owm.currentWeatherAtCity ("london", "UK");
		assertTrue (currentWeather.hasWeatherStatus ());
		OwmClientTest.assertWeatherDataList (currentWeather.getWeatherStatus (), Integer.MAX_VALUE);
		for (WeatherData weather : currentWeather.getWeatherStatus ()) {
			assertTrue ("london".equalsIgnoreCase (weather.getName ()));
		}
	}

	@Test
	public void testForecastAtCityId () throws HttpException, IOException, JSONException {
		HttpClient mockHttpClient = createHttpClientThatRespondsWith (TestData.FORECAST_WEATHER_AT_CITY_ID_LISBON);
		OwmClient owm = new OwmClient (mockHttpClient);
		WeatherForecastResponse forecastResponse = owm.forecastWeatherAtCity (524901);
		City lisbon = forecastResponse.getCity ();
		assertNotNull (lisbon);
		assertTrue ("PT".equalsIgnoreCase (lisbon.getCountryCode ()));
		assertTrue ("Lisbon".equalsIgnoreCase (lisbon.getName ()));
		OwmClientTest.assertForecastWeatherDataList (forecastResponse.getForecasts (), Integer.MAX_VALUE);
	}

	@Test
	public void testForecastAtCityName () throws HttpException, IOException, JSONException {
		HttpClient mockHttpClient = createHttpClientThatRespondsWith (TestData.FORECAST_WEATHER_AT_CITY_NAME_LISBON);
		OwmClient owm = new OwmClient (mockHttpClient);
		WeatherForecastResponse forecastResponse = owm.forecastWeatherAtCity ("Lisbon");
		City lisbon = forecastResponse.getCity ();
		assertNotNull (lisbon);
		assertTrue ("PT".equalsIgnoreCase (lisbon.getCountryCode ()));
		assertTrue ("Lisbon".equalsIgnoreCase (lisbon.getName ()));
		OwmClientTest.assertForecastWeatherDataList (forecastResponse.getForecasts (), Integer.MAX_VALUE);
	}
}
