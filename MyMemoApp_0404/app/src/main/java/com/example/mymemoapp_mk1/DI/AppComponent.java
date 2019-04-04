package com.example.mymemoapp_mk1.DI;


import android.app.Application;

import com.example.mymemoapp_mk1.component.App;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;


@Singleton
@Component(modules={AndroidSupportInjectionModule.class, ActivityModule.class,
        FragmentModule.class, AppModule.class, ServiceModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }

    void inject(App app);
}
