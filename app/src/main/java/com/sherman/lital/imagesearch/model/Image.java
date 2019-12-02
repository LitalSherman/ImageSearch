package com.sherman.lital.imagesearch.model;

import java.io.Serializable;

/**
 * Created by Vlad on 9/22/2019.
 */

public class Image implements Serializable{

    private String theme;
    private String previewURL;
    private String webformatURL;
    private int newHeight;
    private int newWidth;



    public Image(String previewURL, String webformatURL, int newHeight, int newWidth) {
        this.previewURL = previewURL;
        this.webformatURL = webformatURL;
        this.newHeight = newHeight;
        this.newWidth = newWidth;
    }

    public String getPreviewURL() {
        return previewURL;
    }

    public void setPreviewURL(String previewURL) {
        this.previewURL = previewURL;
    }

    public String getWebformatURL() {
        return webformatURL;
    }

    public void setWebformatURL(String webformatURL) {
        this.webformatURL = webformatURL;
    }

    public int getNewHeight() {
        return newHeight;
    }

    public void setNewHeight(int newHeight) {
        this.newHeight = newHeight;
    }

    public int getNewWidth() {
        return newWidth;
    }

    public void setNewWidth(int newWidth) {
        this.newWidth = newWidth;
    }
}
