package com.github.zackhotte.cfbdata.service.rankings;

import com.github.zackhotte.cfbdata.service.teams.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class Ranking {

    private RankingTypes rankingTypes;
    private Team team;
    private int rank;
    private long year;
    private int week;

    @Override
    public String toString() {
        return "Ranking{" +
                "rankingTypes=" + rankingTypes +
                ", team=" + team +
                ", rank=" + rank +
                ", year=" + year +
                ", week=" + week +
                '}';
    }

}
