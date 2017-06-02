package nl.rug.www.rugsummerschool.controller.forum;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import nl.rug.www.rugsummerschool.R;
import nl.rug.www.rugsummerschool.controller.myprofile.SignInFragment;

/**
 * Created by RavenSP on 30/5/2017.
 */

public class ForumLoginFragment extends Fragment{
    protected AppCompatActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum_login, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    FragmentManager fm = mActivity.getSupportFragmentManager();
                    if(!mActivity.isFinishing())
                    fm.beginTransaction().replace(R.id.fragment_forum_container, new ForumFragment()).commitAllowingStateLoss();
                }
            }
        });
        if (user != null) {
            FragmentManager fm = mActivity.getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.fragment_forum_container, new ForumFragment()).commit();
        }
        return view;
    }
}