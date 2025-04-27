# Elasticsearch IK Analysis Plugin Setup

## 问题描述

当前项目在使用Elasticsearch时遇到以下错误：

```
Failed to parse mapping [_doc]: analyzer [ik_smart] has not been configured in mappings
```

这是因为项目中使用了IK中文分词器（`ik_smart`和`ik_max_word`），但Elasticsearch中没有安装该插件。

## 解决方案

### 1. 安装IK Analysis插件

在Elasticsearch服务器上执行以下命令安装IK Analysis插件：

```bash
# 进入Elasticsearch安装目录的bin目录
cd /path/to/elasticsearch/bin

# 安装IK Analysis插件
# 注意：版本号需要与Elasticsearch版本匹配
./elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.17.9/elasticsearch-analysis-ik-7.17.9.zip

# 如果上面的命令失败，请尝试以下命令（使用备用仓库）
./elasticsearch-plugin install https://github.com/infinilabs/analysis-ik/releases/download/v7.17.9/elasticsearch-analysis-ik-7.17.9.zip
```

> 注意：请将上面的版本号（7.17.9）替换为你实际使用的Elasticsearch版本号。

### 2. 重启Elasticsearch服务

安装完插件后，需要重启Elasticsearch服务：

```bash
# 如果是systemd管理的服务
sudo systemctl restart elasticsearch

# 或者如果是手动启动的
# 先停止
kill -SIGTERM <elasticsearch_pid>
# 再启动
cd /path/to/elasticsearch/bin
./elasticsearch -d
```

### 3. 验证插件安装

可以通过以下API验证插件是否安装成功：

```bash
curl -X GET "localhost:9200/_cat/plugins?v"
```

应该能看到类似以下输出：

```
name    component    version    description
node-1  analysis-ik  7.17.9     IK Analyzer for Elasticsearch
```

### 4. 测试IK分词器

可以通过以下API测试IK分词器：

```bash
curl -X POST "localhost:9200/_analyze" -H 'Content-Type: application/json' -d'
{
  "analyzer": "ik_smart",
  "text": "中华人民共和国"
}
'
```

如果一切正常，应该能看到分词结果。

## 项目配置说明

本项目已经在代码中添加了必要的配置，包括：

1. `ElasticsearchMappingConfig.java` - 负责创建索引并配置IK分词器
2. 更新了 `User.java` 实体类，将 `username` 和 `signature` 字段的分析器从 `standard` 改为 `ik_smart`

只要Elasticsearch服务器上正确安装了IK Analysis插件，项目就应该能够正常运行。

## 常见问题

1. **版本不匹配**：确保IK Analysis插件的版本与Elasticsearch版本完全一致
2. **权限问题**：安装插件可能需要管理员权限
3. **网络问题**：如果无法从GitHub下载，可以手动下载插件并安装：
   ```bash
   # 手动下载插件
   wget https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.17.9/elasticsearch-analysis-ik-7.17.9.zip
   # 或者备用仓库
   wget https://github.com/infinilabs/analysis-ik/releases/download/v7.17.9/elasticsearch-analysis-ik-7.17.9.zip

   # 然后安装本地文件
   ./elasticsearch-plugin install file:///path/to/downloaded/elasticsearch-analysis-ik-7.17.9.zip
   ```

4. **找不到文件错误**：如果遇到 `FileNotFoundException` 错误，请检查URL是否正确，并确保网络连接正常。可以尝试使用备用仓库或手动下载方式。
