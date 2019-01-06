package noorg.bookparsing.domain.report;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import noorg.bookparsing.domain.Book;
import noorg.bookparsing.domain.Contributor;
import noorg.bookparsing.domain.types.BookFormat;
import noorg.bookparsing.domain.types.BookGenre;
import noorg.bookparsing.domain.types.ContributorGender;
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
	private static final int STARTING_DECADE = 1800;
	private static final int DECADE = 10;
	
	private int year;
	private Set<Book> rereadBooks = new HashSet<>();
	private Set<Book> backlogBooks = new HashSet<>();
	private Map<BookFormat, Integer> countsByFormat = new HashMap<>();
	private Map<BookFormat, Integer> backlogCountsByFormat = new HashMap<>();
	private Map<BookGenre, Integer> countsByGenre = new HashMap<>();
	private Map<ContributorGender, Integer> countsByAuthorGender = new HashMap<>();
	private Map<Integer, Integer> countsByRating = new HashMap<>();
	private Map<Integer, Integer> countsByYearPublished = new HashMap<>();
	private Map<Integer, Integer> countsByDecadePublished = new HashMap<>();
	private int totalPages;
	private int totalPagesGraphicNovels;
	private int totalHours;
	private long totalRating;
	private Book longestBook;
	private Book longestAudioBook;
	private Book shortestBook;
	private Book shortestAudioBook;
	
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
	 * Add details to the year's statistics
	 */
	@Override
	protected void processAddedBook(Book book) {
		final BookFormat format = book.getFormat();
		incrementMapValue(countsByFormat, format);
		
		final Integer pageCount = book.getNumberOfPages();
		if(pageCount != null){
			if(format != null){
				switch(format){
				case AUDIO_BOOK:
					totalHours += pageCount;
					
					// compare lengths 
					if(isLonger(book, longestAudioBook)) {
						longestAudioBook = book;
					}
					
					if(isShorter(book, shortestAudioBook)) {
						shortestAudioBook = book;
					}
					
					break;
				case BOOK:
				case EBOOK:
					totalPages += pageCount;
					
					// compare lengths 
					if(isLonger(book, longestBook)) {
						longestBook = book;
					}
					
					if(isShorter(book, shortestBook)) {
						shortestBook = book;
					}
					
					
					break;
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
		
		// Generate counts by publish year/decade
		Integer yearPublished = book.getOriginalPublicationYear();
		if(yearPublished == null){			
			// Check for Edition publication year
			yearPublished = book.getYearOfPublication();
			if(yearPublished == null) {
				logger.warn("{} is both its original and edition publication years", book);
				
				// Use the report year. TODO better default?
				yearPublished = year;
			}
		}else if(yearPublished >year){
			logger.warn("{} was published after report year", book);
		}
		incrementMapValue(countsByYearPublished, yearPublished);
		
		int decadePublished = STARTING_DECADE;
		for(;decadePublished<year;decadePublished+=DECADE){
			if(yearPublished >= decadePublished){
				if(yearPublished < (decadePublished + DECADE)){
					logger.debug("Binning {} in {}", yearPublished, decadePublished);
					break;
				}
			}
		}
		incrementMapValue(countsByDecadePublished, decadePublished);
		
		final Contributor author = book.getAuthor();
		if(author != null){
			final ContributorGender gender = author.getGender();
			if(gender != null) {
				incrementMapValue(countsByAuthorGender, author.getGender());
			}
		}
		
		if(rating != null){
			totalRating += rating;
		}
		
		if(book.isFromBacklog()) {
			backlogBooks.add(book);
			incrementMapValue(backlogCountsByFormat, book.getFormat());
		}
	}
	
	@Override
	protected boolean shouldAddBook(Book book) {
		boolean addBook = false;
		
		// only add the book if it matches the report year
		Set<Integer> yearsRead = book.getYearsRead();
		if(yearsRead != null){
			if(yearsRead.contains(getYear())){
				addBook = true;
			}
		}
		
		return addBook;
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
	 * A Set of books that were reread during the year
	 * @return
	 */
	public Set<Book> getRereadBooks() {
		return rereadBooks;
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
	 * A map of the counts of the books by the {@link ContributorGender} of
	 * the author
	 * 
	 * @return
	 */
	public Map<ContributorGender, Integer> getCountsByAuthorGender() {
		return countsByAuthorGender;
	}

	/**
	 * A map of counts of each book rating (1, 2, 3, 4, 5 Stars)
	 * @return
	 */
	public Map<Integer, Integer> getCountsByRating() {
		return countsByRating;
	}

	/**
	 * A map of counts of the books by the year they were published
	 * @return
	 */
	public Map<Integer, Integer> getCountsByYearPublished() {
		return countsByYearPublished;
	}

	/**
	 * A map of counts of the books by the decade they were published
	 * @return
	 */
	public Map<Integer, Integer> getCountsByDecadePublished() {
		return countsByDecadePublished;
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
	public int getTotalPagesGraphicNovels(){
		return totalPagesGraphicNovels;
	}
	
	/**
	 * Return the sum of the page count for read books 
	 * excluding any that are {@link BookFormat#GRAPHIC_NOVEL}
	 * @return
	 */
	public int getTotalPagesExcludingGraphicNovels(){
		return getTotalPages() - getTotalPagesGraphicNovels();
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
	 * Returns the number of Books from the backlog read for the year
	 * @return
	 */
	public int getTotalBacklogRead() {
		return backlogBooks.size();
	}
	
	/**
	 * Returns the number of non {@link BookFormat#AUDIO_BOOK} books were read for the year
	 * 
	 * @return
	 */
	public int getTotalBacklogBooksRead() {
		return getTotalBacklogRead() - getTotalBacklogAudioBooks();
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
		return getMapCount(countsByFormat, BookFormat.AUDIO_BOOK);
	}
	
	/**
	 * Returns how many books from the backlog were {@link BookFormat#AUDIO_BOOK}
	 * 
	 * @return
	 */
	public int getTotalBacklogAudioBooks() {
		return getMapCount(backlogCountsByFormat, BookFormat.AUDIO_BOOK);
	}
	
	/**
	 * Returns how many {@link BookFormat#GRAPHIC_NOVEL} were read
	 * @return
	 */
	public int getTotalGraphicNovels(){
		return getMapCount(countsByFormat, BookFormat.GRAPHIC_NOVEL);
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
	
	/**
	 * Get the average rating of all books read/rated for the year
	 * @return
	 */
	public double getAverageRating(){
		return getAverage(totalRating, getTotal());
	}
	
	/**
	 * Get the average number of pages read for the year
	 * including {@link BookFormat#GRAPHIC_NOVEL}
	 * @return
	 */
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
	
	/**
	 * Get the Average Number of Hours of Audio listened to
	 * @return
	 */
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
		final double totalDbl = Long.valueOf(count).doubleValue();
		
		return totalDbl/total;
	}

	/**
	 * Get the number of pages of the largest book read for the year
	 * 
	 * @return
	 */
	public int getMaxPages() {
		return getBookLength(longestBook);
	}

	/**
	 * Get the number of hours for the longest audio book listened to for the year.
	 * 
	 * @return
	 */
	public int getMaxHours() {
		return getBookLength(longestAudioBook);
	}
	
	/**
	 * Helper to compare if book1 is longer than book 2, if one of them is null the non-null
	 * book will be considered longer.
	 * 
	 * @param book1
	 * @param book2
	 * @return
	 */
	private boolean isLonger(final Book book1, final Book book2) {
		return getBookLength(book1) > getBookLength(book2);
	}
	
	/**
	 * Helper to compare if book1 is shorter than book 2, if one of them is null the non-null
	 * book will be considered shorter.
	 * 
	 * @param book1
	 * @param book2
	 * @return
	 */
	private boolean isShorter(final Book book1, final Book book2) {
		boolean shorter = false;
		
		
		//logger.info("Comparing {} ({}) to {} ({})", book1, getBookLength(book1), book2, getBookLength(book2));
		if(book1 != null) {
			//logger.info("Book1 not null!");
			if(book2 != null) {
				//logger.info("Book2 not null!");
				shorter = getBookLength(book1) < getBookLength(book2);
			}else {
				shorter = true;
			}
		}
		
		return shorter;
	}
	
	/**
	 * Null safe helper to get the book length
	 * @param book
	 * @return
	 */
	private int getBookLength(final Book book) {
		int length = 0;
		
		if (book != null) {
			length = book.getNumberOfPages();
		}
		
		return length;
	}
	
	/**
	 * Helper to print the Book and it's length
	 * @param book
	 * @return
	 */
	public String printBookLength(final Book book, final boolean audio) {
		final StringBuilder sb = new StringBuilder();
		
		if(book != null) {
			sb.append(book.getTitle());
			sb.append(" by ").append(book.getAuthor());
			sb.append(" - ").append(book.getNumberOfPages());
			if(audio) {
				sb.append(" hours");
			}else {
				sb.append(" pages");
			}
		}else {
			sb.append("None");
		}
		
		return sb.toString();
	}

	@Override
	public String getReport(final BookFormatter formatter) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Year: ").append(year).append("\n");
		sb.append("Total Books: ").append(getTotal()).append("\n");
		sb.append("Total Backlog Books: ").append(getTotalBacklogRead()).append("\n");
		sb.append("Rereads: ").append(getRereadCount()).append("\n\n");
		
		sb.append("********************\n* Format Breakdown *\n********************\n");
		sb.append(getCounts(countsByFormat)).append("\n");
		
		sb.append("*******************\n* Genre Breakdown *\n*******************\n");
		sb.append(getCounts(countsByGenre)).append("\n");
		
		sb.append("***************************\n* Author Gender Breakdown *\n***************************\n");
		sb.append(getCounts(countsByAuthorGender)).append("\n");
		
		sb.append("***********\n* Ratings *\n***********\n");
		sb.append("Average Rating: ");
		sb.append(getDoubleAsFixedDecimal(getAverageRating())).append("\n");
		sb.append(getCounts(countsByRating)).append("\n");
		
		sb.append("Years Published:\n");
		sb.append(getCounts(countsByYearPublished)).append("\n");
		
		sb.append("Decade Published:\n");
		sb.append(getCounts(countsByDecadePublished)).append("\n");
		
		sb.append("Number of Books: ").append(getTotalBooksRead()).append("\n");
		sb.append("Total Pages: ").append(getTotalPages()).append("\n");
		sb.append("Total From Backlog: ").append(getTotalBacklogBooksRead()).append("\n");
		sb.append("Longest Book: ").append(printBookLength(longestBook, false)).append("\n");
		sb.append("Shortest Book: ").append(printBookLength(shortestBook, false)).append("\n");
		sb.append("Average Pages: ");
		sb.append(getDoubleAsFixedDecimal(getAveragePages())).append("\n\n");
		
		sb.append("Number of Books (Excluding Graphic Novels): ").append(getTotalBooksReadExcludingGraphicNovels()).append("\n");
		sb.append("Total Pages (Excluding Graphic Novels): ").append(getTotalPagesExcludingGraphicNovels()).append("\n");
		sb.append("Average Pages (Excluding Graphic Novels): ");
		sb.append(getDoubleAsFixedDecimal(getAveragePagesExcludingGraphicNovels())).append("\n\n");
		
		sb.append("Number of Audiobooks: ").append(getTotalAudioBooks()).append("\n");
		sb.append("Total Audio Hours: ").append(getTotalHours()).append("\n");
		sb.append("Total From Backlog: ").append(getTotalBacklogAudioBooks()).append("\n");
		sb.append("Longest Book: ").append(printBookLength(longestAudioBook, true)).append("\n");
		sb.append("Shortest Book: ").append(printBookLength(shortestAudioBook, true)).append("\n");
		sb.append("Average Hours: ");
		sb.append(getDoubleAsFixedDecimal(getAveragesHours())).append("\n\n");
		
		sb.append("*******************\n");
		sb.append("All Books:").append("\n");
		sb.append(formatBookList(books, formatter));
		
		sb.append("*******************\n");
		sb.append("Reread Books:").append("\n");
		sb.append(formatBookList(rereadBooks, formatter));
		
		sb.append("*******************\n");
		sb.append("Backlog Books:").append("\n");
		sb.append(formatBookList(backlogBooks, formatter));
		
		return sb.toString();
	}

}
