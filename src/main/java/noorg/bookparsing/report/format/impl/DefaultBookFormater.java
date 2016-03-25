package noorg.bookparsing.report.format.impl;

import static noorg.bookparsing.util.Utils.getString;

import java.util.ArrayList;
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
 * <p> This is the default format I wrote for myself. If you want a different
 * format, simply write your own implementation of {@link BookFormatter} rather
 * than modifying this formatter.
 * 
 *  It's of the form: Title, Author, Date Read, Rating, Genre, Length
 *  
 *	@author Robert J. Zak
 */
public class DefaultBookFormater extends AbstractBookFormatter {

	@Override
	public List<String> format(Book book) {
		List<String> columns = new ArrayList<>();
		
		columns.add(book.getTitle());
		columns.add(getString(book.getAuthor()));
		columns.add(getMonthDayYear(book.getDateRead()));
		columns.add(getString(book.getMyRating()));
		columns.add(getString(book.getGenre()));
		columns.add(getString(book.getNumberOfPages()));
		
		
		return columns;
	}

	@Override
	public List<String> getFormatHeaders() {
		List<String> columns = new ArrayList<>();
		
		columns.add("Title");
		columns.add("Author");
		columns.add("Date Read");
		columns.add("Rating");
		columns.add("Genre");
		columns.add("Length");
		
		return columns;
	}

}
