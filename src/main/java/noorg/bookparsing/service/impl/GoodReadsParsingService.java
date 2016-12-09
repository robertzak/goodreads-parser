package noorg.bookparsing.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVParser;
import noorg.bookparsing.domain.Book;
import noorg.bookparsing.domain.Contributor;
import noorg.bookparsing.domain.types.BookCondition;
import noorg.bookparsing.domain.types.BookFormat;
import noorg.bookparsing.domain.types.BookGenre;
import noorg.bookparsing.domain.types.ContributorRole;
import noorg.bookparsing.service.ParsingService;


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
 * <p>This is where the magic happens. Here we parse a line of the GoodReads CSV 
 * and make a book out of it.
 * 
 * @author Robert J. Zak
 *
 */
public class GoodReadsParsingService implements ParsingService<String, Book> {
	private static final Logger logger = LoggerFactory.getLogger(
			GoodReadsParsingService.class);
	
	// Some possible shelves that might be used to guess the genre
	private static final String SHELF_NON_FICTION = "non-fiction";
	private static final String SHELF_SCI_FI = "sci-fi";
	private static final String SHELF_SCIENCE_FICTION = "science-fiction";
	
	/**
	 * This prefix will be used to determine additional years a book
	 * was read if shelved that way.
	 * 
	 * Ex: read-2012, read-2014 will add 2012 and 2014 to the list
	 */
	private static final String SHELF_YEAR_READ_PREFIX = "read-";
	
	// TODO move this?
	protected static final DateTimeFormatter GOODREADS_DATE_FORMAT = 
			DateTimeFormatter.ofPattern("yyyy/MM/dd");

	public Book parse(String input) {
		logger.debug("Parsing: {}", input);
		// TODO null check input
		
		Book book = new Book();
		
		/* 
		 * Good reads data..
		 * 
		 * 0 Book Id	
		 * 1 Title
		 * 2 Author
		 * 3 Author l-f
		 * 4 Additional Authors
		 * 5 ISBN
		 * 6 ISBN13
		 * 7 My Rating
		 * 8 Average Rating
		 * 9 Publisher
		 * 10 Binding	(use to get book format)
		 * 11 Number of Pages
		 * 12 Year Published
		 * 13 Original Publication Year
		 * 14 Date Read
		 * 15 Date Added
		 * 16 Bookshelves with positions
		 * 17 Bookshelves	(use to get genre)
		 * 18 Exclusive Shelf	(use to get Read State)
		 * 19 My Review
		 * 20 Spoiler
		 * 21 Private Notes
		 * 22 Read Count
		 * 23 Recommended For
		 * 24 Recommended By
		 * 25 Owned Copies
		 * 26 Original Purchase Date
		 * 27 Original Purchase Location
		 * 28 Condition
		 * 29 Condition Description
		 * 30 BCID
		 * 
		 */
		CSVParser parser = new CSVParser();
		try {
			String[]  tokens = parser.parseLine(input);
			
			if(tokens.length != 31){
				logger.error("{} produced {} tokens", input, tokens.length);
				logger.debug("{}", (Object[])tokens);
			}
			
			// convert the CSV
			book.setId(tokens[0]);
			// TODO tokenize title further for series information?
			book.setTitle(tokens[1]);
			book.setAuthor(getContributor(tokens[2], ContributorRole.AUTHOR));
			// Skip 4th token (reverse author name)
			book.setAdditionalContributors(parseAdditionalContributors(tokens[4]));
			book.setIsbn(parseISBN(tokens[5]));
			book.setIsbn13(parseISBN(tokens[6]));
			book.setMyRating(parseInt(tokens[7]));
			book.setAverageRating(parseFloat(tokens[8]));
			book.setPublisher(tokens[9]);
			book.setBinding(tokens[10]);
			book.setNumberOfPages(parseInt(tokens[11]));
			book.setYearOfPublication(parseInt(tokens[12]));
			book.setOriginalPublicationYear(parseInt(tokens[13]));
			book.setDateRead(convertDate(tokens[14]));
			book.setDateAdded(convertDate(tokens[15]));
			book.setBookshelves(getList(tokens[16]));
			// TODO maybe use this list to rebuild shelves in order?
			book.setBookshelvesWithPositions(getList(tokens[17]));
			book.setExclusiveShelf(tokens[18]);
			book.setMyReview(tokens[19]);
			// TODO Not sure what this column is/contains. Spoiler of what?
			book.setSpoiler(tokens[20]);
			book.setPrivateNotes(tokens[21]);
			book.setReadCount(parseReadCount(tokens[22]));
			book.setRecommendedFor(tokens[23]);
			book.setRecommendedBy(tokens[24]);
			book.setOwnedCopies(parseInt(tokens[25]));
			book.setPurchaseDate(convertDate(tokens[26]));
			book.setPurchaseLocation(tokens[27]);
			book.setCondition(BookCondition.parse(tokens[28]));
			book.setConditionDescription(tokens[29]);
			book.setBcid(tokens[30]);
			
			// enrich the data
			final BookFormat format = BookFormat.parse(book.getBinding());
			if(BookFormat.UNKNOWN.equals(format)){
				logger.debug("{} has unknown format from binding: {}",
						book.getTitle(), book.getBinding());
			}
			book.setFormat(format);
			
			// validate audio book duration
			/* TODO Look into how we handle rereads done in audio. I prefer
			 * to shelve only 1 copy of a book, so when I do a reread
			 * in audio, the page count might be for the text and not the number
			 * of hours, which will mess up the numbers.
			 * 
			 * Right now I'm not sure if those books are being counted as audio books or
			 * not. I suspect they aren't being counted. If that's the case, nothing needs
			 * to be done. Unfortunately there isn't a way to track both the page counts 
			 * and the audio duration apart from adding a book twice (once for each format).
			 * 
			 * This may be necessary to get proper statistics on audio for a year. 
			 */
			if(BookFormat.AUDIO_BOOK.equals(book.getFormat())){
				final Integer duration = book.getNumberOfPages();
				if((duration!= null) && (duration > 50)){
					// 50 hours is extremely long for an audio book warn about it.
					logger.warn("{} has duration of {} hours", book.getTitle(), duration);
				}
			}
			

			book.setGenre(getGenre(book.getBookshelves()));
			// TODO read state..
			
			// set the years read
			book.setYearsRead(getYearsRead(book.getDateRead(), book.getBookshelves()));
			
		} catch (IOException e) {
			logger.error("Problem parsing data", e);
		}
		
		return book;
	}
	
	/**
	 * This is an attempt to use the user's bookshelves to get the genre of
	 * the book. See {@link BookGenre} for more about the genres.
	 * 
	 * This is going to largely depend on how a user shelves their books. What
	 * I'm writing will work for my books, but probably not for everyone.
	 * 
	 * I'm open to suggestions on how to do this better.
	 * 
	 * TODO move this to it's own Enricher class. It's custom to my shelving
	 * and shouldn't be in the main parser.
	 * 
	 * @param bookshelves
	 * @return
	 */
	private BookGenre getGenre(final List<String> bookshelves){
		BookGenre genre = BookGenre.UNKNOWN;
		
		if(bookshelves != null){
			
			genres:
			for(BookGenre g: BookGenre.values()){
				// use lower case
				final String genreStr = g.toString().toLowerCase();
				// first see if any shelf is an exact match
				if(bookshelves.contains(genreStr)){
					// easy. we're done
					genre = g;
					break genres;
				}else{
					// see if any shelf contains one of our genres
					for(String shelf: bookshelves){
						if(shelf.contains(genreStr)){
							// probably good enough
							genre = g;
							break genres;
						}
					}
					
					/* Still don't know? Try some special handling
					 * 
					 * TODO what others can we add, or how can this be done 
					 * better?
					 */
					if(BookGenre.UNKNOWN.equals(genre)){
						for(String shelf: bookshelves){
							switch(shelf){
							case SHELF_NON_FICTION:
								genre = BookGenre.NONFICTION;
								break genres;
							case SHELF_SCI_FI:
							case SHELF_SCIENCE_FICTION:
								genre = BookGenre.SCIFI;
								break genres;
							}
						}
					}
				}
			}
		}
		
		if(BookGenre.UNKNOWN.equals(genre)){
			logger.debug("Unable to find genre from: {}", bookshelves);
		}
		
		return genre;
	}


	/**
	 * Convert the date string to a {@link Calendar}
	 * @param dateStr
	 * @return
	 */
	private LocalDate convertDate(final String dateStr){
		LocalDate date = null;
		
		if((dateStr != null) && (!"".equals(dateStr))){
			
			try{
				date = LocalDate.parse(dateStr, GOODREADS_DATE_FORMAT);
			}catch(Exception e){
				logger.error("Unable to parse date: {}", dateStr, e);
			}
		}
		
		return date;
	}
	
	/**
	 * Cleans up the ISBN data
	 * @param isbn
	 * @return
	 */
	private String parseISBN(final String isbnInput){
		String isbn = isbnInput;
		
		/* The ISBN seems to be of the form ="<NUMBER>. Knowing very little
		 * about CSV formats, I'm not sure if this is meant to indicated
		 * something special or what, but I'm simply stripping it off and
		 * returning the number portion as a string for now.
		 * 
		 */
		final String LEADING_TOKEN = "=\"";
		if(isbnInput != null){
			if(isbnInput.startsWith(LEADING_TOKEN)){
				if(LEADING_TOKEN.equals(isbnInput)){
					isbn = "";
				}else{
					isbn = isbnInput.substring(LEADING_TOKEN.length(), 
							isbnInput.length());
					logger.debug("Converting {} into {}", isbnInput, isbn);
				}
			}
		}
		
		return isbn;
	}
	
	/**
	 * Special Handling turning the read count into an integer.
	 * 
	 * TODO can the be pulled out into an enricher? It doesn't really belong here
	 * @param readCountStr
	 * @return
	 */
	private Integer parseReadCount(String readCountStr){
		/* This handling basically covers my own personal case where I don't
		 * remember the exact number of times I read a book and mark it as N+
		 * to indicate I know I've read it at least N times, but may have read
		 * it N+1 times. For simplicity I will simply parse this as N.
		 * 
		 * However like most things, the field is a text field, not a combo box
		 * so the user can put in whatever they want.
		 */
		// default to zero
		Integer readCount = 0;
		
		if(readCountStr != null){
			if(readCountStr.endsWith("+")){
				readCountStr = readCountStr.substring(0,
						readCountStr.length()-1);
			}
			
			Integer parseCount = parseInt(readCountStr);
			
			if(parseCount != null){
				readCount = parseCount;
			}
		}
		
		return readCount;
	}
	
	/**
	 * Null safe integer parse. 
	 * 
	 * TODO Is there a 3rd party utility that could be used instead? Guava perhaps?
	 * @param input
	 * @return
	 */
	private Integer parseInt(final String input){
		Integer intVal = null;
		
		if((input != null) && (!"".equals(input))){
			try{
				intVal = Integer.parseInt(input);
			}catch(Exception e){
				logger.error("{} failed to parse as Integer", input);
			}
		}
		
		return intVal;
	}
	
	/**
	 * Null safe float parse. 
	 * 
	 * TODO Is there a 3rd party utility that could be used instead? Guava perhaps?
	 * @param input
	 * @return
	 */
	private Float parseFloat(final String input){
		Float floatVal = null;
		
		if((input != null) && (!"".equals(input))){
			try{
				floatVal = Float.parseFloat(input);
			}catch(Exception e){
				logger.error("{} failed to parse as Float", input, e);
			}
		}
		
		return floatVal;
	}
	
	/**
	 * 
	 * @param addContStr
	 * @return
	 */
	private List<Contributor> parseAdditionalContributors(final String addContStr){
		List<Contributor> contributors = null;
		
		if(addContStr != null){
			contributors = new ArrayList<Contributor>();
			CSVParser parser = new CSVParser();
			try {
				String[]  tokens = parser.parseLine(addContStr);
				for(String contributor: tokens){
					/* TODO these are going to be artists, narrators, 
					 * additional authors and possibly editors. I don't
					 * think we can parse this, though we might be able to 
					 * guess based on the Binding and/or a user's shelves.
					 * 
					 * For now everyone gets to be "Unknown". Maybe add in
					 * data enrichment post parsing? 
					 */
					if((contributor != null) && (!"".equals(contributor))){
						contributors.add(getContributor(contributor,
								ContributorRole.UNKNOWN));
					}
				}
			}catch (IOException e) {
				logger.error("Problem parsing additional contributors", e);
			}
		}
		
		return contributors;
	}
	
	/**
	 * Parse the string into a {@link Contributor}
	 * @param contStr
	 * @param
	 * @return
	 */
	private Contributor getContributor(final String contStr, 
			final ContributorRole role){
		Contributor author = null;
		
		if((contStr != null) && (!"".equals(contStr))){
			String[] nameTokens = contStr.split(" ");
			
			String firstName = contStr;
			String middleName = null;
			String lastName = null;
			
			if(nameTokens.length == 2){
				firstName = nameTokens[0];
				lastName = nameTokens[1];
			}else if(nameTokens.length == 3){
				firstName = nameTokens[0];
				middleName = nameTokens[1];
				lastName = nameTokens[2];
			}else{
				logger.debug("Special handling of {} contributor tokens for string: {}", 
						nameTokens.length, contStr);
				/* TODO Can this be made less hacky? For now it kind of 
				 * works. Some of these authors have extra spaces in the 
				 * middle that cause the split to screw up. This is probably due 
				 * to the way Goodreads handles multiple authors with the same name.
				 * Each new author gets an extra space and it's really just guess and 
				 * check at that point. 
				 * 
				 * I'm simply looking for how many tokens I have and guessing..
				 */
				List<String> foundTokens = new ArrayList<>();
				for(String token: nameTokens){
					if(token != null && !"".equals(token)){
						foundTokens.add(token);
					}
				}
				
				if(foundTokens.size() == 1){
					firstName = foundTokens.get(0);
				}else if(foundTokens.size() == 2){
					firstName = foundTokens.get(0);
					lastName = foundTokens.get(1);
				}else if(foundTokens.size() == 3){
					firstName = foundTokens.get(0);
					middleName = foundTokens.get(1);
					lastName = foundTokens.get(2);
				}else if(foundTokens.size() > 3){
					logger.debug("Found {} tokens: {}", foundTokens.size(), 
							foundTokens);
					
					firstName = foundTokens.get(0);
					middleName = foundTokens.get(1);
					
					/* assume the remaining tokens are part of the last name?
					 * ie "Le Guin"
					 * TODO Better way to handle this?
					 */
					StringBuilder sb = new StringBuilder();
					for(int i = 2;i<foundTokens.size();i++){
						sb.append(foundTokens.get(i));
						
						if(i+1<foundTokens.size()){
							// restore the space
							sb.append(" ");
						}
					}
					
					lastName = sb.toString();
				}
			}
			
			author = new Contributor(firstName, middleName, lastName,
					ContributorRole.AUTHOR);
		}else{
			logger.error("Contributor String is null/empty");
		}
		
		return author;
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
	private Set<Integer> getYearsRead(LocalDate dateRead, List<String> shelfList){
		Set<Integer> yearsRead = new HashSet<>();
		
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
	 * Helper to convert comma separated string into list
	 * @param tokenizedList
	 * @return
	 */
	private List<String> getList(final String tokenizedList){
		List<String> list = null;
		
		if(tokenizedList != null){
			list = new ArrayList<String>();
			CSVParser parser = new CSVParser();
			try {
				String[]  tokens = parser.parseLine(tokenizedList);
				for(String contributor: tokens){
					list.add(contributor.trim());
				}
			}catch (IOException e) {
				logger.error("Problem parsing list", e);
			}
		}
		
		return list;
	}

}
