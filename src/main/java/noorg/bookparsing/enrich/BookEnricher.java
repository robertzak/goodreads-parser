/**
 * 
 */
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
 * <p>People may wish to enrich the data in some fashion. I'm not sure having
 * an interface necessarily makes sense as their may be different parameters 
 * required for each implementation, but for now the idea would be you could
 * choose to apply one or more of these enrichers before you generate a report.
 * 
 * @author Robert J. Zak
 *
 */
public interface BookEnricher {

	/**
	 * Use existing book data to further enrich the book.
	 * 
	 * @param books
	 */
	public void enrichBooks(final List<Book> books);
}
