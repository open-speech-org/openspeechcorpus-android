package com.contraslash.android.openspeechcorpus.apps.profile.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.contraslash.android.openspeechcorpus.R;
import com.contraslash.android.openspeechcorpus.apps.profile.models.RankingPosition;
import com.contraslash.android.openspeechcorpus.base.BaseCustomAdapter;

import java.util.List;

/**
 * Created by ma0 on 1/11/16.
 */
public class RankingPositionAdapter extends BaseCustomAdapter {
    public RankingPositionAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        RankingPosition rankingPosition = (RankingPosition)objects.get(position);

        TextView pos = (TextView)view.findViewById(R.id.element_ranking_position_position);
        pos.setText(rankingPosition.getPosition()+"");
        TextView name = (TextView)view.findViewById(R.id.element_ranking_position_name);
        name.setText(rankingPosition.getName());
        TextView recordings = (TextView)view.findViewById(R.id.element_ranking_position_recordings);
        recordings.setText(rankingPosition.getRecordings()+"");


        return view;
    }
}
