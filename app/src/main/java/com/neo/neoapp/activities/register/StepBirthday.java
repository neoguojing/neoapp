package com.neo.neoapp.activities.register;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.neo.neoandroidlib.DateUtils;
import com.neo.neoandroidlib.TextUtils;
import com.neo.neoapp.R;
import com.neo.neoapp.UI.views.NeoBasicTextView;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;


public class StepBirthday extends RegisterStep implements OnDateChangedListener {

	private NeoBasicTextView mHtvConstellation;
	private NeoBasicTextView mHtvAge;
	private DatePicker mDpBirthday;

	private Calendar mCalendar;
	private Date mMinDate;
	private Date mMaxDate;
	private Date mSelectDate;
	private static final int MAX_AGE = 100;
	private static final int MIN_AGE = 12;

	public StepBirthday(RegisterActivity activity, View contentRootView) {
		super(activity, contentRootView);
		initData();

	}

    public String getAge() {
        return this.mHtvAge.getText().toString().trim();
    }

    public String getConstellation() {
        return this.mHtvConstellation.getText().toString().trim();
    }

    @SuppressLint({"SimpleDateFormat"})
    public String getBirthday() {
        return new SimpleDateFormat("yyyy-MM-dd").format(this.mSelectDate);
    }
	private void flushBirthday(Calendar calendar) {
		String constellation = TextUtils.getConstellation(
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		mSelectDate = calendar.getTime();
		mHtvConstellation.setText(constellation);
		int age = TextUtils.getAge(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		mHtvAge.setText(age + "");
	}

	private void initData() {
		mSelectDate = DateUtils.getDate("19900101");

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
		flushBirthday(mCalendar);
		mDpBirthday.init(mCalendar.get(Calendar.YEAR),
				mCalendar.get(Calendar.MONTH),
				mCalendar.get(Calendar.DAY_OF_MONTH), this);
	}

	@Override
	public void initViews() {
		mHtvConstellation = (NeoBasicTextView) findViewById(R.id.reg_birthday_htv_constellation);
		mHtvAge = (NeoBasicTextView) findViewById(R.id.reg_birthday_htv_age);
		mDpBirthday = (DatePicker) findViewById(R.id.reg_birthday_dp_birthday);
	}

	@Override
	public void initEvents() {

	}

	@Override
	public void doNext() {
		mOnNextActionListener.next();
	}

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public boolean isChange() {
		return false;
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		mCalendar = Calendar.getInstance();
		mCalendar.set(year, monthOfYear, dayOfMonth);
		if (mCalendar.getTime().after(mMinDate)
				|| mCalendar.getTime().before(mMaxDate)) {
			mCalendar.setTime(mSelectDate);
			mDpBirthday.init(mCalendar.get(Calendar.YEAR),
					mCalendar.get(Calendar.MONTH),
					mCalendar.get(Calendar.DAY_OF_MONTH), this);
		} else {
			flushBirthday(mCalendar);
		}
	}

}
