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

public class StatusWeatherData extends LocalizedWeatherData {
	private static final String JSON_ID      = "id";
	private static final String JSON_NAME    = "name";
	private static final String JSON_STATION = "station";

	static public class Station {
		private static final String JSON_ZOOM = "zoom";

		private final int zoom;

		public Station (JSONObject json) {
			this.zoom = json.optInt (Station.JSON_ZOOM, Integer.MIN_VALUE);
		}

		public boolean hasZoom () {
			return this.zoom != Integer.MIN_VALUE;
		}
		public int getZoom () {
			return this.zoom;
		}
	}
	private final long id;
	private final String name;
	private final Station station;

	public StatusWeatherData (JSONObject json) {
		super (json);
		this.id = json.optLong (StatusWeatherData.JSON_ID, Long.MIN_VALUE);
		this.name = json.optString (StatusWeatherData.JSON_NAME);
		JSONObject stationJson = json.optJSONObject (StatusWeatherData.JSON_STATION);
		this.station = stationJson != null ? new Station (stationJson) : null;
	}

	public boolean hasId () {
		return this.id != Long.MIN_VALUE;
	}
	public long getId () {
		return this.id;
	}

	public boolean hasName () {
		return this.name != null && this.name.length () > 0;
	}
	public String getName () {
		return this.name;
	}

	public boolean hasStation () {
		return this.station != null;
	}
	public Station getStation () {
		return this.station;
	}
}