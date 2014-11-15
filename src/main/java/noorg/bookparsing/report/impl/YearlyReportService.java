package noorg.bookparsing.report.impl;

import java.util.Calendar;
import java.util.List;

import noorg.bookparsing.domain.Book;
import noorg.bookparsing.domain.report.YearlyReport;
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
 * <p>This service will evaluate the list of Books and generate a year by year
 * report.
 * 
 * @author Robert J. Zak
 * 
 */
public class YearlyReportService extends AbstractReportService {

	@Override
	public String generateReport(List<Book> books, BookFormatter formatter) {
		StringBuilder sb = new StringBuilder();
		
		//sb.append("Book Count: ").append(books.size()). append("\n\n");
		
		//sb.append(formatter.getFormatHeaders()).append("\n");
		
		// TODO sort by year? and year end statistics..
		
		
		YearlyReport report = new YearlyReport(2014);
		for(Book book: books){
			//sb.append(formatter.format(book)).append("\n");
			
			Calendar dateRead = book.getDateRead();
			if(dateRead != null){
				// TODO map of years
				int year = dateRead.get(Calendar.YEAR);
				
				if(year == 2014){
					report.addBook(book);
				}
			}else{
				// TODO how to handle unread books?
			}
		}
		
		
		sb.append(report.getReport());
		
		return sb.toString();

	}

}
