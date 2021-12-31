package noorg.bookparsing.report.impl;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import noorg.bookparsing.domain.Book;
import noorg.bookparsing.domain.Contributor;
import noorg.bookparsing.report.format.BookFormatter;

/**
 * <p>Copyright 2021 Robert J. Zak
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
 * <p>This service will count the number of books you've read by each {@link Contributor}
 * and output the top N read.
 * 
 * @author Robert J. Zak
 *
 */
public class AuthorCountsReportService extends AbstractReportService {
	final private int authorCount;

	// TODO support strict (ie exact authorCount number of authors) filtering?
	public AuthorCountsReportService(int authorCount) {
		super();
		this.authorCount = authorCount;
	}

	@Override
	public String generateReport(List<Book> books, BookFormatter formatter) {
		StringBuilder sb = new StringBuilder();		
		Map<Contributor, Long> authorCounts = books.stream().filter(b-> (b.getReadCount()> 0)).collect(groupingBy(Book::getAuthor, counting()));
		SortedMap<Long, List<Contributor>> sortedCounts = new TreeMap<>(Collections.reverseOrder());
		
		// TODO add book lists?
		// TODO bug in read count logic? am I counting stuff I DNF/put on hold?
		sb.append("\n\nAuthor Counts:\n");
		sb.append("*****************************************\n");
		for(Contributor contributor: authorCounts.keySet()){
			final long count = authorCounts.get(contributor);
			
			List<Contributor> contributors = sortedCounts.get(count);
			
			if(contributors == null) {
				contributors = new ArrayList<>();
				sortedCounts.put(count, contributors);
			}			
			
			contributors.add(contributor);
		}
		
		int authorsAdded = 0;
		for(Long readCount: sortedCounts.keySet()) {
			List<Contributor> contributors = sortedCounts.get(readCount);
			for(Contributor contributor: contributors) {
				sb.append(authorsAdded + 1).append(": ").append(contributor).append(" - ").append(readCount).append("\n");
			}
			
			if (++authorsAdded >= authorCount) {
				break;
			}
		}
		
		sb.append("*****************************************\n");
		
		return sb.toString();
	}
}
