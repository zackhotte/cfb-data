package com.github.zackhotte.cfbdata.service.schedule;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class Schedule {

    private long gameId;
    private long season;
    private int week;
    private LocalDateTime date;
    private String awayTeam;
    private String homeTeam;
    private int awayScore;
    private int homeScore;
    private String winner;

    @Override
    public String toString() {
        return "Schedule{" +
                "gameId=" + gameId +
                ", season=" + season +
                ", week=" + week +
                ", date=" + date +
                ", awayTeam='" + awayTeam + '\'' +
                ", homeTeam='" + homeTeam + '\'' +
                ", awayScore=" + awayScore +
                ", homeScore=" + homeScore +
                ", winner='" + winner + '\'' +
                '}';
    }
}
