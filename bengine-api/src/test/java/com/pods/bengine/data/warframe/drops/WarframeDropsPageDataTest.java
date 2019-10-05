package com.pods.bengine.data.warframe.drops;

import com.pods.bengine.data.warframe.drops.missions.Mission;
import com.pods.bengine.data.warframe.drops.missions.bounties.Bounty;
import com.pods.bengine.data.warframe.drops.relics.Relic;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doAnswer;

@RunWith(MockitoJUnitRunner.class)
public class WarframeDropsPageDataTest {

    @Spy
    private WarframeDropsPageData dropsPageData;

    @Before
    public void setUp() {
        doAnswer((Answer<Document>) invocationOnMock -> {
            URL resource = getClass().getClassLoader().getResource("data/drops.html");
            File file = new File(Objects.requireNonNull(resource).getPath());
            return Jsoup.parse(file, "utf-8");
        }).when(dropsPageData).getDropsPage();
    }

    @Test
    public void testGetMissions() {
        Set<Mission> missions = dropsPageData.getMissions();
        assertEquals(267, missions.size());
    }

    @Test
    public void testGetRelics() {
        Set<Relic> relics = dropsPageData.getRelics();
        assertEquals(872, relics.size());
    }

    @Test
    public void testGetBounties() {
        Set<Bounty> bounties = dropsPageData.getBounties();
        assertEquals(16, bounties.size());
    }

}
