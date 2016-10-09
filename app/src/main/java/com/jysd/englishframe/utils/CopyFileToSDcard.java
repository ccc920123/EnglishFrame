package com.jysd.englishframe.utils;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
/**@类名: CopyFileToSDcard
 * @功能描述: 将assets文件复制到sdcard
 * @作者: 陈渝金
 * @时间: 2016/10/8
 * @最后修改者:
 * @最后修改内容:
 */
public class CopyFileToSDcard {


	/**@方法名称: copyFileToSdCard
	* @方法详述: 复制文件
	* @参数: context，sdcardPath ：sd_card路径，filePath：assets文件
	* @返回值:
	* @异常抛出 Exception:
	* @异常抛出 NullPointerException:
	*/


	public static void copyFileToSDCard(Context context,String sdcardPath,String filePath){
//	Resources rou=context.getResources();
//    InputStream is =rou.openRawResource(id)(R.你的db);
		try {

			File mFile = new File(sdcardPath);
//			if (!mFile.exists()) {创建文件
//				File file2 = new File(mFile.getParent());
//				file2.mkdir();
//			}
			if (!mFile.exists()) {//创建文件夹
				mFile.getParentFile().mkdirs();
				mFile.createNewFile();
			}
			InputStream is= context.getAssets().open(filePath);

			FileOutputStream fos =new FileOutputStream(sdcardPath);
			byte[] buffer = new byte[1024];
			int count =0;
			// 开始复制db文件
			while ((count=is.read(buffer))>0){
				fos.write(buffer, 0, count);
			}
			fos.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
