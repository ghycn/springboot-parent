package com.baomidou.mybatisplus.plugins;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Statement;
import java.util.Properties;

/**
 * Mybatis-plus sql工具
 *
 * @author ghy
 * @date 2018-07-02 下午2:51
 **/
@Intercepts({@Signature(
        type = StatementHandler.class,
        method = "query",
        args = {Statement.class, ResultHandler.class}
), @Signature(
        type = StatementHandler.class,
        method = "update",
        args = {Statement.class}
), @Signature(
        type = StatementHandler.class,
        method = "batch",
        args = {Statement.class}
)})
public class PerformanceInterceptor implements Interceptor {
    private static final Log logger = LogFactory.getLog(PerformanceInterceptor.class);
    private static final String DruidPooledPreparedStatement = "com.alibaba.druid.pool.DruidPooledPreparedStatement";
    private static final String T4CPreparedStatement = "oracle.jdbc.driver.T4CPreparedStatement";
    private static final String OraclePreparedStatementWrapper = "oracle.jdbc.driver.OraclePreparedStatementWrapper";
    private static final String HikariPreparedStatementWrapper = "com.zaxxer.hikari.pool.HikariProxyPreparedStatement";
    private long maxTime = 0L;
    private boolean format = false;
    private boolean writeInLog = false;
    private Method oracleGetOriginalSqlMethod;
    private Method druidGetSQLMethod;

    public PerformanceInterceptor() {
    }

    public Object intercept(Invocation invocation) throws Throwable {
        Object firstArg = invocation.getArgs()[0];
        Statement statement;
        if (Proxy.isProxyClass(firstArg.getClass())) {
            statement = (Statement)SystemMetaObject.forObject(firstArg).getValue("h.statement");
        } else {
            statement = (Statement)firstArg;
        }

        try {
            statement = (Statement)SystemMetaObject.forObject(statement).getValue("stmt.statement");
        } catch (Exception var19) {
            ;
        }

        String originalSql = null;
        String stmtClassName = statement.getClass().getName();
        Class clazz;
        Object stmtSql;
        if ("com.alibaba.druid.pool.DruidPooledPreparedStatement".equals(stmtClassName)) {
            try {
                if (this.druidGetSQLMethod == null) {
                    clazz = Class.forName("com.alibaba.druid.pool.DruidPooledPreparedStatement");
                    this.druidGetSQLMethod = clazz.getMethod("getSql");
                }

                stmtSql = this.druidGetSQLMethod.invoke(statement);
                if (stmtSql != null && stmtSql instanceof String) {
                    originalSql = (String)stmtSql;
                }
            } catch (Exception var18) {
                ;
            }
        } else if (!"oracle.jdbc.driver.T4CPreparedStatement".equals(stmtClassName) && !"oracle.jdbc.driver.OraclePreparedStatementWrapper".equals(stmtClassName)) {
            if ("com.zaxxer.hikari.pool.HikariProxyPreparedStatement".equals(stmtClassName)) {
                try {
                    stmtSql = SystemMetaObject.forObject(statement).getValue("delegate.sqlStatement");
                    if (stmtSql != null) {
                        originalSql = stmtSql.toString();
                    }
                } catch (Exception var16) {
                    ;
                }
            }
        } else {
            try {
                if (this.oracleGetOriginalSqlMethod != null) {
                    stmtSql = this.oracleGetOriginalSqlMethod.invoke(statement);
                    if (stmtSql != null && stmtSql instanceof String) {
                        originalSql = (String)stmtSql;
                    }
                } else {
                    clazz = Class.forName(stmtClassName);
                    this.oracleGetOriginalSqlMethod = this.getMethodRegular(clazz, "getOriginalSql");
                    if (this.oracleGetOriginalSqlMethod != null) {
                        this.oracleGetOriginalSqlMethod.setAccessible(true);
                        if (this.oracleGetOriginalSqlMethod != null) {
                             stmtSql = this.oracleGetOriginalSqlMethod.invoke(statement);
                            if (stmtSql != null && stmtSql instanceof String) {
                                originalSql = (String)stmtSql;
                            }
                        }
                    }
                }
            } catch (Exception var17) {
                ;
            }
        }

        if (originalSql == null) {
            originalSql = statement.toString();
        }
/**
 * ghy
 * sql语句打印不完整
 */

//        int index = originalSql.indexOf(58);
//        if (index > 0) {
//            originalSql = originalSql.substring(index + 1, originalSql.length());
//        }

        long start = SystemClock.now();
        Object result = invocation.proceed();
        long timing = SystemClock.now() - start;
        Object target = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(target);
        MappedStatement ms = (MappedStatement)metaObject.getValue("delegate.mappedStatement");
        StringBuilder formatSql = new StringBuilder();
        formatSql.append(" Time：").append(timing);
        formatSql.append(" ms - ID：").append(ms.getId());
        formatSql.append("\n Execute SQL：").append(SqlUtils.sqlFormat(originalSql, this.format)).append("\n");
        if (this.isWriteInLog()) {
            if (this.getMaxTime() >= 1L && timing > this.getMaxTime()) {
                logger.error(formatSql.toString());
            } else {
                logger.debug(formatSql.toString());
            }
        } else {
            System.err.println(formatSql.toString());
            if (this.getMaxTime() >= 1L && timing > this.getMaxTime()) {
                throw new MybatisPlusException(" The SQL execution time is too large, please optimize ! ");
            }
        }

        return result;
    }

    @Override
    public Object plugin(Object target) {
        return target instanceof StatementHandler ? Plugin.wrap(target, this) : target;
    }

    @Override
    public void setProperties(Properties prop) {
        String maxTime = prop.getProperty("maxTime");
        String format = prop.getProperty("format");
        if (StringUtils.isNotEmpty(maxTime)) {
            this.maxTime = Long.parseLong(maxTime);
        }

        if (StringUtils.isNotEmpty(format)) {
            this.format = Boolean.valueOf(format);
        }

    }

    public long getMaxTime() {
        return this.maxTime;
    }

    public PerformanceInterceptor setMaxTime(long maxTime) {
        this.maxTime = maxTime;
        return this;
    }

    public boolean isFormat() {
        return this.format;
    }

    public PerformanceInterceptor setFormat(boolean format) {
        this.format = format;
        return this;
    }

    public boolean isWriteInLog() {
        return this.writeInLog;
    }

    public PerformanceInterceptor setWriteInLog(boolean writeInLog) {
        this.writeInLog = writeInLog;
        return this;
    }

    public Method getMethodRegular(Class<?> clazz, String methodName) {
        if (Object.class.equals(clazz)) {
            return null;
        } else {
            Method[] arr$ = clazz.getDeclaredMethods();
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                Method method = arr$[i$];
                if (method.getName().equals(methodName)) {
                    return method;
                }
            }

            return this.getMethodRegular(clazz.getSuperclass(), methodName);
        }
    }
}
