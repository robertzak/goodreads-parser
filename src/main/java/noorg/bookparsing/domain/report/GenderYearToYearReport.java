package noorg.bookparsing.domain.report;

import java.util.List;
import java.util.Map;

import noorg.bookparsing.domain.types.ContributorGender;

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
		// TODO Auto-generated method stub
		return 50;
	}
}
