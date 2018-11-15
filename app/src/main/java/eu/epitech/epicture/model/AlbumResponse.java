package eu.epitech.epicture.model;

import java.util.List;

public class AlbumResponse extends BasicResponse {

    private List<Album> data;

    public List<Album> getData() {
        return  data;
    }
}
