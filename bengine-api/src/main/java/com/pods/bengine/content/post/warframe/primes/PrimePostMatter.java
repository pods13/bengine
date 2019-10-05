package com.pods.bengine.content.post.warframe.primes;

import com.pods.bengine.content.generation.warframe.primes.status.PrimePostStatus;
import com.pods.bengine.content.post.matter.PostMatter;

import java.util.List;

public class PrimePostMatter extends PostMatter {

    private String groupId;
    private String itemName;
    private PrimePostStatus status;
    private List<String> alongWithPrimeItems;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public PrimePostStatus getStatus() {
        return status;
    }

    public void setStatus(PrimePostStatus status) {
        this.status = status;
    }

    public List<String> getAlongWithPrimeItems() {
        return alongWithPrimeItems;
    }

    public void setAlongWithPrimeItems(List<String> alongWithPrimeItems) {
        this.alongWithPrimeItems = alongWithPrimeItems;
    }
}
