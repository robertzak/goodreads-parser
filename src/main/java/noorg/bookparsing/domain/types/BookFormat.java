package noorg.bookparsing.domain.types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
 * <p>Basic format types. Maybe expand to sub types like Kindle or Mass Market Paperback, etc?
 * 
 * @author Robert J. Zak
 *
 */
public enum BookFormat implements BookType{
	// I'm squishing these down into fewer formats.
	AUDIO_BOOK, BOOK, EBOOK, GRAPHIC_NOVEL, UNKNOWN;
	
	private static final Logger logger = LoggerFactory.getLogger
			(BookFormat.class);
	
	// KNOWN Goodreads types
	private final static String  AUDIBLE = "Audible Audio";
	private final static String  AUDIO = "Audio";
	private final static String  AUDIOBOOK = "Audiobook";
	private final static String  AUDIO_CASSETTE = "Audio Cassette";
	private final static String  AUDIO_CD = "Audio CD";
	private final static String  COMIC_BOOK = "Comic Book";	
	private final static String  E_BOOK = "ebook";
	private final static String  GRAPHICNOVEL = "Graphic Novel";
	private final static String  HARDCOVER = "Hardcover";
	private final static String  KINDLE = "Kindle Edition";
	private final static String  LEATHER_BOUND = "Leather Bound";
	private final static String  LIBRARY_BINDING = "Library Binding";
	private final static String  MMPB = "Mass Market Paperback";
	private final static String  MP3_CD = "MP3 CD";
	private final static String  NOOK = "Nook";	
	private final static String  PAPERBACK = "Paperback";
	private final static String  PODIOBOOK = "Podiobook";
	
	
	/**
	 * Helper to convert Good Reads binding types into a {@link BookFormat}
	 * @param binding
	 * @return
	 */
	public static BookFormat parse(final String binding){
		BookFormat format = UNKNOWN;
		
		if(binding != null){
			switch(binding){
			case AUDIBLE:
			case AUDIO:
			case AUDIOBOOK:
			case AUDIO_CASSETTE:
			case AUDIO_CD:
			case PODIOBOOK:
			case MP3_CD:
				format = AUDIO_BOOK;
				break;
			case E_BOOK:
			case KINDLE:
			case NOOK:
				format = EBOOK;
				break;
			case COMIC_BOOK:
			case GRAPHICNOVEL:
				format = GRAPHIC_NOVEL;
				break;
			case HARDCOVER:
			case LEATHER_BOUND:
			case LIBRARY_BINDING:
			case MMPB:
			case PAPERBACK:
				format = BOOK;
				break;
			default:
				logger.debug("{} is not a supported binding type", binding);
			}
		}
		
		return format;
	}
}
