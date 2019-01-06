package noorg.bookparsing.domain.report;

import java.awt.print.Book;
import java.util.List;


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
 * <p>Shared code for any year-to-year comparison reports
 * 
 * @author Robert J. Zak
 * 
 * TODO should we refactor {@link Report} and {@link AbstractReport} into
 * BookReport and AbstractBookReport respectively and remove any references
 * to {@link Book} from {@link Report}? What would that common interface buy us?
 * 
 * For now this will be it's own thing since it's a report about other reports,
 * rather than the books themselves
 *
 */
public abstract class AbstractYearToYearReport{
	private static final String DIVIDER = "-";
	protected static final String YEAR_TOTAL_FORMAT = "%-8s%-8s";
	protected static final String COUNT_PERCENT_FORMAT = "%d (%s%%)";
	
	private final List<YearlyReport> reports;
	
	public AbstractYearToYearReport(List<YearlyReport> reports) {
		super();
		this.reports = reports;
	}
	
	/**
	 * Convert this report to a string
	 * 
	 * @return
	 */
	public String getReport(){
		StringBuilder sb = new StringBuilder();
		
		
		sb.append(getReportLabel()).append(":\n\n");
		sb.append(getReportHeaders()).append("\n");
		sb.append(getDivider()).append("\n");
		
		for(YearlyReport yearlyReport: reports){
			sb.append(getReportRow(yearlyReport)).append("\n");
		}
		
		return sb.toString();
	}
	
	/**
	 * Helper to generate a dividing line between the headers and the data
	 * @return
	 */
	protected String getDivider(){
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<getDividerLength(); i++){
			sb.append(DIVIDER);
		}
		
		return sb.toString();
	}
	
	/**
	 * Get the formatted string for the provided count and the 
	 * percentage of the total read for the given yearly report
	 * 
	 * @param yearlyReport
	 * @param count
	 * @return
	 */
	protected String getCountPercent(final YearlyReport yearlyReport, final int count) {
		final String percent = yearlyReport.getPercentAsString(count);
		return String.format(COUNT_PERCENT_FORMAT, count, percent);
	}
	
	/**
	 * A label for the report type
	 * @return
	 */
	protected abstract String getReportLabel();
	
	/**
	 * The headers for the year to year table
	 * @return
	 */
	protected abstract String getReportHeaders();
	
	/**
	 * Convert the given {@link YearlyReport} into a report row
	 * @param yearlyReport
	 * @return
	 */
	protected abstract String getReportRow(final YearlyReport yearlyReport);
	
	/**
	 * Each concrete report should specify how wide to make the dividing line
	 * based on how wide the report data is.
	 *
	 * @return
	 */
	protected abstract int getDividerLength();
}
