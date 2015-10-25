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
 * @author mtavares */
public class ForecastWeatherData extends LocalizedWeatherData {
	static private final String DATETIME_KEY_NAME = "dt";

	private long calcDateTime = Long.MIN_VALUE;
	
	/**
	 * @param json json container with the forecast data */
	public ForecastWeatherData (JSONObject json) {
		super (json);
		this.calcDateTime = json.optLong (ForecastWeatherData.DATETIME_KEY_NAME, Long.MIN_VALUE);
	}

	public long getCalcDateTime () {
		return this.calcDateTime;
	}
}
