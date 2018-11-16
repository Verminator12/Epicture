package eu.epitech.epicture.model;

import java.util.Date;

public class ImgurImage {
    private String id;
    private String title;
    private String description;
    private Integer datetime;
    private String type;
    private Boolean animated;
    private Integer width;
    private Integer height;
    private Integer size;
    private Integer views;
    private Double bandwidth;
    private String deletehash;
    private String name;
    private String section;
    private String link;
    private String gifv;
    private String mp4;
    private Integer mp4_size;
    private Boolean looping;
    private Boolean favorite;
    private Boolean nsfw;
    private String vote;
    private Boolean in_gallery;


    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public Date getDatetimeAsDate() {
        long dateTimeMilliseconds = datetime * 1000;

        return new Date(dateTimeMilliseconds);
    }
    public Integer getDatetime() {
        return datetime;
    }
    public String getType() {
        return type;
    }
    public Boolean getAnimated() {
        return animated;
    }
    public Integer getWidth() {
        return width;
    }
    public Integer getHeight() {
        return height;
    }
    public Integer getSize() {
        return size;
    }
    public Integer getViews() {
        return views;
    }
    public Double getBandwidth() {
        return bandwidth;
    }
    public String getDeletehash() {
        return deletehash;
    }
    public String getName() {
        return name;
    }
    public String getSection() {
        return section;
    }
    public String getLink() {
        return link;
    }
    public String getGifv() {
        return gifv;
    }
    public String getMp4() {
        return mp4;
    }
    public Integer getMp4_size() {
        return mp4_size;
    }
    public Boolean getLooping() {
        return looping;
    }
    public Boolean getFavorite() {
        return favorite;
    }
    public Boolean getNsfw() {
        return nsfw;
    }
    public String getVote() {
        return vote;
    }
    public Boolean getIn_gallery() {
        return in_gallery;
    }
}
