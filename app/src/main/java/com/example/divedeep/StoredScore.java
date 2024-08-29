package com.example.divedeep;

public class StoredScore
{
    private String name;
    private int goldMined;
    private int deepestDive;

    public StoredScore(String name, int goldMined, int deepestDive)
    {
        this.name = name;
        this.goldMined = goldMined;
        this.deepestDive = deepestDive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGoldMined() {
        return goldMined;
    }

    public void setGoldMined(int goldMined) {
        this.goldMined = goldMined;
    }

    public int getDeepestDive() {
        return deepestDive;
    }

    public void setDeepestDive(int deepestDive) {
        this.deepestDive = deepestDive;
    }
}
