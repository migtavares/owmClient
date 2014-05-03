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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherData extends AbstractWeatherData {
	private static final String JSON_CLOUDS    = "clouds";
	private static final String JSON_WEATHER   = "weather";

	public static class Main extends AbstractWeatherData.Main {
		private final float temp;
		private final float tempMin;
		private final float tempMax;
		private final float pressure;
		private final float humidity;

		public Main (JSONObject json) {
			this.temp = (float) json.optDouble (Main.JSON_TEMP);
			this.tempMin = (float) json.optDouble (Main.JSON_TEMP_MIN);
			this.tempMax = (float) json.optDouble (Main.JSON_TEMP_MAX);
			this.pressure = (float) json.optDouble (Main.JSON_PRESSURE);
			this.humidity = (float) json.optDouble (Main.JSON_HUMIDITY);
		}

		public boolean hasTemp () {
			return !Float.isNaN (this.temp);
		}
		public float getTemp () {
			return this.temp;
		}

		public boolean hasTempMin () {
			return !Float.isNaN (this.tempMin);
		}
		public float getTempMin () {
			return this.tempMin;
		}

		public boolean hasTempMax () {
			return !Float.isNaN (this.tempMax);
		}
		public float getTempMax () {
			return this.tempMax;
		}

		public boolean hasPressure () {
			return !Float.isNaN (this.pressure);
		}
		public float getPressure () {
			return this.pressure;
		}

		public boolean hasHumidity () {
			return !Float.isNaN (this.humidity);
		}
		public float getHumidity () {
			return this.humidity;
		}
	}

	public static class Wind extends AbstractWeatherData.Wind {
		private final float speed;
		private final int deg;
		private final float gust;
		private final int varBeg;
		private final int varEnd;

		public Wind (JSONObject json) {
			this.speed = (float) json.optDouble (Wind.JSON_SPEED);
			this.deg = json.optInt (Wind.JSON_DEG, Integer.MIN_VALUE);
			this.gust = (float) json.optDouble (Wind.JSON_GUST);
			this.varBeg = json.optInt (Wind.JSON_VAR_BEG, Integer.MIN_VALUE);
			this.varEnd = json.optInt (Wind.JSON_VAR_END, Integer.MIN_VALUE);
		}

		public boolean hasSpeed () {
			return !Float.isNaN (this.speed);
		}
		public float getSpeed () {
			return this.speed;
		}

		public boolean hasDeg () {
			return this.deg != Integer.MIN_VALUE;
		}
		public int getDeg () {
			return this.deg;
		}

		public boolean hasGust () {
			return !Float.isNaN (this.gust);
		}
		public float getGust () {
			return this.gust;
		}

		public boolean hasVarBeg () {
			return this.varBeg != Integer.MIN_VALUE;
		}
		public int getVarBeg () {
			return this.varBeg;
		}

		public boolean hasVarEnd () {
			return this.varEnd != Integer.MIN_VALUE;
		}
		public int getVarEnd () {
			return this.varEnd;
		}
	}

	private static class TimedDetails {
		private Map<Integer, Integer> measurements = null;

		TimedDetails () {
		}

		TimedDetails (JSONObject json) {
			for (int i=1; i<=24; i++) {
				String value = json.optString (String.format (Locale.ROOT, "%dh", i));
				if (value.length () > 0) {
					try {
						putMeasure (i, Integer.valueOf (value));
					} catch (NumberFormatException nfe) {
						continue;
					}
				}
			}
		}

		public boolean hasMeasures ()  {
			return this.measurements != null && this.measurements.size () > 0;
		}
		private void putMeasure (int lastHours, Integer value) {
			if (this.measurements == null)
				this.measurements = new HashMap<Integer, Integer> ();
			this.measurements.put (Integer.valueOf (lastHours), value);
		}
		public int getMeasure (int lastHours) {
			return getMeasure (Integer.valueOf (lastHours));
		}
		public int getMeasure (Integer lastHours) {
			if (this.measurements == null)
				return Integer.MIN_VALUE;
			Integer value = this.measurements.get (lastHours);
			return value != null? value.intValue () : Integer.MIN_VALUE;
		}
		public Set<Integer> measurements () {
			if (this.measurements == null)
				return Collections.emptySet ();
			return this.measurements.keySet ();
		}
	}

	public static class Clouds extends TimedDetails {
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
			private final int distance;

			public CloudDescription (JSONObject json) {
				this.distance = json.optInt (CloudDescription.JSON_DISTANCE, Integer.MIN_VALUE);
				if (json.has (CloudDescription.JSON_CONDITION)) {
					try {
						this.skyCondition = SkyCondition.valueOf (json.optString (CloudDescription.JSON_CONDITION));
					} catch (IllegalArgumentException e) {
						this.skyCondition = SkyCondition.UNKNOWN;
					}
				}
				if (json.has (CloudDescription.JSON_CUMULUS)) {
					try {
						this.cumulus = Cumulus.valueOf (json.optString (CloudDescription.JSON_CUMULUS));
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

		private final int all;
		private final List<CloudDescription> conditions;

		public Clouds (JSONObject json) {
			super (json);
			this.all = json.optInt (Clouds.JSON_ALL, Integer.MIN_VALUE);
			this.conditions = Collections.emptyList ();
		}

		public Clouds (JSONArray jsonArray) {
			this.all = Integer.MIN_VALUE;
			this.conditions = new ArrayList<CloudDescription> (jsonArray.length ());
			for (int i = 0; i < jsonArray.length (); i++) {
				JSONObject jsonCloudDescription = jsonArray.optJSONObject (i);
				if (jsonCloudDescription != null)
					this.conditions.add (
							new CloudDescription (jsonCloudDescription));
			}
		}

		public boolean hasAll () {
			return this.all != Integer.MIN_VALUE;
		}
		public int getAll () {
			return this.all;
		}

		public boolean hasConditions () {
			return this.conditions != null && !this.conditions.isEmpty ();
		}
		public List<CloudDescription> getConditions () {
			return this.conditions;
		}
	}

	public static class Precipitation extends TimedDetails {
		private static final String JSON_TODAY = "today";
		private final int today;
		
		public Precipitation (JSONObject json) {
			super (json);
			this.today = json.optInt (Precipitation.JSON_TODAY, Integer.MIN_VALUE);
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

		public WeatherCondition (JSONObject json) {
			this.code = ConditionCode.valueof (json.optInt (WeatherCondition.JSON_ID, Integer.MIN_VALUE));
			this.main = json.optString (WeatherCondition.JSON_MAIN);
			this.description = json.optString (WeatherCondition.JSON_DESCRIPTION);
			this.iconName = json.optString (WeatherCondition.JSON_ICON);
		}

		public ConditionCode getCode () {
			return this.code;
		}

		public boolean hasMain () {
			return this.main != null && this.main.length () > 0;
		}
		public String getMain () {
			return this.main;
		}

		public boolean hasDescription () {
			return this.description != null && this.description.length () > 0;
		}
		public String getDescription () {
			return this.description;
		}

		public boolean hasIconName () {
			return this.iconName != null && this.iconName.length () > 0;
		}
		public String getIconName () {
			return this.iconName;
		}
	}

	
	private final Main main;
	private final Wind wind;
	private final Clouds clouds;
	private final Precipitation rain;
	private final Precipitation snow;
	private final List<WeatherCondition> weatherConditions;

	public WeatherData (JSONObject json) {
		super (json);

		JSONObject jsonMain = json.optJSONObject (WeatherData.JSON_MAIN);
		this.main = jsonMain != null ? new Main (jsonMain) : null;

		JSONObject jsonWind = json.optJSONObject (WeatherData.JSON_WIND);
		this.wind = jsonWind != null ? new Wind (jsonWind) : null;

		if (json.has (WeatherData.JSON_CLOUDS)) {
			JSONArray coudsArray = json.optJSONArray (WeatherData.JSON_CLOUDS);
			if (coudsArray != null)
				this.clouds = new Clouds (coudsArray);
			else {
				JSONObject cloudsObj = json.optJSONObject (WeatherData.JSON_CLOUDS);
				if (cloudsObj != null)
					this.clouds = new Clouds (cloudsObj);
				else
					this.clouds = null;
			}
		} else
			this.clouds = null;

		JSONObject jsonRain = json.optJSONObject (WeatherData.JSON_RAIN);
		this.rain = jsonRain != null ? new Precipitation (jsonRain) : null;

		JSONObject jsonSnow = json.optJSONObject (WeatherData.JSON_SNOW);
		this.snow = jsonSnow != null ? new Precipitation (jsonSnow) : null;

		if (json.has (WeatherData.JSON_WEATHER)) {
			JSONArray jsonConditions = json.optJSONArray (WeatherData.JSON_WEATHER);
			if (jsonConditions != null) {
				this.weatherConditions = new ArrayList<WeatherCondition> (jsonConditions.length ());
				if (jsonConditions != null) {
					for (int i = 0; i < jsonConditions.length (); i++) {
						JSONObject jsonCondition = jsonConditions.optJSONObject (i);
						if (jsonCondition != null)
							this.weatherConditions.add (
									new WeatherCondition (jsonCondition));
					}
				}
			} else {
				this.weatherConditions = Collections.emptyList ();
			}
		} else {
			this.weatherConditions = Collections.emptyList ();
		}
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
	public Precipitation getRainObj () {
		return this.rain;
	}

	public boolean hasSnow () {
		return this.snow != null;
	}
	public Precipitation getSnowObj () {
		return this.snow;
	}

	public boolean hasWeatherConditions () {
		return this.weatherConditions != null && !this.weatherConditions.isEmpty ();
	}
	public List<WeatherCondition> getWeatherConditions () {
		return this.weatherConditions;
	}

	/* */

	public float getTemp () {
		if (hasMain () && this.main.hasTemp ())
			return this.main.getTemp ();
		return Float.NaN;
	}

	public float getHumidity () {
		if (hasMain () && this.main.hasHumidity ())
			return this.main.getHumidity ();
		return Float.NaN;
	}

	public float getPressure () {
		if (hasMain () && this.main.hasPressure ())
			return this.main.getPressure ();
		return Float.NaN;
	}

	public float getWindSpeed () {
		if (hasWind () && this.wind.hasSpeed ())
			return this.wind.getSpeed ();
		return Float.NaN;
	}

	public float getWindGust () {
		if (hasWind () && this.wind.hasGust ())
			return this.wind.getGust ();
		return Float.NaN;
	}

	public int getWindDeg () {
		if (hasWind () && this.wind.hasDeg ())
			return this.wind.getDeg ();
		return Integer.MIN_VALUE;
	}

	public int getRain () {
		if (!hasRain ())
			return Integer.MIN_VALUE;
		int measure = this.rain.getMeasure (1);
		if (measure != Integer.MIN_VALUE)
			return measure;
		return this.rain.getToday ();
	}

	public int getSnow () {
		if (!hasSnow ())
			return Integer.MIN_VALUE;
		int measure = this.snow.getMeasure (1);
		if (measure != Integer.MIN_VALUE)
			return measure;
		return this.snow.getToday ();
	}

	public int getPrecipitation () {
		int precipitation = Integer.MIN_VALUE;
		if (hasRain ())
			precipitation = getRain ();
		if (hasSnow ())
			precipitation = precipitation != Integer.MIN_VALUE ? precipitation + getSnow () : getSnow (); 
		return precipitation;
	}
}