package com.pods.bengine.content.generation.warframe.primes.template.location;

import com.pods.bengine.data.warframe.drops.missions.Mission;
import com.pods.bengine.data.warframe.drops.relics.RelicEra;

import java.util.Set;

public class FarmingLocation {

    private Mission mission;
    private RelicEra era;
    private Set<String> relicNames;
    private Set<String> mentionedIn;

    public FarmingLocation(Mission mission, RelicEra era, Set<String> relicNames) {
        this.mission = mission;
        this.era = era;
        this.relicNames = relicNames;
    }

    public FarmingLocation(Mission mission, RelicEra era, Set<String> relicNames, Set<String> mentionedIn) {
        this(mission, era, relicNames);
        this.mentionedIn = mentionedIn;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public RelicEra getEra() {
        return era;
    }

    public void setEra(RelicEra era) {
        this.era = era;
    }

    public Set<String> getRelicNames() {
        return relicNames;
    }

    public void setRelicNames(Set<String> relicNames) {
        this.relicNames = relicNames;
    }

    public Set<String> getMentionedIn() {
        return mentionedIn;
    }

    public void setMentionedIn(Set<String> mentionedIn) {
        this.mentionedIn = mentionedIn;
    }
}
