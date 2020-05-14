package com.pranks.doctalk.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.pranks.doctalk.BlogsAndHome.HomeFragment;
import com.pranks.doctalk.R;
import com.pranks.doctalk.BlogsAndHome.UpdateLive;
import com.pranks.doctalk.BlogsAndHome.chatBot;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Stack;

public class HomeFragmentContainer extends Fragment {

    View v;
    BottomNavigationView bottomNavigationView;
    Stack<Fragment> stack=new Stack<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragmentforbottom, container, false);

        bottomNavigationView = v.findViewById(R.id.patient_bottom_navigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                displaySelectedScreen(menuItem.getItemId());

                return true;
            }
        });
        loadFragment(new HomeFragment());
        stack.push(new HomeFragment());
        return v;
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment!=null){
            getChildFragmentManager().beginTransaction().replace(R.id.framelayoutcontainer,fragment).commit();
            return true;
        }
        return false;
    }

    private void displaySelectedScreen(int itemId) {

        Fragment fragment=null;
        switch(itemId)
        {
            case R.id.patient_blog:
                fragment=new HomeFragment();
                break;
            case R.id.patient_chat:
                fragment=new chatBot();
                break;
            case R.id.live_update:
                fragment=new UpdateLive();
                break;
        }
        stack.push(fragment);
        loadFragment(fragment);
    }



}
