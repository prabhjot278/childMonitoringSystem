package com.prabhjot.pschildmonitoringsystem.parentevrything;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prabhjot.pschildmonitoringsystem.R;
import com.prabhjot.pschildmonitoringsystem.basicmethods.networkinfo;
import com.prabhjot.pschildmonitoringsystem.basicmethods.sendingmessage;
import com.prabhjot.pschildmonitoringsystem.welcome1;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link sendmessageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link sendmessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class sendmessageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseAuth mAuth;
    FirebaseUser user;
    String reference;
    DatabaseReference ref;
    EditText msg;
    Button b;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    boolean networkstate;

    private OnFragmentInteractionListener mListener;

    public sendmessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment sendmessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static sendmessageFragment newInstance(String param1, String param2) {
        sendmessageFragment fragment = new sendmessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth= FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        reference="Message/"+user.getUid();
        ref= FirebaseDatabase.getInstance().getReference(reference);

        //msg=(EditText)getActivity().findViewById(R.id.message_enter);
        //b=(Button)getActivity().findViewById(R.id.message_send);



        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_sendmessage, container, false);
        msg=(EditText)layout.findViewById(R.id.message_enter);
        b=(Button) layout.findViewById(R.id.message_send);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=msg.getText().toString().trim();
                if(!TextUtils.isEmpty(message)){
                    msg.getText().clear();
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(msg.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    networkinfo c=new networkinfo(getContext());
                    final boolean networkstate=c.isNetworkAvailable();
                    String timeStamp = new SimpleDateFormat("dd/MM/yyyy  h:mm aaa").format(Calendar.getInstance().getTime());
                    if(networkstate){
                        String mchildId = ref.push().getKey();
                        sendingmessage cemp=new sendingmessage(message,timeStamp);
                        ref.child(mchildId).setValue(cemp);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Message");
                        builder.setMessage("Message delivered");
                        builder.setCancelable(true);

                        final AlertDialog closedialog= builder.create();

                        closedialog.show();
                        final Timer timer2 = new Timer();
                        timer2.schedule(new TimerTask() {
                            public void run() {
                                closedialog.dismiss();
                                timer2.cancel(); //this will cancel the timer of the system
                            }
                        }, 2000);
                    }else{
                        Toast.makeText(getContext(),"message not delivered",Toast.LENGTH_LONG).show();
                        msg.setError("Internet not available");
                    }

                }else{
                   msg.setError("Enter some message");
                }

            }
        });
        return layout;
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
