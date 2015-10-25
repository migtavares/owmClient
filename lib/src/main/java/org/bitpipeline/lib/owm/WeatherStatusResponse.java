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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
 * @author mtavares */
public class WeatherStatusResponse extends AbstractOwmResponse {
	private final List<StatusWeatherData> status;

	/** A parser for a weather status query response
	 * @param json The JSON obejct built from the OWM response */
	public WeatherStatusResponse (JSONObject json) {
		super (json);
		JSONArray jsonWeatherStatus = json.optJSONArray (AbstractOwmResponse.JSON_LIST);
		if (jsonWeatherStatus == null) {
			this.status = Collections.emptyList ();
		} else {
			this.status = new ArrayList<StatusWeatherData> (jsonWeatherStatus.length ());
			for (int i = 0; i <jsonWeatherStatus.length (); i++) {
				JSONObject jsonStatus = jsonWeatherStatus.optJSONObject (i);
				if (jsonStatus != null) {
					this.status.add (new StatusWeatherData (jsonStatus));
				}
			}
		}
	}

	public boolean hasWeatherStatus () {
		return this.status != null && !this.status.isEmpty ();
	}
	public List<StatusWeatherData> getWeatherStatus () {
		return this.status;
	}
}
