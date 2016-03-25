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
 * <p>Indicates the condition of a book you own.
 * 
 * @author Robert J. Zak
 *
 */
public enum BookCondition {
	UNSPECIFIED, BRAND_NEW, LIKE_NEW, VERY_GOOD, GOOD, ACCEPTABLE, POOR;
	
	private static final String GR_UNSPECIFIED = "unspecified";
	private static final String GR_BRAND_NEW = "Brand new";
	private static final String GR_LIKE_NEW = "Like new";
	private static final String GR_VERY_GOOD = "Very good";
	private static final String GR_GOOD = "Good";
	private static final String GR_ACCEPTABLE = "Acceptable";
	private static final String GR_POOR = "Poor";
	
	
	public static BookCondition parse(final String conditionStr){
		BookCondition condition = UNSPECIFIED;
		
		if(conditionStr != null){
			switch(conditionStr){
			case GR_BRAND_NEW:
				condition = BRAND_NEW;
				break;
			case GR_LIKE_NEW:
				condition = LIKE_NEW;
				break;
			case GR_VERY_GOOD:
				condition = VERY_GOOD;
				break;
			case GR_GOOD:
				condition = GOOD;
				break;
			case GR_ACCEPTABLE:
				condition = ACCEPTABLE;
				break;
			case GR_POOR:
				condition = POOR;
				break;
			case GR_UNSPECIFIED:
			default:
				condition = UNSPECIFIED;
			}
		}
		
		return condition;
	}
}
