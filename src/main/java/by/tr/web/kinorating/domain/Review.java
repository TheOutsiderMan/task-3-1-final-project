package by.tr.web.kinorating.domain;

import java.io.Serializable;
import java.util.Date;

public class Review implements Serializable {
	
	private static final long serialVersionUID = -3423182060568315342L;
	
	private int id;
	private String textReview;
	private User author;
	private Movie reviewedMovie;
	private Date additionDate;

	public Review() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTextReview() {
		return textReview;
	}

	public void setTextReview(String textReview) {
		this.textReview = textReview;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Movie getReviewedMovie() {
		return reviewedMovie;
	}

	public void setReviewedMovie(Movie reviewedMovie) {
		this.reviewedMovie = reviewedMovie;
	}

	public Date getAdditionDate() {
		return additionDate;
	}

	public void setAdditionDate(Date additionDate) {
		this.additionDate = additionDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((additionDate == null) ? 0 : additionDate.hashCode());
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + id;
		result = prime * result + ((reviewedMovie == null) ? 0 : reviewedMovie.hashCode());
		result = prime * result + ((textReview == null) ? 0 : textReview.hashCode());
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
		Review other = (Review) obj;
		if (additionDate == null) {
			if (other.additionDate != null)
				return false;
		} else if (!additionDate.equals(other.additionDate))
			return false;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (id != other.id)
			return false;
		if (reviewedMovie == null) {
			if (other.reviewedMovie != null)
				return false;
		} else if (!reviewedMovie.equals(other.reviewedMovie))
			return false;
		if (textReview == null) {
			if (other.textReview != null)
				return false;
		} else if (!textReview.equals(other.textReview))
			return false;
		return true;
	}

}
