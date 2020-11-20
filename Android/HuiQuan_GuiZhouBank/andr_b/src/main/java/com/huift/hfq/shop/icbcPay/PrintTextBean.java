package com.huift.hfq.shop.icbcPay;

public class PrintTextBean {
    /***
     * 字体 0-small 1-normal 2-large
     */
    private int font;
    /***
     * 对齐方式 0-left 1-center 2-right
     */
    private int align;
    /***
     * 打印数据
     */
    private String textData;

    public int getFont() {
        return font;
    }

    public int getAlign() {
        return align;
    }

    public String getTextData() {
        return textData;
    }

    public void setFont(int font) {
        this.font = font;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public void setTextData(String textData) {
        this.textData = textData;
    }
}
