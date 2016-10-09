package com.jysd.englishframe.utils;

import java.io.File;

/**
 * @类名: SDCardFileUtis
 * @功能描述:
 * @作者: 陈渝金
 * @时间: 2016年10月8日10:30:52
 * @最后修改者:
 * @最后修改内容:
 */

public class SDCardFileUtis {
/**@方法名称: isFile
* @方法详述: 判断是否有文件
* @参数:  path   sdcard  路径
* @返回值: true   or   false
* @异常抛出 Exception:
* @异常抛出 NullPointerException:
*/

    public static boolean isFile(String path)
    {
        File pathFile=new File(path);
        if(pathFile.isFile())
        {
            return true;
        }else{
           return false;
        }

    }

}
