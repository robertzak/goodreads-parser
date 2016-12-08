package noorg.bookparsing.domain.report;

import noorg.bookparsing.domain.Book;
import noorg.bookparsing.report.format.BookFormatter;
import noorg.bookparsing.report.format.impl.DefaultBookFormater;


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
 * <p>This interface is meant to simplify the types of reports, hopefully.
 * 
 * @author Robert J. Zak
 *
 */
public interface Report {
	
	/**
	 * Add a book to the report;
	 * @param book
	 * @return true if added
	 */
	public boolean addBook(final Book book);
	
	/**
	 * Convert this report to a string using the {@link DefaultBookFormater}
	 * 
	 * @return
	 */
	public String getReport();
	
	/**
	 * Convert this report to a string using the given formatter;
	 * 
	 * @param formatter
	 * 
	 * @return
	 */
	public String getReport(final BookFormatter formatter);
}
