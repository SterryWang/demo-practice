package com.example.demo.springcache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Employee;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangxg3
 */
@Component
public class EmployeeCachedDao {

    @Resource
    JdbcTemplate jdbcTemplate;


    @Cacheable(value = "employeeCache", unless = "#result.name.contains('zhangsan')")
    public Employee findOne(int id) {
        // 本地生成一个，暂时用来模拟从数据库里查数据的流程
        //判断是否从缓存里获取，只要看方法有没有执行即可
        System.out.println("从数据库里查询！");
        Employee employee = new Employee();
        //测试条件化缓存，设定zhangsan不缓存，lisi缓存
        if (id == 1) {
            employee.setId(id);
            employee.setName("zhangsan" + id);
             employee.setGender("m");
            System.out.println("id==1的姓名为zhangsan,zhangsan的将不予以写入缓存，但是可以通过saveone方法写入，通过此方法查询时，此信息则不会显示，因为unless会从缓存中查找");
        } else {
            employee.setId(id);
            employee.setName("lisi" + id);
            employee.setGender("m");
            System.out.println("id>1的姓名为lisi，lisi通过此方法写入数据库，仅显示一次，后续均从缓存中获取");

        }
        return employee;

    }

    /**
     * 满足contiton条件的给与缓存，否则关闭缓存存取
     *
     * @param id
     * @return
     */
    @Cacheable(value = "employeeCache", condition = "#id > 1")
    public Employee findOneCondition(int id) {
        // 本地生成一个，暂时用来模拟从数据库里查数据的流程
        //判断是否从缓存里获取，只要看方法有没有执行即可

        Employee employee = new Employee();
        //测试条件化缓存，设定zhangsan缓存，lisi不缓存
        if (id == 1) {
            employee.setId(id);
            employee.setName("zhangsan" + id);
            employee.setGender("m");
            System.out.println("id=1,不予缓存，不会写入缓存，也不会从缓存中查询，每次查询此信息必然显示");
        } else {
            System.out.println("id=" + id + ",所以满足condition条件，此信息仅显示一次，后续从缓存查询");
            employee.setId(id);
            employee.setName("lisi" + id);
            employee.setGender("m");

        }
        return employee;

    }

    @CachePut(value = "employeeCache", key = "#result.id")
    public Employee saveOne(Employee e) {
        // 模拟把数据保存到数据库
        System.out.println("数据已保存到数据库,同时也将通过切面自动保存到缓存中");
        return e;
    }


    @CacheEvict(value = "employeeCache")
    public void delete(int id) {
        System.out.println("从s数据库中删除id=" + id + "的数据！");
    }

    @CachePut(value = "employeeCache", key = "#key")
    public List<Employee> saveList(List<Employee> list, int key) {
        System.out.println("key=" + key + ",value=" + list);
        //插入数据库
        for (Employee e : list) {
            jdbcTemplate.update("insert into employee(id,name,age) values (?,?,?)", e.getId(), e.getName(), e.getAge());
        }

        return list;

    }


    @Cacheable(value = "employeeCache")
    public List<Employee> findList(int key) {
        //测试缓存返回
        System.out.println("查不到任何东西");
        return null;

    }

}
