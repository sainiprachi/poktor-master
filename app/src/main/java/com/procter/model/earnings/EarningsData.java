package com.procter.model.earnings;

import com.google.gson.annotations.SerializedName;

public class EarningsData{

	@SerializedName("most_earnings")
	private int mostEarnings;

	@SerializedName("sales_report")
	private SalesReport salesReport;

	@SerializedName("today_earnings")
	private int todayEarnings;

	@SerializedName("weekly_earnings")
	private int weeklyEarnings;

	@SerializedName("yearly_earnings")
	private int yearlyEarnings;

	@SerializedName("monthly_earnings")
	private int monthlyEarnings;

	public void setMostEarnings(int mostEarnings){
		this.mostEarnings = mostEarnings;
	}

	public int getMostEarnings(){
		return mostEarnings;
	}

	public void setSalesReport(SalesReport salesReport){
		this.salesReport = salesReport;
	}

	public SalesReport getSalesReport(){
		return salesReport;
	}

	public void setTodayEarnings(int todayEarnings){
		this.todayEarnings = todayEarnings;
	}

	public int getTodayEarnings(){
		return todayEarnings;
	}

	public void setWeeklyEarnings(int weeklyEarnings){
		this.weeklyEarnings = weeklyEarnings;
	}

	public int getWeeklyEarnings(){
		return weeklyEarnings;
	}

	public void setYearlyEarnings(int yearlyEarnings){
		this.yearlyEarnings = yearlyEarnings;
	}

	public int getYearlyEarnings(){
		return yearlyEarnings;
	}

	public void setMonthlyEarnings(int monthlyEarnings){
		this.monthlyEarnings = monthlyEarnings;
	}

	public int getMonthlyEarnings(){
		return monthlyEarnings;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"most_earnings = '" + mostEarnings + '\'' + 
			",sales_report = '" + salesReport + '\'' + 
			",today_earnings = '" + todayEarnings + '\'' + 
			",weekly_earnings = '" + weeklyEarnings + '\'' + 
			",yearly_earnings = '" + yearlyEarnings + '\'' + 
			",monthly_earnings = '" + monthlyEarnings + '\'' + 
			"}";
		}
}