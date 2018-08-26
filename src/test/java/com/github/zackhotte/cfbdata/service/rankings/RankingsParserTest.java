package com.github.zackhotte.cfbdata.service.rankings;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class RankingsParserTest {

    private static RankingsParser rankingsParser;

    @BeforeClass
    public static void setUp() throws Exception {
        rankingsParser = new RankingsParser(2017, 10);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        rankingsParser = null;
    }

    @Test
    public void testRankingTypesFormatter() throws Exception {
        assertEquals(RankingTypes.COACHES_POLL, rankingsParser.formatRankingTypes("Coaches Poll"));
        assertEquals(RankingTypes.PLAYOFF_COMMITTEE_RANKINGS, rankingsParser.formatRankingTypes("Playoff Committee Rankings"));
        assertEquals(RankingTypes.AP_TOP_25, rankingsParser.formatRankingTypes("AP Top 25"));
    }

    @Test
    public void testThatAPTop25PollIsAccurate() throws Exception {
        List<String> teams = rankingsParser.getRankingByType("AP Top 25");

        assertEquals("Alabama", teams.get(0));
        assertEquals("Georgia", teams.get(1));
        assertEquals("Notre Dame", teams.get(4));
        assertEquals("Clemson", teams.get(5));
        assertEquals("Oklahoma State", teams.get(10));
        assertEquals("Washington State", teams.get(24));
    }

    @Test
    public void testThatPlayoffCommitteeIsAccurate() throws Exception {
        List<String> teams = rankingsParser.getRankingByType("Playoff Committee Rankings");

        assertEquals("Georgia", teams.get(0));
        assertEquals("Alabama", teams.get(1));
        assertEquals("Notre Dame", teams.get(2));
        assertEquals("Clemson", teams.get(3));
        assertEquals("Ohio State", teams.get(5));
        assertEquals("Oklahoma State", teams.get(10));
        assertEquals("Washington State", teams.get(24));
    }

}