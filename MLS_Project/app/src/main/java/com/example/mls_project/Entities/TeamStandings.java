package com.example.mls_project.Entities;

public class TeamStandings {
    private final String teamName, competitionYear, totalPoints, totalWins, totalLosses, totalDraws;

    public TeamStandings(String teamName, String competitionYear, String totalPoints, String totalWins, String totalLosses, String totalDraws) {
        this.teamName = teamName;
        this.competitionYear = competitionYear;
        this.totalPoints = totalPoints;
        this.totalWins = totalWins;
        this.totalLosses = totalLosses;
        this.totalDraws = totalDraws;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getCompetitionYear() {
        return competitionYear;
    }

    public String getTotalPoints() {
        return totalPoints;
    }

    public String getTotalWins() {
        return totalWins;
    }

    public String getTotalLosses() {
        return totalLosses;
    }

    public String getTotalDraws() {
        return totalDraws;
    }
}
