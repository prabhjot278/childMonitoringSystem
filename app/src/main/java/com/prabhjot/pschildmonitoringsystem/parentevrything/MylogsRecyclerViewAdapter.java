package com.prabhjot.pschildmonitoringsystem.parentevrything;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prabhjot.pschildmonitoringsystem.R;
import com.prabhjot.pschildmonitoringsystem.parentevrything.logsFragment.OnListFragmentInteractionListener;


import org.w3c.dom.Text;
import com.prabhjot.pschildmonitoringsystem.childeverything.*;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MylogsRecyclerViewAdapter extends RecyclerView.Adapter<MylogsRecyclerViewAdapter.ViewHolderlogs> {

    private final List<logshelper> mValues;
    private final OnListFragmentInteractionListener mListener;
    private String reference;   //reference for the database

    public MylogsRecyclerViewAdapter(List<logshelper> items,String reference, OnListFragmentInteractionListener listener) {
        mValues = items;
        this.reference=reference;
        mListener = listener;
    }

    @Override
    public ViewHolderlogs onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_logs, parent, false);
        return new ViewHolderlogs(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolderlogs holder, int position) {
        holder.mItem = mValues.get(position);
        holder.caller_name.setText(mValues.get(position).callname);
        holder.caller_directory.setText(mValues.get(position).calldir);
        holder.caller_durtion.setText(mValues.get(position).callduration);
        holder.caller_date.setText(mValues.get(position).calldate);
        holder.caller_num.setText(mValues.get(position).phnumber);

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

    public class ViewHolderlogs extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView caller_name;
        public final TextView caller_num;
        public final TextView caller_durtion;
        public final TextView caller_date;
        public final TextView caller_directory;
        public logshelper mItem;

        public ViewHolderlogs(View view) {
            super(view);
            mView = view;
            caller_name = (TextView) view.findViewById(R.id.caller_name);
            caller_num = (TextView) view.findViewById(R.id.caller_num);
            caller_durtion=(TextView)view.findViewById(R.id.caller_duration);
            caller_date=(TextView)view.findViewById(R.id.caller_date);
            caller_directory=(TextView)view.findViewById(R.id.caller_directory);
        }

    }
}
