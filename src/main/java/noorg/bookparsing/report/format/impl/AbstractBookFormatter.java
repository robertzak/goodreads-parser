package noorg.bookparsing.report.format.impl;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import noorg.bookparsing.report.format.BookFormatter;


/**
 * <p>Copyright 2014-2016 Robert J. Zak
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
	
	protected static final DateTimeFormatter DAY_MONTH_YEAR = 
			DateTimeFormatter.ofPattern("dd/MM/yyyy");
	protected static final DateTimeFormatter MONTH_DAY_YEAR = 
			DateTimeFormatter.ofPattern("MM/dd/yyyy");
	
	
	/**
	 * Format a {@link TemporalAccessor} to return a string in the form of dd/mm/yyyy
	 * @param temporal
	 * @return
	 */
	protected String getDayMonthYear(final TemporalAccessor temporal){
		return formatTemporal(temporal, DAY_MONTH_YEAR);
	}
	
	/**
	 * Format a {@link LocalTemporalAccessorDate} to return a string in the form of mm/dd/yyyy
	 * @param temporal
	 * @return
	 */
	protected String getMonthDayYear(final TemporalAccessor temporal){
		return formatTemporal(temporal, MONTH_DAY_YEAR);
	}
	
	/**
	 * Null safe helper to convert a {@link TemporalAccessor} to a string with
	 * the provided {@link DateTimeFormatter}
	 * @param temporal
	 * @param formater
	 * @return
	 */
	protected String formatTemporal(final TemporalAccessor temporal, 
			final DateTimeFormatter formater){
		String dateStr = null;
		
		if(temporal != null && formater != null){
			dateStr = formater.format(temporal);
		}
		
		return dateStr;	
	}
}
