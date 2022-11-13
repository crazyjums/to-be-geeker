# Java中arraylist.remove提示索引越界异常

## 1 问题

需求：dayIdList中存着31个数字，表示八月的每一天的dayId，currDataList列表里面的每个元素有个属性是dayId，如果currDataList中的元素的dayId在dayIdList中存在，则把dayIdList中的值删除。

```Java
public static void main() {
    List<Integer> dayIdList = new ArrayList<>(31);
	dayIdList.add(20220801);
	//...  dayIdList: [20220801, ... 20220831]
	currDataList.forEach(item -> dayIdList.remove(Integer.parseInt(String.valueOf(item.getDayId()))));
}
```

但是执行之后，提示索引越界：

```

2022-09-07 15:27:04.807[1662535624425X330362][XNIO-2 task-2] ERROR GlobalExceptionHandler-Index: 20220801, Size: 31
2022-09-07 15:27:04.810[1662535624425X330362][XNIO-2 task-2] ERROR GlobalExceptionHandler-系统异常：
1662535624425X330362	java.lang.IndexOutOfBoundsException: Index: 20220801, Size: 31
1662535624425X330362	at java.util.ArrayList.rangeCheck(ArrayList.java:657)
1662535624425X330362	at java.util.ArrayList.remove(ArrayList.java:496)
```

## 2 解决

查看remove方法源码：

```Java
public E remove(int index) {
    rangeCheck(index);

    modCount++;
    E oldValue = elementData(index);

    int numMoved = size - index - 1;
    if (numMoved > 0)
        System.arraycopy(elementData, index+1, elementData, index,
                         numMoved);
    elementData[--size] = null; // clear to let GC do its work

    return oldValue;
}

public boolean remove(Object o) {
    if (o == null) {
        for (int index = 0; index < size; index++)
            if (elementData[index] == null) {
                fastRemove(index);
                return true;
            }
    } else {
        for (int index = 0; index < size; index++)
            if (o.equals(elementData[index])) {
                fastRemove(index);
                return true;
            }
    }
    return false;
}
```

查看源码发现，arraylist有2个remove方法，一个是根据索引删除，一个是根据对象删除，如果想要arraylist根据对象删除数据，那么传入的参数也得是个对象。在最开始的代码中，由于currDataList中的元素的dayId属性是Long类型，所以我做了一个转换，但是Integer.parseInt返回的是一个int类型的数，而不是Integer对象，所以这里报错了。

```Java
public static int parseInt(String s) throws NumberFormatException {
    return parseInt(s,10);
}
```

将最开始的删除代码转成对象传入remove方法即可：

```Java
currDataList.forEach(item -> dayIdList.remove((Integer) Integer.parseInt(String.valueOf(item.getDayId()))));
```

