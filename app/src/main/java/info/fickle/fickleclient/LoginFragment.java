package info.fickle.fickleclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Bharath on 05/11/16.
 */

public class LoginFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String PREFS = "MY_PREF";
    public static final String EMAIL = "email";
    public static final String PHNO = "phno";
    public static final String Full = "FULLN";
    private int mPageNo;
    private EditText fullname,email,phno;
    private TextInputLayout inp_email,inp_full,inp_phno;
    Button button;
    public static LoginFragment newInstance(int pageNo) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNo = getArguments().getInt(ARG_PAGE);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_login, container, false);
        SharedPreferences sharedPref = getActivity().getSharedPreferences(PREFS,Context.MODE_PRIVATE);
        String fn = sharedPref.getString(Full,null);
        String pn = sharedPref.getString(PHNO,null);
        String ei = sharedPref.getString(EMAIL,null);
        fullname = (EditText) view.findViewById(R.id.fullname);
        email = (EditText) view.findViewById(R.id.emailid);
        phno = (EditText) view.findViewById(R.id.phno);
        inp_email =(TextInputLayout) view.findViewById(R.id.input_email);
        inp_phno =(TextInputLayout) view.findViewById(R.id.input_phno);
        inp_full =(TextInputLayout) view.findViewById(R.id.input_fullname);
        fullname.setText(fn);
        fullname.setFocusable(false);
        fullname.setEnabled(false);
        fullname.setCursorVisible(false);
        fullname.setBackgroundColor(Color.TRANSPARENT);
        email.setText(ei);
        email.setFocusable(false);
        email.setEnabled(false);
        email.setCursorVisible(false);
        email.setKeyListener(null);
        email.setBackgroundColor(Color.TRANSPARENT);
        phno.setText(pn);
        phno.setFocusable(false);
        phno.setEnabled(false);
        phno.setCursorVisible(false);
        phno.setKeyListener(null);
        phno.setBackgroundColor(Color.TRANSPARENT);
        button = (Button) view.findViewById(R.id.LoginBut);
        button.setText("Update");
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                fullname.setFocusable(true);
                fullname.setEnabled(true);
                fullname.setCursorVisible(true);
            }
        });
        return view;
    }
    void disable(){

    }
}
