package com.asb.goldtrap.models.eo;

/**
 * Booster.
 * Created by arjun on 09/04/16.
 */
public class Booster {
    private long id;
    private BoosterType boosterType;
    private String type;
    private long count;

    public Booster(long id, String type, BoosterType boosterType, long count) {
        this.id = id;
        this.type = type;
        this.boosterType = boosterType;
        this.count = count;
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public BoosterType getBoosterType() {
        return boosterType;
    }

    public long getCount() {
        return count;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private long id;
        private String type;
        private BoosterType boosterType;
        private long count;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setBoosterType(BoosterType boosterType) {
            this.boosterType = boosterType;
            return this;
        }

        public Builder setBoosterType(String boosterType) {
            this.boosterType = BoosterType.valueOf(boosterType);
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setCount(long count) {
            this.count = count;
            return this;
        }

        public Booster build() {
            return new Booster(id, type, boosterType, count);
        }
    }
}
