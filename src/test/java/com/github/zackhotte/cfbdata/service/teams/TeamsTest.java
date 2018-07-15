package com.github.zackhotte.cfbdata.service.teams;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TeamsTest {

    private Teams teams;

    @Before
    public void setUp() throws Exception {
        teams = new Teams();
    }

    @After
    public void tearDown() throws Exception {
        teams = null;
    }

    @Test
    public void testThatTotalNumberOfTeamsIsCorrect() {
        assertEquals(760, teams.getTeamNames().size());
    }

    @Test
    public void testGettingTeamInformationById() {
        assertEquals("Auburn", teams.getTeamInfo(2).getSchool());
        assertEquals("Notre Dame", teams.getTeamInfo(87).getSchool());
        assertEquals("Florida State", teams.getTeamInfo(52).getSchool());
        assertEquals("USC", teams.getTeamInfo(30).getSchool());
    }

    @Test
    public void testGettingTeamInformationByName() {
        assertEquals("Auburn", teams.getTeamInfo("auburn").getSchool());
        assertEquals("Notre Dame", teams.getTeamInfo("notre dame").getSchool());
        assertEquals("Florida State", teams.getTeamInfo("Florida St.").getSchool());
        assertEquals("USC", teams.getTeamInfo("usc").getSchool());
    }
}