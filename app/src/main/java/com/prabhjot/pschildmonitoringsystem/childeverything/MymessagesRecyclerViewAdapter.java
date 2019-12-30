package com.prabhjot.pschildmonitoringsystem.childeverything;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.prabhjot.pschildmonitoringsystem.R;
import com.prabhjot.pschildmonitoringsystem.basicmethods.messageservice;
import com.prabhjot.pschildmonitoringsystem.basicmethods.sendingmessage;
import com.prabhjot.pschildmonitoringsystem.childeverything.messagesFragment.OnListFragmentInteractionListener;


import java.util.List;

/**
 *
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MymessagesRecyclerViewAdapter extends RecyclerView.Adapter<MymessagesRecyclerViewAdapter.ViewHolder> {

    private final List<sendingmessage> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MymessagesRecyclerViewAdapter(List<sendingmessage> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_messages, parent, false);
        if(mValues.isEmpty()){
            Toast.makeText(parent.getContext(),"NO Message Found",Toast.LENGTH_LONG).show();
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getTime());
        holder.mContentView.setText(mValues.get(position).getMessage());



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
        public final TextView mIdView;
        public final TextView mContentView;
        public sendingmessage mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.message_show);
            mContentView = (TextView) view.findViewById(R.id.message_showtime);
        }


}
}
