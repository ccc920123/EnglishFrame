package com.jysd.englishframe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.jysd.englishframe.utils.CopyFileToSDcard;
import com.jysd.englishframe.utils.SDCardFileUtis;
import com.jysd.englishframe.utils.StringToBitmap;
import com.jysd.englishframe.utils.orcidentifyview.CameraParametersUtils;
import com.jysd.englishframe.utils.orcidentifyview.CameraSetting;
import com.jysd.englishframe.utils.orcidentifyview.ViewfinderView;
import com.jysd.englishframe.utils.orcidentifyview.VinInformation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @类名: ORCIdentifyActivity
 * @功能描述:
 * @作者: 陈渝金
 * @时间: 2016/9/21
 * @最后修改者:
 * @最后修改内容:
 */

public class ORCIdentifyActivity extends Activity implements SurfaceHolder.Callback, Camera.PreviewCallback, View.OnClickListener,CompoundButton.OnCheckedChangeListener {


    /**
     * 相机界面的3个类
     */
    @Bind(R.id.surfaceview_camera)
    SurfaceView surfaceView;
    @Bind(R.id.iv_camera_back)
    ImageView ivCameraBack;
    @Bind(R.id.iv_camera_flash)
    CheckBox ivCameraFlash;
    @Bind(R.id.imbtn_takepic)
    ImageButton imbtnTakepic;
    @Bind(R.id.bg_template_listView)
    RelativeLayout bgTemplateListView;
    @Bind(R.id.iv_scaner_line)
    ImageView ivScanerLine;
    @Bind(R.id.re)
    RelativeLayout re;
    private SurfaceHolder surfaceHolder;
    private Camera camera;

    /**
     * 拍照分辨率集合
     */
    private List<Integer> srcList = new ArrayList<Integer>();// 拍照分辨率集合
    /**
     * 屏幕相关参数
     */
    private DisplayMetrics dm = new DisplayMetrics();
    /**
     * 相机参数工具
     */
    private CameraParametersUtils cameraParametersUtils;
    /**
     * 相机的参数尺寸
     */
    private Camera.Size size;
    /**
     * 拍照获取的数据
     */
    private byte[] data;
    /**
     * 屏幕尺寸
     */
    private int srcWidth, srcHeight;
    /**
     * orc识别界面参数
     */
    private VinInformation wlci;
    /**
     * 中间那个green框框
     */
    public static ViewfinderView myViewfinderView;
    /**
     * 动态设置界面布局的 重要参数属性
     */
    private RelativeLayout.LayoutParams layoutParams;
    /**
     * 定时对焦
     */
    private TimerTask timer;
    private Timer time = new Timer();
    /**
     * 敏感区域,裁剪时候用到的参数
     */
    private int[] regionPos = new int[4];// 敏感区域

    private OcrIdentifyThreed orcIdentifyThreed;

    private static String  PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/EnglishFrame/ORCData";// 获取跟目录

    private TessBaseAPI baseApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_orcidentify);
        ButterKnife.bind(this);
        if(!SDCardFileUtis.isFile(PATH))
        {
            CopyFileToSDcard.copyFileToSDCard(this,PATH+"/eng.traineddata","eng.traineddata");
        }
         baseApi = new TessBaseAPI();//初始化orc工具api
        baseApi.init(PATH, "eng");
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        cameraParametersUtils = new CameraParametersUtils(this);
        srcWidth = cameraParametersUtils.srcWidth;
        srcHeight = cameraParametersUtils.srcHeight;
        wlci = new VinInformation();
        setUI();
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


    }

    /**
     * @方法名称: setUI
     * @方法详述: 设置预览界面的UI
     * @参数:
     * @返回值:
     * @异常抛出 Exception:
     * @异常抛出 NullPointerException:
     */

    private void setUI() {
        if (myViewfinderView != null) {
            myViewfinderView.destroyDrawingCache();
            re.removeView(myViewfinderView);
            myViewfinderView = null;
        }
        myViewfinderView = new ViewfinderView(this,
                wlci.fieldType, wlci.template.templateType);
        re.addView(myViewfinderView);

        if (srcWidth == surfaceView.getWidth()
                || surfaceView.getWidth() == 0) {

            layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.FILL_PARENT, srcHeight);
            surfaceView.setLayoutParams(layoutParams);

            // 右侧菜单栏的背景布局
            layoutParams = new RelativeLayout.LayoutParams(
                    (int) (0.18 * srcWidth), srcHeight);
            layoutParams.leftMargin = (int) (0.82 * srcWidth);
            layoutParams.topMargin = 0;
            bgTemplateListView.setLayoutParams(layoutParams);
            // 闪光灯按钮UI布局
            layoutParams = new RelativeLayout.LayoutParams(
                    (int) (srcWidth * 0.05), (int) (srcWidth * 0.05));
            layoutParams.leftMargin = (int) (srcWidth * 0.75);
            layoutParams.topMargin = (int) (srcHeight * 0.05);
            ivCameraFlash.setLayoutParams(layoutParams);

        } else if (srcWidth > surfaceView.getWidth()) {

            // 如果将虚拟硬件弹出则执行如下布局代码，相机预览分辨率不变压缩屏幕的高度
            int surfaceViewHeight = (surfaceView.getWidth() * srcHeight)
                    / srcWidth;
            layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.FILL_PARENT,
                    surfaceViewHeight);
            layoutParams.topMargin = (srcHeight - surfaceViewHeight) / 2;
            surfaceView.setLayoutParams(layoutParams);

            // 右侧菜单栏的背景布局
            layoutParams = new RelativeLayout.LayoutParams(
                    (int) (0.20 * srcWidth), srcHeight);
            layoutParams.leftMargin = (int) (0.80 * srcWidth)
                    - (srcWidth - surfaceView.getWidth());
            layoutParams.topMargin = 0;
            bgTemplateListView.setLayoutParams(layoutParams);
            // 闪光灯按钮UI布局
            layoutParams = new RelativeLayout.LayoutParams(
                    (int) (srcWidth * 0.05), (int) (srcWidth * 0.05));
            layoutParams.leftMargin = (int) (srcWidth * 0.75)
                    - (srcWidth - surfaceView.getWidth());
            layoutParams.topMargin = (int) (srcHeight * 0.05);
            ivCameraFlash.setLayoutParams(layoutParams);
        }
        imbtnTakepic.setOnClickListener(this);
// 扫描框的UI布局
        layoutParams = new RelativeLayout.LayoutParams(srcWidth, srcHeight);
        layoutParams.leftMargin = 0;
        layoutParams.topMargin = 0;
        myViewfinderView.setLayoutParams(layoutParams);
        // 返回按钮UI布局
        layoutParams = new RelativeLayout.LayoutParams(
                (int) (srcWidth * 0.05), (int) (srcWidth * 0.05));
        layoutParams.leftMargin = (int) (srcWidth * 0.05);
        layoutParams.topMargin = (int) (srcHeight * 0.05);
        ivCameraBack.setLayoutParams(layoutParams);
        ivCameraBack.setOnClickListener(this);
        ivCameraFlash.setOnCheckedChangeListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (camera != null) {
            camera = CameraSetting.getInstance(this).closeCamera(camera);
        }
    }

    @Override
    public void onBackPressed() {
        Intent  intent = new Intent( ORCIdentifyActivity.this,MainActivity.class);
        startActivity(intent);
        finish();


    }

    @Override
    protected void onDestroy() {

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (camera != null) {
            camera = CameraSetting.getInstance(this).closeCamera(camera);
        }
        if (myViewfinderView != null) {
            re.removeView(myViewfinderView);
            myViewfinderView.destroyDrawingCache();
            myViewfinderView.fieldsPosition = 0;
            myViewfinderView = null;
        }
        super.onDestroy();

    }


    /**
     * @方法名称: autoFocus
     * @方法详述: 自动对焦
     * @参数:
     * @返回值:
     * @异常抛出 Exception:
     * @异常抛出 NullPointerException:
     */

    public void autoFocus() {

        if (camera != null) {

            try {
                if (camera.getParameters().getSupportedFocusModes() != null
                        && camera.getParameters().getSupportedFocusModes()
                        .contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                    camera.autoFocus(new Camera.AutoFocusCallback() {
                        public void onAutoFocus(boolean success, Camera camera) {
                            if (success) {

                            }

                        }
                    });
                } else {

                    Toast.makeText(getBaseContext(),
                            getString(R.string.unsupport_auto_focus),
                            Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                camera.stopPreview();
                camera.startPreview();
                Toast.makeText(this, R.string.toast_autofocus_failure,
                        Toast.LENGTH_SHORT).show();

            }
        }
    }

    /**
     * 创建预览试图
     *
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (null == camera) {
                camera = Camera.open();
            }
            if (timer == null) {
                timer = new TimerTask() {
                    public void run() {
                        if (camera != null) {
                            try {
                                autoFocus();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                };
            }
            time.schedule(timer, 200, 2500);
            if (ivCameraFlash.isChecked() == false) {
                CameraSetting.getInstance(this).closedCameraFlash(camera);
            } else {
                CameraSetting.getInstance(this).openCameraFlash(camera);
            }
            orcIdentifyThreed = new OcrIdentifyThreed();//实例化扫描线程

        } catch (Exception e) {
        }
    }

    /**
     * @param holder
     * @param format
     * @param width
     * @param height
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        CameraSetting.getInstance(this).setCameraParameters(
                this, surfaceHolder,
                this, camera, (float) srcWidth / srcHeight,
                srcList, false);
    }

    /**
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /**
     * 相机预览时返回每帧的图片数据NV21格式编码
     *
     * @param databyte
     * @param camera
     */
    @Override
    public void onPreviewFrame(byte[] databyte, Camera camera) {
        data=databyte;
        synchronized (orcIdentifyThreed) {
            orcIdentifyThreed.run();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回按钮触发事件
            case R.id.iv_camera_back:
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }

                onBackPressed();

                break;

            // 拍照按钮触发事件
            case R.id.imbtn_takepic:
//           msg = new Message();
//           msg.what = 3;
//           handler.sendMessage(msg);

                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked == false) {
            CameraSetting.getInstance(this).closedCameraFlash(camera);
        } else {
            CameraSetting.getInstance(this).openCameraFlash(camera);
        }
    }


    /**
     * @类名: ORCIdentifyActivity
     * @功能描述: 识别线程
     * @作者: 陈渝金
     * @时间: 2016/9/23
     * @最后修改者:
     * @最后修改内容:
     */

    class OcrIdentifyThreed extends Thread {

        @Override
        public void run() {
            size = camera.getParameters().getPreviewSize();

            regionPos[0] = (int) (0.1 * size.width);
            regionPos[1] = (int) (0.4 * size.height);
            regionPos[2] = (int) ((0.1 + wlci.fieldType.width) * size.width);
            regionPos[3] = (int) ((0.4 + wlci.fieldType.height) * size.height);
           String imagePath= savePicture(data);//开始处理数据预览时的数据
            //
            getImageText(imagePath);


        }
    }
  /**@方法名称:
  * @方法详述:
  * @参数:
  * @返回值:
  * @异常抛出 Exception:
  * @异常抛出 NullPointerException:
  */

    private void getImageText(String path) {
        Bitmap bmp=StringToBitmap.convertToBitmap(path,wlci.fieldType.width,wlci.fieldType.height);
        baseApi.setImage(bmp);
        // 根据Init的语言，获得ocr后的字符串
        String text = baseApi.getUTF8Text();
        // 释放bitmap
        baseApi.clear();

        // 如果连续ocr多张图片，这个end可以不调用，但每次ocr之后，必须调用clear来对bitmap进行释放
        // 释放native内存
        baseApi.end();

        Toast.makeText(ORCIdentifyActivity.this,"数据"+text,Toast.LENGTH_LONG).show();

    }

    /**
     * @方法名称:
     * @方法详述: 得到保存图片的路径
     * @参数: 字节
     * @返回值: 图片路径
     * @异常抛出 Exception:
     * @异常抛出 NullPointerException:
     */

    public String savePicture(byte[] databyte) {
        String PATH = Environment.getExternalStorageDirectory().toString()
                + "/EnglishFrame/";
        File file = new File(PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        String picPath = PATH + "identify.jpg";
        saveImage(databyte, picPath);
        return picPath;
    }

    /**
     * @方法名称: saveImage
     * @方法详述: 保存每一帧的图片
     * @参数:
     * @返回值:
     * @异常抛出 Exception:
     * @异常抛出 NullPointerException:
     */

    public void saveImage(byte[] databyte, String pathString) {
        YuvImage yuvimage = new YuvImage(databyte, ImageFormat.NV21, size.width,
                size.height, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(new Rect(regionPos[0], regionPos[1],
                regionPos[2], regionPos[3]), 100, baos);
        FileOutputStream outStream;
        try {
            outStream = new FileOutputStream(pathString);
            outStream.write(baos.toByteArray());
            outStream.close();
            baos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch
            // block
            e.printStackTrace();
        }

    }
}
