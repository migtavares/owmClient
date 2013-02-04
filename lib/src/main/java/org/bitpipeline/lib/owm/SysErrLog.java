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

/** A very simple logging to system err. */
public class SysErrLog implements LogInterface {
	@Override
	public void i (String msg, Object... params) {
		System.err.println (String.format (msg, params));
	}

	@Override
	public void i (Throwable tr, String msg, Object... params) {
		this.i (msg, params);
		tr.printStackTrace (System.err);
	}

	@Override
	public void e (String msg, Object... params) {
		System.err.println (String.format (msg, params));
	}

	@Override
	public void e (Throwable tr, String msg, Object... params) {
		this.e (msg, params);
		tr.printStackTrace (System.err);
	}

	@Override
	public void w (String msg, Object... params) {
		System.err.println (String.format (msg, params));
	}

	@Override
	public void w (Throwable tr, String msg, Object... params) {
		this.w (msg, params);
		tr.printStackTrace (System.err);
	}

	@Override
	public void d (String msg, Object... params) {
		System.err.println (String.format (msg, params));
	}

	@Override
	public void d (Throwable tr, String msg, Object... params) {
		this.d (msg, params);
		tr.printStackTrace (System.err);
	}
}
