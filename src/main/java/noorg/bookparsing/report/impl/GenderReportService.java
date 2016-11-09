package noorg.bookparsing.report.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import noorg.bookparsing.domain.Book;
import noorg.bookparsing.domain.Contributor;
import noorg.bookparsing.domain.report.GenderReport;
import noorg.bookparsing.domain.types.ContributorGender;
import noorg.bookparsing.report.format.BookFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * <p>This service will evaluate the list of Books and generate a gender by gender
 * report.
 * 
 * @author Robert J. Zak
 * 
 */
public class GenderReportService extends AbstractReportService {
	private static final Logger logger = LoggerFactory.getLogger(GenderReportService.class);
	
	private Map<ContributorGender, GenderReport> reports = new HashMap<>();

	@Override
	public String generateReport(List<Book> books, BookFormatter formatter) {
		
		/* TODO when YearlyReportService is re-factored, much of this code should move up to AbstractReportService.
		 * It's very boiler plate and most of it is copied right from the GenreReportService
		 * 
		 * loop books
		 * conditionally add a book to 1 or more reports
		 * log the reports
		 */
		// Create the Reports
		int totalBooks = 0;
		for(Book book: books){
			
			final Contributor author = book.getAuthor();
			if(author != null){
				ContributorGender authorGender = author.getGender();
				
				if(authorGender == null){
					logger.debug("Unable to determine author gender: {}", book);
					authorGender = ContributorGender.UNKNOWN;
				}
				
				GenderReport report = reports.get(authorGender);
				if(report == null){
					report = new GenderReport(authorGender);
					reports.put(authorGender, report);
				}
				
				if(report.addBook(book)){
					totalBooks++;
				}else{
					logger.debug("Skipped book: {}", formatter.format(book));
				}
				
			} // else skip
		}
		
		// Generate the report output
		StringBuilder sb = new StringBuilder();
		
		sb.append("\n\n**********Gender Report***************");
		sb.append("\nTotal Books: ").append(totalBooks);
		for(GenderReport report: reports.values()){
			sb.append(report.getReport()).append("\n");
		}
		sb.append("*************************************");
		
		
		return sb.toString();
	}
}
