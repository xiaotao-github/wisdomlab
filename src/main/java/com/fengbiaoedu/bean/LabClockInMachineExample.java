package com.fengbiaoedu.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LabClockInMachineExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public LabClockInMachineExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

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

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andClockinIdIsNull() {
            addCriterion("clockin_id is null");
            return (Criteria) this;
        }

        public Criteria andClockinIdIsNotNull() {
            addCriterion("clockin_id is not null");
            return (Criteria) this;
        }

        public Criteria andClockinIdEqualTo(String value) {
            addCriterion("clockin_id =", value, "clockinId");
            return (Criteria) this;
        }

        public Criteria andClockinIdNotEqualTo(String value) {
            addCriterion("clockin_id <>", value, "clockinId");
            return (Criteria) this;
        }

        public Criteria andClockinIdGreaterThan(String value) {
            addCriterion("clockin_id >", value, "clockinId");
            return (Criteria) this;
        }

        public Criteria andClockinIdGreaterThanOrEqualTo(String value) {
            addCriterion("clockin_id >=", value, "clockinId");
            return (Criteria) this;
        }

        public Criteria andClockinIdLessThan(String value) {
            addCriterion("clockin_id <", value, "clockinId");
            return (Criteria) this;
        }

        public Criteria andClockinIdLessThanOrEqualTo(String value) {
            addCriterion("clockin_id <=", value, "clockinId");
            return (Criteria) this;
        }

        public Criteria andClockinIdLike(String value) {
            addCriterion("clockin_id like", value, "clockinId");
            return (Criteria) this;
        }

        public Criteria andClockinIdNotLike(String value) {
            addCriterion("clockin_id not like", value, "clockinId");
            return (Criteria) this;
        }

        public Criteria andClockinIdIn(List<String> values) {
            addCriterion("clockin_id in", values, "clockinId");
            return (Criteria) this;
        }

        public Criteria andClockinIdNotIn(List<String> values) {
            addCriterion("clockin_id not in", values, "clockinId");
            return (Criteria) this;
        }

        public Criteria andClockinIdBetween(String value1, String value2) {
            addCriterion("clockin_id between", value1, value2, "clockinId");
            return (Criteria) this;
        }

        public Criteria andClockinIdNotBetween(String value1, String value2) {
            addCriterion("clockin_id not between", value1, value2, "clockinId");
            return (Criteria) this;
        }

        public Criteria andLabIdIsNull() {
            addCriterion("lab_id is null");
            return (Criteria) this;
        }

        public Criteria andLabIdIsNotNull() {
            addCriterion("lab_id is not null");
            return (Criteria) this;
        }

        public Criteria andLabIdEqualTo(Long value) {
            addCriterion("lab_id =", value, "labId");
            return (Criteria) this;
        }

        public Criteria andLabIdNotEqualTo(Long value) {
            addCriterion("lab_id <>", value, "labId");
            return (Criteria) this;
        }

        public Criteria andLabIdGreaterThan(Long value) {
            addCriterion("lab_id >", value, "labId");
            return (Criteria) this;
        }

        public Criteria andLabIdGreaterThanOrEqualTo(Long value) {
            addCriterion("lab_id >=", value, "labId");
            return (Criteria) this;
        }

        public Criteria andLabIdLessThan(Long value) {
            addCriterion("lab_id <", value, "labId");
            return (Criteria) this;
        }

        public Criteria andLabIdLessThanOrEqualTo(Long value) {
            addCriterion("lab_id <=", value, "labId");
            return (Criteria) this;
        }

        public Criteria andLabIdIn(List<Long> values) {
            addCriterion("lab_id in", values, "labId");
            return (Criteria) this;
        }

        public Criteria andLabIdNotIn(List<Long> values) {
            addCriterion("lab_id not in", values, "labId");
            return (Criteria) this;
        }

        public Criteria andLabIdBetween(Long value1, Long value2) {
            addCriterion("lab_id between", value1, value2, "labId");
            return (Criteria) this;
        }

        public Criteria andLabIdNotBetween(Long value1, Long value2) {
            addCriterion("lab_id not between", value1, value2, "labId");
            return (Criteria) this;
        }

        public Criteria andFkNameIsNull() {
            addCriterion("fk_name is null");
            return (Criteria) this;
        }

        public Criteria andFkNameIsNotNull() {
            addCriterion("fk_name is not null");
            return (Criteria) this;
        }

        public Criteria andFkNameEqualTo(String value) {
            addCriterion("fk_name =", value, "fkName");
            return (Criteria) this;
        }

        public Criteria andFkNameNotEqualTo(String value) {
            addCriterion("fk_name <>", value, "fkName");
            return (Criteria) this;
        }

        public Criteria andFkNameGreaterThan(String value) {
            addCriterion("fk_name >", value, "fkName");
            return (Criteria) this;
        }

        public Criteria andFkNameGreaterThanOrEqualTo(String value) {
            addCriterion("fk_name >=", value, "fkName");
            return (Criteria) this;
        }

        public Criteria andFkNameLessThan(String value) {
            addCriterion("fk_name <", value, "fkName");
            return (Criteria) this;
        }

        public Criteria andFkNameLessThanOrEqualTo(String value) {
            addCriterion("fk_name <=", value, "fkName");
            return (Criteria) this;
        }

        public Criteria andFkNameLike(String value) {
            addCriterion("fk_name like", value, "fkName");
            return (Criteria) this;
        }

        public Criteria andFkNameNotLike(String value) {
            addCriterion("fk_name not like", value, "fkName");
            return (Criteria) this;
        }

        public Criteria andFkNameIn(List<String> values) {
            addCriterion("fk_name in", values, "fkName");
            return (Criteria) this;
        }

        public Criteria andFkNameNotIn(List<String> values) {
            addCriterion("fk_name not in", values, "fkName");
            return (Criteria) this;
        }

        public Criteria andFkNameBetween(String value1, String value2) {
            addCriterion("fk_name between", value1, value2, "fkName");
            return (Criteria) this;
        }

        public Criteria andFkNameNotBetween(String value1, String value2) {
            addCriterion("fk_name not between", value1, value2, "fkName");
            return (Criteria) this;
        }

        public Criteria andOperatorIdIsNull() {
            addCriterion("operator_id is null");
            return (Criteria) this;
        }

        public Criteria andOperatorIdIsNotNull() {
            addCriterion("operator_id is not null");
            return (Criteria) this;
        }

        public Criteria andOperatorIdEqualTo(Integer value) {
            addCriterion("operator_id =", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdNotEqualTo(Integer value) {
            addCriterion("operator_id <>", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdGreaterThan(Integer value) {
            addCriterion("operator_id >", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("operator_id >=", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdLessThan(Integer value) {
            addCriterion("operator_id <", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdLessThanOrEqualTo(Integer value) {
            addCriterion("operator_id <=", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdIn(List<Integer> values) {
            addCriterion("operator_id in", values, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdNotIn(List<Integer> values) {
            addCriterion("operator_id not in", values, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdBetween(Integer value1, Integer value2) {
            addCriterion("operator_id between", value1, value2, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdNotBetween(Integer value1, Integer value2) {
            addCriterion("operator_id not between", value1, value2, "operatorId");
            return (Criteria) this;
        }

        public Criteria andSupportedEnrollDataIsNull() {
            addCriterion("supported_enroll_data is null");
            return (Criteria) this;
        }

        public Criteria andSupportedEnrollDataIsNotNull() {
            addCriterion("supported_enroll_data is not null");
            return (Criteria) this;
        }

        public Criteria andSupportedEnrollDataEqualTo(String value) {
            addCriterion("supported_enroll_data =", value, "supportedEnrollData");
            return (Criteria) this;
        }

        public Criteria andSupportedEnrollDataNotEqualTo(String value) {
            addCriterion("supported_enroll_data <>", value, "supportedEnrollData");
            return (Criteria) this;
        }

        public Criteria andSupportedEnrollDataGreaterThan(String value) {
            addCriterion("supported_enroll_data >", value, "supportedEnrollData");
            return (Criteria) this;
        }

        public Criteria andSupportedEnrollDataGreaterThanOrEqualTo(String value) {
            addCriterion("supported_enroll_data >=", value, "supportedEnrollData");
            return (Criteria) this;
        }

        public Criteria andSupportedEnrollDataLessThan(String value) {
            addCriterion("supported_enroll_data <", value, "supportedEnrollData");
            return (Criteria) this;
        }

        public Criteria andSupportedEnrollDataLessThanOrEqualTo(String value) {
            addCriterion("supported_enroll_data <=", value, "supportedEnrollData");
            return (Criteria) this;
        }

        public Criteria andSupportedEnrollDataLike(String value) {
            addCriterion("supported_enroll_data like", value, "supportedEnrollData");
            return (Criteria) this;
        }

        public Criteria andSupportedEnrollDataNotLike(String value) {
            addCriterion("supported_enroll_data not like", value, "supportedEnrollData");
            return (Criteria) this;
        }

        public Criteria andSupportedEnrollDataIn(List<String> values) {
            addCriterion("supported_enroll_data in", values, "supportedEnrollData");
            return (Criteria) this;
        }

        public Criteria andSupportedEnrollDataNotIn(List<String> values) {
            addCriterion("supported_enroll_data not in", values, "supportedEnrollData");
            return (Criteria) this;
        }

        public Criteria andSupportedEnrollDataBetween(String value1, String value2) {
            addCriterion("supported_enroll_data between", value1, value2, "supportedEnrollData");
            return (Criteria) this;
        }

        public Criteria andSupportedEnrollDataNotBetween(String value1, String value2) {
            addCriterion("supported_enroll_data not between", value1, value2, "supportedEnrollData");
            return (Criteria) this;
        }

        public Criteria andIsDeletedIsNull() {
            addCriterion("is_deleted is null");
            return (Criteria) this;
        }

        public Criteria andIsDeletedIsNotNull() {
            addCriterion("is_deleted is not null");
            return (Criteria) this;
        }

        public Criteria andIsDeletedEqualTo(Boolean value) {
            addCriterion("is_deleted =", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedNotEqualTo(Boolean value) {
            addCriterion("is_deleted <>", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedGreaterThan(Boolean value) {
            addCriterion("is_deleted >", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_deleted >=", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedLessThan(Boolean value) {
            addCriterion("is_deleted <", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedLessThanOrEqualTo(Boolean value) {
            addCriterion("is_deleted <=", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedIn(List<Boolean> values) {
            addCriterion("is_deleted in", values, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedNotIn(List<Boolean> values) {
            addCriterion("is_deleted not in", values, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedBetween(Boolean value1, Boolean value2) {
            addCriterion("is_deleted between", value1, value2, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_deleted not between", value1, value2, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andTotalUserCountIsNull() {
            addCriterion("total_user_count is null");
            return (Criteria) this;
        }

        public Criteria andTotalUserCountIsNotNull() {
            addCriterion("total_user_count is not null");
            return (Criteria) this;
        }

        public Criteria andTotalUserCountEqualTo(Short value) {
            addCriterion("total_user_count =", value, "totalUserCount");
            return (Criteria) this;
        }

        public Criteria andTotalUserCountNotEqualTo(Short value) {
            addCriterion("total_user_count <>", value, "totalUserCount");
            return (Criteria) this;
        }

        public Criteria andTotalUserCountGreaterThan(Short value) {
            addCriterion("total_user_count >", value, "totalUserCount");
            return (Criteria) this;
        }

        public Criteria andTotalUserCountGreaterThanOrEqualTo(Short value) {
            addCriterion("total_user_count >=", value, "totalUserCount");
            return (Criteria) this;
        }

        public Criteria andTotalUserCountLessThan(Short value) {
            addCriterion("total_user_count <", value, "totalUserCount");
            return (Criteria) this;
        }

        public Criteria andTotalUserCountLessThanOrEqualTo(Short value) {
            addCriterion("total_user_count <=", value, "totalUserCount");
            return (Criteria) this;
        }

        public Criteria andTotalUserCountIn(List<Short> values) {
            addCriterion("total_user_count in", values, "totalUserCount");
            return (Criteria) this;
        }

        public Criteria andTotalUserCountNotIn(List<Short> values) {
            addCriterion("total_user_count not in", values, "totalUserCount");
            return (Criteria) this;
        }

        public Criteria andTotalUserCountBetween(Short value1, Short value2) {
            addCriterion("total_user_count between", value1, value2, "totalUserCount");
            return (Criteria) this;
        }

        public Criteria andTotalUserCountNotBetween(Short value1, Short value2) {
            addCriterion("total_user_count not between", value1, value2, "totalUserCount");
            return (Criteria) this;
        }

        public Criteria andUserCountIsNull() {
            addCriterion("user_count is null");
            return (Criteria) this;
        }

        public Criteria andUserCountIsNotNull() {
            addCriterion("user_count is not null");
            return (Criteria) this;
        }

        public Criteria andUserCountEqualTo(Short value) {
            addCriterion("user_count =", value, "userCount");
            return (Criteria) this;
        }

        public Criteria andUserCountNotEqualTo(Short value) {
            addCriterion("user_count <>", value, "userCount");
            return (Criteria) this;
        }

        public Criteria andUserCountGreaterThan(Short value) {
            addCriterion("user_count >", value, "userCount");
            return (Criteria) this;
        }

        public Criteria andUserCountGreaterThanOrEqualTo(Short value) {
            addCriterion("user_count >=", value, "userCount");
            return (Criteria) this;
        }

        public Criteria andUserCountLessThan(Short value) {
            addCriterion("user_count <", value, "userCount");
            return (Criteria) this;
        }

        public Criteria andUserCountLessThanOrEqualTo(Short value) {
            addCriterion("user_count <=", value, "userCount");
            return (Criteria) this;
        }

        public Criteria andUserCountIn(List<Short> values) {
            addCriterion("user_count in", values, "userCount");
            return (Criteria) this;
        }

        public Criteria andUserCountNotIn(List<Short> values) {
            addCriterion("user_count not in", values, "userCount");
            return (Criteria) this;
        }

        public Criteria andUserCountBetween(Short value1, Short value2) {
            addCriterion("user_count between", value1, value2, "userCount");
            return (Criteria) this;
        }

        public Criteria andUserCountNotBetween(Short value1, Short value2) {
            addCriterion("user_count not between", value1, value2, "userCount");
            return (Criteria) this;
        }

        public Criteria andManagerCountIsNull() {
            addCriterion("manager_count is null");
            return (Criteria) this;
        }

        public Criteria andManagerCountIsNotNull() {
            addCriterion("manager_count is not null");
            return (Criteria) this;
        }

        public Criteria andManagerCountEqualTo(Short value) {
            addCriterion("manager_count =", value, "managerCount");
            return (Criteria) this;
        }

        public Criteria andManagerCountNotEqualTo(Short value) {
            addCriterion("manager_count <>", value, "managerCount");
            return (Criteria) this;
        }

        public Criteria andManagerCountGreaterThan(Short value) {
            addCriterion("manager_count >", value, "managerCount");
            return (Criteria) this;
        }

        public Criteria andManagerCountGreaterThanOrEqualTo(Short value) {
            addCriterion("manager_count >=", value, "managerCount");
            return (Criteria) this;
        }

        public Criteria andManagerCountLessThan(Short value) {
            addCriterion("manager_count <", value, "managerCount");
            return (Criteria) this;
        }

        public Criteria andManagerCountLessThanOrEqualTo(Short value) {
            addCriterion("manager_count <=", value, "managerCount");
            return (Criteria) this;
        }

        public Criteria andManagerCountIn(List<Short> values) {
            addCriterion("manager_count in", values, "managerCount");
            return (Criteria) this;
        }

        public Criteria andManagerCountNotIn(List<Short> values) {
            addCriterion("manager_count not in", values, "managerCount");
            return (Criteria) this;
        }

        public Criteria andManagerCountBetween(Short value1, Short value2) {
            addCriterion("manager_count between", value1, value2, "managerCount");
            return (Criteria) this;
        }

        public Criteria andManagerCountNotBetween(Short value1, Short value2) {
            addCriterion("manager_count not between", value1, value2, "managerCount");
            return (Criteria) this;
        }

        public Criteria andFpCountIsNull() {
            addCriterion("fp_count is null");
            return (Criteria) this;
        }

        public Criteria andFpCountIsNotNull() {
            addCriterion("fp_count is not null");
            return (Criteria) this;
        }

        public Criteria andFpCountEqualTo(Short value) {
            addCriterion("fp_count =", value, "fpCount");
            return (Criteria) this;
        }

        public Criteria andFpCountNotEqualTo(Short value) {
            addCriterion("fp_count <>", value, "fpCount");
            return (Criteria) this;
        }

        public Criteria andFpCountGreaterThan(Short value) {
            addCriterion("fp_count >", value, "fpCount");
            return (Criteria) this;
        }

        public Criteria andFpCountGreaterThanOrEqualTo(Short value) {
            addCriterion("fp_count >=", value, "fpCount");
            return (Criteria) this;
        }

        public Criteria andFpCountLessThan(Short value) {
            addCriterion("fp_count <", value, "fpCount");
            return (Criteria) this;
        }

        public Criteria andFpCountLessThanOrEqualTo(Short value) {
            addCriterion("fp_count <=", value, "fpCount");
            return (Criteria) this;
        }

        public Criteria andFpCountIn(List<Short> values) {
            addCriterion("fp_count in", values, "fpCount");
            return (Criteria) this;
        }

        public Criteria andFpCountNotIn(List<Short> values) {
            addCriterion("fp_count not in", values, "fpCount");
            return (Criteria) this;
        }

        public Criteria andFpCountBetween(Short value1, Short value2) {
            addCriterion("fp_count between", value1, value2, "fpCount");
            return (Criteria) this;
        }

        public Criteria andFpCountNotBetween(Short value1, Short value2) {
            addCriterion("fp_count not between", value1, value2, "fpCount");
            return (Criteria) this;
        }

        public Criteria andFaceCountIsNull() {
            addCriterion("face_count is null");
            return (Criteria) this;
        }

        public Criteria andFaceCountIsNotNull() {
            addCriterion("face_count is not null");
            return (Criteria) this;
        }

        public Criteria andFaceCountEqualTo(Short value) {
            addCriterion("face_count =", value, "faceCount");
            return (Criteria) this;
        }

        public Criteria andFaceCountNotEqualTo(Short value) {
            addCriterion("face_count <>", value, "faceCount");
            return (Criteria) this;
        }

        public Criteria andFaceCountGreaterThan(Short value) {
            addCriterion("face_count >", value, "faceCount");
            return (Criteria) this;
        }

        public Criteria andFaceCountGreaterThanOrEqualTo(Short value) {
            addCriterion("face_count >=", value, "faceCount");
            return (Criteria) this;
        }

        public Criteria andFaceCountLessThan(Short value) {
            addCriterion("face_count <", value, "faceCount");
            return (Criteria) this;
        }

        public Criteria andFaceCountLessThanOrEqualTo(Short value) {
            addCriterion("face_count <=", value, "faceCount");
            return (Criteria) this;
        }

        public Criteria andFaceCountIn(List<Short> values) {
            addCriterion("face_count in", values, "faceCount");
            return (Criteria) this;
        }

        public Criteria andFaceCountNotIn(List<Short> values) {
            addCriterion("face_count not in", values, "faceCount");
            return (Criteria) this;
        }

        public Criteria andFaceCountBetween(Short value1, Short value2) {
            addCriterion("face_count between", value1, value2, "faceCount");
            return (Criteria) this;
        }

        public Criteria andFaceCountNotBetween(Short value1, Short value2) {
            addCriterion("face_count not between", value1, value2, "faceCount");
            return (Criteria) this;
        }

        public Criteria andPasswordCountIsNull() {
            addCriterion("password_count is null");
            return (Criteria) this;
        }

        public Criteria andPasswordCountIsNotNull() {
            addCriterion("password_count is not null");
            return (Criteria) this;
        }

        public Criteria andPasswordCountEqualTo(Short value) {
            addCriterion("password_count =", value, "passwordCount");
            return (Criteria) this;
        }

        public Criteria andPasswordCountNotEqualTo(Short value) {
            addCriterion("password_count <>", value, "passwordCount");
            return (Criteria) this;
        }

        public Criteria andPasswordCountGreaterThan(Short value) {
            addCriterion("password_count >", value, "passwordCount");
            return (Criteria) this;
        }

        public Criteria andPasswordCountGreaterThanOrEqualTo(Short value) {
            addCriterion("password_count >=", value, "passwordCount");
            return (Criteria) this;
        }

        public Criteria andPasswordCountLessThan(Short value) {
            addCriterion("password_count <", value, "passwordCount");
            return (Criteria) this;
        }

        public Criteria andPasswordCountLessThanOrEqualTo(Short value) {
            addCriterion("password_count <=", value, "passwordCount");
            return (Criteria) this;
        }

        public Criteria andPasswordCountIn(List<Short> values) {
            addCriterion("password_count in", values, "passwordCount");
            return (Criteria) this;
        }

        public Criteria andPasswordCountNotIn(List<Short> values) {
            addCriterion("password_count not in", values, "passwordCount");
            return (Criteria) this;
        }

        public Criteria andPasswordCountBetween(Short value1, Short value2) {
            addCriterion("password_count between", value1, value2, "passwordCount");
            return (Criteria) this;
        }

        public Criteria andPasswordCountNotBetween(Short value1, Short value2) {
            addCriterion("password_count not between", value1, value2, "passwordCount");
            return (Criteria) this;
        }

        public Criteria andIdcardCountIsNull() {
            addCriterion("idcard_count is null");
            return (Criteria) this;
        }

        public Criteria andIdcardCountIsNotNull() {
            addCriterion("idcard_count is not null");
            return (Criteria) this;
        }

        public Criteria andIdcardCountEqualTo(Short value) {
            addCriterion("idcard_count =", value, "idcardCount");
            return (Criteria) this;
        }

        public Criteria andIdcardCountNotEqualTo(Short value) {
            addCriterion("idcard_count <>", value, "idcardCount");
            return (Criteria) this;
        }

        public Criteria andIdcardCountGreaterThan(Short value) {
            addCriterion("idcard_count >", value, "idcardCount");
            return (Criteria) this;
        }

        public Criteria andIdcardCountGreaterThanOrEqualTo(Short value) {
            addCriterion("idcard_count >=", value, "idcardCount");
            return (Criteria) this;
        }

        public Criteria andIdcardCountLessThan(Short value) {
            addCriterion("idcard_count <", value, "idcardCount");
            return (Criteria) this;
        }

        public Criteria andIdcardCountLessThanOrEqualTo(Short value) {
            addCriterion("idcard_count <=", value, "idcardCount");
            return (Criteria) this;
        }

        public Criteria andIdcardCountIn(List<Short> values) {
            addCriterion("idcard_count in", values, "idcardCount");
            return (Criteria) this;
        }

        public Criteria andIdcardCountNotIn(List<Short> values) {
            addCriterion("idcard_count not in", values, "idcardCount");
            return (Criteria) this;
        }

        public Criteria andIdcardCountBetween(Short value1, Short value2) {
            addCriterion("idcard_count between", value1, value2, "idcardCount");
            return (Criteria) this;
        }

        public Criteria andIdcardCountNotBetween(Short value1, Short value2) {
            addCriterion("idcard_count not between", value1, value2, "idcardCount");
            return (Criteria) this;
        }

        public Criteria andTotalLogCountIsNull() {
            addCriterion("total_log_count is null");
            return (Criteria) this;
        }

        public Criteria andTotalLogCountIsNotNull() {
            addCriterion("total_log_count is not null");
            return (Criteria) this;
        }

        public Criteria andTotalLogCountEqualTo(Integer value) {
            addCriterion("total_log_count =", value, "totalLogCount");
            return (Criteria) this;
        }

        public Criteria andTotalLogCountNotEqualTo(Integer value) {
            addCriterion("total_log_count <>", value, "totalLogCount");
            return (Criteria) this;
        }

        public Criteria andTotalLogCountGreaterThan(Integer value) {
            addCriterion("total_log_count >", value, "totalLogCount");
            return (Criteria) this;
        }

        public Criteria andTotalLogCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("total_log_count >=", value, "totalLogCount");
            return (Criteria) this;
        }

        public Criteria andTotalLogCountLessThan(Integer value) {
            addCriterion("total_log_count <", value, "totalLogCount");
            return (Criteria) this;
        }

        public Criteria andTotalLogCountLessThanOrEqualTo(Integer value) {
            addCriterion("total_log_count <=", value, "totalLogCount");
            return (Criteria) this;
        }

        public Criteria andTotalLogCountIn(List<Integer> values) {
            addCriterion("total_log_count in", values, "totalLogCount");
            return (Criteria) this;
        }

        public Criteria andTotalLogCountNotIn(List<Integer> values) {
            addCriterion("total_log_count not in", values, "totalLogCount");
            return (Criteria) this;
        }

        public Criteria andTotalLogCountBetween(Integer value1, Integer value2) {
            addCriterion("total_log_count between", value1, value2, "totalLogCount");
            return (Criteria) this;
        }

        public Criteria andTotalLogCountNotBetween(Integer value1, Integer value2) {
            addCriterion("total_log_count not between", value1, value2, "totalLogCount");
            return (Criteria) this;
        }

        public Criteria andFirmwareIsNull() {
            addCriterion("firmware is null");
            return (Criteria) this;
        }

        public Criteria andFirmwareIsNotNull() {
            addCriterion("firmware is not null");
            return (Criteria) this;
        }

        public Criteria andFirmwareEqualTo(String value) {
            addCriterion("firmware =", value, "firmware");
            return (Criteria) this;
        }

        public Criteria andFirmwareNotEqualTo(String value) {
            addCriterion("firmware <>", value, "firmware");
            return (Criteria) this;
        }

        public Criteria andFirmwareGreaterThan(String value) {
            addCriterion("firmware >", value, "firmware");
            return (Criteria) this;
        }

        public Criteria andFirmwareGreaterThanOrEqualTo(String value) {
            addCriterion("firmware >=", value, "firmware");
            return (Criteria) this;
        }

        public Criteria andFirmwareLessThan(String value) {
            addCriterion("firmware <", value, "firmware");
            return (Criteria) this;
        }

        public Criteria andFirmwareLessThanOrEqualTo(String value) {
            addCriterion("firmware <=", value, "firmware");
            return (Criteria) this;
        }

        public Criteria andFirmwareLike(String value) {
            addCriterion("firmware like", value, "firmware");
            return (Criteria) this;
        }

        public Criteria andFirmwareNotLike(String value) {
            addCriterion("firmware not like", value, "firmware");
            return (Criteria) this;
        }

        public Criteria andFirmwareIn(List<String> values) {
            addCriterion("firmware in", values, "firmware");
            return (Criteria) this;
        }

        public Criteria andFirmwareNotIn(List<String> values) {
            addCriterion("firmware not in", values, "firmware");
            return (Criteria) this;
        }

        public Criteria andFirmwareBetween(String value1, String value2) {
            addCriterion("firmware between", value1, value2, "firmware");
            return (Criteria) this;
        }

        public Criteria andFirmwareNotBetween(String value1, String value2) {
            addCriterion("firmware not between", value1, value2, "firmware");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

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
}