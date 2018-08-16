package com.github.zackhotte.cfbdata.service.teams;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TeamParserTest {

    private TeamParser teamParser;

    @Before
    public void setUp() throws Exception {
        teamParser = new TeamParser();
    }

    @After
    public void tearDown() throws Exception {
        teamParser = null;
    }

    @Test
    public void testThatTotalNumberOfTeamsIsCorrect() {
        assertEquals(763, teamParser.getTeamNames().size());
    }

    @Test
    public void testGettingTeamInformationById() {
        assertEquals("Auburn", teamParser.getTeamInfo(2).getSchool());
        assertEquals("Notre Dame", teamParser.getTeamInfo(87).getSchool());
        assertEquals("Florida State", teamParser.getTeamInfo(52).getSchool());
        assertEquals("USC", teamParser.getTeamInfo(30).getSchool());
    }

    @Test
    public void testGettingTeamInformationByName() {
        assertEquals("Auburn", teamParser.getTeamInfo("auburn").getSchool());
        assertEquals("Notre Dame", teamParser.getTeamInfo("notre dame").getSchool());
        assertEquals("Florida State", teamParser.getTeamInfo("Florida St.").getSchool());
        assertEquals("USC", teamParser.getTeamInfo("usc").getSchool());
        assertEquals("Miami (OH)", teamParser.getTeamInfo("Miami (OH)").getSchool());
        assertEquals("Texas A&M", teamParser.getTeamInfo("texas a&m").getSchool());
        assertEquals("Hawai'i", teamParser.getTeamInfo("hawai'i").getSchool());
    }

    @Test(expected = NullPointerException.class)
    public void testShouldThrowExceptionIfSchoolDoesNotExist() {
        teamParser.getTeamInfo("McGill University");
        teamParser.getTeamInfo(9000);
    }
}