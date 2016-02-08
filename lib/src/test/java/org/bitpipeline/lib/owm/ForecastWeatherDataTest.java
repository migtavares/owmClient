package org.bitpipeline.lib.owm;

import static org.junit.Assert.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ForecastWeatherData}
 * 
 * @author cs4r
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ForecastWeatherDataTest {

	private static final String DATETIME_KEY_NAME = "dt";
	private static final long DESIRED_DATETIME = 123456;

	@Mock
	private JSONObject jsonObject;

	@Test
	public void testGetCalcDateTimeWhenDateTimeIsPresentReturns123456()
			throws JSONException {
		when(jsonObject.optLong(DATETIME_KEY_NAME, Long.MIN_VALUE)).thenReturn(
				DESIRED_DATETIME);
		assertEquals(DESIRED_DATETIME, getDateTimeFromForecastWeatherData());
	}

	@Test
	public void testGetCalcDateTimeWhenDateTimeIsNotPresentReturnsLongMinValue() {
		when(jsonObject.optLong(DATETIME_KEY_NAME, Long.MIN_VALUE)).thenReturn(
				Long.MIN_VALUE);
		assertEquals(Long.MIN_VALUE, getDateTimeFromForecastWeatherData());
	}

	private long getDateTimeFromForecastWeatherData() {
		return new ForecastWeatherData(jsonObject).getCalcDateTime();
	}

}
