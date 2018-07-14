package com.github.zackhotte.cfbdata.service.games;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Drive {

    private String result;
    private List<Play> plays;

    @Override
    public String toString() {
        String drive = "DRIVE:\n\n" +
                "Result: " + result + "\n\n" +
                "Plays: \n";
        for (Play play : plays) {
            if (play.isScoringPlay()) {
                drive += "*SCORING PLAY* ";
            }
            drive += "Type: " + play.getPlayType() + ", Play: " + play.getPlay() + "\n";
        }
        return drive;
    }
}
