package noorg.bookparsing.report;

import java.util.List;

import noorg.bookparsing.domain.Book;
import noorg.bookparsing.report.format.BookFormatter;


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
 * <p>Basic interface for a service to generate some kind of report from your list
 * of books.
 * 
 * @author Robert J. Zak
 *
 */
public interface ReportService {

	/**
	 * Generate a Report from the given list of {@link Book}s using
	 * the provided {@link BookFormatter}
	 * 
	 * @param books
	 * @param formatter
	 */
	public String generateReport(final List<Book> books, 
			BookFormatter formatter);
}
