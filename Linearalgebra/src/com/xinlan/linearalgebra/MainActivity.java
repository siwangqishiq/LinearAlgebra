package com.xinlan.linearalgebra;

import com.xinlan.linearalgebra.core.XinlanMatrix;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;

public class MainActivity extends Activity {
	private TextView content;
	private LinearLayout inputPanel;
	private Button resetMatrixBtn;
	private Context mContext;
	private Button compute;

	private MyTextEdit[][] inputs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		content = (TextView) findViewById(R.id.content);
		inputPanel = (LinearLayout) findViewById(R.id.input_panel);
		resetMatrixBtn = (Button) findViewById(R.id.setMatrixBtn);
		compute = (Button) findViewById(R.id.compute);
		resetMatrixBtn.setOnClickListener(new ReSetMatrix());
		compute.setOnClickListener(new DoTransform());

		reSet(3, 4);
	}

	private void reSet(int m, int n) {
		inputs = null;
		inputs = new MyTextEdit[m][n];
		inputPanel.removeAllViews();
		System.gc();

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < m; i++) {
			LinearLayout colLayout = new LinearLayout(mContext);
			colLayout.setOrientation(LinearLayout.HORIZONTAL);
			for (int j = 0; j < n; j++) {
				MyTextEdit text = new MyTextEdit(mContext);
				text.setSingleLine(true);
				text.setInputType(InputType.TYPE_CLASS_PHONE);
				colLayout.addView(text, layoutParams);

				inputs[i][j] = text;
			}// end for j
			inputPanel.addView(colLayout);
		}// end for i
	}

	private int[][] getData() {
		if (inputs == null) {
			return null;
		}
		int m = inputs.length, n = inputs[0].length;
		int[][] retData = new int[m][n];
		for (int i = 0; i < inputs.length; i++) {
			for (int j = 0; j < inputs[i].length; j++) {
				String contentText = inputs[i][j].getText().toString().trim();
				if ("".equals(contentText) || contentText == null) {
					retData[i][j] = 0;
					inputs[i][j].setText("0");
				} else {
					try {
						retData[i][j] = Integer.parseInt(contentText);
					} catch (Exception e) {
						retData[i][j] = 0;
						inputs[i][j].setText("0");
					}
				}
			}// end for j
		}// end for i
		return retData;
	}

	private final class ReSetMatrix implements OnClickListener {
		@Override
		public void onClick(View v) {
			
		}
	}// end inner class

	private final class DoTransform implements OnClickListener {
		@Override
		public void onClick(View v) {
			int[][] data = getData();
			if(data==null) return;
			
			XinlanMatrix matrix = new XinlanMatrix();
			matrix.setData(data);
			matrix.toLadderMatrix();
			content.setText("½×ÌÝ¾ØÕó:\n"+matrix.showString());
		}
	}// end inner class
}// end class
