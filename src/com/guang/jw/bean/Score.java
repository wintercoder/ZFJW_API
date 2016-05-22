package com.guang.jw.bean;

//不含期中、实验成绩等
public class Score {
	private String courseName;		//课程名
	private String credit;			//学分
	private String regularScore;	//平时分
	private String paperScore;		//期末卷面分
	private String totalScore;		//总评
	
	public String getCredit() {
		return credit;
	}
	public void setCredit(String credit) {
		this.credit = credit;
	}
	public String getRegularScore() {
		return regularScore;
	}
	public void setRegularScore(String regularScore) {
		this.regularScore = regularScore;
	}
	public String getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(String totalScore) {
		this.totalScore = totalScore;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getPaperScore() {
		return paperScore;
	}
	public void setPaperScore(String paperScore) {
		this.paperScore = paperScore;
	}
	
}
