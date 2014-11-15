package noorg.bookparsing.report.sort;

import java.util.Calendar;
import java.util.Comparator;

import noorg.bookparsing.domain.Book;


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
 * <p>Compare two books by read date in ascending order.
 * 
 * @author Robert J. Zak
 *
 */
public class AscendingDateReadComparator implements Comparator<Book> {

	@Override
	public int compare(Book book1, Book book2) {
		int val = 0;
		
		if(book1 == null || book2 == null){
			throw new IllegalArgumentException("Can't compare null books!");
		}
		
		final Calendar b1Read = book1.getDateRead();
		final Calendar b2Read = book2.getDateRead();
		
		// TODO why doesn't this seem to be working? Debug the dates..
		
		if(b1Read != null){
			if(b2Read != null){
				// neither is null, use calendar function
				val = b1Read.compareTo(b2Read);
			}else{
				// book2's read date is null, but book1's isn't. book1 is first
				val = 1;
			}
		}else if(book2 != null){
			// book1's read date is null, but book2's isn't. book2 is first
			val = -1;
		}
		
		return val;
	}

}
