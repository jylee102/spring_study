package com.example.spring_semi_project.dto;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// LISTAGG 함수의 결과를 List<String>으로 매핑하기 위한 커스텀 TypeHandler
public class StringListTypeHandler extends BaseTypeHandler<List<String>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, String.join(",", parameter));
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String columnValue = rs.getString(columnName);
        if (columnValue == null) {
            return new ArrayList<>(); // NULL 처리
        }
        List<String> resultList = new ArrayList<>();
        resultList.add(columnValue); // 결과를 가변 리스트의 0번째 인덱스에 추가
        return resultList;
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String columnValue = rs.getString(columnIndex);
        if (columnValue == null) {
            return new ArrayList<>(); // NULL 처리
        }
        List<String> resultList = new ArrayList<>();
        resultList.add(columnValue); // 결과를 가변 리스트의 0번째 인덱스에 추가
        return resultList;
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String columnValue = cs.getString(columnIndex);
        if (columnValue == null) {
            return new ArrayList<>(); // NULL 처리
        }
        List<String> resultList = new ArrayList<>();
        resultList.add(columnValue); // 결과를 가변 리스트의 0번째 인덱스에 추가
        return resultList;
    }
}
