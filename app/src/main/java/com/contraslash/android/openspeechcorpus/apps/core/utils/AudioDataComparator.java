package com.contraslash.android.openspeechcorpus.apps.core.utils;

import com.contraslash.android.openspeechcorpus.apps.core.models.AudioData;

import java.util.Comparator;

/**
 * Created by ma0 on 12/9/15.
 */
public class AudioDataComparator implements Comparator<AudioData> {
    @Override
    public int compare(AudioData lhs, AudioData rhs) {
        return lhs.getSentence_id() - rhs.getSentence_id();
    }
}
