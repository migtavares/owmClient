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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherData {
	private static final String JSON_ID        = "id";
	private static final String JSON_NAME      = "name";
	private static final String JSON_DATE_TIME = "dt";
	private static final String JSON_COORD     = "coord";
	private static final String JSON_MAIN      = "main";
	private static final String JSON_WIND      = "wind";
	private static final String JSON_CLOUDS    = "clouds";
	private static final String JSON_RAIN      = "rain";
	private static final String JSON_SNOW      = "snow";
	private static final String JSON_WEATHER   = "weather";

	public static class GeoCoord {
		private static final String JSON_LAT = "lat";
		private static final String JSON_LON = "lon";

		private float latitude;
		private float longitude;

		public GeoCoord (JSONObject json) throws JSONException {
			this.latitude = (float) json.getDouble (GeoCoord.JSON_LAT);
			this.longitude = (float) json.getDouble (GeoCoord.JSON_LON);
		}

		public float getLatitude () {
			return latitude;
		}

		public float getLongitude () {
			return longitude;
		}
	}

	private static class NullibleData {
		private long setFieldFlags = 0;

		protected void setFieldFlag (byte bitToSet) {
			this.setFieldFlags |= ((long) 1) << bitToSet;
		}
		protected boolean checkFieldFlag (byte bitToCheck) {
			return (this.setFieldFlags & ((long) 1) << bitToCheck) != 0;
		}
	}

	public static class Main extends NullibleData {
		private static final String JSON_TEMP     = "temp";
		private static final String JSON_TEMP_MIN = "temp_min";
		private static final String JSON_TEMP_MAX = "temp_max";
		private static final String JSON_HUMIDITY = "humidity";
		private static final String JSON_PRESSURE = "pressure";

		static final private byte TEMP     = 1;
		static final private byte TEMP_MIN = 2;
		static final private byte TEMP_MAX = 3;
		static final private byte PRESSURE = 4;
		static final private byte HUMIDITY = 5;
		
		private float temp;
		private float tempMin;
		private float tempMax;
		private float pressure;
		private float humidity;

		public Main (JSONObject json) throws JSONException {
			if (json.has (Main.JSON_TEMP))
				setTemp ((float) json.getDouble (Main.JSON_TEMP));
			if (json.has (Main.JSON_TEMP_MIN))
				setTempMin ((float) json.getDouble (Main.JSON_TEMP_MIN));
			if (json.has (Main.JSON_TEMP_MAX))
				setTempMax ((float) json.getDouble (Main.JSON_TEMP_MAX));
			if (json.has (Main.JSON_HUMIDITY))
				setHumidity ((float) json.getDouble (Main.JSON_HUMIDITY));
			if (json.has (Main.JSON_PRESSURE))
				setPressure ((float) json.getDouble (Main.JSON_PRESSURE));
		}

		private void setTemp (float temp) {
			setFieldFlag (Main.TEMP);
			this.temp = temp;
		}
		public boolean hasTemp () {
			return checkFieldFlag (Main.TEMP);
		}
		public float getTemp () {
			return this.temp;
		}

		private void setTempMin (float tempMin) {
			setFieldFlag (Main.TEMP_MIN);
			this.tempMin = tempMin;
		}
		public boolean hasTempMin () {
			return checkFieldFlag (Main.TEMP_MIN);
		}
		public float getTempMin () {
			return this.tempMin;
		}

		private void setTempMax (float tempMax) {
			setFieldFlag (Main.TEMP_MAX);
			this.tempMax = tempMax;
		}
		public boolean hasTempMax () {
			return checkFieldFlag (Main.TEMP_MAX);
		}
		public float getTempMax () {
			return this.tempMax;
		}

		private void setPressure (float pressure) {
			setFieldFlag (Main.PRESSURE);
			this.pressure = pressure;
		}
		public boolean hasPressure () {
			return checkFieldFlag (Main.PRESSURE);
		}
		public float getPressure () {
			return this.pressure;
		}

		private void setHumidity (float humidity) {
			setFieldFlag (Main.HUMIDITY);
			this.humidity = humidity;
		}
		public boolean hasHumidity () {
			return checkFieldFlag (Main.HUMIDITY);
		}
		public float getHumidity () {
			return this.humidity;
		}
	}

	public static class Wind extends NullibleData {
		private static final String JSON_SPEED   = "speed";
		private static final String JSON_DEG     = "deg";
		private static final String JSON_GUST    = "gust";
		private static final String JSON_VAR_BEG = "var_beg";
		private static final String JSON_VAR_END = "var_end";
		
		private static final byte SPEED   = 1;
		private static final byte DEG     = 2;
		private static final byte GUST    = 3;
		private static final byte VAR_BEG = 4;
		private static final byte VAR_END = 5;

		private float speed;
		private int deg;
		private float gust;
		private int varBeg;
		private int varEnd;

		public Wind (JSONObject json) throws JSONException {
			if (json.has (Wind.JSON_SPEED))
				setSpeed ((float) json.getDouble (Wind.JSON_SPEED));
			if (json.has (Wind.JSON_DEG))
				setDeg (json.getInt (Wind.JSON_DEG));
			if (json.has (Wind.JSON_GUST))
				setGust ((float) json.getDouble (Wind.JSON_GUST));
			if (json.has (Wind.JSON_VAR_BEG))
				setVarBeg (json.getInt (Wind.JSON_VAR_BEG));
			if (json.has (Wind.JSON_VAR_END))
				setVarEnd (json.getInt (Wind.JSON_VAR_END));
			
		}

		private void setSpeed (float speed) {
			super.setFieldFlag (SPEED);
			this.speed = speed;
		}
		public boolean hasSpeed () {
			return checkFieldFlag (SPEED);
		}
		public float getSpeed () {
			return this.speed;
		}

		private void setDeg (int deg) {
			super.setFieldFlag (DEG);
			this.deg = deg;
		}
		public boolean hasDeg () {
			return checkFieldFlag (DEG);
		}
		public int getDeg () {
			return this.deg;
		}

		private void setGust (float gust) {
			super.setFieldFlag (GUST);
			this.gust = gust;
		}
		public boolean hasGust () {
			return checkFieldFlag (GUST);
		}
		public float getGust () {
			return this.gust;
		}

		private void setVarBeg (int dir) {
			super.setFieldFlag (VAR_BEG);
			this.varBeg = dir;
		}
		public boolean hasVarBeg () {
			return checkFieldFlag (VAR_BEG);
		}
		public int getVarBeg () {
			return this.varBeg;
		}

		private void setVarEnd (int dir) {
			super.setFieldFlag (VAR_END);
			this.varEnd = dir;
		}
		public boolean hasVarEnd () {
			return checkFieldFlag (VAR_END);
		}
		public int getVarEnd () {
			return this.varEnd;
		}
	}

	private static class Details {
		private Map<Integer, Float> measurements = null;

		public Details () {
		}

		public Details (JSONObject json) {
			// TODO
		}

		public boolean hasMeasures ()  {
			return this.measurements != null && this.measurements.size () > 0;
		}
		public void putMeasure (int lastHours, float value) {
			if (this.measurements == null)
				this.measurements = new HashMap<Integer, Float> ();
			this.measurements.put (Integer.valueOf (lastHours), Float.valueOf (value));
		}
		public float getMeasure (int lastHours) {
			Float value = this.measurements.get (Integer.valueOf (lastHours));
			return value != null? value.floatValue () : Float.NaN;
		}
		public float getMeasure (Integer lastHours) {
			Float value = this.measurements.get (lastHours);
			return value != null? value.floatValue () : Float.NaN;
		}
		public Set<Integer> measurements () {
			if (this.measurements == null)
				return Collections.emptySet ();
			return this.measurements.keySet ();
		}
	}

	public static class Clouds extends Details {
		private static final String JSON_ALL = "all";

		public static class CloudDescription {
			private static final String JSON_DISTANCE = "distance";
			private static final String JSON_CONDITION = "condition";
			private static final String JSON_CUMULUS = "cumulus";

			public static enum SkyCondition {
				UNKNOWN ("unknown"),
				FEW ("few [12.5%, 25%]"),
				SCT ("scattered [37.5%, 50%]"),
				BKN ("broken sky [62%, 87.5%]"),
				OVC ("overcast {100%}"),
				VV ("vertical visibility");

				private final String description;

				private SkyCondition (String description) {
					this.description = description;
				}

				public String getDescription () {
					return this.description;
				}
			}

			public static enum Cumulus {
				UNKNOWN ("unknown"),
				TCU ("towering cumulus"),
				CB ("cumulonimbus"),
				ACC ("altocumulus castellanus");

				private final String description;

				private Cumulus (String description) {
					this.description = description;
				}

				public String getDescription () {
					return this.description;
				}
			}

			private SkyCondition skyCondition = null;
			private Cumulus cumulus = null;
			private int distance = Integer.MIN_VALUE;

			public CloudDescription (JSONObject json) throws JSONException {
				if (json.has (CloudDescription.JSON_DISTANCE))
					this.distance = json.getInt (CloudDescription.JSON_DISTANCE);
				if (json.has (CloudDescription.JSON_CONDITION)) {
					try {
						this.skyCondition = SkyCondition.valueOf (json.getString (CloudDescription.JSON_CONDITION));
					} catch (IllegalArgumentException e) {
						this.skyCondition = SkyCondition.UNKNOWN;
					}
				}
				if (json.has (CloudDescription.JSON_CUMULUS)) {
					try {
						this.cumulus = Cumulus.valueOf (json.getString (CloudDescription.JSON_CUMULUS));
					} catch (IllegalArgumentException e) {
						this.cumulus = Cumulus.UNKNOWN;
					}
				}
			}

			public boolean hasDistance () {
				return this.distance != Integer.MIN_VALUE;
			}
			public int getDistance () {
				return this.distance;
			}

			public boolean hasSkyCondition () {
				return this.skyCondition != null;
			}
			public SkyCondition getSkyCondition () {
				return this.skyCondition;
			}

			public boolean hasCumulus () {
				return this.cumulus != null;
			}
			public Cumulus getCumulus () {
				return this.cumulus;
			}
		}

		private int all = Integer.MIN_VALUE;
		List<CloudDescription> conditions = null;

		public Clouds (JSONObject json) throws JSONException {
			super (json);
			if (json.has (Clouds.JSON_ALL))
				this.all = json.getInt (Clouds.JSON_ALL);
		}

		public Clouds (JSONArray jsonArray) throws JSONException {
			this.conditions = new ArrayList<CloudDescription> (jsonArray.length ());
			for (int i = 0; i < jsonArray.length (); i++) {
				this.conditions.add (
						new CloudDescription (jsonArray.getJSONObject (i)));
			}
		}

		public boolean hasAll () {
			return this.all != Integer.MIN_VALUE;
		}
		public int getAll () {
			return this.all;
		}

		public boolean hasConditions () {
			return this.conditions != null;
		}
		public List<CloudDescription> getConditions () {
			if (this.conditions != null)
				return this.conditions;
			else
				return Collections.emptyList ();
		}
	}

	public static class Precipitation extends Details {
		private static final String JSON_TODAY = "today";
		private int today = Integer.MIN_VALUE;
		
		public Precipitation (JSONObject json) throws JSONException {
			super (json);
			if (json.has (Precipitation.JSON_TODAY))
				this.today = json.getInt (Precipitation.JSON_TODAY);
		}

		public boolean hasToday () {
			return this.today != Integer.MIN_VALUE;
		}
		public int getToday () {
			return this.today;
		}
	}

	public static class WeatherCondition {
		public static enum ConditionCode {
			UNKNOWN                         (Integer.MIN_VALUE),
			/* Thunderstorm */
			THUNDERSTORM_WITH_LIGHT_RAIN    (200),
			THUNDERSTORM_WITH_RAIN          (201),
			THUNDERSTORM_WITH_HEAVY_RAIN    (202),
			LIGHT_THUNDERSTORM              (210),
			THUNDERSTORM                    (211),
			HEAVY_THUNDERSTORM              (212),
			RAGGED_THUNDERSTORM             (221),
			THUNDERSTORM_WITH_LIGHT_DRIZZLE (230),
			THUNDERSTORM_WITH_DRIZZLE       (231),
			THUNDERSTORM_WITH_HEAVY_DRIZZLE (232),
			/* Drizzle */
			LIGHT_INTENSITY_DRIZZLE         (300),
			DRIZZLE                         (301),
			HEAVY_INTENSITY_DRIZZLE         (302),
			LIGHT_INTENSITY_DRIZZLE_RAIN    (310),
			DRIZZLE_RAIN                    (311),
			HEAVY_INTENSITY_DRIZZLE_RAIN    (312),
			SHOWER_DRIZZLE                  (321),
			/* Rain */
			LIGHT_RAIN                      (500),
			MODERATE_RAIN                   (501),
			HEAVY_INTENSITY_RAIN            (502),
			VERY_HEAVY_RAIN                 (503),
			EXTREME_RAIN                    (504),
			FREEZING_RAIN                   (511),
			LIGHT_INTENSITY_SHOWER_RAIN     (520),
			SHOWER_RAIN                     (521),
			HEAVY_INTENSITY_SHOWER_RAIN     (522),
			/* Snow */
			LIGHT_SNOW                      (600),
			SNOW                            (601),
			HEAVY_SNOW                      (602),
			SLEET                           (611),
			SHOWER_SNOW                     (621),
			/* Atmosphere */
			MIST                            (701),
			SMOKE                           (711),
			HAZE                            (721),
			SAND_OR_DUST_WHIRLS             (731),
			FOG                             (741),
			/* Clouds */
			SKY_IS_CLEAR                    (800),
			FEW_CLOUDS                      (801),
			SCATTERED_CLOUDS                (802),
			BROKEN_CLOUDS                   (803),
			OVERCAST_CLOUDS                 (804),
			/* Extreme */
			TORNADO                         (900),
			TROPICAL_STORM                  (901),
			HURRICANE                       (902),
			COLD                            (903),
			HOT                             (904),
			WINDY                           (905),
			HAIL                            (906);

			private int id;
			private ConditionCode (int code) {
				this.id = code;
			}

			static public ConditionCode valueof (int id) {
				for (ConditionCode condition : ConditionCode.values ()) {
					if (condition.id == id)
						return condition;
				}
				return ConditionCode.UNKNOWN;
			}

			public int getId () {
				return this.id;
			}
		}
		
		private static final String JSON_ID = "id";
		private static final String JSON_MAIN = "main";
		private static final String JSON_DESCRIPTION = "description";
		private static final String JSON_ICON = "icon";

		private ConditionCode code = null;
		private String main = null;
		private String description = null;
		private String iconName = null;

		public WeatherCondition (JSONObject json) throws JSONException {
			this.code = ConditionCode.valueof (
					json.getInt (WeatherCondition.JSON_ID));
			if (json.has (WeatherCondition.JSON_MAIN))
				this.main = json.getString (WeatherCondition.JSON_MAIN);
			if (json.has (WeatherCondition.JSON_DESCRIPTION))
				this.description = json.getString (WeatherCondition.JSON_DESCRIPTION);
			if (json.has (WeatherCondition.JSON_ICON))
				this.iconName = json.getString (WeatherCondition.JSON_ICON);
		}

		public ConditionCode getCode () {
			return this.code;
		}

		public boolean hasMain () {
			return this.main != null;
		}
		public String getMain () {
			return this.main;
		}

		public boolean hasDescription () {
			return this.description != null;
		}
		public String getDescription () {
			return this.description;
		}

		public boolean hasIconName () {
			return this.iconName != null;
		}
		public String getIconName () {
			return this.iconName;
		}
	}

	private long id = Long.MIN_VALUE;
	private String name = null;
	private long dateTime = Long.MIN_VALUE;

	private GeoCoord coord = null;
	private Main main = null;
	private Wind wind = null;
	private Clouds clouds = null;
	private Precipitation rain = null;
	private Precipitation snow = null;
	private List<WeatherCondition> weatherConditions = null;

	public WeatherData (JSONObject json) throws JSONException {
		this.id = json.getLong (WeatherData.JSON_ID);
		this.name = json.getString (WeatherData.JSON_NAME);
		this.dateTime = json.getLong (WeatherData.JSON_DATE_TIME);
		if (json.has (WeatherData.JSON_COORD))
			this.coord = new GeoCoord (json.getJSONObject (WeatherData.JSON_COORD));
		if (json.has (WeatherData.JSON_MAIN))
			this.main = new Main (json.getJSONObject (WeatherData.JSON_MAIN));
		if (json.has (WeatherData.JSON_WIND))
			this.wind = new Wind (json.getJSONObject (WeatherData.JSON_WIND));
		if (json.has (WeatherData.JSON_CLOUDS)) {
			JSONArray coudsArray = json.optJSONArray (WeatherData.JSON_CLOUDS);
			if (coudsArray != null)
				this.clouds = new Clouds (coudsArray);
			else {
				JSONObject cloudsObj = json.optJSONObject (WeatherData.JSON_CLOUDS);
				if (cloudsObj != null)
					this.clouds = new Clouds (json.getJSONObject (WeatherData.JSON_CLOUDS));
			}
		}
		if (json.has (WeatherData.JSON_RAIN))
			this.rain = new Precipitation (json.getJSONObject (WeatherData.JSON_RAIN));
		if (json.has (WeatherData.JSON_SNOW))
			this.snow = new Precipitation (json.getJSONObject (WeatherData.JSON_SNOW));
		if (json.has (WeatherData.JSON_WEATHER)) {
			this.weatherConditions = new ArrayList<WeatherCondition> ();
			JSONArray jsonConditions = json.getJSONArray (WeatherData.JSON_WEATHER);
			if (jsonConditions != null) {
				for (int i = 0; i < jsonConditions.length (); i++) {
					this.weatherConditions.add (
							new WeatherCondition (
									jsonConditions.getJSONObject (i)));
				}
			}
		}
	}

	public long getId () {
		return this.id;
	}

	public String getName () {
		return this.name;
	}

	public long getDateTime () {
		return this.dateTime;
	}

	public boolean hasCoord () {
		return this.coord != null;
	}
	public GeoCoord getCoord () {
		return this.coord;
	}

	public boolean hasMain () {
		return this.main != null;
	}
	public Main getMain () {
		return this.main;
	}

	public boolean hasWind () {
		return this.wind != null;
	}
	public Wind getWind () {
		return this.wind;
	}

	public boolean hasClouds () {
		return this.clouds != null;
	}
	public Clouds getClouds () {
		return this.clouds;
	}

	public boolean hasRain () {
		return this.rain != null;
	}
	public Precipitation getRain () {
		return this.rain;
	}

	public boolean hasSnow () {
		return this.snow != null;
	}
	public Precipitation getSnow () {
		return this.snow;
	}

	public boolean hasWeatherConditions () {
		return this.weatherConditions != null;
	}
	public List<WeatherCondition> getWeatherConditions () {
		return this.weatherConditions;
	}
}