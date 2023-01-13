# Golang

## 一、go语言相关

### 1 字符串相关

### （1）字符串基础

##### 1）基本使用

参考：[http://c.biancheng.net/view/17.html](http://c.biancheng.net/view/17.html)

##### 2）计算字符串长度

go语言中，字符串都是基于UTF-8编码的，所以在go语言中不存在乱码的情况，但是，在统计字符串长度时，需要注意，如果字符串中含有中文，且我们的预期也是一个中文算一个字符的话，那么不能好用`len()`计算长度，应该用`utf8.RuneCountInString()`计算。

```go
package main

import "fmt"

func main() {
    //---计算字符串的长度---
	//ASCII 字符串长度使用 len() 函数。
	//Unicode 字符串长度使用 utf8.RuneCountInString() 函数。
	str1 := "I am a man;"
	str2 := "很好aa"
	//len() 函数的返回值的类型为 int，表示字符串的 ASCII 字符个数或字节长度
	//如果字符串中有中文，返回的结果可能会不是我们想要的结果
	//因为在go中，字符串都是用utf8编码，一个中文字占3个字符
	fmt.Println(len(str1), len(str2)) //11 6
	//utf8.RuneCountInString会将字符串中的中文当成一个"字符"来统计
	fmt.Println(utf8.RuneCountInString(str1), utf8.RuneCountInString(str2)) //11 2
}
```



### 2 数组相关

### 3 切片相关

### 4 map相关

### 5 函数相关

### 6 defer相关

### 7 结构体相关

### 8 sync包

### 9 sync.map原理

### 10 sync.Pool原理

### 11 sync.Atomic原子操作及CAS操作

### 12 context

### 13 单元测试、性能测试

### 14 golang官方包相关使用

### 15 pprof使用

### 16 内存管理

#### （1）TCMalloc

#### （2）内存分配器

#### （3）竞态

#### （4）逃逸分析

#### （5）垃圾收集器

#### （6）主流垃圾回收算法

#### （7）栈内存管理

### 17 go-routine

#### （1）GPM

#### （2）调度器的设计策略

#### （3）Go协作式和抢占式调度

#### （4）routine相关函数

#### （5）协程池

#### （6）select

#### （7）channel

### 18 gin框架相关

#### （1）gin中间件算法

#### （2）gin的路由算法

#### （3）gin的context

### 19 go语言实现UDP协议及TCP通讯

## 二、go build执行详解

## 三、go软件包管理器

## 四、go编译器相关

### 1 go语言编译器执行流程

### 2 语法解析

### 3 词法解析

### 4 抽象语法树构建

### 5 类型检查

### 6 变量捕捉

### 7 函数内联

### 8 逃逸分析

### 9 闭包重写

### 10 遍历函数

### 11 SSA生产

### 12 机器码生成--汇编器

## 五、二进制可执行文件





## Reference

1. [《**Go语言底层原理剖析(博文视点出品)**》](https://item.jd.com/13398708.html)

1. [go引入三方包](./go引入三方包.md)
2. [go语言特征](./go语言特征.md)
3. [Go语言学习笔记.md](./Go语言学习笔记.md)
4. [GO语言中的闭包.md](./GO语言中的闭包.md)