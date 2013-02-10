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

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherData extends AbstractWeatherData {
	private static final String JSON_ID   = "id";
	private static final String JSON_NAME = "name";

	private long id = Long.MIN_VALUE;
	private String name = null;

	public WeatherData (JSONObject json) throws JSONException {
		super (json);
		this.id = json.getLong (WeatherData.JSON_ID);
		this.name = json.getString (WeatherData.JSON_NAME);
	}

	public long getId () {
		return this.id;
	}

	public String getName () {
		return this.name;
	}
}