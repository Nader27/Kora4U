package fcih.kora4u;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity implements matchFragment.OnListFragmentInteractionListener {

    private static final String TAG = "MainActivity";
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private List<FireBaseHelper.Tournaments> Tournaments;
    private AlertDialog dialog;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        //FireBaseHelper.Teams t = new FireBaseHelper.Teams();
        //t.name = "Barcelona";
        //t.image = "http://2.bp.blogspot.com/-AjfAudNVzaY/U_aAa1P_7-I/AAAAAAAADtY/dp4aPJOSTBA/s1600/Logo%2BBarcelona%2BFC.png";
        //String k1 = t.Add();
        //t.name = "Real Madrid";
        //t.image = "https://i.imgsafe.org/0309de5e55.png";
        //String k2 = t.Add();
//        FireBaseHelper.Tournaments tt = new FireBaseHelper.Tournaments();
//        tt.name = "Egyptian League";
//        String kk1 = tt.Add();
//        tt.name = "Egypt Cup";
//        String kk2 = tt.Add();
        /*FireBaseHelper.Matches mm = new FireBaseHelper.Matches();
        mm.hometeam = "-KjOBSW3tqRr-hZyvGfg";
        mm.awayteam = "-KjOBSWREo7TaM8e5Haw";
        mm.date = "20/5/2017";
        mm.result = "3 : 2";
        mm.time = "17:30";
        mm.tournament = "-KjNcv59KMcM6OjB3hHl";
        mm.Add();*/


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FireBaseHelper.Tournaments Tournament = new FireBaseHelper.Tournaments();
        Tournament.Tolist(new FireBaseHelper.OnGetDataListListener<FireBaseHelper.Tournaments>() {
            @Override
            public void onSuccess(List<FireBaseHelper.Tournaments> Data) {
                Tournaments = Data;
                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

                // Set up the ViewPager with the sections adapter.
                mViewPager = (ViewPager) findViewById(R.id.container);
                mViewPager.setAdapter(mSectionsPagerAdapter);

                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                tabLayout.setupWithViewPager(mViewPager);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_admin) {
            //region email form
            if (mAuth.getCurrentUser() != null) {
                startActivity(new Intent(MainActivity.this, AdminActivity.class));
            } else {
                AlertDialog.Builder mEBuilder = new AlertDialog.Builder(MainActivity.this);
                View mEView = getLayoutInflater().inflate(R.layout.loginform, null);
                mEBuilder.setView(mEView);
                dialog = mEBuilder.create();
                Button mEButton = (Button) mEView.findViewById(R.id.login);
                mEButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText mEmail = (EditText) dialog.findViewById(R.id.email);
                        EditText mPassword = (EditText) dialog.findViewById(R.id.password);
                        if (!mEmail.getText().toString().isEmpty() && !mPassword.getText().toString().isEmpty()) {
                            if (android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail.getText().toString()).matches()) {
                                if (mPassword.getText().length() >= 6) {
                                    mAuth.signInWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                                                        startActivity(new Intent(MainActivity.this, AdminActivity.class));
                                                        // If sign in fails, display a message to the user. If sign in succeeds
                                                        // the auth state listener will be notified and logic to handle the
                                                        // signed in user can be handled in the listener.
                                                    } else if (!task.isSuccessful()) {
                                                        Log.w(TAG, "signInWithEmail:failed", task.getException());
                                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(),
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else
                                {
                                    mPassword.setError("Password is too Short");
                                    mPassword.requestFocus();
                                }
                            } else {
                                mEmail.setError("Email is Wrong");
                                mEmail.requestFocus();
                            }
                        } else
                            Toast.makeText(getApplicationContext(),
                                    "Fill Empty Fields",
                                    Toast.LENGTH_LONG).show();

                    }
                });
                dialog.show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(FireBaseHelper.Matches item) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return matchFragment.newInstance(Tournaments.get(position).Key);
        }

        @Override
        public int getCount() {
            return Tournaments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Tournaments.get(position).name;
        }
    }
}
