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

import org.json.JSONObject;

/**
 * 
 * @author mtavares */
abstract public class AbstractWeatherData {
	protected static final String JSON_DATE_TIME = "dt";
	protected static final String JSON_MAIN      = "main";
	protected static final String JSON_WIND      = "wind";
	protected static final String JSON_RAIN      = "rain";
	protected static final String JSON_SNOW      = "snow";


	static abstract public class Main {
		protected static final String JSON_TEMP     = "temp";
		protected static final String JSON_TEMP_MIN = "temp_min";
		protected static final String JSON_TEMP_MAX = "temp_max";
		protected static final String JSON_HUMIDITY = "humidity";
		protected static final String JSON_PRESSURE = "pressure";

		abstract public float getTemp ();
		abstract public float getTempMin ();
		abstract public float getTempMax ();
		abstract public float getPressure ();
		abstract public float getHumidity ();
	}

	static abstract public class Wind {
		protected static final String JSON_SPEED   = "speed";
		protected static final String JSON_DEG     = "deg";
		protected static final String JSON_GUST    = "gust";
		protected static final String JSON_VAR_BEG = "var_beg";
		protected static final String JSON_VAR_END = "var_end";

		abstract public float getSpeed ();
		abstract public int getDeg ();
		abstract public float getGust ();
		abstract public int getVarBeg ();
		abstract public int getVarEnd ();
	}

	private final long dateTime;

	public AbstractWeatherData (JSONObject json) {
		this.dateTime = json.optLong (WeatherData.JSON_DATE_TIME, Long.MIN_VALUE);
	}

	public long getDateTime () {
		return this.dateTime;
	}

	/** Get the temperature of this weather report 
	 * @return <code>Float.NaN</code> if the report doesn't have temperature data; the value of the temperature in Kelvin otherwise. */
	abstract public float getTemp ();
	
	/** Get the humidity of this weather report 
	 * @return <code>Float.NaN</code> if the report doesn't have humidity data; the value of the humidity in percentage to condensation point otherwise. */
	abstract public float getHumidity ();

	/** Get the atmospheric pressure of this weather report 
	 * @return <code>Float.NaN</code> if the report doesn't have pressure data; the value of the pressure in hectopascal otherwise. */
	abstract public float getPressure ();

	/** Get the average wind speed of this weather report 
	 * @return <code>Float.NaN</code> if the report doesn't have wind speed data; the value of the wind speed in metre per second otherwise. */
	abstract public float getWindSpeed ();

	/** Get the wind gust speed of this weather report 
	 * @return <code>Float.NaN</code> if the report doesn't have wind gust speed data; the value of the wind gust speed in metre per second otherwise. */
	abstract public float getWindGust ();

	/** Get the average wind direction of this weather report 
	 * @return <code>Integer.MIN_VALUE</code> if the report doesn't have wind direction data; the degree of the wind direction otherwise. */
	abstract public int getWindDeg ();

	/** Get the rain amount of this weather report 
	 * @return <code>Integer.MIN_VALUE</code> if the report doesn't have rain data; the amount of rain in mm per hour otherwise. */
	abstract public int getRain ();
	
	/** Get the snow amount of this weather report 
	 * @return <code>Integer.MIN_VALUE</code> if the report doesn't have snow data; the amount of snow in mm per hour otherwise. */
	abstract public int getSnow ();

	/** Get the amount of precipitation in this weather report 
	 * @return <code>Integer.MIN_VALUE</code> if the report doesn't have precipitation data; the amount of precipitation in mm per hour otherwise. */
	abstract public int getPrecipitation ();
}
