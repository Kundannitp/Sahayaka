package com.pranks.doctalk.Education;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.pranks.doctalk.Home;
import com.pranks.doctalk.R;

public class OnlineTests extends Fragment {
    View v;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_online_tests, container, false);
        ((Home) getActivity()).getSupportActionBar().setTitle("Tests");

        return v;
    }
}
