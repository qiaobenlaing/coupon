package com.whhft.sysmanage.common.entity;

import javax.validation.constraints.Size;

import com.whhft.sysmanage.common.base.BaseEntity;

public class DictDefine extends BaseEntity {

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column dict_define.DICT_DEFINE_ID
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	private Integer dictDefineId;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column dict_define.DICT_DEFINE_KEY
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	@Size(min=6,max=10,message="不符合")
	private String dictDefineKey;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column dict_define.DICT_DEFINE_NAME
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	private String dictDefineName;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column dict_define.DICT_DEFINE_TYPE
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	private String dictDefineType;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column dict_define.DICT_DEFINE_VALUE
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	private String dictDefineValue;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column dict_define.REMARK
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	private String remark;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column dict_define.DICT_DEFINE_ID
	 * @return  the value of dict_define.DICT_DEFINE_ID
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	public Integer getDictDefineId() {
		return dictDefineId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column dict_define.DICT_DEFINE_ID
	 * @param dictDefineId  the value for dict_define.DICT_DEFINE_ID
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	public void setDictDefineId(Integer dictDefineId) {
		this.dictDefineId = dictDefineId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column dict_define.DICT_DEFINE_KEY
	 * @return  the value of dict_define.DICT_DEFINE_KEY
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	public String getDictDefineKey() {
		return dictDefineKey;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column dict_define.DICT_DEFINE_KEY
	 * @param dictDefineKey  the value for dict_define.DICT_DEFINE_KEY
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	public void setDictDefineKey(String dictDefineKey) {
		this.dictDefineKey = dictDefineKey;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column dict_define.DICT_DEFINE_NAME
	 * @return  the value of dict_define.DICT_DEFINE_NAME
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	public String getDictDefineName() {
		return dictDefineName;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column dict_define.DICT_DEFINE_NAME
	 * @param dictDefineName  the value for dict_define.DICT_DEFINE_NAME
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	public void setDictDefineName(String dictDefineName) {
		this.dictDefineName = dictDefineName;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column dict_define.DICT_DEFINE_TYPE
	 * @return  the value of dict_define.DICT_DEFINE_TYPE
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	public String getDictDefineType() {
		return dictDefineType;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column dict_define.DICT_DEFINE_TYPE
	 * @param dictDefineType  the value for dict_define.DICT_DEFINE_TYPE
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	public void setDictDefineType(String dictDefineType) {
		this.dictDefineType = dictDefineType;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column dict_define.DICT_DEFINE_VALUE
	 * @return  the value of dict_define.DICT_DEFINE_VALUE
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	public String getDictDefineValue() {
		return dictDefineValue;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column dict_define.DICT_DEFINE_VALUE
	 * @param dictDefineValue  the value for dict_define.DICT_DEFINE_VALUE
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	public void setDictDefineValue(String dictDefineValue) {
		this.dictDefineValue = dictDefineValue;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column dict_define.REMARK
	 * @return  the value of dict_define.REMARK
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column dict_define.REMARK
	 * @param remark  the value for dict_define.REMARK
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
}