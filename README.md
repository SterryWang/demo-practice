---

---

# demo-practice
a project aimed at training,testing,validating

##  Introduction
  基于SPRING BOOT 工程骨架，用于编写验证测试各种在学习、培训、工作中的用到的各种小功能，例如多线程，事务，设计模式，注解，代理，SPRING AOP切面编程等等，提供一些可随时使用、容易理解的DEMO，以PACKAGE为单位
每一个package展示一项小功能。



## 第一章 SPRING CACHE
代码库链接：
[springaop](src/main/java/com/example/demo/springcache/package-info.java)  

### 缓存支持方式
有两种
- 注解驱动的缓存

- XML声明的缓存  
  XML生命式缓存我们先不表具体请参照书本学习吧，先说注解式缓存，就是说在bean上添加缓存注解，使得这个类支持缓存（比如一个dao类），而开启注解式缓存有两种方式，

  第一种，在XML配置文件中加一行
  ```<cache:annotation-driven/>```

  第二种，在一个配置类中加上注解 ``` @EnableCaching``` ,举个例子:

  ```java
  import ...
  @Configuration
  @EnableCaching
  public class CachingConfig{
  ...
  }
  ```

  我们都知道，@Configuration等同于一个XML配置文件，而```@EnableCaching``` 又等同于在XML配置文件里加```<cache:annotation-driven/>```。所以效果是一样的

### SPRING Cache应用步骤

结合上一个小节，Spring Cache应用步骤分三步：

1. 开启注解式或者XML声明式缓存支持

   就同上个小节所述

2. [配置缓存管理器](#配置缓存管理器)

3. [为具体的实现方法添加缓存支持](#为具体的实现方法添加缓存支持)

现在我们第一个步骤上一小节已经讲完了，后面讲围绕后面两个步骤来说。

### 配置缓存管理器

缓存管理器多种多样，我们简单说两种，一种是很简单的本地缓存，另一种是Redis缓存

代码链接：[缓存配置DEMO](src/main/java/com/example/demo/springcache/CachingConfig.java)

基本的配置方法直接看代码即可。在这里，我们关注以下几点：

1. 多缓存管理器的情况，以及```@Primary``` 注解的使用

2. 组合缓存管理器CompositeCacheManager的配置方法和工作机制

3. redis 缓存管理器 （独立缓存服务器）

   redis缓存其实可以一分为二的看，一个是REDIS服务端的安装配置使用；另一个，是SPRING端接入REDIS的相关API。所以我们后面单独讲REDIS的时候再集中说吧。

4. <span id="cachename">缓存管理器名称集合</span>，可以把缓存管理器划分成不同的分区，就类似数据库里不同的表一样。

5. 缓存管理器可以通过代码或者配置文件等形式进行参数配置，比如超时时间，存储空间等等

### 为具体的实现方法添加缓存支持

本质上，缓存是通过切面来实现的。无非是before或者after那一套，一般来讲，dao层的很多实现方法或者实现类，添加上方法注解后就可以支持缓存机制

[**代码链接**](src/main/java/com/example/demo/springcache/EmployeeCachedDao.java)

#### 常用的四个注解声明：

| 注解        | 描述                                                         |
| :---------- | :----------------------------------------------------------- |
| @Cacheable  | 适用于查询，首先查缓存，缓存查不到查数据库，并将返回结果存入缓存中；<br />方法必须为非void返回值；<br />默认的缓存key是方法是入参； |
| @CachePut   | 适用于插入或更新，强制把返回值放到缓存中，并不会先到缓存中查找，而是会直接执行dao方法<br />==方法必须为非void返回值==；<br />默认的缓存key是方法的入参； |
| @CacheEvict | 适用于从缓存中删除，根据方法入参删除缓存中的键值对，然后再执行方法；<br />==方法可以为void返回==；<br />默认key是方法入参； |
| @Caching    | 分组注解，可应用多个其他的缓存注解                           |

这些注解都有非常丰富的属性，可以帮助我们来指定cachename（及上文说的缓存管理器名称），指定缓存管理器，指定存取的key，条件化缓存等等。具体可以自己点开缓存注解看一下。有时候我们不想使用默认key，就需要自定义key



#### 自定义缓存key

自定义缓存key支持SpEL表达式，具体看书，只介绍两个

| 表达式    | 描述                                                        |
| --------- | ----------------------------------------------------------- |
| #result   | 方法的返回值（不能用在@Cacheable注解）                      |
| #Argument | 任意的方法参数名（如#argName ）或者参数索引（如#a0或者#p0） |

具体使用还是参照代码链接中的```saveOne``` 方法。

#### 条件化缓存

```@Cacheable``` 和```@CachePut``` 都支持条件化缓存，通过配置他们各自的属性unless 和 condition即可。条件化配置也都使用SpEL表达式

```
unless:满足后面加的条件则不写入缓存，但仍然会在执行方法前去缓存查找；
condition：缓存机制的总开关（包括存取），如果为false，则全面禁止缓存的存取。
```

​     具体使用方法，还是参见代码链接和对应的testcase                                                                                                            

#### 移除缓存条目

同数据库类似，缓存同样也是具备CRUD流程。考虑一个场景，如果你先从数据库删除一个数据，这个数据可能也同时存在于缓存之中，那么应该把缓存中的数据一并删掉。

这个需要配合```@CacheEvict```注解使用。该注解可以加在void或者非void方法上，默认key为方法入参。

具体参见代码链接.



## 第二章  SPRING DATA REDIS

### 1. 为什么用REDIS?

[为何用redis](https://www.cnblogs.com/jebysun/p/9417699.html)

redis单独使用的作为数据存储的场景很少，更多是配合数据库，==**作为redis缓存使用**==。因为本章内容也是讲述实现redis缓存功能的方法。

### 2.准备工作

首先你要安装并配置好redis服务端，我是安装了redis的docker版本，顺便也是为了学习一下docker ，安装过程已经记录在了有道笔记中。

### 3. 配置spring data redis  

#### 3.1 引入依赖

```xml
<!--引入spring-data redis组件-->
   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-redis</artifactId>
   </dependency>
   <!--jedis客户端组件支持-->
   <dependency>
   <groupId>redis.clients</groupId>
   <artifactId>jedis</artifactId>
</dependency>
```

SPRING DATA REDIS 准备了一下集中客户端实现提供了连接工厂

- JedisConnectionFactory
- JredisConnectionFactory
- LettuceConnectionFactory
- SrpConnectionFactory

因为我们选择使用jedis连接工厂，如果要支持**==连接池==**的方式，就有必要引入redis.client..jedis这个依赖。

#### 3.2 基础设施及配置

要实现redis缓存 ，我们需要以下基础设施，[代码链接](src/main/java/com/example/demo/redis/RedisConfig.java)：

- 连接工厂 connectionFactory（需要注入连接池配置）

- 模板redisTemplate(需要注入连接工厂；redis缓存模式下redisTemplate非必要,它是用于直接操作redis存储的)

- redis缓存管理器

  其实对于连接工厂和redis模板，SPRING  DATA REDIS 是有自动装配的，但我们还是希望自己来配置这些组件，为了避免冲突，我们在启动类中关闭了这些组件的自动装配：

  ```java
  @EnableAspectJAutoProxy//此注解可加可不加，因为是SPRING 默认是将它开启的
  @SpringBootApplication(scanBasePackages="com.example.demo",exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class})
  public class DemoApplication {
  
     public static void main(String[] args) {
        System.out.println("启动类启动啦！");
        SpringApplication.run(DemoApplication.class, args);
        
     }
  }
  ```

  excluede 这两个类即可去除redis组件的自动装配：

  RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class

  我们把以上组件都封装到了RedisConfig.java这个类里面了，下面我们会结合代码逐个说一下：

1. **连接工厂**

   ```java
   @Bean("jedisCF")
   public RedisConnectionFactory   jedisCF(@Qualifier("jedisPoolCfg") JedisPoolConfig  jedisPoolConfig){
       //单机版jedis配置类
       RedisStandaloneConfiguration redisStandaloneConfiguration =
               new RedisStandaloneConfiguration();
       //设置redis服务器的host或者ip地址
       redisStandaloneConfiguration.setHostName("wslip");
       //设置默认使用的数据库
       redisStandaloneConfiguration.setDatabase(0);
       //设置密码
       //redisStandaloneConfiguration.setPassword(RedisPassword.of("123456"));
       //设置redis的服务的端口号
       redisStandaloneConfiguration.setPort(6379);
   
   
       //获得默认的连接池构造器(怎么设计的，为什么不抽象出单独类，供用户使用呢)
       JedisClientConfiguration jedisClientConfiguration =
              JedisClientConfiguration.builder().usePooling()
               .poolConfig(jedisPoolConfig).build();
   
   
   
       //单机配置 + 客户端配置 = jedis连接工厂
       return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
   
   
       }
   
   }
   ```

   

   可以看到连接工厂需要依赖注入jedis连接池配置对象，所以这里先附上连接池配置对象jedisPoolConfig的生成过程(只配置了关键连接池属性)：

   

```java
 /**
     * jedis连接池配置
     * @return
     */
    @Bean("jedisPoolCfg")
public JedisPoolConfig jedisPoolConfig(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //最大连接数
        jedisPoolConfig.setMaxTotal(100);
        //最小空闲连接数
        jedisPoolConfig.setMinIdle(20);
        //当池内没有可用的连接时，最大等待时间
        jedisPoolConfig.setMaxWaitMillis(10000);
        jedisPoolConfig.setMaxIdle(30);
        //------其他属性根据需要自行添加-------------
        return jedisPoolConfig;

}
```

​	再回到前面的连接工厂，jedis连接工厂需要依赖两个对象：

​	redisStandaloneConfiguration 和  jedisClientConfiguration。具体过程参见代码即可。

关于连接工厂，这里多说一个***小插曲***，连接工厂的地址我一开始用的主机地址是localhost，结果测试时怎么都访问不通，但是我在CMD里面就可以telnet通这个地址。然后我ping了一下localhost，发现地址是IPV6形式的，于是我合理怀疑java进程连接的IP地址不是这个IPV6（::1）地址，于是我根据连接错误堆栈一路打断点watch变量，最后发现进程里试图连接的地址其实是127.0.0.1而不是那个IPV6地址。因为我redis是安装在WSL中的DOCKER里面的，主机和WSL的固定连接地址一直是我想解决的问题，最终这促使我解决了WINDOWS主机和WSL 在host文件中互相动态记录WSL虚拟机IP的问题，此教程已经写在了云笔记中。

2. **模板redisTemplate**

   这个我们其实可以类比一下jdbcTemplate,redisTemplate类似地也为开发人员封装了一系列操作redis服务器的操作（比如增删改查等）。场景非常丰富。我们[**测试类**](src/test/java/com/example/demo/redis/tests/SimpleRedisTests.java)中已经展示了它的用法。但是就像我们前面说的，脱离数据库直接操作redis服务器的场景很少，redis缓存场景是主流。但是它的很多配置方式和后面要说的redis缓存管理器有异曲同工之处。

3. **redis缓存管理器**

   在spring boot 1.x版本，RedisCacheManager是通过注入依赖redisTemplate生成的。自2.x版本后，这一机制发生了改变，剥离了同redisTemplate之间的依赖关系，而是改为注入依赖`RedisConnectionFactory`生成的：

   ```java
   @Bean("redisCacheManager")
   @Primary
   public CacheManager redisCacheManager(@Qualifier("jedisCF") RedisConnectionFactory redisCF){
       /**
        * 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        */
       Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);
       //RedisSerializer<Object>  jacksonSeial= new GenericJackson2JsonRedisSerializer();
       ObjectMapper om = new ObjectMapper();
       // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
       om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
       // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
       om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
       jacksonSeial.setObjectMapper(om);
   
       /**
        *  装在redisCacheManager配置类，设置超时时间，序列化器
        */
   
       //正确写法
       RedisCacheConfiguration  config = RedisCacheConfiguration
               .defaultCacheConfig()
               .entryTtl(Duration.ofMinutes(3))
               .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer.UTF_8))
               .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jacksonSeial))
               .disableCachingNullValues();
   
       /**
        * 错误写法示范：config.entryTtl等后面一系列的方法，并不是类似set方法，它的返回其实是一个new出来的新对象
        * 这样子的话， “return RedisCacheManager.builder(redisCF).cacheDefaults(config).build();”这一句，
        * 依然使用config作为设置类，那么后面那一串设置方法则已经无效了
        */
   
   
      /* RedisCacheConfiguration  config = RedisCacheConfiguration
                      .defaultCacheConfig();
        config.entryTtl(Duration.ofMinutes(3));
              .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
               .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jacksonSeial))
               .disableCachingNullValues();*/
       RedisCacheManager  redisCacheManager  = RedisCacheManager.builder(redisCF).cacheDefaults(config).build();
       redisCacheManager.setTransactionAware(true);
       return redisCacheManager;
   
   
   }
   ```

   从最后的代码可以看出来，RedisCacheManager依赖连接工厂`redisCF`和缓存配置对象`RedisCacheConfiguration`来创建，其中缓存配置对象定义了KEY和VALUE的序列化执行器。这里我们选择json序列化执行器，同时还需要映射器`mapper`的支持。序列化执行器有多种，我们在`RedisSerializer.class`中可以透过几个静态工厂方法很清楚地看到这几种序列化器。默认使用的是`JdkSerializationRedisSerializer`，它会把<key,value>转换成字节码形式，可读性比较差，同时也给通过其他客户端工具存取REDIS数据带来障碍：

   ![image-20200710151749401](https://raw.githubusercontent.com/SterryWang/picsbed/master/img/20200710151751.png?token=ADFHXAVIDT4MWYVJFEY3ZJ27BALFY)

   而我们按照代码中的方式，给KEY配置`StringRedisSerializer`，给VALUE配置`Jackson2JsonRedisSerializer`即JSON序列化工具，那么结果就要友好很多了：

   ![image-20200710152427450](https://raw.githubusercontent.com/SterryWang/picsbed/master/img/20200710152429.png?token=ADFHXAS5MA2QLPYRJW6BUBC7BAL6S)

   此外，还可以配置数据存活时间，以及开关redis事务。

   总结下，结合redis缓存管理器配置，比较关键的配置有以下几点：

   - 键值序列化执行器（包括mapper）
   - 存活时间：超过该时间redis缓存数据失效。据观测超时后redis服务器会清除该数据（至少让你查不到了）
   - redis事务

   关于redis事务的机制，我们不表了。但是它的应用和实现我们会在下一节讲述。

#### 3.3 spring data redis缓存应用

在SPRING CACHE一章中，我们说了会把redis缓存独立出来单独说下，于是就有了本章内容。但是redis缓存管理器本质上还是继承自`CacheManager`接口的，同其他类型的缓存一样在设计上归属同一层。为了**==方便查看，我们还是把redisCacheManager放到了`RedisConfig.java`,而没有放到`CacheConfig`中==**，并在类上开启了缓存支持`@EnableCaching`.但是测试类，**我还是放回到了[Cache的测试类](src/test/java/com/example/demo/springcache/tests/SpringCacheTests.java)中：**

```java
@Test
//@Transactional
public void testSaveList() {
   List<Employee> list = new ArrayList<>();
   for (int i = 0; i < 3; i++) {

      Employee e = new Employee();
      e.setId(i);
      e.setName("葫芦娃" + i);
      list.add(e);


   }

   employeeCachedDao.saveList(list,100);
   System.out.println("查回来的信息为："+employeeCachedDao.findList(100));
   try {
      //因为DEBUG时，H2-CONSOLE无法访问，所以只能用RUN跑junit,不可以断点DEBUG,通过延时两分钟，来查看h2-console
      Thread.currentThread().sleep(120000);
   } catch (InterruptedException e) {
      e.printStackTrace();
   }
}
```

除了测试缓存，我还一并测试了事务，其实SPRING BOOT的事务管理本身是支持REDIS事务的（这里要感慨一下SPRING 事务的强大之处）。这就是说数据库的事务开启，提交，回滚时，redis事务也会做同样操作。我这里为了作对比，同时也加入了H2数据库，其实不管有没有数据库，都不会影响SPRING 事务对于REDIS事务的支持。我们看下`employeeCachedDao`里面是怎么写的（[代码链接](src/main/java/com/example/demo/springcache/EmployeeCachedDao.java)）：

```java
@CachePut(value = "employeeCache", key = "#key")
public List<Employee> saveList(List<Employee> list,int key){
   System.out.println("key="+key+",value="+list);
   //插入数据库
   for(Employee e:list)
   jdbcTemplate.update("insert into employee(id,name,age) values (?,?,?)",e.getId(),e.getName(),e.getAge());

   return  list;

}
```

同上一章所讲的，其他缓存的实现方法没有区别，SPRING 的分层做的非常好。这里呢，我们还特别演示了一种把list存入到REDIS缓存中的方法，序列化之后，也不过是在JSON中放数组。值的注意的是，在使用`@CachePut`注解时，我通过`#key`来指定了入参中的参数为redis缓存的key，value则默认取方法的返回结果，[具体参见上一章](#常用的四个注解声明)。同样地，我们再测试方法里还调用了查找方法验证是否从缓存中查找，调用的dao层代码示例同上一章无任何修改，还是接口封装的优势：

```java
@Cacheable(value = "employeeCache")
public List<Employee> findList(int key) {
   //测试缓存返回
   System.out.println("查不到任何东西");
   return null;

}
```



好了，总结下redis缓存应用

1. 在需要缓存的方法上添加上一章的几种缓存注解
2. 缓存事务的应用方法同其他的SPRING 的数据库事务没有区别，注解或者手动事务都可以。

### 4.本章小结

常用的关于spring data redis的功能，基本都有所讲述。涉及到REDIS的原理和机制，我们就不把它和JAVA放在一起说了。REDIS应用场景广泛，就像MYSQL一样，它的机制和底层原理当然是大厂面试的必备题目，我们会在后续学习中单独讲述这部分。

### 附：SPIRNG BOOT 引入H2数据库

H2数据库在我们测试中经常用到，对于测试场景来说很方便和很轻量。讲redis缓存时也恰好用到了。所以简单说下简单配置方法：

1. **引入依赖**

   ```java
   <!-- 引入H2数据库用于本地测试 -->
   <dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <scope>runtime</scope>
   </dependency>
   <!-- JPA 中已经包含了 com.zaxxer.hikari 数据源 与 spring-jdbc-->
   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
   </dependency>
   ```

2. **自动配置**

   最快速的使用方式当然是自动配置，只要在`application.properties`里面配置以下信息即可：

   ```properties
   server.port=8088
   spring.h2.console.enabled=true
   spring.h2.console.path=/h2-console
   
   
   # 是否开启查询语句在控制台打印
   spring.jpa.show-sql = true
   #内存式数据库
   #spring.datasource.url =jdbc:h2:mem:testdb
   #文件式数据库
   spring.datasource.url=jdbc:h2:~/testdb;AUTO_SERVER=TRUE
   spring.datasource.username=sa
   spring.datasource.password=123456
   #进行该配置后，每次启动程序，程序都会运行resources/schema.sql文件，对数据库的结构进行操作，相当于新建一个表。
   spring.datasource.schema=classpath:schema.sql
   #进行该配置后，每次启动程序，程序都会运行resources/data.sql文件，对数据库的数据操作，相当于往表中插入数据。
   #spring.datasource.data=classpath:data.sql
   spring.datasource.driver-class-name=org.h2.Driver
   ```

   H2数据库支持内存嵌入型，或者文件型。`AUTO_SERVER=TRUE`只能配置到文件型数据库的URL上，标识该数据库不仅当前应用可访问，其他的DBMS客户端工具也可以远程连接。

   `spring.datasource.schema`和`spring.datasource.data`两个参数支持建表和数据初始化。

   我测试时是使用了自动配置的`jdbcTemplate`,具体可以参考[代码链接](src/main/java/com/example/demo/springcache/EmployeeCachedDao.java)。

   我在前文也说了，**==DEBUG模式下，H2-CONSOLE不可访问==**，但是我又要做单元测试时观测H2数据库数据，所以就在junit测试中用了线程睡眠的方式,RUN TEST而不是DEBUG TEST,利用线程睡眠间隙访问H2-CONSOLE验证测试结果。

## 第三章  SPRING 消息

> 本章内容以《SPRING 实战》一书中的   第17章 **SPRING 消息**为蓝本

### 3.1 异步消息简介

#### 3.1.1 为何用异步消息

​		弥补同步通信的无法满足的应用场景，客户端无需等待全程过程完成后再继续执行。可以类比一个场景：发完邮件后赶飞机。

#### 3.1.2 两种通用的目的地

**队列**和**主题**，即点对点和发布/订阅模型。

![image-20200720182043236](https://raw.githubusercontent.com/SterryWang/picsbed/master/img/20200720182145.png)

所谓的点对点，指的都是一则消息的生产者肯定只有一个，而且这则消息最终也会被某一一个消费者消费。但队列是允许有多个消费者的。而主题模式下，同一则消息，所有的订阅者都会接受到这则消息的副本。

![image-20200720182330002](https://raw.githubusercontent.com/SterryWang/picsbed/master/img/20200720182331.png)

#### 3.1.3 异步消息的可选方案

- Java 消息服务（JMS）

  JAVA定义的消息服务API

- 高级消息队列协议（AMQP）

  关于两者的不同，我们会在章节末做个比较

#### 3.1.4 异步消息的优点

- 规避同步通信时客户端等待，提高客户端应用性能
- 面向消息，解耦客户端和服务端（针对RPC通信做比较）
- 位置独立（不必记录远程服务地址等）
- 确保投递（服务端崩溃了，重新拉起来时依然可以从消息服务器中接收消息）

### 3.2  SPRING & JMS 实现

​		**JMS**是JAVA定义的消息服务API，PACKAGE前缀为`javax.jms.*`, 定义了JAVA消息的通用规范，只有接口和异常，只有极少数几个实现类。上一节我们说了异步消息常用的两种方案有JMS 和AMQP，我们将开两节分开讲述这两部分内容。JMS我们使用**ActiveMQ** 作为消息代理服务器。

#### 3.2.1 准备工作

- **安装消息服务器ActiveMQ**

  消息代理服务器我们使用**==ActiveMQ==**，它是使用**JMS**进行异步消息传递的。具体的安装方法我已经写到笔记了。

- **引入POM 依赖**

```xml
<!-- 引入AMQ依赖 -->
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-activemq</artifactId>
</dependency>
```

理论上序列化，消息格式等操作还需要`jackson`，但是`spring-boot-starter-web`已经包含该组件，这里就列举核心组件了。

#### 3.2.2 JMS 基础配置

​		SPRING 中搭建消息代理，其实我们需要分成***发送者（生产者）和 接收者（消费者）***两部分来说，他们所需要的基础组件还是有些差别的。但是他们**有些组件作为基础设施是共有的**：

- **连接工厂**

  顾名思义，连接到消息代理服务器的工厂类，对于发送者和接受者都是必备的。作为直接面向消息代理服务器的组件，它的实现类往往捆绑了相应代理服务器的特性（就想JAVA针对不通过数据库的驱动），但是它最终又是实现了`javax.jms.ConnectionFactory`接口的

- **消息目的地**

  [3.1.2](#3.1.2 两种通用的目的地)小节中我们介绍了**队列和主题**两种消息目的地，消息收发双方都需要连接到该目的地。

- **JMS模板**

  SPRING定义了这个模板类封装了消息**发送和接收**各类操作，大大简化了原本失控的重复的代码（就像jdbcTemlate之于基础的JDBC操作一样）。**模板是需要注入连接工厂的**。后面我们了解到，模板对于**接收端**来说，**不是必须的**，因为SPRING 2.0 ==**对于消息接收，除了使用模板外，还有另一种方案——消息驱动POJO**==，后面我们会讲到。

  

  简单来描述这三者关系，连接工厂是三者中最底层的，直接面向消息代理服务器（或者说它产生的连接对象直接面向代理服务器）。而**JMS模板是依赖于连接工厂和消息目的地**的，JMS封装了基本的消息发送和接收操作，我们看下一段样板的消息发送和接收代码有多么麻烦：

  **发送代码**

  ![image-20200722103718782](https://raw.githubusercontent.com/SterryWang/picsbed/master/img/20200722103720.png)

  **接收代码**

  ![image-20200722102148152](https://raw.githubusercontent.com/SterryWang/picsbed/master/img/20200722102150.png)

  看看出现了多少对象

  1. 连接工厂`ConnectionFactory`
  2. 连接对象`Connection`,连接工厂产生。
  3. 会话`Session`,由连接产生，在ActiveMQ模式下，连接和会话是**关联**关系
  4. 目的地`Destination`，分为queue和topic两种
  5. 消息生产者`MessageConsumer`和消费者`MessageProducer`，由``session`通过`Destination`产生
  6. 消息主体`Message`

  注意，接收的时候是通过`conn.start()`方法；还有最后IO的关闭操作必须的。我们看到这些代码冗长而重复，JmsTemplate就是为了==**消除这些冗长繁复的代码而生的，它可以创建连接，获得会话，以及发送和接收消息，使得我们可以专注于构建要发送的消息或者处理收到的消息**==。其实我们看下`javax.jms.*`包的类图，可以看下接口类型和关系：

  ![Package jms](https://raw.githubusercontent.com/SterryWang/picsbed/master/img/20200722111439.png)

  好了，基础设施我们就介绍到这里，下面我们看看如何使用这些基础组件来简化JMS消息的收发。

#### 3.2.3 使用JmsTemplate发送消息

##### 3.2.3.1 具体配置

JmsTemplate的主要价值就提现在消息发送上，在接收消息方面，JmsTemplate的并不是最优选，关于JmsTemplate接收消息的实现机制有兴趣可以去自己研究。其实利用上一节的JMS基础设施，我们就足够完成JMS消息发送。直接看**配置代码**：[代码连接](src/main/java/com/example/demo/message/ActiveMQConfig.java)

```java
package com.example.demo.message;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Destination;
import javax.jms.Queue;
import javax.jms.Topic;

/**
 * @author wangxg3
 * ActiveMQ消息代理配置类
 */
@Configuration
@EnableConfigurationProperties(ActiveMQProperties.class)
public class ActiveMQConfig {

    /**
     * 自定义队列名属性，表队列名
     */
    @Value("${spring.activemq.queue-name}")
    private String queueName;
    /**
     * 自定义属性，表主题名
     */
    @Value("${spring.activemq.topic-name}")
    private String topicName;

    /**
     * ActiveMQ连接工厂
     *
     * @param properties
     * @return
     */
    @Bean("MyActiveMQCF")
    public ActiveMQConnectionFactory jmsConnectionFactory(ActiveMQProperties properties) {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();


        //packages的设置是sonar提醒的，是考虑安全性的问题
        ActiveMQProperties.Packages packages = properties.getPackages();
        if (packages.getTrustAll() != null) {
            connectionFactory.setTrustAllPackages(packages.getTrustAll());
        }
        /*if (!packages.getTrusted().isEmpty()) {
            connectionFactory.setTrustedPackages(packages.getTrusted());
        }*/
        connectionFactory.setTrustAllPackages(true);
        //设置消息代理服务器的地址，其实还可以用户密码，超时时间等，不赘述
        if (properties.getBrokerUrl() != null) {
            connectionFactory.setBrokerURL(properties.getBrokerUrl());
        }
        if (properties.getUser() != null && properties.getPassword() != null) {
            connectionFactory.setUserName(properties.getUser());
            connectionFactory.setPassword(properties.getPassword());

        }


        return connectionFactory;
    }

    /**
     * 队列
     *
     * @return
     */
    @Bean(name = {"activeMQQueue", "defJmsDst"})
    public Queue activeMQQueue() {
        return new ActiveMQQueue(queueName);
    }

    /**
     * 主题
     *
     * @return
     */
    @Bean(name = {"activeMQTopic"})
    public Topic activeMQTopic() {
        return new ActiveMQTopic(topicName);
    }

    /**
     * jms模板
     *
     * @param connectionFactory
     * @param dst
     * @return
     */
    @Bean(name = "MyJmsTemplate")
    public JmsTemplate jmsTemplate(@Qualifier("MyActiveMQCF") ActiveMQConnectionFactory connectionFactory, @Qualifier("defJmsDst") Destination dst) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        //设置默认的目的地，即使设置了默认目的地，使用JmsTemplate发送消息时依然可以指定目的地
        jmsTemplate.setDefaultDestination(dst);

       //是否使用订阅模式，当目的地为字符串形式时，jmstemplate将默认发送或者接收到主题类型的目的地
        // jmsTemplate.setPubSubDomain(true);


        return jmsTemplate;

    }


}

```

如上所述，配置类里共配置了**连接工厂**，**目的地**，**jms模板**三种基础设施，同时还注入了`ActiveMQProperties.class`，注意点在代码里都有所体现，重点说3点：

1. connectionFactory.setTrustAllPackages()方法设置可信任packages，这点很重要，在接收消息并转换成对象时，涉及到安全问题。
2. jmsTempate 可以设置默认目的地，这样使用一些方法时（比如`jmsTempalte.send()`）会使用默认目的地
3. `jmsTemplate.setPubSubDomain()`方法可以设置是否开启订阅模式，如果设定为`true`,则所有发送或者接收方法在不明确目的地类型的情况下（比如仅使用jndi 目的地名称），则目的地类型自动默认为Topic类型。

##### 3.2.3.2 发送案例

消息发送操作其实可以像书上一样封装成一个针对某种消息类型的`service`类，这样可以再业务代码层面隔绝jms模板，消息目的地这些基础设施，从而实现二者的解耦，做好代码分层：

![image-20200806163158323](https://raw.githubusercontent.com/SterryWang/picsbed/master/img/20200806163201.png)

但是为了方便起见，直接在testcase中编写了，代码链接：[ActiveMQTests](src/test/java/com/example/demo/message/tests/ActiveMQTests.java)

```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ActiveMQTests {


    @Resource(name = "simpleExecutor")
    ExecutorService executorService;
    @Resource(name = "MyJmsTemplate")
    private JmsOperations jmsTemplate;

    @Resource
    private Queue jmsQueue;

    @Resource
    private Topic jmsTopic;


    private static Logger log = LoggerFactory.getLogger(ActiveMQTests.class);

    /**
     * 测试发送到队列
     * 使用MessageCreator
     */
    @Test
    public void testSendToQueue() {
//将发送到jmsTemplate定义的默认队列
        //演示第一种发送方式，使用messageCreator先创建message再发送
        jmsTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage("你好啊！我是生产者");
            }
        });


    }

    /**
     * 测试从队列获取
     * 使用jmsTemplate先接收再转换
     *
     * @throws JMSException
     */
    @Test
    public void testRecvFromQueue() throws JMSException {
        //第一种接收信息方式，返回类型为Message ,使用类型强制转换为特定类型的Message实现类，本质上
        //读取的还是消息
       /* TextMessage msg = (TextMessage)jmsTemplate.receive("jms.queue");

        System.out.println(msg.getText());*/
        //第二种接收信息转换方式，使用jmsTemplate内部定义的消息转换器，把接收到的message转换为具体的对象Object
        // String msg = (String) jmsTemplate.receiveAndConvert("jms.queue");
        String msg = (String) jmsTemplate.receiveAndConvert(jmsQueue);

        System.out.println(msg);
    }

    /**
     * 测试发送到主题Topic
     * 使用jmsTemplatex先转换对象为msg,再发送
     */
    @Test
    public void testSendToTopic() {
        Employee e = new Employee();
        e.setId(1);
        e.setName("小白");
        e.setAge(18);
        //这种写法会默认目的地类型是队列而不是主题
        log.info("开始发送topic信息了。。。");
        jmsTemplate.send(jmsTopic, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                // return session.createObjectMessage(e);
                return session.createTextMessage("你好，这是发往TOPIC的文本信息！");
            }
        });

        //发送主题型目的地的正确写法
        //第二种发送方式，常用，使用jmsTemplate内部消息转换器将对象转换成message再发送
        //jmsTemplate.convertAndSend(jmsTopic, e);


    }

    /**
     * 测试从主题接收信息
     */
    @Test
    public void recvFromTopic() {
        Employee e = (Employee) jmsTemplate.receiveAndConvert(jmsTopic);
        System.out.println(e);

    }


    @Test
    public void recvFromTopic2() throws IOException, JMSException {
        log.info("开始监听了。。。");
        Message msg = jmsTemplate.receive(jmsTopic);

        assert msg != null;
        System.out.println("接收到的MESSAGE类型为：" + msg.getClass());

        log.info("接收到的MESSAGE类型为：{}", msg.getClass());
        if (msg instanceof TextMessage) {

            log.info("接收到的信息为：{}", ((TextMessage) msg).getText());
        } else if (msg instanceof ObjectMessage) {
            log.info("接收到的对象为：{}", ((ObjectMessage) msg).getObject());
        } else {
            log.info("接收的信息为：{}", msg);
        }
        /*ufferedWriter  bufferedWriter = new BufferedWriter(new FileWriter("d:/topicmsg.log"));

        bufferedWriter.write("接收到的MESSAGE类型为：" + msg.getClass());
        bufferedWriter.close();*/
    }


}
```

`testSendToQueue()`和`testSendToTopic()`展示了使用了jmsTemplate发送消息到队列和主题的方法，但二者的消息载体的JAVA类型分别是字符串和对象，但实现方式都是通过`MessageCreator`实现的，它的作用是把JAVA类型的消息载体转换为``Message`类型。代码里也说明了第二种消息转换方法，就是直接使用`jmsTemplate.convertAndSend()`方法，这是因为`jmsTemplate`是拥有默认消息转换器的，此方法正是使用了消息转换器。此外Spring还提供了各种消息转换器，他们都实现了`MessageConverter`接口，开发人员可以自行选择注入到模板中。

![image-20200811183345620](https://raw.githubusercontent.com/SterryWang/picsbed/master/img/20200811183348.png)

​		然后我们就可以在ActiveMQ服务器上看到了：

发送到队列：

![image-20200806165325810](https://raw.githubusercontent.com/SterryWang/picsbed/master/img/20200806165328.png)

发送到主题(订阅)：

![image-20200806165431963](https://raw.githubusercontent.com/SterryWang/picsbed/master/img/20200806165434.png)

#### 3.2.4 接收JMS消息

​		上一小节的testcase里我们已经展示了使用jmsTemplate接收消息的方式。但使用模板接收消息是存在弊端的，首先因为它的receive方法是同步的，接收不到消息该方法是阻塞的（除非超时），其次只有此方法被调用时才可以接收消息，这种主动接收的方法时效性也成问题。有没有那么一种机制，接收端**==可以异步接收消息==**，就像我们的手机收到微信一样？这就是我们要说的**==消息监听==**。

##### 3.2.4.1 Spring消息驱动pojo

​		为了让我们理解消息驱动模型，书上用了一些篇幅从EJB的MDB(message-driven  bean)开始讲，一路演进到SPRING 的消息驱动POJO。SPRING 给普普通通的POJO赋予了消息接收能力 ，是把pojo作为消息监听器，并且需要把pojo放到消息监听器容器里。

![image-20200807153708854](https://raw.githubusercontent.com/SterryWang/picsbed/master/img/20200807153711.png)

​		SPRING的JMS消息监听原理相同。





