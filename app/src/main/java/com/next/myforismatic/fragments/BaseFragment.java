package com.next.myforismatic.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.next.myforismatic.R;
import com.next.myforismatic.api.ForismaticApplication;
import com.next.myforismatic.api.ForismaticService;

/**
 * Created by Next on 07.04.2016.
 */
public class BaseFragment extends Fragment {

    private ForismaticApplication getForismaticApplication() {
        return (ForismaticApplication)getActivity().getApplication();
    }

    protected ForismaticService getForismaticService() {
        return getForismaticApplication().getForismaticService();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quote_list, container, false);
        return v;
    }
}
