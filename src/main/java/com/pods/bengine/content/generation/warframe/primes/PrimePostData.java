package com.pods.bengine.content.generation.warframe.primes;

import com.pods.bengine.content.generation.warframe.primes.status.PrimePostStatus;

import java.util.List;

public class PrimePostData {

    private String itemName;
    private PrimePostStatus status;
    private boolean draft;
    private List<String> alongWithPrimeItems;

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

    public boolean isDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }

    public List<String> getAlongWithPrimeItems() {
        return alongWithPrimeItems;
    }

    public void setAlongWithPrimeItems(List<String> alongWithPrimeItems) {
        this.alongWithPrimeItems = alongWithPrimeItems;
    }
}
