package noorg.bookparsing.domain.types;


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
 * <p>An enum to define basic book genres.
 *	 
 * @author Robert J. Zak
 *
 */
public enum BookGenre{
	/* TODO what other genres? And NO, Young Adult is NOT a Genre.
	 * 
	 * Also, we should avoid sub-genres like Urban Fantasy or 
	 * Paranormal Romance in my opinion..
	 * TODO Make Steampunk a subgenre of fantasy, or leave at its own thing?
	 */
	FANTASY, HISTORICAL, HORROR, HUMOR, MYSTERY, NONFICTION, ROMANCE, STEAMPUNK, SCIFI, THRILLER, UNKNOWN;

}
