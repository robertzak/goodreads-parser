package noorg.bookparsing.report.format;

import java.util.List;

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
 * <p>People are probably going to want to format the book output in different ways. 
 * This simple interface will allow for Reports to be run with different
 * formats for the books.
 * 
 * @author Robert J. Zak
 *
 */
public interface BookFormatter {
	
	/**
	 * Return the book as a List of Strings.
	 * @param book
	 * @return
	 */
	public List<String> format(final Book book);
	
	/**
	 * Return the Headers for the columns of this format.
	 * @return
	 */
	public List<String> getFormatHeaders();
}
