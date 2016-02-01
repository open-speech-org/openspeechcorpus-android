package com.contraslash.android.openspeechcorpus.apps.miscellany.models;

import com.contraslash.android.openspeechcorpus.db.Table;

/**
 * Created by ma0 on 1/8/16.
 */
public class Command extends Table {


    public static String TABLE_NAME = DB_PREFIX + "command";
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    String text;
    int commandId;
    String fileLocation;


    /**
     * Int determinate state of record: 0 Not recorded, 1 Uploaded, 2 Skipped
     */
    int uploaded;

    public Command(){}

    public Command(int commandId,String text, String fileLocation, int uploaded)
    {
        this.commandId=commandId;
        this.text=text;
        this.fileLocation=fileLocation;
        this.uploaded=uploaded;
    }

    public int getUploaded() {
        return uploaded;
    }

    public void setUploaded(int uploaded) {
        this.uploaded = uploaded;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }



    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public int getCommandId() {
        return commandId;
    }

    public void setCommandId(int commandId) {
        this.commandId = commandId;
    }
}
