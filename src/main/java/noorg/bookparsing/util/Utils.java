package noorg.bookparsing.util;


/**
 * <p>Copyright 2014 Robert J. Zak
 * 
 * <p>Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * <p>    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * <p>Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * <p>This stuff is probably unnecessary and should be replaced by an 3rd party jar
 * that does it already.
 * 
 * @author Robert J. Zak
 *
 */
public class Utils {

	
	/**
	 * Helper to prevent null pointer exceptions when converting an
	 * object to a string.
	 * 
	 * @param o
	 * @return
	 */
	public static String getString(final Object o){
		String str = "";
		
		if(o != null){
			str = o.toString();
		}
		
		return str;
	}
}
