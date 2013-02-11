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

public class WeatherData extends AbstractWeatherData {
	private static final String JSON_ID   = "id";
	private static final String JSON_NAME = "name";

	private final long id;
	private final String name;

	public WeatherData (JSONObject json) {
		super (json);
		this.id = json.optLong (WeatherData.JSON_ID, Long.MIN_VALUE);
		this.name = json.optString (WeatherData.JSON_NAME);
	}

	public boolean hasId () {
		return this.id != Long.MIN_VALUE;
	}
	public long getId () {
		return this.id;
	}

	public boolean hasName () {
		return this.name != null && !this.name.isEmpty ();
	}
	public String getName () {
		return this.name;
	}
}