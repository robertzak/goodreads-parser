package noorg.bookparsing.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import noorg.bookparsing.domain.types.BookCondition;
import noorg.bookparsing.domain.types.BookFormat;
import noorg.bookparsing.domain.types.BookGenre;
import noorg.bookparsing.domain.types.ReadState;

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
 * <p>The main domain object used to describe a book
 * 
 * @author  Robert J. Zak
 *
 */
public class Book {

	private String id;
	private String title;
	private Contributor author;	
	/**
	 * TODO catch all list, sub classes with specific names?
	 */
	private List<Contributor> additionalContributors;
	private String isbn;
	private String isbn13;
	// TODO should this be an enum?
	//private BookRating myRating;
	private Integer myRating;
	// TODO should I make this an enum?
	private Float averageRating;
	private String publisher;
	private String binding;
	private Integer numberOfPages;
	private Integer yearOfPublication;
	private Integer originalPublicationYear;
	private LocalDate dateRead;
	private LocalDate dateAdded;
	private List<String> bookshelves;
	private List<String> bookshelvesWithPositions;
	private String exclusiveShelf;
	private ReadState readState;
	private String myReview;
	private String spoiler;
	private String privateNotes;
	private Integer readCount;
	private String recommendedFor;
	private String recommendedBy;
	private Integer ownedCopies;
	private LocalDate purchaseDate;
	private String purchaseLocation;
	private BookCondition condition;
	private String conditionDescription;
	private String bcid;
	
	// Inferred/converted data
	private BookFormat format;
	private BookGenre genre;
	private Set<Integer> yearsRead = new HashSet<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Contributor getAuthor() {
		return author;
	}

	public void setAuthor(Contributor author) {
		this.author = author;
	}	
	
	public List<Contributor> getAdditionalContributors() {
		return additionalContributors;
	}

	public void setAdditionalContributors(List<Contributor> additionalContributors) {
		this.additionalContributors = additionalContributors;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getIsbn13() {
		return isbn13;
	}

	public void setIsbn13(String isbn13) {
		this.isbn13 = isbn13;
	}

	public Integer getMyRating() {
		return myRating;
	}

	public void setMyRating(Integer myRating) {
		this.myRating = myRating;
	}

	public Float getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(Float averageRating) {
		this.averageRating = averageRating;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getBinding() {
		return binding;
	}

	public void setBinding(String binding) {
		this.binding = binding;
	}

	public Integer getNumberOfPages() {
		return numberOfPages;
	}

	public void setNumberOfPages(Integer numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	public Integer getYearOfPublication() {
		return yearOfPublication;
	}

	public void setYearOfPublication(Integer yearOfPublication) {
		this.yearOfPublication = yearOfPublication;
	}

	public Integer getOriginalPublicationYear() {
		return originalPublicationYear;
	}

	public void setOriginalPublicationYear(Integer originalPublicationYear) {
		this.originalPublicationYear = originalPublicationYear;
	}

	public LocalDate getDateRead() {
		return dateRead;
	}

	public void setDateRead(LocalDate dateRead) {
		this.dateRead = dateRead;
	}

	public LocalDate getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(LocalDate dateAdded) {
		this.dateAdded = dateAdded;
	}
	
	public boolean addYearRead(Integer year){
		return yearsRead.add(year);
	}

	public Set<Integer> getYearsRead() {
		return yearsRead;
	}

	public void setYearsRead(Set<Integer> yearsRead) {
		this.yearsRead = yearsRead;
	}

	public List<String> getBookshelves() {
		return bookshelves;
	}

	public void setBookshelves(List<String> bookshelves) {
		this.bookshelves = bookshelves;
	}

	public List<String> getBookshelvesWithPositions() {
		return bookshelvesWithPositions;
	}

	public void setBookshelvesWithPositions(List<String> bookshelvesWithPositions) {
		this.bookshelvesWithPositions = bookshelvesWithPositions;
	}

	public String getExclusiveShelf() {
		return exclusiveShelf;
	}

	public void setExclusiveShelf(String exclusiveShelf) {
		this.exclusiveShelf = exclusiveShelf;
	}

	public ReadState getReadState() {
		return readState;
	}

	public void setReadState(ReadState readState) {
		this.readState = readState;
	}

	public String getMyReview() {
		return myReview;
	}

	public void setMyReview(String myReview) {
		this.myReview = myReview;
	}

	public String getSpoiler() {
		return spoiler;
	}

	public void setSpoiler(String spoiler) {
		this.spoiler = spoiler;
	}

	public String getPrivateNotes() {
		return privateNotes;
	}

	public void setPrivateNotes(String privateNotes) {
		this.privateNotes = privateNotes;
	}

	public Integer getReadCount() {
		return readCount;
	}

	public void setReadCount(Integer readCount) {
		this.readCount = readCount;
	}

	public String getRecommendedFor() {
		return recommendedFor;
	}

	public void setRecommendedFor(String recommendedFor) {
		this.recommendedFor = recommendedFor;
	}

	public String getRecommendedBy() {
		return recommendedBy;
	}

	public void setRecommendedBy(String recommendedBy) {
		this.recommendedBy = recommendedBy;
	}

	public Integer getOwnedCopies() {
		return ownedCopies;
	}

	public void setOwnedCopies(Integer ownedCopies) {
		this.ownedCopies = ownedCopies;
	}

	public LocalDate getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(LocalDate purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getPurchaseLocation() {
		return purchaseLocation;
	}

	public void setPurchaseLocation(String purchaseLocation) {
		this.purchaseLocation = purchaseLocation;
	}

	public BookCondition getCondition() {
		return condition;
	}

	public void setCondition(BookCondition condition) {
		this.condition = condition;
	}

	public String getConditionDescription() {
		return conditionDescription;
	}

	public void setConditionDescription(String conditionDescription) {
		this.conditionDescription = conditionDescription;
	}

	public String getBcid() {
		return bcid;
	}

	public void setBcid(String bcid) {
		this.bcid = bcid;
	}

	public BookFormat getFormat() {
		return format;
	}

	public void setFormat(BookFormat format) {
		this.format = format;
	}

	public BookGenre getGenre() {
		return genre;
	}

	public void setGenre(BookGenre genre) {
		this.genre = genre;
	}

	/**
	 * Dumps the whole object as a string
	 * @return
	 */
	public String debugString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Book [id=").append(id);
		sb.append(", title=").append(title);
		sb.append(", author=").append(author);
		sb.append(", additionalContributors=").append(additionalContributors);
		sb.append(", isbn=").append(isbn);
		sb.append(", isbn13=").append(isbn13);
		sb.append(", myRating=").append(myRating);
		sb.append(", averageRating=").append(averageRating);
		sb.append(", publisher=").append(publisher);
		sb.append(", binding=").append(binding);
		sb.append(", numberOfPages=").append(numberOfPages);
		sb.append(", yearOfPublication=").append(yearOfPublication);
		sb.append(", originalPublicationYear=").append(originalPublicationYear);
		sb.append(", dateRead=").append(dateRead);
		sb.append(", dateAdded=").append(dateAdded);
		sb.append(", yearsRead=").append(yearsRead);
		sb.append(", bookshelves=").append(bookshelves);
		sb.append(", bookshelvesWithPositions=").append(bookshelvesWithPositions);
		sb.append(", exclusiveShelf=").append(exclusiveShelf);
		sb.append(", readState=").append(readState);
		sb.append(", myReview=").append(myReview);
		sb.append(", spoiler=").append(spoiler);
		sb.append(", privateNotes=").append(privateNotes);
		sb.append(", readCount=").append(readCount);
		sb.append(", recommendedFor=").append(recommendedFor);
		sb.append(", recommendedBy=").append(recommendedBy);
		sb.append(", ownedCopies=").append(ownedCopies);
		sb.append(", purchaseDate=").append(purchaseDate);
		sb.append(", purchaseLocation=").append(purchaseLocation);
		sb.append(", condition=").append(condition);
		sb.append(", conditionDescription=").append(conditionDescription);
		sb.append(", bcid=").append(bcid);
		sb.append(", format=").append(format);
		sb.append(", genre=").append(genre);
		sb.append("]");
		
		return sb.toString();
	}

	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		
		sb.append(title).append(" by ");
		sb.append(author);
		
		return sb.toString();
	}
}
