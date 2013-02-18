# Introduction
This is a Java library to retrieve weather information and forecasts from [Open Weather Map](http://http://openweathermap.org/).

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
+ using an OpenWeatherMap APPID

## Planned

+ weather station history
+ city weather history

# Usage

Just create a instance of OwmClient, call it's methods and use their return values.

## Note of caution
There's almost no validation build in this library and the fetching of data from the JSON OpenWeatherMap API tries to survive without making much of a fuss.

This means that if you need some value you should use the `has` methods to check if the value was received (and understood) or not.

For enumerations there's a special value `UNKNOWN` that means that although a value was received at the JSON interface it was not one that the library knows about.

## Simple Example

	OwmClient owm = new OwmClient ();
	WeatherStatusResponse currentWeather = owm.currentWeatherAtCity ("london", "UK");
	if (currentWeather.hasWeatherStatus ()) {
		WeatherData weather = currentWeather.getWeatherStatus ().get (0);
		if (weather.hasRain ()) {
			Precipitation rain = weather.getRain ();
			if (rain.hasToday ()) {
				if (rain.getToday () == 0)
					System.out.println ("No reports of rain in London");
				else
					System.out.println ("Another report of rain in London");
			}
		} else {
			System.out.println ("No rain information in London");
		}
	}

Actually we should also check if there are hourly reports of rain by calling `rain.measurements ()`which would give us a set of integers that represent the hours of the measurement. Then we can get each of the measure by calling `rain.getMeasure (intHour)`.

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
