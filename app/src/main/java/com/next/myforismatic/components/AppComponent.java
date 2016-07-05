package com.next.myforismatic.components;

import com.next.myforismatic.fragments.BaseFragment;
import com.next.myforismatic.modules.AppModule;
import com.next.myforismatic.services.ForismaticIntentService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by maslparu on 28.06.2016.
 */
@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {

    void inject(BaseFragment fragment);
    void inject(ForismaticIntentService intentService);
}
