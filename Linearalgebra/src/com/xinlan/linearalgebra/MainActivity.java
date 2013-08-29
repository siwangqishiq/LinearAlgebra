package com.xinlan.linearalgebra;

import com.xinlan.linearalgebra.core.XinlanMatrix;

import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class MainActivity extends Activity {
	private TextView content;
	private LinearLayout inputPanel;
	private Button resetMatrixBtn;
	private Context mContext;
	private Button compute;

	private MyTextEdit[][] inputs;
	private int row, col;

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

		row = 3;
		col = 4;
		reSet(row, col);
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
			LayoutInflater inflater = getLayoutInflater();
			View layout = inflater.inflate(R.layout.input, null);

			new AlertDialog.Builder(mContext)
					.setTitle("请您输入新矩阵的行数与列数")
					.setView(layout)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									AlertDialog ad = (AlertDialog) dialog;
									EditText rowText = (EditText) ad
											.findViewById(R.id.rowNum);
									EditText colText = (EditText) ad
											.findViewById(R.id.colNum);

									try {
										row = Integer.parseInt(rowText
												.getText().toString().trim());
										col = Integer.parseInt(colText
												.getText().toString().trim());
									} catch (Exception e) {
										Toast.makeText(mContext, "输入非法!",
												Toast.LENGTH_LONG).show();
										return;
									}

									if (row <= 0 || col <= 0) {
										Toast.makeText(mContext, "输入非法!",
												Toast.LENGTH_LONG).show();
										return;
									}

									if (row > col) {
										Toast.makeText(mContext,
												"程序目前只支持m<=n形式的矩阵，请将矩阵转置.",
												Toast.LENGTH_LONG).show();
										return;
									}

									reSet(row, col);
									content.setText("");
								}

							}).setNegativeButton("取消", null).show();

		}
	}// end inner class

	private final class DoTransform implements OnClickListener {
		@Override
		public void onClick(View v) {
			int[][] data = getData();
			if (data == null)
				return;

			XinlanMatrix matrix = new XinlanMatrix();
			matrix.setData(data);
			matrix.toLadderMatrix();
			String str1 = "阶梯矩阵:\n" + matrix.showString() + "\n\n" + "矩阵的秩:"
					+ matrix.rankOfMatrix() + "\n\n";

			boolean isRight = matrix.toRowSimplestMatrix();
			String str2 = "行最简矩阵:\n"
					+ (isRight ? matrix.showString() : matrix.showString());

			content.setText(str1 + str2);
		}
	}// end inner class

	/**
	 * 添加菜单内容
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(1, 1, 1, "发现BUG，联系作者");
		return true;
	}

	/**
	 * 菜单的事件响应
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			Uri uri = Uri.parse("smsto:13451865083");            
			Intent it = new Intent(Intent.ACTION_SENDTO, uri);            
			it.putExtra("sms_body", "BUG描述:");            
			this.startActivity(it);  
		}
		return true;
	}
}// end class
