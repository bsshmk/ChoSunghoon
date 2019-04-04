package com.example.mymemoapp_mk1.DI;

import com.example.mymemoapp_mk1.component.Alarm.Service.AlarmService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ServiceModule {
    @ContributesAndroidInjector
    abstract AlarmService contributeAlarmService();
}
