package noorg.bookparsing.report.sort;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import noorg.bookparsing.domain.Book;
import noorg.bookparsing.domain.Contributor;
import noorg.bookparsing.domain.types.ContributorRole;

/**
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
 * <p>Test the {@link AscendingDateReadComparator} class
 * 
 * @author Robert J. Zak
 *
 */
public class AscendingDateReadComparatorTest {
	private static Logger logger = LoggerFactory.getLogger(
			AscendingDateReadComparatorTest.class);
	
	private AscendingDateReadComparator comparator = 
			new AscendingDateReadComparator();

	@Test
	public void testCompare() throws Exception {
		logger.info("testCompare");
		
		final String date1 = "2000-01-15";
		final String date2 = "2005-07-04";
		final String date3 = null;
		
		Book book1 = getTestBook("First Book with date1", date1);
		Book book2 = getTestBook("Book with a later date", date2);
		Book book3 = getTestBook("Second Book with date1", date1);
		Book book4 = getTestBook("Book with null Read Date", date3);
		Book book5 = getTestBook("Another Book with null Read Date", date3);
		
		assertTrue("Book 1 should be first", comparator.compare(book1, book2) < 0);
		assertTrue("Book 1 should be first", comparator.compare(book2, book1) > 0);
		assertEquals("Books should be equal", 0, comparator.compare(book1, book3));
		assertTrue("Book 1 should be first", comparator.compare(book1, book4) < 0);
		assertTrue("Book 1 should be first", comparator.compare(book4, book1) > 0);
		assertEquals("Books should be equal", 0, comparator.compare(book4, book5));
		assertEquals("Books should be equal", 0, comparator.compare(book5, book4));
	}
	
	private Book getTestBook(final String title, final String dateString) throws Exception{
		Book book = new Book();
		book.setTitle(title);
		book.setAuthor(new Contributor("test", "book", "author", ContributorRole.AUTHOR));
		
		if(dateString != null){
			book.setDateRead(LocalDate.parse(dateString));
		}
		
		return book;
	}
}
