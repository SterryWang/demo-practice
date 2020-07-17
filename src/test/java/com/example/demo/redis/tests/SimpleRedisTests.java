package com.example.demo.redis.tests;

import com.example.demo.DemoApplication;
import com.example.demo.entity.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { DemoApplication.class },webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SimpleRedisTests {


    @Resource
    RedisTemplate<String, Employee> redisTemplate1;
    @Resource
    RedisTemplate<String,Object> redisTemplateN;
    @Resource
    ExecutorService  simpleExecutor;

    /**
     * 简单测试下redis的存取
     */
    @Test
    public void test1(){
        Employee   e = new Employee();
        e.setId(1);
        e.setName("zhangsan");
        e.setAge(19);
        e.setGender("male");

        redisTemplate1.opsForValue().set(e.getName(),e);
       Employee  e2  = redisTemplate1.opsForValue().get(e.getName());
       // System.out.println("从redis查询回来的信息为："+redisTemplate.opsForValue().get(e.getName()));

        System.out.println(e==e2);
        assert e2 != null;
        System.out.println(e.toString().equals(e2.toString()));


    }

    /**
     * 测试新型redisTemplate的存取
     */
    @Test
    public void  test2(){

        Employee   e = new Employee();
        e.setId(1);
        e.setName("zhangsan");
        e.setAge(19);
        e.setGender("male");
        redisTemplateN.opsForValue().set(e.getName(),e);
       // redisTemplateN.opsForValue().set(e.getName(),e);
        Employee  e2  =(Employee) redisTemplateN.opsForValue().get(e.getName());
        // System.out.println("从redis查询回来的信息为："+redisTemplate.opsForValue().get(e.getName()));

        System.out.println(e==e2);
        assert e2 != null;
        System.out.println(e.toString().equals(e2.toString()));

    }



    @Test
    public  void test3(){
        //通过多线程DEBUG测试，检查reids连接池中是否真的有池连接
        for(int i=1;i<=8;i++){

            Employee   e = new Employee();
            e.setId(i);
            e.setName("zhangsan"+i);
            e.setAge(19);
            e.setGender("male");

           simpleExecutor.execute(new Runnable() {
               @Override
               public void run() {
                   redisTemplateN.opsForValue().set(e.getName(),e);
                   Employee  e2  = (    Employee) redisTemplateN.opsForValue().get(e.getName());
                   // System.out.println("从redis查询回来的信息为："+redisTemplate.opsForValue().get(e.getName()));

                   System.out.println(e==e2);
                   assert e2 != null;
                   System.out.println(e.toString().equals(e2.toString()));

                   System.out.println("数据提交到redis"+e);


               }
           }

           );

            System.out.println("提交了一个线程");

        }
        try {
            Thread.currentThread().sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("等待子线程执行完成！");

    }
}
