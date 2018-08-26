package com.github.zackhotte.cfbdata.service.rankings;

public enum RankingTypes {

    PLAYOFF_COMMITTEE_RANKINGS("Playoff Committee Rankings"),
    AP_TOP_25("AP Top 25"),
    COACHES_POLL("Coaches Poll");

    private final String name;

    RankingTypes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
