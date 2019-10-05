package com.pods.bengine.data.warframe.drops;

import com.pods.bengine.data.warframe.drops.missions.bounties.Bounty;
import com.pods.bengine.data.warframe.drops.missions.Mission;
import com.pods.bengine.data.warframe.drops.missions.bounties.BountyType;
import com.pods.bengine.data.warframe.drops.missions.bounties.Tier;
import com.pods.bengine.data.warframe.drops.relics.Relic;
import com.pods.bengine.data.warframe.drops.relics.RelicEra;
import com.pods.bengine.data.warframe.drops.relics.RelicType;
import com.pods.bengine.data.warframe.drops.rewards.Reward;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class WarframeDropsPageData {

    private static final Pattern MISSION_NAME_PATTERN = Pattern.compile("(.+)/(.+)\\s\\((.+)\\)");

    private static final String RELIC_SIGN = "Relic";

    private static final String MISSION_REWARDS_ID = "missionRewards";
    private static final String RELIC_REWARDS_ID = "relicRewards";
    private static final String CETUS_BOUNTY_REWARDS_ID = "cetusRewards";
    private static final String ORB_VALLIS_BOUNTY_REWARDS_ID = "solarisRewards";
    private static final String BLANK_ROW_CLASS_NAME = "blank-row";

    private static final String TR_TAG = "tr";
    private static final String TD_TAG = "td";

    private final String dropsPageUrl;

    public WarframeDropsPageData(@Value("${DROPS_PAGE_URL}") String dropsPageUrl) {
        this.dropsPageUrl = dropsPageUrl;
    }

    @SuppressWarnings("ConstantConditions")
    public Set<Mission> getMissions() {
        Mission currentMission = null;
        Rotation currentRotation = Rotation.NO_ROTATION;
        boolean isCacheRewards = false;
        Map<String, Mission> missionByName = new HashMap<>();
        for (Element row : getTableRowsNearHeader(MISSION_REWARDS_ID)) {
            if (row.hasClass(BLANK_ROW_CLASS_NAME)) {
                currentMission = null;
                currentRotation = Rotation.NO_ROTATION;
                continue;
            }

            String rowText = row.text().trim();
            Rotation rotation = Rotation.findRotationByName(rowText);

            if (rotation != null) {
                currentRotation = rotation;
            } else if (row.is(":has(td)")) {
                Elements rewardAndProbability = row.select(TD_TAG);
                //TODO create RelicReward to keep relicType of the reward
                String rewardName = formatRelicName(rewardAndProbability.get(0).text());
                String probability = rewardAndProbability.get(1).text();
                Map<Rotation, Set<Reward>> rewardsByRotation = isCacheRewards
                        ? currentMission.getCacheRewardsByRotation() : currentMission.getRewardsByRotation();
                rewardsByRotation.computeIfAbsent(currentRotation, r -> new HashSet<>())
                        .add(new Reward(rewardName, probability));
            } else {
                Matcher matcher = MISSION_NAME_PATTERN.matcher(rowText);
                if (!matcher.find()) {
                    currentMission = new Mission(rowText, isEventMission(rowText));
                } else {
                    String planet = matcher.group(1).trim();
                    String missionName = matcher.group(2).trim();
                    String missionType = matcher.group(3).trim();
                    currentMission = new Mission(missionName, planet, missionType, isEventMission(rowText));
                }

                if (missionByName.containsKey(currentMission.getName())) {
                    currentMission = missionByName.get(currentMission.getName());
                    isCacheRewards = true;
                } else {
                    missionByName.put(currentMission.getName(), currentMission);
                    isCacheRewards = false;
                }
            }
        }

        return new HashSet<>(missionByName.values());
    }

    private Elements getTableRowsNearHeader(String headerId) {
        Document dropsPage = getDropsPage();
        Element header = dropsPage.getElementById(headerId);
        Element table = header.nextElementSibling();
        return table.select(TR_TAG);
    }

    private boolean isEventMission(String missionName) {
        return missionName.startsWith("Event:");
    }

    public Set<Bounty> getBounties() {
        return Stream.concat(getCetusBounties().stream(), getOrbVallisBounties().stream())
                .collect(Collectors.toSet());
    }

    private Set<Bounty> getCetusBounties() {
        return parseBounties(CETUS_BOUNTY_REWARDS_ID);
    }

    private Set<Bounty> getOrbVallisBounties() {
        return parseBounties(ORB_VALLIS_BOUNTY_REWARDS_ID);
    }

    private Set<Bounty> parseBounties(String headerId) {
        Set<Bounty> bounties = new HashSet<>();
        Bounty currentBounty = null;
        Mission currentStage = null;
        Rotation currentRotation = Rotation.NO_ROTATION;
        String stagePreName = "";
        for (Element row : getTableRowsNearHeader(headerId)) {
            if (row.hasClass(BLANK_ROW_CLASS_NAME)) {
                currentBounty = null;
                currentRotation = Rotation.NO_ROTATION;
                continue;
            }

            String rowText = row.text().trim();
            Rotation rotation = Rotation.findRotationByName(rowText);

            if (rotation != null) {
                currentRotation = rotation;
            } else if (rowText.contains("Completion")) {
                stagePreName = rowText;
            } else if (rowText.contains("Stage")) {
                currentStage = Objects.requireNonNull(currentBounty).getStages().stream()
                        .filter(stage -> rowText.equals(stage.getName()))
                        .findFirst()
                        .orElse(new Mission(stagePreName + " " + rowText, false));
                currentBounty.getStages().add(currentStage);
                stagePreName = "";
            } else if (row.is(":has(td)")) {
                Elements rewardAndProbability = row.select(TD_TAG);
                String rewardName = formatRelicName(rewardAndProbability.get(1).text());
                String probability = rewardAndProbability.get(2).text();
                Objects.requireNonNull(currentStage).getRewardsByRotation()
                        .computeIfAbsent(currentRotation, r -> new HashSet<>())
                        .add(new Reward(rewardName, probability));
            } else {
                Tier tier = Tier.findTierByEnemyLevels(rowText);
                BountyType bountyType = BountyType.findTypeByBountyName(rowText);
                currentBounty = new Bounty(rowText, tier, bountyType);
                bounties.add(currentBounty);
            }
        }
        return bounties;
    }

    public Set<Relic> getRelics() {
        Set<Relic> relics = new HashSet<>();
        Relic currentRelic = null;

        for (Element row : getTableRowsNearHeader(RELIC_REWARDS_ID)) {
            if (row.hasClass(BLANK_ROW_CLASS_NAME)) {
                currentRelic = null;
                continue;
            }

            String rowText = row.text().trim();
            if (currentRelic == null) {
                String relicName = formatRelicName(rowText);
                RelicType relicType = RelicType.findRelicTypeByFullRelicName(rowText);
                RelicEra relicEra = RelicEra.findRelicEraByFullRelicName(rowText);
                currentRelic = new Relic(relicName, relicType, relicEra);
                relics.add(currentRelic);
            } else {
                Elements rewardAndProbability = row.select(TD_TAG);
                String rewardName = rewardAndProbability.get(0).text();
                String probability = rewardAndProbability.get(1).text();
                currentRelic.getRewards().add(new Reward(rewardName, probability));
            }
        }
        return relics;
    }

    private String formatRelicName(String text) {
        if (text.contains(RELIC_SIGN)) {
            return text.split(RELIC_SIGN)[0].trim();
        }
        return text;
    }

    @Cacheable("pages")
    public Document getDropsPage() {
        try {
            return Jsoup.connect(dropsPageUrl).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
