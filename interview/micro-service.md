# 一、微服务相关面试题

## （1）什么是服务熔断，什么是服务降级 | [link](https://zhuanlan.zhihu.com/p/341939685)

### [1] **服务降级:系统有限的资源的合理协调**

- 概念：服务降级一般是指在服务器压力剧增的时候，根据实际业务使用情况以及流量，对一些服务和页面有策略的不处理或者用一种简单的方式进行处理，从而**释放服务器资源的资源以保证核心业务的正常高效运行。**

- 原因： 服务器的资源是有限的，而请求是无限的。在用户使用即并发高峰期，会影响整体服务的性能，严重的话会导致宕机，以至于某些重要服务不可用。故高峰期为了保证核心功能服务的可用性，就需要对某些服务降级处理。可以理解为舍小保大

- 应用场景： 多用于微服务架构中，一般当整个微服务架构整体的负载超出了预设的上限阈值（和服务器的配置性能有关系），或者即将到来的流量预计会超过预设的阈值时（比如双11、6.18等活动或者秒杀活动）

- 服务降级是从整个系统的负荷情况出发和考虑的，对某些负荷会比较高的情况，为了预防某些功能（业务场景）出现负荷过载或者响应慢的情况，在其内部暂时舍弃对一些非核心的接口和数据的请求，而直接返回一个提前准备好的fallback（退路）错误处理信息。这样，虽然提供的是一个有损的服务，但却保证了整个系统的稳定性和可用性。

- 需要考虑的问题：

- - 区分那些服务为核心？那些非核心
  - 降级策略（处理方式，一般指如何给用户友好的提示或者操作）
  - 自动降级还是手动降

### [2] **服务熔断：应对雪崩效应的链路自我保护机制。可看作降级的特殊情况**

- 概念：应对微服务雪崩效应的一种链路保护机制，类似股市、保险丝
- 原因： 微服务之间的数据交互是通过远程调用来完成的。服务A调用服务，服务B调用服务c，某一时间链路上对服务C的调用响应时间过长或者服务C不可用，随着时间的增长，对服务C的调用也越来越多，然后服务C崩溃了，但是链路调用还在，对服务B的调用也在持续增多，然后服务B崩溃，随之A也崩溃，导致雪崩效应
- 服务熔断是应对雪崩效应的一种微服务链路保护机制。例如在高压电路中，如果某个地方的电压过高，熔断器就会熔断，对电路进行保护。同样，在微服务架构中，熔断机制也是起着类似的作用。当调用链路的某个微服务不可用或者响应时间太长时，会进行服务熔断，不再有该节点微服务的调用，快速返回错误的响应信息。当检测到该节点微服务调用响应正常后，恢复调用链路。
- 服务熔断的作用类似于我们家用的保险丝，当某服务出现不可用或响应超时的情况时，为了防止整个系统出现雪崩，暂时停止对该服务的调用。

在Spring Cloud框架里，熔断机制通过Hystrix实现。Hystrix会监控微服务间调用的状况，当失败的调用到一定阈值，缺省是5秒内20次调用失败，就会启动熔断机制。

-应用场景：微服务架构中，多个微服务相互调用出使用

- 需要考虑问题：

- - 如何所依赖的服务对象不稳定
  - 失败之后如何快速恢复依赖对象，如何探知依赖对象是否恢复

### [3] **服务降级和服务熔断区别**

- 触发原因不一样，服务熔断由链路上某个服务引起的，服务降级是从整体的负载考虑

- 管理目标层次不一样，服务熔断是一个框架层次的处理，服务降级是业务层次的处理

- - 实现方式不一样，服务熔断一般是自我熔断恢复，服务降级相当于人工控制

- 触发原因不同 服务熔断一般是某个服务（下游服务）故障引起，而服务降级一般是从整体负荷考虑；

一句话：

服务熔断是应对系统服务雪崩的一种保险措施，给出的一种特殊降级措施。而服务降级则是更加宽泛的概念，主要是对系统整体资源的合理分配以应对压力。

服务熔断是服务降级的一种特殊情况，他是防止服务雪崩而采取的措施。系统发生异常或者延迟或者流量太大，都会触发该服务的服务熔断措施，链路熔断，返回兜底方法。这是对局部的一种保险措施。

服务降级是对系统整体资源的合理分配。区分核心服务和非核心服务。对某个服务的访问延迟时间、异常等情况做出预估并给出兜底方法。这是一种全局性的考量，对系统整体负荷进行管理。

限流：限制并发的请求访问量，超过阈值则拒绝；

降级：服务分优先级，牺牲非核心服务（不可用），保证核心服务稳定；从整体负荷考虑；

熔断：依赖的下游服务故障触发熔断，避免引发本系统崩溃；系统自动执行和恢复

# 二、zookeeper相关面试题

## （1）zookeeper的应用场景 | [link](https://blog.csdn.net/lovoo/article/details/131445272)

1. 配置中心：Zookeeper可以用来存储和管理配置信息，例如集群中的机器配置、服务地址配置等。通过Zookeeper，可以将配置信息统一管理，同时实现动态加载和更新。
2. 统一命名服务：Zookeeper可以用来实现命名服务，例如将集群中的机器名称和IP地址进行映射，或者将服务的唯一标识和实际地址进行映射。这样，客户端可以通过名称或标识来访问服务，而不需要知道服务的实际地址。
3. 分布式锁：Zookeeper可以用来实现分布式锁，通过创建一个特殊的节点，各个节点可以竞争同一个锁，从而保证分布式系统中的一致性。
4. 分布式队列：Zookeeper可以用来实现分布式队列，通过创建一个特殊的节点，各个节点可以加入或离开队列，同时队列中的节点可以按照一定的顺序进行排序。

## （2）作为服务注册中心，Eureka比Zookeeper好在哪里?

著名的CAP理论指出，一个分布式系统不可能同时满足C(一致性)、A(可用性)和P(分区容错性)。由于分区容错性P在是分布式系统中必须要保证的，因此我们只能在A和C之间进行权衡。

因此，Zookeeper 保证的是CP, Eureka 则是AP。

Zookeeper保证CP

当向注册中心查询服务列表时，我们可以容忍注册中心返回的是几分钟以前的注册信息，但不能接受服务直接down掉不可用。也就是说，服务注册功能对可用性的要求要高于一致性。但是zk会出现这样一种情况，当master节点因为网络故障与其他节点失去联系时，剩余节点会重新进行leader选举。问题在于，选举leader的时间太长，30~120s,且选举期间整个zk集群都是不可用的，这就导致在选举期间注册服务瘫痪。在云部署的环境下，因网络问题使得zk集群失去master节点是较大概率会发生的事，虽然服务能够最终恢复，但是漫长的选举时间导致的注册长期不可用是不能容忍的。

Eureka保证AP

Eureka看明白了这一点，因此在设计时就优先保证可用性。Eureka各个节点都是平等的，几个节点挂掉不会影响正常节点的工作，剩余的节点依然可以提供注册和查询服务。而Eureka的客户端在向某个Eureka注册或时如果发现连接失败，则会自动切换至其它节点，只要有一台Eureka还在，就能保证注册服务可用(保证可用性)，只不过查到的信息可能不是最新的(不保证强一致性)。

除此之外，Eureka还有一种自我保护机制，如果在15分钟内超过85%的节点都没有正常的心跳，那么Eureka就认为客户端与注册中心出现了网络故障，此时会出现以下几种情况：

Eureka不再从注册列表中移除因为长时间没收到心跳而应该过期的服务
Eureka仍然能够接受新服务的注册和查询请求，但是不会被同步到其它节点上(即保证当前节点依然可用)
当网络稳定时，当前实例新的注册信息会被同步到其它节点中

因此， Eureka可以很好的应对因网络故障导致部分节点失去联系的情况，而不会像zookeeper那样使整个注册服务瘫痪。

# 三、秒杀系统相关的面试题

1. 什么是秒杀系统？请简要描述秒杀系统的特点和挑战。
2. 在秒杀系统中，如何解决高并发问题？
3. 请解释热点数据和热点问题在秒杀系统中的影响，并提供相应的优化方案。
4. 如何保证秒杀系统的数据一致性和可靠性？
5. 秒杀系统的流量穿透和击穿问题是什么？请提供相应的解决方案。
6. 如何防止秒杀系统中的重复购买和超卖问题？
7. 请描述秒杀系统中的限流和排队措施，以应对高并发请求。
8. 在秒杀系统中，如何做到合理的负载均衡和水平扩展？
9. 请列举一些常见的优化策略，用于提升秒杀系统的性能和响应速度。
10. 在秒杀系统中，如何处理异常和故障，以保证系统的稳定性？
11. 请简要介绍一种秒杀系统的架构设计，并阐述其中的关键技术点。
12. 在秒杀系统中，如何设计数据库表结构以及合理使用缓存来提高性能？
13. 请解释如何使用消息队列在秒杀系统中异步处理订单和库存等问题。
14. 有没有在实际项目中参与过秒杀系统的开发和优化经验？

## （1）什么是秒杀系统？请简要描述秒杀系统的特点和挑战。

秒杀系统是一种特殊的电商营销活动，通常在短时间内推出限量商品或服务，吸引大量用户参与，通过抢购的形式快速售罄商品。在秒杀系统中，用户需要在限定的时间内抢购商品，一旦商品售罄或时间结束，用户将无法继续购买。

秒杀系统的特点和挑战如下：

特点：

1. 高并发：秒杀活动吸引大量用户同时访问系统，导致系统并发量剧增。
2. 短时间内的高流量：秒杀活动通常在几分钟内完成，因此需要处理短时间内大量的请求。
3. 限时限量：秒杀活动商品数量有限，用户需在限定时间内完成抢购，增加商品的热度和稀缺性。
4. 瞬时高峰：活动开始瞬间，会有大量用户同时请求抢购，造成系统瞬时高峰压力。

挑战：

1. 高并发处理：秒杀活动时，系统面临大量并发请求，需要高效地处理请求，确保响应时间快速且稳定。
2. 事务处理：秒杀活动涉及到商品库存和订单的处理，需要确保事务的一致性和可靠性。
3. 数据一致性：秒杀系统需要保证商品库存准确，防止超卖和卖断。
4. 资源竞争：多用户同时抢购同一商品时，会涉及到资源竞争问题，需要解决并发竞争带来的问题。
5. 防止恶意请求：秒杀活动容易吸引恶意用户进行刷单等行为，需要进行有效的防刷和安全措施。
6. 系统稳定性：秒杀活动对系统的稳定性和可用性要求较高，需要预防系统宕机和崩溃。

综上所述，秒杀系统具有高并发、高压力和短时间内处理大量请求的特点和挑战，对系统的设计和性能有较高的要求。为了应对这些挑战，需要采取合理的架构和优化措施，确保系统稳定运行并提供良好的用户体验。

## （2）在秒杀系统中，如何解决高并发问题？

1. 优化数据库：数据库通常是秒杀系统的瓶颈之一。可以采用数据库读写分离，将读操作和写操作分别分配到不同的数据库实例上，减轻数据库的压力。同时，对数据库进行合理的索引优化，避免全表扫描等低效操作。
2. 缓存数据：使用缓存可以显著提高系统的读取性能。在秒杀系统中，可以将热门商品的信息和库存等数据缓存到内存中，减少对数据库的频繁访问。常用的缓存技术包括Redis和Memcached。
3. 使用分布式系统：通过将系统拆分为多个独立的服务，可以将请求分散到不同的服务器上，从而提高系统的并发处理能力。分布式系统还可以进行负载均衡，确保各个服务器的负载均衡。
4. 使用消息队列：秒杀系统中，用户的请求往往会涌入，使用消息队列可以将请求进行缓冲和异步处理，避免系统的瞬时压力过大。比如将用户下单请求放入消息队列，后台异步处理订单生成和库存扣减等操作。
5. 限流和排队：为了保护系统不被过多请求压垮，可以实施限流策略，限制每秒处理的请求数量。对于超过限定数量的请求，可以让其排队等待，避免瞬时高并发导致系统宕机。
6. 数据预热：秒杀活动开始前可以提前将商品信息和库存等数据加载到缓存中，减少活动期间的数据库访问，保证系统稳定性。
7. 无状态服务：尽量使用无状态服务，避免依赖服务器上下文信息，使得请求可以平均分布到不同的服务器上。
8. 异地部署：将秒杀系统部署到不同的地理位置，利用CDN加速和分发，降低距离带来的网络延迟。

## （3）请解释热点数据和热点问题在秒杀系统中的影响，并提供相应的优化方案。

在秒杀系统中，热点数据和热点问题是指某些商品或操作引起的高并发访问，导致系统性能瓶颈和潜在的问题。热点数据通常是指热门商品或活动，热点问题是由于高并发访问而导致的性能问题。这些热点现象会对秒杀系统产生以下影响：

1. 系统性能下降：热点数据和问题会导致服务器资源集中在少数商品或操作上，其他请求受到影响，造成系统性能下降，请求响应时间增加。
2. 严重的请求延迟：由于大量用户同时请求热门商品，服务器的处理能力可能无法及时响应所有请求，导致请求延迟和超时。
3. 系统崩溃：热点数据和问题可能导致服务器负载过高，超过服务器处理能力，从而导致系统崩溃和不可用。

优化方案：

1. 数据分片：对于热门商品，可以将数据进行分片处理，将不同商品的数据存储在不同的服务器上，避免热点集中在单一服务器上。
2. 缓存热点数据：将热门商品的数据缓存在缓存服务器上，减少数据库的访问压力，提高读取性能。
3. 数据预热：秒杀活动开始前，提前将商品信息和库存等数据加载到缓存中，避免活动期间的数据库访问，提高系统的稳定性。
4. 限流控制：对热点数据和请求进行限流，控制并发访问数量，防止服务器过载。
5. 异步处理：对于热点操作，可以采用异步处理的方式，将请求放入消息队列中，然后由后台进行处理，减轻前台服务器的负担。
6. 数据冗余：对于一些关键数据，可以进行冗余存储，确保高可用性和快速访问。
7. CDN加速：使用CDN加速，将静态资源缓存在CDN节点上，降低网络延迟和响应时间，减轻后端服务器的负载。

通过以上优化方案，可以有效地解决热点数据和热点问题在秒杀系统中带来的影响，提高系统的性能和稳定性，为用户提供良好的秒杀体验。

## （4）如何保证秒杀系统的数据一致性和可靠性？

保证秒杀系统的数据一致性和可靠性是非常重要的，以下是一些方法来实现这些目标：

1. 使用分布式事务：在秒杀系统中，可能涉及到多个操作，比如下单和减库存，这些操作必须要保证一致性。可以使用分布式事务来保证多个操作的原子性，确保这些操作要么全部成功，要么全部失败。
2. 限制每个用户的请求频率：在秒杀活动期间，可能会有大量的并发请求，为了保护系统不被过多的请求压垮，可以限制每个用户的请求频率，防止恶意用户的频繁请求导致系统过载。
3. 使用缓存：可以将热门商品的信息缓存在缓存中，减轻数据库的压力。同时，可以使用分布式缓存，如Redis，来实现缓存的高可用性和数据一致性。
4. 乐观锁和悲观锁：在秒杀系统中，可能会涉及到库存的减少操作，为了避免库存超卖的问题，可以使用乐观锁或悲观锁来保证库存的一致性。
5. 数据备份和恢复：对于重要的数据，建议进行定期的数据备份，以防止数据丢失或损坏。同时，可以设置数据恢复机制，确保在数据丢失或损坏时可以及时恢复。
6. 监控和报警：建立监控系统，实时监控秒杀系统的运行状态，包括数据库、缓存、服务器等。同时，设置报警机制，当系统出现异常时及时发出警报，以便及时处理问题。
7. 降级处理：在系统遇到异常或高峰期时，可以采取降级处理策略，暂时关闭某些功能或服务，确保核心功能的正常运行。
8. 自动化运维：建议使用自动化运维工具，自动监控和处理系统异常，减少人工干预，提高系统的稳定性和可靠性。

## （8）在秒杀系统中，如何做到合理的负载均衡和水平扩展？

在秒杀系统中，合理的负载均衡和水平扩展是保障系统高并发处理能力的关键措施。以下是一些在秒杀系统中实现负载均衡和水平扩展的方法：

1. 使用反向代理：在秒杀系统前面部署反向代理服务器，如Nginx或HAProxy等，用于接收用户请求并根据负载均衡策略将请求分发到多台后端服务器上，从而分担服务器的负载。
2. 采用分布式架构：将秒杀系统拆分为多个独立的服务模块，如用户认证、商品查询、下单、库存扣减等。通过分布式架构，将请求均匀地分布到不同的服务器上，减轻单一服务器的压力。
3. 使用服务注册与发现：采用服务注册与发现机制，如Consul或etcd，帮助实现服务的自动发现和注册。这样可以让新加入的服务器自动参与到负载均衡中，同时实现服务器的动态扩缩容。
4. 弹性伸缩：根据实际的访问压力，采用弹性伸缩机制，自动增加或减少服务器的数量。例如，使用云服务提供商的自动伸缩功能，根据负载自动增加或减少服务器的数量。
5. CDN加速：使用CDN(Content Delivery Network)加速，将静态资源缓存在CDN节点上，降低网络延迟和响应时间，减轻后端服务器的负载。
6. 分布式缓存：使用分布式缓存，如Redis或Memcached，减少对数据库的访问，提高系统的读取性能。通过多台缓存服务器，可以均衡地分担缓存请求。
7. 数据预热：秒杀活动开始前，提前将商品信息和库存等数据加载到缓存中，避免活动期间的数据库访问，保证系统的稳定性。
8. 无状态服务：尽量使用无状态服务，避免依赖服务器上下文信息，使得请求可以平均分布到不同的服务器上。
9. 分区和分片：对于热门商品，可以采用分区和分片策略，将数据分布到多个服务器上，实现水平扩展。

## （10）在秒杀系统中，如何处理异常和故障，以保证系统的稳定性？

在秒杀系统中，处理异常和故障是非常重要的，以确保系统的稳定性和可靠性。以下是处理异常和故障的一些建议：

1. 异常处理：对于可能出现的异常情况，需要进行合理的异常处理，避免系统因为异常而崩溃或出现错误。比如，对于数据库连接异常、网络异常等，可以进行错误重试或回滚操作，保证数据的一致性。
2. 限流控制：在秒杀活动期间，可能会出现大量的并发请求，为了保护系统不被过多的请求压垮，可以使用限流控制技术，限制每秒钟的请求量，防止系统过载。
3. 降级处理：在系统遇到异常或高峰期时，可以采取降级处理策略，暂时关闭某些功能或服务，确保核心功能的正常运行。
4. 数据备份：对于重要数据和订单信息，建议进行定期的数据备份，以防止数据丢失或损坏。
5. 超时设置：对于一些需要等待的操作，可以设置合理的超时时间，避免长时间的等待导致系统资源浪费。
6. 错误日志：及时记录系统产生的错误和异常，以便及时发现问题并进行处理。
7. 故障恢复：对于系统故障，需要及时发现问题并进行恢复，可以通过监控系统实时监控系统运行状态，发现问题及时处理。
8. 灰度发布：在系统升级或发布新功能时，可以采用灰度发布的方式，逐步将流量导向新版本，以避免一次性出现大规模的故障。
9. 事务处理：对于关键操作和数据更新，需要使用事务处理，确保数据的一致性和可靠性。
10. 自动化运维：建议使用自动化运维工具，自动监控和处理系统异常，减少人工干预，提高系统的稳定性。

# 四、日志相关的面试题 | [link](https://tech.meituan.com/2022/07/21/visualized-log-tracing.html)

## （1）基于日志的ELK方案

ELK是一个流行的日志管理方案，由Elasticsearch、Logstash和Kibana组成，用于实时地搜索、分析和可视化日志数据。下面我将简要介绍基于日志的ELK方案的每个组件：

1. Elasticsearch：Elasticsearch是一个开源的分布式搜索和分析引擎。它负责存储和索引大量的日志数据，使其可以高效地进行快速搜索和查询。Elasticsearch使用Lucene作为其底层搜索引擎，能够处理海量数据，并支持复杂的搜索操作。
2. Logstash：Logstash是一个用于收集、处理和传输日志数据的开源工具。它可以从各种来源收集日志，例如文件、网络、消息队列等，然后对数据进行过滤、转换和解析，最后将处理后的数据发送到Elasticsearch进行存储。Logstash允许您定义自定义的数据处理管道，以适应特定的日志格式和需求。
3. Kibana：Kibana是一个强大的数据可视化工具，它与Elasticsearch集成，用于创建实时的仪表板和图表，以展示存储在Elasticsearch中的日志数据。Kibana提供了各种图表类型，如柱状图、饼图、折线图等，使用户能够直观地理解和分析日志数据的趋势和模式。

基于日志的ELK方案的工作流程如下：

1. 数据采集：Logstash负责从不同来源（如应用程序、服务器、网络设备等）收集原始的日志数据。
2. 数据处理：Logstash对采集到的日志数据进行过滤、解析和转换，将其标准化为一致的格式，以便后续的搜索和分析。
3. 数据存储：处理后的数据被发送到Elasticsearch中进行索引和存储。Elasticsearch使用其强大的搜索引擎来优化数据的存储和检索。
4. 数据可视化：Kibana与Elasticsearch连接，使用Elasticsearch的查询功能来检索数据，并将其以各种可视化图表的形式展示出来。这些仪表板和图表可以实时更新，使用户能够实时监控日志数据的变化。

基于日志的ELK方案在实时日志分析、故障排查、性能监控等方面具有广泛的应用，它为组织和企业提供了一种强大的工具来管理和利用海量的日志数据。