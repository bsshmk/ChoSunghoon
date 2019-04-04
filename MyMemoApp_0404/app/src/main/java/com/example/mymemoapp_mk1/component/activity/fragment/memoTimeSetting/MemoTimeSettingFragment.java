package com.example.mymemoapp_mk1.component.activity.fragment.memoTimeSetting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymemoapp_mk1.DB.data.MemoData;
import com.example.mymemoapp_mk1.R;
import com.example.mymemoapp_mk1.ViewModel.MemoViewModel;
import com.example.mymemoapp_mk1.component.Alarm.RandomTimeMaker;
import com.example.mymemoapp_mk1.component.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.AndroidSupportInjection;

public class MemoTimeSettingFragment extends Fragment {

    MemoData MD;
    String mTitle,mText;

    MainActivity mainActivity;
    Button saveButton;
    SimpleDateFormat mFormat;
    String time;
    Button TimeSettingPageBackButton;

    RelativeLayout settingLayout;

    Button selectDate;
    TextView textView1;
    String deadLine;

    int interval;

    final String notChanged = "There's no Deadline.. (Click above button)";
    final String emptyTitle = "";
    final String emptyContent = "";

    public static final int REQUEST_CODE = 11;

    NumberPicker np1;
    final String[] values = {"하루 2~3회", "하루 1~2회", "주 6~7회", "주 3회 미만"};
    final int[] intervals = {2,4,13,23};


    private MemoViewModel memoViewModel;

    @Inject
    ViewModelProvider.Factory viewModelFactory;





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity)getActivity();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.configureDagger();
        this.configureViewModel();

    }


    private void configureDagger(){
        AndroidSupportInjection.inject(this);
    }
    private void configureViewModel(){
        memoViewModel = ViewModelProviders.of(this, viewModelFactory).get(MemoViewModel.class);
        memoViewModel.init();

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.memo_time_setting,container,false);

        super.onCreateView(inflater,container,savedInstanceState);
        init(rootView);
        hideKeyboard();
        clickHideKeyboard();
        final FragmentManager fm = ((AppCompatActivity)getActivity()).getSupportFragmentManager();

        //임시,, 지우거나 빼기
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDialogFragment newFragment = new SelectDateFragment();
                newFragment.setTargetFragment(MemoTimeSettingFragment.this, REQUEST_CODE);
                newFragment.show(fm, "datePicker");
            }
        });

        //save button
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(deadLine == notChanged){
                    Toast.makeText(getContext(), "Set Deadline!!", Toast.LENGTH_SHORT).show();
                }
                else { saveData(); }
            }
        });

        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if(requestCode == REQUEST_CODE && requestCode == Activity.RESULT_OK){
        textView1.setText(data.getStringExtra("selectedDate"));
        deadLine = data.getStringExtra("selectedDate");
        //}
//        super.onActivityResult(requestCode, resultCode, data);
    }

    public void init(ViewGroup rootView ){

        MD = new MemoData();
        TimeSettingPageBackButton = rootView.findViewById(R.id.TimeSettingPageBackButton);
        settingLayout = rootView.findViewById(R.id.settingLayout);
        saveButton=(Button)rootView.findViewById(R.id.settingSaveButton);//저장버튼

        selectDate = (Button)rootView.findViewById(R.id.settingDate);
        textView1 = (TextView)rootView.findViewById(R.id.dateTextView);

        deadLine = notChanged;

        np1 = (NumberPicker) rootView.findViewById(R.id.np1);

        np1.setMinValue(0);
        np1.setMaxValue(values.length - 1);
        np1.setDisplayedValues(values);
        np1.setValue(0);


        mFormat=new SimpleDateFormat("yy-MM-dd kk:mm");//날짜 형식 지정

        //메모 받기
        mText=emptyTitle;
        mTitle=emptyContent;

        if(getArguments()!=null) {
            mTitle = getArguments().getString("title");
            mText = getArguments().getString("content");
        }
        clickBack();
    }

    //내부저장소 저장기능
    public void saveData(){

        //현재 시간 받기
        Calendar calendar = Calendar.getInstance();
        time=mFormat.format(calendar.getTime());

        MD.setEndDateTextView(deadLine);
        MD.setRegistDateTextView(time.substring(0, 8));

        MD.setMemoText(mText);
        MD.setMemoTitle(mTitle);

        np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected value from picker
                interval = newVal;
            }
        });

        MD.setMinTime(Integer.toString(makeInterval(interval)));


        //여기에 내부저장소 저장
        //파일명은 메모의 아이디 스트링값

        /*
        try {
            FileOutputStream fos = getContext().openFileOutput(Integer.toString(MD.getId()), Context.MODE_PRIVATE);
            fos.write(new RandomTimeMaker().Randomize(deadLine,time,Integer.parseInt(MD.getMinTime())*60).getBytes());
            fos.write("19040121501904012149".getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MD.setRandomTime(Integer.toString(MD.getId()));
        */
        //여기까지가 바뀐 코드


        //기존 코드

        MD.setRandomTime(new RandomTimeMaker().Randomize(
                deadLine,
                time,
                Integer.parseInt(MD.getMinTime())*60));//수면 시작은 시간만


       // MD.setRandomTime("190401211519040121101904012105");//수면 시작은 시간만




           //년도가 너무 커지면 생성되는 랜덤사이즈가 너무 커진다.
        memoViewModel.insertMemoData(MD);
        changeFragment(0);

    }


    //프래그먼트 변환
    public void changeFragment(int idx){

        //디비에 저장....
        mainActivity.OnFragmentChange(idx, null);//이 페이지에서 데이터 처리하고 널을 넘기자.
    }

    private void hideKeyboard(){

        MainActivity.mainActivity.getHideKeyboard().hideKeyboard();
    }
    private void clickHideKeyboard(){
        settingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();

            }
        });
    }
    private void clickBack(){
        TimeSettingPageBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.OnFragmentChange(1, null);
            }
        });
    }
    public int makeInterval(int idx){
        return intervals[idx];
    }

}
