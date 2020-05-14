package com.pranks.doctalk.Education;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pranks.doctalk.BlogsAndHome.HomeFragment;
import com.pranks.doctalk.R;

import java.util.Stack;


public class EducationHome extends Fragment {
    View v;
    BottomNavigationView bottomNavigationView;
    Stack<Fragment> stack=new Stack<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_education_home, container, false);

        bottomNavigationView = v.findViewById(R.id.education_bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                displaySelectedScreen(menuItem.getItemId());

                return true;
            }
        });
        loadFragment(new Notes());
        stack.push(new HomeFragment());
        return v;
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment!=null){
            getChildFragmentManager().beginTransaction().replace(R.id.framelayout_container_education,fragment).commit();
            return true;
        }
        return false;
    }

    private void displaySelectedScreen(int itemId) {

        Fragment fragment=null;
        switch(itemId)
        {
            case R.id.subject_notes:
                fragment=new Notes();
                break;
            case R.id.subject_papers:
                fragment=new PreviousPapers();
                break;
            case R.id.subject_tests:
                fragment=new OnlineTests();
                break;
        }
        stack.push(fragment);
        loadFragment(fragment);
    }


}
