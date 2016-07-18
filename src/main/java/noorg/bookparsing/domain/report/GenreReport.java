package noorg.bookparsing.domain.report;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import noorg.bookparsing.domain.Book;
import noorg.bookparsing.domain.types.BookGenre;
import noorg.bookparsing.report.format.BookFormatter;

/**
 * <p>Copyright 2016 Robert J. Zak
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
 * <p>A genre report is used to generate overall details about your reading 
 * for that genre. 
 * 
 * This includes: 
 * 
 * <ul>
 *  <li>Total Count</li>
 *  <li>Count per year</li>
 * 	<li>tbd..</li>
 * </ul>
 * 
 * @author Robert J. Zak
 *
 */
public class GenreReport extends AbstractReport {	
	public static final int UNKNOWN_YEAR = Integer.MIN_VALUE;
	
	private BookGenre genre;
	
	private HashMap<Integer, Set<Book>> booksByYear = new HashMap<>();

	public GenreReport(BookGenre genre) {
		super();
		this.genre = genre;
	}
	/**
	 * Add a book read this year. This not only adds the book, but
	 * adds details to this year's statistics.
	 * 
	 */
	@Override
	public boolean addBook(final Book book) {
		boolean added = super.addBook(book);
		
		if(added){
			Set<Integer> years = book.getYearsRead();
			
			// if added, count by year
			if((years != null) && (!years.isEmpty())){
				for(Integer bookYear: years){
					incrementYear(bookYear, book);
				}
			}else{
				incrementYear(UNKNOWN_YEAR, book);
			}
		}
		
		return added;
	}
	
	private void incrementYear(final Integer bookYear, final Book book){
		int yearKey;
		
		if(bookYear != null){
			yearKey = bookYear;
		}else{
			yearKey = UNKNOWN_YEAR;
		}
		
		Set<Book> yearBooks = booksByYear.get(yearKey);
		if(yearBooks == null){
			yearBooks = new HashSet<>();
			booksByYear.put(bookYear, yearBooks);
		}
		
		yearBooks.add(book);
	}

	@Override
	public String getReport(BookFormatter formatter) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\n\nGenre: ").append(genre).append("\n");
		sb.append("Total Books: ").append(getTotal()).append("\n");
		sb.append("Year Breakdown:\n");
		
		SortedSet<Integer> yearsSorted = new TreeSet<Integer>(booksByYear.keySet());
		
		for(Integer year: yearsSorted){
			Set<Book> yearBooks = booksByYear.get(year);
			
			// TODO null check on year?
			if(yearBooks != null){
				if(UNKNOWN_YEAR == year){
					sb.append("\n\tNone: ");
				}else{
					sb.append("\n\t").append(year).append(": ");
				}
				
				sb.append(yearBooks.size());
			}
		}
		
		return sb.toString();
	}

}
