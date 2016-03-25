package noorg.bookparsing.domain;

import noorg.bookparsing.domain.types.ContributorRole;


/**
 * <p>Copyright 2014-2016 Robert J. Zak
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
 * <p>A Contributor is just a person who contributed to the book somehow.
 * 
 * @author Robert J. Zak
 *
 */
public class Contributor {
	private String firstName;
	private String middleName;
	private String lastName;
	private ContributorRole role;
	
	/**
	 * Create a blank Contributor
	 */
	public Contributor(){}
	
	/**
	 * Create a new Contributor with the given name and role.
	 * 
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 * @param role
	 */
	public Contributor(final String firstName, final String middleName, 
			final String lastName, final ContributorRole role) {
		super();
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.role = role;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public ContributorRole getRole() {
		return role;
	}
	
	public void setRole(ContributorRole role) {
		this.role = role;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contributor other = (Contributor) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (role != other.role)
			return false;
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		
		sb.append(firstName).append(" ");
		if((middleName != null) && (!"".equals(middleName))){
			sb.append(middleName).append(" ");
		}
		sb.append(lastName);
				
		return sb.toString();
	}

	public String debugString() {
		final StringBuilder sb = new StringBuilder();
		
		sb.append("Contributor [firstName=").append(firstName);
		sb.append(", middleName=").append(middleName);
		sb.append(", lastName=").append(lastName);
		sb.append(", role=").append(role).append( "]");
		
		return sb.toString();
	}
	
	
}
