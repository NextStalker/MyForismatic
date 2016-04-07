package com.next.myforismatic.fragments;

import android.support.v4.app.Fragment;

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
}
