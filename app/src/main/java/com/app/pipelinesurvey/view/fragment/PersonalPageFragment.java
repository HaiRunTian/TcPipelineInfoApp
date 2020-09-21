package com.app.pipelinesurvey.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.pipelinesurvey.R;


/**
 * Created by Kevin on 2018-05-16.
 *ViewPager.OnPageChangeListener,RadioGroup.OnCheckedChangeListener,
 */

public class PersonalPageFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personalpage, container, false);
        initView(view);

        return view;
    }

    private void initView(View view) {

    }

}
