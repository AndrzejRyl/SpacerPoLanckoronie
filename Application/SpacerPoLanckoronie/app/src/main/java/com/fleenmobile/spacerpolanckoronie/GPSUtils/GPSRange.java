package com.fleenmobile.spacerpolanckoronie.GPSUtils;

import android.location.Location;

/**
 * This class represents a rectangular range described by two
 * points on the map
 *
 * @author FleenMobile at 2015-08-22
 */
public class GPSRange {
    private GPSPoint leftBottom, rightTop;

    public GPSRange(GPSPoint leftBottom, GPSPoint rightTop) {
        this.leftBottom = leftBottom;
        this.rightTop = rightTop;
    }

    public GPSPoint getLeftBottom() {
        return leftBottom;
    }

    public void setLeftBottom(GPSPoint leftBottom) {
        this.leftBottom = leftBottom;
    }

    public GPSPoint getRightTop() {
        return rightTop;
    }

    public void setRightTop(GPSPoint rightTop) {
        this.rightTop = rightTop;
    }

    /**
     * Checks whether the given location is in this range or not
     *
     * @param location Location we're checking
     * @return True if this location is in range and False otherwise
     */
    public boolean inRange(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        if (latitude > leftBottom.getLatitude() &&
                latitude < rightTop.getLatitude() &&
                longitude > leftBottom.getLongitude() &&
                longitude < rightTop.getLongitude())
            return true;

        return false;
    }
}
