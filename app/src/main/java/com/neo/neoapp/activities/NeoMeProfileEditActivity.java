package com.neo.neoapp.activities;



import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.neo.neoandroidlib.DateUtils;
import com.neo.neoandroidlib.TextUtils;
import com.neo.neoapp.NeoBasicActivity;
import com.neo.neoapp.R;
import com.neo.neoapp.UI.views.NeoBasicTextView;
import com.neo.neoapp.entity.People;
import com.neo.neoapp.entity.PeopleProfile;

public class NeoMeProfileEditActivity extends NeoBasicActivity implements OnFocusChangeListener {
	//private NeoCommonListView commonList;
	//private NeoMeProfileEditListAdapter commonListAdpt;
	NeoBasicTextView tvNameTitle;
	NeoBasicTextView tvSignTitle;
	NeoBasicTextView tvAgeTitle;
	NeoBasicTextView tvBirthdayTitle;
	NeoBasicTextView tvGenderTitle;
	NeoBasicTextView tvIndustryTitle;
	
	EditText etNameValue;
	EditText etSignValue;
	NeoBasicTextView tvAgeValue;
	NeoBasicTextView tvBirthdayValue;
	EditText etIndustryValue;
	
	RadioGroup rgGender;
	RadioButton female;
	RadioButton male;
	
	Button btSubmmit;
	
	private Calendar mCalendar;
	private Date mMinDate;
	private Date mMaxDate;
	private Date mSelectDate;
	private static final int MAX_AGE = 100;
	private static final int MIN_AGE = 12;
	
	People mMe = null;
	PeopleProfile mMyProfile = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_me_profile_edit);
		initViews();
		initEvents();
		initData();
	}
	
	private void initData() {
		// TODO Auto-generated method stub
		mSelectDate = DateUtils.getDate(mApplication.mMe.getBirthday(),"yyyy-MM-dd");

		Calendar mMinCalendar = Calendar.getInstance();
		Calendar mMaxCalendar = Calendar.getInstance();

		mMinCalendar.set(Calendar.YEAR, mMinCalendar.get(Calendar.YEAR)
				- MIN_AGE);
		mMinDate = mMinCalendar.getTime();
		mMaxCalendar.set(Calendar.YEAR, mMaxCalendar.get(Calendar.YEAR)
				- MAX_AGE);
		mMaxDate = mMaxCalendar.getTime();

		mCalendar = Calendar.getInstance();
		mCalendar.setTime(mSelectDate);
		
		mMe = new People(mApplication.mMe);
		mMyProfile = new PeopleProfile(mApplication.mMyProfile);
	}
	
	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		/*commonListAdpt = new NeoMeProfileEditListAdapter(mApplication,this);
		commonList = (NeoCommonListView)findViewById(R.id.me_profile_edit_list);
		commonList.setAdapter(commonListAdpt);*/
		etNameValue = (EditText) findViewById(R.id.me_edit_value_name);
		etNameValue.setText(mApplication.mMe.getName());
		
		etSignValue = (EditText) findViewById(R.id.me_edit_value_sign);
		etSignValue.setText(mApplication.mMe.getSign());
		
		tvAgeValue = (NeoBasicTextView) findViewById(R.id.me_edit_value_age);
		tvAgeValue.setText(String.valueOf(mApplication.mMe.getAge()));
		
		tvBirthdayValue = (NeoBasicTextView)findViewById(R.id.me_edit_value_birthday);
		tvBirthdayValue.setText(mApplication.mMe.getBirthday());
		
		etIndustryValue = (EditText) findViewById(R.id.me_edit_value_industry);
		etIndustryValue.setText(mApplication.mMe.getIndustry());
		
		rgGender = (RadioGroup)findViewById(R.id.me_edit_radioGroup);
		female = (RadioButton) findViewById(R.id.me_edit_radioFemale);
		male = (RadioButton) findViewById(R.id.me_edit_radioMale);
		if (mApplication.mMe.getGender()==0)
			female.setChecked(true);
		else
			male.setChecked(true);
		
		btSubmmit = (Button)findViewById(R.id.me_edit_value_button);
		
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		etNameValue.setOnFocusChangeListener(this);
		etSignValue.setOnFocusChangeListener(this);
		etIndustryValue.setOnFocusChangeListener(this);
		
		tvBirthdayValue.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDatePickerDialog();
			}
			
		});
		
		rgGender.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				if (arg1 == female.getId())
					mMe.setGender(0);
				
				if (arg1 == male.getId())
					mMe.setGender(1);
			}
			
		});
		
		btSubmmit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	@SuppressLint("SimpleDateFormat")
	protected void showDatePickerDialog() {
		// TODO Auto-generated method stub		
		DatePickerDialog dpDialog = 
				new DatePickerDialog(this,dpDialogListner,
						mCalendar.get(Calendar.YEAR),
						mCalendar.get(Calendar.MONTH),
						mCalendar.get(Calendar.DAY_OF_MONTH));
		dpDialog.show();
	}
	
	private DatePickerDialog.OnDateSetListener dpDialogListner = 
			new DatePickerDialog.OnDateSetListener(){

				@Override
				public void onDateSet(DatePicker arg0, int year, int monthOfYear,
						int dayOfMonth) {
					// TODO Auto-generated method stub
					mCalendar = Calendar.getInstance();
					mCalendar.set(year, monthOfYear, dayOfMonth);
					if (mCalendar.getTime().after(mMinDate)
							|| mCalendar.getTime().before(mMaxDate)) {
						mCalendar.setTime(mSelectDate);
						
					} else {
						flushBirthday(mCalendar);
					}
				}
		
	};
	
	@Override
	public void onFocusChange(View arg0, boolean arg1) {
		// TODO Auto-generated method stub
		if (!arg1){
			if (arg0.getId()==etNameValue.getId()){
				
			}else if (arg0.getId()==etSignValue.getId()){
				mMe.setSign(etSignValue.getText().toString().trim());
			}else if (arg0.getId()==etIndustryValue.getId()){
				mMe.setSign(etIndustryValue.getText().toString().trim());
			}
		}
	}

	protected void flushBirthday(Calendar calendar) {
		// TODO Auto-generated method stub
		String constellation = TextUtils.getConstellation(
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		mSelectDate = calendar.getTime();
		mMyProfile.setConstellation(constellation);
		int age = TextUtils.getAge(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		mMe.setAge(age);
		mMe.setBirthday(DateUtils.dateToString(mSelectDate,"yyyy-MM-dd"));
		//tvAgeValue.setText(age);
	}

}
