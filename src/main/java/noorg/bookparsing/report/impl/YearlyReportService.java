package noorg.bookparsing.report.impl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import noorg.bookparsing.domain.Book;
import noorg.bookparsing.domain.report.YearlyReport;
import noorg.bookparsing.report.format.BookFormatter;


/**
 * <p>Copyright 2014-2016 Robert J. Zak
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
 * <p>This service will evaluate the list of Books and generate a year by year
 * report.
 * 
 * @author Robert J. Zak
 * 
 */
public class YearlyReportService extends AbstractReportService {
	/* TODO change to support set of years
	 * Generate a YearlyReport for each year, then generate
	 * statistics comparing those years 
	 * 
	 * (ex: deltas between counts for each year, mins, maxes, averages, etc)
	 */
	private final SortedSet<Integer> reportYears;
	

	public YearlyReportService(final Integer... reportYears) {
		super();
		this.reportYears = new TreeSet<>(Arrays.asList(reportYears));
	}


	@Override
	public String generateReport(List<Book> books, BookFormatter formatter) {
		StringBuilder sb = new StringBuilder();
		
		// TODO year end statistics..
		
		sb.append("Yearly Reports:\n");
		for(Integer reportYear: reportYears){
		
			YearlyReport report = new YearlyReport(reportYear);
			for(Book book: books){
				
				boolean bookAdded = false;
				if(book.getYearsRead().contains(reportYear)){
					report.addBook(book);
					bookAdded = true;
				}
				
				// Attempt to determine re-reads.
				if(bookAdded){
					
					/* Check if this book has a read date different from report year.
					 * This will rely on the user shelving data a certain way.
					 */
					LocalDate dateRead = book.getDateRead();
					if(dateRead != null){
						final int yearRead = dateRead.getYear();
						if(yearRead != reportYear){
							report.addRereadBook(book);
						}
					}
					
					// Use their readCount (this may be more accurate)
					final Integer readCount = book.getReadCount();
					if(readCount != null && readCount> 1){
						report.addRereadBook(book);
					}
				}
			}
			
			sb.append("*****************************************\n");
			sb.append(report.getReport());
		}
		
		return sb.toString();
	}

}
