package com.github.zackhotte.cfbdata.service.schedule;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.*;


@RunWith(Parameterized.class)
public class ScheduleParserTest {

    @Parameters
    public static Collection<Object[]> scores() {
        return Arrays.asList(new Object[][]{
                {"Texas", "Oklahoma State", 13, 10}, {"Florida State", "Louisville", 31, 28},
                {"Alabama", "Tennessee", 7, 45}, {"Ole Miss", "LSU", 40, 24}, {"Penn State", "Michigan", 13, 42},
                {"Notre Dame", "USC", 14, 49}, {"Baylor", "West Virginia", 38, 36}
        });
    }

    @Parameter
    public String homeTeam;
    @Parameter(1)
    public String awayTeam;
    @Parameter(2)
    public int awayScore;
    @Parameter(3)
    public int homeScore;

    private ScheduleParser scheduleParser;
    private Map<String, Schedule> schedules;

    @Before
    public void setUp() throws Exception {
        scheduleParser = new ScheduleParser(2017, 8);
        schedules = scheduleParser.getSchedules();
    }

    @After
    public void tearDown() throws Exception {
        scheduleParser = null;
    }

    @Test
    public void main() throws Exception {
        ScheduleParser parser = new ScheduleParser(2018, 1);
        System.out.println(parser.getSchedules().get("Notre Dame"));
    }

    @Test
    public void testThatAllDatesArePresent() throws Exception {
        assertTrue(scheduleParser.getScheduleNode().has("20171019"));
        assertTrue(scheduleParser.getScheduleNode().has("20171020"));
        assertTrue(scheduleParser.getScheduleNode().has("20171021"));
    }

    @Test
    public void testTeamsExist() {
        assertNotNull(schedules.get(homeTeam));
        assertNotNull(schedules.get(awayTeam));
    }

    @Test
    public void testScoresOfGames() throws Exception {
        Schedule schedule = schedules.get(homeTeam);
        System.out.println(schedule.getDate());
        assertEquals(awayScore, schedule.getAwayScore());
        assertEquals(homeScore, schedule.getHomeScore());
    }

    @Test
    public void testThatWinnerIsCorrect() throws Exception {

    }

}