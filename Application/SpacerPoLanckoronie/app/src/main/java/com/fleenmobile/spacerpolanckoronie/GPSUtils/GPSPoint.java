package com.fleenmobile.spacerpolanckoronie.GPSUtils;

/**
 * This class just represents a point on the map
 * described by latitude and longitude
 *
 * @author FleenMobile at 2015-08-22
 */
public class GPSPoint {
    private double latitude;
    private double longitude;

    public GPSPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
