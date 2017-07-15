package fcih.kora4u;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MatchesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MatchesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MatchesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private AlertDialog dialog;
    private List<FireBaseHelper.Teams> Teams;
    private List<FireBaseHelper.Tournaments> Tournaments;
    private FirebaseListAdapter<FireBaseHelper.Matches> mAdapter;

    public MatchesFragment() {
        // Required empty public constructor
        mAdapter = null;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MatchesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MatchesFragment newInstance(String param1, String param2) {
        MatchesFragment fragment = new MatchesFragment();
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

    private boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_matches, container, false);
        Button Add = (Button) view.findViewById(R.id.Add_button);
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog(null);
            }
        });
        ListView List = (ListView) view.findViewById(R.id.Main_List);
        mAdapter = new FirebaseListAdapter<FireBaseHelper.Matches>(
                getActivity(), FireBaseHelper.Matches.class, android.R.layout.simple_list_item_2, FireBaseHelper.Matches.Ref) {
            @Override
            protected void populateView(View v, FireBaseHelper.Matches model, int position) {
                TextView text1 = (TextView) v.findViewById(android.R.id.text1);
                TextView text2 = (TextView) v.findViewById(android.R.id.text2);
                FireBaseHelper.Teams teams = new FireBaseHelper.Teams();
                model.Findbykey(mAdapter.getRef(position).getKey(), new FireBaseHelper.OnGetDataListener<FireBaseHelper.Matches>() {
                    @Override
                    public void onSuccess(FireBaseHelper.Matches Data) {
                        text1.setText(Data.Hometeams.name + " VS " + Data.Awayteams.name);
                        text2.setText(model.date + " — " + Data.Tournaments.name);
                    }
                });

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        model.Findbykey(mAdapter.getRef(position).getKey(), new FireBaseHelper.OnGetDataListener<FireBaseHelper.Matches>() {
                            @Override
                            public void onSuccess(FireBaseHelper.Matches Data) {
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

    private void ShowDialog(FireBaseHelper.Matches updatematch) {

        final List<String> Teamlst = new ArrayList<>();
        final List<String> Tourlst = new ArrayList<>();
        AlertDialog.Builder mEBuilder = new AlertDialog.Builder(getActivity());
        View mEView = getActivity().getLayoutInflater().inflate(R.layout.addmatch, null);
        final Spinner hometeam = (Spinner) mEView.findViewById(R.id.home_team);
        final Spinner awayteam = (Spinner) mEView.findViewById(R.id.away_team);
        FireBaseHelper.Teams teams = new FireBaseHelper.Teams();
        teams.Tolist(new FireBaseHelper.OnGetDataListListener<FireBaseHelper.Teams>() {
            @Override
            public void onSuccess(List<FireBaseHelper.Teams> Data) {
                List<String> lst = new ArrayList<>();

                for (FireBaseHelper.Teams TEAM : Data) {
                    Teams = Data;
                    lst.add(TEAM.name);
                    Teamlst.add(TEAM.Key);
                }
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, lst);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                hometeam.setAdapter(spinnerArrayAdapter);
                awayteam.setAdapter(spinnerArrayAdapter);
                if (updatematch != null) {
                    hometeam.setSelection(Teamlst.indexOf(updatematch.hometeam));
                    awayteam.setSelection(Teamlst.indexOf(updatematch.awayteam));
                }
            }
        });
        final Spinner tournament = (Spinner) mEView.findViewById(R.id.tournament);
        FireBaseHelper.Tournaments tour = new FireBaseHelper.Tournaments();
        tour.Tolist(new FireBaseHelper.OnGetDataListListener<FireBaseHelper.Tournaments>() {
            @Override
            public void onSuccess(List<FireBaseHelper.Tournaments> Data) {
                List<String> lst = new ArrayList<>();
                for (FireBaseHelper.Tournaments TO : Data) {
                    Tournaments = Data;
                    lst.add(TO.name);
                    Tourlst.add(TO.Key);
                }
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, lst);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                tournament.setAdapter(spinnerArrayAdapter);
                if (updatematch != null) {
                    tournament.setSelection(Tourlst.indexOf(updatematch.tournament));
                }
            }
        });
        final EditText home_result = (EditText) mEView.findViewById(R.id.home_result);
        final EditText away_result = (EditText) mEView.findViewById(R.id.away_result);
        final TextView msettime = (TextView) mEView.findViewById(R.id.Time_picker);
        msettime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        msettime.setText(String.format(Locale.ENGLISH, "%02d", selectedHour) + ":" + String.format(Locale.ENGLISH, "%02d", selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        final TextView msetDate = (TextView) mEView.findViewById(R.id.Date_picker);
        msetDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int year = mcurrentTime.get(Calendar.YEAR);
                int month = mcurrentTime.get(Calendar.MONTH);
                int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        msetDate.setText(dayOfMonth + "/" + month + "/" + year);
                    }
                }, year, month, day);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();

            }
        });
        home_result.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText mtext = (EditText) v;
                String txt = mtext.getText().toString();
                if (hasFocus) {
                    if (txt.equals("—"))
                        mtext.setText("");
                } else {
                    if (txt.isEmpty())
                        mtext.setText("—");
                }
            }
        });
        away_result.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText mtext = (EditText) v;
                String txt = mtext.getText().toString();
                if (hasFocus) {
                    if (txt.equals("—"))
                        mtext.setText("");
                } else {
                    if (txt.isEmpty())
                        mtext.setText("—");
                }
            }
        });
        Button mbutton = (Button) mEView.findViewById(R.id.Add);
        Button mdelbutton = (Button) mEView.findViewById(R.id.Delete);
        mdelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatematch.Remove(updatematch.Key);
                dialog.dismiss();
            }
        });
        if (updatematch != null) {
            home_result.setText(updatematch.result.split(" : ")[0]);
            away_result.setText(updatematch.result.split(" : ")[1]);
            msettime.setText(updatematch.time);
            msetDate.setText(updatematch.date);
            mbutton.setText("Update Match");
            mdelbutton.setVisibility(View.VISIBLE);
        }
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (home_result.getText().toString().isEmpty()) {
                    home_result.setText("—");
                }
                if (away_result.getText().toString().isEmpty()) {
                    away_result.setText("—");
                }
                if (home_result.getText().toString().equals("—") && !away_result.getText().toString().equals("—")) {
                    home_result.setError("Enter Result");
                    home_result.setText("");
                    home_result.requestFocus();
                } else if (away_result.getText().toString().equals("—") && !home_result.getText().toString().equals("—")) {
                    away_result.setError("Enter Result");
                    away_result.setText("");
                    away_result.requestFocus();
                } else if (hometeam.getSelectedItemId() == awayteam.getSelectedItemId()) {
                    Toast.makeText(getContext(), "Teams Must be Different", Toast.LENGTH_LONG).show();
                    awayteam.requestFocus();
                } else if (msetDate.getText().toString().equals("Select Date")) {
                    msetDate.setError("Enter Date");
                    msetDate.requestFocus();
                } else if (msettime.getText().toString().equals("Select Time")) {
                    msetDate.setError("Enter Time");
                    msetDate.requestFocus();
                } else {
                    FireBaseHelper.Matches Match = new FireBaseHelper.Matches();
                    Match.result = home_result.getText().toString() + " : " + away_result.getText().toString();
                    Match.tournament = Tourlst.get((int) tournament.getSelectedItemId());
                    Match.hometeam = Teamlst.get((int) hometeam.getSelectedItemId());
                    Match.awayteam = Teamlst.get((int) awayteam.getSelectedItemId());
                    Match.date = msetDate.getText().toString();
                    Match.time = msettime.getText().toString();
                    Match.result = home_result.getText().toString() + " : " + away_result.getText().toString();
                    if (updatematch != null)
                        Match.Update(updatematch.Key);
                    else
                        Match.Add();
                    dialog.dismiss();
                }
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
