package com.example.mls_project.Entities;

public class Team {
    private final String teamName, teamShortName;

    public Team(String teamName, String teamShortName) {
        this.teamName = teamName;
        this.teamShortName = teamShortName;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getTeamShortName() {
        return teamShortName;
    }
}
