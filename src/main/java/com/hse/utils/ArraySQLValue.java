package com.hse.utils;

import org.springframework.jdbc.support.SqlValue;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.google.common.base.Preconditions.checkNotNull;

public class ArraySQLValue implements SqlValue {
    private final Object[] arr;
    private final String   dbTypeName;

    public static ArraySQLValue create(final Object[] arr, final String dbTypeName) {
        return new ArraySQLValue(arr, dbTypeName);
    }

    private ArraySQLValue(final Object[] arr, final String dbTypeName) {
        this.arr        = checkNotNull(arr);
        this.dbTypeName = checkNotNull(dbTypeName);
    }

    @Override
    public void setValue(final PreparedStatement ps, final int paramIndex) throws SQLException {
        final Array arrayValue = ps.getConnection().createArrayOf(dbTypeName, arr);
        ps.setArray(paramIndex, arrayValue);
    }

    @Override
    public void cleanup() {}
}