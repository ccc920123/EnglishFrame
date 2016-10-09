package com.jysd.englishframe.utils.orcidentifyview;

/**@类名: VinInformation
 * @功能描述: 扫描的界面参数，盟版
 * @作者: 陈渝金
 * @时间: 2016/9/23
 * @最后修改者:
 * @最后修改内容:
 */

public class VinInformation
{
  public String[] typeStrings = { "", "", "", "", "", "", "", "", "", "" };
  public String[] duedateStrings = { "", "", "", "", "", "", "", "", "", "" };
  public String[] sumStrings = { "", "", "", "", "", "", "", "", "", "" };
  public String androidPlatform;
  public TempleModel template;
  public ConfigParamsModel fieldType;


  
  public VinInformation(){
	 androidPlatform="yes";
	 typeStrings[0]="17";
	  TempleModel templeModel = new TempleModel();
	  templeModel.templateType = "003";
      templeModel.templateName = "VIN码";
      templeModel.isSelected = true;
     template=templeModel;
      ConfigParamsModel model = new ConfigParamsModel();
      model.width = Float.valueOf("0.7").floatValue();
      model.height = Float.valueOf("0.15").floatValue();
      model.color = "255_159_242_74";
      model.name = "VIN码";
      model.nameTextSize = "40";
      model.nameTextColor = "255_255_255_255";
      model.namePositionX = Float.valueOf("0.38").floatValue();
      model.namePositionY = Float.valueOf("0.35").floatValue();
      model.ocrId = "SV_ID_VIN_CARWINDOW";
     fieldType=model;
  }
}