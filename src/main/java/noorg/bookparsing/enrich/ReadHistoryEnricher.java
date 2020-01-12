package noorg.bookparsing.enrich;

import java.time.LocalDate;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import noorg.bookparsing.domain.Book;

/**
 * <p>Copyright 2020 Robert J. Zak
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
 * <p>This enricher will look at your book shelves for custom shelves that either start with the
 * prefix {@value #SHELF_YEAR_READ_PREFIX} or marked as being read before
 * goodreads with {@value #SHELF_READ_BEFORE_GOODREADS}
 * 
 * @author Robert J. Zak
 *
 */
public class ReadHistoryEnricher extends AbstractBookEnricher {
	private static Logger logger = LoggerFactory.getLogger(ReadHistoryEnricher.class);
	
	/**
	 * This prefix will be used to determine additional years a book
	 * was read if shelved that way.
	 * 
	 * Ex: read-2012, read-2014 will add 2012 and 2014 to the list
	 */
	private static final String SHELF_YEAR_READ_PREFIX = "read-";
	
	/**
	 * This is a custom shelf you can use to indicate that you read a book before you started 
	 * tracking them on goodreads. This could indicate that the firstRead date will be wrong/unknown
	 * if you only started tracking the years read after joining.
	 * 
	 * This is currently used in re-read logic to assume any yearly report a book with this shelf
	 * is on will be a reread without needing to know what year you originally read it.
	 */
	private static final String SHELF_READ_BEFORE_GOODREADS = "read-before-goodreads";

	@Override
	protected void enrichBook(Book book) {
		// set the years read
		final SortedSet<Integer> yearsRead = getYearsRead(book.getDateRead(), book.getBookshelves());			
		book.setYearsRead(yearsRead);
		if(!yearsRead.isEmpty()) {
			book.setFirstRead(yearsRead.first());
		}
		
		book.setReadBeforeGoodReads(getReadBeforeGoodReads(book.getBookshelves()));
	}
	
	/**
	 * Determine the years this book was read. 
	 * By default it will use your book's read date.
	 * 
	 * <p>Additionally if you shelf your books by year it will add those years as well.
	 * see: {@link #SHELF_YEAR_READ}
	 * 
	 * @param dateRead
	 * @param shelfList
	 * @return
	 */
	private SortedSet<Integer> getYearsRead(LocalDate dateRead, List<String> shelfList){
		SortedSet<Integer> yearsRead = new TreeSet<>();
		
		// add the year of read date if set
		if(dateRead != null){		
			yearsRead.add(dateRead.getYear());
		}
		
		// parse shelves for additional years
		if(shelfList != null){
			for(String shelf: shelfList){
				if(shelf.startsWith(SHELF_YEAR_READ_PREFIX)){
					// possible year shelf
					final String yearToken = shelf.substring(SHELF_YEAR_READ_PREFIX.length());
					try{
						yearsRead.add(Integer.parseInt(yearToken));
					}catch(Exception e){
						logger.debug("Failed to convert {} into a year", yearToken, e);
					}
				}
			}
		}
		
		return yearsRead;
	}
	
	/**
	 * Looks at the shelves to determine if the book was first read before the user started
	 * tracking their books on Goodreads. This assumes they shelve their data with a custom
	 * shelf: {@value #SHELF_READ_BEFORE_GOODREADS}
	 * @param shelfList
	 * @return
	 */
	private boolean getReadBeforeGoodReads(final List<String> shelfList) {
		boolean readBefore = false;
		if(shelfList != null){
			for(String shelf: shelfList) {
				if(shelf.equals(SHELF_READ_BEFORE_GOODREADS)) {
					readBefore = true;
				}
			}
		}
		
		return readBefore;
	}

}
