## MySQL中删除数据truncate和delete的区别？

### （1）删除表信息的方式有两种 :

```sql
truncate table table_name;
delete * from table_name;
```

> 注 : `truncate`操作中的table可以省略，`delete`操作中的*可以省略

### （2）truncate、delete 清空表数据的区别 :

1. `truncate` 是整体删除 (速度较快)，`delete`是逐条删除 (速度较慢)
2. `truncate`  不写服务器 log，delete 写服务器 log，这也是 `truncate` 效率比 `delete`高的原因
3. `truncate` 不激活trigger (触发器)，但是会重置Identity (标识列、自增字段)，相当于自增列会被置为初始值，又重新从1开始记录，而不是接着原来的 ID继续自增。而 delete 删除以后，identity 依旧是接着被删除的最近的那一条记录ID加1后进行记录。如果只需删除表中的部分记录，只能使用 DELETE语句配合 where条件即可，如果需要清空表数据，建议使用`truncate`清空。

## Reference

1. https://blog.csdn.net/chenshun123/article/details/79676446