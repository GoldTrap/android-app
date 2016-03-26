package com.asb.goldtrap.models.eo;

import com.asb.goldtrap.models.states.enums.GoodiesState;

/**
 * Goodie.
 * Created by arjun on 26/03/16.
 */
public class Goodie {
    private long id;
    private GoodiesState goodiesState;
    private String type;
    private long count;

    public Goodie(long id, GoodiesState goodiesState, String type, long count) {
        this.id = id;
        this.goodiesState = goodiesState;
        this.type = type;
        this.count = count;
    }

    public long getId() {
        return id;
    }

    public GoodiesState getGoodiesState() {
        return goodiesState;
    }

    public String getType() {
        return type;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long id;
        private GoodiesState goodiesState;
        private String type;
        private long count;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setGoodiesState(String goodiesState) {
            this.goodiesState = GoodiesState.valueOf(goodiesState);
            return this;
        }

        public Builder setGoodiesState(GoodiesState goodiesState) {
            this.goodiesState = goodiesState;
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

        public Goodie build() {
            return new Goodie(id, goodiesState, type, count);
        }
    }
}
