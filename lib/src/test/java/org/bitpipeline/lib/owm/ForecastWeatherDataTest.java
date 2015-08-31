/**
 * Copyright 2015 J. Miguel P. Tavares
 * Copyright 2015 Cesar Aguilera
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for {@link ForecastWeatherData}
 * 
 * @author cs4r
 */
@RunWith (MockitoJUnitRunner.class)
public class ForecastWeatherDataTest {

	private static final String DATETIME_KEY_NAME = "dt";
	private static final long DESIRED_DATETIME = 123456;

	@Mock
	private JSONObject jsonObject;

	@Test
	public void testGetCalcDateTimeWhenDateTimeIsPresentReturns123456 () throws JSONException {
		when (jsonObject.optLong (DATETIME_KEY_NAME, Long.MIN_VALUE)).thenReturn (DESIRED_DATETIME);
		assertEquals (DESIRED_DATETIME, getDateTimeFromForecastWeatherData ());
	}

	@Test
	public void testGetCalcDateTimeWhenDateTimeIsNotPresentReturnsLongMinValue () {
		when (jsonObject.optLong (DATETIME_KEY_NAME, Long.MIN_VALUE)).thenReturn (Long.MIN_VALUE);
		assertEquals (Long.MIN_VALUE, getDateTimeFromForecastWeatherData ());
	}

	private long getDateTimeFromForecastWeatherData () {
		return new ForecastWeatherData (jsonObject).getCalcDateTime ();
	}

}
