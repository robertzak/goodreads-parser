package noorg.bookparsing.domain.types;


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
 * <p>An enum for Star ratings.
 * 
 * @author Robert J. Zak
 *
 */
public enum BookRating {
	//TODO add half stars even though good reads doesn't support it?
	ONE_STAR(1), TWO_STARS(2), THREE_STARS(3), FOUR_STARS(4), FIVE_STARS(5);
	
	private int rating;
	
	private BookRating(final int rating){
		this.rating = rating;
	}
	
	public int getRatingAsInt(){
		return rating;
	}
	
	// TODO parsing/lookup for int->ENUM value
}
