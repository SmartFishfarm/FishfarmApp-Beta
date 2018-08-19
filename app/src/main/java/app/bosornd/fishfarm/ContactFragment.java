package app.bosornd.fishfarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by jun on 17. 9. 29.
 */

public class ContactFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private TextView text_view_for_normal_selection;

    private static final String TAG = "1";

    public ContactFragment() {
        // Required empty public constructor
    }

/*
    public static ContactFragment newInstance(String normalText) {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        args.putString(Constants.FRAG_C, normalText);
        fragment.setArguments(args);
        return fragment;
    }
*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_contact, container, false);
        text_view_for_normal_selection=(TextView) view.findViewById(R.id.text_view_for_normal_selection);
        text_view_for_normal_selection.setText(getArguments().getString(Constants.FRAG_D));


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Email = new Intent(Intent.ACTION_SEND);
                Email.setType("text/email");
                Email.putExtra(Intent.EXTRA_EMAIL,
                        new String[]{"junwoo.park@bosornd.com"});  //developer 's email
                Email.putExtra(Intent.EXTRA_SUBJECT,
                        "주제"); // Email 's Subject
                Email.putExtra(Intent.EXTRA_TEXT, "문의, 요구, 건의사항 작성해주세요." + "");  //Email 's Greeting text
                startActivity(Intent.createChooser(Email, "Send Feedback:"));
            }
        });
        return view;
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


    @Override
    public void onResume() {
        super.onResume();
    }
}
