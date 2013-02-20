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

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.bitpipeline.lib.owm.OwmClient.HistoryType;
import org.bitpipeline.lib.owm.WeatherForecastResponse.City;
import org.json.JSONException;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * 
 * @author mtavares */
public class OwmClientTest {

	private HttpClient createHttpClientThatRespondsWith (final String responseBody) throws IOException {
		HttpClient mockHttpClient = mock (HttpClient.class);
		when (mockHttpClient.execute (any (HttpGet.class))).then (new Answer<HttpResponse>() {
			@Override
			public HttpResponse answer (InvocationOnMock invocation) throws Throwable {
				HttpResponse response = new BasicHttpResponse (HttpVersion.HTTP_1_1, 200, "Ok");
				response.setEntity (new StringEntity (responseBody));
				return response;
			}
		});
		return mockHttpClient;
	}

	private static void assertWeatherData (StatusWeatherData weatherData) {
		assertNotNull (weatherData);
		assertNotNull (weatherData.getName ());
		assertFalse (weatherData.getId () == Long.MIN_VALUE);
		assertFalse (weatherData.getDateTime () == Long.MIN_VALUE);
		assertTrue (weatherData.hasMain ());
	}

	private static void assertWeatherDataList (List<StatusWeatherData> weatherDataList, int maxData) {
		assertNotNull (weatherDataList);
		assertTrue (weatherDataList.size () <= maxData);
		for (StatusWeatherData weatherData : weatherDataList) {
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
		when (mockHttpClient.execute (any (HttpGet.class))).then (new Answer<HttpResponse>() {
			@Override
			public HttpResponse answer (InvocationOnMock invocation) throws Throwable {
				HttpGet httpGet = (HttpGet) invocation.getArguments ()[0];
				Header[] headers = httpGet.getHeaders ("x-api-key");
				assertNotNull (headers);
				assertEquals (1, headers.length);
				assertEquals (appid, headers[0].getValue ());
				
				HttpResponse response = new BasicHttpResponse (HttpVersion.HTTP_1_1, 200, "Ok");
				response.setEntity (new StringEntity (TestData.CURRENT_WEATHER_AROUND_CITY_COORD));
				return response;
			}
		});

		OwmClient owm = new OwmClient (mockHttpClient);
		owm.setAPPID (appid);
		owm.currentWeatherAtCity (55f, 37f, 10);
	}

	@Test
	public void testCurrentWeatherAroundPoint () throws IOException, JSONException {
		HttpClient mockHttpClient = createHttpClientThatRespondsWith (TestData.CURRENT_WEATHER_AROUND_POINT);
		OwmClient owm = new OwmClient (mockHttpClient);
		WeatherStatusResponse currentWeather = owm.currentWeatherAroundPoint (55f, 37f, 10);
		assertTrue (currentWeather.hasWeatherStatus ());
		OwmClientTest.assertWeatherDataList (currentWeather.getWeatherStatus (), 10);
	}

	@Test
	public void testCurrentWeatherAroundCity () throws IOException, JSONException {
		HttpClient mockHttpClient = createHttpClientThatRespondsWith (TestData.CURRENT_WEATHER_AROUND_CITY_COORD);
		OwmClient owm = new OwmClient (mockHttpClient);
		WeatherStatusResponse currentWeather = owm.currentWeatherAtCity (55f, 37f, 10);
		assertTrue (currentWeather.hasWeatherStatus ());
		OwmClientTest.assertWeatherDataList (currentWeather.getWeatherStatus (), 10);
	}

	@Test
	public void testCurrentWeatherInBoundingBox () throws IOException, JSONException {
		HttpClient mockHttpClient = createHttpClientThatRespondsWith (TestData.CURRENT_WEATHER_IN_BBOX);
		OwmClient owm = new OwmClient (mockHttpClient);
		WeatherStatusResponse currentWeather = owm.currentWeatherInBoundingBox (12f, 32f, 15f, 37f);
		assertTrue (currentWeather.hasWeatherStatus ());
		OwmClientTest.assertWeatherDataList (currentWeather.getWeatherStatus (), Integer.MAX_VALUE);
	}

	@Test
	public void testCurrentWeatherAtCityBoundingBox () throws IOException, JSONException {
		HttpClient mockHttpClient = createHttpClientThatRespondsWith (TestData.CURRENT_WEATHER_IN_CITY_BBOX);
		OwmClient owm = new OwmClient (mockHttpClient);
		WeatherStatusResponse currentWeather = owm.currentWeatherAtCityBoundingBox (12f, 32f, 15f, 37f);
		assertTrue (currentWeather.hasWeatherStatus ());
		OwmClientTest.assertWeatherDataList (currentWeather.getWeatherStatus (), Integer.MAX_VALUE);
	}

	@Test
	public void testCurrentWeatherInCircle () throws IOException, JSONException {
		HttpClient mockHttpClient = createHttpClientThatRespondsWith (TestData.CURRENT_WEATHER_IN_CIRCLE);
		OwmClient owm = new OwmClient (mockHttpClient);
		WeatherStatusResponse currentWeather = owm.currentWeatherInCircle (55.5f, 37.5f, 40f);
		assertTrue (currentWeather.hasWeatherStatus ());
		OwmClientTest.assertWeatherDataList (currentWeather.getWeatherStatus (), Integer.MAX_VALUE);
	}

	@Test
	public void testCurrentWeatherAtCityInCircle () throws IOException, JSONException {
		HttpClient mockHttpClient = createHttpClientThatRespondsWith (TestData.CURRENT_WEATHER_IN_CIRCLE);
		OwmClient owm = new OwmClient (mockHttpClient);
		WeatherStatusResponse currentWeather = owm.currentWeatherAtCityCircle (55.5f, 37.5f, 40f);
		assertTrue (currentWeather.hasWeatherStatus ());
		OwmClientTest.assertWeatherDataList (currentWeather.getWeatherStatus (), Integer.MAX_VALUE);
	}

	@Test
	public void testCurrentWeatherAtCityId () throws IOException, JSONException {
		HttpClient mockHttpClient = createHttpClientThatRespondsWith (TestData.CURRENT_WEATHER_AT_CITY_ID_MOSKOW);
		OwmClient owm = new OwmClient (mockHttpClient);
		StatusWeatherData weatherData = owm.currentWeatherAtCity (524901);
		assertEquals ("Moscow", weatherData.getName ());
		assertEquals (524901, weatherData.getId ());
		OwmClientTest.assertWeatherData (weatherData);
	}

	@Test
	public void testCurrentWeatherAtStationId () throws IOException, JSONException {
		HttpClient mockHttpClient = createHttpClientThatRespondsWith (TestData.CURRENT_WEATHER_AT_STATION_ID_9040);
		OwmClient owm = new OwmClient (mockHttpClient);
		StatusWeatherData weatherData = owm.currentWeatherAtStation (9040);
		assertEquals ("CT1AKV-10", weatherData.getName ());
		assertEquals (9040, weatherData.getId ());
		assertEquals (1360924205, weatherData.getDateTime ());

		assertTrue (weatherData.hasMain ());
		assertEquals (  281.48f, weatherData.getMain ().getTemp (),     0.001f);
		assertEquals (  99f,     weatherData.getMain ().getHumidity (), 0.001f);
		assertEquals (1016f,     weatherData.getMain ().getPressure (), 0.001f);
		
		assertTrue (weatherData.hasWind ());
		assertEquals (0f, weatherData.getWind ().getSpeed (), 0.001f);
		assertEquals (0f, weatherData.getWind ().getGust (),  0.001f);
		assertEquals (0, weatherData.getWind ().getDeg ());
		
		assertTrue (weatherData.hasRain ());
		assertEquals (2,  weatherData.getRainObj ().measurements ().size ());
		assertEquals (0f, weatherData.getRainObj ().getMeasure (1),  0.001f);
		assertEquals (0f, weatherData.getRainObj ().getMeasure (24), 0.001f);
		assertEquals (0,  weatherData.getRainObj ().getToday ());
		
		assertTrue (weatherData.hasCoord ());
		assertTrue (weatherData.getCoord ().hasLongitude ());
		assertTrue (weatherData.getCoord ().hasLatitude ());
		assertEquals (-8.7363f, weatherData.getCoord ().getLongitude (), 0.00001f);
		assertEquals (39.1862f, weatherData.getCoord ().getLatitude (),  0.00001f);
		
		assertTrue (weatherData.hasStation ());
		assertEquals (7, weatherData.getStation ().getZoom ());
	}

	@Test
	public void testCurrentWeatherAtCityName () throws IOException, JSONException {
		HttpClient mockHttpClient = createHttpClientThatRespondsWith (TestData.CURRENT_WEATHER_AT_CITY_NAME_LONDON);
		OwmClient owm = new OwmClient (mockHttpClient);
		WeatherStatusResponse currentWeather = owm.currentWeatherAtCity ("london");
		assertTrue (currentWeather.hasWeatherStatus ());
		OwmClientTest.assertWeatherDataList (currentWeather.getWeatherStatus (), Integer.MAX_VALUE);
		for (StatusWeatherData weather : currentWeather.getWeatherStatus ()) {
			assertTrue ("london".equalsIgnoreCase (weather.getName ()));
		}
	}

	@Test
	public void testCurrentWeatherAtCityNameWithCountryCode () throws IOException, JSONException {
		HttpClient mockHttpClient = createHttpClientThatRespondsWith (TestData.CURRENT_WEATHER_AT_CITY_NAME_LONDON_COUNTRY_CODE_UK);
		OwmClient owm = new OwmClient (mockHttpClient);
		WeatherStatusResponse currentWeather = owm.currentWeatherAtCity ("london", "UK");
		assertTrue (currentWeather.hasWeatherStatus ());
		OwmClientTest.assertWeatherDataList (currentWeather.getWeatherStatus (), Integer.MAX_VALUE);
		for (StatusWeatherData weather : currentWeather.getWeatherStatus ()) {
			assertTrue ("london".equalsIgnoreCase (weather.getName ()));
		}
	}

	@Test
	public void testForecastAtCityId () throws IOException, JSONException {
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
	public void testForecastAtCityName () throws IOException, JSONException {
		HttpClient mockHttpClient = createHttpClientThatRespondsWith (TestData.FORECAST_WEATHER_AT_CITY_NAME_LISBON);
		OwmClient owm = new OwmClient (mockHttpClient);
		WeatherForecastResponse forecastResponse = owm.forecastWeatherAtCity ("Lisbon");
		City lisbon = forecastResponse.getCity ();
		assertNotNull (lisbon);
		assertTrue ("PT".equalsIgnoreCase (lisbon.getCountryCode ()));
		assertTrue ("Lisbon".equalsIgnoreCase (lisbon.getName ()));
		OwmClientTest.assertForecastWeatherDataList (forecastResponse.getForecasts (), Integer.MAX_VALUE);
	}

	@Test
	public void testHourHistoryWeatherAtCity () throws IOException, JSONException {
		HttpClient mockHttpClient = createHttpClientThatRespondsWith (TestData.HISTORY_WEATHER_AT_CITY_ID);
		OwmClient owm = new OwmClient (mockHttpClient);
		WeatherHistoryCityResponse history = owm.historyWeatherAtCity (2885679, HistoryType.HOUR);
		assertEquals (0.0186d, history.getCalcTime (), 0.0001d);
		assertEquals (0.0056d, history.getCalcTimeFetch (), 0.0001d);
		assertEquals (0.0131d, history.getCalcTimeFind (), 0.0001d);
		assertEquals (2885679, history.getCityId ());
		assertTrue (history.hasHistory ());
	}

	@Test
	public void testTickHistoryWeatherAtStation () throws IOException, JSONException {
		HttpClient mockHttpClient = createHttpClientThatRespondsWith (TestData.HISTORY_WEATHER_AT_STATION_ID_BY_TICK);
		OwmClient owm = new OwmClient (mockHttpClient);
		WeatherHistoryStationResponse history = owm.historyWeatherAtStation (9040, HistoryType.TICK);
		assertEquals (1.5991d, history.getCalcTime (), 0.0001d);
		assertEquals (0.0456d, history.getCalcTimeTick (), 0.0001d);
		assertEquals (9040, history.getStationId ());
		assertEquals (OwmClient.HistoryType.TICK, history.getType ());
		assertTrue (history.hasHistory ());
	}

	@Test
	public void testHourlyHistoryWeatherAtStation () throws IOException, JSONException {
		HttpClient mockHttpClient = createHttpClientThatRespondsWith (TestData.HISTORY_WEATHER_AT_STATION_ID_BY_HOUR);
		OwmClient owm = new OwmClient (mockHttpClient);
		WeatherHistoryStationResponse historyResponse = owm.historyWeatherAtStation (9040, HistoryType.HOUR);
		assertNotNull  (historyResponse);
		List<AbstractWeatherData> history = historyResponse.getHistory ();
		assertNotNull (history);
		assertTrue (history.size () == 14);
		AbstractWeatherData weather = history.get (0);
		assertNotNull (weather);
		assertEquals (289.26f, weather.getTemp (), 0.001f);
		assertEquals (1019f, weather.getPressure (), 0.01f);
		assertEquals (99f, weather.getHumidity (), 0.01f);
		// wind
		assertEquals (0f, weather.getWindSpeed (), 0.01f);
		assertEquals (0f, weather.getWindGust (), 0.01f);
		assertEquals (Integer.MIN_VALUE, weather.getWindDeg ());
		// rain, snow and precipitation
		assertEquals (0, weather.getRain ());
		assertEquals (Integer.MIN_VALUE, weather.getSnow ());
		assertEquals (0, weather.getPrecipitation ());
	}
}
