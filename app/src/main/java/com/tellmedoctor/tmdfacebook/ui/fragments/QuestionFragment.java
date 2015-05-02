package com.tellmedoctor.tmdfacebook.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.tellmedoctor.tmdfacebook.R;
import com.tellmedoctor.tmdfacebook.adapters.QuestionAdapter;
import com.tellmedoctor.tmdfacebook.model.questionItem;
import com.tellmedoctor.tmdfacebook.ui.activities.MainActivity;
import com.tellmedoctor.tmdfacebook.utils.PrefsUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.gson.Gson;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Activity _context;
    private List<questionItem> questions_list;
    private View rootView;
    private RecyclerView recList;
    private int currPosition;
    private QuestionAdapter adapter;
    private ImageButton fab_question;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionFragment newInstance(String param1, String param2) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public QuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        _context = getActivity();
        questions_list = PrefsUtils.getQuestions(_context);
        if (PrefsUtils.getQuestions(_context) == null) {

            //initialize
            questionItem[] targetRangeItem = new questionItem[]{
                    new questionItem("1", "Question one ", "1"),
                    new questionItem("2", "Question two ", "70"),
                    new questionItem("3", "Question threee ring", "3"),
                    new questionItem("4", "Quesion four", "6")
            };

            // was there a previously save target range set?
            List<questionItem> pref_t = PrefsUtils.getQuestions(_context);

            if (pref_t == null) {
                Gson gson = new Gson();
                String json = gson.toJson(targetRangeItem);
                PrefsUtils.setQuestions(_context, json);
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_question, container, false);
        recList = (RecyclerView) rootView.findViewById(R.id.qcardlist);

        fab_question = (ImageButton) rootView.findViewById(R.id.fab_question);
        fab_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager f = getActivity().getFragmentManager();
                FragmentTransaction ft = f.beginTransaction();


                 /*   AskAQuestionFragment x = AskAQuestionFragment.newInstance("AskAQuestionFragment ", "Main");
                    int targetCode = 1;
                    x.setTargetFragment(QuestionFragment.this, targetCode);
                    ft = ft.replace(R.id.container, x);
                    //ft.addToBackStack(this.position);

                    ft.commit();//AllowingStateLoss();*/

                Intent returnIntent = new Intent(getActivity(), MainActivity.class);
                returnIntent.putExtra("fragment", "AskQuestion");
                //getActivity().setResult(getActivity().RESULT_OK, returnIntent);
                startActivity(returnIntent);
                getActivity().finish();


            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (questions_list != null) {
            initAdapter(view);
        }
    }

    private void initAdapter(View view) {

        adapter = new QuestionAdapter(questions_list,
                R.layout.fragment_question_item, _context, view);

        recList.setHasFixedSize(true);
        recList.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(_context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setItemAnimator(new DefaultItemAnimator());
        recList.setLayoutManager(llm);

        adapter.SetOnQuestionItemClickListener(new QuestionAdapter.OnQuestionItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                currPosition = position;

                // capture the id of the row selected
                long dataEntryId = adapter.getItemId(position);

                /*Intent dataEntryIntent = new Intent(getActivity(), ReportsActivity.class);
                String keyIdentifier_data_entry = "dataEntryFragment";
                dataEntryIntent.putExtra("dataEntryItem", dataEntryId + "");
                dataEntryIntent.putExtra("fragment", keyIdentifier_data_entry);
                startActivity(dataEntryIntent);*/
            }
        });
        adapter.notifyDataSetChanged();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();
        initAdapter(this.rootView);
    }
}
