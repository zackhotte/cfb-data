package com.github.zackhotte.cfbdata.service.rankings;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.zackhotte.cfbdata.service.CFBParser;
import com.github.zackhotte.cfbdata.service.teams.Team;
import com.github.zackhotte.cfbdata.service.teams.TeamParser;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RankingsParser extends CFBParser<Ranking> {

    private final int MAX_WEEKS = 15;

    private int year;
    private int week;

    @Getter
    private Map<String, List<Ranking>> rankings = new HashMap<>();

    public RankingsParser(Integer year, Integer week) {
        super("http://site.api.espn.com/apis/site/v2/sports/football/college-football/rankings?seasons=%s&weeks=%s", year, week);
        if (week > MAX_WEEKS) {
            throw new IllegalArgumentException(String.format("Max weeks in a CFB season is %s, you entered week %s", MAX_WEEKS, week));
        }
        this.year = year;
        this.week = week;

        setRankings();
    }

    @Override
    public List<Ranking> getData() {
        List<Ranking> data = new ArrayList<>();
        rankings.forEach((key, value) -> data.addAll(value));
        return data;
    }

    private void setRankings() {
        JsonNode rankings = root.get("rankings");

        StreamSupport.stream(rankings.spliterator(), false)
                .filter(node -> isApprovedPoll(node.get("name").textValue()))
                .forEach(rankingNode -> {
                    String rankingName = rankingNode.get("name").textValue();
                    RankingTypes rankingType = formatRankingTypes(rankingName);

                    List<Ranking> rankingList = new ArrayList<>();
                    rankingNode.get("ranks").forEach(rank -> {
                        Ranking ranking = new Ranking();
                        ranking.setRankingTypes(rankingType);

                        int teamId = rank.get("team").get("id").asInt();
                        ranking.setTeam(TeamParser.getTeamById(teamId));
                        ranking.setRank(rank.get("current").asInt());
                        ranking.setYear(year);
                        ranking.setWeek(week);

                        rankingList.add(ranking);
                    });
                    this.rankings.put(rankingName, rankingList);
                });
    }

    private boolean isApprovedPoll(String pollName) {
        return RankingTypes.PLAYOFF_COMMITTEE_RANKINGS.getName().equals(pollName) ||
                RankingTypes.AP_TOP_25.getName().equals(pollName) ||
                RankingTypes.COACHES_POLL.getName().equals(pollName);
    }

    public RankingTypes formatRankingTypes(String name) {
        name = name.toUpperCase();
        name = name.replace(" ", "_");
        return RankingTypes.valueOf(name);
    }

    public List<String> getRankingByType(String name) {
        List<Ranking> rankings = getRankings().get(name);
        return rankings.stream()
                .map(Ranking::getTeam)
                .map(Team::getSchool)
                .collect(Collectors.toList());
    }

}
