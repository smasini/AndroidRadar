package it.smasini.radar;

import android.graphics.Bitmap;

/**
 * Created by Simone Masini on 12/10/2016.
 */
public class RadarPoint {

    public float x;
    public float y;
    public int radius;
    public String identifier;
    private String imageUrl;
    private Bitmap bitmap;
    private boolean bitmapLoaded = false;
    private boolean bitmapLoadedError = false;

    public RadarPoint(String identifier, float x, float y, int radius){
        this.identifier = identifier;
        this.radius = radius;
        this.x = x;
        this.y = y;
    }

    public RadarPoint(String identifier, float x, float y){
        this.identifier = identifier;
        this.x = x;
        this.y = y;
    }

    public RadarPoint(String identifier, float x, float y, int radius, String imageUrl) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.identifier = identifier;
        this.imageUrl = imageUrl;
    }

    public RadarPoint(String identifier, float x, float y, String imageUrl) {
        this.x = x;
        this.y = y;
        this.identifier = identifier;
        this.imageUrl = imageUrl;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.bitmapLoaded = true;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public boolean isBitmapLoaded() {
        return bitmapLoaded;
    }

    public boolean isBitmapLoadedError() {
        return bitmapLoadedError;
    }

    public void setBitmapLoadedError(boolean bitmapLoadedError) {
        this.bitmapLoadedError = bitmapLoadedError;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
