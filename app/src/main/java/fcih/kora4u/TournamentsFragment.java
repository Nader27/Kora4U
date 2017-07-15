package fcih.kora4u;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TournamentsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TournamentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TournamentsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private AlertDialog dialog;
    private FirebaseListAdapter<FireBaseHelper.Tournaments> mAdapter = null;

    public TournamentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TournamentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TournamentsFragment newInstance(String param1, String param2) {
        TournamentsFragment fragment = new TournamentsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tournaments, container, false);
        Button Add = (Button) view.findViewById(R.id.Add_button);
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog(null);
            }
        });
        ListView List = (ListView) view.findViewById(R.id.Main_List);
        mAdapter = new FirebaseListAdapter<FireBaseHelper.Tournaments>(
                getActivity(), FireBaseHelper.Tournaments.class, android.R.layout.simple_list_item_1, FireBaseHelper.Tournaments.Ref) {
            @Override
            protected void populateView(View v, FireBaseHelper.Tournaments model, int position) {
                TextView text1 = (TextView) v.findViewById(android.R.id.text1);
                text1.setText(model.name);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        model.Findbykey(mAdapter.getRef(position).getKey(), new FireBaseHelper.OnGetDataListener<FireBaseHelper.Tournaments>() {
                            @Override
                            public void onSuccess(FireBaseHelper.Tournaments Data) {
                                ShowDialog(Data);
                            }
                        });
                    }
                });
            }
        };
        List.setAdapter(mAdapter);
        return view;
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

    private void ShowDialog(FireBaseHelper.Tournaments updatetour) {
        AlertDialog.Builder mEBuilder = new AlertDialog.Builder(getActivity());
        View mEView = getActivity().getLayoutInflater().inflate(R.layout.addtournament, null);
        final EditText mname = (EditText) mEView.findViewById(R.id.name);
        Button mbutton = (Button) mEView.findViewById(R.id.Add);
        Button mdelbutton = (Button) mEView.findViewById(R.id.Delete);
        mdelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireBaseHelper.Matches m = new FireBaseHelper.Matches();
                m.Where(FireBaseHelper.Matches.Table.Tournament, updatetour.Key, new FireBaseHelper.OnGetDataListListener<FireBaseHelper.Matches>() {
                    @Override
                    public void onSuccess(List<FireBaseHelper.Matches> Data) {
                        for (FireBaseHelper.Matches d : Data) {
                            m.Remove(d.Key);
                        }
                        updatetour.Remove(updatetour.Key);
                        dialog.dismiss();
                    }
                });
            }
        });
        if (updatetour != null) {
            mname.setText(updatetour.name);
            mbutton.setText("Update Tournament");
            mdelbutton.setVisibility(View.VISIBLE);
        }
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireBaseHelper.Tournaments Tournament = new FireBaseHelper.Tournaments();
                Tournament.name = mname.getText().toString();
                if (updatetour != null)
                    Tournament.Update(updatetour.Key);
                else
                    Tournament.Add();
                dialog.dismiss();
            }
        });
        mEBuilder.setView(mEView);
        dialog = mEBuilder.create();
        dialog.show();
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
