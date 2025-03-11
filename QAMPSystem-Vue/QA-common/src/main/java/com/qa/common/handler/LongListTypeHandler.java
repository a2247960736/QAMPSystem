package com.qa.common.handler;

import com.qa.common.utils.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 22479
 * Date: 2025-03-11
 * Time: 21:52
 */
public class LongListTypeHandler extends BaseTypeHandler<List<Long>> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    List<Long> parameter, JdbcType jdbcType) {
        // 不需要实现写入操作
    }


    @Override
    public List<Long> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return StringUtils.isBlank(value) ?
                new ArrayList<>() :
                Arrays.stream(value.split(","))
                        .map(Long::parseLong)
                        .collect(Collectors.toList());
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public List<Long> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return null;
    }
}
