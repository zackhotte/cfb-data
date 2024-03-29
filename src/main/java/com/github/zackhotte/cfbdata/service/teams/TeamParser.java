package com.github.zackhotte.cfbdata.service.teams;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.zackhotte.cfbdata.service.CFBParser;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class TeamParser extends CFBParser<Team> {

    private JsonNode teamsNode;

    @Getter
    private Map<Integer, Team> teams = new HashMap<>();
    @Getter
    private List<String> teamNames = new ArrayList<>();
    @Getter
    private Map<String, Integer> teamIds = new HashMap<>();

    public TeamParser() {
        super("http://site.api.espn.com/apis/site/v2/sports/football/college-football/teams?limit=1000");
        this.teamsNode = root.get("sports").get(0).get("leagues").get(0).get("teams");
        getAllTeams();
    }

    public static Team getTeamById(int teamId) {
        TeamParser parser = new TeamParser();
        return parser.getTeamInfo(teamId);
    }

    @Override
    public List<Team> getData() {
        return null;
    }

    private void getAllTeams() {
        this.teamsNode.forEach((JsonNode node) -> {
            JsonNode teamNode = node.get("team");
            Integer teamId = Integer.parseInt(teamNode.get("id").textValue());
            Team team = createTeam(teamNode);
            String schoolName = team.getSchool();

            this.teams.put(teamId, team);
            this.teamNames.add(schoolName);
            this.teamIds.put(schoolName, teamId);
        });
        Collections.sort(teamNames);
    }

    private Team createTeam(JsonNode teamNode) {
        Team team = new Team();

        team.setSchool(teamNode.get("location").textValue());
        team.setName(teamNode.get("name").textValue());
        team.setDisplayName(teamNode.get("displayName").textValue());
        team.setShortDisplayName(teamNode.get("shortDisplayName").textValue());
        team.setActive(teamNode.get("isActive").booleanValue());
        team.setEspnId(Long.parseLong(teamNode.get("id").textValue()));

        // Fields that need to be verified as they do not always exist in the API
        team.setNickname(verifyNodeKey(teamNode, "nickname", JsonNode::textValue));
        team.setColor(verifyNodeKey(teamNode, "color", JsonNode::textValue));
        team.setColor(verifyNodeKey(teamNode, "alternateColor", JsonNode::textValue));
        team.setLogo(verifyNodeKey(teamNode, "logos", jsonNode -> jsonNode.get(0).get("href").textValue()));
        team.setLogoDark(verifyNodeKey(teamNode, "logos", jsonNode -> {
            if (jsonNode.size() > 1) {
                return jsonNode.get(1).get("href").textValue();
            }
            return "";
        }));

        return team;
    }

    private String verifyNodeKey(JsonNode node, String key, Function<JsonNode, String> function) {
        if (node.has(key)) {
            return function.apply(node.get(key));
        }
        return null;
    }

    public Team getTeamInfo(int teamId) {
        return teams.get(teamId);
    }

    public Team getTeamInfo(String school) {
        String schoolName = findTeamName(school);
        int teamId = teamIds.get(schoolName);
        return getTeamInfo(teamId);
    }

    private String findTeamName(String name) {
        final String finalName = name.toLowerCase().replaceAll("[^a-zA-Z0-9\\s\\-()'&]", "");
        List<String> filteredTeams = teamNames.parallelStream()
                .filter((String teamName) -> teamName.toLowerCase().contains(finalName))
                .collect(toList());

        if (filteredTeams.size() == 1) {
            return filteredTeams.get(0);
        }

        if (filteredTeams.size() > 1) {
            for (String teamName : filteredTeams) {
                if (teamName.toLowerCase().equals(name)) {
                    return teamName;
                }
            }
        }

        throw new NullPointerException("Cannot find the team name: " + name);
    }

}
