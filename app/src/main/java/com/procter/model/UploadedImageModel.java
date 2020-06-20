package com.procter.model;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;

/**
 * Created by Ashish on 24/7/19.
 */

public class UploadedImageModel {
    private Bitmap imageBitmap;
    private File imageFile;
    private String imageUrl;
    private Uri imageUri;
    private String imageId;

    public UploadedImageModel(String imageId, Bitmap imageBitmap, File imageFile, String imageUrl, Uri imageUri) {
        this.imageId = imageId;
        this.imageBitmap = imageBitmap;
        this.imageFile = imageFile;
        this.imageUrl = imageUrl;
        this.imageUri = imageUri;
    }

    public UploadedImageModel(String imageUrl,String imageId){
        this.imageUrl=imageUrl;
        this.imageId=imageId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
