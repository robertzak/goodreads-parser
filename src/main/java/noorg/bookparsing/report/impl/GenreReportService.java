package noorg.bookparsing.report.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import noorg.bookparsing.domain.Book;
import noorg.bookparsing.domain.report.GenreReport;
import noorg.bookparsing.domain.types.BookGenre;
import noorg.bookparsing.report.format.BookFormatter;

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
 * <p>This service will evaluate the list of Books and generate a genre by genre
 * report.
 * 
 * @author Robert J. Zak
 * 
 */
public class GenreReportService extends AbstractReportService {
	private static final Logger logger = LoggerFactory.getLogger(GenreReportService.class);
	
	private Map<BookGenre, GenreReport> reports = new HashMap<>();

	@Override
	public String generateReport(List<Book> books, BookFormatter formatter) {
		
		// Create the Reports
		int totalBooks = 0;
		for(Book book: books){
			
			final BookGenre genre = book.getGenre();
			
			GenreReport report = reports.get(genre);
			if(report == null){
				report = new GenreReport(genre);
				reports.put(genre, report);
			}
			
			if(report.addBook(book)){
				totalBooks++;
			}else{
				logger.debug("Skipped book: {}", formatter.format(book));
			}
		}
		
		// Generate the report output
		StringBuilder sb = new StringBuilder();
		
		sb.append("\n\n**********Genre Report***************");
		sb.append("\nTotal Books: ").append(totalBooks);
		for(GenreReport report: reports.values()){
			sb.append(report.getReport()).append("\n");
		}
		sb.append("*************************************");
		
		
		return sb.toString();
	}

}
