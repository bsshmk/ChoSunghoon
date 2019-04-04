package com.example.mymemoapp_mk1.DI;


import android.app.Application;

import com.example.mymemoapp_mk1.DB.AppDB;
import com.example.mymemoapp_mk1.DB.MemoDataDao;
import com.example.mymemoapp_mk1.DB.MemoReposityDB;
import com.example.mymemoapp_mk1.DB.OptionDataDao;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;

@Module(includes = ViewModelModule.class)
public class AppModule {





    // --- DATABASE INJECTION ---
    @Provides
    @Singleton
    AppDB provideDatabase(Application application) {
        return Room.databaseBuilder(application,
                AppDB.class, "testDatabase.db")
                //.fallbackToDestructiveMigration()
                .build();
    }
    @Provides
    @Singleton
    MemoDataDao provideMemoDataDao(AppDB database) { return database.memoDataDao(); }

    @Provides
    @Singleton
    OptionDataDao provideOptionDataDao(AppDB database) { return database.optionDataDao(); }

    // --- REPOSITORY INJECTION ---
    @Provides
    @Singleton
    MemoReposityDB provideMemoReposityDB(MemoDataDao memoDataDao, OptionDataDao optionDataDao) {
        return new MemoReposityDB(memoDataDao, optionDataDao);
    }



}
