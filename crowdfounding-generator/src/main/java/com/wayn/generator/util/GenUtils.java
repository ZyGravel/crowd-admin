package com.wayn.generator.util;

import com.wayn.commom.consts.Constant;
import com.wayn.commom.domain.ColumnInfo;
import com.wayn.commom.domain.TableInfo;
import com.wayn.generator.config.GenConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.velocity.VelocityContext;

import java.util.*;

/**
 * 代码生成器 工具类
 *
 * @author ruoyi
 */
public class GenUtils {
    /**
     * 项目空间路径
     */
    private static final String PROJECT_PATH = getProjectPath();

    /**
     * mybatis空间路径
     */
    private static final String MYBATIS_PATH = "main/resources";

    /**
     * html空间路径
     */
    private static final String TEMPLATES_PATH = "main/resources/templates";

    /**
     * 类型转换
     */
    public static Map<String, String> javaTypeMap = new HashMap<String, String>();

    /**
     * 设置列信息
     */
    public static List<ColumnInfo> transColums(List<ColumnInfo> columns) {
        // 列信息
        List<ColumnInfo> columsList = new ArrayList<>();
        for (ColumnInfo column : columns) {
            // 列名转换成Java属性名
            String attrName = convertToCamelCase(column.getColumnName());
            column.setAttrName(attrName);
            column.setAttrname(StringUtils.uncapitalize(attrName));
            column.setExtra(column.getExtra());

            // 列的数据类型，转换成Java类型
            String attrType = javaTypeMap.get(column.getDataType());
            column.setAttrType(attrType);

            columsList.add(column);
        }
        return columsList;
    }

    private static String convertToCamelCase(String columnName) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isEmpty(columnName)) {
            return "";
        }
        String[] split = columnName.split("_", -1);
        for (String s : split) {
            if (StringUtils.isEmpty(s)) {
                continue;
            }
            sb.append(StringUtils.capitalize(s));
        }
        return sb.toString();
    }

    /**
     * 获取模板信息
     *
     * @return 模板列表
     */
    public static VelocityContext getVelocityContext(TableInfo table) {
        // java对象数据传递到模板文件vm
        VelocityContext velocityContext = new VelocityContext();
        String packageName = GenConfig.getPackageName();
        velocityContext.put("tableName", table.getTableName());
        velocityContext.put("tableComment", replaceKeyword(table.getTableComment()));
        velocityContext.put("primaryKey", table.getPrimaryKey());
        velocityContext.put("className", table.getClassName());
        velocityContext.put("classname", table.getClassname());
        velocityContext.put("moduleName", getModuleName(packageName));
        velocityContext.put("columns", table.getColumns());
        velocityContext.put("basePackage", getBasePackage(packageName));
        velocityContext.put("package", packageName);
        velocityContext.put("author", GenConfig.getAuthor());
        velocityContext.put("datetime", DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
        return velocityContext;
    }

    /**
     * 获取模板信息
     *
     * @return 模板列表
     */
    public static List<String> getTemplates() {
        List<String> templates = new ArrayList<String>();
        templates.add("vm/java/domain.java.vm");
        templates.add("vm/java/Dao.java.vm");
        templates.add("vm/java/Service.java.vm");
        templates.add("vm/java/ServiceImpl.java.vm");
        templates.add("vm/java/Controller.java.vm");
        templates.add("vm/xml/Mapper.xml.vm");
        /*templates.add("vm/html/list.html.vm");
        templates.add("vm/html/add.html.vm");
        templates.add("vm/html/edit.html.vm");
        templates.add("vm/sql/sql.vm");*/
        return templates;
    }

    /**
     * 表名转换成Java类名
     */
    public static String tableToJava(String tableName) {
        String autoRemovePre = GenConfig.getAutoRemovePre();
        String tablePrefix = GenConfig.getTablePrefix();
        if (Constant.TRUE.equals(autoRemovePre) && StringUtils.isNotEmpty(tablePrefix)) {
            tableName = tableName.replaceFirst(tablePrefix, "");
        }
        return convertToCamelCase(tableName);
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, TableInfo table, String moduleName) {
        // 小写类名
        String classname = table.getClassname();
        // 大写类名
        String className = table.getClassName();
        String javaPath = PROJECT_PATH;
        String mybatisPath = MYBATIS_PATH + "/" + GenConfig.getPackageName() + "/dao/" + className;
        String htmlPath = TEMPLATES_PATH + "/" + moduleName + "/" + classname;

        if (template.contains("domain.java.vm")) {
            return javaPath + "domain" + "/" + className + ".java";
        }

        if (template.contains("Dao.java.vm")) {
            return javaPath + "dao" + "/" + className + "Mapper.java";
        }

        if (template.contains("Service.java.vm")) {
            return javaPath + "service" + "/" + className + "Service.java";
        }

        if (template.contains("ServiceImpl.java.vm")) {
            return javaPath + "service" + "/impl/" + className + "ServiceImpl.java";
        }

        if (template.contains("Controller.java.vm")) {
            return javaPath + "controller" + "/" + className + "Controller.java";
        }

        if (template.contains("Mapper.xml.vm")) {
            return mybatisPath + "Mapper.xml";
        }

        if (template.contains("list.html.vm")) {
            return htmlPath + "/" + classname + ".html";
        }
        if (template.contains("add.html.vm")) {
            return htmlPath + "/" + "add.html";
        }
        if (template.contains("edit.html.vm")) {
            return htmlPath + "/" + "edit.html";
        }
        if (template.contains("sql.vm")) {
            return classname + "Menu.sql";
        }
        return null;
    }

    /**
     * 获取模块名
     *
     * @param packageName 包名
     * @return 模块名
     */
    public static String getModuleName(String packageName) {
        int lastIndex = packageName.lastIndexOf(".");
        int nameLength = packageName.length();
        String moduleName = StringUtils.substring(packageName, lastIndex + 1, nameLength);
        return moduleName;
    }

    public static String getBasePackage(String packageName) {
        int lastIndex = packageName.lastIndexOf(".");
        String basePackage = StringUtils.substring(packageName, 0, lastIndex);
        return basePackage;
    }

    public static String getProjectPath() {
        String packageName = GenConfig.getPackageName();
        StringBuffer projectPath = new StringBuffer();
        projectPath.append("main/java/");
        projectPath.append(packageName.replace(".", "/"));
        projectPath.append("/");
        return projectPath.toString();
    }

    public static String replaceKeyword(String keyword) {
        String keyName = keyword.replaceAll("(?:表|信息|管理)", "");
        return keyName;
    }

    static {
        javaTypeMap.put("tinyint", "Integer");
        javaTypeMap.put("smallint", "Integer");
        javaTypeMap.put("mediumint", "Integer");
        javaTypeMap.put("int", "Integer");
        javaTypeMap.put("number", "Integer");
        javaTypeMap.put("integer", "integer");
        javaTypeMap.put("bigint", "Long");
        javaTypeMap.put("float", "Float");
        javaTypeMap.put("double", "Double");
        javaTypeMap.put("decimal", "BigDecimal");
        javaTypeMap.put("bit", "Boolean");
        javaTypeMap.put("char", "String");
        javaTypeMap.put("varchar", "String");
        javaTypeMap.put("varchar2", "String");
        javaTypeMap.put("tinytext", "String");
        javaTypeMap.put("text", "String");
        javaTypeMap.put("mediumtext", "String");
        javaTypeMap.put("longtext", "String");
        javaTypeMap.put("time", "Date");
        javaTypeMap.put("date", "Date");
        javaTypeMap.put("datetime", "Date");
        javaTypeMap.put("timestamp", "Date");
    }
}