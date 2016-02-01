package com.contraslash.android.openspeechcorpus.base;

import android.app.Activity;
import android.app.DialogFragment;

/**
 * Created by ma0 on 12/30/15.
 */
public class BaseDialog extends DialogFragment {

    protected String TAG;

    protected BaseActivity baseActivity;

    public BaseDialog()
    {
        TAG = this.getClass().getSimpleName();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.baseActivity=(BaseActivity)activity;
    }


}
