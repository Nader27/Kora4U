package fcih.kora4u;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TeamsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TeamsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int RESULT_LOAD_IMAGE = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private StorageReference mStorageRef;
    private OnFragmentInteractionListener mListener;
    private AlertDialog dialog;
    private EditText mimage;
    private FirebaseListAdapter<FireBaseHelper.Teams> mAdapter = null;

    public TeamsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TeamsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TeamsFragment newInstance(String param1, String param2) {
        TeamsFragment fragment = new TeamsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Uri file = Uri.parse(picturePath);
            StorageReference riversRef = mStorageRef.child("TeamImage/" + file.getLastPathSegment());
            UploadTask uploadTask = riversRef.putFile(file);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    mimage.setText(downloadUrl.toString());

                }
            });

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_teams, container, false);
        Button Add = (Button) view.findViewById(R.id.Add_button);
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog(null);
            }
        });
        ListView List = (ListView) view.findViewById(R.id.Main_List);
        mAdapter = new FirebaseListAdapter<FireBaseHelper.Teams>(
                getActivity(), FireBaseHelper.Teams.class, android.R.layout.activity_list_item, FireBaseHelper.Teams.Ref) {
            @Override
            protected void populateView(View v, FireBaseHelper.Teams model, int position) {
                TextView text1 = (TextView) v.findViewById(android.R.id.text1);
                ImageView icon = (ImageView) v.findViewById(android.R.id.icon);
                text1.setText(model.name);
                Picasso.with(getContext()).load(model.image).into(icon);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        model.Findbykey(mAdapter.getRef(position).getKey(), new FireBaseHelper.OnGetDataListener<FireBaseHelper.Teams>() {
                            @Override
                            public void onSuccess(FireBaseHelper.Teams Data) {
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

    private void ShowDialog(FireBaseHelper.Teams updateteam) {
        AlertDialog.Builder mEBuilder = new AlertDialog.Builder(getActivity());
        View mEView = getActivity().getLayoutInflater().inflate(R.layout.addteam, null);
        final EditText mname = (EditText) mEView.findViewById(R.id.name);
        mimage = (EditText) mEView.findViewById(R.id.image);
        Button browse = (Button) mEView.findViewById(R.id.browse_button);
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        Button mbutton = (Button) mEView.findViewById(R.id.Add);
        Button mdelbutton = (Button) mEView.findViewById(R.id.Delete);
        mdelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireBaseHelper.Matches m = new FireBaseHelper.Matches();
                m.Where(FireBaseHelper.Matches.Table.Hometeam, updateteam.Key, new FireBaseHelper.OnGetDataListListener<FireBaseHelper.Matches>() {
                    @Override
                    public void onSuccess(List<FireBaseHelper.Matches> Data) {
                        for (FireBaseHelper.Matches d : Data) {
                            m.Remove(d.Key);
                        }
                        m.Where(FireBaseHelper.Matches.Table.Awayteam, updateteam.Key, new FireBaseHelper.OnGetDataListListener<FireBaseHelper.Matches>() {
                            @Override
                            public void onSuccess(List<FireBaseHelper.Matches> Data) {
                                for (FireBaseHelper.Matches d : Data) {
                                    m.Remove(d.Key);
                                }
                                updateteam.Remove(updateteam.Key);
                                dialog.dismiss();
                            }
                        });
                    }
                });
            }
        });
        if (updateteam != null) {
            mname.setText(updateteam.name);
            mimage.setText(updateteam.image);
            mbutton.setText("Update Team");
            mdelbutton.setVisibility(View.VISIBLE);
        }
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mname.getText().toString().isEmpty()) {
                    mname.setError("name Field is Empty");
                    mname.requestFocus();
                } else if (mimage.getText().toString().isEmpty()) {
                    mimage.setError("image Field is Empty");
                    mimage.requestFocus();
                } else {
                    FireBaseHelper.Teams team = new FireBaseHelper.Teams();
                    team.name = mname.getText().toString();
                    team.image = mimage.getText().toString();
                    if (updateteam != null)
                        team.Update(updateteam.Key);
                    else
                        team.Add();
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
