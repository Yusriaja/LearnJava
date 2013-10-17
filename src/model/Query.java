package model;

public class Query
{
	private long id;
	private String category;
	private String subcategory;
	private String question;
	private String answer;
	private String starred;
	public String getCategory()
	{
		return category;
	}
	public void setCategory(String category)
	{
		this.category = category;
	}
	public String getSubcategory()
	{
		return subcategory;
	}
	public void setSubcategory(String subcategory)
	{
		this.subcategory = subcategory;
	}
	public String getStarred()
	{
		return starred;
	}
	public void setStarred(String starred)
	{
		this.starred = starred;
	}
	public long getId()
	{
		return id;
	}
	public void setId(long id)
	{
		this.id = id;
	}
	public String getQuestion()
	{
		return question;
	}
	public void setQuestion(String question)
	{
		this.question = question;
	}
	public String getAnswer()
	{
		return answer;
	}
	public void setAnswer(String answer)
	{
		this.answer = answer;
	}
}
