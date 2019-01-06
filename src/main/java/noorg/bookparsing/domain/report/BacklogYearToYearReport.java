package noorg.bookparsing.domain.report;

import java.util.List;

/**
 * <p>Copyright 2019 Robert J. Zak
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
 * <p>This report compares the number of books read from your owned backlog
 * 
 * @author Robert J. Zak
 *
 */
public class BacklogYearToYearReport  extends AbstractYearToYearReport {
	private static final int DATA_WIDTH  = 15;
	private static final int NUM_COLUMNS  = 4;
	private final String format;
	

	public BacklogYearToYearReport(final List<YearlyReport> reports) {
		super(reports);
		
		StringBuilder formatBuilder = new StringBuilder();
		formatBuilder.append("%-8s"); // year
		for(int i=0;i<NUM_COLUMNS;i++){
			formatBuilder.append("%-");
			formatBuilder.append(DATA_WIDTH);
			formatBuilder.append("s");
		}
		
		format = formatBuilder.toString();
	}

	@Override
	protected String getReportLabel() {
		return "Backlog Counts";
	}

	@Override
	protected String getReportHeaders() {
		return String.format(format,
				"Year", "Total Read", "Backlog Read", "Backlog Books", "Backlog Audio");
	}

	@Override
	protected String getReportRow(YearlyReport yearlyReport) {		
		return String.format(format,
				yearlyReport.getYear(),
				yearlyReport.getTotal(),
				getCountPercent(yearlyReport, yearlyReport.getTotalBacklogRead()),
				getCountPercent(yearlyReport, yearlyReport.getTotalBacklogBooksRead()),
				getCountPercent(yearlyReport, yearlyReport.getTotalBacklogAudioBooks()));
	}

	@Override
	protected int getDividerLength() {
		return 70;
	}

}
