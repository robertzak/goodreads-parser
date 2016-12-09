package noorg.bookparsing.domain.report;

import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

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
 * <p>This interface is meant to simplify the types of reports, hopefully.
 * 
 * @author Robert J. Zak
 *
 */
public class DecadeYearToYearReport extends AbstractBookCountsYearToYearReport<Integer> {
	private SortedSet<Integer> decades = new TreeSet<>();

	public DecadeYearToYearReport(List<YearlyReport> reports) {
		super(reports);
		
		// track all represented decades
		for(YearlyReport report: reports){
			Map<Integer, Integer> decadeCounts = report.getCountsByDecadePublished();
			decades.addAll(decadeCounts.keySet());
		}
	}

	@Override
	protected String getReportLabel() {
		return "Decade Counts";
	}

	@Override
	protected Integer[] getDataKeys() {
		return decades.toArray(new Integer[decades.size()]);
	}

	@Override
	protected Map<Integer, Integer> getCountMap(YearlyReport yearlyReport) {
		return yearlyReport.getCountsByDecadePublished();
	}
}
