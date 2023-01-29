# Golang

## 一、go语言相关

### 1 字符串相关

#### （1）字符串基础

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

#### （2）字符串底层原理

##### 1）为什么字符串不允许修改？

在`go`实现中，`string`不包含内存空间，只有一个内存的地址，这样做的好处是`string`变得非常轻量，可以很方便的进行传递而不用担心内存拷贝。

`string`通常指向字符串字面量，而字符串字面量存储存储位置是只读段，而不是堆或栈上，所以`string`不可修改。

> 如果非要修改`string`类型的变量，可以先将其转换成`[]byte`切片进行修改
>
> ```go
> var str string = "hello"
> strBytes := []byte(str)
> strBytes[0] = 'H'
> str = string(strBytes)
> fmt.Println(str)
> ```

##### 2）如何高效的拼接字符串

Go 语言中，字符串是只读的，也就意味着每次修改操作都会创建一个新的字符串。如果需要拼接多次，应使用 `strings.Builder`，最小化内存拷贝次数。

```go
var str strings.Builder
for i := 0; i < 1000; i++ {
    str.WriteString("a")
}
fmt.Println(str.String())
```



### 2 数组相关

#### （1）数组的特点：

1. **数组在内存中的空间时连续的（无论是在栈上，还是在静态区）。**
2. 数组的大小不可改变，存储元素类型相同、但是大小不同的数组类型在 Go 语言看来也是完全不同的，只有两个条件（元素类型和大小）都相同才是同一类型

#### （2）在`Go`和`C`中，数组的工作方式有很大的不同，在`Go`中：

- 数组就是值。将一个数组赋值给另一个数组会复制所有的元素。
- 特别是，如果你把一个数组传递给一个函数，它将收到一个数组的副本，而不是一个指向它的指针。（如果希望传入函数中的数组是一个指针，可以使用切片）
- 数组的大小是其类型的一部分。类型 `[10]int` 和 `[20]int` 是不同的。

#### （3）`[n]T{1,2,3}`和`[...]T{1,2,3}`的区别

```go
var arr1 = [3]int{1, 2, 3}
var arr2 = [...]int{1, 2, 3}
```

go语言中，`[n]T{1,2,3}`和`[...]T{1,2,3}`在运行期二者等价，`[...]T{1,2,3}`会在编译器通过**遍历**的方式计算出数组中的元素个数，然后替换掉`...`。

#### （4）数组初始化（对于由字面量组成的数组）

- 当元素数量**小于或者等于 4 个**时，会直接将数组中的元素放置在**栈上**；
- 当元素数量**大于 4 个**时，会将数组中的元素放置到**静态区**并在运行时取出；

#### （5）数组访问

```go
var arr [3]int = [3]int{1, 2, 3}
i := 4
arr[4]//编译期检查出数组越界
arr[i]//运行期才能检查出数组越界
```

1. 直接使用整数或者常量访问数组，可以在编译期间检查出是否越界
2. 如果使用表达式访问数组，go语言无法在编译期间检查出是否越界，只能在运行期间才能检查出

### 3 切片相关

#### （1）定义切片

```go
slice1 := make([]int, 5) //表示创建一个len=5的切片
slice2 := make([]int, 0, 5) //表示创建一个容量cap=5的切片，len=0，即没有元素
slice3 := make([]int, 0, 5) // len(b)=0, cap(b)=5

//初始化
arr[0:3] or slice[0:3] //通过下标的方式获得数组或者切片的一部分；原切片数据被修改后，新切片数据也会被修改
slice := []int{1, 2, 3} //使用字面量初始化新的切片；
slice := make([]int, 10) //使用关键字 make 创建切片
```

切片的特点：

1. 大小不固定，相当于一个长度可变的数组
2. 切片会自动扩容，
3. 切片中存储数组数据的数据结构在内存中是连续的
4. 切片和数组不同，**数组的内存固定且连续，多数操作都会直接读写内存的特定位置**，切片是运行时才会确定内容的结构，所有操作还需要依赖 Go 语言的运行时状态

##### 2）切片扩容

1. 如果期望容量大于当前容量的两倍就会使用期望容量；
2. 如果当前切片的长度小于 1024 就会将容量翻倍；
3. 如果当前切片的长度大于 1024 就会每次增加 25% 的容量，直到新容量大于期望容量；

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

#### （1）unsafe

go语言是一门静态的编程语言，也就是不同类型的变量不能进行相互转换，比如把一个`int32`指针变量转换成`float32`类型的指针变量这个操作在go语言中是不合法的。因为指针中存放的是实际变量的地址，如果我们想要进行不同类型的指针变量互转怎么办，使用`unsafe.Pointer`。

简单来讲就是，`unsafe`包就是不怎么推荐你使用。因为它是 unsafe（不安全的），但是在特殊的场景下，使用了它。可以打破 Go 的类型和内存安全机制

##### 1）Pointer

- 任何类型的指针值都可以转换为 Pointer
- Pointer 可以转换为任何类型的指针值
- uintptr 可以转换为 Pointer
- Pointer 可以转换为 uintptr

错误示例：

```go
func main(){
    num := 5
    numPointer := &num

    flnum := (*float32)(numPointer)//这里不能互转
    fmt.Println(flnum)
}
```

正确示例：

```go
func main(){
    num := 5
    numPointer := &num

    flnum := (*float32)(unsafe.Pointer(numPointer))
    fmt.Println(flnum)
}
```

##### 2）Offsetof

```go
type Num struct{
    i string
    j int64
}

func main(){
    n := Num{i: "EDDYCJY", j: 1}
    nPointer := unsafe.Pointer(&n)

    niPointer := (*string)(unsafe.Pointer(nPointer))
    *niPointer = "煎鱼"

    njPointer := (*int64)(unsafe.Pointer(uintptr(nPointer) + unsafe.Offsetof(n.j)))
    *njPointer = 2

    fmt.Printf("n.i: %s, n.j: %d", n.i, n.j)
}
```

输出：

```
n.i: 煎鱼, n.j: 2
```

结构体的一些基本概念：

- 结构体的成员变量在内存存储上是一段连续的内存
- 结构体的初始地址就是第一个成员变量的内存地址
- 基于结构体的成员地址去计算偏移量。就能够得出其他成员变量的内存地址

再回来看看上述代码，得出执行流程：

- 修改 `n.i` 值：`i` 为第一个成员变量。因此不需要进行偏移量计算，直接取出指针后转换为 `Pointer`，再强制转换为字符串类型的指针值即可
- 修改 `n.j` 值：`j` 为第二个成员变量。需要进行偏移量计算，才可以对其内存地址进行修改。在进行了偏移运算后，当前地址已经指向第二个成员变量。接着重复转换赋值即可

需要注意的是，这里使用了如下方法（来完成偏移计算的目标）：

1、uintptr：`uintptr` 是 Go 的内置类型。返回无符号整数，可存储一个完整的地址。常用于指针运算，`uintptr` 类型是不能存储在临时变量中的。因为从 GC 的角度来看，`uintptr` 类型的临时变量只是一个无符号整数，并不知道它是一个指针地址，因此当满足一定条件后，`ptr` 这个临时变量是可能被垃圾回收掉的，如果被GC回收掉了，那么接下来的内存操作会引发一些系统错误。

2、unsafe.Offsetof：返回成员变量 x 在结构体当中的偏移量。更具体的讲，就是返回结构体初始位置到 x 之间的字节数。需要注意的是入参 `ArbitraryType` 表示任意类型，并非定义的 `int`。它实际作用是一个占位符

### 15 pprof使用

### 16 内存管理

#### （1）TCMalloc

#### （2）内存分配器

#### （3）竞态

#### （4）逃逸分析

##### 1）什么是逃逸分析

> 在 C 语言中，可以使用 `malloc` 和 `free` 手动在堆上分配和回收内存。Go 语言中，堆内存是通过垃圾回收机制自动管理的，无需开发者指定。那么，Go 编译器怎么知道某个变量需要分配在栈上，还是堆上呢？编译器决定内存分配位置的方式，就称之为逃逸分析(escape analysis)。逃逸分析由编译器完成，作用于编译阶段。

在go语言中，有2个地方可以为变量分配内存，分别是**栈（stack）**和**堆（heap）**。其中，局部变量在栈中分配，全局变量在堆中分配，如果一个指针对象在某个函数（函数内部的创建的对象理论上是分配在栈上，函数结束，对象随之被释放）中产生，但是该被该函数返回了，也就是说该对象还有引用对象，那么就不能和函数一起被释放，所以需要将该对象“逃逸”到堆上分配。

##### 2）哪些情况会发生逃逸

1. 指针逃逸

   ```go
   package main
   
   import "fmt"
   
   type Demo struct {
   	name string
   }
   
   func createDemo(name string) *Demo {
   	d := new(Demo) // 局部变量 d 逃逸到堆
   	d.name = name
   	return d
   }
   
   func main() {
   	demo := createDemo("demo")
   	fmt.Println(demo)
   }
   ```

   这个例子中，函数 `createDemo` 的局部变量 `d` 发生了逃逸。d 作为返回值，在 main 函数中继续使用，因此 d 指向的内存不能够分配在栈上，随着函数结束而回收，只能分配在堆上。

   编译时可以借助选项 `-gcflags=-m`，查看变量逃逸的情况：

   ```
   $ go build -gcflags=-m main_pointer.go 
   ./main_pointer.go:10:6: can inline createDemo
   ./main_pointer.go:17:20: inlining call to createDemo
   ./main_pointer.go:18:13: inlining call to fmt.Println
   ./main_pointer.go:10:17: leaking param: name
   ./main_pointer.go:11:10: new(Demo) escapes to heap
   ./main_pointer.go:17:20: new(Demo) escapes to heap
   ./main_pointer.go:18:13: demo escapes to heap
   ./main_pointer.go:18:13: main []interface {} literal does not escape
   ./main_pointer.go:18:13: io.Writer(os.Stdout) escapes to heap
   <autogenerated>:1: (*File).close .this does not escape
   ```

   `new(Demo) escapes to heap` 即表示 `new(Demo)` 逃逸到堆上了。

2. interface{} 动态类型逃逸

   在 Go 语言中，空接口即 `interface{}` 可以表示任意的类型，如果函数参数为 `interface{}`，编译期间很难确定其参数的具体类型，也会发生逃逸。

3. 栈空间不足

   操作系统对内核线程使用的栈空间是有大小限制的，64 位系统上通常是 8 MB。可以使用 `ulimit -a` 命令查看机器上栈允许占用的内存的大小。

   - 当切片占用内存超过一定大小（栈的最大容积），或无法确定当前切片长度时，对象占用内存将在堆上分配。
   - 当切片占用内存小于一定大小，则不会发生逃逸

4. 闭包

   > 什么是闭包？
   >
   > 一个函数和对其周围状态（lexical environment，词法环境）的引用捆绑在一起（或者说函数被引用包围），这样的组合就是闭包（closure）。也就是说，闭包让你可以在一个内层函数中访问到其外层函数的作用域。

   ```go
   func Increase() func() int {
   	n := 0
   	return func() int {
   		n++
   		return n
   	}
   }
   
   func main() {
   	in := Increase()
   	fmt.Println(in()) // 1
   	fmt.Println(in()) // 2
   }
   ```

   `Increase()` 返回值是一个闭包函数，该闭包函数访问了外部变量 n，那变量 n 将会一直存在，直到 `in` 被销毁。很显然，变量 n 占用的内存不能随着函数 `Increase()` 的退出而回收，因此将会逃逸到堆上。

   ```
   $ go build -gcflags=-m main_closure.go 
   # command-line-arguments
   ./main_closure.go:6:2: moved to heap: n
   ```

##### 3）如何利用逃逸分析提升性能

传值会拷贝整个对象，而传指针只会拷贝指针地址，指向的对象是同一个。传指针可以减少值的拷贝，但是会导致内存分配逃逸到堆中，增加垃圾回收(GC)的负担。在对象频繁创建和删除的场景下，传递指针导致的 GC 开销可能会严重影响性能。

一般情况下，对于需要修改原对象值，或占用内存比较大的结构体，选择传指针。对于只读的占用内存较小的结构体，直接传值能够获得更好的性能。

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

2. [go引入三方包](./go引入三方包.md)
3. [go语言特征](./go语言特征.md)
4. [Go语言学习笔记.md](./Go语言学习笔记.md)
5. [GO语言中的闭包.md](./GO语言中的闭包.md)
6. [《Go 语言设计与实现》](https://draveness.me/golang/)
7. [有点不安全却又一亮的 Go unsafe.Pointer](https://segmentfault.com/a/1190000017389782)