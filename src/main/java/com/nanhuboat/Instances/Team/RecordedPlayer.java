package com.nanhuboat.Instances.Team;

public abstract class RecordedPlayer {
    public Team team;
    public int kills;
    public int finalKills;
    public int deaths;
    public int beds;
    public RecordedPlayer(Team team) {
        this.team = team;
        this.kills = 0;
        this.finalKills = 0;
        this.deaths = 0;
        this.beds = 0;
    }
}
