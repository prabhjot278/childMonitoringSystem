package com.prabhjot.pschildmonitoringsystem.parentevrything;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prabhjot.pschildmonitoringsystem.R;
import com.prabhjot.pschildmonitoringsystem.basicmethods.usageHelper;
import com.prabhjot.pschildmonitoringsystem.parentevrything.usageFragment.OnListFragmentInteractionListener;


import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyusageRecyclerViewAdapter extends RecyclerView.Adapter<MyusageRecyclerViewAdapter.ViewHolder> {

    private final List<usageHelper> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyusageRecyclerViewAdapter(List<usageHelper> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_usage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.usagename.setText(mValues.get(position).getAppname());
        holder.usagelastused.setText(mValues.get(position).getLast_time_used());
        long minutes = TimeUnit.MILLISECONDS.toMinutes(mValues.get(position).getTotltime());
        long seconds = TimeUnit.MILLISECONDS.toSeconds(mValues.get(position).getTotltime());
        holder.usagetotaltime.setText(minutes+"m "+seconds+"s");

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView usagename;
        public final TextView usagelastused;
        public final TextView usagetotaltime;
        public usageHelper mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            usagename = (TextView) view.findViewById(R.id.usage_name);
            usagelastused = (TextView) view.findViewById(R.id.usage_lastused);
            usagetotaltime = (TextView) view.findViewById(R.id.usage_totaltime);
        }

    }
}
