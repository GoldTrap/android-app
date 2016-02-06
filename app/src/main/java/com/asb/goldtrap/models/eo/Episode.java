package com.asb.goldtrap.models.eo;

/**
 * Created by arjun on 06/02/16.
 */
public class Episode {
    private int number;
    private String name;
    private String image;
    private String code;
    private boolean completed;

    public Episode() {
    }

    private Episode(int number, String name, String image, String code, boolean completed) {
        this.number = number;
        this.name = name;
        this.image = image;
        this.code = code;
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

        private int number;
        private String name;
        private String image;
        private String code;
        private boolean completed = false;

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
            return new Episode(number, name, image, code, completed);
        }
    }
}
