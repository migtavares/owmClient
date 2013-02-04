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

public class TestData {
	static final String CURRENT_WEATHER_CITY = "{\n" + 
			"	\"id\": 7325,\n" + 
			"	\"dt\": 1359979200,\n" + 
			"	\"name\": \"UUMO\",\n" + 
			"	\"type\": 1,\n" + 
			"	\"coord\": {\n" + 
			"		\"lat\": 55.5,\n" + 
			"		\"lon\": 37.5\n" + 
			"	},\n" + 
			"	\"distance\": 63.995,\n" + 
			"	\"main\": {\n" + 
			"		\"temp\": 272.15,\n" + 
			"		\"humidity\": 100\n" + 
			"	},\n" + 
			"	\"wind\": {\n" + 
			"		\"speed\": 7,\n" + 
			"		\"deg\": 340\n" + 
			"	},\n" + 
			"	\"rang\": 50,\n" + 
			"	\"clouds\": [\n" + 
			"		{\n" + 
			"			\"distance\": 91,\n" + 
			"			\"condition\": \"BKN\"\n" + 
			"		},\n" + 
			"		{\n" + 
			"			\"distance\": 305,\n" + 
			"			\"condition\": \"OVC\",\n" + 
			"			\"cumulus\": \"CB\"\n" + 
			"		}\n" + 
			"	]\n" + 
			"}\n" + 
			""; 

	static String CURRENT_WEATHER_POINT = "        {\n" + 
			"            \"id\": 5106529,\n" + 
			"            \"name\": \"Woodbridge\",\n" + 
			"            \"coord\": {\n" + 
			"                \"lon\": -74.284592,\n" + 
			"                \"lat\": 40.557598\n" + 
			"            },\n" + 
			"            \"distance\": 0.814,\n" + 
			"            \"main\": {\n" + 
			"                \"temp\": 267.97,\n" + 
			"                \"pressure\": 1026,\n" + 
			"                \"humidity\": 45,\n" + 
			"                \"temp_min\": 267.15,\n" + 
			"                \"temp_max\": 269.15\n" + 
			"            },\n" + 
			"            \"dt\": 1359818400,\n" + 
			"            \"wind\": {\n" + 
			"                \"speed\": 3.1,\n" + 
			"                \"deg\": 260\n" + 
			"            },\n" + 
			"            \"clouds\": {\n" + 
			"                \"all\": 40\n" + 
			"            },\n" + 
			"            \"weather\": [\n" + 
			"                {\n" + 
			"                    \"id\": 802,\n" + 
			"                    \"main\": \"Clouds\",\n" + 
			"                    \"description\": \"scattered clouds\",\n" + 
			"                    \"icon\": \"03d\"\n" + 
			"                }\n" + 
			"            ]\n" + 
			"        }"; 
}
