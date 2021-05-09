package com.hse.utils;

import static com.google.common.base.Preconditions.checkNotNull;

// copy-pasted from SO:
// https://stackoverflow.com/questions/1715711/how-to-update-a-postgresql-array-column-with-spring-jdbctemplate

import java.sql.Array;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;

import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.support.SqlValue;

public class ArraySQLValue implements SqlValue {
    private final Object[] arr;
    private final String   dbTypeName;

    public static ArraySQLValue create(final Object[] arr) {
        return new ArraySQLValue(arr, determineDbTypeName(arr));
    }

    public static ArraySQLValue create(final Object[] arr, final String dbTypeName) {
        return new ArraySQLValue(arr, dbTypeName);
    }

    private ArraySQLValue(final Object[] arr, final String dbTypeName) {
        this.arr = checkNotNull(arr);
        this.dbTypeName = checkNotNull(dbTypeName);
    }

    @Override
    public void setValue(final PreparedStatement ps, final int paramIndex) throws SQLException {
        final Array arrayValue = ps.getConnection().createArrayOf(dbTypeName, arr);
        ps.setArray(paramIndex, arrayValue);
    }

    @Override
    public void cleanup() {}

    private static String determineDbTypeName(final Object[] arr) {
        // use Spring Utils similar to normal JdbcTemplate inner workings
        final int sqlParameterType =
                StatementCreatorUtils.javaTypeToSqlParameterType(arr.getClass().getComponentType());
        final JDBCType jdbcTypeToUse = JDBCType.valueOf(sqlParameterType);
        // lowercasing typename for Postgres
        final String typeNameToUse = jdbcTypeToUse.getName().toLowerCase(Locale.US);
        return typeNameToUse;
    }
}