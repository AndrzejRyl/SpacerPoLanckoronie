package com.fleenmobile.spacerpolanckoronie.adapters;

import com.fleenmobile.spacerpolanckoronie.GPSUtils.GPSPoint;
import com.fleenmobile.spacerpolanckoronie.GPSUtils.GPSRange;

/**
 * This class contains all the information connected
 * to interesting places in Lanckorona
 *
 * @author FleenMobile at 2015-08-22
 */
public class InterestingPlace {

    private String name;
    private String description;
    private String shortDesc;
    private int image;
    private int audio;
    private GPSPoint coordinates;
    private GPSRange range;

    public InterestingPlace(String name, String description, String shortDesc, int image, int audio, GPSPoint coordinates, GPSRange range) {
        this.name = name;
        this.description = description;
        this.shortDesc = shortDesc;
        this.image = image;
        this.audio = audio;
        this.coordinates = coordinates;
        this.range = range;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getAudio() {
        return audio;
    }

    public void setAudio(int audio) {
        this.audio = audio;
    }

    public GPSPoint getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(GPSPoint coordinates) {
        this.coordinates = coordinates;
    }

    public GPSRange getRange() {
        return range;
    }

    public void setRange(GPSRange range) {
        this.range = range;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }
}
