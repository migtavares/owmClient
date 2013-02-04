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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.json.JSONException;
import org.junit.Test;

/**
 * 
 * @author mtavares */
public class CurrentWeatherTest {
	static final String FIND_AROUND_POINT_RESPONSE_BODDY = "{\n" + 
			"    \"calctime\": \"\",\n" + 
			"    \"cnt\": 8,\n" + 
			"    \"cod\": \"200\",\n" + 
			"    \"list\": [\n" + 
			"        {\n" + 
			"            \"clouds\": [\n" + 
			"                {\n" + 
			"                    \"condition\": \"BKN\",\n" + 
			"                    \"distance\": 91\n" + 
			"                },\n" + 
			"                {\n" + 
			"                    \"condition\": \"OVC\",\n" + 
			"                    \"cumulus\": \"CB\",\n" + 
			"                    \"distance\": 305\n" + 
			"                }\n" + 
			"            ],\n" + 
			"            \"coord\": {\n" + 
			"                \"lat\": 55.5,\n" + 
			"                \"lon\": 37.5\n" + 
			"            },\n" + 
			"            \"distance\": 63.995,\n" + 
			"            \"dt\": 1359970200,\n" + 
			"            \"id\": 7325,\n" + 
			"            \"main\": {\"temp\": 273.15},\n" + 
			"            \"name\": \"UUMO\",\n" + 
			"            \"rang\": 50,\n" + 
			"            \"type\": 1,\n" + 
			"            \"wind\": {\n" + 
			"                \"deg\": 30,\n" + 
			"                \"speed\": 4\n" + 
			"            }\n" + 
			"        },\n" + 
			"        {\n" + 
			"            \"coord\": {\n" + 
			"                \"lat\": 55.5862,\n" + 
			"                \"lon\": 37.2445\n" + 
			"            },\n" + 
			"            \"distance\": 66.995,\n" + 
			"            \"dt\": 1359972020,\n" + 
			"            \"id\": 37653,\n" + 
			"            \"main\": {\n" + 
			"                \"humidity\": 93,\n" + 
			"                \"pressure\": 994,\n" + 
			"                \"temp\": 273.15\n" + 
			"            },\n" + 
			"            \"name\": \"UUWW\",\n" + 
			"            \"rang\": 0,\n" + 
			"            \"type\": 2,\n" + 
			"            \"wind\": {\n" + 
			"                \"deg\": 0,\n" + 
			"                \"speed\": 4.63\n" + 
			"            }\n" + 
			"        },\n" + 
			"        {\n" + 
			"            \"clouds\": [{\n" + 
			"                \"condition\": \"VV\",\n" + 
			"                \"distance\": 122\n" + 
			"            }],\n" + 
			"            \"coord\": {\n" + 
			"                \"lat\": 55.5915,\n" + 
			"                \"lon\": 37.2615\n" + 
			"            },\n" + 
			"            \"distance\": 67.823,\n" + 
			"            \"dt\": 1359970200,\n" + 
			"            \"id\": 7329,\n" + 
			"            \"main\": {\n" + 
			"                \"pressure\": 993,\n" + 
			"                \"temp\": 273.15\n" + 
			"            },\n" + 
			"            \"name\": \"UUWW\",\n" + 
			"            \"rang\": 50,\n" + 
			"            \"type\": 1,\n" + 
			"            \"wind\": {\n" + 
			"                \"deg\": 350,\n" + 
			"                \"speed\": 6\n" + 
			"            }\n" + 
			"        },\n" + 
			"        {\n" + 
			"            \"coord\": {\n" + 
			"                \"lat\": 55.4,\n" + 
			"                \"lon\": 37.9167\n" + 
			"            },\n" + 
			"            \"distance\": 73.228,\n" + 
			"            \"dt\": 1359972182,\n" + 
			"            \"id\": 37768,\n" + 
			"            \"main\": {\n" + 
			"                \"humidity\": 99,\n" + 
			"                \"pressure\": 993,\n" + 
			"                \"temp\": 273.15\n" + 
			"            },\n" + 
			"            \"name\": \"UUDD\",\n" + 
			"            \"rang\": 30,\n" + 
			"            \"type\": 2,\n" + 
			"            \"wind\": {\n" + 
			"                \"deg\": 0,\n" + 
			"                \"speed\": 2.06\n" + 
			"            }\n" + 
			"        },\n" + 
			"        {\n" + 
			"            \"clouds\": [{\n" + 
			"                \"condition\": \"VV\",\n" + 
			"                \"distance\": 91\n" + 
			"            }],\n" + 
			"            \"coord\": {\n" + 
			"                \"lat\": 55.4088,\n" + 
			"                \"lon\": 37.9063\n" + 
			"            },\n" + 
			"            \"distance\": 73.303,\n" + 
			"            \"dt\": 1359970200,\n" + 
			"            \"id\": 7322,\n" + 
			"            \"main\": {\n" + 
			"                \"pressure\": 992,\n" + 
			"                \"temp\": 273.15\n" + 
			"            },\n" + 
			"            \"name\": \"UUDD\",\n" + 
			"            \"rang\": 50,\n" + 
			"            \"type\": 1,\n" + 
			"            \"wind\": {\n" + 
			"                \"deg\": 20,\n" + 
			"                \"speed\": 5\n" + 
			"            }\n" + 
			"        },\n" + 
			"        {\n" + 
			"            \"coord\": {\n" + 
			"                \"lat\": 55.5522,\n" + 
			"                \"lon\": 37.7078\n" + 
			"            },\n" + 
			"            \"distance\": 76.026,\n" + 
			"            \"dt\": 1359972785,\n" + 
			"            \"id\": 43600,\n" + 
			"            \"main\": {\n" + 
			"                \"humidity\": 48,\n" + 
			"                \"pressure\": 972.2,\n" + 
			"                \"temp\": 298.25\n" + 
			"            },\n" + 
			"            \"name\": \"ankuch\",\n" + 
			"            \"rang\": 20,\n" + 
			"            \"type\": 5\n" + 
			"        },\n" + 
			"        {\n" + 
			"            \"coord\": {\n" + 
			"                \"lat\": 55.6567,\n" + 
			"                \"lon\": 37.5711\n" + 
			"            },\n" + 
			"            \"distance\": 81.468,\n" + 
			"            \"dt\": 1359972001,\n" + 
			"            \"id\": 45148,\n" + 
			"            \"main\": {\"temp\": 273.15},\n" + 
			"            \"name\": \"EvgeshaS\",\n" + 
			"            \"rang\": 20,\n" + 
			"            \"type\": 5\n" + 
			"        },\n" + 
			"        {\n" + 
			"            \"coord\": {\n" + 
			"                \"lat\": 55.5966,\n" + 
			"                \"lon\": 38.1115\n" + 
			"            },\n" + 
			"            \"distance\": 96.702,\n" + 
			"            \"dt\": 1359972825,\n" + 
			"            \"id\": 39277,\n" + 
			"            \"main\": {\"temp\": 273.75},\n" + 
			"            \"name\": \"st2340\",\n" + 
			"            \"rang\": 30,\n" + 
			"            \"type\": 5\n" + 
			"        }\n" + 
			"    ],\n" + 
			"    \"message\": \"\"\n" + 
			"}\n" + 
			"";

	private static void assertWeatherDataList (List<WeatherData> weatherDataList, int maxData) {
		assertNotNull (weatherDataList);
		assertTrue (weatherDataList.size () <= maxData);
		for (WeatherData wd : weatherDataList) {
			assertNotNull (wd);
			assertNotNull (wd.getName ());
			assertFalse (wd.getId () == Long.MIN_VALUE);
			assertFalse (wd.getDateTime () == Long.MIN_VALUE);
			assertTrue (wd.hasMain ());
		}
	}

	@Test
	public void testCurrentWeatherAroundPoint () throws HttpException, IOException, JSONException {
		OwmClient owm = new OwmClient ();
		List<WeatherData> weatherDataList = owm.currentWeatherAroundPoint (55f, 37f, 10);
		CurrentWeatherTest.assertWeatherDataList (weatherDataList, 10);
	}

	@Test
	public void testCurrentWeatherAroundCity () throws HttpException, IOException, JSONException {
		OwmClient owm = new OwmClient ();
		List<WeatherData> weatherDataList = owm.currentWeatherAroundCity (55f, 37f, 10);
		CurrentWeatherTest.assertWeatherDataList (weatherDataList, 10);
	}

}
