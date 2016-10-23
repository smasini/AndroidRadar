# AndroidRadar
Android library for a radar view with scan and load points as image or colored circle

### Download:

Add this to your gradle file:
```java
    compile 'it.smasini:android-radar:1.0'
    compile 'com.squareup.picasso:picasso:2.5.2'
```
We need picasso for loading image.

### Usages:

In your xml add the RadarView, like this:
```xml
	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:app="http://schemas.android.com/apk/res-auto"
	    >
	    <it.smasini.radar.RadarView
        app:load_async_image="true"
        app:center_pin_color="#000000"
        app:background_color="#FFFFFF"
        app:arrow_color="#4CAF50"
        app:pins_radius="100"
        android:id="@+id/radar_view"
        app:radar_background="@drawable/radar_background"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
	</LinearLayout>
```

In the activity we need to set a reference point, and then we can call method for start or stop the animation of the arrow:

```java
    radarView.setReferencePoint(new RadarPoint("center", 44.139644f,12.246429f));

    radarView.startAnimation();
    radarView.stopAnimation();
```

We can set a List of point like this:

```java
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
```


#### Advenced usages:

Other Attributes:

```xml
    app:radar_background='@drawable/radar_background'//res for image of background
    app:load_async_image='@drawable/radar_background'//force to load image from url or draw pin without image
    app:center_pin_radius="20" // Radius of the pin in the center
    app:pins_radius="20" // Radius of pins on the Radar
    app:pins_color="@color/light_green" // Color of pins on the Radar
    app:background_color="@color/light_green" // Color of the circle of the Radar
    app:arrow_color="@color/light_green" // Color of arrow that animate the Radar
    app:center_pin_color="@color/fourth_color" // Color of the pin in the center
    app:radar_image="@drawable/radar_background" // image of the radar
    app:pins_image="@drawable/pin" // pins icon
    app:center_pin_image="@drawable/center_pin" //  center pin icon
    app:max_distance="-1" //  Max distance by metters to cover, -1 to infinit, default velue is 10000

```
