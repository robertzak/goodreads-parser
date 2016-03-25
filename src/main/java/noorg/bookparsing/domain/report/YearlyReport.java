package noorg.bookparsing.domain.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import noorg.bookparsing.domain.Book;
import noorg.bookparsing.domain.types.BookFormat;
import noorg.bookparsing.domain.types.BookGenre;
import noorg.bookparsing.report.format.BookFormatter;
import noorg.bookparsing.report.sort.AscendingDateReadComparator;


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
 * <p>A yearly report is used to generate overall details about your reading 
 * for that year. 
 * 
 * This includes: 
 * 
 * <ul>
 *  <li>Total Count</li>
 *  <li>Reread Count</li>
 * 	<li>counts by format, genre,  and rating</li>
 *  <li>total and average page count</li>
 *  <li>total and average audio hours</li>
 *  <li>average rating</li>
 *  <li>List of books</li>
 *  <li>List of reread books</li>
 * </ul>
 * 
 * @author Robert J. Zak
 *
 */
public class YearlyReport extends AbstractReport{
	
	private int year;
	private Set<Book> rereadBooks = new HashSet<>();
	private Map<BookFormat, Integer> countsByFormat = new HashMap<>();
	private Map<BookGenre, Integer> countsByGenre = new HashMap<>();
	private Map<Integer, Integer> countsByRating = new HashMap<>();
	private int totalPages;
	private int totalPagesGraphicNovels;
	private int totalHours;
	private long totalRating;
	
	public YearlyReport(final int year){
		this.year = year;
	}
	
	/**
	 * Add a book that's been reread.
	 * @param book
	 */
	public void addRereadBook(final Book book){
		if(book != null){
			rereadBooks.add(book);
		}else{
			logger.debug("Cannot add null book");
		}
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
			final BookFormat format = book.getFormat();
			incrementMapValue(countsByFormat, format);
			
			final Integer pageCount = book.getNumberOfPages();
			if(pageCount != null){
				if(format != null){
					switch(format){
					case AUDIO_BOOK:
						totalHours += pageCount;
						break;
					case BOOK:
					case EBOOK:
					case GRAPHIC_NOVEL:
						totalPages += pageCount;
						break;
					case UNKNOWN:
					default:
						logger.warn("{} has unknown Book Format This will mess up the "
								+ "report statistics related to page counts/hours", book);
						break;
					}
					
					/* Keep track of pages from Graphic Novels separately for more
					 * detailed statistics.
					 */
					if(BookFormat.GRAPHIC_NOVEL.equals(format)){
						totalPagesGraphicNovels += pageCount;
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
		
		return added;
	}
	
	/**
	 * The year of the report.
	 * 
	 * @return
	 */
	public int getYear() {
		return year;
	}

	/**
	 * The list of books for the year.
	 * @return
	 */
	public Set<Book> getBooks() {
		return books;
	}

	/**
	 * A map of the counts of the books for each {@link BookFormat}
	 * @return
	 */
	public Map<BookFormat, Integer> getCountsByFormat() {
		return countsByFormat;
	}

	/**
	 * A map of the counts of the books by {@link BookGenre}
	 * @return
	 */
	public Map<BookGenre, Integer> getCountsByGenre() {
		return countsByGenre;
	}

	/**
	 * A map of counts of each book rating (1, 2, 3, 4, 5 Stars)
	 * @return
	 */
	public Map<Integer, Integer> getCountsByRating() {
		return countsByRating;
	}

	/**
	 * Sum of the page count of all Read books.
	 * 
	 * @return
	 */
	public int getTotalPages() {
		return totalPages;
	}
	
	/**
	 * Return the sum of the page count for {@link BookFormat#GRAPHIC_NOVEL}
	 * @return
	 */
	public int getTotalPagesGraphicNovel(){
		return totalPagesGraphicNovels;
	}
	
	/**
	 * Return the sum of the page count for read books 
	 * excluding any that are {@link BookFormat#GRAPHIC_NOVEL}
	 * @return
	 */
	public int getTotalPagesExcludingGraphicNovels(){
		return getTotalPages() - getTotalPagesGraphicNovel();
	}

	/**
	 * Sum of length of all {@link BookFormat#AUDIO_BOOK}s
	 * @return
	 */
	public int getTotalHours() {
		return totalHours;
	}

	/**
	 * Sum of the ratings for ALL books.
	 * @return
	 */
	public long getTotalRating() {
		return totalRating;
	}
	
	/**
	 * How many books (regardless of {@link BookFormat}) done this year
	 * @return
	 */
	public int getTotal(){
		return books.size();
	}
	
	/**
	 * How many books reread this year
	 * NOTE: This is VERY data dependent and therefore may not be accurate
	 * @return
	 */
	public int getRereadCount(){
		return rereadBooks.size();
	}
	
	/**
	 * Returns how many audio books listened to.
	 * @return
	 */
	public int getTotalAudioBooks(){
		return getFormatCount(BookFormat.AUDIO_BOOK);
	}
	
	public int getTotalGraphicNovels(){
		return getFormatCount(BookFormat.GRAPHIC_NOVEL);
	}
	
	private int getFormatCount(final BookFormat format){
		int total = 0;
		
		final Integer abCount = countsByFormat.get(format);
		if(abCount != null){
			total = abCount;
		}
		
		return total;
	}
	
	/**
	 * How many books were read (ie NOT audio), this includes
	 * all other {@link BookFormat}'s such as {@link BookFormat#GRAPHIC_NOVEL}
	 * @return
	 */
	public int getTotalBooksRead(){
		return getTotal() - getTotalAudioBooks();
	}
	
	/**
	 * Exclude {@link BookFormat#GRAPHIC_NOVEL} in the {@link #getTotalBooksRead()}
	 * @return
	 */
	public int getTotalBooksReadExcludingGraphicNovels(){
		return getTotalBooksRead() - getTotalGraphicNovels();
	}
	
	public double getAverageRating(){
		return getAverage(totalRating, getTotal());
	}
	
	public double getAveragePages(){
		return getAverage(totalPages, getTotalBooksRead());
	}
	
	/**
	 * Exclude {@link BookFormat#GRAPHIC_NOVEL} from the average since
	 * they will skew the data.
	 * @return
	 */
	public double getAveragePagesExcludingGraphicNovels(){
		final int count = totalPages - totalPagesGraphicNovels;
		return getAverage(count, getTotalBooksReadExcludingGraphicNovels());
	}
	
	public double getAveragesHours(){
		return getAverage(totalHours, getTotalAudioBooks());
	}
	
	/**
	 * Helper to ensure double value for averages
	 * 
	 * @param totalCount
	 * @return
	 */
	private double getAverage(final long count, final long total){
		double totalDbl = new Long(count).doubleValue();
		
		return totalDbl/total;
	}

	@Override
	public String getReport(final BookFormatter formatter) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Year: ").append(year).append("\n");
		sb.append("Total Books: ").append(getTotal()).append("\n");
		sb.append("Rereads: ").append(getRereadCount()).append("\n\n");
		
		sb.append("********************\n* Format Breakdown *\n********************\n");
		sb.append(getCounts(countsByFormat)).append("\n");
		
		sb.append("*******************\n* Genre Breakdown *\n*******************\n");
		sb.append(getCounts(countsByGenre)).append("\n");
		
		sb.append("Average Rating: ");
		sb.append(getDoubleAsFixedDecimal(getAverageRating())).append("\n");
		sb.append(getCounts(countsByRating)).append("\n");
		
		sb.append("Number of Books: ").append(getTotalBooksRead()).append("\n");
		sb.append("Total Pages: ").append(getTotalPages()).append("\n");
		sb.append("Average Pages: ");
		sb.append(getDoubleAsFixedDecimal(getAveragePages())).append("\n\n");
		
		sb.append("Number of Books (Excluding Graphic Novels): ").append(getTotalBooksReadExcludingGraphicNovels()).append("\n");
		sb.append("Total Pages (Excluding Graphic Novels): ").append(getTotalPagesExcludingGraphicNovels()).append("\n");
		sb.append("Average Pages (Excluding Graphic Novels): ");
		sb.append(getDoubleAsFixedDecimal(getAveragePagesExcludingGraphicNovels())).append("\n\n");
		
		sb.append("Number of Audiobooks: ").append(getTotalAudioBooks()).append("\n");
		sb.append("Total Audio Hours: ").append(getTotalHours()).append("\n");
		sb.append("Average Hours: ");
		sb.append(getDoubleAsFixedDecimal(getAveragesHours())).append("\n\n");
		
		sb.append("*******************\n");
		sb.append("All Books:").append("\n");
		sb.append(appendBooks(books, formatter));
		
		sb.append("*******************\n");
		sb.append("Reread Books:").append("\n");
		sb.append(appendBooks(rereadBooks, formatter));
		
		return sb.toString();
	}
	
	private String appendBooks(final Collection<Book> books, final BookFormatter formatter){
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

}
