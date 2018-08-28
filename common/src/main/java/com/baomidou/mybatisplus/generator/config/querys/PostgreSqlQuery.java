package com.baomidou.mybatisplus.generator.config.querys;

import com.baomidou.mybatisplus.annotation.DbType;
import com.ghycn.common.MpGenerator;

/**
 * @author ghy
 * @date 2018-06-14 下午1:14
 * @description 。。
 **/
public class PostgreSqlQuery extends AbstractDbQuery{
    public PostgreSqlQuery() {
    }

    @Override
    public DbType dbType() {
        return DbType.POSTGRE_SQL;
    }

    @Override
    public String tablesSql() {
        return " SELECT a.tablename ,b.description comments FROM sys_tables  a , SYS_DESCRIPTION  b WHERE  a.oid = b.objoid AND a.schemaname = '"+ MpGenerator.SCHEM_ANAME +"' AND b.objsubid=0 ";
//        return "select table_name as tablename, comments from all_tab_comments WHERE TABLE_TYPE='TABLE' ";
//        return "SELECT A.tablename, obj_description(relfilenode, 'sys_class') AS comments FROM sys_tables A, sys_class B WHERE A.schemaname='%s' AND A.tablename = B.relname";
    }

    @Override
    public String tableFieldsSql() {
        return "SELECT A.attname AS name, format_type(A.atttypid, A.atttypmod) AS type,col_description(A.attrelid, A.attnum) AS comment, (CASE C.contype WHEN 'p' THEN 'PRI' ELSE '' END) AS key FROM sys_attribute A LEFT JOIN sys_constraint C ON A.attnum = C.conkey[1] AND A.attrelid = C.conrelid WHERE  A.attrelid = '%s.%s'::regclass AND A.attnum > 0 AND NOT A.attisdropped ORDER  BY A.attnum";
    }

    @Override
    public String tableName() {
        return "tablename";
    }

    @Override
    public String tableComment() {
        return "comments";
    }

    @Override
    public String fieldName() {
        return "name";
    }

    @Override
    public String fieldType() {
        return "type";
    }

    @Override
    public String fieldComment() {
        return "comment";
    }

    @Override
    public String fieldKey() {
        return "key";
    }
}
