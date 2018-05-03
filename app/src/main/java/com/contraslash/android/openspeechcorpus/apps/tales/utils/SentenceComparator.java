package com.contraslash.android.openspeechcorpus.apps.tales.utils;

import com.contraslash.android.openspeechcorpus.apps.tales.models.Sentence;

import java.util.Comparator;

/**
 * Created by ma0 on 2/18/16.
 */
public class SentenceComparator implements Comparator<Sentence> {
    @Override
    public int compare(Sentence lhs, Sentence rhs) {
        return lhs.getId() - rhs.getId();
        }
    }