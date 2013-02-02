/**
 * Copyright 2012 
 *         J. Miguel P. Tavares <mtavares@bitpipeline.eu>
 *         BitPipeline
 */
package org.bitpipeline.lib.owm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.bitpipeline.lib.owm.WeatherData.GeoCoord;
import org.bitpipeline.lib.owm.WeatherData.WeatherCondition;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

/**
 * @author mtavares */
public class WeatherDataTest {
	static String WEATHER_DATA_STRING = "        {\n" + 
			"            \"id\": 5106529,\n" + 
			"            \"name\": \"Woodbridge\",\n" + 
			"            \"coord\": {\n" + 
			"                \"lon\": -74.284592,\n" + 
			"                \"lat\": 40.557598\n" + 
			"            },\n" + 
			"            \"distance\": 0.814,\n" + 
			"            \"main\": {\n" + 
			"                \"temp\": 267.97,\n" + 
			"                \"pressure\": 1026,\n" + 
			"                \"humidity\": 45,\n" + 
			"                \"temp_min\": 267.15,\n" + 
			"                \"temp_max\": 269.15\n" + 
			"            },\n" + 
			"            \"dt\": 1359818400,\n" + 
			"            \"wind\": {\n" + 
			"                \"speed\": 3.1,\n" + 
			"                \"deg\": 260\n" + 
			"            },\n" + 
			"            \"clouds\": {\n" + 
			"                \"all\": 40\n" + 
			"            },\n" + 
			"            \"weather\": [\n" + 
			"                {\n" + 
			"                    \"id\": 802,\n" + 
			"                    \"main\": \"Clouds\",\n" + 
			"                    \"description\": \"scattered clouds\",\n" + 
			"                    \"icon\": \"03d\"\n" + 
			"                }\n" + 
			"            ]\n" + 
			"        }"; 

	@Test
	public void testWeatherDataParsing () throws JSONException {
		JSONObject weatherDatajson = new JSONObject (WeatherDataTest.WEATHER_DATA_STRING);
		WeatherData weather = new WeatherData (weatherDatajson);
		
		assertNotNull (weather);
		assertEquals (5106529, weather.getId ());
		assertEquals ("Woodbridge", weather.getName ());
		assertEquals (1359818400, weather.getDateTime ());

		assertTrue (weather.hasCoord ());
		GeoCoord coord = weather.getCoord ();
		assertNotNull (coord);
		assertEquals (-74.284592f, coord.getLongitude (), 0.000001f);
		assertEquals (40.557598, coord.getLatitude (), 0.000001f);

		WeatherData.Main mainWeather = weather.getMain ();
		assertNotNull (mainWeather);
		assertTrue (mainWeather.hasTemp ());
		assertEquals (267.97f, mainWeather.getTemp (), 0.001f);
		assertTrue (mainWeather.hasTempMin ());
		assertEquals (267.15f, mainWeather.getTempMin (), 0.001f);
		assertTrue (mainWeather.hasTempMax ());
		assertEquals (269.15f, mainWeather.getTempMax (), 0.001f);
		assertTrue (mainWeather.hasPressure ());
		assertEquals (1026f, mainWeather.getPressure (), 0.1f);
		assertTrue (mainWeather.hasHumidity ());
		assertEquals (45f, mainWeather.getHumidity (), 0.1f);

		assertTrue (weather.hasWind ());
		WeatherData.Wind wind = weather.getWind ();
		assertNotNull (wind);
		assertTrue (wind.hasSpeed ());
		assertEquals (3.1f, wind.getSpeed (), 0.01f);
		assertTrue (wind.hasDeg ());
		assertEquals (260, wind.getDeg ());
		assertFalse (wind.hasGust ());
		assertFalse (wind.hasVarBeg ());
		assertFalse (wind.hasVarEnd ());

		assertTrue (weather.hasClouds ());
		WeatherData.Clouds clouds = weather.getClouds ();
		assertNotNull (clouds);
		assertEquals (40, clouds.getAll ());
		assertFalse (clouds.hasMeasures ());

		assertFalse (weather.hasRain ());
		assertFalse (weather.hasSnow ());

		assertTrue (weather.hasWeatherConditions ());
		List<WeatherCondition> weatherConditions = weather.getWeatherConditions ();
		assertNotNull (weatherConditions);
		assertEquals (1, weatherConditions.size ());
		WeatherCondition condition = weatherConditions.get (0);
		assertNotNull (condition);
		assertEquals (802, condition.getCode ().getId ());
		assertEquals (WeatherCondition.ConditionCode.SCATTERED_CLOUDS, condition.getCode ());
		assertEquals ("Clouds", condition.getMain ());
		assertEquals ("scattered clouds", condition.getDescription ());
		assertEquals ("03d", condition.getIconName ());
	}

	// TODO test with invalid data.
}
