package com.next.myforismatic.ui.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.next.myforismatic.R;
import com.next.myforismatic.ui.base.BaseFragment;

/**
 * Created by maslparu on 22.06.2016.
 */
public class SettingsFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

}
