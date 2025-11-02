-- 若库不存在创建一个
CREATE DATABASE IF NOT EXISTS `${(database.name?replace("([a-z])([A-Z]+)","$1_$2","r")?lower_case)!""}`;
USE `${(database.name?replace("([a-z])([A-Z]+)","$1_$2","r")?lower_case)!""}`;

<#list tables as table>
drop table if exists `${(table.name?replace("([a-z])([A-Z]+)","$1_$2","r")?lower_case)!"tb_"+table_index}`;
CREATE TABLE IF NOT EXISTS `${(table.name?replace("([a-z])([A-Z]+)","$1_$2","r")?lower_case)!"tb_"+table_index}`(
    <#list table.columns as column>
        <#if column??!>
    `${(column.name?replace("([a-z])([A-Z]+)","$1_$2","r")?lower_case)!""}` <#if column.typ??>${column.typ!""}<#if (column.length)?? && (column.length?length gt 0)>(${column.length!""})</#if></#if><#if column.unsigned?? && column.unsigned> UNSIGNED</#if><#if column.required?? && column.required == "1"> NOT NULL<#else> DEFAULT NULL</#if><#if (column.required?? && column.required == "1") && (column.def??)> DEFAULT '${column.def!""}'</#if><#if column.autoIncrement?? && column.autoIncrement> AUTO_INCREMENT</#if><#if column.onUpdate?? && column.onUpdate?length gt 0> ${column.onUpdate!""}</#if> COMMENT '${column.descr!""}',
        </#if>
    </#list>
    <#if table.pks??>
    ${table.pks!""} <#if table.keys?? || table.uniques??>,</#if>
    </#if>
    <#if table.keys??>
    ${table.keys!""} <#if table.uniques??>,</#if>
    </#if>
    <#if table.uniques??>
    ${table.uniques!""}
    </#if>
) ENGINE=InnoDB COMMENT='${table.comment!""}';

</#list>

