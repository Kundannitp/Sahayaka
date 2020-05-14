package com.pranks.doctalk.BlogsAndHome;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pranks.doctalk.BlogsAndHome.UploadingClass.BlogUploadClass;
import com.pranks.doctalk.Home;
import com.pranks.doctalk.BlogsAndHome.Adapters.PagerAdapter;
import com.pranks.doctalk.R;

import java.util.Calendar;


public class HomeFragment extends Fragment
{

    public HomeFragment(){
    }


    View v;
    Context mContext;
    private PopupWindow mPopupWindow;
    private RelativeLayout mRelativeLayout;
    DatabaseReference mrefrence;
    String uid;
    TabLayout tabLayout;
    ViewPager viewPager;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, container, false);

        ((Home) getActivity()).getSupportActionBar().setTitle("Blogs");

        mRelativeLayout = v.findViewById(R.id.rl);
        mrefrence = FirebaseDatabase.getInstance().getReference();
        tabLayout=v.findViewById(R.id.tablayout);
        viewPager=v.findViewById(R.id.viewpage);

        tabing();

        mContext = getContext();
        FloatingActionButton fab = v.findViewById(R.id.add_blogs);
        fab.setOnClickListener(view -> {

            final Dialog customView = new Dialog(mContext);
            customView.setContentView(R.layout.writeblogs);
            customView.setTitle("Write Blogs");
            customView.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            customView.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_bottom;
//            customView.getWindow().setGravity(Gravity.BOTTOM);

            // Get a reference for the custom view close button
            ImageButton closeButton = customView.findViewById(R.id.ib_close);
            closeButton.setOnClickListener(v -> customView.dismiss());
            ImageView profilepic = customView.findViewById(R.id.blog_profile_pic);
            TextView personname = customView.findViewById(R.id.personname);
            Button btn = customView.findViewById(R.id.post);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
            if (acct != null) {
                String personName = acct.getDisplayName();
                Uri personPhoto = acct.getPhotoUrl();
                String personId = acct.getId();
                String personmail=acct.getEmail();
                String personPhotostr1="https://firebasestorage.googleapis.com/v0/b/doctalk-80d98.appspot.com/o/man.jpg?alt=media&token=52c8c697-f8ce-4a09-bbb1-7f7adcda4fa2";
                if(personPhoto!=null){
                    personPhotostr1=personPhoto.toString();
                }


                personname.setText(personName);
                Glide.with(this)
                        .load(personPhotostr1)
                        .into(profilepic);
                btn.setOnClickListener(v -> {
                    EditText blogdata = customView.findViewById(R.id.blog_data);
                    String blogs = blogdata.getText().toString();
                    if (!blogs.equals("")) {

                        String personPhotostr="https://firebasestorage.googleapis.com/v0/b/doctalk-80d98.appspot.com/o/man.jpg?alt=media&token=52c8c697-f8ce-4a09-bbb1-7f7adcda4fa2";
                        if(personPhoto!=null){
                            personPhotostr=personPhoto.toString();
                        }

                        BlogUploadClass objblog = new BlogUploadClass(personPhotostr, personName, blogs,personmail,0, Calendar.getInstance().getTimeInMillis(),"true");
                        String key=mrefrence.child("Blogs").push().getKey();
                        mrefrence.child("Blogs").child(key).setValue(objblog);
                        BlogUploadClass objblogper = new BlogUploadClass(blogs,0,Calendar.getInstance().getTimeInMillis(),key,"true");


                        mrefrence.child("PersonBlogs").child(personId).push().setValue(objblogper);
                        Toast.makeText(mContext, "Successfully posted", Toast.LENGTH_SHORT).show();
                        customView.dismiss();
                    } else {
                        Toast.makeText(mContext, "Please write something", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            customView.show();

        });
        return v;
    }


    public void tabing() {
        tabLayout.addTab(tabLayout.newTab().setText("All Blogs"));
        tabLayout.addTab(tabLayout.newTab().setText("My Blogs"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final PagerAdapter adapter=new PagerAdapter(getChildFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }


}