package it.smasini.radar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Location;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Simone Masini on 12/10/2016.
 */
public class Radar extends View {

    private ArrayList<RadarPoint> pinsInCanvas = new ArrayList<RadarPoint>();
    private Context context;
    private Canvas canvas;
    private int zoomDistance;
    private boolean showRadarAnimation = false;

    public void setPoints(ArrayList<RadarPoint> points) {
        this.points = points;
        invalidate();
    }

    public ArrayList<RadarPoint> getPoints() {
        return points;
    }

    private ArrayList<RadarPoint> points  = new ArrayList<RadarPoint>();

    private RadarPoint referencePoint;


    private final int DEFAULT_MAX_DISTANCE = 10000;

    private final int DEFAULT_PINS_RADIUS = 60;
    private final int DEFAULT_CENTER_PIN_RADIUS = 18;
    private final int DEFAULT_PINS_COLORS = Color.GREEN;
    private final int DEFAULT_CENTER_PIN_COLOR = Color.RED;
    private final int DEFAULT_BACKGROUND_COLOR = Color.CYAN;

    private int pinsImage;
    private int centerPinImage;

    private int maxDistance;
    private int pinsRadius;
    private int centerPinRadius;
    private int pinsColor;
    private int centerPinColor;
    private int backgroundColor;
    private int arrowColor;

    float alpha = 0;
    private int fps = 50;
    private final int POINT_ARRAY_SIZE = 25;
    Point latestPoint[] = new Point[POINT_ARRAY_SIZE];
    Paint latestPaint[] = new Paint[POINT_ARRAY_SIZE];

    public Radar(Context context) {
        this(context, null);
        init(context, null);
    }

    public Radar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs);
    }

    public Radar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        this.context = context;
        setPaint(DEFAULT_BACKGROUND_COLOR);

        referencePoint = new RadarPoint("example", 10.00000f,22.0000f);
    }

    public void setPaint(int color){
        Paint localPaint = new Paint();
        localPaint.setColor(color);
        localPaint.setAntiAlias(true);
        localPaint.setStyle(Paint.Style.STROKE);
        localPaint.setStrokeWidth(1.0F);
        localPaint.setAlpha(0);
        int alpha_step = 255 / POINT_ARRAY_SIZE;
        for (int i=0; i < latestPaint.length; i++) {
            latestPaint[i] = new Paint(localPaint);
            latestPaint[i].setAlpha(255 - (i* alpha_step));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        makeRadar();
    }

    public void refresh() {
        invalidate();
    }

    protected void makeRadar() {
        pinsInCanvas = new ArrayList<RadarPoint>();

        int width = getWidth();

        drawStroke(width / 2, width / 2, getBackgroundColor(), width / 2);

        if (centerPinImage != 0) {
            long pnt = (width / 2) - getCenterPinRadius();
            drawImage(pnt,pnt, centerPinImage, getCenterPinRadius()*2);
        }else{
            drawPin(width / 2, width / 2, getCenterPinColor(), getCenterPinRadius());
        }

        int pxCanvas = width/2;
        int metterDistance ;

        maxDistance = getMaxDistance();

        Location u0 = new Location("");
        u0.setLatitude(referencePoint.x);
        u0.setLongitude(referencePoint.y);

        ArrayList<Location> locations = buildLocations(u0);


        metterDistance = zoomDistance + (zoomDistance/16);
        if (metterDistance > maxDistance) metterDistance = maxDistance;

        if(showRadarAnimation) {
            drawLine();
        }else{
            drawPins(u0, locations, pxCanvas, metterDistance);
        }
    }


    ArrayList<Location> buildLocations(Location referenceLocation){

        zoomDistance = 0;

        ArrayList<Location> locations = new ArrayList<Location>();

        for (int i = 0; i < points.size(); i++) {

            Location uLocation = new Location("");
            uLocation.setLatitude(points.get(i).x);
            uLocation.setLongitude(points.get(i).y);
            locations.add(uLocation);

            if (zoomDistance < distanceBetween(referenceLocation, uLocation)) {
                zoomDistance = Math.round(distanceBetween(referenceLocation, uLocation));
            }
        }

        return locations ;
    }

    void drawPins(Location referenceLocation, ArrayList<Location> locations, int pxCanvas, int metterDistance){

        Random rand = new Random();

        for (int i = 0; i < locations.size(); i++) {

            int distance = Math.round(distanceBetween(referenceLocation, locations.get(i)));

            if (distance > maxDistance) continue;

            int virtualDistance = (distance * pxCanvas / metterDistance) ;

            int angle = rand.nextInt(360)+1;

            long cX = pxCanvas + Math.round(virtualDistance*Math.cos(angle*Math.PI/180));
            long cY = pxCanvas + Math.round(virtualDistance*Math.sin(angle * Math.PI / 180));

            pinsInCanvas.add(new RadarPoint(points.get(i).identifier, cX, cY, getPinsRadius()));

            if (pinsImage != 0) {
                long pnt = cX - getPinsRadius();
                long pnt2 = cY - getPinsRadius();
                drawImage(pnt, pnt2, pinsImage, getPinsRadius()*2);
            }else{
                RadarPoint rp = points.get(i);
                if(!rp.isBitmapLoadedError() && rp.getBitmap() != null) {
                    long pnt = cX - getPinsRadius();
                    long pnt2 = cY - getPinsRadius();
                    drawImage(pnt, pnt2, points.get(i).getBitmap(), getPinsRadius());
                }else{
                    drawPin(cX, cY, getPinsColor(), getPinsRadius());
                }
            }
        }
    }

    float distanceBetween(Location l1, Location l2)
    {
        float lat1= (float)l1.getLatitude();
        float lon1=(float)l1.getLongitude();
        float lat2=(float)l2.getLatitude();
        float lon2=(float)l2.getLongitude();
        return RadarUtility.distanceBetween(lat1, lon1, lat2, lon2);
    }

    public void drawImage(long x, long y, Bitmap bitmap, int size){
        Bitmap scaledBitmap =  Bitmap.createScaledBitmap(bitmap, size, size, true);
        canvas.drawBitmap(scaledBitmap, x, y, null);
    }

    public void drawLine(){
        int width = getWidth();
        int height = getHeight();

        int r = Math.min(width, height);

        int i = r / 2;
        int j = i - 1;
        Paint localPaint = latestPaint[0]; // GREEN
        alpha -= 0.5;
        if (alpha < -360) alpha = 0;
        double angle = Math.toRadians(alpha);
        int offsetX =  (int) (i + (float)(i * Math.cos(angle)));
        int offsetY = (int) (i - (float)(i * Math.sin(angle)));

        latestPoint[0]= new Point(offsetX, offsetY);

        for (int x=POINT_ARRAY_SIZE-1; x > 0; x--) {
            latestPoint[x] = latestPoint[x-1];
        }

        int lines = 0;
        for (int x = 0; x < POINT_ARRAY_SIZE; x++) {
            Point point = latestPoint[x];
            if (point != null) {
                canvas.drawLine(i, i, point.x, point.y, latestPaint[x]);
            }
        }
        lines = 0;
        for (Point p : latestPoint) if (p != null) lines++;

    }

    public void drawImage(long x, long y, int image,int size){
        Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), image);
        Bitmap scaledBitmap =  Bitmap.createScaledBitmap(myBitmap, size, size, true);
        canvas.drawBitmap( scaledBitmap, x, y, null);
    }

    public void drawStroke(long x, long y, int Color,int radius){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10.0F);
        paint.setColor(Color);
        canvas.drawCircle(x, y, radius, paint);
    }

    public void drawPin(long x, long y, int Color,int radius){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color);
        canvas.drawCircle(x, y, radius, paint);
    }

    public String getTouchedPin(MotionEvent event) {

        int xTouch;
        int yTouch;

        // get touch event coordinates and make transparent circle from it
        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                // it's the first pointer, so clear all existing pointers data

                xTouch = (int) event.getX(0);
                yTouch = (int) event.getY(0);

                // check if we've touched inside some circle
                return getTouchedCircle(xTouch, yTouch);
        }
        return  null;
    }

    private String getTouchedCircle(final int xTouch, final int yTouch) {
        RadarPoint touched = null;

        for (RadarPoint rPoint : pinsInCanvas) {

            if ((rPoint.x - xTouch) * (rPoint.x - xTouch) + (rPoint.y - yTouch) * (rPoint.y - yTouch) <= rPoint.radius * rPoint.radius * (rPoint.radius/2)) {
                touched = rPoint;
                break;
            }
        }

        if (touched != null) return touched.identifier;
        return null;
    }

    public int getPinsRadius() {
        if (pinsRadius == 0) return DEFAULT_PINS_RADIUS;
        return pinsRadius;
    }

    public int getPinsColor() {
        if (pinsColor == 0) return DEFAULT_PINS_COLORS;
        return pinsColor;
    }

    public int getCenterPinRadius() {
        if (centerPinRadius == 0) return DEFAULT_CENTER_PIN_RADIUS;
        return centerPinRadius;
    }

    public int getCenterPinColor() {
        if (centerPinColor == 0) return DEFAULT_CENTER_PIN_COLOR;
        return centerPinColor;
    }

    public void setPinsColor(int pinsColor) {
        this.pinsColor = pinsColor;
    }

    public void setCenterPinColor(int centerPinColor) {
        this.centerPinColor = centerPinColor;
    }

    public int getBackgroundColor() {
        if (backgroundColor == 0) return DEFAULT_BACKGROUND_COLOR;
        return backgroundColor;
    }

    public void setPinsImage(int pinsImage) {
        this.pinsImage = pinsImage;
    }

    public void setCenterPinImage(int centerPinImage) {
        this.centerPinImage = centerPinImage;
    }

    public void setPinsRadius(int pinsRadius) {
        this.pinsRadius = pinsRadius;
    }


    public void setArrowColor(int arrowColor) {
        this.arrowColor = arrowColor;
        setPaint(getArrowColor());
    }

    public int getArrowColor() {
        if (arrowColor == 0) return DEFAULT_BACKGROUND_COLOR;
        return arrowColor;
    }

    public void setCenterPinRadius(int centerPinRadius) {
        this.centerPinRadius = centerPinRadius;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    public int getMaxDistance() {
        if (maxDistance == 0) return DEFAULT_MAX_DISTANCE;
        if (maxDistance < 0) return 1000000000;
        return maxDistance;
    }


    public void setReferencePoint(RadarPoint referencePoint) {
        this.referencePoint = referencePoint;
    }

    android.os.Handler mHandler = new android.os.Handler();
    Runnable mTick = new Runnable() {
        @Override
        public void run() {
            invalidate();
            mHandler.postDelayed(this, 1000 / fps);
        }
    };


    public void startAnimation() {
        showRadarAnimation = true;
        mHandler.removeCallbacks(mTick);
        mHandler.post(mTick);
    }

    public void stopAnimation() {
        showRadarAnimation = false;
        mHandler.removeCallbacks(mTick);
    }

    public RadarPoint getReferencePoint() {
        return referencePoint;
    }
}
