package it.smasini.androidradar;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import it.smasini.radar.RadarPoint;
import it.smasini.radar.RadarView;

public class MainActivity extends AppCompatActivity {

    private RadarView radarView;
    private ArrayList<RadarPoint> points = new ArrayList<RadarPoint>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radarView = (RadarView) findViewById(R.id.radar_view);
        radarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAll();
            }
        });
        radarView.setOnRadarPinClickListener(new RadarView.OnRadarPinClickListener() {
            @Override
            public void onPinClicked(String identifier) {
                Toast.makeText(MainActivity.this, identifier, Toast.LENGTH_LONG).show();
            }
        });
        radarView.setReferencePoint(new RadarPoint("center", 44.139644f,12.246429f));
        startAll();
    }

    private void startAll(){
        points = new ArrayList<RadarPoint>();
        radarView.resetPoints();
        radarView.startAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RadarPoint r1 = new RadarPoint("identifier1", 44.139175f,12.247117f, "http://x1.xingassets.com/assets/frontend_minified/img/users/nobody_m.original.jpg");
                RadarPoint r2 = new RadarPoint("identifier2", 44.138205f,12.248533f, "http://x1.xingassets.com/assets/frontend_minified/img/users/nobody_m.original.jpg");
                RadarPoint r3 = new RadarPoint("identifier3", 44.137265f,12.250056f, "http://x1.xingassets.com/assets/frontend_minified/img/users/nobody_m.original.jpg");
                RadarPoint r4 = new RadarPoint("identifier4", 44.134374f,12.251215f, "http://x1.xingassets.com/assets/frontend_minified/img/users/nobody_m.original.jpg");
                RadarPoint r5 = new RadarPoint("identifier5", 44.132491f,12.248833f, "http://x1.xingassets.com/assets/frontend_minified/img/users/nobody_m.original.jpg");
                RadarPoint r6 = new RadarPoint("identifier6", 44.130676f,12.248908f, "http://x1.xingassets.com/assets/frontend_minified/img/users/nobody_m.original.jpg");
                RadarPoint r7 = new RadarPoint("identifier7", 44.128889f,12.248286f, "http://x1.xingassets.com/assets/frontend_minified/img/users/nobody_m.original.jpg");
                RadarPoint r8 = new RadarPoint("identifier8", 44.124769f,12.242053f, "http://x1.xingassets.com/assets/frontend_minified/img/users/nobody_m.original.jpg");
                RadarPoint r9 = new RadarPoint("identifier9", 44.118592f,12.242053f, "http://x1.xingassets.com/assets/frontend_minified/img/users/nobody_m.original.jpg");
                RadarPoint r10 = new RadarPoint("identifier10", 44.116289f,12.240840f, "http://x1.xingassets.com/assets/frontend_minified/img/users/nobody_m.original.jpg");

                points.add(r1);
                points.add(r2);
                points.add(r3);
                points.add(r4);
                points.add(r5);
                points.add(r6);
                points.add(r7);
                points.add(r8);
                points.add(r9);
                points.add(r10);

                radarView.setPoints(points);

            }
        }, 10000);
    }
}
