# Introduction
This is a Java library to retrieve weather information and forecasts from [Open Weather Map](http://http://openweathermap.org/).

# Continuous Integration

[![Build Status](https://travis-ci.org/migtavares/owmClient.png?branch=master)](https://travis-ci.org/migtavares/owmClient)

Continuous integration is done on [Travis CI](https://travis-ci.org) and the status for the OwmClient can be found [here](https://travis-ci.org/migtavares/owmClient).

# Features

## Implemented

+ getting current weather at
	+ around a geographic coordinate
	+ in a circle
	+ in a bounding box
	+ city by
	  + geographic coordinate
	  + OpenWeatherMap city id
	  + city name
	  + city name and country code
	  + city(ies) within a bounding box
	  + city(ies) in a circle
+ getting weather forecast for a city by
	+ OpenWeatherMap city id
	+ city name
+ getting weather history
	+ from a city id
	+ from a station id (partial)
+ using an OpenWeatherMap APPID

## Planned

The weather history for stations is still not complete.

# Usage

Just create a instance of OwmClient, call it's methods and use their return values.

## Using maven to fetch the own-lib dependency.

Add the [OSSRH](https://oss.sonatype.org) maven repository to your project by adding the following snipet to your pom.xml file:

	<repositories>
		<repository>
			<id>ossrh</id>
			<url>https://oss.sonatype.org/content/groups/public/</url>
		</repository>
	</repositories>

Then in the dependencies of your project add the own-lib dependency:

	<dependency>
		<groupId>org.bitpipeline.lib</groupId>
		<artifactId>owm-lib</artifactId>
		<version>2.1.3-SNAPSHOT</version>
	</dependency>


## Dependencies

To use this library in a environment that doesn't include Apache `httpclient` from `httpcommons` you must include the dependency in the final project.

The same for the json parser `org.json`.

With maven that can be accomplished with:

	<dependency>
		<groupId>org.apache.httpcomponents</groupId>
		<artifactId>httpclient</artifactId>
		<version>4.2.3</version>
	</dependency>

	<dependency>
		<groupId>org.json</groupId>
		<artifactId>json</artifactId>
		<version>20090211</version>
	</dependency>

## Note of caution
There's almost no validation build in this library and the fetching of data from the JSON OpenWeatherMap API tries to survive without making much of a fuss.

This means that if you need some value you should use the `has` methods to check if the value was received (and understood) or not.

For enumerations there's a special value `UNKNOWN` that means that although a value was received at the JSON interface it was not one that the library knows about.

## Simple Example


	OwmClient owm = new OwmClient ();
	WeatherStatusResponse currentWeather = owm.currentWeatherAtCity ("Tokyo", "JP");
	if (currentWeather.hasWeatherStatus ()) {
		WeatherData weather = currentWeather.getWeatherStatus ().get (0);
		if (weather.getPrecipitation () == Integer.MIN_VALUE) {
			WeatherCondition weatherCondition = weather.getWeatherConditions ().get (0);
			String description = weatherCondition.getDescription ();
			if (description.contains ("rain") || description.contains ("shower"))
				System.out.println ("No rain measures in Tokyo but reports of " + description);
			else
				System.out.println ("No rain measures in Tokyo: " + description);
		} else
			System.out.println ("It's raining in Tokyo: " + weather.getPrecipitation () + " mm/h");
	}


# Class Diagrams
## Basic Weather Data Structure

	                        +---------------------+
	                        | AbstractWeatherData |
	                        +------·-------·------+
				                  /_\     /_\
						           |       |
				           +-------+       +------+
			               |                      |
	                 +-------------+    +--------------------+
	                 | WeatherData |    | SampledWeatherData |
	                 +-----·-------+    +--------------------+
	                      /_\
                           |
                           |
	            +----------------------+
	            | LocalizedWeatherData |
	            +---·--------------·---+
                   /_\            /_\
				    |              |
               +----+              +----+
	           |                        |
	+---------------------+   +-------------------+
	| ForecastWeatherData |   | StatusWeatherData |
	+---------------------+   +-------------------+

## Open Weather Map responses classes

+ `WeatherForecastResponse` - list of `ForecastWeatherData`
+ `WeatherHistoryCityResponse` - list of `WeatherData`
+ `WeatherHistoryStationResponse` - list of `AbstractWeatherData`, using the implementation `WeatherData` for responses to `TICK` history and `SampledWeatherData` for `HOUR` and `DAY`
+ `WeatherStatusResponse` - list of `StatusWeatherData`

# License												
Copyright 2013 J. Miguel P. Tavares

Licensed under the Apache License, Version 2.0 (the "License")
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
