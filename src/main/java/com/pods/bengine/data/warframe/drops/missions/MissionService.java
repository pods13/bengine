package com.pods.bengine.data.warframe.drops.missions;

import com.pods.bengine.data.warframe.drops.WarframeDropsPageData;
import org.springframework.stereotype.Service;

@Service
public class MissionService {

    private final WarframeDropsPageData dropsPageData;

    public MissionService(WarframeDropsPageData dropsPageData) {
        this.dropsPageData = dropsPageData;
    }

    public Mission getMissionByName(String name) {
        return dropsPageData.getMissions().stream()
                .filter(mission -> mission.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
