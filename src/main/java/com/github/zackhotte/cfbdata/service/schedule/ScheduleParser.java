package com.github.zackhotte.cfbdata.service.schedule;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.zackhotte.cfbdata.service.CFBParser;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Getter
public class ScheduleParser extends CFBParser<Schedule> {

    private JsonNode scheduleNode;

    private long season;
    private int week;
    private List<String> dates = new ArrayList<>();
    private Map<String, Schedule> schedules = new HashMap<>();

    public ScheduleParser(Integer season, Integer week) {
        super("http://cdn.espn.com/core/college-football/schedule?year=%s&week=%s&xhr=1", season, week);
        this.season = season;
        this.week = week;
        this.scheduleNode = this.root.get("content").get("schedule");
        getAllDatesInWeek();
        getAllScheduledGames();
    }

    @Override
    public List<Schedule> getData() {
        return new ArrayList<>(schedules.values());
    }

    private void getAllDatesInWeek() {
        for (Iterator<String> it = scheduleNode.fieldNames(); it.hasNext(); ) {
            String dateStr = it.next();
            dates.add(dateStr);
        }
    }

    private LocalDateTime convertToDate(String dateStr) {
        DateTimeFormatter dateTimeFormatter;
        if (dateStr.contains("Z") || dateStr.contains("T")) {
            dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'");
        } else {
            dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        }

        return LocalDateTime.parse(dateStr, dateTimeFormatter);
    }

    private void getAllScheduledGames() {
        List<Schedule> schedules = new ArrayList<>();
        dates.forEach((String date) -> {
            JsonNode nodes = scheduleNode.get(date).get("games");
            nodes.forEach(this::setSchedule);
        });
    }

    private void setSchedule(JsonNode node) {
        Schedule schedule = new Schedule();

        long gameId = Long.parseLong(node.get("id").textValue());
        LocalDateTime date = convertToDate(node.get("date").textValue());

        JsonNode competitors = node.get("competitions").get(0).get("competitors");
        String homeTeam = null;
        String awayTeam = null;
        String winner = null;
        int homeScore = 0;
        int awayScore = 0;

        for (JsonNode competitor : competitors) {
            String teamName = competitor.get("team").get("location").textValue();
            String homeAway = competitor.get("homeAway").textValue();

            if (homeAway.equals("home")) {
                homeTeam = teamName;
                homeScore = Integer.parseInt(competitor.get("score").textValue());
            } else {
                awayTeam = teamName;
                awayScore = Integer.parseInt(competitor.get("score").textValue());
            }

            if (competitor.has("winner")) {
                winner = teamName;
            }
        }

        schedule.setGameId(gameId);
        schedule.setSeason(season);
        schedule.setWeek(week);
        schedule.setDate(date);
        schedule.setAwayTeam(awayTeam);
        schedule.setHomeTeam(homeTeam);
        schedule.setAwayScore(awayScore);
        schedule.setHomeScore(homeScore);
        schedule.setWinner(winner);

        schedules.put(awayTeam, schedule);
        schedules.put(homeTeam, schedule);
    }

}
