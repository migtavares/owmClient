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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * 
 * @author mtavares */
public class CalcTimePartExtratTest {
	static final private String CALC_TIME = "find = 0.0131 fetch = 0.0056 total=0.0186";

	@Test
	public void testGetValueStrFromCalcTimePart () {
		String findValue = AbstractOwmResponse.getValueStrFromCalcTimePart (CalcTimePartExtratTest.CALC_TIME, "find");
		assertNotNull (findValue);
		assertEquals ("0.0131", findValue);

		String fetchValue = AbstractOwmResponse.getValueStrFromCalcTimePart (CalcTimePartExtratTest.CALC_TIME, "fetch");
		assertNotNull (fetchValue);
		assertEquals ("0.0056", fetchValue);

		String totalValue = AbstractOwmResponse.getValueStrFromCalcTimePart (CalcTimePartExtratTest.CALC_TIME, "total");
		assertNotNull (totalValue);
		assertEquals ("0.0186", totalValue);
	}

	@Test
	public void testGetValueFromCalcTimeStr () {
		double findValue = AbstractOwmResponse.getValueFromCalcTimeStr (CalcTimePartExtratTest.CALC_TIME, "find");
		assertNotNull (findValue);
		assertEquals (0.0131d, findValue, 0.00001d);

		double fetchValue = AbstractOwmResponse.getValueFromCalcTimeStr (CalcTimePartExtratTest.CALC_TIME, "fetch");
		assertNotNull (fetchValue);
		assertEquals (0.0056d, fetchValue, 0.00001d);

		double totalValue = AbstractOwmResponse.getValueFromCalcTimeStr (CalcTimePartExtratTest.CALC_TIME, "total");
		assertNotNull (totalValue);
		assertEquals (0.0186d, totalValue, 0.00001d);
	}

}
