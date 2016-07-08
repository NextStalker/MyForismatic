package com.next.myforismatic.di;

import com.next.myforismatic.ui.base.BaseFragment;
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
