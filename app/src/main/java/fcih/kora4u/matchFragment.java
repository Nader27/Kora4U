package fcih.kora4u;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.grantland.widget.AutofitTextView;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class matchFragment extends Fragment {

    private static final String ARG_Tournament = "Tournament_ID";
    private String Tournament_ID = "";
    private OnListFragmentInteractionListener mListener;
    private FirebaseRecyclerAdapter<FireBaseHelper.Matches, MatcheHolder> mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public matchFragment() {
    }

    public static matchFragment newInstance(String Tournament) {
        matchFragment fragment = new matchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_Tournament, Tournament);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Tournament_ID = getArguments().getString(ARG_Tournament);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            final Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            Query ref = FireBaseHelper.Matches.Ref.orderByChild("tournament").equalTo(Tournament_ID);
            mAdapter = new FirebaseRecyclerAdapter<FireBaseHelper.Matches, MatcheHolder>(
                    FireBaseHelper.Matches.class, R.layout.fragment_match, MatcheHolder.class, ref) {
                @Override
                protected void populateViewHolder(final MatcheHolder viewHolder, final FireBaseHelper.Matches model, int position) {
                    FireBaseHelper.Teams Team = new FireBaseHelper.Teams();
                    Team.Findbykey(model.hometeam, new FireBaseHelper.OnGetDataListener<FireBaseHelper.Teams>() {
                        @Override
                        public void onSuccess(FireBaseHelper.Teams Data) {
                            viewHolder.mHomeTitle.setText(Data.name);
                            Picasso.with(context).load(Data.image).resize(100, 100).into(viewHolder.mHomeImage);
                        }
                    });
                    Team.Findbykey(model.awayteam, new FireBaseHelper.OnGetDataListener<FireBaseHelper.Teams>() {
                        @Override
                        public void onSuccess(FireBaseHelper.Teams Data) {
                            viewHolder.mAwayTitle.setText(Data.name);
                            Picasso.with(context).load(Data.image).resize(100, 100).into(viewHolder.mAwayImage);
                        }
                    });
                    viewHolder.mDate.setText(model.date);
                    SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                    SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                    SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                    DateFormat format2 = new SimpleDateFormat("EE", Locale.ENGLISH);
                    Date _24HourDt = null;
                    Date dt1 = null;
                    try {
                        _24HourDt = _24HourSDF.parse(model.time);
                        dt1 = format1.parse(model.date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String Day = format2.format(dt1);
                    viewHolder.mTime.setText(Day + " " + _12HourSDF.format(_24HourDt));
                    viewHolder.mResult.setText(model.result);
                }
            };
            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(FireBaseHelper.Matches item);
    }

    public static class MatcheHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public AutofitTextView mHomeTitle;
        public AutofitTextView mAwayTitle;
        public ImageView mHomeImage;
        public ImageView mAwayImage;
        public AutofitTextView mDate;
        public AutofitTextView mResult;
        public AutofitTextView mTime;

        public MatcheHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mHomeTitle = (AutofitTextView) mView.findViewById(R.id.home_team_title);
            mAwayTitle = (AutofitTextView) mView.findViewById(R.id.away_team_title);
            mHomeImage = (ImageView) mView.findViewById(R.id.home_team_image);
            mAwayImage = (ImageView) mView.findViewById(R.id.away_team_image);
            mDate = (AutofitTextView) mView.findViewById(R.id.Date_Text);
            mResult = (AutofitTextView) mView.findViewById(R.id.Result_text);
            mTime = (AutofitTextView) mView.findViewById(R.id.time_text);
        }
    }
}
