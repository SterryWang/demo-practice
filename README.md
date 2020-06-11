# demo-practice
a project aimed at training,testing,validating
##Introduction
  基于SPRING BOOT 工程骨架，用于编写验证测试各种在学习、培训、工作中的用到的各种小功能，例如多线程，事务，设计模式，注解，代理，SPRING AOP切面编程等等，提供一些可随时使用、容易理解的DEMO，以PACKAGE为单位
每一个package展示一项小功能。



## SPRING CACHE
代码库链接：
[springaop](src/main/java/com/example/demo/springcache/package-info.java)  

### 缓存支持方式
有两种
- 注解驱动的缓存

- XML声明的缓存  
  XML生命式缓存我们先不表，先说注解式缓存，就是说在bean上添加缓存注解，使得这个类支持缓存（比如一个dao类），而开启注解式缓存有两种方式，

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

3. 为具体的实现类添加缓存支持

现在我们第一个步骤上一小节已经讲完了，后面讲围绕后面两个步骤来说。

### 配置缓存管理器

缓存管理器多种多样，我们简单说两种，一种是很简单的本地缓存，另一种是Redis缓存

代码链接：[缓存配置DEMO](src/main/java/com/example/demo/springcache/CachingConfig.java)

基本的配置方法直接看代码即可。在这里，我们关注以下几点：

1. 多缓存管理器的情况，以及```@Primary``` 注解的使用

2. 组合缓存管理器CompositeCacheManager的配置方法和工作机制

3. redis 缓存管理器 （独立缓存服务器）

   redis缓存其实可以一分为二的看，一个是REDIS服务端的安装配置使用；另一个，是SPRING端接入REDIS的相关API。所以我们后面单独讲REDIS的时候再集中说吧。



### 







