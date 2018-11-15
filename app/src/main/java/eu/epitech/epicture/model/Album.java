package eu.epitech.epicture.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

public class Album implements Parcelable {

    private final String id;
    private final String title;
    private final String description;
    private final Integer datetime;
    private final String cover;
    private final Integer cover_width;
    private final Integer cover_height;
    private final String account_url;
    private final Integer account_id;
    private final String privacy;
    private final String link;
    private final String layout;
    private final Integer views;
    private final Boolean favorite;
    private final Boolean nsfw;
    private final String section;
    private final Integer order;
    private final String deletehash;
    private final Integer images_count;
    //private final List<Image> images;
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

    public String getCover() {
        return cover;
    }

    public Integer getCover_width() {
        return cover_width;
    }

    public Integer getCover_height() {
        return cover_height;
    }

    public String getAccount_url() {
        return account_url;
    }

    public Integer getAccount_id() {
        return account_id;
    }

    public String getPrivacy() {
        return privacy;
    }

    public Integer getImages_count() {
        return images_count;
    }

/*    public List<Image> getImages() {
        return images;
    }*/

    public String getLink() {
        return link;
    }

    public String getLayout() {
        return layout;
    }

    public Integer getOrder() {
        return order;
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
        out.writeString(cover);
        out.writeInt(cover_width);
        out.writeInt(cover_height);
        out.writeString(account_url);
        out.writeInt(account_id);
        out.writeString(privacy);
        out.writeString(layout);
        out.writeInt(views);
        out.writeString(link);
        out.writeInt(favorite ? 1 : 0);
        out.writeInt(nsfw ? 1 : 0);
        out.writeString(section);
        out.writeInt(order);
        out.writeString(deletehash);
        out.writeInt(images_count);
        //out.writeList(images);
        out.writeInt(in_gallery ? 1 : 0);
    }

    public static final Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>() {
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    private Album(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        datetime = in.readInt();
        cover = in.readString();
        cover_width = in.readInt();
        cover_height = in.readInt();
        account_url = in.readString();
        account_id = in.readInt();
        privacy = in.readString();
        layout = in.readString();
        views = in.readInt();
        link = in.readString();
        favorite = (in.readInt() == 1);
        nsfw = (in.readInt() == 1);
        section = in.readString();
        order = in.readInt();
        deletehash = in.readString();
        images_count = in.readInt();
        //images = in.readArrayList(Image);
        in_gallery = (in.readInt() == 1);
    }
}
