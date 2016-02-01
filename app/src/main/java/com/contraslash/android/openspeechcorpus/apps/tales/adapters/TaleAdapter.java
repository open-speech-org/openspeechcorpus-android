package com.contraslash.android.openspeechcorpus.apps.tales.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.contraslash.android.openspeechcorpus.R;
import com.contraslash.android.openspeechcorpus.apps.tales.models.Tale;
import com.contraslash.android.openspeechcorpus.base.BaseCustomAdapter;

import java.util.List;

/**
 * Created by ma0 on 12/2/15.
 */
public class TaleAdapter extends BaseCustomAdapter {

    public TaleAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position,convertView,parent);

        Tale tale = (Tale)objects.get(position);
        TextView title = (TextView)view.findViewById(R.id.element_tale_name);
        title.setText(tale.getTitle());
        RatingBar calification = (RatingBar)view.findViewById(R.id.element_tale_calification);
        calification.setRating(tale.getCalification());
        TextView totalVotes = (TextView)view.findViewById(R.id.element_tale_total_votes);
        totalVotes.setText(context.getString(R.string.total_votes)+tale.getTotalVotes());

        return view;
    }
}
