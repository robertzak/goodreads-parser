package noorg.bookparsing.enrich;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import noorg.bookparsing.domain.Book;
import noorg.bookparsing.domain.types.BookGenre;

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
 * <p>This is an attempt to use the user's bookshelves to get the genre of
 * the book. See {@link BookGenre} for more about the genres.
 * 
 * This is going to largely depend on how a user shelves their books. What
 * I'm writing will work for my books, but probably not for everyone.
 * 
 * I'm open to suggestions on how to do this better.
 * 
 * @author Robert J. Zak
 *
 */
public class GenreEnricher extends AbstractBookEnricher {
	private static final Logger logger = LoggerFactory.getLogger(GenreEnricher.class);
	
	// Some possible shelves that might be used to guess the genre
	private static final String SHELF_NON_FICTION = "non-fiction";
	private static final String SHELF_SCI_FI = "sci-fi";
	private static final String SHELF_SCIENCE_FICTION = "science-fiction";

	@Override
	protected void enrichBook(final Book book) {
		BookGenre genre = BookGenre.UNKNOWN;
		final List<String> bookshelves = book.getBookshelves();
		
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
		
		book.setGenre(genre);
	}

}
