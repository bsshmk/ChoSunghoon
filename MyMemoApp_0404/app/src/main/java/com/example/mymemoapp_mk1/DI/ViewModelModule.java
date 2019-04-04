package com.example.mymemoapp_mk1.DI;


import com.example.mymemoapp_mk1.ViewModel.FactoryViewModel;
import com.example.mymemoapp_mk1.ViewModel.MemoViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MemoViewModel.class)
    abstract ViewModel bindMemoDataViewModel(MemoViewModel memoViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(FactoryViewModel factory);
}
