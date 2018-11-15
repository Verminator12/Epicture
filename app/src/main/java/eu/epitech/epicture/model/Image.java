package eu.epitech.epicture.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Image implements Parcelable {

    private final String id;
    private final String title;
    private final String description;
    private final Integer datetime;
    private final String type;
    private final Boolean animated;
    private final Integer width;
    private final Integer height;
    private final Integer size;
    private final Integer views;
    private final Double bandwidth;
    private final String deletehash;
    private final String name;
    private final String section;
    private final String link;
    private final String gifv;
    private final String mp4;
    private final Integer mp4_size;
    private final Boolean looping;
    private final Boolean favorite;
    private final Boolean nsfw;
    private final String vote;
    private final Boolean in_gallery;


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

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(title);
        out.writeString(description);
        out.writeInt(datetime);
        out.writeString(type);
        out.writeInt(animated ? 1 : 0);
        out.writeInt(width);
        out.writeInt(height);
        out.writeInt(size);
        out.writeInt(views);
        out.writeDouble(bandwidth);
        out.writeString(deletehash);
        out.writeString(name);
        out.writeString(section);
        out.writeString(link);
        out.writeString(gifv);
        out.writeString(mp4);
        out.writeInt(mp4_size);
        out.writeInt(looping ? 1 : 0);
        out.writeInt(favorite ? 1 : 0);
        out.writeInt(nsfw ? 1 : 0);
        out.writeString(vote);
        out.writeInt(in_gallery ? 1 : 0);
    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    private Image(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        datetime = in.readInt();
        type = in.readString();
        animated = (in.readInt() == 1);
        width = in.readInt();
        height = in.readInt();
        size = in.readInt();
        views = in.readInt();
        bandwidth = in.readDouble();
        deletehash = in.readString();
        name = in.readString();
        section = in.readString();
        link = in.readString();
        gifv = in.readString();
        mp4 = in.readString();
        mp4_size = in.readInt();
        looping = (in.readInt() == 1);
        favorite = (in.readInt() == 1);
        nsfw = (in.readInt() == 1);
        vote = in.readString();
        in_gallery = (in.readInt() == 1);
    }
}
