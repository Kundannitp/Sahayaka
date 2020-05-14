package com.pranks.doctalk.BlogsAndHome;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pranks.doctalk.BlogsAndHome.Adapters.BlogAdapter;
import com.pranks.doctalk.BlogsAndHome.Adapters.BlogAdapterMyBlog;
import com.pranks.doctalk.BlogsAndHome.UploadingClass.BlogUploadClass;
import com.pranks.doctalk.R;

import java.util.ArrayList;
import java.util.Comparator;


public class MyBlogs extends Fragment {
    View v;

    DatabaseReference mrefrence;
    BlogAdapterMyBlog blogAdapter;

    RecyclerView mRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_my_blogs, container, false);

        mRecyclerView = v.findViewById(R.id.my_blog_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mrefrence= FirebaseDatabase.getInstance().getReference();
        try {
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
            mrefrence.child("PersonBlogs").child(acct.getId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        GoogleSignInAccount acct1 = GoogleSignIn.getLastSignedInAccount(getContext());
                        ArrayList<BlogUploadClass> blg = new ArrayList<>();
//                        ArrayList<String> uids = new ArrayList<>();
//                        ArrayList<String> key = new ArrayList<>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String name = acct1.getDisplayName();
                            Uri profilepic = acct1.getPhotoUrl();
                            String personPhotostr = "https://firebasestorage.googleapis.com/v0/b/doctalk-80d98.appspot.com/o/man.jpg?alt=media&token=52c8c697-f8ce-4a09-bbb1-7f7adcda4fa2";
                            if (profilepic != null) {
                                personPhotostr = profilepic.toString();
                            }
                            String uids=ds.getKey();
                            String blogmsg = ds.child("blogdata").getValue().toString();
                            String mail = acct1.getEmail();
                            String key=ds.child("key").getValue().toString();
                            int like = Integer.parseInt(ds.child("likes").getValue().toString());
                            long timestamp = Long.parseLong(ds.child("timestamp").getValue().toString());
                            if(ds.child("havetoshow").getValue().toString().equals("true")){
                                BlogUploadClass upl = new BlogUploadClass(personPhotostr, name, blogmsg, mail, like, timestamp,uids,key);
                                blg.add(upl);
                            }
                            }

//                        Collections.sort(blg,new Sortbyroll());

                        blogAdapter = new BlogAdapterMyBlog(getContext(), blg);
                        mRecyclerView.setAdapter(blogAdapter);
                        ProgressBar progressBar = v.findViewById(R.id.my_progress_circle);
                        progressBar.setVisibility(View.GONE);
                    }catch (Exception e){

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });
        }catch (Exception e){

        }

        return v;
    }
}
