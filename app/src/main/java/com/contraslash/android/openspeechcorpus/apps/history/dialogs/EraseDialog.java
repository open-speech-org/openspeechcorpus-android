package com.contraslash.android.openspeechcorpus.apps.history.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.contraslash.android.openspeechcorpus.R;
import com.contraslash.android.openspeechcorpus.apps.core.models.AudioData;
import com.contraslash.android.openspeechcorpus.apps.core.models.AudioDataDAO;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ma0 on 12/28/15.
 */
public class EraseDialog extends DialogFragment {

    AudioDataDAO audioDataDAO;
    ArrayList<AudioData> audioDatas;

    Context context;
    public EraseDialog()
    {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context= activity;
        audioDataDAO = new AudioDataDAO(context);
        audioDatas = audioDataDAO.readAll();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.erase_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteAllRecordings();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void deleteAllRecordings()
    {
        for(AudioData audioData: audioDatas)
        {
            File audioFile = new File(audioData.getFileLocation());
            if(audioFile.exists())
            {
                audioFile.delete();
            }
        }
    }
}
