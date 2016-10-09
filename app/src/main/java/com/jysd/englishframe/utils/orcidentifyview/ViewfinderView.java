package com.jysd.englishframe.utils.orcidentifyview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.jysd.englishframe.R;


/**@类名: ViewfinderView
 * @功能描述: 扫描框框
 * @作者: 陈渝金
 * @时间: 2016/9/23
 * @最后修改者:
 * @最后修改内容:
 */

public final class ViewfinderView extends View {

	/**
	 * 刷新界面的时间
	 */
	private static final long ANIMATION_DELAY = 25L;

	/**
	 * 四周边框的宽度
	 */
	private static final int FRAME_LINE_WIDTH = 4;
	private Rect frame;
	private int width;
	private int height;
	private Paint paint;
	public static int fieldsPosition = 0;// 输出结果的先后顺序
	public ConfigParamsModel configParamsModel;
	private Context context;
	private static final int[] SCANNER_ALPHA = { 0, 64 };
	private int scannerAlpha;
	private Bitmap bitmap;
	private float count = 1;
	private boolean isAdd = true;
	

	public ViewfinderView(Context context, ConfigParamsModel configParamsModels,
			String type) {
		super(context);
		paint = new Paint();
		configParamsModel = configParamsModels;
		this.context = context;
		scannerAlpha = 0;
		count = 1;
		isAdd = true;
		this.bitmap = BitmapFactory.decodeResource(getResources(),
				R.mipmap.scanline);
	}

	@Override
	public void onDraw(Canvas canvas) { 
		width = canvas.getWidth();
		height = canvas.getHeight();
		// System.out.println("刷新:"+fieldsPosition);
		String a = configParamsModel.nameTextColor
				.split("_")[0];
		String r = configParamsModel.nameTextColor
				.split("_")[1];
		String g = configParamsModel.nameTextColor
				.split("_")[2];
		String b = configParamsModel.nameTextColor
				.split("_")[3];
		paint.setColor(Color.argb(Integer.valueOf(a), Integer.valueOf(r),
				Integer.valueOf(g), Integer.valueOf(b)));
		paint.setTextSize(Float.valueOf(configParamsModel.nameTextSize));
		if (configParamsModel.leftPointX == 0.0
				&& configParamsModel.leftPointY == 0.0) {
			/**
			 * 这个矩形就是中间显示的那个框框
			 */
			frame = new Rect(
					(int) (0.1 * width),
					(int) (height * 0.4),
					(int) ((configParamsModel.width + 0.1) * width),
					(int) (height * (0.4 + configParamsModel
							.height)));
			
		} else {
			/**
			 * 这个矩形就是中间显示的那个框框
			 */
			frame = new Rect(
					(int) (configParamsModel.leftPointX * width),
					(int) (height * configParamsModel.leftPointY),
					(int) ((configParamsModel.leftPointX + configParamsModel
							.width) * width),
					(int) (height * (configParamsModel.leftPointY + configParamsModel
							.height)));
			

		}
		canvas.drawText("正在识别"
				+ configParamsModel.name + " ",
				configParamsModel.namePositionX * width,
				configParamsModel.namePositionY * height,
				paint);
		if (frame == null) {
			return;
		}
		// 画出扫描框外面的阴影部分，共四个部分，扫描框的上面到屏幕上面，扫描框的下面到屏幕下面
		// 扫描框的左边面到屏幕左边，扫描框的右边到屏幕右边
		paint.setColor(Color.argb(48, 0, 0, 0));
		canvas.drawRect(0, 0, width, frame.top, paint);
		canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
		canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
				paint);
		canvas.drawRect(0, frame.bottom + 1, width, height, paint);
		a = configParamsModel.color.split("_")[0];
		r = configParamsModel.color.split("_")[1];
		g = configParamsModel.color.split("_")[2];
		b = configParamsModel.color.split("_")[3];
		// 绘制两个像素边宽的绿色线框
		paint.setColor(Color.argb(Integer.valueOf(a), Integer.valueOf(r),
				Integer.valueOf(g), Integer.valueOf(b)));
		canvas.drawRect(frame.left + FRAME_LINE_WIDTH - 2, frame.top,
				frame.right - FRAME_LINE_WIDTH + 2, frame.top
						+ FRAME_LINE_WIDTH, paint);// 上边
		canvas.drawRect(frame.left + FRAME_LINE_WIDTH - 2, frame.top,
				frame.left + FRAME_LINE_WIDTH + 2, frame.bottom
						+ FRAME_LINE_WIDTH, paint);// 左边
		canvas.drawRect(frame.right - FRAME_LINE_WIDTH - 2, frame.top,
				frame.right - FRAME_LINE_WIDTH + 2, frame.bottom
						+ FRAME_LINE_WIDTH, paint);// 右边
		canvas.drawRect(frame.left + FRAME_LINE_WIDTH - 2, frame.bottom,
				frame.right - FRAME_LINE_WIDTH + 2, frame.bottom
						+ FRAME_LINE_WIDTH, paint);// 底边
		fresh();

	}

	public void fresh() {
		/**
		 * 当我们获得结果的时候，我们更新整个屏幕的内容
		 */

		postInvalidateDelayed(ANIMATION_DELAY, 0, 0, (int) (width * 0.8),
				height);

//		postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right,
//				frame.bottom);
	}
}
