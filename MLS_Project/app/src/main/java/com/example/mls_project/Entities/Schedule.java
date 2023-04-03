package com.example.mls_project.Entities;

public class Schedule {
    private final String scheduleDate, homeTeamName, awayTeamName, scheduleTime, score, homeClubName, awayClubName, result;
    private final Float home_pct, away_pct, draw_pct;

    public Schedule(String scheduleDate, String homeTeamName, String awayTeamName, String scheduleTime, String score, String homeCLubName, String awayClubName, String result, Float home_pct, Float away_pct, Float draw_pct) {
        this.scheduleDate = scheduleDate;
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
        this.scheduleTime = scheduleTime;
        this.score = score;
        this.homeClubName = homeCLubName;
        this.awayClubName = awayClubName;
        this.result = result;
        this.home_pct = home_pct;
        this.away_pct = away_pct;
        this.draw_pct = draw_pct;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public String getScore() {
        return score;
    }

    public String getHomeClubName() {
        return homeClubName;
    }

    public String getAwayClubName() {
        return awayClubName;
    }

    public String getResult() {
        return result;
    }

    public String getHome_pct() {
        return String.format("%.0f%%", home_pct * 100);
    }

    public String getAway_pct() {
        return String.format("%.0f%%", away_pct * 100);
    }

    public String getDraw_pct() {
        return String.format("%.0f%%", draw_pct * 100);
    }
}
