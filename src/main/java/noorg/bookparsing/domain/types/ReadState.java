package noorg.bookparsing.domain.types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
 * <p>Indicate the state of the book.
 * 
 * @author Robert J. Zak
 *
 */
public enum ReadState {
	ABANDONED, CURRENTLY_READING, ON_HOLD, READ, TO_READ, UNKNOWN;
	
	private static final Logger logger = LoggerFactory.getLogger(
			ReadState.class);

	private static final String CUSTOM_ABANDONED = "abandoned";
	// For the Sword & Laser folks
	private static final String CUSTOM_LEMMED = "lemmed";
	private static final String CUSTOM_ON_HOLD = "on-hold";
	private static final String GR_CURRENTLY_READING = "currently-reading";
	private static final String GR_READ = "read";
	private static final String GR_TO_READ = "to-read";
	
	/**
	 * Convert a string to the read state from the 'exclusive' shelf.
	 * If the user has any custom exclusive shelves the result will likely
	 * be {@link #UNKNOWN}. 
	 * 
	 * Support can be added custom shelves as needed, but should probably be
	 * done minimally.
	 * 
	 * @param exclusiveShelf
	 * @return
	 */
	public static ReadState parse(final String exclusiveShelf){
		ReadState state = UNKNOWN;
		
		if(exclusiveShelf != null){
			switch(exclusiveShelf){
			case CUSTOM_ABANDONED:
			case CUSTOM_LEMMED:
				state = ABANDONED;
				break;
			case CUSTOM_ON_HOLD:
				state = ON_HOLD;
				break;
			case GR_CURRENTLY_READING:
				state = CURRENTLY_READING;
				break;
			case GR_READ:
				state = READ;
				break;
			case GR_TO_READ:
				state = TO_READ;
				break;
				default:
					logger.warn("{} is not a supported state", exclusiveShelf);
				state = UNKNOWN;
			}
		}
		
		return state;
	}
}
