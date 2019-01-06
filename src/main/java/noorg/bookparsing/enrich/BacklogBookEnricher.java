package noorg.bookparsing.enrich;

import java.util.List;


import noorg.bookparsing.domain.Book;

/**
 * <p>Copyright 2019 Robert J. Zak
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
 * <p>This enricher determines if the book has been shelved as coming from your backlog of owned books.
 * 
 * <p>This code is largely dependent on the user's shelves, or more specifically
 * how I shelve my books, and may not be very useful to someone else. I did make it
 * configurable so if you want to use a different shelf name to mark books that come from your
 * backlog you can. 
 * 
 * @author Robert J. Zak
 *
 */
public class BacklogBookEnricher extends AbstractBookEnricher {
	
	private static final String DEFAULT_BACKLOG_BOOK = "own-backlog";
	
	private final String backlogShelfName;
	
	public BacklogBookEnricher() {
		this(DEFAULT_BACKLOG_BOOK);
	}
	
	public BacklogBookEnricher(String backlogShelfName) {
		super();
		this.backlogShelfName = backlogShelfName;
	}

	@Override
	protected void enrichBook(final Book book) {
			
		final List<String> shelves = book.getBookshelves();
		
		boolean fromBacklog = false;
		
		if(shelves != null){
			fromBacklog = shelves.contains(backlogShelfName);
		}
		
		book.setFromBacklog(fromBacklog);
	}

}
