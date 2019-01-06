package noorg.bookparsing.domain.report;

import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

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
 * 
 * @author Robert J. Zak
 *
 */
public abstract class AbstractBookCountsYearToYearReport <T> extends
		AbstractYearToYearReport {
	private static final int DEFAULT_DATA_WIDTH = 15;
	private final String DATA_FORMAT;
	
	private SortedSet<T> dataKeys = new TreeSet<>();

	/**
	 * Generate a year-to-year report using the {@link #DEFAULT_DATA_WIDTH}
	 * @param reports
	 */
	public AbstractBookCountsYearToYearReport(final List<YearlyReport> reports){
		this(reports, DEFAULT_DATA_WIDTH);
	}
	
	/**
	 * Generate a year-to-year report for the given data width
	 * @param reports
	 * @param dataWidth
	 */
	public AbstractBookCountsYearToYearReport(final List<YearlyReport> reports, final int dataWidth) {
		super(reports);
		
		DATA_FORMAT = "%-" + dataWidth + "s";
		
		// Initialize Data Keys
		for(YearlyReport yearlyReport: reports){
			Map<T, Integer> dataMap = getDataMap(yearlyReport);
			dataKeys.addAll(dataMap.keySet());
		}
	}

	@Override
	protected String getReportHeaders() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(String.format(YEAR_TOTAL_FORMAT, "Year", "Total"));
		sb.append(getDataHeaders());
		
		return sb.toString();
	}
	
	/**
	 * Return the appropriate data map for the given yearly report
	 * @param yearlyReport
	 * @return
	 */
	protected abstract Map<T, Integer> getDataMap(final YearlyReport yearlyReport);

	/**
	 * Helper to generate the Report Headers from the data keys
	 * @return
	 */
	private String getDataHeaders(){
		StringBuilder sb = new StringBuilder();
		
		for(T value: dataKeys){
			sb.append(String.format(DATA_FORMAT, value));
		}
		
		return sb.toString();
	}

	@Override
	protected String getReportRow(YearlyReport yearlyReport) {
		StringBuilder sb = new StringBuilder();
		
		
		final String year = Integer.toString(yearlyReport.getYear());
		final String total = Integer.toString(yearlyReport.getTotal());
		
		// append the report year and total number of books
		sb.append(String.format(YEAR_TOTAL_FORMAT, year, total));
		
		Map<T, Integer> countMap = getDataMap(yearlyReport);
		for(T key: dataKeys){
			Integer count = yearlyReport.getMapCount(countMap, key);
			sb.append(String.format(DATA_FORMAT, getCountPercent(yearlyReport, count)));
		}
		
		return sb.toString();
	}
}
