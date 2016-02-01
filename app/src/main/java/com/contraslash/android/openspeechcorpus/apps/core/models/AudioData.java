package com.contraslash.android.openspeechcorpus.apps.core.models;

import com.contraslash.android.openspeechcorpus.db.Table;

/**
 * Created by ma0 on 11/3/15.
 */
public class AudioData extends Table {

    static String TABLE_NAME = DB_PREFIX + "audio_data";

    int sentence_id;
    String sentence_text;
    String fileLocation;

    /**
     * Int determinate state of record: 0 Not recorded, 1 Uploaded, 2 Skipped
     */
    int uploaded;

    public AudioData() {
    }

    public AudioData( int _id, int sentence_id, String text, String fileLocation, int uploaded) {
        this._id=_id;
        this.sentence_id = sentence_id;
        this.sentence_text = text;
        this.fileLocation = fileLocation;
        this.uploaded=uploaded;
    }





    public String getTABLE_NAME() {
        return TABLE_NAME;
    }

    public void setTABLE_NAME(String TABLE_NAME) {
        this.TABLE_NAME = TABLE_NAME;
    }

    public int getSentence_id() {
        return sentence_id;
    }

    public void setSentence_id(int sentence_id) {
        this.sentence_id = sentence_id;
    }

    public String getSentence_text() {
        return sentence_text;
    }

    public void setSentence_text(String sentence_text) {
        this.sentence_text = sentence_text;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }



    public int getUploaded() {
        return uploaded;
    }

    public void setUploaded(int uploaded) {
        this.uploaded = uploaded;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String toString() {
        if(this.getSentence_text()!=null)
        {
            return this.getSentence_text();
        }
        else
        {
            return "";
        }
    }
}
