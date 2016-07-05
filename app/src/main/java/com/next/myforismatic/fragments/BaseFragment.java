package com.next.myforismatic.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.next.myforismatic.ForismaticApplication;
import com.next.myforismatic.api.ForismaticService;

import javax.inject.Inject;

/**
 * Created by Next on 07.04.2016.
 */
public class BaseFragment extends Fragment {

    @Inject
    protected ForismaticService forismaticService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ForismaticApplication.getComponent().inject(this);
    }

    protected ForismaticService getForismaticService() {
        return forismaticService;
    }

}
