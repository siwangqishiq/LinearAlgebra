package com.xinlan.linearalgebra.core;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Panyi
 * 
 */
public class XinlanMatrix {
	private int m, n;
	private int[][] data;

	private boolean isDoTransposition = false;

	public XinlanMatrix() {
	}

	public XinlanMatrix(int m, int n) {
		this.m = m;
		this.n = n;
		data = new int[m][n];
	}

	public void setData(int[][] data) {
		this.data = data;
	}

	public void show() {
		DecimalFormat df = new DecimalFormat("%10s");
		for (int i = 0, row = data.length; i < row; i++) {
			for (int j = 0, col = data[i].length; j < col; j++) {
				System.out.print(String.format("%10s", data[i][j]));
			}// end for j
			System.out.println();
		}// end for i
		System.out
				.println("<---*****************************************---->");
	}
	
	public String showString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0, row = data.length; i < row; i++) {
			for (int j = 0, col = data[i].length; j < col; j++) {
				sb.append(String.format("%12s", data[i][j]));
			}// end for j
			sb.append("\n\n");
		}// end for i
		return sb.toString();
	}

	private static int[] doReduction(int[] a) {
		int startIndex = 0;
		for (int i = 0, acount = a.length; i < acount; i++) {
			if (a[i] != 0) {
				startIndex = i;
				break;
			}
		}
		List<Integer> same = factorAnalysis(a[startIndex]);
		for (int i = 0, acount = a.length; i < acount; i++) {
			if (a[i] != 0)
				same.retainAll(factorAnalysis(a[i]));
		}// end for i

		for (int i = 0; i < same.size(); i++) {
			if (isCanDiv(a, same.get(i))) {
				divArray(a, same.get(i));
			}
		}// end for
		return a;
	}

	private static boolean isCanDiv(int a[], int divNum) {
		for (int i = 0, length = a.length; i < length; i++) {
			if (a[i] % divNum != 0) {
				return false;
			}
		}
		return true;
	}

	private static void divArray(int a[], int divNum) {
		for (int i = 0, length = a.length; i < length; i++) {
			a[i] = a[i] / divNum;
		}
	}

	private static List<Integer> factorAnalysis(int number) {
		List<Integer> ret = new ArrayList<Integer>();
		if (number == 0) {
			return ret;
		}

		if (number < 0) {
			ret.add(-1);
			number = Math.abs(number);
		}

		int start = 1;
		while (number != 1) {
			if (number % start == 0) {
				if (start != 1) {
					ret.add(start);
					number = number / start;
				} else {
					start++;
				}
			} else {
				start++;
			}
		}// end while
		return ret;
	}

	/**
	 * 交换矩阵行
	 * 
	 * @param src
	 * @param dst
	 */
	private void swapRows(int src, int dst) {
		int row_num = data.length;
		if (src < 0 || src >= row_num || dst < 0 || dst >= row_num)
			return;
		for (int i = 0, col = data[0].length; i < col; i++) {
			int temp;
			temp = data[src][i];
			data[src][i] = data[dst][i];
			data[dst][i] = temp;
		}// end fori
	}

	private boolean isColAllZero(int index, int from) {
		int colNum = data[0].length;
		int rowNum = data.length;

		if (index < 0 || index >= colNum)
			return false;
		if (from < 0 || from >= rowNum)
			return false;

		for (int i = from; i < rowNum; i++) {
			if (data[i][index] != 0)
				return false;
		}// end for

		return true;
	}

	private boolean isRowAllZero(int rowIndex, int from) {
		int rowNum = data.length;
		int colNum = data[0].length;
		if (rowIndex < 0 || rowIndex >= rowNum)
			return false;
		if (from < 0 || from >= colNum)
			return false;

		for (int i = from; i < colNum; i++) {
			if (data[rowIndex][i] != 0)
				return false;
		}// end for
		return true;
	}

	public int rankOfMatrix() {
		int ret = 0;
		// 保证data[][]为阶梯型矩阵
		for (int i = 0, rowNum = data.length; i < rowNum; i++) {
			if (!isRowAllZero(i, 0))
				ret++;
		}// end for
		return ret;
	}

	/**
	 * 转置矩阵
	 */
	public void transpositionMatrix() {
		int origin_rowNum = data.length, origin_colNum = data[0].length;

		int[][] newMatrix = new int[origin_colNum][origin_rowNum];

		for (int i = 0; i < origin_rowNum; i++) {
			for (int j = 0; j < origin_colNum; j++) {
				newMatrix[j][i] = data[i][j];
			}
		}
		data = newMatrix;
		isDoTransposition = !isDoTransposition;
	}

	/**
	 * 转化为行最简矩阵
	 */
	public boolean toRowSimplestMatrix() {
		int doRowsNum = rankOfMatrix();
		int colNum = data[0].length;
		for (int i = 0; i < doRowsNum; i++) {
			for (int j = 0; j < colNum; j++) {
				if (data[i][j] != 0) {
					// 找到第一个不为0的元素
					int x = j;
					int[] base = data[i];
					for (int begin = 0; begin < i; begin++) {
						if (data[begin][x] == 0) {
							continue;
						} else {// 执行化简
							int temp[] = new int[colNum];
							System.arraycopy(base, 0, temp, 0, colNum);//
							int multiTemp = data[begin][x];
							int multi = data[i][j];
							for (int index = 0, length = temp.length; index < length; index++) {
								temp[index] *= multiTemp;
								data[begin][index] *= multi;
								data[begin][index] -= temp[index];
							}// end for
						}
					}// end for
					break;
				}
			}// end for j
		}// end for

		for (int i = 0; i < data.length; i++) {
			doReduction(data[i]);// 约分矩阵行
		}// end for i

		int error = 0;
		for (int i = 0; i < doRowsNum; i++) {
			for (int j = 0; j < colNum; j++) {
				int value = data[i][j];
				if (value != 0) {
					if (data[i][j] != 1) {
						for (int k = 0; k < colNum; k++) {
							if (data[i][k] % value == 0) {
								data[i][k] = data[i][k] / value;
							} else {
								error++;
							}
						}// end for k
					}// end if
					break;
				}
			}// end for j
		}// end for

		return error == 0 ? true : false;
	}

	public int[][] getData() {
		return data;
	}

	public void toLadderMatrix() {
		if (data == null) {
			return;
		}
		int rowNum = data.length, colNum = data[0].length;
		if (rowNum > colNum) {
			transpositionMatrix();
			rowNum = data.length;
			colNum = data[0].length;
		}

		// m<n
		// TODO
		for (int startY = 0, startX = 0; startY < rowNum; startY++) {
			if (startX >= colNum) {// 底部元素全为0 退出循环
				break;
			}

			if (data[startY][startX] == 0) {
				int first_none_zero = -1;
				for (int i = startY + 1; i < rowNum; i++) {
					if (data[i][startX] != 0) {
						first_none_zero = i;
						break;
					}
				}// end for i
				if (first_none_zero == -1) {
					startX++;
					startY--;
					if (startX >= colNum) {// 底部元素全为0 退出循环
						break;
					}
				} else {
					swapRows(startY, first_none_zero);
				}
			}

			if (startY < 0)
				continue;
			int base[] = data[startY];
			doReduction(base);
			for (int i = startY + 1; i < rowNum; i++) {
				int baseMulti = data[i][startX];
				int dataMulti = base[startX];
				for (int j = startX, colsNum = data[startY].length; j < colsNum; j++) {
					if (baseMulti == 0) {
						continue;
					}
					base[j] *= baseMulti;
					data[i][j] *= dataMulti;
					data[i][j] -= base[j];
				}// end for j
				doReduction(data[i]);// 约分矩阵行
			}// end for i
			startX++;

			// show();
		}// end for main

		for (int i = 0; i < data.length; i++) {
			doReduction(data[i]);// 约分矩阵行
		}// end for i
	}

	/**
	 * 转化为阶梯型矩阵
	 */
	@Deprecated
	public void toLadderMatrixSomeError() {
		if (data == null) {
			return;
		}

		for (int startX = 0, startY = 0, rowsNum = data.length, side = data[0].length; startY < rowsNum
				&& startX < side; startY++, startX++) {
			if (data[startX][startY] == 0) {
				System.out.println("首个数字为0");
				int first_none_zero = -1;
				for (int i = startX + 1; i < data.length; i++) {
					if (data[i][startX] != 0) {
						first_none_zero = i;
						break;
					}
				}// end for i
				if (first_none_zero == -1) {
					startX++;
				} else {
					swapRows(startY, first_none_zero);
				}
			}

			if (startY >= rowsNum - 1) {
				doReduction(data[startY]);
				return;
			}
			int base[] = data[startY];
			for (int i = startY + 1; i < rowsNum; i++) {
				int baseMulti = data[i][startX];
				int dataMulti = base[startX];
				for (int j = startX, colNum = data[startY].length; j < colNum; j++) {
					if (baseMulti == 0) {
						continue;
					}
					base[j] *= baseMulti;
					data[i][j] *= dataMulti;
					data[i][j] -= base[j];
				}// end for j
					// System.out.println("-->" + startX);
				// show();
			}// end for i
			doReduction(base);// 约分矩阵行
		}// end for
	}

	public static void main(String[] agrs) {
		XinlanMatrix matrix = new XinlanMatrix(10, 10);
		// int[][] a = { { 1, -1, 3, -4, 3 }, { 3, -3, 5, -4, 1 },
		// { 2, -2, 3, -2, 0 }, { 3, -3, 4, -2, -1 } };
		//
//		 int[][] a = { { 2, -1, -1, 1, 2 }, { 1, 1, -2, 1, 4 },
//		 { 4, -6, 2, -2, 4 }, { 3, 6, -9, 7, 9 } };
		// int[][] a={{1,2,3},{4,5,6},{7,8,9},{3,2,1},{100,10,1}};
		// int[][] a={{1,2,3},{1,2,3},{1,2,3},{1,2,3},{1,2,3}};
		// int[][] a={{1,0,0},{0,1,0}};
		// int[][] a = { { 0, 0, 0 }, { 0, 0, 0 } ,{0,0,1}};
		// int[][] a = { { 1, 1, 1, 6 }, { 1,1,-1,0 },
		// { 1,-1,1,2}};
		// int[][] a = { { 1, 2, 0, 0,1 }, { 0,6,2,4,10
		// },{1,11,3,6,16},{1,-19,-7,-14,-34}
		// };
		// int[][] a = {{0,0,1},{1,0,0},{0,1,0}};
		// int[][] a = { { 1, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } };
		// int[][] a = { { 3, -2, 1,0,1 }, { 0, 3, -2,1,0 }, { 0, 0, 0,0,-2
		// },{0,0,0,0,0} };
		// int[][] a = { {1,2,-1},{0,1,1},{2,5,-1} };
//		int[][] a = { { 1, 2, 2 }, { 2, 1, -2 }, { 2, -2, 1 } };
		 int[][] a = { { 3, 2, 1 ,6}, { 5,7,8,20}, { 1,-3,1,-1 } };
		matrix.setData(a);
		matrix.show();
		// matrix.toLadderMatrix();
		System.out.println();
		System.out.println();
		matrix.toLadderMatrix();
		matrix.show();

		System.out.println();
		System.out.println();
		boolean isRight=matrix.toRowSimplestMatrix();
		matrix.show();

		System.out.println("秩-->" + matrix.rankOfMatrix()+","+isRight);
	}

}// end class
