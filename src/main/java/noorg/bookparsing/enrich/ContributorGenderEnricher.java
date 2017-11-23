package noorg.bookparsing.enrich;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import noorg.bookparsing.domain.Book;
import noorg.bookparsing.domain.Contributor;
import noorg.bookparsing.domain.types.ContributorGender;

/**
 * 
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
 * <p>This enricher will look at the book's shelves and set the author's gender
 * if there is only one contributor and a gender shelf is present.
 * 
 * <p>This code is largely dependent on the user's shelves, or more specifically
 * how I shelve my books, and may not be very useful to someone else.
 * 
 * @author Robert J. Zak
 *
 */
public class ContributorGenderEnricher extends AbstractBookEnricher {
	private static final Logger logger = LoggerFactory.getLogger(ContributorGenderEnricher.class);
	
	private static final String AUTHOR_MALE = "author-male";
	private static final String AUTHOR_FEMALE = "author-female";
	
	@Override
	protected void enrichBook(final Book book) {
			
		final List<String> shelves = book.getBookshelves();
		
		boolean authorMale = false;
		boolean authorFemale = false;
		if(shelves != null){
			authorMale = shelves.contains(AUTHOR_MALE);
			authorFemale = shelves.contains(AUTHOR_FEMALE);
		}
		
		// Ensure only 1 gender was found
		Contributor author = book.getAuthor();
		if(authorMale && !authorFemale){ // only male
			author.setGender(ContributorGender.MALE);
		}else if(authorFemale && !authorMale){ // only female
			author.setGender(ContributorGender.FEMALE);
		}else if (!authorMale && !authorFemale){ // no gender shelf at all
			if(book.getReadCount() > 0) {
				logger.info("Not setting Author Gender for {}, authorMale={}, authorFemale={}", 
					book, authorMale, authorFemale);
			}
		}
	}

}
