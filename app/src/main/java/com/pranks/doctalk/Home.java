package com.pranks.doctalk;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.pranks.doctalk.BlogsAndHome.UploadingClass.ProfileClass;
import com.pranks.doctalk.Education.EducationHome;
import com.pranks.doctalk.ui.gallery.ProfileFrag;
import com.pranks.doctalk.ui.home.HomeFragmentContainer;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    ImageView personimage;
    TextView personname,personemail;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    NavigationView navigationView;
    DrawerLayout drawer ;
    View v;
    private ActionBarDrawerToggle mToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        mToggle=new ActionBarDrawerToggle(this,drawer,R.string.open,R.string.close);

        Intent intent = getIntent();
        drawer.addDrawerListener(mToggle);
        mToggle.syncState();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_navigation_drawer);
        setUpDrawerContent(navigationView);
        navigationView.getMenu().getItem(0).setChecked(true);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                displaySelectedScreen(menuItem.getItemId());

                return true;
            }
        });
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.nav_host_fragment, new HomeFragmentContainer());
        tx.commit();
    }

    private void setUpDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                displaySelectedScreen(menuItem.getItemId());
                return true;
            }
        });
    }

    protected void onStart() {

        super.onStart();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);

        personimage=hView.findViewById(R.id.patient_profile_pic);
        personname=hView.findViewById(R.id.patient_name);
        personemail=hView.findViewById(R.id.patient_email);

        DatabaseReference mref= FirebaseDatabase.getInstance().getReference();
        try {
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(Home.this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personEmail = acct.getEmail();
                Uri personPhoto = acct.getPhotoUrl();
                personemail.setText(personEmail);
                personname.setText(personName);

                String personPhotostr = "https://firebasestorage.googleapis.com/v0/b/doctalk-80d98.appspot.com/o/man.jpg?alt=media&token=52c8c697-f8ce-4a09-bbb1-7f7adcda4fa2";
                if (personPhoto != null) {
                    personPhotostr = personPhoto.toString();
                }

                ProfileClass profileClass = new ProfileClass(personName, personEmail, personPhotostr);
                mref.child("Profiles").child(acct.getId()).setValue(profileClass);

                Glide.with(this)
                        .load(personPhotostr)
                        .into(personimage);
            }
        }catch (Exception e){

        }
    }


    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {

            case R.id.nav_patient_profile:
                navigationView.getMenu().getItem(1).setChecked(false);
                navigationView.getMenu().getItem(0).setChecked(false);
                navigationView.getMenu().getItem(2).setChecked(true);
                fragment = new ProfileFrag();
                break;
            case R.id.nav_patient_logout:
                showLogoutDialog();
                break;
            case R.id.nav_patient_home:
                navigationView.getMenu().getItem(0).setChecked(true);
                navigationView.getMenu().getItem(1).setChecked(false);
                navigationView.getMenu().getItem(2).setChecked(false);
                fragment=new HomeFragmentContainer();
                break;
            case R.id.nav_patient_education:
                navigationView.getMenu().getItem(0).setChecked(false);
                navigationView.getMenu().getItem(1).setChecked(true);
                navigationView.getMenu().getItem(2).setChecked(false);
//                fragment=new EducationHome();
                Toast.makeText(this, "Comming Soon...", Toast.LENGTH_SHORT).show();
                break;
        }


        //replacing the fragment
        if (fragment != null) {
            navigationView.getMenu().getItem(0).setChecked(true);
            navigationView.getMenu().getItem(1).setChecked(false);
            navigationView.getMenu().getItem(2).setChecked(false);
            getSupportFragmentManager().beginTransaction().
            replace(R.id.nav_host_fragment, fragment)
            .commit();
        }
        drawer.closeDrawer(GravityCompat.START);
    }

    private void showLogoutDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setMessage("Sure to logout?");
        builder.setCancelable(false);

        builder.setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                mGoogleSignInClient.signOut();

                Toast.makeText(Home.this, "Successfully signed out", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(Home.this, MainActivity.class));
                finish();

                dialog.cancel();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        builder.show();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
