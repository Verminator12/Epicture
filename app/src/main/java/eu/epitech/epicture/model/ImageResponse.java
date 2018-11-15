package eu.epitech.epicture.model;

import java.util.List;

public class ImageResponse extends BasicResponse {

    private List<ImgurImage> data;

    public List<ImgurImage> getData() {
        return  data;
    }
}
