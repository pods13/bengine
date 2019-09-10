package com.pods.bengine.data.warframe.drops.relics;

import com.pods.bengine.data.warframe.drops.rewards.Reward;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class RelicServiceTest {

    @InjectMocks
    private RelicService relicService;

    @Test
    public void testGetPrimeItemsByRelic() {
        Relic relic = new Relic("Axi R2 Relic", RelicType.INTACT, RelicEra.AXI);
        relic.getRewards().add(new Reward("Forma Blueprint", "Uncommon (25.33%)"));
        relic.getRewards().add(new Reward("Redeemer Prime Handle", "Rare (2.00%)"));

        Set<String> primeItems = relicService.getPrimeItemNamesByRelic(relic);
        assertEquals(1, primeItems.size());
        assertEquals("Redeemer Prime", primeItems.iterator().next());

    }
}
