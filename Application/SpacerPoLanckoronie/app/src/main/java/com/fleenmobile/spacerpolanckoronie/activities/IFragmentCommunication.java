package com.fleenmobile.spacerpolanckoronie.activities;

/**
 * @author FleenMobile at 2015-08-22
 */
public interface IFragmentCommunication {

    /**
     * This method allow fragments to communicate with the activity
     * @param mssg Main reason of the mssg
     * @param arg Argument connected to this mssg
     */
    public void onMssgReceived(String mssg, String arg);
}
