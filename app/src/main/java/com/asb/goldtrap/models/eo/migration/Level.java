package com.asb.goldtrap.models.eo.migration;

/**
 * Created by arjun on 06/02/16.
 */
public class Level {
    private long id;
    private int number;
    private String code;
    private String name;
    private String image;
    private String episodeCode;
    private long bestScore;
    private long bestStar;
    private boolean locked;
    private boolean completed;
    private long numberOfLevels;

    public Level() {
    }

    public Level(long id, int number, String code, String name, String image,
                 String episodeCode, long bestScore, long bestStar, boolean completed,
                 boolean locked,
                 long numberOfLevels) {
        this.id = id;
        this.number = number;
        this.code = code;
        this.name = name;
        this.image = image;
        this.episodeCode = episodeCode;
        this.bestScore = bestScore;
        this.bestStar = bestStar;
        this.completed = completed;
        this.locked = locked;
        this.numberOfLevels = numberOfLevels;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getNumberOfLevels() {
        return numberOfLevels;
    }

    public void setNumberOfLevels(long numberOfLevels) {
        this.numberOfLevels = numberOfLevels;
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

    public String getEpisodeCode() {
        return episodeCode;
    }

    public void setEpisodeCode(String episodeCode) {
        this.episodeCode = episodeCode;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public long getBestScore() {
        return bestScore;
    }

    public void setBestScore(long bestScore) {
        this.bestScore = bestScore;
    }

    public long getBestStar() {
        return bestStar;
    }

    public void setBestStar(long bestStar) {
        this.bestStar = bestStar;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private long id;
        private int number;
        private String code;
        private String name;
        private String image;
        private String episodeCode;
        private boolean completed;
        private boolean locked;
        private long bestScore;
        private long bestStar;
        private long numberOfLevels;

        public Builder withId(long id) {
            this.id = id;
            return this;
        }

        public Builder withNumber(int number) {
            this.number = number;
            return this;
        }

        public Builder withCode(String code) {
            this.code = code;
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

        public Builder withEpisodeCode(String episodeCode) {
            this.episodeCode = episodeCode;
            return this;
        }

        public Builder withBestScore(long bestScore) {
            this.bestScore = bestScore;
            return this;
        }

        public Builder withBestStar(long bestStar) {
            this.bestStar = bestStar;
            return this;
        }

        public Builder withCompleted(int completed) {
            this.completed = completed == 1;
            return this;
        }

        public Builder withLocked(int locked) {
            this.locked = locked == 1;
            return this;
        }

        public Builder withNumberOfLevels(long numberOfLevels) {
            this.numberOfLevels = numberOfLevels;
            return this;
        }

        public Level build() {
            return new Level(id, number, code, name, image, episodeCode, bestScore, bestStar,
                    completed,
                    locked, numberOfLevels);
        }
    }
}
