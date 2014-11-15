package noorg.bookparsing.enrich;

import java.util.List;

import noorg.bookparsing.domain.Book;
import noorg.bookparsing.domain.types.BookFormat;
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
 * <p>This enricher will look at the book's shelves and modify the 
 * book format to be {@link BookFormat#GRAPHIC_NOVEL} if the keywords
 * "graphic-novel" or manga exist.
 * 
 * <p> This code is largely dependent on the user's shelves, or more specifically
 * how I shelve my books, and may not be very useful to someone else.
 * 
 * @author Robert J. Zak
 * 
 *
 */
public class GraphicNovelEnricher extends AbstractBookEnricher {
	private static final String GRAPHIC_NOVEL = "graphic-novel";
	private static final String MANGA = "manga";

	@Override
	protected void enrichBook(Book book) {
		final List<String> shelves = book.getBookshelves();
		
		if(shelves != null){
			for(String shelf: shelves){
				if(shelf.contains(GRAPHIC_NOVEL) || shelf.contains(MANGA)){
					book.setFormat(BookFormat.GRAPHIC_NOVEL);
				}
			}
		}
		
	}

	

}
