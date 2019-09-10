package com.pods.bengine.data.warframe.drops.rewards;

public class Reward {

    private String name;

    private String probability;

    public Reward(String rewardName, String probability) {
        this.name = rewardName;
        this.probability = probability;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProbability() {
        return probability;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }
}
