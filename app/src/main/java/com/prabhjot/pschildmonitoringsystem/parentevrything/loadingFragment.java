package com.prabhjot.pschildmonitoringsystem.parentevrything;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prabhjot.pschildmonitoringsystem.ProgressHUD;
import com.prabhjot.pschildmonitoringsystem.R;
import com.prabhjot.pschildmonitoringsystem.basicmethods.usageHelper;
import com.prabhjot.pschildmonitoringsystem.childeverything.logshelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link loadingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link loadingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class loadingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String Valuename="valuename";
    public static final String Valuekey="valuekey";
    public static final String ValuePage="valuePage";
    public String reference,reference2;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public String name,key,page;
    private OnFragmentInteractionListener mListener;
    FirebaseAuth mAuth;
    FirebaseUser user;
    private static ProgressHUD a;
    public static List<logshelper> ITEMS=new ArrayList<>();
    public static List<usageHelper> ITEMS_usagedaily=new ArrayList<>();
    public static List<usageHelper> ITEMS_usageweekly=new ArrayList<>();
    DatabaseReference mref,mref2;

    public loadingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment loadingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static loadingFragment newInstance(String param1, String param2) {
        loadingFragment fragment = new loadingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ITEMS.clear();
        ITEMS_usagedaily.clear();
        ITEMS_usageweekly.clear();
        name= getArguments().getString(Valuename);
        key= getArguments().getString(Valuekey);
        page=getArguments().getString(ValuePage);
        //Log.i("yoyoyo",name+" / "+key+" / "+page);
        mAuth= FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        String uid=user.getUid();
        if(page.equals("calls")) {
            reference = "User/" + uid + "/child/" + key + "/data/call_logs";
            mref= FirebaseDatabase.getInstance().getReference(reference);
        }else{
            reference="User/"+ uid + "/child/" + key + "/data/app_usage/Daily";
            reference2="User/"+ uid + "/child/" + key + "/data/app_usage/Weekly";
            mref= FirebaseDatabase.getInstance().getReference(reference);
            mref2= FirebaseDatabase.getInstance().getReference(reference2);
        }
        if(page.equals("calls")){
            AsyncTasklogs asyncTasklogs=new AsyncTasklogs();
            asyncTasklogs.execute();
        }else{
            AsyncTaskusage asyncTaskusage=new AsyncTaskusage();
            asyncTaskusage.execute();
        }

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public class AsyncTasklogs extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            a = ProgressHUD.show(getActivity(), "please wait!!", true, false, null);


        }
        @Override
        protected String doInBackground(String... strings) {

            mref.orderByKey().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){

                        logshelper post=ds.getValue(logshelper.class);
                        ITEMS.add(post);
                    }

                    Collections.reverse(ITEMS);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            try{Thread.sleep(2000);}catch (Exception e){ }
            Log.i("yupyup","you r in bckground");
            return "OK";
        }

        @Override
        protected void onPostExecute(String nowfter) {
            super.onPostExecute(nowfter);
            if(nowfter.equals("OK")){
                if(a.isShowing()){
                    a.cancel();
                    Log.i("yupyup","you r in postview");
                    Bundle xxxxx=new Bundle();
                    xxxxx.putString("kiddo",name);
                    Fragment myFragment = new logsFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map2, myFragment).commit();
                }
            }else{
                Log.i("yupyup","you r in postview else");
            }
        }
    }
    public class AsyncTaskusage extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            a = ProgressHUD.show(getActivity(), "please wait!!", true, false, null);


        }
        @Override
        protected String doInBackground(String... strings) {

            mref.orderByKey().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){

                        usageHelper post=ds.getValue(usageHelper.class);
                        if(ITEMS_usagedaily.contains(ds.getValue(usageHelper.class))){

                            ds.getValue(usageHelper.class).getLast_time_used();
                            ITEMS_usagedaily.contains(ds.getValue(usageHelper.class));


                        }else{
                            ITEMS_usagedaily.add(post);
                        }

                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            mref2.orderByKey().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){

                        usageHelper post=ds.getValue(usageHelper.class);


                        ITEMS_usageweekly.add(post);
                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            try{Thread.sleep(2000);}catch (Exception e){ }
            Log.i("yupyup","you r in bckground");
            return "OK";
        }

        @Override
        protected void onPostExecute(String nowfter) {
            super.onPostExecute(nowfter);
            if(nowfter.equals("OK")){
                if(a.isShowing()){
                    a.cancel();
                    Log.i("yupyup","you r in postview");
                    Bundle xxxxx=new Bundle();
                    xxxxx.putString("kiddo",name);
                    Fragment myFragment = new usageFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map2, myFragment).commit();
                }
            }else{
                Log.i("yupyup","you r in postview else");
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    class StringDateComparator implements Comparator<String>
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        int x;
        public int compare(String lhs, String rhs)
        {
            try{
               x= dateFormat.parse(lhs).compareTo(dateFormat.parse(rhs));
            }catch (Exception e){

            }
            return x;
        }
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

