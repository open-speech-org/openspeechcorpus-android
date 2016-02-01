package com.contraslash.android.openspeechcorpus.apps.profile.models;

/**
 * Created by ma0 on 1/11/16.
 */
public class RankingPosition {

    int position;
    String name;
    int recordings;

    public RankingPosition(int position, String name, int recordings) {
        this.position = position;
        this.name = name;
        this.recordings = recordings;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRecordings() {
        return recordings;
    }

    public void setRecordings(int recordings) {
        this.recordings = recordings;
    }
}
