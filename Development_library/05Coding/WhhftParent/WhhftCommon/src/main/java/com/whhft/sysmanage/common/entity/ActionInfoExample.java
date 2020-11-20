package com.whhft.sysmanage.common.entity;

import java.util.ArrayList;
import java.util.List;

public class ActionInfoExample {
    /**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table action_info
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	protected String orderByClause;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table action_info
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	protected boolean distinct;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table action_info
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	protected List<Criteria> oredCriteria;

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table action_info
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	public ActionInfoExample() {
		oredCriteria = new ArrayList<Criteria>();
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table action_info
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table action_info
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	public String getOrderByClause() {
		return orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table action_info
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table action_info
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	public boolean isDistinct() {
		return distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table action_info
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	public List<Criteria> getOredCriteria() {
		return oredCriteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table action_info
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	public void or(Criteria criteria) {
		oredCriteria.add(criteria);
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table action_info
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	public Criteria or() {
		Criteria criteria = createCriteriaInternal();
		oredCriteria.add(criteria);
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table action_info
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	public Criteria createCriteria() {
		Criteria criteria = createCriteriaInternal();
		if (oredCriteria.size() == 0) {
			oredCriteria.add(criteria);
		}
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table action_info
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	protected Criteria createCriteriaInternal() {
		Criteria criteria = new Criteria();
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table action_info
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	public void clear() {
		oredCriteria.clear();
		orderByClause = null;
		distinct = false;
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table action_info
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	protected abstract static class GeneratedCriteria {
		protected List<Criterion> criteria;

		protected GeneratedCriteria() {
			super();
			criteria = new ArrayList<Criterion>();
		}

		public boolean isValid() {
			return criteria.size() > 0;
		}

		public List<Criterion> getAllCriteria() {
			return criteria;
		}

		public List<Criterion> getCriteria() {
			return criteria;
		}

		protected void addCriterion(String condition) {
			if (condition == null) {
				throw new RuntimeException("Value for condition cannot be null");
			}
			criteria.add(new Criterion(condition));
		}

		protected void addCriterion(String condition, Object value, String property) {
			if (value == null) {
				throw new RuntimeException("Value for " + property + " cannot be null");
			}
			criteria.add(new Criterion(condition, value));
		}

		protected void addCriterion(String condition, Object value1, Object value2, String property) {
			if (value1 == null || value2 == null) {
				throw new RuntimeException("Between values for " + property + " cannot be null");
			}
			criteria.add(new Criterion(condition, value1, value2));
		}

		public Criteria andActionIdIsNull() {
			addCriterion("ACTION_ID is null");
			return (Criteria) this;
		}

		public Criteria andActionIdIsNotNull() {
			addCriterion("ACTION_ID is not null");
			return (Criteria) this;
		}

		public Criteria andActionIdEqualTo(Integer value) {
			addCriterion("ACTION_ID =", value, "actionId");
			return (Criteria) this;
		}

		public Criteria andActionIdNotEqualTo(Integer value) {
			addCriterion("ACTION_ID <>", value, "actionId");
			return (Criteria) this;
		}

		public Criteria andActionIdGreaterThan(Integer value) {
			addCriterion("ACTION_ID >", value, "actionId");
			return (Criteria) this;
		}

		public Criteria andActionIdGreaterThanOrEqualTo(Integer value) {
			addCriterion("ACTION_ID >=", value, "actionId");
			return (Criteria) this;
		}

		public Criteria andActionIdLessThan(Integer value) {
			addCriterion("ACTION_ID <", value, "actionId");
			return (Criteria) this;
		}

		public Criteria andActionIdLessThanOrEqualTo(Integer value) {
			addCriterion("ACTION_ID <=", value, "actionId");
			return (Criteria) this;
		}

		public Criteria andActionIdIn(List<Integer> values) {
			addCriterion("ACTION_ID in", values, "actionId");
			return (Criteria) this;
		}

		public Criteria andActionIdNotIn(List<Integer> values) {
			addCriterion("ACTION_ID not in", values, "actionId");
			return (Criteria) this;
		}

		public Criteria andActionIdBetween(Integer value1, Integer value2) {
			addCriterion("ACTION_ID between", value1, value2, "actionId");
			return (Criteria) this;
		}

		public Criteria andActionIdNotBetween(Integer value1, Integer value2) {
			addCriterion("ACTION_ID not between", value1, value2, "actionId");
			return (Criteria) this;
		}

		public Criteria andActionNameIsNull() {
			addCriterion("ACTION_NAME is null");
			return (Criteria) this;
		}

		public Criteria andActionNameIsNotNull() {
			addCriterion("ACTION_NAME is not null");
			return (Criteria) this;
		}

		public Criteria andActionNameEqualTo(String value) {
			addCriterion("ACTION_NAME =", value, "actionName");
			return (Criteria) this;
		}

		public Criteria andActionNameNotEqualTo(String value) {
			addCriterion("ACTION_NAME <>", value, "actionName");
			return (Criteria) this;
		}

		public Criteria andActionNameGreaterThan(String value) {
			addCriterion("ACTION_NAME >", value, "actionName");
			return (Criteria) this;
		}

		public Criteria andActionNameGreaterThanOrEqualTo(String value) {
			addCriterion("ACTION_NAME >=", value, "actionName");
			return (Criteria) this;
		}

		public Criteria andActionNameLessThan(String value) {
			addCriterion("ACTION_NAME <", value, "actionName");
			return (Criteria) this;
		}

		public Criteria andActionNameLessThanOrEqualTo(String value) {
			addCriterion("ACTION_NAME <=", value, "actionName");
			return (Criteria) this;
		}

		public Criteria andActionNameLike(String value) {
			addCriterion("ACTION_NAME like", value, "actionName");
			return (Criteria) this;
		}

		public Criteria andActionNameNotLike(String value) {
			addCriterion("ACTION_NAME not like", value, "actionName");
			return (Criteria) this;
		}

		public Criteria andActionNameIn(List<String> values) {
			addCriterion("ACTION_NAME in", values, "actionName");
			return (Criteria) this;
		}

		public Criteria andActionNameNotIn(List<String> values) {
			addCriterion("ACTION_NAME not in", values, "actionName");
			return (Criteria) this;
		}

		public Criteria andActionNameBetween(String value1, String value2) {
			addCriterion("ACTION_NAME between", value1, value2, "actionName");
			return (Criteria) this;
		}

		public Criteria andActionNameNotBetween(String value1, String value2) {
			addCriterion("ACTION_NAME not between", value1, value2, "actionName");
			return (Criteria) this;
		}

		public Criteria andActionRemarkIsNull() {
			addCriterion("ACTION_REMARK is null");
			return (Criteria) this;
		}

		public Criteria andActionRemarkIsNotNull() {
			addCriterion("ACTION_REMARK is not null");
			return (Criteria) this;
		}

		public Criteria andActionRemarkEqualTo(String value) {
			addCriterion("ACTION_REMARK =", value, "actionRemark");
			return (Criteria) this;
		}

		public Criteria andActionRemarkNotEqualTo(String value) {
			addCriterion("ACTION_REMARK <>", value, "actionRemark");
			return (Criteria) this;
		}

		public Criteria andActionRemarkGreaterThan(String value) {
			addCriterion("ACTION_REMARK >", value, "actionRemark");
			return (Criteria) this;
		}

		public Criteria andActionRemarkGreaterThanOrEqualTo(String value) {
			addCriterion("ACTION_REMARK >=", value, "actionRemark");
			return (Criteria) this;
		}

		public Criteria andActionRemarkLessThan(String value) {
			addCriterion("ACTION_REMARK <", value, "actionRemark");
			return (Criteria) this;
		}

		public Criteria andActionRemarkLessThanOrEqualTo(String value) {
			addCriterion("ACTION_REMARK <=", value, "actionRemark");
			return (Criteria) this;
		}

		public Criteria andActionRemarkLike(String value) {
			addCriterion("ACTION_REMARK like", value, "actionRemark");
			return (Criteria) this;
		}

		public Criteria andActionRemarkNotLike(String value) {
			addCriterion("ACTION_REMARK not like", value, "actionRemark");
			return (Criteria) this;
		}

		public Criteria andActionRemarkIn(List<String> values) {
			addCriterion("ACTION_REMARK in", values, "actionRemark");
			return (Criteria) this;
		}

		public Criteria andActionRemarkNotIn(List<String> values) {
			addCriterion("ACTION_REMARK not in", values, "actionRemark");
			return (Criteria) this;
		}

		public Criteria andActionRemarkBetween(String value1, String value2) {
			addCriterion("ACTION_REMARK between", value1, value2, "actionRemark");
			return (Criteria) this;
		}

		public Criteria andActionRemarkNotBetween(String value1, String value2) {
			addCriterion("ACTION_REMARK not between", value1, value2, "actionRemark");
			return (Criteria) this;
		}

		public Criteria andActionUrlIsNull() {
			addCriterion("ACTION_URL is null");
			return (Criteria) this;
		}

		public Criteria andActionUrlIsNotNull() {
			addCriterion("ACTION_URL is not null");
			return (Criteria) this;
		}

		public Criteria andActionUrlEqualTo(String value) {
			addCriterion("ACTION_URL =", value, "actionUrl");
			return (Criteria) this;
		}

		public Criteria andActionUrlNotEqualTo(String value) {
			addCriterion("ACTION_URL <>", value, "actionUrl");
			return (Criteria) this;
		}

		public Criteria andActionUrlGreaterThan(String value) {
			addCriterion("ACTION_URL >", value, "actionUrl");
			return (Criteria) this;
		}

		public Criteria andActionUrlGreaterThanOrEqualTo(String value) {
			addCriterion("ACTION_URL >=", value, "actionUrl");
			return (Criteria) this;
		}

		public Criteria andActionUrlLessThan(String value) {
			addCriterion("ACTION_URL <", value, "actionUrl");
			return (Criteria) this;
		}

		public Criteria andActionUrlLessThanOrEqualTo(String value) {
			addCriterion("ACTION_URL <=", value, "actionUrl");
			return (Criteria) this;
		}

		public Criteria andActionUrlLike(String value) {
			addCriterion("ACTION_URL like", value, "actionUrl");
			return (Criteria) this;
		}

		public Criteria andActionUrlNotLike(String value) {
			addCriterion("ACTION_URL not like", value, "actionUrl");
			return (Criteria) this;
		}

		public Criteria andActionUrlIn(List<String> values) {
			addCriterion("ACTION_URL in", values, "actionUrl");
			return (Criteria) this;
		}

		public Criteria andActionUrlNotIn(List<String> values) {
			addCriterion("ACTION_URL not in", values, "actionUrl");
			return (Criteria) this;
		}

		public Criteria andActionUrlBetween(String value1, String value2) {
			addCriterion("ACTION_URL between", value1, value2, "actionUrl");
			return (Criteria) this;
		}

		public Criteria andActionUrlNotBetween(String value1, String value2) {
			addCriterion("ACTION_URL not between", value1, value2, "actionUrl");
			return (Criteria) this;
		}
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table action_info
	 * @mbg.generated  Thu Mar 23 17:35:08 CST 2017
	 */
	public static class Criterion {
		private String condition;
		private Object value;
		private Object secondValue;
		private boolean noValue;
		private boolean singleValue;
		private boolean betweenValue;
		private boolean listValue;
		private String typeHandler;

		public String getCondition() {
			return condition;
		}

		public Object getValue() {
			return value;
		}

		public Object getSecondValue() {
			return secondValue;
		}

		public boolean isNoValue() {
			return noValue;
		}

		public boolean isSingleValue() {
			return singleValue;
		}

		public boolean isBetweenValue() {
			return betweenValue;
		}

		public boolean isListValue() {
			return listValue;
		}

		public String getTypeHandler() {
			return typeHandler;
		}

		protected Criterion(String condition) {
			super();
			this.condition = condition;
			this.typeHandler = null;
			this.noValue = true;
		}

		protected Criterion(String condition, Object value, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.typeHandler = typeHandler;
			if (value instanceof List<?>) {
				this.listValue = true;
			} else {
				this.singleValue = true;
			}
		}

		protected Criterion(String condition, Object value) {
			this(condition, value, null);
		}

		protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.secondValue = secondValue;
			this.typeHandler = typeHandler;
			this.betweenValue = true;
		}

		protected Criterion(String condition, Object value, Object secondValue) {
			this(condition, value, secondValue, null);
		}
	}

	/**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table action_info
     *
     * @mbg.generated do_not_delete_during_merge Wed Mar 15 10:17:21 CST 2017
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }
}