package com.xinlan.linearalgebra;

import android.content.Context;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.widget.EditText;

public class MyTextEdit extends EditText{
	public MyTextEdit(Context context) {
		super(context);
		this.setKeyListener(new NumberListener());
	}
	public MyTextEdit(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setKeyListener(new NumberListener());
	}
	
	private final class NumberListener extends NumberKeyListener{

		@Override
		public int getInputType() {
			return 0;
		}

		@Override
		protected char[] getAcceptedChars() {
			char[] myChar={'0','1','2','3','4','5','6','7','8','9','-'};
			return myChar;
		}
	}

}
