package com.pranks.doctalk.BlogsAndHome;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pranks.doctalk.BlogsAndHome.Adapters.BlogAdapter;
import com.pranks.doctalk.BlogsAndHome.UploadingClass.BlogRetriveClass;
import com.pranks.doctalk.BlogsAndHome.UploadingClass.BlogUploadClass;
import com.pranks.doctalk.R;

import java.util.ArrayList;


public class AllBlogs extends Fragment {
    View v;

    DatabaseReference mrefrence,dsr;
    BlogAdapter blogAdapter;

    RecyclerView mRecyclerView;
    ArrayList<BlogRetriveClass> blg;
    boolean async=false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_all_blogs, container, false);
        setHasOptionsMenu(true);

        mRecyclerView = v.findViewById(R.id.all_blog_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mrefrence= FirebaseDatabase.getInstance().getReference();
        dsr=FirebaseDatabase.getInstance().getReference();
//      addListenerForSingleValueEvent
        mrefrence.child("Blogs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<BlogRetriveClass> blg=new ArrayList<>();
                try {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String name = ds.child("name").getValue().toString();
                        String profilepic = ds.child("profilepic").getValue().toString();
                        String blogmsg = ds.child("blogdata").getValue().toString();
                        String mail = ds.child("mail").getValue().toString();
                        long timestamp = Long.parseLong(ds.child("timestamp").getValue().toString());
                        int like = Integer.parseInt(ds.child("likes").getValue().toString());
                        String havetoshow=ds.child("havetoshow").getValue().toString();
                        String uids=ds.getKey();
                        GoogleSignInAccount acct1 = GoogleSignIn.getLastSignedInAccount(getContext());
                        String uidper1 = acct1.getId();
                        boolean coloroflikes=false;
                        if(ds.child("likedperson").exists()) {
                            for (DataSnapshot ds1 : ds.child("likedperson").getChildren()) {
                                if (ds1.getValue().toString().equals(uidper1)) {
                                    coloroflikes = true;
                                }
                            }
                        }
                        long noofcomments=0;
                        if(ds.child("Comments").exists()){
                            noofcomments = ds.child("Comments").getChildrenCount();
                        }
                        if(havetoshow.equals("true")){
                            BlogRetriveClass upl = new BlogRetriveClass(profilepic,name,blogmsg,mail,like,noofcomments,coloroflikes,timestamp,uids);
                            blg.add(upl);
                        }

                    }

                    blogAdapter = new BlogAdapter(getContext(), blg);
                    UpdatedatatoArrays(blg,blogAdapter);
                    sendItToSync();
                    mRecyclerView.setAdapter(blogAdapter);
                    ProgressBar progressBar = v.findViewById(R.id.progress_circle);
                    progressBar.setVisibility(View.GONE);
                }catch (Exception e){

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        return v;
    }

    void UpdatedatatoArrays(ArrayList<BlogRetriveClass> blg1,BlogAdapter blogAdapter1){
        blg=new ArrayList<>(blg1);
        blogAdapter=blogAdapter1;
    }

    void sendItToSync(){
        mrefrence.child("Blogs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<BlogRetriveClass> blg=new ArrayList<>();
                try {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String name = ds.child("name").getValue().toString();
                        String profilepic = ds.child("profilepic").getValue().toString();
                        String blogmsg = ds.child("blogdata").getValue().toString();
                        String mail = ds.child("mail").getValue().toString();
                        long timestamp = Long.parseLong(ds.child("timestamp").getValue().toString());
                        int like = Integer.parseInt(ds.child("likes").getValue().toString());
                        String havetoshow=ds.child("havetoshow").getValue().toString();
                        String uids=ds.getKey();
                        GoogleSignInAccount acct1 = GoogleSignIn.getLastSignedInAccount(getContext());
                        String uidper1 = acct1.getId();
                        boolean coloroflikes=false;
                        if(ds.child("likedperson").exists()) {
                            for (DataSnapshot ds1 : ds.child("likedperson").getChildren()) {
                                if (ds1.getValue().toString().equals(uidper1)) {
                                    coloroflikes = true;
                                }
                            }
                        }
                        long noofcomments=0;
                        if(ds.child("Comments").exists()){
                            noofcomments = ds.child("Comments").getChildrenCount();
                        }
                        if(havetoshow.equals("true")){
                            BlogRetriveClass upl = new BlogRetriveClass(profilepic,name,blogmsg,mail,like,noofcomments,coloroflikes,timestamp,uids);
                            blg.add(upl);
                        }

                    }
                    blogAdapter.updateList(blg);
                    UpdatedatatoArrays(blg,blogAdapter);
                }catch (Exception e){

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }


    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_name);
        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(searchItem);
        searchView.setQueryHint("By name or mail...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String text) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                filter(text);
//                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    void filter(String text){
        ArrayList<BlogRetriveClass> blogup=new ArrayList<>();
        for(int k=0;k<blg.size();k++){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(blg.get(k).getName().toLowerCase().contains(text.trim())||blg.get(k).getMail().toLowerCase().contains(text.trim())){
                blogup.add(blg.get(k));
            }
        }
        //update recyclerview
        blogAdapter.updateList(blogup);
    }


}
