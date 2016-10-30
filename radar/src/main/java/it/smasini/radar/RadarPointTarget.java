package it.smasini.radar;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by Simone Masini on 22/10/2016.
 */
public abstract class RadarPointTarget implements Target {

    private RadarPoint radarPoint;

    public RadarPointTarget(RadarPoint radarPoint) {
        this.radarPoint = radarPoint;
    }


    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        radarPoint.setBitmap(bitmap);
        bitmapLoaded(false);
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
        Log.e("onBitmapFailed", errorDrawable.toString());
        radarPoint.setBitmapLoadedError(true);
        bitmapLoaded(true);
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        Log.d("onPrepareLoad","onPrepareLoad");
    }

    public abstract void bitmapLoaded(boolean error);
}
