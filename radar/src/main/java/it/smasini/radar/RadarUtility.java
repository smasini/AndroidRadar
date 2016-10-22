package it.smasini.radar;

/**
 * Created by Simone Masini on 22/10/2016.
 */
public class RadarUtility {

    public static float distanceBetween(float lat1, float lon1, float lat2, float lon2) {
        float R = 6371; // km
        float dLat = (float)((lat2-lat1)*Math.PI/180);
        float dLon = (float)((lon2-lon1)*Math.PI/180);
        lat1 = (float)(lat1*Math.PI/180);
        lat2 = (float)(lat2*Math.PI/180);

        float a = (float)(Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2));
        float c = (float)(2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)));
        float d = R * c * 1000;

        return d;
    }
}
