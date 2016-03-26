package com.asb.goldtrap.models.eo;

/**
 * Score.
 * Created by arjun on 26/03/16.
 */
public class Score {
    private long id;
    private String type;
    private long value;

    public Score(long id, String type, long value) {
        this.id = id;
        this.type = type;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private long id;
        private String type;
        private long value;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setValue(long value) {
            this.value = value;
            return this;
        }

        public Score build() {
            return new Score(id, type, value);
        }
    }
}
