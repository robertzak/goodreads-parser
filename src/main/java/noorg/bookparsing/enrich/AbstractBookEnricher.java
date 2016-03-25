package noorg.bookparsing.enrich;

import java.util.List;

import noorg.bookparsing.domain.Book;
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
 * <p>A simple base class for shared code and based loop/enrich implementation
 * of {@link BookEnricher}
 * 
 * @author Robert J. Zak
 * 
 *
 */
public abstract class AbstractBookEnricher implements BookEnricher {

	@Override
	public void enrichBooks(List<Book> books) {
		if(books != null){
			for(Book book: books){
				enrichBook(book);
			}
		}
	}

	/**
	 * Enrich the book in some fashion.
	 * @param book
	 */
	protected abstract void enrichBook(final Book book);
}
