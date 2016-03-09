package com.limxing.datepicker.DatePicker;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.limxing.datepicker.DatePicker.adapter.NumericWheelAdapter;
import com.limxing.datepicker.DatePicker.utils.DisplayUtil;
import com.limxing.datepicker.R;

import java.util.Calendar;

/**
 * created by Limxing 2016-3-9
 */
public class DatePickerView  {
	private WheelView year;
	private WheelView month;
	private WheelView day;
	
	private Context mContext;
	private DatePickerListener mListener;
	private Dialog dialog;
	private int fromYear=1950;
	private int toYear;

	public   DatePickerView(Context context,DatePickerListener listener){
		mContext=context;
		mListener=listener;
	}

	public void show(){
		 dialog = new Dialog(mContext, R.style.MMTheme_DatePicker);
		dialog.setCancelable(false);

		dialog.setContentView(getDataPick());
		Window w=dialog.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.dimAmount=0.4f;
		lp.gravity = Gravity.BOTTOM;
		lp.width = DisplayUtil.getScreenWith(mContext); //设置宽度
		w.setAttributes(lp);

		dialog.show();
	}

	/**
	 * 设置开始结束的年
	 * @param fromYear
	 * @param toYear
	 */
	public void setFromYearAndToYear(int fromYear,int toYear){
		this.fromYear=fromYear;
		this.toYear=toYear;
	}


	private View getDataPick() {
		Calendar c = Calendar.getInstance();
		int norYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH) + 1;
		int curDate = c.get(Calendar.DATE);

		toYear=norYear;
		View view = View.inflate(mContext,R.layout.wheel_date_picker, null);
		view.findViewById(R.id.finish).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				int n_year = year.getCurrentItem() + 1950;//
				int n_month = month.getCurrentItem() + 1;//
				int n_day = day.getCurrentItem() + 1;//
				mListener.finish(n_year+"-"+n_month+"-"+n_day);
			}
		});

		month = (WheelView) view.findViewById(R.id.month);
		year = (WheelView) view.findViewById(R.id.year);

		NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(
				mContext, fromYear, toYear);
		numericWheelAdapter1.setLabel("年");
		numericWheelAdapter1.setTextGravity(Gravity.RIGHT);
		year.setViewAdapter(numericWheelAdapter1);
		year.setCyclic(true);//
		year.addScrollingListener(scrollListener);

		NumericWheelAdapter numericWheelAdapter2 = new NumericWheelAdapter(
				mContext, 1, 12, "%d");
		numericWheelAdapter2.setLabel("月");
		month.setViewAdapter(numericWheelAdapter2);
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);

		day = (WheelView) view.findViewById(R.id.day);
		initDay(norYear, curMonth);
		day.setCyclic(true);
		day.addScrollingListener(scrollListener);

		year.setVisibleItems(5);
		month.setVisibleItems(5);
		day.setVisibleItems(5);

		year.setCurrentItem(norYear - 1950);
		month.setCurrentItem(curMonth - 1);
		day.setCurrentItem(curDate - 1);

		return view;
	}

	/**
	 */
	private void initDay(int arg1, int arg2) {
		NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(mContext,
				1, getDay(arg1, arg2), "%d");
		numericWheelAdapter.setTextGravity(Gravity.LEFT);
		numericWheelAdapter.setLabel("日");
		day.setViewAdapter(numericWheelAdapter);
	}
	/**
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	private int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}
	/**
	 *
	 */
	OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			int n_year = year.getCurrentItem() + 1950;
			int n_month = month.getCurrentItem() + 1;
			int n_day = day.getCurrentItem() + 1;
			initDay(n_year, n_month);

			mListener.dateChange(n_year+"-"+n_month+"-"+n_day);
		}
	};


	public void setOnListener(DatePickerListener listener) {
		// TODO Auto-generated method stub
		this.mListener=listener;
		
	}
	
	public interface DatePickerListener{
		 void dateChange(String string);
		 void finish(String string);
	}

}