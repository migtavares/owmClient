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
public class WeatherHistoryStationResponse extends AbstractOwmResponse {
	static private final String JSON_CALCTIME_TICK = "tick";
	static private final String JSON_STATION_ID    = "station_id";
	static private final String JSON_TYPE          = "type";

	private final double calctimeTick;
	private final int stationId;
	private final OwmClient.HistoryType type;
	private final List<WeatherData> history;

	/**
	 * @param json */
	public WeatherHistoryStationResponse (JSONObject json) {
		super (json);

		String calcTimeStr = json.optString (AbstractOwmResponse.JSON_CALCTIME);
		this.calctimeTick = AbstractOwmResponse.getValueFromCalcTimeStr (calcTimeStr, WeatherHistoryStationResponse.JSON_CALCTIME_TICK);
		this.stationId = json.optInt (WeatherHistoryStationResponse.JSON_STATION_ID, Integer.MIN_VALUE);

		OwmClient.HistoryType typeValue = null;
		String typeStr = json.optString (WeatherHistoryStationResponse.JSON_TYPE);
		if (typeStr != null && !typeStr.isEmpty ()) {
			try {
				typeValue = OwmClient.HistoryType.valueOf (typeStr.trim ().toUpperCase ());
			} catch (IllegalArgumentException e) {
				typeValue = OwmClient.HistoryType.UNKNOWN;
			}
		}
		this.type = typeValue;
		
		JSONArray jsonHistory = json.optJSONArray (AbstractOwmResponse.JSON_LIST);
		if (jsonHistory == null) {
			this.history = Collections.emptyList ();
		} else {
			this.history = new ArrayList<WeatherData> (jsonHistory.length ());
			for (int i = 0; i <jsonHistory.length (); i++) {
				JSONObject jsonBaseWeatherData = jsonHistory.optJSONObject (i);
				if (jsonBaseWeatherData != null) {
					this.history.add (new WeatherData (jsonBaseWeatherData));
				}
			}
		}

	}

	public boolean hasCalcTimeTick () {
		return !Double.isNaN (this.calctimeTick);
	}
	public double getCalcTimeTick () {
		return this.calctimeTick;
	}

	public boolean hasStationId () {
		return this.stationId != Integer.MIN_VALUE;
	}
	public int getStationId () {
		return this.stationId;
	}

	public boolean hasType () {
		return this.type != null;
	}
	public OwmClient.HistoryType getType () {
		return this.type;
	}

	public boolean hasHistory () {
		return this.history != null && !this.history.isEmpty ();
	}
	public List<WeatherData> getHistory () {
		return this.history;
	}
}
