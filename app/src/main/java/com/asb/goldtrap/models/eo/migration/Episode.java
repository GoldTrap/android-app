package com.asb.goldtrap.models.eo.migration;

/**
 * Created by arjun on 06/02/16.
 */
public class Episode {
    private Level level;
    private long id;
    private int number;
    private String name;
    private String image;
    private String code;
    private boolean completed;

    public Episode() {
    }

    public Episode(Level level, long id, int number, String name, String image, String code,
                   boolean completed) {
        this.level = level;
        this.id = id;
        this.number = number;
        this.name = name;
        this.image = image;
        this.code = code;
        this.completed = completed;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Level level;
        private long id;
        private int number;
        private String name;
        private String image;
        private String code;
        private boolean completed = false;

        public Builder withId(long id) {
            this.id = id;
            return this;
        }

        public Builder withLevel(Level level) {
            this.level = level;
            return this;
        }

        public Builder withNumber(int number) {
            this.number = number;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withImage(String image) {
            this.image = image;
            return this;
        }

        public Builder withCode(String code) {
            this.code = code;
            return this;
        }

        public Builder withCompleted(int completed) {
            this.completed = completed == 1;
            return this;
        }

        public Episode build() {
            return new Episode(level, id, number, name, image, code, completed);
        }
    }
}
