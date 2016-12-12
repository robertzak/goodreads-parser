package noorg.bookparsing.domain.report;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import noorg.bookparsing.domain.Book;
import noorg.bookparsing.report.format.BookFormatter;
import noorg.bookparsing.report.format.impl.DefaultBookFormater;
import noorg.bookparsing.report.sort.AscendingDateReadComparator;
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
			if(shouldAddBook(book)){
				added = books.add(book);
				processAddedBook(book);
			}
		}else{
			logger.debug("Cannot add null book");
		}
		
		return added;
	}
	
	/**
	 * The base class will add the book to the Set, but each report may
	 * need to do additional processing.
	 * 
	 * TODO make this an overridable empty implementation instead of forcing
	 * the subclass to implement? So far both concrete reports will use it..
	 * 
	 * @param book The book being added
	 * 
	 */
	protected abstract void processAddedBook(final Book book);
	
	/**
	 * Override this method to add criteria for when to add a book to the report.
	 * The default implementation is to add all non-null books.
	 * 
	 * @param book the potential book to add
	 * @return
	 */
	protected boolean  shouldAddBook(final Book book){
		return true;
	}
	
	@Override
	public String getReport() {
		return getReport(new DefaultBookFormater());
	}
	
	/**
	 * How many books in this report
	 * @return
	 */
	public int getTotal(){
		return books.size();
	}
	
	/**
	 * 
	 * @param books
	 * @param formatter
	 * @return
	 */
	protected String formatBookList(final Collection<Book> books, final BookFormatter formatter){
		StringBuilder sb = new StringBuilder();
		// TODO tabs or some better spacing..
		// sort by read date
		final List<Book> bookList = new ArrayList<>(books);
		Collections.sort(bookList, new AscendingDateReadComparator());
		
		sb.append(formatter.getFormatHeaders()).append("\n");
		for(Book book: bookList){
			sb.append(formatter.format(book)).append("\n");
		}
		
		sb.append("\n");		
		
		return sb.toString();
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
	 * Total KEY Count: VALUE (PERCENT)
	 * 
	 * @param map
	 * @return
	 */
	protected String getCounts(final Map<?,Integer> map){
		StringBuilder sb = new StringBuilder();
		
		// Sort the Keys
		SortedSet<Object> sortedKeys = new TreeSet<Object>(map.keySet());
		
		for(Object key: sortedKeys){
			final Integer count = map.get(key);
			final String countStr = Utils.getString(count);
			final String percent = getPercentAsString(count);
			
			sb.append("Total ").append(key).append(" Count: ").append(countStr);
			sb.append(" (").append(percent).append("%)").append("\n");
		}
		
		return sb.toString();
	}
	
	/**
	 * Helper to determine the percentage of total books rounded to
	 * two decimal places.
	 * 
	 * @param count
	 * @return
	 * 
	 * TODO should this be public, or moved elsewhere (with total as second param)?
	 */
	public String getPercentAsString(final Integer count){
		double percent = 0.0;
		
		if(count != null){
			percent = ((double)(count*100))/books.size();
		}
		
		return String.format("%.2f", percent);
	}
	

	
	/**
	 * Helper to convert a double into a fixed decimal String with 2
	 * decimal places.
	 *  
	 * @param value
	 * @return
	 */
	public static String getDoubleAsFixedDecimal(final double value){
		return DECIMAL_FORMAT.format(value);
	}
}
