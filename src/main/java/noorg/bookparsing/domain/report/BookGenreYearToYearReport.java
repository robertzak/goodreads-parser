package noorg.bookparsing.domain.report;

import java.util.List;
import java.util.Map;

import noorg.bookparsing.domain.types.BookGenre;

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
 * <p>Compare {@link BookGenre} between years
 * 
 * @author Robert J. Zak
 *
 */
public class BookGenreYearToYearReport extends AbstractBookCountsYearToYearReport<BookGenre> {

	public BookGenreYearToYearReport(List<YearlyReport> reports) {
		super(reports);
	}

	@Override
	protected Map<BookGenre, Integer> getDataMap(YearlyReport yearlyReport) {
		return yearlyReport.getCountsByGenre();
	}

	@Override
	protected String getReportLabel() {
		return "Genre Counts";
	}

	@Override
	protected int getDividerLength() {
		return 160;
	}
}
