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

- 连接工厂 **==connectionFactory==**（需要注入连接池配置）

- 模板**==redisTemplate==**(需要注入连接工厂；redis缓存模式下redisTemplate非必要,它是用于直接操作redis存储的)

- ==**redis缓存管理器**==

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

#### 3.3 spring data redis缓存的应用

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

​		SPRING的JMS消息监听原理同上，实现消息监听的核心组件有三：

- 连接工厂

- 消息监听器

- 消息监听器容器，依赖连接工厂和消息监听器

  ​	创建消息监听器的方式有两种

1. 实现`javax.jms.MessageListener`接口

2. 使用注解将普通pojo类注册为消息监听器

   

   在此之前，我们==**先讲述一下SPRING JMS的几种消息监听器**==，SPRING提供的监听器接口或者实现类都在两个package:

   `org.springframework.jms.listener`

   `org.springframework.jms.listener.adapter`

   

   把他们的类图画一下（剔除了一些无关紧要的类；为了便于理解，类图中还引入了javax.jms.MessageListener接口，其余都是SPRING的）

   ![image-20200817111943393](https://raw.githubusercontent.com/SterryWang/picsbed/master/img/20200817111946.png)

   1. **MessageListener**接口

      MessageListener是最原始的消息监听器，它是JMS规范中定义的一个接口。其中定义了一个用于处理接收到的消息的onMessage方法，该方法只接收一个Message参数。

   2. **SessionAwareMessageListener接口**

      SessionAwareMessageListener是Spring为我们提供的，它不是标准的JMS MessageListener。MessageListener的设计只是纯粹用来接收消息的，假如我们在使用MessageListener处理接收到的消息时我们需要发送一个消息通知对方我们已经收到这个消息了，那么这个时候我们就需要在代码里面去重新获取一个Connection或Session。SessionAwareMessageListener的设计就是为了方便我们在接收到消息后发送一个回复的消息，它同样为我们提供了一个处理接收到的消息的onMessage方法，但是这个方法可以同时接收两个参数，一个是表示当前接收到的消息Message，另一个就是可以用来发送消息的Session对象。先来看一段代码：

      ```java
      package com.tiantian.springintejms.listener;
      
      import javax.jms.Destination;
      import javax.jms.JMSException;
      import javax.jms.Message;
      import javax.jms.MessageProducer;
      import javax.jms.Session;
      import javax.jms.TextMessage;
      
      import org.springframework.jms.listener.SessionAwareMessageListener;
      
      public class ConsumerSessionAwareMessageListener implements
              SessionAwareMessageListener<TextMessage> {
      
          private Destination destination;
      
          public void onMessage(TextMessage message, Session session) throws JMSException {
              System.out.println("收到一条消息");
              System.out.println("消息内容是：" + message.getText());
              MessageProducer producer = session.createProducer(destination);
              Message textMessage = session.createTextMessage("ConsumerSessionAwareMessageListener。。。");
              producer.send(textMessage);
          }
      
          public Destination getDestination() {
              returndestination;
          }
      
          public void setDestination(Destination destination) {
              this.destination = destination;
          }
      
      }
      
      ```

      

   3. **MessageListenerAdapter**

      如上方类图所示，它是Spring提供的一个实现了MessageListener接口和SessionAwareMessageListener接口的实现类，其作用为：

      它的主要作用是将接收到的消息进行类型转换，然后通过反射的形式把它交给一个普通的Java类进行处理。并且还可以回复broker确认信息。我们可以简单分析源码：

      ```java
      @Override
      @SuppressWarnings("unchecked")
      public void onMessage(Message message, @Nullable Session session) throws JMSException {
         // Check whether the delegate is a MessageListener impl itself.
         // In that case, the adapter will simply act as a pass-through.
         Object delegate = getDelegate();
         if (delegate != this) {
            if (delegate instanceof SessionAwareMessageListener) {
               Assert.state(session != null, "Session is required for SessionAwareMessageListener");
               ((SessionAwareMessageListener<Message>) delegate).onMessage(message, session);
               return;
            }
            if (delegate instanceof MessageListener) {
               ((MessageListener) delegate).onMessage(message);
               return;
            }
         }
      
         // Regular case: find a handler method reflectively.
         Object convertedMessage = extractMessage(message);
         String methodName = getListenerMethodName(message, convertedMessage);
      
         // Invoke the handler method with appropriate arguments.
         Object[] listenerArguments = buildListenerArguments(convertedMessage);
         Object result = invokeListenerMethod(methodName, listenerArguments);
         if (result != null) {
            handleResult(result, message, session);
         }
         else {
            logger.trace("No result object given - no result to handle");
         }
      }
      ```

      这段代码结合注释看还是非常清晰的，这里的delegate，就是我们自己编写的listener，该adapter会判断

      delegate是否为SessionAwareMessageListener或者MessageListener的实现类，并调用他们各自的接口onMessage()方法来处理消息，从而实现消息监听。更常见的情形，是我们自定义的pojolistener并没有实现两个接口，那么代码中是通过`invokeListenerMethod()`这个方法来调用pojolistener的监听器方法的。点开`handleResult(result, message, session)`方法，我们可以发现它会返回响应信息给broker(在其继承的抽象类：org.springframework.jms.listener.adapter.AbstractAdaptableMessageListener#handleResult):

      ```java
      /**
       * Handle the given result object returned from the listener method,
       * sending a response message back.
       * @param result the result object to handle (never {@code null})
       * @param request the original request message
       * @param session the JMS Session to operate on (may be {@code null})
       * @throws ReplyFailureException if the response message could not be sent
       * @see #buildMessage
       * @see #postProcessResponse
       * @see #getResponseDestination
       * @see #sendResponse
       */
      protected void handleResult(Object result, Message request, @Nullable Session session) {
         if (session != null) {
            if (logger.isDebugEnabled()) {
               logger.debug("Listener method returned result [" + result +
                     "] - generating response message for it");
            }
            try {
               Message response = buildMessage(session, result);
               postProcessResponse(request, response);
               Destination destination = getResponseDestination(request, response, session, result);
               sendResponse(session, destination, response);
            }
            catch (Exception ex) {
               throw new ReplyFailureException("Failed to send reply with payload [" + result + "]", ex);
            }
         }
      
         else {
            // No JMS Session available
            if (logger.isWarnEnabled()) {
               logger.warn("Listener method returned result [" + result +
                     "]: not generating response message for it because of no JMS Session given");
            }
         }
      }
      ```

   4. **MessagingMessageListenerAdapter**

      从类图中可以看出，同样继承自 AbstractAdaptableMessageListener，实现了MessageListener, SessionAwareMessageListener两个接口，是SPRING 4.1 以后新添加的。[在3.2.4.3](#3.2.4.3 jms消息监听（二）)小节中，我所使用的PojoListener 这种注解式的监听方式，就是需要借由这个类来实现，我们在broker一侧可以观察到这一点：

      ![image-20200817141220862](https://raw.githubusercontent.com/SterryWang/picsbed/master/img/20200817141231.png)

      使用上我觉得它的功能和MessageListenerAdapter的功能类似，这里不再多花时间去详细剖析它了。

      附参考：

      [Spring JMS---三种消息监听器](https://blog.csdn.net/u012881904/article/details/51388456)

      

##### 3.2.4.2 jms消息监听（一）

我们先看第一种实现方式：

[消息监听器实现类](src/main/java/com/example/demo/message/MyJmsMessageListener.java)：

```java
package com.example.demo.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;


/**
 * 一个实现了MessageListener接口的监听器
 */
public class MyJmsMessageListener implements MessageListener {
    private static Logger log = LoggerFactory.getLogger(MyJmsMessageListener.class);

    @Override
    public void onMessage(Message message) {

        log.info("接收到的MESSAGE类型为：{}", message.getClass());
        if (message instanceof TextMessage) {

            try {
                log.info("接收到的信息为：{}", ((TextMessage) message).getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        } else if (message instanceof ObjectMessage) {
            try {
                log.info("接收到的对象为：{}", ((ObjectMessage) message).getObject());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        } else {
            log.info("接收的信息为：{}", message);
        }

    }
}
```

该类实现了MessageListener接口，并重写了onMessage()方法，消息会直接通过入参送进来，我们可以在方法内处理消息，[前文3.2.3](#3.2.3.2 发送案例)小节提到的消息转换器把消息java对象化等等。

下一步，我们把消息监听器放入消息监听器容器里，[参见ActiveMQConfig.java](src/main/java/com/example/demo/message/ActiveMQConfig.java)：

```java
@Bean
public MyJmsMessageListener myJmsMessageListener() {
    return new MyJmsMessageListener();
}

@Bean
public MessageListenerContainer msgListenerContainer(@Qualifier("MyActiveMQCF") ConnectionFactory cf, @Qualifier("defJmsDst") Destination destination, @Qualifier("myJmsMessageListener") Object messageListener) {

    DefaultMessageListenerContainer messageListenerContainer = new DefaultMessageListenerContainer();

       //注入消息监听器
        messageListenerContainer.setMessageListener(messageListener);
        //注入连接工厂
        messageListenerContainer.setConnectionFactory(cf);
        //设置消息目的地
        messageListenerContainer.setDestination(destination);

        //设置持久化订阅，非订阅模式其实无需设置
        messageListenerContainer.setClientId("wangxglalala");
        messageListenerContainer.setSubscriptionDurable(true);
        messageListenerContainer.setSubscriptionName("hey,jude");





    return messageListenerContainer;
}
```

注意到我们使用的是` DefaultMessageListenerContainer`作为监听器容器，并且要设置好它的各项关键属性，说明在代码注释里都已经标明了。

测试流程就比较简单了，应用启动后会开启监听，我们往队列或者主题里发送消息，就可以快速监听到了。

##### 3.2.4.3 jms消息监听（二）

这小节讲述第二种消息监听的实现方式：使用注解将普通pojo类注册为消息监听器

这种方法下我们无需实现`MessageListener`接口，只要定义一个简单的pojo类，[通过注解把它声明为监听器](src/main/java/com/example/demo/message/PojoListener.java)：

```java
package com.example.demo.message;


import com.example.demo.entity.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;

/**
 * 不实现messageListener接口的监听器
 */
@Component
public class PojoListener {

    private static Logger log = LoggerFactory.getLogger(PojoListener.class);


    @JmsListener(containerFactory = "pojoJmsListenerContainer", destination = "jms.topic")
    public void receiveMsg(Message msg) {
        log.info("pojolistener 接收到消息:{}", msg);
        log.info("延时中。。。");
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("延时完成，该则消息处理结束！");
    }

  /* @JmsListener(containerFactory = "myJmsListenerContainer", destination = "jms.topic")
    public void receiveMsg(Employee e) {
        log.info("pojolistener 接收到消息:{}" , e);
    }*/
}
```

第一，把pojo通过`@Component`注解实例化，第二，在方法上添加`JmsListener`注解，并说明容器的beanid,消息目的地。最后，其实我们可以直接在方法上入参使用具体的pojo类来接收信息，spring 会自动把消息转换成该对象送入，就是我们注解掉的这部分代码。

然后我们看下具体的监听器容器配置：[JmsReceiverConfig](src/main/java/com/example/demo/message/JmsReceiverConfig.java)

```java
package com.example.demo.message;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;

import javax.jms.ConnectionFactory;

@Configuration
@EnableJms
public class JmsReceiverConfig {

    @Bean
    public JmsListenerContainerFactory pojoJmsListenerContainer(@Qualifier("consumerCachingConnectionFactory") ConnectionFactory cf) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        //注入连接工厂
        factory.setConnectionFactory(cf);
        //设置默认域为订阅模式
        factory.setPubSubDomain(true);

        //设置持久化订阅，如果consumerCachingConnectionFactory()中设置了client
        // factory.setClientId("pojolistenercontainer");
        factory.setSubscriptionDurable(true);

        //DestinationResolver有默认值 DynamicDestinationResolver,所以无需设置
        //factory.setDestinationResolver(destinationResolver());

        //设置事务
        // factory.setSessionTransacted(true);
        //开启并发,也就是设置多消费者，它和持久化订阅是冲突的，因为持久化订阅要求clientID唯一
        // factory.setConcurrency("2");

        //每个线程处理n个消息后使用新线程，可以不设置
        // factory.setMaxMessagesPerTask(2);


        return factory;

    }


    @Bean
    public ConnectionFactory consumerCachingConnectionFactory(ConnectionFactory cf) {
        CachingConnectionFactory ccf = new CachingConnectionFactory(cf);
        //设置会话缓存数量，如果监听器容器开启了并发，可以提高会话缓存数量来提高效率
        ccf.setSessionCacheSize(50);
        //持久化订阅需要设置clientID,
        ccf.setClientId("lalala");

        return ccf;

    }


}
```

`consumerCachingConnectionFactory(ConnectionFactory cf)`方法通过入参注入了前文的`ActiveMQConnectionFactory`，并完成了`CachingConnectionFactory`连接工厂的实例化，它其实是在底层的连接工厂上包了一层，该工厂类继承了`SingleConnectionFactory`，该工厂的特点为它只有一个工厂连接，并且可以通过缓存Session 对象，以及Consumer和Producer对象，从而避免了传统监听方式下，反复创建Connection,Session等对象造成的资源消耗。

因为我们在上面的容器里开启了持久化订阅，那么久势必要设置统一的ClientID,一个ClientID只能对应一个连接，在这里我们只能设置`CachingConnectionFactory`的ClientID，如果设置上方容器的ClientID是会报错的。



监听器容器我们是通过容器的工厂类`DefaultJmsListenerContainerFactory`来生成的，`factory.setConcurrency("2")`这步是设置监听器并发的，也就是多消费者，当容器使用了持久化订阅的时候，该容器是应该关掉并发的（多消费者），因为持久化订阅时一个容器里只能有一个消费者；但是但我们监听的是队列的时候，就不存在持久化订阅的说法，为了加快处理速度，我们就可以开启并发（同时注解掉clientID）。其他的一些关键参数的说明，参照代码注释即可。以上都测试过的。

==**关于持久化订阅**==

持久订阅和非持久订阅是针对Topic而言的，不是针对Queue的。在持久化订阅模式下，

- 生产者在不停地生产消息，此时若没有人订阅，消息直接废弃（启动Producer）
- 消费者1启动，无法接收到之前Producer生产的消息，只能接收到当前的消息（启动Consumer1）
- 消费者2启动，也无法接收之前Producer生产的消息，只能接收到当前的消息（启动Consumer2）
- 中断消费者2，消费者1继续接收消息，消费者2无法接收消息（停止Consumer2）
- 启动消费者2，消费者1继续接收消息，消费者2可以接收到之前停止后丢失的消息，并可以继续接收当前消息（启动Consumer2）
- ActiveMQ是通过ClientID判断消息是否已经发给连接点，若消费者的ClientID相同，那么只会被某一个消费者接收到消息，而另外一个会报错
- 与非持久订阅模式的区别仅为设置了ClinetID及创建消费者使用createDurableSubscriber方法

和非持久订阅比较，一个典型的特征是，非持久化订阅者无法接收离线消息，就是客户端离线后再连上来，无法接收到离线期间发送到topic的消息。相关参考：

[持久化订阅和非持久订阅对比](https://www.cnblogs.com/hapjin/p/5644402.html)

[TOPIC模式](https://blog.csdn.net/kedadiannao220/article/details/78917340)



==**关于PrefetchPolicy**==

**ActiveMQ使用预取极限（prefetch limit**）来限制一次性分发给单个消费者的最大消息个数。消费者则使用预取极限（prefetch limit）来设置其消息缓冲区的大小。具体的机制我们不去研究，我们说下它的现象，当consumer端设置prefetch个数为1时，客户端手上处理着一条消息，同时客户端的消息缓冲区里还排队放着一条消息。但是这条消息虽然留在了客户端的缓冲区里，但在ActiveMQ这里，它并没有出列，依然在队列算在队列中，具体表现为有两个consumer，消费同一个队列，设置适当的启动时间间隔和消息处理间隔，你就会发现他们处理消息的过程为：consumer1处理1、2则消息，consumer2处理3、4 则消息，如果consumer1先一步处理完了，就会继续处理5、6则消息，依次进行。

既然这是ActiveMQ的特性，那么我们就需要在ActiveMQ的相关属性上设置，有两种方法，可以再url上设置，也可以在ActiveMQ的专用[工厂类上设置](src/main/java/com/example/demo/message/ActiveMQConfig.java)：

```java
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
    ActiveMQPrefetchPolicy  prefetchPolicy  =  new ActiveMQPrefetchPolicy();

    //设置PrefetchPolicy
    prefetchPolicy.setQueuePrefetch(0);
    prefetchPolicy.setDurableTopicPrefetch(1);
    prefetchPolicy.setTopicPrefetch(1);
    connectionFactory.setPrefetchPolicy(prefetchPolicy);








    return connectionFactory;
}
```

我们在最后几行设置了prefetch policy，可以分别设置queue 和  topic的prefetch个数。

==**关于JMS事务**==

如果消息的发送或者接收过程也可以是一个事务，举个例子，开启事务后，发送过程的原子方法出现异常，我们可以回滚消息发送，从broker一侧观察到的就是，broker并没有接收到该消息；同样的，在接收端，如果接收消息出现错误，我们也可以通过回滚消息接收，从broker一侧看到的现象是，该消息并没有出列。

比如以jmsTemplate为例，我们使用@Transcational注解，然后认为抛出异常，[代码链接](src/main/java/com/example/demo/message/EmployeeMsgServiceImpl.java)

```java
package com.example.demo.message;

import com.example.demo.entity.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Destination;
import javax.jms.Message;

/**
 * @author wangxg3
 */

@Component
public class EmployeeMsgServiceImpl  implements   IEmployMsgService{
    private  static Logger  log   = LoggerFactory.getLogger(EmployeeMsgServiceImpl.class);

    JmsOperations jmsOperations;

    public  EmployeeMsgServiceImpl(@Qualifier("MyJmsTemplate") JmsOperations  jmsOperations){
        this.jmsOperations = jmsOperations;
    }
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void sendEmployeeInfo(Employee e, Destination dst) {
        jmsOperations.convertAndSend(dst,e);
        if(true){
            throw new  RuntimeException("故意抛出的异常，测试jms事务！");
        }
    }

    @Override
    @Transactional
    public String recvMsg(Destination dst) {
        Message msg;
        try {
           msg =jmsOperations.receive(dst);
           log.info("接收到的信息为：{}",msg);

        } catch (Exception ex) {
           throw new RuntimeException("接收并转换消息失败！",ex);
        }
        if(true){

            throw new  RuntimeException("故意抛出的异常，测试jms接收消息的事务！");

        }
        return msg.toString();

    }
}
```

然后在controller中添加测试入口:[DemoController.java](src/main/java/com/example/demo/controller/DemoController.java)

```java
@GetMapping("/jms/testTransaction")
public String testTransaction() {
    Employee e = new Employee();
    e.setId(1);
    e.setName("小白");
    e.setAge(18);
    //这种写法会默认目的地类型是队列而不是主题
    log.info("开始发送信息到queue了。。。");
    try {
        employMsgService.sendEmployeeInfo(e, jmsQueue);
    } catch (Exception ex) {

        log.error("消息发送失败，请检查事务是否已经回滚！", ex);
        return "消息发送失败，请检查事务是否已经回滚！";
    }
    return null;

}
@GetMapping("/jms/testTransactionRecv")
public String testTransactionRecv() {

    //这种写法会默认目的地类型是队列而不是主题
    log.info("开始从queue中接收信息了。。。");
    try {
       employMsgService.recvMsg(jmsQueue);
    } catch (Exception ex) {

        log.error("消息接收失败，请检查事务是否已经回滚！", ex);
        return "消息接收失败，请检查事务是否已经回滚！";
    }
    return null;

}
```

测试结果满足我们前文的结论。不过，当我在监听器的方式时，比如：

```java
@Bean
public JmsListenerContainerFactory pojoJmsListenerContainer(@Qualifier("consumerCachingConnectionFactory") ConnectionFactory cf) {
    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
    //注入连接工厂
    factory.setConnectionFactory(cf);
    //设置默认域为订阅模式
    factory.setPubSubDomain(false);

    //设置持久化订阅，需要consumerCachingConnectionFactory()设置clientID;
    // factory.setClientId("pojolistenercontainer");
    factory.setSubscriptionDurable(true);

    //DestinationResolver有默认值 DynamicDestinationResolver,所以无需设置
    //factory.setDestinationResolver(destinationResolver());

    //设置事务
     factory.setSessionTransacted(true);
    //开启并发,也就是设置多消费者，它和持久化订阅是冲突的，因为持久化订阅要求clientID唯一
    //factory.setConcurrency("2");

    //每个线程处理n个消息后使用新线程，可以不设置
    // factory.setMaxMessagesPerTask(2);


    return factory;

}
```

虽然我们 通过factory.setSessionTransacted(true)开启了事务，并且在listener的onMessage()方法中故意抛出了异常，而且也添加了@Transaction注解，可却并不能像前文jmsTemplate那样的事务回滚，也许是我这样的配置方法不对，时间有限，只能留待以后去探讨了。

#### 3.2.5 基于JMS的RPC

​		RPC(Remote Procedure Call)，远程过程调用。我们熟知的一种RPC服务，比如SOAP，客户端只要塞好请求头和请求体，然后在客户端端上执行方法A的调用（该方法在客户端无实现体），那么服务端对应的SOAP接口上的对应的方法A就会响应此接口，执行服务端业务逻辑，然后通过方法A返回结果。这样看，客户端只要执行API调用，API的实现过程放在服务端，这就是一个典型的RPC流程。

​		基于JMS的RPC也是类似，只不过它是使用JMS作为传输通道来进行远程方法调用。不过由于JMS本身的异步消息特性，**==基于JMS的RPC是异步的而非同步==**，所以他的RPC调用时没有返回结果的，方法的返回是void。

​		Spring 目前提供的基于JMS的RPC**==仅支持点对点消息（队列），==**而不支持订阅模式。Spring 提供的JMS 的RPC方案分为服务端和客户端两部分，我们分开来看。

##### 3.2.5.1 服务端配置

​		服务端有一个核心组件是JmsInvokerServiceExporter，顾名思义，就是一个导出JmsInvokerService(基于JMS的远程调用服务)的组件，它实现了`SessionAwareMessageListener`接口，所以本质上它是一个消息。我们直接看代码配置:

[JmsRpcServiceServerConfig.java](src/main/java/com/example/demo/message/JmsRpcServiceServerConfig.java)

```java
package com.example.demo.message;


import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.remoting.JmsInvokerServiceExporter;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Queue;

/**
 * @author wangxg3
 */
@Configuration
public class JmsRpcServiceServerConfig {

    /**
     * 实例化消息目的地
     *
     * @return
     */
    @Bean
    public Queue jmsRpcDst() {
        return new ActiveMQQueue("jms.rpc.queue");
    }

    /**
     * 实例化待导出的RPC服务
     *
     * @return
     */
    @Bean
    public IMsgRpcService msgRpcService() {
        return new MsgRpcServiceImpl();
    }

    /**
     * 实例化JMS RPC服务 exporter
     * 本质上也是一个消息监听器
     *
     * @param msgRpcService
     * @param jmsRpcDst
     * @return
     */
    @Bean
    public JmsInvokerServiceExporter jmsInvokerServiceExporter(IMsgRpcService msgRpcService, Destination jmsRpcDst) {
        JmsInvokerServiceExporter jmsInvokerServiceExporter = new JmsInvokerServiceExporter();
        jmsInvokerServiceExporter.setService(msgRpcService);
        jmsInvokerServiceExporter.setServiceInterface(IMsgRpcService.class);

        return jmsInvokerServiceExporter;
    }

    /**
     * 消息监听器容器
     *
     * @param cf
     * @param jmsRpcDst
     * @param jmsInvokerServiceExporter
     * @return
     */
    @Bean
    public DefaultMessageListenerContainer rpcMsgListenerContainer(@Qualifier("MyActiveMQCF") ConnectionFactory cf, Destination jmsRpcDst, JmsInvokerServiceExporter jmsInvokerServiceExporter) {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setDestination(jmsRpcDst);
        container.setConnectionFactory(cf);
        container.setMessageListener(jmsInvokerServiceExporter);

        return container;
    }


}
```

从配置中我们基本可以看出它所需要的基础组件：

```java
DefaultMessageListenerContainer  rpcMsgListenerContainer //监听器容器
  -ConnectionFactory cf              //注入连接工厂
  -Destination  jmsRpcDst           //jms监听目的地队列
  -MessageListener jmsInvokerServiceExporter    //  jmsInvokerServiceExporter,RPC服务导出器
     --Object service               //服务端业务层接口
     --Class<?> interface           //服务端业务层实现类，远程调用的业务逻辑实现
  
```

我们定义了`IMsgRpcService.java`作为服务端接口，`MsgRpcServiceImpl.java`作为业务接口的实现类，这些需要set进JmsInvokerServiceExporter，最后把JmsInvokerServiceExporter作为监听器注入到监听器容器中。需要注意的是，exporter里面仅需要执行接口和实现类，并没有指定方法的入口，这是不是说明可以把接口的所有方法都注册成RPC服务供调用方调用呢？我们将在后面3.2.5.3小节揭示这个猜测。附上服务端接口实现类的代码：



附上服务端的接口和实现类代码

##### 3.2.5.2 客户端配置

​		客户端负责触发RPC调用过程，因为JMS是异步消息机制，所以客户端不会通过接口来接收返回结果（返回是void）。客户端配置如下：[JmsRpcClientConfig.java](src/main/java/com/example/demo/message/JmsRpcClientConfig.java)

```java
package com.example.demo.message;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.remoting.JmsInvokerProxyFactoryBean;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

/**
 * 基于jms的RPC服务的客户端配置
 *
 * @author wangxg3
 */
@Configuration
public class JmsRpcClientConfig {
    /**
     * jms的Rpc调用的代理bean工厂
     * 该工厂会通过代理技术，
     * 实例化一个实现了接口{@link  JmsInvokerProxyFactoryBean#setServiceInterface(Class xInterface)} XInterface的代理类
     *
     * @param cf
     * @param jmsRpcDst
     * @return
     */
    @Bean
    public JmsInvokerProxyFactoryBean jmsRpcClient(@Qualifier("MyActiveMQCF") ConnectionFactory cf, Queue jmsRpcDst) {
        JmsInvokerProxyFactoryBean jmsInvokerProxyFactoryBean = new JmsInvokerProxyFactoryBean();
        //注入工厂
        jmsInvokerProxyFactoryBean.setConnectionFactory(cf);
        //注入目标队列
        jmsInvokerProxyFactoryBean.setQueue(jmsRpcDst);
        //注入
        //jmsInvokerProxyFactoryBean.setServiceInterface(IMsgRpcService.class);
        jmsInvokerProxyFactoryBean.setServiceInterface(IMsgRpcServiceClientCopy.class);


        return jmsInvokerProxyFactoryBean;

    }


}
```

通过分析代码可知客户端依赖的基础组件如下：

```java
JmsInvokerProxyFactoryBean jmsRpcClient      //rpc客户端调用代理工厂，可生成实现了设定接口的代理实现类，可能使用了动态代理技术
	-ConnectionFactory  connectionFactory=cf  //连接工厂
	-Queue  queue=jmsRpcDst                   //消息目的地队列
	-Class<?> serverInterface=IMsgRpcServiceClientCopy.class   //动态代理类将要实现的接口
```

核心组件就是这个工厂类，它将在容器初始化的时候生成一个对象，该对象是通过动态代理技术实现了IMsgRpcServiceClientCopy.class接口，所以我们可以看成它是IMsgRpcServiceClientCopy.class的代理bean,代替它完成RPC调用。既然是远程RPC,实现都在服务端，那么客户端只要接口作为RPC调用入口，是不需要实现类的。需要指出的是，为了模拟客户端和服务端是分离部署的，特意在客户端定义了基于服务端RPC接口的一个副本：[IMsgRpcServiceClientCopy.java](src/main/java/com/example/demo/message/IMsgRpcServiceClientCopy.java)

```java
package com.example.demo.message;

/**
 * IMsgRpcService接口在客户端侧的副本
 * @author wangxg3
 */
public interface IMsgRpcServiceClientCopy {
    /**
     * 定义一个不同于服务端接口方法名的方法
     * @param name
     */
    void sayHelloCopy(String name);

    /**
     * 客户端的调用方法保持和服务端IMsgRpcService方法名一致
     * @param name
     */
    void sayHello(String name);
}
```

##### 3.2.5.3 测试

直接看测试代码：[ActiveMQTests.java](src/test/java/com/example/demo/message/tests/ActiveMQTests.java)

```java
...
  /*  @Resource
    IMsgRpcService  jmsRpcClient;*/

    /**
     * 注入rpc客户端代理对象
     */
    @Resource
    IMsgRpcServiceClientCopy jmsRpcClient;
...


 @Test
    public void TestJmsRpc(){
        //服务端执行的结果为：向周杰伦问好
        jmsRpcClient.sayHello("周杰伦");

        //当客户端和服务端的方法名不一致的时候，远程RPC会失败的
        //jmsRpcClient.sayHelloCopy("周杰伦");
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
```



我在上一小节通过JmsInvokerProxyFactoryBean 生成代理对象jmsRpcClient，且该对象实现了 IMsgRpcServiceClientCopy接口，所以我在单元测试中通过 IMsgRpcServiceClientCopy类型进行bean注入。然后再TestJmsRpc()中直接调用它的接口方法，实现了RPC调用，服务端显示的结果符合预期：

![image-20200820155351712](https://raw.githubusercontent.com/SterryWang/picsbed/master/img/20200820155355.png)

前面我说了，还在客户端的RPC接口里添加了sayHelloCopy()接口，我们把注释放开，测试下这个方法调用，最终结果是报错了，服务端找不到对应方法：

```java
org.springframework.remoting.RemoteInvocationFailureException: Invocation of method [public abstract void com.example.demo.message.IMsgRpcServiceClientCopy.sayHelloCopy(java.lang.String)] failed in JMS invoker remote service at queue [queue://jms.rpc.queue]; nested exception is java.lang.NoSuchMethodException: com.sun.proxy.$Proxy128.sayHelloCopy(java.lang.String)

	at org.springframework.jms.remoting.JmsInvokerClientInterceptor.invoke(JmsInvokerClientInterceptor.java:217)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
	at org.springframework.aop.framework.JdkDynamicAopProxy.invoke(JdkDynamicAopProxy.java:212)
	at com.sun.proxy.$Proxy127.sayHelloCopy(Unknown Source)
	at com.example.demo.message.tests.ActiveMQTests.TestJmsRpc(ActiveMQTests.java:189)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at org.springframework.test.context.junit4.statements.RunBeforeTestExecutionCallbacks.evaluate(RunBeforeTestExecutionCallbacks.java:74)
	at org.springframework.test.context.junit4.statements.RunAfterTestExecutionCallbacks.evaluate(RunAfterTestExecutionCallbacks.java:84)
	at org.springframework.test.context.junit4.statements.RunBeforeTestMethodCallbacks.evaluate(RunBeforeTestMethodCallbacks.java:75)
	at org.springframework.test.context.junit4.statements.RunAfterTestMethodCallbacks.evaluate(RunAfterTestMethodCallbacks.java:86)
	at org.springframework.test.context.junit4.statements.SpringRepeat.evaluate(SpringRepeat.java:84)
	at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.runChild(SpringJUnit4ClassRunner.java:251)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.runChild(SpringJUnit4ClassRunner.java:97)
	at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
	at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
	at org.springframework.test.context.junit4.statements.RunBeforeTestClassCallbacks.evaluate(RunBeforeTestClassCallbacks.java:61)
	at org.springframework.test.context.junit4.statements.RunAfterTestClassCallbacks.evaluate(RunAfterTestClassCallbacks.java:70)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.run(SpringJUnit4ClassRunner.java:190)
	at org.junit.runner.JUnitCore.run(JUnitCore.java:137)
	at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:68)
	at com.intellij.rt.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:33)
	at com.intellij.rt.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:230)
	at com.intellij.rt.junit.JUnitStarter.main(JUnitStarter.java:58)
Caused by: java.lang.NoSuchMethodException: com.sun.proxy.$Proxy128.sayHelloCopy(java.lang.String)
	at java.lang.Class.getMethod(Class.java:1786)
	at org.springframework.remoting.support.RemoteInvocation.invoke(RemoteInvocation.java:214)
	at org.springframework.remoting.support.DefaultRemoteInvocationExecutor.invoke(DefaultRemoteInvocationExecutor.java:39)
	at org.springframework.remoting.support.RemoteInvocationBasedExporter.invoke(RemoteInvocationBasedExporter.java:78)
	at org.springframework.remoting.support.RemoteInvocationBasedExporter.invokeAndCreateResult(RemoteInvocationBasedExporter.java:114)
	at org.springframework.jms.remoting.JmsInvokerServiceExporter.onMessage(JmsInvokerServiceExporter.java:104)
	at org.springframework.jms.listener.AbstractMessageListenerContainer.doInvokeListener(AbstractMessageListenerContainer.java:736)
	at org.springframework.jms.listener.AbstractMessageListenerContainer.invokeListener(AbstractMessageListenerContainer.java:696)
	at org.springframework.jms.listener.AbstractMessageListenerContainer.doExecuteListener(AbstractMessageListenerContainer.java:674)
	at org.springframework.jms.listener.AbstractPollingMessageListenerContainer.doReceiveAndExecute(AbstractPollingMessageListenerContainer.java:318)
	at org.springframework.jms.listener.AbstractPollingMessageListenerContainer.receiveAndExecute(AbstractPollingMessageListenerContainer.java:257)
	at org.springframework.jms.listener.DefaultMessageListenerContainer$AsyncMessageListenerInvoker.invokeListener(DefaultMessageListenerContainer.java:1189)
	at org.springframework.jms.listener.DefaultMessageListenerContainer$AsyncMessageListenerInvoker.executeOngoingLoop(DefaultMessageListenerContainer.java:1179)
	at org.springframework.jms.listener.DefaultMessageListenerContainer$AsyncMessageListenerInvoker.run(DefaultMessageListenerContainer.java:1076)
	at java.lang.Thread.run(Thread.java:748)
	at org.springframework.remoting.support.RemoteInvocationUtils.fillInClientStackTraceIfPossible(RemoteInvocationUtils.java:45)
	at org.springframework.remoting.support.RemoteInvocationResult.recreate(RemoteInvocationResult.java:156)
	at org.springframework.jms.remoting.JmsInvokerClientInterceptor.recreateRemoteInvocationResult(JmsInvokerClientInterceptor.java:422)
	at org.springframework.jms.remoting.JmsInvokerClientInterceptor.invoke(JmsInvokerClientInterceptor.java:210)
	... 34 more

2020-08-20 15:56:58.717 [Thread-8] INFO  o.s.context.support.DefaultLifecycleProcessor - Failed to shut down 1 bean with phase value 2147483647 within timeout of 30000: [rpcMsgListenerContainer]
2020-08-20 15:56:59.724 [rpcMsgListenerContainer-1] WARN  o.s.jms.listener.DefaultMessageListenerContainer - Setup of JMS message listener invoker failed for destination 'queue://jms.rpc.queue' - trying to recover. Cause: java.lang.InterruptedException
2020-08-20 15:56:59.741 [Thread-8] INFO  o.s.orm.jpa.LocalContainerEntityManagerFactoryBean - Closing JPA EntityManagerFactory for persistence unit 'default'
2020-08-20 15:56:59.742 [Thread-8] INFO  o.h.t.s.i.SchemaDropperImpl$DelayedDropActionImpl - HHH000477: Starting delayed evictData of schema as part of SessionFactory shut-down'
2020-08-20 15:56:59.750 [Thread-8] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Shutdown initiated...
2020-08-20 15:56:59.756 [Thread-8] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Shutdown completed.
2020-08-20 15:56:59.756 [Thread-8] INFO  o.s.cache.ehcache.EhCacheManagerFactoryBean - Shutting down EhCache CacheManager

Process finished with exit code -1

```



这说明了两点：

1. 服务端的RPC接口类名和客户端的RPC接口类名允许不一样
2. 客户端PRC发起调用时，客户端的接口方法名必须和服务端的接口方法名保持一致。

我们曾在3.2.5.1小节卖了个关子，如果我在服务端的PRC接口和实现类中都再添加一个方法sayHelloCopy(),是不是两个方法都可以映射成RPC服务呢？试试看：

```java
package com.example.demo.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangxg3
 */
public class MsgRpcServiceImpl implements  IMsgRpcService {
    private static Logger  log = LoggerFactory.getLogger(MsgRpcServiceImpl.class);
    @Override
    public void sayHello(String name) {
        log.info("向{}问好！",name);

    }

    @Override
    public void sayHelloCopy(String name) {
        log.info("sayHelloCopy()方法被调用，特向{}问好",name
        );
    }
}
```

如上，在服务端的接口和实现类中也添加了sayHelloCopy()方法，再重新测试下：

![image-20200820161155006](https://raw.githubusercontent.com/SterryWang/picsbed/master/img/20200820161157.png)



测试结果符合预期。

#### 3.2.6 小结

有空再写吧。

### 3.3 SPRING & AMQP实现

