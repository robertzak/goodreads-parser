package noorg.bookparsing.domain.report;

import java.util.List;
import java.util.Map;

import noorg.bookparsing.domain.types.BookFormat;
import noorg.bookparsing.domain.types.BookGenre;
import noorg.bookparsing.domain.types.BookType;

/**
 * <p>Copyright 2016 Robert J. Zak
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
 * <p>This is shared code for a year-to-year report comparing counts (with percentages)
 * for {@link BookType}s (enums) of interest (ex {@link BookGenre} or {@link BookFormat})
 * 
 * @author Robert J. Zak
 *
 */
public abstract class AbstractBookTypeYearToYearReport <T extends BookType> extends
		AbstractYearToYearReport {
	
	private static final String YEAR_TOTAL_FORMAT = "%-8s%-8s";
	private static final String DATA_FORMAT = "%-15s";
	private static final String COUNT_PERCENT_FORMAT = "%d (%s%%)";

	
	public AbstractBookTypeYearToYearReport(List<YearlyReport> reports) {
		super(reports);
	}
	
	/* TODO Add support for excluding keys? The service would need to expose this and pass it 
	 * down to the report. Maybe instead we evaluate every report and exclude any Enum value 
	 * that doesn't appear in *any* of the reports?
	 */

	@Override
	protected String getReportHeaders() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(getReportLabel()).append(":\n\n");
		sb.append(String.format(YEAR_TOTAL_FORMAT, "Year", "Total"));
		sb.append(getEnumHeaders()).append("\n");
		
		return sb.toString();
	}

	/**
	 * Each sub-class should compare One Enum type which is used in the Report Headers, 
	 * and well as 
	 * @return
	 */
	protected abstract T[] getEnumValues();
	
	protected abstract Map<T, Integer> getCountMap(final YearlyReport yearlyReport);

	/**
	 * Helper to generate the Report Headers from the enum values
	 * @return
	 */
	private String getEnumHeaders(){
		StringBuilder sb = new StringBuilder();
		
		final T[] enumsValues = getEnumValues();
		for(T value: enumsValues){
			sb.append(String.format(DATA_FORMAT, value));
		}
		
		return sb.toString();
	}

	@Override
	protected String getReportRow(YearlyReport yearlyReport) {
		StringBuilder sb = new StringBuilder();
		
		T[] enumValues = getEnumValues();
		
		final String year = Integer.toString(yearlyReport.getYear());
		final String total = Integer.toString(yearlyReport.getTotal());
		
		// append the report year and total number of books
		sb.append(String.format(YEAR_TOTAL_FORMAT, year, total));
		
		Map<T, Integer> countMap = getCountMap(yearlyReport);
		for(T key: enumValues){
			Integer count = countMap.get(key);
			if(count == null){
				count = 0;
			}
			
			final String percent = yearlyReport.getPercentAsString(count);
			String line = String.format(COUNT_PERCENT_FORMAT, count, percent);
			
			sb.append(String.format(DATA_FORMAT, line));
		}
		sb.append("\n");
		
		return sb.toString();
	}
}