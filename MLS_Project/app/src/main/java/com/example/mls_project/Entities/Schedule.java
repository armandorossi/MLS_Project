package com.example.mls_project.Entities;

public class Schedule {
    private final String scheduleDate, homeTeamName, awayTeamName, scheduleTime, score, homeClubName, awayClubName, result;

    public Schedule(String scheduleDate, String homeTeamName, String awayTeamName, String scheduleTime, String score, String homeCLubName, String awayClubName, String result) {
        this.scheduleDate = scheduleDate;
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
        this.scheduleTime = scheduleTime;
        this.score = score;
        this.homeClubName = homeCLubName;
        this.awayClubName = awayClubName;
        this.result = result;
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
}
