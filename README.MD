# 程序员称为极客之路

## 一、数据库

### 1.1 [MySQL](./MySQL)

1. [MySQL查询原理](./MySQL/MySQL查询原理.md)
2. [MySQL索引原理](./MySQL/MySQL索引原理.md)
3. [MySQL修改大数据表结构时的风险与解决办法](./MySQL/MySQL修改大数据表结构时的风险与解决办法.md)
4. [MySQL中truncate和delete删除数据的区别](./MySQL/MySQL中truncate和delete删除数据的区别.md)
5. [MySQL最左匹配原则](./MySQL/MySQL最左匹配原则.md)
6. [update table set a = a+ 1 where id = 1是否存在线程安全问题](./MySQL/update%20table%20set%20a%20%3D%20a%2B%201%20where%20id%20%3D%201是否存在线程安全问题.md)
7. [MySQL 是怎么加行级锁的？](./MySQL/MySQL 是怎么加行级锁的？.md)（为什么使用`select * form table ... for update`d的查询条件不走索引会所全表） | [reference](https://mp.weixin.qq.com/s/Ef73pSWb_k6yiTTlNCrEjg)
8. [mysql幻读](./MySQL/mysql幻.md) |[reference](https://mp.weixin.qq.com/s?__biz=MzUxODAzNDg4NQ==&mid=2247520893&idx=1&sn=7152fa58a1d03b48cfe8ef00e90aa2ea&chksm=f98dd6d7cefa5fc13877a1c99dcb06d03f51cbbc9afee75d0cd87f4847566746fcced9948e54&scene=21#wechat_redirect)
9. [美团二面：考我幻读，结果答的不好](./MySQL/美团二面：考我幻读，结果答的不好) | [reference](https://mp.weixin.qq.com/s?__biz=MzUxODAzNDg4NQ==&mid=2247520075&idx=1&sn=3119bcb4f3c2dde7dfa2070f26816eed&chksm=f98dcbe1cefa42f76d1d944898ad1ed12ac645aa41fbc03405e33970eb62e3b2b73ca1de858b&scene=21&key=c7cacdfa2c0e8a2c8da558ec2d4f4c79055bb9a8a62280d247de763ce7b864c9141393abfb9d7cc5ded9736d73010add6e9c83f4c2c74569e4b9ad36167a95262550cbab43fdeb1174caad38681430a9eca4fb6fc4e4efff7d3ccd15f650461c0181486f9c3e2533da80b041c2f1a5ad19e2489f8c0483d8fa64d8f0d28badb4&ascene=1&uin=MTc4MzkyNDIyNA%3D%3D&devicetype=Windows+10+x64&version=63080014&lang=zh_CN&exportkey=n_ChQIAhIQ7eodp%2FOPONIwokDZhnlEkhLqAQIE97dBBAEAAAAAABYlOKHmNQMAAAAOpnltbLcz9gKNyK89dVj0ufWmzo0aqJHfq%2FBVxo5SoDoTNilgEfAiKknnjplGl44VKcqgbUtJNL0SyUcj5ezryDeqys%2B1%2FYkNmAa5XMbmZWU%2FV1cuZXb85xptMLhxoOufwJSXwiSj19SP%2Fn4Gs920AO7YJrfLMyiQ5W22MZcpk6WC0HA4hnW0nTH6a7gOyrifxm3IgkrPneeK0C2RuXHnJ5oIkx5GJ51ShOZq0ClNhtiNKKP9RfDHjj5HdD2rnCqWudC4RzHs5slZlCs2Gjlv9wWbsQ%3D%3D&acctmode=0&pass_ticket=yOpJo0zYyJqrfgC4VkReOlPqL01cwvktXKCQYX35jC88CGFdUDKJ9uP6KzbWur4x&wx_header=0&fontgear=2)

### 1.2 [Redis](./redis)

## 二、编程语言

### 2.1 编译语言

#### （1）[Java](./Java)

1. [idea集成Docker实现SpringBoot微服务镜像打包一键部署](./Java/idea集成Docker实现SpringBoot微服务镜像打包一键部署.MD)
2. [java垃圾回收机制](./Java/java垃圾回收机制.md)
3. [java类加载机制](./Java/java类加载机制.md)
4. [java类加载机制](./Java/java类加载机制.md)
5. [Java如何优雅的写代码](./Java/Java如何优雅的写代码.md)
6. [java中的静态代码块、构造代码块以及构造函数的初始化顺序](./Java/java中的静态代码块、构造代码块以及构造函数的初始化顺序.md)
7. [Java中arraylist.remove提示索引越界异常](./Java/Java中arraylist.remove提示索引越界异常.md)
8. [JVM垃圾收集时的并发可达性分析](./Java/JVM垃圾收集时的并发可达性分析.md)
9. [jvm虚拟机性能监控与故障处理工具](./Java/jvm虚拟机性能监控与故障处理工具.md)
10. [springboot+mybatis读写分离](./Java/springboot+mybatis读写分离.md)
11. [springBoot操作redis](./Java/springBoot操作redis.md)

#### （2）[Golang](./Go)

1. [go引入三方包](./Go/go引入三方包.md)
2. [go语言特征](./Go/go语言特征.md)
3. [Go语言学习笔记.md](./Go/Go语言学习笔记.md)
4. [GO语言中的闭包.md](./Go/GO语言中的闭包.md)

### 2.2 脚本语言

#### （1）[PHP](./php)

1. [cgi、php-cgi、fast-cgi、php-fpm的优缺点以及区别？](./php/cgi、php-cgi、fast-cgi、php-fpm的优缺点以及区别？.md)
2. [opcache工作原理](./php/opcache工作原理.md)

#### （2）shell

#### （3）python

#### （4）lua

#### （5）JavaScript

## 三、中间件

### 3.1 [消息队列](./middleware/messageQueue)

#### （1）kafka

#### （2）ActiveMQ

#### （3）RocketMQ

#### （4）RabbitMQ

### 3.2 全文检索

#### （1）[ElasticSearch](./middleware/elasticsearch)

## 四、云原生

### 4.1 [docker](./docker)

### 4.2 [kubernetes](./kubernestes)

## 五、[计算机网络](./network)

1. [在浏览器地址栏输入一个URL后回车，背后会进行哪些技术步骤](./network/%E5%9C%A8%E6%B5%8F%E8%A7%88%E5%99%A8%E5%9C%B0%E5%9D%80%E6%A0%8F%E8%BE%93%E5%85%A5%E4%B8%80%E4%B8%AAURL%E5%90%8E%E5%9B%9E%E8%BD%A6%EF%BC%8C%E8%83%8C%E5%90%8E%E4%BC%9A%E8%BF%9B%E8%A1%8C%E5%93%AA%E4%BA%9B%E6%8A%80%E6%9C%AF%E6%AD%A5%E9%AA%A4.md)
2. [字节一面：TCP 三次握手，问的好细！](./network/字节一面：TCP 三次握手，问的好细！.md) | [reference](https://mp.weixin.qq.com/s?__biz=MzUxODAzNDg4NQ==&mid=2247521478&idx=1&sn=66528a27aa3556b40f4a0ad64a43045f&chksm=f98dd06ccefa597aed91d1cfc8234f0b65123c141e4b7f7246e387735b0594879a623f4e085e&scene=21&key=97d114acb76dd546d4ff231544e299e852b96658e939e38083d848d06ade5e704751d3da6f42009c3df15e2e92b1ca8c0e71293a0944a2d6a6f957a4f1ded6dcb4f3cba8cf744624f1189c6af2aa1daba528e11577b1a3d5d2e278951335e0283261c823d8580215a2e9605d3bbc3a72eee267e8284428251c7a8cd60234788c&ascene=7&uin=MTc4MzkyNDIyNA%3D%3D&devicetype=Windows+10+x64&version=63080014&lang=zh_CN&session_us=gh_fc46cf2353fc&exportkey=n_ChQIAhIQTrymq28Os5ptwK79m8qeuBLvAQIE97dBBAEAAAAAAO47KqUijqYAAAAOpnltbLcz9gKNyK89dVj02v34X1W0F4sQnjXWmBZZ0a4QBgA002NDVtNyLfYOXRsbYiX%2FBi7G1Uu0Oa8e1gIwvF5EIyHKMMfa9fsdyAfIPJ43cTamfxW0l49XkkUPLcx23BghHYTSnq5i3tOUEka%2Bdr888ENXZi9DfEgE23SmONthJAToElFbRtfrZQEcog2ZAaSWC0B9bkZ%2BczfNxHxmolmAhL9CEnSg03KlxziNrXpCs8gmHAclxa18lZCuwIGKCwSR1Fu18KVzBmGMJcY%2FuOmatB40OKOr&acctmode=0&pass_ticket=yOpJo0zYyJqrfgC4VkReOlPqL01cwvktXKCQYX35jC%2BGDXUbRhJFE5c7yOXcKxIz&wx_header=0&fontgear=2)
3. [TCP 重传、滑动窗口、流量控制、拥塞控好难？看完图解就不愁了](./network/TCP 重传、滑动窗口、流量控制、拥塞控好难？看完图解就不愁了.md) | [reference](https://mp.weixin.qq.com/s/toQUgqJHeUbFZMtxfzFCxw)
4. [美团二面：TCP 四次挥手，可以变成三次吗？](./network/美团二面：TCP 四次挥手，可以变成三次吗？.md) | [reference](https://mp.weixin.qq.com/s/JUt-N0VxF_lXACFr6WJN8w)
5. [硬不硬你说了算！近 40 张图解被问千百遍的 TCP 三次握手和四次挥手面试题](./network/硬不硬你说了算！近 40 张图解被问千百遍的 TCP 三次握手和四次挥手面试题.md) | [reference](https://mp.weixin.qq.com/s/tH8RFmjrveOmgLvk9hmrkw)
6. [告别 TCP！](./network/告别 TCP！.md) | [reference](https://mp.weixin.qq.com/s/3rM_RlwejkMFWm0GdwK6Hg)

7. [在浏览器地址栏输入一个URL后回车，背后会进行哪些技术步骤](./network/%E5%9C%A8%E6%B5%8F%E8%A7%88%E5%99%A8%E5%9C%B0%E5%9D%80%E6%A0%8F%E8%BE%93%E5%85%A5%E4%B8%80%E4%B8%AAURL%E5%90%8E%E5%9B%9E%E8%BD%A6%EF%BC%8C%E8%83%8C%E5%90%8E%E4%BC%9A%E8%BF%9B%E8%A1%8C%E5%93%AA%E4%BA%9B%E6%8A%80%E6%9C%AF%E6%AD%A5%E9%AA%A4.md)
8. [TCPIP协议中的三次握手和四次挥手](./network/TCPIP%E5%8D%8F%E8%AE%AE%E4%B8%AD%E7%9A%84%E4%B8%89%E6%AC%A1%E6%8F%A1%E6%89%8B%E5%92%8C%E5%9B%9B%E6%AC%A1%E6%8C%A5%E6%89%8B.md)
9. [字节一面：服务端挂了，客户端的 TCP 连接还在吗？](./network/字节一面：服务端挂了，客户端的 TCP 连接还在吗？.md) | [reference](https://mp.weixin.qq.com/s?__biz=MzUxODAzNDg4NQ==&mid=2247520003&idx=1&sn=3e70c6b54007ffa3130c5956df232bdb&chksm=f98dcba9cefa42bfbf35fd5f867314d8c96c54f5b285db542be42fc0594706d1429eaa069165&scene=21&key=c7cacdfa2c0e8a2cd6d8af4cb7ee5ec9f94feea488f23e09f9417ad395a6b722b93b7308d5b0b31793906016f47985258e4c95f5fd86256bef57dc6cfcfd5b5968021b2b8e023818e3667589b6bc172e2e8fcba406fbe52aafee535c1d1b30718cd42ac2087b5b6b316f0c39c3b1b5dbdc8cf626d71ba5e989e85f65aadee2dc&ascene=1&uin=MTc4MzkyNDIyNA%3D%3D&devicetype=Windows+10+x64&version=63080014&lang=zh_CN&exportkey=n_ChQIAhIQ75lVlh2cc%2FwcvhPFvN94wBLxAQIE97dBBAEAAAAAAF1HNsX6iQ0AAAAOpnltbLcz9gKNyK89dVj0bim7gxIXrAAJS55c6iDJ9bbjj0IRk3tLcgt5TPpq6rJzAqA5ygLWOpWF1wu3S5xaJWM%2BIFNUiB7RZPuuYeomz88qTl%2FxtPR1fPur4PTv5Pw9vjz0OlrayP%2FL26U7olrM2ZGC%2B3tT7iECgAsqqw4DLmJ7ni%2FhV33scgn3gi7uQu5veFD3VTh7vkEAHdwvl2vHJgJlSm3h8skZzVIQ5gseOSjjCOxwLcwyCriy0cncY%2FHUzWxZyM5Libk79u%2Bg6amMAx2l%2FHfrmAMZ2cQ%3D&acctmode=0&pass_ticket=yOpJo0zYyJqrfgC4VkReOlPqL01cwvktXKCQYX35jC%2FGAdGuFqN58IaPQKpd%2FCc2&wx_header=0&fontgear=2)

#### 5.1 七层协议

#### 5.2 五层协议

## 六、Linux

### 6.1 ansible

### 6.2 awk

### 6.3 sed

### 6.4 [gitlab](./gitlab&cicd)

## 七、数据统计

### 7.1 [Prometheus](./prometheus)

### 7.2 Grfana

## 八、[分布式](./distributed)

1. [分布式事务如何保证数据的一致性](./distributed/分布式事务如何保证数据的一致性.md)

## 九、[数据结构&算法](./dataStructures&Algorithms)

