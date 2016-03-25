package noorg.bookparsing.domain.report;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import noorg.bookparsing.domain.Book;
import noorg.bookparsing.report.format.impl.DefaultBookFormater;
import noorg.bookparsing.util.Utils;

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
 * <p>Shared code for all Report Types
 * 
 * @author Robert J. Zak
 *
 */
public abstract class AbstractReport implements Report{
	protected static final Logger logger = LoggerFactory.getLogger(
			AbstractReport.class);
	protected static final DecimalFormat DECIMAL_FORMAT = 
			new DecimalFormat("###.##");

	protected Set<Book> books = new HashSet<>();

	@Override
	public String toString(){
		// getReport doubles as toString by default
		return getReport();
	}
	
	/**
	 * Default implementation simply adds the book to the set. 
	 * Override to add additional functionality.
	 */
	@Override
	public boolean addBook(final Book book) {
		boolean added = false;
		if(book != null){ 
			added = books.add(book);
		}else{
			logger.debug("Cannot add null book");
		}
		
		return added;
	}
	
	@Override
	public String getReport() {
		return getReport(new DefaultBookFormater());
	}
	
	/**
	 * Helper to increment counts in a map of type <K,Integer>
	 * @param map
	 * @param key
	 */
	protected <K> void incrementMapValue(final Map<K,Integer> map, final K key){
		if(map != null){
			if(key != null){
				Integer mapValue = map.get(key);
				
				int newValue = 0;
				if(mapValue != null){
					newValue = mapValue;
				}
				
				// now increment and add
				map.put(key, ++newValue);
			}else{
				logger.warn("Cannot Increment null key {}", key);
			}

		}else{
			logger.warn("Cannot Increment key {} in null map", key);
		}
	}
	
	/**
	 * Helper to convert a map into rows of output of the form:
	 * 
	 * Total KEY Count: VALUE
	 * 
	 * @param map
	 * @return
	 */
	protected String getCounts(final Map<?,Integer> map){
		StringBuilder sb = new StringBuilder();
		
		for(Object key: map.keySet()){
			String value = Utils.getString(map.get(key));
			sb.append("Total ").append(key).append(" Count: ");
			sb.append(value).append("\n");
		}
		
		return sb.toString();
	}
	

	
	/**
	 * Helper to convert a double into a fixed decimal String with 2
	 * decimal places.
	 *  
	 * @param value
	 * @return
	 */
	protected String getDoubleAsFixedDecimal(final double value){
		return DECIMAL_FORMAT.format(value);
	}
}
