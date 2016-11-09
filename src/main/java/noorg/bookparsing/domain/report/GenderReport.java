package noorg.bookparsing.domain.report;

import noorg.bookparsing.domain.types.ContributorGender;

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
 * <p>A genre report is used to generate overall details about your reading 
 * for that genre. 
 * 
 * This includes: 
 * 
 * <ul>
 *  <li>Total Count</li>
 *  <li>Count per year</li>
 * 	<li>tbd..</li>
 * </ul>
 * 
 * @author Robert J. Zak
 *
 */
public class GenderReport extends AbstractMultipleYearReport {
	private ContributorGender gender;
	
	public GenderReport(ContributorGender gender) {
		super();
		this.gender = gender;
	}

	@Override
	protected String getReportHeader() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Gender: ").append(gender);
		
		return sb.toString();
	}

}
