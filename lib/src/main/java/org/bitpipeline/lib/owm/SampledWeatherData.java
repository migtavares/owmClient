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

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

public class SampledWeatherData extends AbstractWeatherData {
	private static final String JSON_PRECIPITATION   = "precipitation";
	private static final String JSON_PRECIPITATION_V = "v";

	static abstract class SampledValue {
		protected static final String JSON_VALUE = "v";
		protected static final String JSON_COUNT = "c";
		protected static final String JSON_MIN   = "mi";
		protected static final String JSON_MAX   = "ma";

		private final int count;

		protected SampledValue (JSONObject json) {
			this.count = json.optInt (SampledFValue.JSON_COUNT, Integer.MIN_VALUE);
		}

		public boolean hasCount () {
			return this.count != Integer.MIN_VALUE;
		}
		public int getCount () {
			return this.count;
		}

		abstract public boolean hasValue ();

		static public boolean hasSampledValue (SampledValue sampled) {
			return sampled != null && sampled.hasValue ();
		}
	}

	static class SampledFValue extends SampledValue {
		private final float value;
		private final float min;
		private final float max;

		public SampledFValue (JSONObject json) {
			super (json);
			this.value = (float) json.optDouble (SampledFValue.JSON_VALUE, Float.NaN);
			this.min = (float) json.optDouble (SampledFValue.JSON_MIN, Float.NaN);
			this.max = (float) json.optDouble (SampledFValue.JSON_MAX, Float.NaN);
		}

		public boolean hasValue () {
			return !Float.isNaN (this.value);
		}
		public float getValue () {
			return this.value;
		}

		public boolean hasMin () {
			return !Float.isNaN (this.min);
		}
		public float getMin () {
			return this.min;
		}

		public boolean hasMax () {
			return !Float.isNaN (this.max);
		}
		public float getMax () {
			return this.max;
		}

		static public SampledFValue createSampledFValueIfExisting (final JSONObject json, final String key) {
			JSONObject jsonSampledValue = json.optJSONObject (key);
			if (jsonSampledValue == null)
				return null;
			return new SampledFValue (jsonSampledValue);
		}

		static public float getSampledValue (SampledFValue sampled) {
			if (sampled != null && sampled.hasValue ())
				return sampled.getValue ();
			return Float.NaN;
		}
	}

	static class SampledIValue extends SampledValue {
		private final int value;
		private final int min;
		private final int max;

		public SampledIValue (JSONObject json) {
			super (json);
			this.value = json.optInt (SampledIValue.JSON_VALUE, Integer.MIN_VALUE);
			this.min = json.optInt (SampledIValue.JSON_MIN, Integer.MIN_VALUE);
			this.max = json.optInt (SampledIValue.JSON_MAX, Integer.MIN_VALUE);
		}

		public boolean hasValue () {
			return this.value != Integer.MIN_VALUE;
		}
		public int getValue () {
			return this.value;
		}

		public boolean hasMin () {
			return this.min != Integer.MIN_VALUE;
		}
		public int getMin () {
			return this.min;
		}

		public boolean hasMax () {
			return this.max != Integer.MIN_VALUE;
		}
		public int getMax () {
			return this.max;
		}

		static public SampledIValue createSampledIValueIfExisting (final JSONObject json, final String key) {
			JSONObject jsonSampledValue = json.optJSONObject (key);
			if (jsonSampledValue == null)
				return null;
			return new SampledIValue (jsonSampledValue);
		}

		static public int getSampledValue (SampledIValue sampled) {
			if (sampled != null && sampled.hasValue ())
				return sampled.getValue ();
			return Integer.MIN_VALUE;
		}
	}

	public static class Main extends AbstractWeatherData.Main{
		private final SampledFValue temp;
		private final SampledFValue tempMin;
		private final SampledFValue tempMax;
		private final SampledFValue pressure;
		private final SampledFValue humidity;

		public Main (JSONObject json) {
			this.temp = SampledFValue.createSampledFValueIfExisting (json, Main.JSON_TEMP);
			this.tempMin = SampledFValue.createSampledFValueIfExisting (json, Main.JSON_TEMP_MIN);
			this.tempMax = SampledFValue.createSampledFValueIfExisting (json, Main.JSON_TEMP_MAX);
			this.pressure = SampledFValue.createSampledFValueIfExisting (json, Main.JSON_PRESSURE);
			this.humidity = SampledFValue.createSampledFValueIfExisting (json, Main.JSON_HUMIDITY);
		}

		public boolean hasTemp () {
			return SampledValue.hasSampledValue (this.temp);
		}
		public float getTemp () {
			return SampledFValue.getSampledValue (this.temp);
		}
		public SampledFValue getSampledTemp () {
			return this.temp;
		}

		public boolean hasTempMin () {
			return SampledValue.hasSampledValue (this.tempMin);
		}
		public float getTempMin () {
			return SampledFValue.getSampledValue (this.tempMin);
		}
		public SampledFValue getSampledTempMin () {
			return this.tempMin;
		}

		public boolean hasTempMax () {
			return SampledValue.hasSampledValue (this.tempMax);
		}
		public float getTempMax () {
			return SampledFValue.getSampledValue (this.tempMax);
		}
		public SampledFValue getSampledTempMax () {
			return this.tempMax;
		}

		public boolean hasPressure () {
			return SampledValue.hasSampledValue (this.pressure);
		}
		public float getPressure () {
			return SampledFValue.getSampledValue (this.pressure);
		}
		public SampledFValue getSampledPressure () {
			return this.pressure;
		}

		public boolean hasHumidity () {
			return SampledValue.hasSampledValue (this.humidity);
		}
		public float getHumidity () {
			return SampledFValue.getSampledValue (this.humidity);
		}
		public SampledFValue getSampledHumidity () {
			return this.humidity;
		}
	}

	public static class Wind extends AbstractWeatherData.Wind {
		private final SampledFValue speed;
		private final SampledIValue deg;
		private final SampledFValue gust;
		private final SampledIValue varBeg;
		private final SampledIValue varEnd;

		public Wind (JSONObject json) {
			this.speed = SampledFValue.createSampledFValueIfExisting (json, Wind.JSON_SPEED);
			this.deg = SampledIValue.createSampledIValueIfExisting (json, Wind.JSON_DEG);
			this.gust = SampledFValue.createSampledFValueIfExisting (json, Wind.JSON_GUST);
			this.varBeg = SampledIValue.createSampledIValueIfExisting (json, Wind.JSON_VAR_BEG);
			this.varEnd = SampledIValue.createSampledIValueIfExisting (json, Wind.JSON_VAR_END);
		}

		public boolean hasSpeed () {
			return SampledValue.hasSampledValue (this.speed);
		}
		public float getSpeed () {
			return SampledFValue.getSampledValue (this.speed);
		}
		public SampledFValue getSampledSpeed () {
			return this.speed;
		}

		public boolean hasDeg () {
			return SampledValue.hasSampledValue (this.deg);
		}
		public int getDeg () {
			return SampledIValue.getSampledValue (this.deg);
		}
		public SampledIValue getSampledDeg () {
			return this.deg;
		}

		public boolean hasGust () {
			return SampledValue.hasSampledValue (this.gust);
		}
		public float getGust () {
			return SampledFValue.getSampledValue (this.gust);
		}
		public SampledFValue getSampledGust () {
			return this.gust;
		}

		public boolean hasVarBeg () {
			return SampledValue.hasSampledValue (this.varBeg);
		}
		public int getVarBeg () {
			return SampledIValue.getSampledValue (this.varBeg);
		}
		public SampledIValue getSampledVarBeg () {
			return this.varBeg;
		}

		public boolean hasVarEnd () {
			return SampledValue.hasSampledValue (this.varEnd);
		}
		public int getVarEnd () {
			return SampledIValue.getSampledValue (this.varEnd);
		}
		public SampledIValue getSampledVarEnd () {
			return this.varEnd;
		}
	}

	private static class SampledTimedDetails {
		private Map<Integer, SampledIValue> measurements = null;

		SampledTimedDetails (JSONObject json) {
			for (int i=1; i<=24; i++) {
				JSONObject value = json.optJSONObject (String.format (Locale.ROOT, "%dh", i));
				if (value != null) {
						putMeasure (i, new SampledIValue (value));
				}
			}
		}

		public boolean hasMeasures ()  {
			return this.measurements != null && this.measurements.size () > 0;
		}
		private void putMeasure (int lastHours, SampledIValue value) {
			if (this.measurements == null)
				this.measurements = new HashMap<Integer, SampledIValue> ();
			this.measurements.put (Integer.valueOf (lastHours), value);
		}

		public SampledIValue getMeasure (int lastHours) {
			return this.measurements.get (Integer.valueOf (lastHours));
		}
		public SampledIValue getMeasure (Integer lastHours) {
			return this.measurements.get (lastHours);
		}

		public Set<Integer> measurements () {
			if (this.measurements == null)
				return Collections.emptySet ();
			return this.measurements.keySet ();
		}
	}

	public static class Precipitation extends SampledTimedDetails {
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

	private final SampledFValue temp;
	private final SampledFValue pressure;
	private final SampledFValue humidity;
	private final Main main;
	private final Wind wind;
	private final Precipitation rain;
	private final Precipitation snow;
	private final SampledIValue precipitation;

	public SampledWeatherData (JSONObject json) {
		super (json);

		this.temp = SampledFValue.createSampledFValueIfExisting (json, Main.JSON_TEMP);
		this.pressure = SampledFValue.createSampledFValueIfExisting (json, Main.JSON_PRESSURE);
		this.humidity = SampledFValue.createSampledFValueIfExisting (json, Main.JSON_HUMIDITY);

		JSONObject jsonMain = json.optJSONObject (AbstractWeatherData.JSON_MAIN);
		this.main = jsonMain != null ? new Main (jsonMain) : null;

		JSONObject jsonWind = json.optJSONObject (AbstractWeatherData.JSON_WIND);
		this.wind = jsonWind != null ? new Wind (jsonWind) : null;

		JSONObject jsonRain = json.optJSONObject (AbstractWeatherData.JSON_RAIN);
		this.rain = jsonRain != null ? new Precipitation (jsonRain) : null;

		JSONObject jsonSnow = json.optJSONObject (AbstractWeatherData.JSON_SNOW);
		this.snow = jsonSnow != null ? new Precipitation (jsonSnow) : null;

		JSONObject jsonPrecipitation = json.optJSONObject (SampledWeatherData.JSON_PRECIPITATION);
		if (jsonPrecipitation != null) {
			JSONObject jsonPrecipitationValue = jsonPrecipitation.optJSONObject (SampledWeatherData.JSON_PRECIPITATION_V);
			this.precipitation = jsonPrecipitationValue != null ? new SampledIValue (jsonPrecipitationValue) : null;
		} else {
			this.precipitation = null;
		}
	}

	public boolean hasTemp () {
		return this.temp != null && this.temp.hasValue ()
				|| hasMain () && this.main.hasTemp ();
	}
	public float getTemp () {
		if (this.temp != null && this.temp.hasValue ())
			return this.temp.getValue ();
		if (hasMain () && this.main.hasTemp ())
			return this.main.getTemp ();
		return Float.NaN;
	}

	public boolean hasHumidity () {
		return this.humidity != null && this.humidity.hasValue ()
				|| hasMain () && this.main.hasHumidity ();
			
	}
	public float getHumidity () {
		if (this.humidity != null && this.humidity.hasValue ())
			return this.humidity.getValue ();
		if (hasMain () && this.main.hasHumidity ())
			return this.main.getHumidity ();
		return Float.NaN;
	}

	public boolean hasPressure () {
		return this.pressure != null && this.pressure.hasValue ()
				|| hasMain () && this.main.hasPressure ();
	}
	public float getPressure () {
		if (this.pressure != null && this.pressure.hasValue ())
			return this.pressure.getValue ();
		if (hasMain () && this.main.hasPressure ())
			return this.main.getPressure ();
		return Float.NaN;
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

	public boolean hasPrecipitationSample () {
		return this.precipitation != null;
	}
	public SampledIValue getPrecipitationObj () {
		return this.precipitation;
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

		SampledIValue lastHourRainReport = this.rain.getMeasure (1);
		if (lastHourRainReport != null && lastHourRainReport.hasValue ())
			return lastHourRainReport.getValue ();
		return this.rain.getToday ();
	}

	public int getSnow () {
		if (!hasSnow ())
			return Integer.MIN_VALUE;

		SampledIValue lastHourRainReport = this.rain.getMeasure (1);
		if (lastHourRainReport != null && lastHourRainReport.hasValue ())
			return lastHourRainReport.getValue ();
		return this.rain.getToday ();
	}

	public int getPrecipitation () {
		if (hasPrecipitationSample () && this.precipitation.hasValue ())
			return this.precipitation.getValue ();
		int precipitation = Integer.MIN_VALUE;
		if (hasRain ())
			precipitation = getRain ();
		if (hasSnow ())
			precipitation =  precipitation != Integer.MIN_VALUE ? precipitation + getSnow () : getSnow ();
		return precipitation;
	}
}