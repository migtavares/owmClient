/**
 * Copyright 2012 
 *         J. Miguel P. Tavares <mtavares@bitpipeline.eu>
 *         BitPipeline
 */
package org.bitpipeline.lib.owm;

import java.util.List;


/** Implements a synchronous HTTP client to the Open Weather Map service described
 * in http://openweathermap.org/wiki/API/JSON_API
 * @author mtavares */
public class OwmClient {
	static enum Lang {
		RU, EN, DE, FR, ES, IT
	}

	/** Find current weather around a point*/
	public List<WeatherData> findStations (float lat, float lon, int cnt, boolean cluster, OwmClient.Lang lang) {
		return null;
	}
}
