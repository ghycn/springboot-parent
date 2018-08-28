package com.baomidou.mybatisplus.toolkit;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
/**
 * @author ghy
 * @date 2018-06-08 下午3:46
 * @description Jdbc连接工具
 **/

public class JdbcUtils {
    private static final Log logger = LogFactory.getLog(JdbcUtils.class);

    public JdbcUtils() {
    }

    public static DbType getDbType(String jdbcUrl) {
        if (StringUtils.isEmpty(jdbcUrl)) {
            throw new MybatisPlusException("Error: The jdbcUrl is Null, Cannot read database type");
        } else if (!jdbcUrl.startsWith("jdbc:mysql:") && !jdbcUrl.startsWith("jdbc:cobar:") && !jdbcUrl.startsWith("jdbc:log4jdbc:mysql:")) {
            if (!jdbcUrl.startsWith("jdbc:oracle:") && !jdbcUrl.startsWith("jdbc:log4jdbc:oracle:")) {
                if (!jdbcUrl.startsWith("jdbc:sqlserver:") && !jdbcUrl.startsWith("jdbc:microsoft:")) {
                    if (jdbcUrl.startsWith("jdbc:sqlserver2012:")) {
                        return DbType.SQL_SERVER;
                    } else if (!jdbcUrl.startsWith("jdbc:kingbase:") && !jdbcUrl.startsWith("jdbc:log4jdbc:postgresql:")) {
                        if (!jdbcUrl.startsWith("jdbc:hsqldb:") && !jdbcUrl.startsWith("jdbc:log4jdbc:hsqldb:")) {
                            if (jdbcUrl.startsWith("jdbc:db2:")) {
                                return DbType.DB2;
                            } else if (jdbcUrl.startsWith("jdbc:sqlite:")) {
                                return DbType.SQLITE;
                            } else if (!jdbcUrl.startsWith("jdbc:h2:") && !jdbcUrl.startsWith("jdbc:log4jdbc:h2:")) {
                                logger.warn("The jdbcUrl is " + jdbcUrl + ", Mybatis Plus Cannot Read Database type or The Database's Not Supported!");
                                return DbType.OTHER;
                            } else {
                                return DbType.H2;
                            }
                        } else {
                            return DbType.HSQL;
                        }
                    } else {
                        return DbType.POSTGRE_SQL;
                    }
                } else {
                    return DbType.SQL_SERVER2005;
                }
            } else {
                return DbType.ORACLE;
            }
        } else {
            return DbType.MYSQL;
        }
    }
}
