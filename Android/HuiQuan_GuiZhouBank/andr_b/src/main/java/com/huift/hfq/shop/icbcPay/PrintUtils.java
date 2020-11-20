package com.huift.hfq.shop.icbcPay;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class PrintUtils {

    public static String assemble_ADDITIONAL_INFO(String bill_info, String cust_dingdan_no) {
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("bill_info", bill_info);
            jsonObject.put("cust_dingdan_no", cust_dingdan_no);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /***
     * 获取打印文本内容
     * @param font 字体 0-small 1-normal 2-large
     * @param align 对齐方式 0-left 1-center 2-right
     * @param data 文本信息
     * @return String
     */
    public static String assemble_text_print(int font, int align, String data){
        JSONObject jsonObject_text = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj = new JSONObject();
        try{
            jsonObject_text.put("font", font);
            jsonObject_text.put("align", align);
            jsonObject_text.put("data", data);
            jsonArray.put(jsonObject_text);
            jsonObj.put("type", "1");
            jsonObj.put("text", jsonArray);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    /***
     * 获取打印文本内容
     * @param printTexts 待打印文本集合
     * @return String
     */
    public static String assemble_text_print(ArrayList<PrintTextBean> printTexts){

        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try{
            int font;
            int align;
            String textData;
            for (int i=0;i<printTexts.size();i++){
                JSONObject jsonObject_text = new JSONObject();
                font = printTexts.get(i).getFont();
                align = printTexts.get(i).getAlign();
                textData = printTexts.get(i).getTextData();

                jsonObject_text.put("font", font);
                jsonObject_text.put("align", align);
                jsonObject_text.put("data", textData);

                jsonArray.put(jsonObject_text);
            }
            jsonObj.put("type", "1");
            jsonObj.put("text", jsonArray);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    /***
     * 获取打印图片
     * @param offset 横向起始位置。单位：像素点
     * @param width 图片打印宽度。单位：像素点 (由于不同厂家对打印逻辑处理不一致，请尽量传入原图片的长宽)
     * @param height 图片打印高度。单位：像素点 (由于不同厂家对打印逻辑处理不一致，请尽量传入原图片的长宽)
     * @param image 图片数据。
     * @return String
     */
    public static String assemble_bmp_print(int offset,int width,int height,Bitmap image){
        HashMap<String,String> map = new HashMap<>();
        map.put("type","2");
        HashMap<String,String> map_text = new LinkedHashMap<>();
        map_text.put("offset",String.valueOf(offset));
        map_text.put("width",String.valueOf(width));
        map_text.put("height",String.valueOf(height));
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArray_bmp = new JSONArray();
        JSONObject jsonObj_data = new JSONObject();

        try {
            byte[] imageBytes = image2byte(image);
            for(int i = 0 ; i < imageBytes.length ;i++)  //依次将数组元素添加进JSONArray对象中
                jsonArray_bmp.put((int)imageBytes[i]);
            jsonObj_data.put("offset", String.valueOf(offset));
            jsonObj_data.put("width", String.valueOf(width));
            jsonObj_data.put("height", String.valueOf(height));
            jsonObj_data.put("data", jsonArray_bmp);

            jsonObj.put("type", "2");
            jsonObj.put("image", jsonObj_data);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    /***
     * 获取打印图片
     * @param offset 横向起始位置。单位：像素点
     * @param width 图片打印宽度。单位：像素点 (由于不同厂家对打印逻辑处理不一致，请尽量传入原图片的长宽)
     * @param height 图片打印高度。单位：像素点 (由于不同厂家对打印逻辑处理不一致，请尽量传入原图片的长宽)
     * @param imageBytes 图片数据。
     * @return String
     */
    public static String assemble_bmp_print(int offset, int width, int height, byte[] imageBytes){
        HashMap<String,String> map = new HashMap<>();
        map.put("type","2");
        HashMap<String,String> map_text = new LinkedHashMap<>();
        map_text.put("offset",String.valueOf(offset));
        map_text.put("width",String.valueOf(width));
        map_text.put("height",String.valueOf(height));
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArray_bmp = new JSONArray();
        JSONObject jsonObj_data = new JSONObject();

        try {
            for (byte imageByte : imageBytes){
                jsonArray_bmp.put((int) imageByte);
            }
            jsonObj_data.put("offset", String.valueOf(offset));
            jsonObj_data.put("width", String.valueOf(width));
            jsonObj_data.put("height", String.valueOf(height));
            jsonObj_data.put("data", jsonArray_bmp);

            jsonObj.put("type", "2");
            jsonObj.put("image", jsonObj_data);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    /***
     * 获取打印一维条码
     * @param align 对齐方式。0-left 1-center 2-right
     * @param width 条码宽度。单位：像素点
     * @param height 条码高度。单位：像素点
     * @param barcode 条形码数据
     * @return String
     */
    public static String assemble_barcode_print(int align, int width, int height, String barcode){
        JSONObject jsonObject_brdata = new JSONObject();
        JSONObject jsonObj = new JSONObject();
        try{
            jsonObject_brdata.put("align", align);
            jsonObject_brdata.put("width", width);
            jsonObject_brdata.put("height", height);
            jsonObject_brdata.put("data", barcode);

            jsonObj.put("type", "3");
            jsonObj.put("barcode", jsonObject_brdata);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    /***
     * 获取打印二维码
     * @param offset 横向起始位置。单位：像素点
     * @param length 二维码的边长。单位：像素点
     * @param qrcode 二维码数据
     * @return String
     */
    public static String assemble_qrcode_print(int offset, int length, String qrcode){
        JSONObject jsonObject_qrdata = new JSONObject();
        JSONObject jsonObj = new JSONObject();
        try{
            jsonObject_qrdata.put("offset", offset);
            jsonObject_qrdata.put("length", length);
            jsonObject_qrdata.put("data", qrcode);

            jsonObj.put("type", "4");
            jsonObj.put("qrcode", jsonObject_qrdata);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    /**
     * bitmap转byte
     */
    public static byte[] image2byte(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap bytes2image(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

}
