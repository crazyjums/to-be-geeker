# update table set a=a+1 where id=1; 是否存在线程安全问题？

**结论：不存在，这个SQL是一个原子操作，不存在线程安全问题。**

MySQL在执行**update**操作时，会持有该**id=1**这行数据的**行级锁**（也叫排他锁），如果MySQL还想update该id=1这一行的数据，那么首先他需要拿到这一行的行级锁才能对其进行操作，由于前一个会话还没有释放该行级锁，所以当前会话就拿不到该行级锁，就不能执行update的操作。



**如果是在代码中，先select这一行数据，然后在加1，然后update回去，这样会有线程安全问题？**

伪代码：

```java
@Select("select num from a where id = #{id}; ")
int selectA(int id);

@Update("update a set num = #{num} where id = #{id}; ")
int updateA(int num, int id);

int a = selectA(1);
a++;
updateA(a, 1);
```

这样操作是有线程安全问题的，默认情况下，select操作时没有加行级锁的。

> **MySQL表中的数据行加了行级锁和不加行级锁的区别？**
>
> - 一旦用户对某个行施加了行级加锁，则该用户**即可以查询又可以更新**被加锁的数据行
>
> - 而其它用户**只能查询但不能更新**被加锁的数据行
>
> 如果其他用户也想修改该行数据，那只能拿到该行的行级锁（行级锁是一种独占锁）才能进行修改，否则只能等待。

**那么如何给select操作加上行级锁呢？**

```java
@Select("select num from a where id = #{id} for update; ")  //加行级锁
int selectA(int id);

@Update("update a set num = #{num} where id = #{id}; ")
int updateA(int num, int id);

int a = selectA(1);
a++;
updateA(a, 1);
```

> 注意：select … for update ,如果查询条件带有主键，会锁行数据，如果没有，会**锁表**，另外并发访问下可能会造成**死锁**！

# Reference

1. https://blog.csdn.net/C_AJing/article/details/106547240