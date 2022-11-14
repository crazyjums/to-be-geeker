# ElasticSearch

## 一、安装

### 1.1 docker安装

拉取镜像：

```
docker pull elasticsearch:7.17.7
```

启动镜像：

```
docker run -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:7.17.7
```

启动完成之后，在浏览器输入：http://localhost:9200，如果看到以下信息，表示成功。

或者用postman向http://localhost:9200地址发送GET请求，也能得到如下结果。

```json
{
    "name": "74161fdd8f45",
    "cluster_name": "docker-cluster",
    "cluster_uuid": "Awq40Zk4TayaXFfjxoRj1Q",
    "version": {
        "number": "7.17.7",
        "build_flavor": "default",
        "build_type": "docker",
        "build_hash": "78dcaaa8cee33438b91eca7f5c7f56a70fec9e80",
        "build_date": "2022-10-17T15:29:54.167373105Z",
        "build_snapshot": false,
        "lucene_version": "8.11.1",
        "minimum_wire_compatibility_version": "6.8.0",
        "minimum_index_compatibility_version": "6.0.0-beta1"
    },
    "tagline": "You Know, for Search"
}
```

## 二、用法

### 0 引言

索引名：`my-index`

es的服务器地址：`docker-es-url`

### 1 基本操作

#### （1）健康检查 | GET

```
GET {{docker-es-url}}/_cat/health?v
```

结果：

```
epoch      timestamp cluster        status node.total node.data shards pri relo init unassign pending_tasks max_task_wait_time active_shards_percent
1667833273 15: 01: 13  docker-cluster yellow          1         1      5   5    0    0        2             0                  -                 71.4%
```

### 2 索引操作

#### （1）创建index | PUT

```bash
GET {{docker-es-url-url}}/{{index-name}}
```

其中`docker-es-url`表示es的服务器地址，如果是本地的话，一般是`http://localhost:9200`，如果是工程项目中，用具体的IP替换即可，`index-name`表示你要创建的索引的名称，只要不重复，这个名称可以任意，以下是示例：

```
PUT {{docker-es-url}}/my-index
```

结果：

```json
{
    "acknowledged": true,
    "shards_acknowledged": true,
    "index": "my-index2"
}
```

#### （2）查询ES中所有的index | GET

```BASH
GET {{docker-es-url}}/_cat/indices?v
```

结果：

```
health status index            uuid                   pri rep docs.count docs.deleted store.size pri.store.size
green  open   .geoip_databases xOQ5T5XBRSSaJoV-iJRx2w   1   0         40            0     38.4mb         38.4mb
yellow open   my-index         VXJAfSD0RruAHcEG6Q5LxA   1   1          3            0     11.1kb         11.1kb
```

#### （3）给index新增mapping字段 | PUT

```
PUT {{docker-es-url}}/my-index/_mapping
requestBody:
{
    "properties": {
        "map_name": {
            "type": "text"
        }
    }
}
```

结果：

```json
{
    "acknowledged": true
}
```

#### （4）查询index下所有的mapping | GET

```
GET {{docker-es-url}}/my-index/_mapping
```

结果：

```json
{
    "my-index": {
        "mappings": {
            "properties": {
                "address": {
                    "type": "text"
                },
                "address2": {
                    "type": "text"
                },
                "age": {
                    "type": "long"
                },
                "name": {
                    "type": "text"
                },
                "sex": {
                    "type": "text"
                }
            }
        }
    }
}
```

如果想给`_mapping`中的新增字段设置一个初始值，可以使用下面的语句：

```
POST my-index/_update_by_query
{
  "script": {
    "lang": "painless",
    "source": "if (ctx._source.new_field == null) {ctx._source.new_field = 0}"
  }
}
```

`_update_by_query`底层工作原理是，先创建一个文档的快照，然后再修改每一个匹配的文档，有可能会出现以下异常。

> When you submit an update by query request, Elasticsearch gets a snapshot of the data stream or index when it begins processing the request and updates matching documents using `internal` versioning.

```
version conflict, required seqNo [607], primary term [16]. current document has seqNo [608] and primary term [16]
```

出现该异常的原因是：当前线程再修改该文档的值的同时，有其他线程也在修改该文档，导致冲突了。

解决办法：

1. 可以再请求url中添加参数，忽略该冲突：

```
POST test/_update_by_query?conflicts=proceed
```

2. 在使用update_by_query更新时，加上参数wait_for_completion=false。加上这个参数后，更新请求会返回一个这个更新请求对应的任务的 task id。之后在进行下一次查询前，可以根据该id，查询任务是否完成，在完成后再进行后续操作。

### 3 数据的增删改查

所谓词条查询，也就是ES不会对查询条件进行分词处理，只有当词条和查询字符串完全匹配时，才会被查询到。

#### 3.1 查询数据

##### （1）模糊查询数据 | GET

```
GET {{docker-es-url}}/my-index/_search
requestBody:
{
    "query": {
        "match": {
            "sex": "女"
        }
    }
}
```

结果：

```json
{
    "took": 1,//耗时
    "timed_out": false,//是否超时
    "_shards": {//分片
        "total": 1,//一共有几个分片
        "successful": 1,
        "skipped": 0,
        "failed": 0
    },
    "hits": {//命中数据
        "total": {//命中的数据总数
            "value": 1,//总数
            "relation": "eq"
        },
        "max_score": 0.9808291,
        "hits": [//命中的数据
            {
                "_index": "my-index",//数据所在索引
                "_type": "_doc",//数据所在类型
                "_id": "GjuOUoQBlYWTOam_he6t",//数据id
                "_score": 0.9808291,
                "_source": {//数据
                    "name": "王五",
                    "sex": "女",
                    "age": 82
                }
            }
        ]
    }
}
```

`took` – 查询花费时长（毫秒）
`timed_out` – 请求是否超时
`_shards` – 搜索了多少分片，成功、失败或者跳过了多个分片（明细）
`max_score` – 最相关的文档分数
`hits.total`.value - 找到的文档总数
`hits.sort` - 文档排序方式 （如没有则按相关性分数排序）
`hits._score` - 文档的相关性算分 (match_all 没有算分)

##### （2）精确查询数据 | GET

如果要全部匹配而不是仅仅是包含关键字之一，你需要使用 `match_phrase` 而不是 `match`。(match 和 match_phrase 的区别就像是 or 和 and 的区别)。

```
GET {{docker-es-url}}/my-index/_search
requestBody:
{
    "query": {
        "match_phrase": {
            "sex": "女"
        }
    }
}
```

##### （3）根据id查询数据 | GET

```
GET {{docker-es-url}}/my-index/_doc/HDuuUoQBlYWTOam_WO5n
```

其中，`HDuuUoQBlYWTOam_WO5n`为文档数据的id

结果：

```json
{
    "_index": "my-index",
    "_type": "_doc",
    "_id": "HDuuUoQBlYWTOam_WO5n",
    "_version": 2,
    "_seq_no": 9,
    "_primary_term": 1,
    "found": true,
    "_source": {
        "address": "未知地址",
        "sex": "女",
        "name": "钱力",
        "age": 100
    }
}
```

##### （4）查询某个字段是否为空 | GET

```
GET pepper_group/_search
{
  "query": {
    "bool": {
      "must_not": {
        "exists": {
          "field": "字段名"
        }
      }
    }
  }
}
```

结果：

如果返回结果为空，则表示ES中所有数据该字段都有值，不是空。

```json
{
  "took" : 7,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 0,
      "relation" : "eq"
    },
    "max_score" : null,
    "hits" : [ ]
  }
}
```

如果结果不为空，则表示所有数据中有数据该字段为空，返回的数据就表示该字段为空的数据。

##### （5）某个字段按范围查找 | GET

```
GET my-index/_search
{
	"query": {
		"range": {
			"field" 
		}
	}
}
```



#### 3.2 删除数据

##### （1）根据主键删除数据 | DELETE

```
DELETE {{docker-es-url}}/my-index/_doc/GzukUoQBlYWTOam_we7z
```

其中`my-index`为索引名，`_doc`表示文档类型，`GzukUoQBlYWTOam_we7z`表示某个文件的主键

结果：

```json
{
    "_index": "my-index",
    "_type": "_doc",
    "_id": "GzukUoQBlYWTOam_we7z",
    "_version": 2,
    "result": "deleted",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 4,
    "_primary_term": 1
}		
```

##### （2）根据匹配条件删除数据 | POST

```
POST {{docker-es-url}}/my-index/_delete_by_query
requestBody:
{
    "query": {
        "term": {
            "_id": "GTuOUoQBlYWTOam_X-4B"
        }
    }
}
```

结果：

```json
{
    "took": 51,
    "timed_out": false,
    "total": 1,
    "deleted": 1,
    "batches": 1,
    "version_conflicts": 0,
    "noops": 0,
    "retries": {
        "bulk": 0,
        "search": 0
    },
    "throttled_millis": 0,
    "requests_per_second": -1.0,
    "throttled_until_millis": 0,
    "failures": []
}
```

#### 3.3 更新数据

##### （1）根据id更新数据

```
POST {{docker-es-url}}/my-index/_doc/HDuuUoQBlYWTOam_WO5n/_update
reqyestBody:
{
    "doc": {
        "age": 10
    }
}
```

其中，`HDuuUoQBlYWTOam_WO5n`为需要更新的文档的id

结果：

```josn
{
    "_index": "my-index",
    "_type": "_doc",
    "_id": "HDuuUoQBlYWTOam_WO5n",
    "_version": 3,
    "result": "updated",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 12,
    "_primary_term": 1
}
```

##### （2）根据查询条件更新数据 | POST

```
POST {{docker-es-url}}/my-index/_doc/_update_by_query
requestBody:
{
    "script": {
        "source": "ctx._source.age=300;ctx._source.who_can_join=100;"
    },
    "query": {
        "bool": {
            "must": {
                "match": {
                    "name": "li"
                }
            }
        }
    }
}
```

结果：

```json
{
    "took": 556,
    "timed_out": false,
    "total": 2,
    "updated": 2,
    "deleted": 0,
    "batches": 1,
    "version_conflicts": 0,
    "noops": 0,
    "retries": {
        "bulk": 0,
        "search": 0
    },
    "throttled_millis": 0,
    "requests_per_second": -1.0,
    "throttled_until_millis": 0,
    "failures": []
}
```

#### 3.4 新增数据

##### （1）直接增加数据 | PUT

```
PUT /my-index/_doc/1
{
  "name": "John Doe"
}
```

这个请求（如不存在）将自动创建一个 ID 为 1 的新文档，并储存键值对，并为其建立索引。

```json
{
  "_index" : "my-index",
  "_type" : "_doc",
  "_id" : "1",
  "_version" : 1,
  "result" : "created",
  "_shards" : {
    "total" : 2,
    "successful" : 2,
    "failed" : 0
  },
  "_seq_no" : 26,
  "_primary_term" : 4
}
```

## 三、kibana与ES连接

### 1 简介

Kibana 是为 Elasticsearch设计的开源分析和可视化平台。你可以使用 Kibana 来搜索，查看存储在 Elasticsearch 索引中的数据并与之交互。你可以很容易实现高级的数据分析和可视化，以图表的形式展现出来。
使用前我们肯定需要先有Elasticsearch啦，安装使用Elasticsearch可以参考[Elasticsearch构建全文搜索系统](https://www.cnblogs.com/chenqionghe/p/12496827.html)

下面分别演示一下Kibana的安装、自定义索引，搜索，控制台调用es的api和可视化等操作，特别需要注意的是，控制台可以非常方便的来调用es的api，强烈推荐使用

#### （1）版本选择

ES与kibana的版本需要一致，才能连接上，否则会提示如下界面，[版本对照表](https://www.elastic.co/cn/support/matrix#matrix_compatibility)。

![image-20221110225823354](.img/image-20221110225823354.png)



## Reference

1. https://z.itpub.net/article/detail/A7B79869961FB96969AADEA98959D9FC
1. https://endymecy.gitbooks.io/elasticsearch-guide-chinese/content/getting-started/modifying-data.html
1. https://blog.csdn.net/qq_41100433/article/details/80604168
1. https://blog.csdn.net/imHanweihu/article/details/122043486
1. https://www.vinchin.com/blog/vinchin-technique-share-details.html?id=12756
1. https://blog.csdn.net/cooper20/article/details/108780376
1. https://endymecy.gitbooks.io/elasticsearch-guide-chinese/content/index.html
1. [Elasticsearch中文文档](https://learnku.com/docs/elasticsearch73/7.3)
1. [Kibana详细入门教程](https://www.cnblogs.com/chenqionghe/p/12503181.html)
1. [Kibana报错：Login is currently disabled. Administrators should consult the Kibana logs for more details](https://blog.csdn.net/qq_38038143/article/details/101311887)
1. [Elasticsearch和Kibana版本对应关系](https://www.elastic.co/cn/support/matrix#matrix_compatibility)
1. [Elastic Search Getting Version Conflict even while updating document sequentially using update_by_query](https://stackoverflow.com/questions/63505214/elastic-search-getting-version-conflict-even-while-updating-document-sequentiall)
1. [elasticsearch update_by_query ConflictError](https://blog.csdn.net/u010483897/article/details/103526227)



