package com.ghycn.common;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.config.rules.PropertyInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ghy
 * @date 2018-06-14 上午11:08
 * @description 代码生成器
 **/
public class MpGenerator {

    public static String SCHEM_ANAME = "";


    /**
     * 全局配置
     */
    private static GlobalConfig globalConfig() {
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir("./");
        gc.setFileOverride(true);
        // 不需要ActiveRecord特性的请改为false
        gc.setActiveRecord(true);
        // XML 二级缓存
        gc.setEnableCache(false);
        // XML ResultMap
        gc.setBaseResultMap(true);
        // XML columList
        gc.setBaseColumnList(true);
        gc.setSwagger2(true);
        gc.setIdType(IdType.UUID);
        // .setKotlin(true) 是否生成 kotlin 代码
        gc.setAuthor("ghy");
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapping");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setControllerName("%sController");
        gc.setOpen(false);
        gc.setActiveRecord(true);
        return gc;
    }

    /**
     * 数据源配置
     */
    private static DataSourceConfig dataSourceConfig() {
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.POSTGRE_SQL);
        dsc.setTypeConvert(new MySqlTypeConvert() {
            // 自定义数据库表字段类型转换【可选】
            public PropertyInfo processTypeConvert(String fieldType) {
                System.out.println("转换类型：" + fieldType);
                // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
                return super.processTypeConvert( globalConfig(),fieldType);
            }
        });

        SCHEM_ANAME = "META";
//        SCHEM_ANAME = "PUBLIC";

        dsc.setDriverName("com.kingbase.Driver");
        dsc.setUsername("SYSTEM");
        dsc.setPassword("MANAGER");
        dsc.setUrl("jdbc:kingbase://192.168.9.170:54321/DEV?ClientEncoding=GBK");
        dsc.setSchemaName(SCHEM_ANAME);
        return dsc;
    }

    /**
     * 策略配置
     */
    private static StrategyConfig strategyConfig() {
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        strategy.setTablePrefix(new String[]{"T_"});// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
//        strategy.setInclude(new String[]{"T_MODEL_STAND_R"}); // 需要生成的表
        // strategy.setExclude(new String[]{"test"}); // 排除生成的表
        // 自定义实体父类
//         strategy.setSuperEntityClass("com.kingbase.xy.base.model.BaseModel");
        // 自定义实体，公共字段
         strategy.setSuperEntityColumns(new String[] { "VERSION", "DELETE_FLAG","CREATER","CREATE_TIME","UPDATER","UPDATE_TIME","ORG_ID" });
        // 自定义 mapper 父类
        // strategy.setSuperMapperClass("com.baomidou.demo.TestMapper");
        // 自定义 service 父类
        // strategy.setSuperServiceClass("com.baomidou.demo.TestService");
        // 自定义 service 实现类父类
        // strategy.setSuperServiceImplClass("com.baomidou.demo.TestServiceImpl");
        // 自定义 controller 父类
//        strategy.setSuperControllerClass("com.kingbase.xy.base.controller.BaseController");
        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        // strategy.setEntityColumnConstant(true);
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        // strategy.setEntityBuilderModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setEntityColumnConstant(true);
        // 设置逻辑删除字段名称
        strategy.setLogicDeleteFieldName("DELETE_FLAG");
        // 设置乐观锁字段名称
        strategy.setVersionFieldName("VERSION");
        // 设置Lombok
        strategy.setEntityLombokModel(true);
        return strategy;
    }


    /**
     * <p>
     * MySQL 生成演示
     * </p>
     */
    public static void main(String[] args) {
        AutoGenerator mpg = new AutoGenerator();
        // 选择 freemarker 引擎，默认 Veloctiy
        // mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        //全局配置
        mpg.setGlobalConfig(MpGenerator.globalConfig());

        // 数据源配置
        mpg.setDataSource(MpGenerator.dataSourceConfig());

        // 策略配置
        mpg.setStrategy(MpGenerator.strategyConfig());

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.ghycn");
        pc.setModuleName("common");
        pc.setController("controller");
        mpg.setPackageInfo(pc);

        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
//                map.put("package", "com.kingbase.xy.dto");
                this.setMap(map);
            }
        };

//        // 自定义 xxList.jsp 生成
//        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
//        cfg.setFileOutConfigList(focList);
//        mpg.setCfg(cfg);
//
//        // 自定义DTO文件模板
//        focList.add(new FileOutConfig("/templates/dto.java.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                return "com/kingbase/xy/dto/" + tableInfo.getEntityName() + "Dto" + ".java";
//            }
//        });

//
//        // 自定义ts文件模板
//        focList.add(new FileOutConfig("/templates/typescript.java.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                return "com/kingbase/xy/ts/" + tableInfo.getEntityName() + ".ts";
//            }
//        });
//
//        //自定义service的ts文件
//        focList.add(new FileOutConfig("/templates/vue_service.java.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                return "com/kingbase/xy/ts/service/" + tableInfo.getEntityName() + "Service.ts";
//            }
//        });
//
//
//        // 自定义vue文件模板
//        focList.add(new FileOutConfig("/templates/vue.java.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                return "com/kingbase/xy/vue/" + tableInfo.getEntityName() + "ListView" + ".vue";
//            }
//        });
//
//
//        // 自定义vue分页文件模板
//        focList.add(new FileOutConfig("/templates/vue_page.java.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                return "com/kingbase/xy/page/" + tableInfo.getEntityName() + "Page" + ".ts";
//            }
//        });


        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/templates 下面内容修改，
        // 放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也可以自定义模板名称
//        TemplateConfig tc = new TemplateConfig();
//        tc.setController("/templates/controller.java.vm");
//        tc.setEntity("/templates/entity.java.vm");
//        tc.setMapper("/templates/mapper.java.vm");
//        tc.setXml("/templates/mapper.xml.vm");
//        tc.setService("/templates/service.java.vm");
//        tc.setServiceImpl("/templates/serviceImpl.java.vm");
        // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
//        mpg.setTemplate(tc);

        // 执行生成
        mpg.execute();

    }
}
