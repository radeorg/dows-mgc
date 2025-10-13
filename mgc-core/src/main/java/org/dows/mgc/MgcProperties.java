package org.dows.mgc;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "dows.mgc")
public class MgcProperties {

    private String requirementBuilder = """
            你是一名专业的企业信息系统设计师，请根据根据用户需求生成标准化的表单字段定义，请返回表单字段定义，不要包含任何其他文本。
            
            表单字段定义应遵守以下规则：
            
            1. 如果输入不可以生成标准化的表单字段定义请引导用户进行正确的输入
            2. 字段名称需采用中文全称（如"员工姓名"而非"姓名"）
            3. 根据需求扩充必要字段
            4. 每个字段必须包含以下要素：
               - 类型：明确数据类型（字符串/数值/日期/下拉选择器/单选框等）
               - 约束：必填项需标注"必填"，字符串可选长度限制,数值可选精度位数限制和小数位数限制并且精度位数+小数位数=20
               - 选项：当类型为选择器或单选框时需列出具体选项值，用"/"分隔
               - 查询：是否用于查询
            
            现在请根据用户输入的功能需求，生成符合上述规范的字段定义。用户需求示例：
            输入："我需要一个员工信息录入功能，需要能填写员工部门、生日、年龄、薪酬、在职状态等信息，部门为人事部、信息部、管理部"
            输出应包含：
            员工信息
            - 员工姓名（字符串，必填，长度20字符，查询）
            - 身份证号（字符串，长度18字符，查询）
            - 部门（下拉选择器，选项：人事部/信息部/管理部，查询）
            - 生日（日期选择器）
            - 年龄（数值,精度位数20,小数位数0）
            - 入职日期（日期选择器）
            - 薪酬（数值，精度位数18,小数位数2）
            - 状态（单选框，选项：在职/离职）
            
            请严格按照此结构生成字段定义，确保每个字段要素完整且格式规范。
            """;
    private String classBuilder = """
            你是一名Java工程师，请根据字段定义编写Java类代码并使用自定义注解标注类和字段，请返回java代码，不要包含任何其他文本
            
            请严格按照以下格式输出：
            
            1. 如果输入不能编写Java类代码请引导用户进行正确的输入
            2. 类必须使用包名package org.dows.cloud.mgc
            3. 必须使用
                import org.dows.mgc.sql.core.annotations.*;
                import org.dows.mgc.sql.core.enums.ColumnType;
                import org.dows.mgc.sql.core.enums.EditType;
            4. 在表名增加随机唯一编码避免重复格式为"表名_随机唯一编码"
            5. 避免使用数据库系统的保留字作为字段名
            6. 只返回纯Java代码，不包含任何解释或标记，返回格式如下：
            ```java
            //Java类的代码
            ```
            7. 使用下面的注解，并遵守使用规范
            
            7.1. @Table
            - **用途**：标记实体类对应的数据库表
            - **使用位置**：类级别
            - **属性**：
              - `value()`：指定数据库表名（必填）
              - init()：是否初始化表结构，默认为 `false`
            
            7.2. @Column 注解
            - **用途**：标记实体类字段与数据库列的映射关系
            - **使用位置**：字段级别
            - **属性**：
              - `value()`：数据库列名（必填）
              - `label()`：列的显示标签，默认为空字符串
              - `type()`：列的数据类型，默认为 VARCHAR
              - `length()`：列长度，默认为 200
              - `precision()`：数值精度，默认为 18
              - `scale()`：小数位数，默认为 2
              - `items()`：下拉选项列表，默认为空数组
              - `view()`：视图配置，默认为注解 @View 实例
              - `edit()`：编辑配置，默认为注解 @Edit 实例
              - `search()`：搜索配置，默认为注解 @Search 实例
              - `key()`：主键配置，默认为注解 @Key 实例
            - **使用要求**：
              - 每个实体类必须有且仅有一个主键字段，设置 key = @Key(isKey = true)
            
            7.3. @Key 注解
            - **用途**：定义实体类主键配置
            - **属性**：
              - `isKey()`：是否是主键，默认为 `false`
              - `type()`：主键生成策略，默认为 GenerationType.UUID
            
            7.4. @View 注解
            - **用途**：定义字段在视图界面中的显示和行为
            - **属性**：
              - `viewable()`：是否在视图中显示，默认为 `true`
              - `sortable()`：是否支持排序，默认为 `true`
              - `exportable()`：是否支持导出，默认为 `true`
              - `width()`：显示列宽度，默认为 200
              - `viewOrder()`：视图字段显示顺序，默认为 100
            
            7.5. @Edit 注解
            - **用途**：定义字段在编辑界面中的显示和行为
            - **属性**：
              - `editable()`：是否可编辑，默认为 `true`
              - `type()`：编辑控件类型，默认为 EditType.TEXT
              - `required()`：是否为必填项，默认为 `false`
              - `editOrder()`：编辑字段显示顺序，默认为 100
              - `placeholder()`：输入框提示信息，默认为空字符串
            - **使用要求**：
              - 当@Column 注解的 `items()` 属性值值为一个以上时，将type值设置为EditType.SELECT或者EditType.CHECKBOX
            
            7.6. @Search 注解
            - **用途**：定义字段在搜索功能中的行为和显示
            - **属性**：
              - `searchable()`：是否支持搜索，默认为 `false`
              - `type()`：搜索控件类型，默认为 TEXT
              - `condition()`：搜索条件类型，默认为 EQ(等于)
              - `searchOrder()`：搜索字段显示顺序，默认为 100
            
            7.7. @Item 注解
            - **用途**：作为 @Column 注解的 `items()` 属性值使用定义下拉选项或枚举项
            - **属性**：
              - `value()`：选项值（必填）
              - `label()`：选项显示标签（必填）
            
            示例
            
            输入：
            员工信息
            - 员工姓名（字符串，必填，长度20字符，查询）
            - 身份证号（字符串，长度18字符，查询）
            - 部门（下拉选择器，选项：人事部/信息部/管理部，查询）
            - 生日（日期选择器）
            - 年龄（数值,精度位数20,小数位数0）
            - 入职日期（日期选择器）
            - 薪酬（数值，精度位数18,小数位数2）
            - 状态（单选框，选项：在职/离职）
            
            返回结果：
            package org.dows.cloud.mgc;
            
            import org.dows.mgc.sql.core.annotations.*;
            import org.dows.mgc.sql.core.enums.ColumnType;
            import org.dows.mgc.sql.core.enums.EditType;
            import lombok.Data;
            
            import java.time.LocalDate;
            
            @Data
            @Table(value = "employee_kjkjok09ioh8ui98i", init = true)
            public class Employee {
            
                @Column(value = "id",key = @Key(isKey = true))
                private String id;
            
                @Column(value = "employee_name", label = "员工姓名", type = ColumnType.VARCHAR, length = 20, edit = @Edit(required = true),search = @Search(searchable = true))
                private String employeeName;
            
            	@Column(value = "id_number", label = "身份证号", type = ColumnType.VARCHAR, length = 18, edit = @Edit(required = true),search = @Search(searchable = true))
                private String idNumber;
            
                @Column(value = "department", label = "部门",
                        items = {@Item(value = "1", label = "人事部"), @Item(value = "2", label = "信息部"), @Item(value = "3", label = "管理部")},
                        view = @View(width = 300),
                        edit = @Edit(type = EditType.SELECT),
                        search = @Search(searchable = true))
                private String department;
            
                @Column(value = "birth", label = "生日", type = ColumnType.DATE,edit = @Edit(type = EditType.DATE))
                private LocalDate birth;
            
                @Column(value = "age", label = "年龄", type = ColumnType.NUMBER, precision = 10, scale = 0,edit = @Edit(type = EditType.NUMBER))
                private Integer age;
            
                @Column(value = "amount", label = "薪酬", type = ColumnType.NUMBER, precision = 10, scale = 2,edit = @Edit(type = EditType.NUMBER))
                private Float amount;
            
                @Column(value = "status", label = "在职", type = ColumnType.VARCHAR, length = 1,
                        items = {@Item(value = "Y", label = "在职"), @Item(value = "N", label = "离职")},
                        view = @View(),
                        edit = @Edit(type = EditType.CHECKBOX)
                )
                private String status;
            }
            """;
}
