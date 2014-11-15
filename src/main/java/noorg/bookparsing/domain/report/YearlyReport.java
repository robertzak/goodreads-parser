package noorg.bookparsing.domain.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import noorg.bookparsing.domain.Book;
import noorg.bookparsing.domain.types.BookFormat;
import noorg.bookparsing.domain.types.BookGenre;
import noorg.bookparsing.report.format.BookFormatter;
import noorg.bookparsing.report.sort.AscendingDateReadComparator;


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
 * <p>A yearly report is used to generate
 * 
 * @author Robert J. Zak
 *
 */
public class YearlyReport extends AbstractReport{
	private int year;
	private List<Book> books = new ArrayList<>();
	private Map<BookFormat, Integer> countsByFormat = new HashMap<>();
	private Map<BookGenre, Integer> countsByGenre = new HashMap<>();
	private Map<Integer, Integer> countsByRating = new HashMap<>();
	private int totalPages;
	private int totalHours;
	private long totalRating;
	
	public YearlyReport(final int year){
		this.year = year;
	}

	@Override
	public void addBook(Book book) {
		if(book != null){
			books.add(book);
			
			
			final BookFormat format = book.getFormat();
			incrementMapValue(countsByFormat, format);
			
			final Integer pageCount = book.getNumberOfPages();
			if(pageCount != null){				
				if(format != null){
					switch(format){
					case AUDIO_BOOK:
						break;
					case BOOK:
					case EBOOK:
					case GRAPHIC_NOVEL:
						totalPages += pageCount;
						break;
					case UNKOWN:
					default:
						logger.warn("{} has unknown Book Format This will mess up the "
								+ "report statistics related to page counts/hours", book);
						break;
					
					}
				}else{
					// TODO should we just add to page count by default?
					logger.warn("{} has unknown Book Format This will mess up the "
						+ "report statistics related to page counts/hours", book);
				}
			}else{
				logger.warn("{} has unknown page count. This will mess up the "
						+ "report statistics related to page counts/hours", book);
			}
			
			
			incrementMapValue(countsByGenre, book.getGenre());
			
			final Integer rating = book.getMyRating();
			incrementMapValue(countsByRating, rating);
			
			if(rating != null){
				totalRating += rating;
			}
		}
	}
	
	public int getYear() {
		return year;
	}

	public List<Book> getBooks() {
		return books;
	}

	public Map<BookFormat, Integer> getCountsByFormat() {
		return countsByFormat;
	}

	public Map<BookGenre, Integer> getCountsByGenre() {
		return countsByGenre;
	}

	public Map<Integer, Integer> getCountsByRating() {
		return countsByRating;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getTotalHours() {
		return totalHours;
	}

	public long getTotalRating() {
		return totalRating;
	}
	
	public int getTotal(){
		return books.size();
	}
	
	public double getAverageRating(){
		return totalRating/getTotal();
	}
	
	public double getAveragePages(){
		return totalPages/getTotal();
	}
	
	public double getAveragesHours(){
		return totalHours/getTotal();
	}

	@Override
	public String getReport(final BookFormatter formatter) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Year: ").append(year).append("\n");
		sb.append("Total Books: ").append(getTotal()).append("\n");
		sb.append(getCounts(countsByFormat)).append("\n");
		sb.append(getCounts(countsByGenre)).append("\n");		
		
		sb.append("Average Rating: ").append(getAverageRating()).append("\n");
		sb.append(getCounts(countsByRating)).append("\n");
		
		sb.append("Total Pages: ").append(getTotalPages()).append("\n");
		sb.append("Average Pages: ").append(getAveragePages()).append("\n");
		
		sb.append("Total Hours: ").append(getTotalHours()).append("\n");
		sb.append("Average Pages: ").append(getAveragesHours()).append("\n\n");
		
		sb.append("Books:").append("\n");
		// TODO tabs or some better spacing..
		// sort by read date
		Collections.sort(books, new AscendingDateReadComparator());
		
		sb.append(formatter.getFormatHeaders()).append("\n");
		for(Book book: books){
			sb.append(formatter.format(book)).append("\n");
		}
		
		return sb.toString();
	}

}
