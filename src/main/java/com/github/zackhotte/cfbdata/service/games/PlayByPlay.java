package com.github.zackhotte.cfbdata.service.games;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PlayByPlay {

    private JsonNode json;
    private JsonNode jsonDrives;
    private List<Drive> drives;

    public PlayByPlay(String gameId) {
        try {
            String mainUrl = String.format("http://cdn.espn.com/college-football/playbyplay?gameId=%s&xhr=1&render=false&userab=18", gameId);
            URL url = new URL(mainUrl);

            ObjectMapper mapper = new ObjectMapper();
            this.json = mapper.readTree(url);
            this.jsonDrives = this.json.get("gamepackageJSON").get("drives").get("previous");
            getAllDrives();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Drive> getDrives() {
        return drives;
    }

    private void getAllDrives() {
        drives = new ArrayList<>();
        for (JsonNode drive : jsonDrives) {
            drives.add(createDrive(drive));
        }
    }

    private Drive createDrive(JsonNode drive) {
        String result = getDriveResult(drive);
        List<Play> plays = getDrive(drive);
        return new Drive(result, plays);
    }

    private String getDriveResult(JsonNode drive) {
        return drive.get("displayResult").textValue();
    }

    private List<Play> getDrive(JsonNode drive) {
        List<Play> plays = new ArrayList<>();
        for (JsonNode play : drive.get("plays")) {
            String playText = play.get("text").textValue();
            String playType = play.get("type").get("text").textValue();
            boolean isScoringPlay = play.get("scoringPlay").booleanValue();
            plays.add(new Play(playText, playType, isScoringPlay));
        }
        return plays;
    }

}
