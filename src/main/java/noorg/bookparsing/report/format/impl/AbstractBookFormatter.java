package noorg.bookparsing.report.format.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import noorg.bookparsing.report.format.BookFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
 * <p>Shared formatting code.
 * 
 * @author Robert J. Zak
 * 
 *
 */
public abstract class AbstractBookFormatter implements BookFormatter {
	protected static final Logger logger = LoggerFactory.getLogger(
			AbstractBookFormatter.class);
	
	protected static final SimpleDateFormat DAY_MONTH_YEAR = 
			new SimpleDateFormat("dd/mm/yyyy");
	protected static final SimpleDateFormat MONTH_DAY_YEAR = 
			new SimpleDateFormat("mm/dd/yyyy");
	
	
	/**
	 * Format a {@link Calendar} to return a string in the form of dd/mm/yyyy
	 * @param calendar
	 * @return
	 */
	protected String getDayMonthYear(final Calendar calendar){
		return formatCalendar(calendar, DAY_MONTH_YEAR);
	}
	
	/**
	 * Format a {@link Calendar} to return a string in the form of mm/dd/yyyy
	 * @param calendar
	 * @return
	 */
	protected String getMonthDayYear(final Calendar calendar){
		return formatCalendar(calendar, MONTH_DAY_YEAR);
	}

	/**
	 * Null safe helper to convert a {@link Calendar} to a string with
	 * the provided {@link SimpleDateFormat}
	 * @param calendar
	 * @param sdf
	 * @return
	 */
	protected String formatCalendar(final Calendar calendar, 
			SimpleDateFormat sdf){
		String dateStr = null;
		
		if(calendar != null && sdf != null){
			dateStr = sdf.format(calendar.getTime());
		}
		
		return dateStr;		
	}
}
