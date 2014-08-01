package ui.review;

import java.util.Date;
public class ReviewDisplayListItem {

	public int Id;
	public Date Date;
	public String Reviewer;
	public String Restaurant;
	public String ReviewText;
	public int Food;
	public int Service;
	public int Ambience;
	public int Score;

	public ReviewDisplayListItem (int id, Date date,String reviewer, String reviewText, int food , int service, int ambience, int score, String Restaurant) {
		this.Id = id;
		this.Date = date;
		this.Reviewer = reviewer;
		this.ReviewText =reviewText;
		this.Food = food;
		this.Service = service;
		this.Ambience = ambience;
		this.Score = score;
		this.Restaurant = Restaurant;
	}
}
