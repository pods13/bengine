package com.pods.bengine.content.generation.warframe.primes.template.location.mission;

import com.pods.bengine.data.warframe.drops.rewards.Reward;
import com.pods.bengine.data.warframe.drops.missions.Mission;
import com.pods.bengine.data.warframe.drops.missions.MissionService;
import com.pods.bengine.data.warframe.drops.relics.RelicEra;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MissionTemplateResolver {

    private static final String OROKIN_DERELICT_DEFENSE_MISSION_NAME = "Orokin Derelict Defense";
    private static final String IO_MISSION_NAME = "Io";
    private static final String HYDRON_MISSION_NAME = "Hydron";
    private static final String XINI_MISSION_NAME = "Xini";

    private static final String HEPPIT_MISSION_NAME = "Hepit";
    private static final String UKKO_MISSION_NAME = "Ukko";
    private static final String BELENUS_MISSION_NAME = "Belenus";
    private static final String MARDUK_MISSION_NAME = "Marduk";

    private final MissionService missionService;

    public MissionTemplateResolver(MissionService missionService) {
        this.missionService = missionService;
    }

    public Mission resolve(RelicEra era, Set<String> relicNames) {
        if (RelicEra.LITH == era) {
            return selectFirstThatContainsAllRelics(relicNames,
                    OROKIN_DERELICT_DEFENSE_MISSION_NAME,
                    HEPPIT_MISSION_NAME);
        } else if (RelicEra.MESO == era) {
            return selectFirstThatContainsAllRelics(relicNames,
                    IO_MISSION_NAME, UKKO_MISSION_NAME);
        } else if (RelicEra.NEO == era) {
            return selectFirstThatContainsAllRelics(relicNames,
                    HYDRON_MISSION_NAME, BELENUS_MISSION_NAME);
        } else if (RelicEra.AXI == era) {
            return selectFirstThatContainsAllRelics(relicNames,
                    XINI_MISSION_NAME, MARDUK_MISSION_NAME);
        }

        return null;
    }

    private Mission selectFirstThatContainsAllRelics(Set<String> relicNames, String ... missionNames) {
        return Stream.of(missionNames)
                .map(missionService::getMissionByName)
                .filter(mission -> checkRelics(mission, relicNames))
                .findFirst()
                .orElse(null);
    }

    private boolean checkRelics(Mission mission, Set<String> relicNames) {
        Set<String> rewardNames = mission.getRewardsByRotation().values().stream()
                .flatMap(Collection::stream)
                .map(Reward::getName)
                .collect(Collectors.toSet());
        return rewardNames.containsAll(relicNames);
    }
}
