package com.prabhjot.pschildmonitoringsystem.parentevrything;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prabhjot.pschildmonitoringsystem.R;
import com.prabhjot.pschildmonitoringsystem.parentevrything.childFragment.OnListFragmentInteractionListener;
import com.prabhjot.pschildmonitoringsystem.*;

import java.util.Collections;
import java.util.List;


/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MychildRecyclerViewAdapter extends RecyclerView.Adapter<examViewHolder>  {

    List<childhelper> list = Collections.EMPTY_LIST;
    Context context;
    String page;
    private static ProgressHUD a;
    public static final String Valuename="valuename";
    public static final String Valuekey="valuekey";
    public static final String ValuePage="valuePage";

    public MychildRecyclerViewAdapter(List<childhelper> list,String gotit, Context context)
    {
        this.list = list;
        this.page=gotit;
        this.context = context;
    }

    @Override
    public examViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the layout

        View photoView = inflater.inflate(R.layout.fragment_child,
                viewGroup, false);

        examViewHolder viewHolder = new examViewHolder(photoView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final examViewHolder viewHolder, final int position) {

        viewHolder.Name.setText(list.get(position).username);
        viewHolder.age.setText(list.get(position).age+" yrs");
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    viewHolder.linearLayout.setBackgroundColor(R.color.maincolor);
                    //a = ProgressHUD.show(context, "please wait", true, false, null);
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment myFragment = new loadingFragment();
                    Bundle xx=new Bundle();
                    xx.putString(Valuename,list.get(position).getUsername());
                    xx.putString(Valuekey,list.get(position).getKey());
                    xx.putString(ValuePage,page);
                    myFragment.setArguments(xx);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.map2, myFragment).commit();



            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
class examViewHolder extends RecyclerView.ViewHolder {
    TextView Name;
    TextView age;
    LinearLayout linearLayout;

    examViewHolder(View itemView)
    {
        super(itemView);
        Name = (TextView)itemView.findViewById(R.id.item_number);
        age = (TextView)itemView.findViewById(R.id.content);
        linearLayout=(LinearLayout)itemView.findViewById(R.id.listofchild);
    }
}
