package noorg.bookparsing.domain.report;

import java.util.List;
import java.util.Map;

import noorg.bookparsing.domain.types.ContributorGender;

/**
 * <p>Copyright 2017 Robert J. Zak
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
 * <p>This Report compares the number of books read by Author Gender between years
 *
 * backlog you can. 
 * 
 * @author Robert J. Zak
 *
 */
public class GenderYearToYearReport extends AbstractBookCountsYearToYearReport<ContributorGender>{

	public GenderYearToYearReport(List<YearlyReport> reports) {
		super(reports);
	}

	@Override
	protected Map<ContributorGender, Integer> getDataMap(
			YearlyReport yearlyReport) {
		return yearlyReport.getCountsByAuthorGender();
	}

	@Override
	protected String getReportLabel() {
		return "Author Gender";
	}

	@Override
	protected int getDividerLength() {
		return 50;
	}
}
