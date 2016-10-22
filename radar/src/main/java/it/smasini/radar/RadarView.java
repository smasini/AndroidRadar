package it.smasini.radar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Simone Masini on 16/10/2016.
 */
public class RadarView extends RelativeLayout {

    private OnRadarPinClickListener onRadarPinClickListener;
    private BitmapChangerListener bitmapChangerListener;
    private ArrayList<RadarPoint> points = new ArrayList<RadarPoint>();
    private ImageView imageViewBackground;
    private Radar radar;
    private boolean loadImageAsyncWithPicasso = false;
    private Picasso picasso;
    private boolean isStartedAnimation;
    private boolean maxDistanceSetted = false;
    private double maxDistance = -1;

    public RadarView(Context context) {
        super(context);
        init(context, null);
    }

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RadarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RadarView, 0, 0);
        int srcBackgroundImage = R.drawable.radar_background;
        int centerPinRadius;
        int pinsImage;
        int pinsRadius;
        int centerPinImage;
        int pinsColor = 0;
        int centerPinColor = 0;
        int backgroundColor = 0;
        int arrowColor = 0;
        try {
            srcBackgroundImage = ta.getResourceId(R.styleable.RadarView_radar_background, 0);
            loadImageAsyncWithPicasso = ta.getBoolean(R.styleable.RadarView_load_async_image, loadImageAsyncWithPicasso);

            pinsRadius = ta.getInt(R.styleable.RadarView_pins_radius, 0);
            centerPinRadius = ta.getInt(R.styleable.RadarView_center_pin_radius, 0);

            pinsImage = ta.getResourceId(R.styleable.RadarView_pins_image, 0);
            centerPinImage = ta.getResourceId(R.styleable.RadarView_center_pin_image, 0);

            pinsColor = ta.getColor(R.styleable.RadarView_pins_color, 0);
            centerPinColor = ta.getColor(R.styleable.RadarView_center_pin_color, 0);
            backgroundColor = ta.getColor(R.styleable.RadarView_background_color, 0);
            arrowColor = ta.getColor(R.styleable.RadarView_arrow_color, 0);

            maxDistance = ta.getInt(R.styleable.RadarView_max_distance, 0);
            maxDistanceSetted = maxDistance != 0;
        } finally {
            ta.recycle();
        }

        imageViewBackground = new ImageView(context);
        imageViewBackground.setAdjustViewBounds(true);
        imageViewBackground.setId(R.id.radar_background_image);
        if(srcBackgroundImage != 0){
            imageViewBackground.setImageResource(srcBackgroundImage);
        }
        LayoutParams lp1 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageViewBackground.setLayoutParams(lp1);

        radar = new Radar(context);
        LayoutParams lp2 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.addRule(ALIGN_BOTTOM, R.id.radar_background_image);
        lp2.addRule(ALIGN_TOP, R.id.radar_background_image);
        lp2.addRule(ALIGN_START, R.id.radar_background_image);
        lp2.addRule(ALIGN_END, R.id.radar_background_image);
        radar.setLayoutParams(lp2);
        radar.setMaxDistance((int)maxDistance);
        radar.setBackgroundColor(backgroundColor);
        radar.setCenterPinColor(centerPinColor);
        radar.setPinsColor(pinsColor);
        radar.setCenterPinRadius(centerPinRadius);
        radar.setPinsRadius(pinsRadius);
        radar.setCenterPinImage(centerPinImage);
        radar.setPinsImage(pinsImage);
        radar.setArrowColor(arrowColor);
        addView(imageViewBackground);
        addView(radar);

        if(!isInEditMode()) {
            Picasso.Builder picassoBuilder = new Picasso.Builder(context);
            picassoBuilder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    Log.e("PICASSO", uri.toString(), exception);
                }
            });
            picasso = picassoBuilder.build();
        }
    }

    public Radar getRadar() {
        return radar;
    }

    public ImageView getImageViewBackground() {
        return imageViewBackground;
    }

    public void setReferencePoint(RadarPoint referencePoint) {
        if(radar!=null)
            radar.setReferencePoint(referencePoint);
    }

    public void setMaxDistance(int maxDistance) {
        this.radar.setMaxDistance(maxDistance);
    }

    public void startAnimation() {
        if(radar!=null) {
            radar.startAnimation();
            isStartedAnimation = true;
        }
    }

    public void stopAnimation() {
        if(radar!=null) {
            radar.stopAnimation();
            isStartedAnimation = false;
        }
    }

    public void setPoints(ArrayList<RadarPoint> points) {
        if(!isStartedAnimation)
            radar.startAnimation();
        this.points = points;
        if(!maxDistanceSetted){
            for(RadarPoint rp : points){
                double distance = RadarUtility.distanceBetween(radar.getReferencePoint().x,radar.getReferencePoint().y, rp.x, rp.y);
                if(distance > maxDistance){
                    maxDistance = distance;
                }
            }
        }
        if(loadImageAsyncWithPicasso){
            for(RadarPoint rp : points){
                picasso.load(rp.getImageUrl()).into(new RadarPointTarget(rp, bitmapChangerListener) {
                    @Override
                    public void bitmapLoaded(boolean error) {
                        checkComplete();
                    }
                });
            }
        }else{
            checkComplete();
        }
    }

    public void resetPoints(){
        this.points = new ArrayList<>();
        radar.setPoints(points);
    }

    private void checkComplete(){
        if(isAllBitmapLoaded()){
            radar.stopAnimation();
            radar.setMaxDistance((int)maxDistance);
            radar.setPoints(points);
        }
    }

    public void setOnRadarPinClickListener(OnRadarPinClickListener onRadarPinClickListener){
        this.onRadarPinClickListener = onRadarPinClickListener;
        radar.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(RadarView.this.onRadarPinClickListener!=null) {
                    String pinIdentifier = radar.getTouchedPin(event);
                    if (pinIdentifier != null) {
                        RadarView.this.onRadarPinClickListener.onPinClicked(pinIdentifier);
                    }
                }
                return true;
            }
        });
    }


    public boolean isAllBitmapLoaded(){
        if(!loadImageAsyncWithPicasso){
            return true;
        }
        for(RadarPoint rp : points){
            if(!rp.isBitmapLoaded() && !rp.isBitmapLoadedError()){
                return false;
            }
        }
        return true;
    }

    public void setBitmapChangerListener(BitmapChangerListener bitmapChangerListener) {
        this.bitmapChangerListener = bitmapChangerListener;
    }

    public interface BitmapChangerListener{
        Bitmap changeBitmap(Bitmap bitmap);
    }

    public interface OnRadarPinClickListener{
        void onPinClicked(String identifier);
    }

}
