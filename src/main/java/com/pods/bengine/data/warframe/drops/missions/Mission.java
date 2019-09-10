package com.pods.bengine.data.warframe.drops.missions;

import com.pods.bengine.data.warframe.drops.rewards.Reward;
import com.pods.bengine.data.warframe.drops.Rotation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Mission {

    private String name;
    private String planet;
    private String type;
    private boolean event;
    private Map<Rotation, Set<Reward>> rewardsByRotation;
    private Map<Rotation, Set<Reward>> cacheRewardsByRotation;

    public Mission(String name, boolean eventMission) {
        this.name = name;
        this.event = eventMission;
        this.rewardsByRotation = new HashMap<>();
        this.cacheRewardsByRotation = new HashMap<>();
    }

    public Mission(String name, String planet, String type, boolean event) {
        this(name, event);
        this.planet = planet;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mission mission = (Mission) o;
        return event == mission.event &&
                name.equals(mission.name) &&
                Objects.equals(planet, mission.planet) &&
                Objects.equals(type, mission.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, planet, type, event);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isEvent() {
        return event;
    }

    public void setEvent(boolean event) {
        this.event = event;
    }

    public Map<Rotation, Set<Reward>> getRewardsByRotation() {
        return rewardsByRotation;
    }

    public void setRewardsByRotation(Map<Rotation, Set<Reward>> rewardsByRotation) {
        this.rewardsByRotation = rewardsByRotation;
    }

    public Map<Rotation, Set<Reward>> getCacheRewardsByRotation() {
        return cacheRewardsByRotation;
    }

    public void setCacheRewardsByRotation(Map<Rotation, Set<Reward>> cacheRewardsByRotation) {
        this.cacheRewardsByRotation = cacheRewardsByRotation;
    }
}
