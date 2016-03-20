package com.asb.goldtrap.models.eo;

import com.asb.goldtrap.models.results.Result;

import java.util.Date;

/**
 * Created by arjun on 20/03/16.
 */
public class Attempt {
    private long id;
    private long levelId;
    private String levelCode;
    private Date time;
    private long score;
    private long star;
    private Result result;

    private Attempt(long id, long levelId, String levelCode, Date time, long score, long star,
                    Result result) {
        this.id = id;
        this.levelId = levelId;
        this.levelCode = levelCode;
        this.time = time;
        this.score = score;
        this.star = star;
        this.result = result;
    }

    public long getId() {
        return id;
    }

    public long getLevelId() {
        return levelId;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public Date getTime() {
        return time;
    }

    public long getScore() {
        return score;
    }

    public long getStar() {
        return star;
    }

    public Result getResult() {
        return result;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private long id;
        private long levelId;
        private String levelCode;
        private Date time;
        private long score;
        private long star;
        private Result result;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setLevelId(long levelId) {
            this.levelId = levelId;
            return this;
        }

        public Builder setLevelCode(String levelCode) {
            this.levelCode = levelCode;
            return this;
        }

        public Builder setTime(Date time) {
            this.time = time;
            return this;
        }

        public Builder setTime(long time) {
            this.time = new Date(time);
            return this;
        }

        public Builder setScore(long score) {
            this.score = score;
            return this;
        }

        public Builder setStar(long star) {
            this.star = star;
            return this;
        }

        public Builder setResult(Result result) {
            this.result = result;
            return this;
        }

        public Builder setResult(String result) {
            this.result = Result.valueOf(result);
            return this;
        }

        public Attempt build() {
            return new Attempt(id, levelId, levelCode, time, score, star, result);
        }
    }
}
