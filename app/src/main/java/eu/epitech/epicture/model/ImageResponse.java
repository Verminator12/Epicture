package eu.epitech.epicture.model;

import java.util.List;

public class ImageResponse extends BasicResponse {

    private List<Image> data;

    public List<Image> getData() {
        return  data;
    }
}
