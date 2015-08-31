/**
 * Copyright 2015 J. Miguel P. Tavares
 * Copyright 2015 J. Cesar Aguilera
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

import org.bitpipeline.lib.owm.OwmClient.HistoryType;
import org.json.JSONException;

/**
 * Provides weather data coming from http://openweathermap.org/
 * @author cs4r
 *
 */
public interface OwmWeatherProvider_V_2_1 {

	/** Find current weather around a geographic point
	 * @param lat is the latitude of the geographic point of interest (North/South coordinate)
	 * @param lon is the longitude of the geographic point of interest (East/West coordinate)
	 * @param cnt is the requested number of weather stations to retrieve (the
	 * 	actual answer might be less than the requested).
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	WeatherStatusResponse currentWeatherAroundPoint (float lat, float lon, int cnt) throws IOException, JSONException;

	/** Find current weather around a city coordinates
	 * @param lat is the latitude of the geographic point of interest (North/South coordinate)
	 * @param lon is the longitude of the geographic point of interest (East/West coordinate)
	 * @param cnt is the requested number of weather stations to retrieve (the
	 * 	actual answer might be less than the requested).
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	WeatherStatusResponse currentWeatherAtCity (float lat, float lon, int cnt) throws IOException, JSONException;

	/** Find current weather within a bounding box
	 * @param northLat is the latitude of the geographic top left point of the bounding box
	 * @param westLon is the longitude of the geographic top left point of the bounding box
	 * @param southLat is the latitude of the geographic bottom right point of the bounding box
	 * @param eastLon is the longitude of the geographic bottom right point of the bounding box
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	WeatherStatusResponse currentWeatherInBoundingBox (float northLat, float westLon, float southLat, float eastLon) throws IOException, JSONException;

	/** Find current city weather within a bounding box
	 * @param northLat is the latitude of the geographic top left point of the bounding box
	 * @param westLon is the longitude of the geographic top left point of the bounding box
	 * @param southLat is the latitude of the geographic bottom right point of the bounding box
	 * @param eastLon is the longitude of the geographic bottom right point of the bounding box
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	WeatherStatusResponse currentWeatherAtCityBoundingBox (float northLat, float westLon, float southLat, float eastLon) throws IOException, JSONException;

	/** Find current weather within a circle
	 * @param lat is the latitude of the geographic center of the circle (North/South coordinate)
	 * @param lon is the longitude of the geographic center of the circle (East/West coordinate)
	 * @param radius is the radius of the circle (in kilometres)
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	WeatherStatusResponse currentWeatherInCircle (float lat, float lon, float radius) throws IOException, JSONException;

	/** Find current city weather within a circle
	 * @param lat is the latitude of the geographic center of the circle (North/South coordinate)
	 * @param lon is the longitude of the geographic center of the circle (East/West coordinate)
	 * @param radius is the radius of the circle (in kilometres)
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	WeatherStatusResponse currentWeatherAtCityCircle (float lat, float lon, float radius) throws IOException, JSONException;

	/** Find current city weather
	 * @param cityId is the ID of the city
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	StatusWeatherData currentWeatherAtCity (int cityId) throws IOException, JSONException;

	/** Find current station weather report
	 * @param stationId is the ID of the station
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	StatusWeatherData currentWeatherAtStation (int stationId) throws IOException, JSONException;

	/** Find current city weather
	 * @param cityName is the name of the city
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	WeatherStatusResponse currentWeatherAtCity (String cityName) throws IOException, JSONException;

	/** Find current city weather
	 * @param cityName is the name of the city
	 * @param countryCode is the two letter country code
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	WeatherStatusResponse currentWeatherAtCity (String cityName, String countryCode) throws IOException, JSONException;

	/** Get the weather forecast for a city
	 * @param cityId is the ID of the city
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	WeatherForecastResponse forecastWeatherAtCity (int cityId) throws JSONException, IOException;

	/** Get the weather forecast for a city
	 * @param cityName is the Name of the city
	 * @throws JSONException if the response from the OWM server can't be parsed
	 * @throws IOException if there's some network error or the OWM server replies with a error. */
	WeatherForecastResponse forecastWeatherAtCity (String cityName) throws JSONException, IOException;

	/** Get the weather history of a city.
	 * @param cityId is the OWM city ID
	 * @param type is the history type (frequency) to use. */
	WeatherHistoryCityResponse historyWeatherAtCity (int cityId, HistoryType type) throws JSONException, IOException;

	/** Get the weather history of a city.
	 * @param stationId is the OWM station ID
	 * @param type is the history type (frequency) to use. */
	WeatherHistoryStationResponse historyWeatherAtStation (int stationId, HistoryType type) throws JSONException, IOException;

}
