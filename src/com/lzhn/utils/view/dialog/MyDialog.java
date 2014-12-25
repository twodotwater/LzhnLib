package com.lzhn.utils.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzhn.lib.R;
import com.lzhn.utils.common.Resource;
import com.lzhn.utils.view.ViewUtils;

/**
 * 对话框
 * 
 * @author lzhn
 * 
 */
public class MyDialog extends Dialog implements
		android.view.View.OnClickListener {
	private static final String TAG = "MyDialog";

	/** 消息对话框 */
	public static final int MODE_MESSAGE = 1;
	/** 输入对话框 */
	public static final int MODE_INPUT = 2;
	/** 自定义内容对话框 */
	public static final int MODE_VIEW = 3;

	private Context context;

	/** 对话框的布局view */
	private View view;
	/** 对话框主布局 */
	private LinearLayout ll_dialog;
	/** 显示contentView中的子View */
	private int id_btn_OK;
	private int id_btn_NO;
	private View childView;
	private TextView tv_title;
	private TextView tv_titleGap;
	private TextView tv_message;
	private EditText et_input;
	private Button btn_OK;
	private Button btn_NO;
	private TextView tv_gap;
	/** 对话框中间显示内容的View */
	private LinearLayout contentView;
	private OnButtonClickListener onButtonClickListener;

	private Window window;
	private LayoutParams lp;

	private MyDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
		view = LayoutInflater.from(context).inflate(R.layout.my_dialog, null);
		initViews(view);
	}

	private MyDialog(Context context, int theme, int mode) {
		this(context, theme);
		switch (mode) {
		case MODE_MESSAGE:
			tv_message.setVisibility(View.VISIBLE);
			et_input.setVisibility(View.GONE);
			break;
		case MODE_INPUT:
			tv_message.setVisibility(View.GONE);
			et_input.setVisibility(View.VISIBLE);
			break;
		case MODE_VIEW:
			tv_message.setVisibility(View.GONE);
			et_input.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	/**
	 * 生成一个对话框
	 * 
	 * @param context
	 * @param mode
	 *            对话框模式：{@link #MODE_MESSAGE},{@link #MODE_INPUT},
	 *            {@link #MODE_VIEW}
	 * @return
	 */
	public static MyDialog newInstance(Context context, int mode) {
		return new MyDialog(context, android.R.style.Theme_Light_NoTitleBar,
				mode);
	}

	private void initViews(View view2) {
		id_btn_OK = Resource.getInstance(context).getIdIdentifier("btn_OK");
		id_btn_NO = Resource.getInstance(context).getIdIdentifier("btn_NO");
		ll_dialog = (LinearLayout) view2.findViewById(R.id.ll_dialog);
		tv_title = (TextView) view2.findViewById(R.id.tv_title);
		tv_titleGap = (TextView) view2.findViewById(R.id.tv_titleGap);
		tv_message = (TextView) view2.findViewById(R.id.tv_message);
		et_input = (EditText) view2.findViewById(R.id.et_input);
		btn_OK = (Button) view2.findViewById(id_btn_OK);
		btn_NO = (Button) view2.findViewById(id_btn_NO);
		tv_gap = (TextView) view2.findViewById(R.id.tv_gap);
		contentView = (LinearLayout) view2.findViewById(R.id.contentView);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(view);
		initWindow();
		refreshDialogSize();
		setListeners();
	}

	private void initWindow() {
		window = getWindow();
		lp = window.getAttributes();
		setDialogAlpha(1f);
		setDialogMinHeight(getDefaultHeightScale());
		setDialogMinWidth(getDefaultWidthScale());
	}

	/**
	 * 指定对话框最小高度
	 * 
	 * @param heightScale
	 *            手机屏幕高度的百分比：(0,1)
	 */
	public void setDialogMinHeight(float heightScale) {
		if (heightScale < 0 || heightScale > 1) {
			heightScale = 0.5f;
		}
		view.setMinimumHeight((int) (ViewUtils.getWindowDisplayMetrics(context).heightPixels * heightScale));
	}

	/**
	 * 指定对话框最小宽度
	 * 
	 * @param widthScale
	 *            手机屏幕宽度的百分比：(0,1)
	 */
	public void setDialogMinWidth(float widthScale) {
		if (widthScale < 0 || widthScale > 1) {
			widthScale = 0.5f;
		}
		view.setMinimumWidth((int) (ViewUtils.getWindowDisplayMetrics(context).widthPixels * widthScale));
	}

	/**
	 * 指定对话框透明度
	 * 
	 * @param alpha
	 *            透明度：[0,1]
	 */
	public void setDialogAlpha(float alpha) {
		if (alpha < 0)
			alpha = 0;
		else if (alpha > 1)
			alpha = 1;
		lp.alpha = alpha;
		window.setAttributes(lp);
	}

	private float getDefaultHeightScale() {
		boolean isLandscape = lp.screenOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
				|| lp.screenOrientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
		return isLandscape ? 0.5f : 0.3f;
	}

	private float getDefaultWidthScale() {
		boolean isLandscape = lp.screenOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
				|| lp.screenOrientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
		return isLandscape ? 0.5f : 0.7f;
	}

	private MyDialog setListeners() {
		btn_OK.setOnClickListener(this);
		btn_NO.setOnClickListener(this);
		return this;
	}

	private void refreshDialogSize() {
		view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		lp.width = view.getMeasuredWidth();
		lp.height = view.getMeasuredHeight();
		window.setAttributes(lp);
	}

	public MyDialog setOnBtnClickListener(
			OnButtonClickListener onBtnClickListener) {
		this.onButtonClickListener = onBtnClickListener;
		return this;
	}

	/**
	 * 设定背景图片
	 * 
	 * @param id
	 *            图片id
	 * @return
	 */
	public MyDialog setBackgroundDrawable(int id) {
		if (id != 0)
			ll_dialog.setBackground(context.getResources().getDrawable(id));
		return this;
	}

	/**
	 * 设定对话框背景色
	 * 
	 * @param color
	 * @return
	 */
	public MyDialog setBackgroundColor(int color) {
		ll_dialog.setBackgroundColor(color);
		return this;
	}

	/**
	 * 指定对话框主体文字颜色
	 * 
	 * @param color
	 * @return
	 */
	public MyDialog setColor(int color) {
		setTitleColor(color);
		setTitleGapColor(color);
		setMessageColor(color);
		setInputTextColor(color);
		setPositiveTextColor(color);
		setGapColor(color);
		// setNegativeTextColor(color);
		return this;
	}

	public MyDialog setTitleGapColor(int color) {
		tv_titleGap.setBackgroundColor(color);
		return this;
	}

	public MyDialog setGapColor(int color) {
		tv_gap.setBackgroundColor(color);
		return this;
	}

	/**
	 * 对话框标题
	 * 
	 * @param title
	 * @return
	 */
	public MyDialog setTitle(String title) {
		tv_title.setText(title);
		return this;
	}

	public MyDialog setTitleColor(int color) {
		tv_title.setTextColor(color);
		return this;
	}

	/**
	 * 提示消息
	 * 
	 * @param msg
	 * @return
	 */
	public MyDialog setMessage(String msg) {
		tv_message.setText(msg);
		return this;
	}

	public MyDialog setMessageColor(int color) {
		tv_message.setTextColor(color);
		return this;
	}

	/**
	 * ok键文字
	 * 
	 * @param positiveText
	 * @return
	 */
	public MyDialog setPositiveText(String positiveText) {
		btn_OK.setText(positiveText);
		return this;
	}

	public MyDialog setPositiveTextColor(int color) {
		btn_OK.setTextColor(color);
		return this;
	}

	/**
	 * no键文字
	 * 
	 * @param negativeText
	 * @return
	 */
	public MyDialog setNegativeText(String negativeText) {
		btn_NO.setText(negativeText);
		return this;
	}

	public MyDialog setNegativeTextColor(int color) {
		btn_NO.setTextColor(color);
		return this;
	}

	/**
	 * 指定输入框内容
	 * 
	 * @param inputText
	 * @return
	 */
	public MyDialog setInputText(String inputText) {
		et_input.setText(inputText);
		return this;
	}

	public MyDialog setInputTextColor(int color) {
		et_input.setTextColor(color);
		return this;
	}

	/**
	 * 获取输入框内容
	 * 
	 * @return
	 */
	public String getInputText() {
		return et_input.getText().toString().trim();
	}

	/**
	 * 隐藏ok按钮
	 * 
	 * @return
	 */
	public MyDialog hidePositiveButton() {
		btn_OK.setVisibility(View.GONE);
		tv_gap.setVisibility(View.GONE);
		return this;
	}

	private MyDialog addView(View childView) {
		contentView.removeAllViews();
		contentView.addView(childView);

		this.childView = childView;
		return this;
	}

	/**
	 * 获取自定义内容View
	 * 
	 * @return
	 */
	public View getChildView() {
		return childView;
	}

	/**
	 * 指定对话框显示的子View
	 * 
	 * @param childView
	 * @return
	 */
	public MyDialog setChildView(View childView) {
		addView(childView);
		setContentView(view);
		refreshDialogSize();
		return this;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == id_btn_OK) {
			if (onButtonClickListener != null) {
				onButtonClickListener.onOKClick(this);
			}
		} else if (id == id_btn_NO) {
			if (onButtonClickListener != null)
				onButtonClickListener.onNOClick(this);
			this.dismiss();
		}
	}

	/**
	 * 按钮点击事件
	 * 
	 * @author lzhn
	 * 
	 */
	public interface OnButtonClickListener {
		/**
		 * 点击确认按钮
		 * 
		 * @param dialog
		 */
		void onOKClick(MyDialog dialog);

		/**
		 * 点击取消按钮
		 * 
		 * @param dialog
		 */
		void onNOClick(MyDialog dialog);
	}

}
