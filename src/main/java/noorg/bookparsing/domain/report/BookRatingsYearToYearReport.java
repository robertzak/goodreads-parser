package noorg.bookparsing.domain.report;

import java.util.List;
import java.util.Map;

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
 * <p>Compare the ratings given between years
 * 
 * @author Robert J. Zak
 *
 */
public class BookRatingsYearToYearReport extends AbstractBookCountsYearToYearReport<Integer> {

	public BookRatingsYearToYearReport(List<YearlyReport> reports) {
		super(reports);
	}

	@Override
	protected Map<Integer, Integer> getDataMap(YearlyReport yearlyReport) {
		return yearlyReport.getCountsByRating();
	}

	@Override
	protected String getReportLabel() {
		return "Book Ratings";
	}

	@Override
	protected int getDividerLength() {	
		return 85;
	}
}
