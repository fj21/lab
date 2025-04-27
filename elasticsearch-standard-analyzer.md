# Elasticsearch 标准分析器配置

## 问题描述

当前项目在使用Elasticsearch时遇到以下错误：

```
Failed to parse mapping [_doc]: analyzer [ik_smart] has not been configured in mappings
```

这是因为项目中使用了IK中文分词器（`ik_smart`和`ik_max_word`），但Elasticsearch中没有安装该插件。

## 临时解决方案：使用标准分析器

如果无法安装IK Analysis插件，可以使用Elasticsearch内置的标准分析器（standard analyzer）作为临时替代方案。

### 步骤1：使用标准分析器配置

我们已经创建了一个使用标准分析器的配置类 `ElasticsearchStandardConfig.java`，它不需要IK Analysis插件。

要使用这个配置，请按照以下步骤操作：

1. 确保 `ElasticsearchMappingConfig.java` 类被禁用（可以通过添加 `@Component("disabled")` 或者删除 `@Component` 注解）
2. 确保 `ElasticsearchStandardConfig.java` 类被启用（保留 `@Component` 注解）
3. 将 `User.java` 实体类中的分析器从 `ik_smart` 改回 `standard`

### 步骤2：修改User实体类

将 `User.java` 中的字段注解修改为使用标准分析器：

```java
/**
 * 用户名
 */
@Field(type = FieldType.Text, analyzer = "standard")
private String username;

/**
 * 个性签名
 */
@Field(type = FieldType.Text, analyzer = "standard")
private String signature;
```

### 步骤3：重启应用

修改完成后，重启Spring Boot应用。

## 注意事项

1. 标准分析器对中文的分词效果不如IK分析器，可能会影响搜索结果的准确性
2. 这只是一个临时解决方案，建议在条件允许的情况下安装IK Analysis插件
3. 如果之前已经创建了使用IK分析器的索引，需要先删除索引再重新创建

## 长期解决方案

长期解决方案仍然是安装IK Analysis插件，请参考 `elasticsearch-ik-plugin-setup.md` 文件中的说明。
