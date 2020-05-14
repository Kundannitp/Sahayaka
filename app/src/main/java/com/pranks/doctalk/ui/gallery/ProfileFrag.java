package com.pranks.doctalk.ui.gallery;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.pranks.doctalk.Home;
import com.pranks.doctalk.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFrag extends Fragment {

    View v;
    DatabaseReference mrefrence;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile, container, false);
        ((Home) getActivity()).getSupportActionBar().setTitle("Profile");

        mrefrence= FirebaseDatabase.getInstance().getReference();
        ImageView personimg=v.findViewById(R.id.person_pic);
        TextView personname=v.findViewById(R.id.person_name);
        TextView persongen=v.findViewById(R.id.person_gender);
        TextView personmail=v.findViewById(R.id.person_mail);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String gender="";
            Uri personPhoto = acct.getPhotoUrl();
            personmail.setText(personEmail);
            personname.setText(personName);

            String personPhotostr="https://firebasestorage.googleapis.com/v0/b/doctalk-80d98.appspot.com/o/man.jpg?alt=media&token=52c8c697-f8ce-4a09-bbb1-7f7adcda4fa2";
            if(personPhoto!=null){
                personPhotostr=personPhoto.toString();
            }

            Glide.with(this)
                    .load(personPhotostr)
                    .into(personimg);
        }
        return v;
    }
}