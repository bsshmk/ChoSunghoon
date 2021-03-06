package com.example.mymemoapp_mk1.component.activity.fragment.memoBody;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.mymemoapp_mk1.DB.MemoReposityDB;
import com.example.mymemoapp_mk1.DB.data.MemoData;
import com.example.mymemoapp_mk1.DB.data.OptionData;
import com.example.mymemoapp_mk1.R;
import com.example.mymemoapp_mk1.ViewModel.MemoViewModel;
import com.example.mymemoapp_mk1.component.activity.MainActivity;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.support.AndroidSupportInjection;

public class MemoBodyFragment extends Fragment {
    public MemoBodyFragment(){

    }
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    MemoAdapter memoAdapter;
    Button sortButton;
    Button addButton;

    MainActivity mainActivity;
    AlertDialog dialog;
    final CharSequence[] howWrite = {"등록일", "마감일"};

    private MemoViewModel memoViewModel;


    ItemTouchHelper itemTouchHelper;
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    MemoReposityDB memoReposityDB;




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.configureViewModel();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.configureDagger();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_setting:
                Toast.makeText(getContext(), "setting page", Toast.LENGTH_SHORT).show();
                mainActivity.OnFragmentChange(3,null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void configureDagger(){

        AndroidSupportInjection.inject(this);
    }
    private void configureViewModel(){
        memoViewModel = ViewModelProviders.of(this, viewModelFactory).get(MemoViewModel.class);
        memoViewModel.init();
        memoViewModel.getMemoDataLiveData().observe(this, memoDataLiveData -> refreshDB(memoDataLiveData));
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.memo_body, container, false);

        init(rootView);

        clickSortButton();
        clickAddButton();

        hideKeyboard();

        return rootView;
    }

    private void init(ViewGroup rootView){
        if(memoReposityDB.getOptionData() == null){
            OptionData optionData = new OptionData();
            optionData.setSleepStartTime(23);
            optionData.setSleepEndTime(9);
            memoReposityDB.insertOption(optionData);

        }


        mainActivity = (MainActivity) getActivity();
        recyclerView = (RecyclerView)rootView.findViewById(R.id.memoListRecyclerView);
        layoutManager = new LinearLayoutManager(rootView.getContext());

        addButton = (Button)rootView.findViewById(R.id.addButton);
        sortButton = (Button)rootView.findViewById(R.id.sortButton);


        makeDialog(mainActivity);
        initListView();

    }


    private void initListView(){



        recyclerView.setHasFixedSize(true);

        memoAdapter = new MemoAdapter(getContext());
        recyclerView.setAdapter(memoAdapter);
        recyclerView.setLayoutManager(layoutManager);
        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(memoAdapter, memoReposityDB));
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }







    private void clickSortButton(){
        if(sortButton == null)
            return;
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

            }
        });
    }



    private void clickAddButton(){
        if(addButton == null)
            return;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.OnFragmentChange(1,null);
            }
        });
    }


    private void makeDialog(MainActivity mainActivity){
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle("정렬 방법을 선택하세요.");
        builder.setItems(howWrite, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    memoAdapter.registDateSort();

                }else{

                    memoAdapter.endDateSort();

                }//정렬을 외부에서 하고 다시 리사이크러뷰를 초기화해주는 병신같은 짓은 하지 말자.

            }
        });
        dialog = builder.create();    // 알림창 객체 생성

    }


    private void hideKeyboard(){
        MainActivity.mainActivity.getHideKeyboard().hideKeyboard();
    }

    public void refreshDB(@Nullable List<MemoData> memoDataList){
        memoAdapter.refreshItem(memoDataList);

    }








}
