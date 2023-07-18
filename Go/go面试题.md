## GO面试题总结

### （1）make(chan int, 1)  和 make(chan int)有区别吗

`make(chan int, 1)` 和 `make(chan int)` 之间有区别。

1. `make(chan int, 1)` 创建了一个有缓冲的通道，容量为1。这意味着通道可以缓存一个整数元素，即使没有接收方，发送操作也不会被阻塞，直到通道已满。如果没有接收方，发送操作会立即完成。如果通道已满，发送操作会被阻塞，直到有接收方接收数据。这种通道适用于发送方和接收方的速度不一致的情况。
2. `make(chan int)` 创建了一个无缓冲的通道，容量为0。这意味着通道没有缓冲区，发送操作会阻塞直到有接收方接收数据，而接收操作也会阻塞直到有发送方发送数据。这种通道适用于同步操作，即发送方和接收方需要同步地进行数据交换。

因此，根据具体的需求和使用场景，选择适当的通道类型是很重要的。如果需要在发送和接收之间有一定的缓冲空间，可以使用有缓冲通道；如果需要同步操作，确保发送和接收的同步性，可以使用无缓冲通道。

### （2）Go GC有几个阶段

目前的go GC采用**三色标记法**和**混合写屏障**技术。

Go GC有**四**个阶段:

- STW，开启混合写屏障，扫描栈对象；
- 将所有对象加入白色集合，从根对象开始，将其放入灰色集合。每次从灰色集合取出一个对象标记为黑色，然后遍历其子对象，标记为灰色，放入灰色集合；
- 如此循环直到灰色集合为空。剩余的白色对象就是需要清理的对象。
- STW，关闭混合写屏障；
- 在后台进行GC（并发）。

### （3）go中的字符串拼接有哪几种，每种方式的效率如何

在 Go 中，有几种常见的字符串拼接方式：

1. `+` 运算符：使用 `+` 运算符可以直接连接两个字符串，例如 `str := "Hello, " + "World!"`。这种方式简单直接，适用于少量字符串的拼接。
2. `fmt.Sprintf` 函数：`fmt.Sprintf` 函数可以根据格式化字符串将多个值拼接为一个字符串。它的使用方式类似于 `fmt.Printf`，但是不会将结果打印到标准输出，而是返回拼接后的字符串。例如 `str := fmt.Sprintf("The answer is %d", 42)`。
3. `strings.Join` 函数：`strings.Join` 函数用于将字符串切片按照指定的分隔符连接成一个字符串。函数的参数是一个字符串切片和分隔符，返回拼接后的字符串。例如 `str := strings.Join([]string{"Hello", "World"}, ", ")`。
4. 字符串缓冲（`bytes.Buffer`）：使用 `bytes.Buffer` 类型可以高效地拼接大量的字符串。通过调用 `Buffer` 的 `WriteString` 方法来追加字符串，最后通过调用 `Buffer` 的 `String` 方法获取拼接后的结果。这种方式适用于需要频繁拼接大量字符串的情况。

这些字符串拼接方式各有特点，可以根据具体的场景和需求选择合适的方法。对于简单的拼接，使用 `+` 运算符或 `fmt.Sprintf` 即可；对于多个字符串的连接，可以使用 `strings.Join`；而对于频繁拼接大量字符串的场景，使用 `bytes.Buffer` 是更高效的选择。

> 效率对比：`+`<`fmt.Sprintf`<`strings.Join`<字符串缓冲（`bytes.Buffer`）

在这几种字符串拼接方式中，使用 `+` 运算符拼接字符串的效率通常是最低的。

原因是，字符串是不可变的，每次使用 `+` 运算符拼接字符串时，都会创建一个新的字符串，并将原字符串和新的字符串复制到新分配的内存空间中。这意味着每次拼接都需要进行内存分配和数据复制的操作，对于大量的字符串拼接会导致性能下降。

相比之下，其他方式的字符串拼接更加高效。`fmt.Sprintf` 函数使用了内部的缓冲区来构建字符串，可以避免频繁的内存分配和复制操作。`strings.Join` 函数直接将多个字符串连接到一个预先分配的内存空间中，而不需要重复分配和复制。而使用 `bytes.Buffer` 类型可以高效地进行字符串拼接，因为它提供了一个可变的缓冲区，避免了频繁的内存分配。

综上所述，对于大量的字符串拼接操作，使用 `+` 运算符的效率较低，而 `fmt.Sprintf`、`strings.Join` 和 `bytes.Buffer` 的效率相对较高。在性能要求较高的场景中，建议选择后三种方式来进行字符串拼接。

### （4）go中的数组和切片的区别是什么

在 Go 中，数组（Array）和切片（Slice）是两种不同的数据类型，它们具有一些区别。

1. 大小固定 vs 大小可变：数组的大小在创建时就确定，并且无法改变，而切片的大小是动态的，可以根据需要进行扩展或缩减。
2. 内存布局：数组是一段连续的内存空间，其中每个元素具有相同的类型和固定的大小。切片则是对底层数组的一个引用，包含了指向底层数组的指针、长度和容量。
3. 传递方式：数组作为函数参数传递时，会进行值拷贝，即传递的是整个数组的副本。而切片作为函数参数传递时，传递的是底层数组的引用，对切片的修改会影响到底层数组。
4. 动态性和灵活性：切片具有更大的灵活性和动态性，可以根据需要进行动态增长和缩减。切片支持切片操作、追加元素、删除元素等操作，使得处理动态数据集合更加方便。
5. 声明方式：数组的长度必须在声明时指定，例如 `[5]int` 表示长度为 5 的整型数组。而切片可以使用 `[]Type` 的方式声明，例如 `[]int` 表示一个整型切片。

在实际应用中，切片更加常用，因为它提供了更多的灵活性和便利性。切片可以根据需求动态调整大小，并且在函数传递和返回时更加高效和方便。数组在特定场景下仍然有其用处，例如固定大小的数据集合或对内存布局有特殊要求的情况下。

### （5）go中的new和make的区别是什么

在 Go 中，`new` 和 `make` 是两个用于创建数据结构的内建函数，它们有不同的使用场景和作用。

1. `new` 函数：`new` 用于创建某种类型的指针，并返回该指针的零值。它接受一个参数，即要创建的类型，返回一个指向该类型的零值的指针。使用 `new` 函数适用于创建值类型（如结构体、整型、浮点型等）的指针。

   示例：`ptr := new(int)`，在这个示例中，`new(int)` 创建了一个 `int` 类型的指针，并将其初始化为 `int` 类型的零值。

2. `make` 函数：`make` 用于创建切片、映射和通道等引用类型的数据结构，并进行初始化。它接受两个或三个参数，第一个参数是要创建的类型，后面的参数根据类型的不同而有所不同。

   - 对于切片（`slice`）：`make([]T, length, capacity)`，其中 `T` 是切片的元素类型，`length` 是切片的长度，`capacity` 是切片的容量。
   - 对于映射（`map`）：`make(map[K]V, capacity)`，其中 `K` 是键的类型，`V` 是值的类型，`capacity` 是映射的初始容量。
   - 对于通道（`channel`）：`make(chan T, capacity)`，其中 `T` 是通道中元素的类型，`capacity` 是通道的缓冲区容量。

   `make` 函数适用于创建引用类型的数据结构，并且会进行初始化，返回一个已经初始化的引用类型的值。

总结：`new` 用于创建值类型的指针，并返回指针的零值；`make` 用于创建引用类型的数据结构，并进行初始化。根据不同的需求和数据类型，选择适当的函数来创建相应的数据结构。

#### 5.1）如果使用new初始化map、slice、channel会怎么样

在 Go 中，使用 `new` 初始化 `map`、`slice` 和 `channel` 会得到它们的零值，并返回一个指向该类型的指针。

1. 初始化 `map`： 使用 `new` 初始化 `map` 会得到一个指向 `map` 类型的指针，其值为 `nil`，表示一个空的 `map`。需要注意的是，尽管 `map` 被初始化为 `nil`，但是仍然可以进行操作，例如对其进行赋值、添加键值对等操作。然而，在使用 `map` 前，需要确保对其进行初始化，通常使用 `make` 来创建非空的 `map`。

   示例：

   ```
   goCopy codem := new(map[string]int)
   fmt.Println(m) // 输出: &map[]
   ```

2. 初始化 `slice`： 使用 `new` 初始化 `slice` 会得到一个指向 `slice` 类型的指针，其值为 `nil`，表示一个空的 `slice`。与 `map` 类似，尽管 `slice` 被初始化为 `nil`，但是仍然可以进行操作，例如对其进行赋值、追加元素等操作。在使用 `slice` 前，同样需要对其进行初始化，通常使用 `make` 来创建非空的 `slice`。

   示例：

   ```
   goCopy codes := new([]int)
   fmt.Println(s) // 输出: &[]
   ```

3. 初始化 `channel`： 使用 `new` 初始化 `channel` 会得到一个指向 `channel` 类型的指针，其值为 `nil`，表示一个未初始化的 `channel`。这样的 `channel` 无法直接使用，需要使用 `make` 创建一个具体的通道并分配相应的缓冲区大小后才能使用。

   示例：

   ```
   goCopy codech := new(chan int)
   fmt.Println(ch) // 输出: <nil>
   ```

综上所述，虽然可以使用 `new` 初始化 `map`、`slice` 和 `channel`，但得到的是一个指向对应类型的指针，其值为 `nil`。如果需要创建一个非空的 `map`、`slice` 或 `channel`，通常建议使用 `make` 函数进行初始化，并分配相应的内存和缓冲区。

### （6）grpc是什么

gRPC（gRPC Remote Procedure Call）是一种高性能、通用的开源远程过程调用（RPC）框架，由Google开发并开源。它基于HTTP/2协议，并使用Protocol Buffers作为默认的序列化机制。

gRPC旨在简化分布式应用程序之间的通信，使得客户端和服务器可以像调用本地方法一样调用远程服务。它支持多种编程语言（如Go、Java、C++、Python等），允许开发者使用定义服务接口的方式来描述请求和响应的数据结构，然后自动生成相应的客户端和服务器端代码。

gRPC具有以下特点和优势：

1. 高性能：gRPC基于HTTP/2协议，采用了二进制传输和多路复用等技术，具有较低的延迟和高吞吐量，适用于高性能的分布式系统。
2. 跨平台和多语言支持：gRPC支持多种编程语言，使得不同语言的应用程序可以相互通信，方便实现跨平台和跨语言的分布式应用。
3. 自动生成代码：通过使用Protocol Buffers定义服务接口和消息类型，gRPC可以自动生成客户端和服务器端的代码，减少了手动编写繁琐的网络通信代码的工作量。
4. 支持多种通信模式：gRPC支持四种常见的通信模式，包括单一请求-单一响应、单一请求-流式响应、流式请求-单一响应以及流式请求-流式响应，使得开发者可以根据实际需求选择适合的通信方式。
5. 强大的拦截器和中间件支持：gRPC提供了丰富的拦截器和中间件支持，可以在请求和响应的处理过程中进行拦截和处理，方便实现认证、授权、日志记录等功能。
6. 可扩展性：通过使用Protocol Buffers，可以定义复杂的数据结构和服务接口，并支持版本化、演进和后向兼容等扩展性方面的需求。

总之，gRPC是一种功能强大的远程过程调用框架，提供了高性能、跨平台和多语言支持，适用于构建分布式系统中的服务间通信。它简化了开发者的工作，提供了简洁、高效和可扩展的解决方案。

### （7）进程被kill，如何保证所有goroutine顺利退出

当进程被kill时，操作系统会终止进程并清理相关资源，包括所有的goroutine。在这种情况下，无法直接保证所有的goroutine能够顺利退出。不过，可以采取一些措施来优雅地退出goroutine并确保资源的正确释放。

1. 通过信号通知：在进程被kill之前，可以通过操作系统的信号机制向进程发送特定的信号（如SIGINT、SIGTERM），在信号处理函数中做一些清理工作并通知goroutine退出。可以使用Go的`os/signal`包来捕获信号并执行相应的处理逻辑。
2. 使用`context`来控制goroutine：在启动goroutine时，传递一个`context.Context`对象给它，可以通过该对象来控制goroutine的生命周期。在进程即将退出时，可以调用`cancel`函数取消所有相关的`context`，从而通知goroutine退出。
3. 使用`sync.WaitGroup`等待goroutine退出：在主goroutine中，使用`sync.WaitGroup`来等待所有的goroutine完成。在goroutine退出时，通过`Done`方法减少`WaitGroup`的计数器，主goroutine可以通过`Wait`方法等待所有的goroutine退出。
4. 使用通道（Channel）来通知退出：在goroutine中使用一个退出通道来接收退出信号，当需要退出时，向通道发送退出信号。其他的goroutine可以监听该通道，收到退出信号后进行清理工作并退出。

需要注意的是，对于一些无法中断或阻塞的操作，比如网络请求或文件IO等，可能需要通过设置超时或取消的机制来确保它们能够及时退出。

综上所述，保证所有goroutine顺利退出的方法可以结合使用信号通知、使用`context`控制、使用`sync.WaitGroup`等待、使用退出通道等多种手段。具体的实现方式取决于应用的需求和场景。

#### 7.1）通过信号通知

```go
package main

import (
	"context"
	"fmt"
	"os"
	"os/signal"
	"sync"
	"syscall"
	"time"
)

func main() {
	ctx, cancel := context.WithCancel(context.Background())
	wg := sync.WaitGroup{}

	// 启动多个goroutine
	for i := 0; i < 5; i++ {
		wg.Add(1)
		go func(id int) {
			defer wg.Done()
			for {
				select {
				case <-ctx.Done():
					fmt.Printf("Goroutine %d: Exiting\n", id)
					return
				default:
					fmt.Printf("Goroutine %d: Running\n", id)
					time.Sleep(time.Second)
				}
			}
		}(i)
	}

	// 捕获终止信号
	signalCh := make(chan os.Signal, 1)
	signal.Notify(signalCh, syscall.SIGINT, syscall.SIGTERM)

	// 等待终止信号
	<-signalCh
	fmt.Println("Received termination signal. Cleaning up...")

	// 发送取消信号，通知所有goroutine退出
	cancel()

	// 等待所有goroutine退出
	wg.Wait()

	fmt.Println("All goroutines have exited. Exiting main...")
}
```

#### 7.2）使用`context`控制：

```go
goCopy codepackage main

import (
	"context"
	"fmt"
	"sync"
	"time"
)

func main() {
	ctx, cancel := context.WithCancel(context.Background())
	wg := sync.WaitGroup{}

	// 启动多个goroutine
	for i := 0; i < 5; i++ {
		wg.Add(1)
		go func(id int) {
			defer wg.Done()
			for {
				select {
				case <-ctx.Done():
					fmt.Printf("Goroutine %d: Exiting\n", id)
					return
				default:
					fmt.Printf("Goroutine %d: Running\n", id)
					time.Sleep(time.Second)
				}
			}
		}(i)
	}

	// 等待一段时间后，取消所有goroutine
	time.Sleep(5 * time.Second)
	cancel()

	// 等待所有goroutine退出
	wg.Wait()

	fmt.Println("All goroutines have exited. Exiting main...")
}
```

### （8）K8S中的服务发现是怎么实现的

在Kubernetes（K8s）中，服务发现是通过以下方式实现的：

1. Service资源：Kubernetes使用Service资源来定义逻辑服务。Service是一组提供相同功能的Pod的抽象。Service资源会分配一个虚拟的Cluster IP，作为服务的入口地址。其他的Pod或Service可以使用该虚拟IP和端口来访问服务。
2. DNS解析：Kubernetes集群中的每个Pod都自动配置了一个DNS解析器。通过该解析器，Pod可以使用域名来查找和访问其他服务。Kubernetes使用内部DNS服务（kube-dns或CoreDNS）来为每个Service创建一个DNS记录，使得其他Pod或Service可以使用服务名称进行解析。
3. 环境变量注入：Kubernetes会自动将Service的相关信息注入到每个Pod的环境变量中。这包括Service的名称、虚拟IP和端口等信息。通过环境变量，Pod可以获取到需要访问的Service的信息，从而实现服务发现。
4. Kubernetes DNS：Kubernetes DNS服务是一个集群内部的DNS服务器，负责解析Kubernetes集群中的域名。当Pod需要访问其他Pod或Service时，它可以直接使用服务名称进行DNS解析，而无需关心具体的IP地址和端口。
5. kube-proxy：kube-proxy是Kubernetes的一个组件，负责为每个Service创建代理。这个代理会监听Service的虚拟IP和端口，并根据负载均衡策略将请求转发到后端的Pod。kube-proxy使用IPVS、Iptables或者Userspace模式来实现负载均衡。

通过以上机制，Kubernetes实现了服务发现功能。应用程序可以使用Service的名称进行通信，而不需要直接暴露具体的Pod的IP地址和端口。Kubernetes会负责将请求路由到适当的Pod，实现了服务间的透明通信和负载均衡。这种方式使得应用程序更具弹性和可伸缩性，可以轻松地扩展和管理服务实例。

### （9）goroutine什么情况会发生内存泄漏？如何避免。

在Go中内存泄露分为暂时性内存泄露和永久性内存泄露。

**暂时性内存泄露**

- 获取长字符串中的一段导致长字符串未释放
- 获取长slice中的一段导致长slice未释放
- 在长slice新建slice导致泄漏

string相比切片少了一个容量的cap字段，可以把string当成一个只读的切片类型。获取长string或者切片中的一段内容，由于新生成的对象和老的string或者切片共用一个内存空间，会导致老的string和切片资源暂时得不到释放，造成短暂的内存泄漏

**永久性内存泄露**

- goroutine永久阻塞而导致泄漏
- time.Ticker未关闭导致泄漏
- 不正确使用Finalizer（Go版本的析构函数）导致泄漏

> 在Go中，goroutine会在其执行完成后自动被垃圾回收，因此不会出现传统意义上的内存泄漏。然而，有一些情况可能导致goroutine无法正常退出，从而导致资源泄漏或无法释放的问题。以下是一些可能导致goroutine泄漏的情况以及如何避免它们：
>
> 1. 未关闭的通道（channel）：如果一个goroutine向一个通道发送数据，而没有其他goroutine接收该数据，该goroutine将会被阻塞并无法退出。为了避免这种情况，确保在不需要继续发送数据时，正确地关闭通道。
> 2. 循环引用：如果一个goroutine持有对其他对象的引用，而这些对象又持有对该goroutine的引用，就会形成循环引用。这可能导致垃圾回收器无法回收这些对象，从而导致内存泄漏。为了避免循环引用，确保在不再需要时，及时解除对对象的引用。
> 3. 资源未释放：如果goroutine在完成任务后没有正确释放所使用的资源（如打开的文件、数据库连接等），就可能导致资源泄漏。确保在不再需要资源时，及时进行关闭、释放或销毁。
> 4. 无限循环或阻塞：如果goroutine进入无限循环或阻塞状态，它将无法正常退出并释放相关资源。确保goroutine的执行逻辑能够合理终止或定时退出，避免陷入无限循环或永久阻塞的情况。
> 5. 忘记等待goroutine完成：如果主goroutine在退出前没有等待其他goroutine完成，可能会导致这些goroutine无法完成任务或资源清理。使用`sync.WaitGroup`等机制来等待所有需要等待的goroutine完成。
>
> 总之，避免goroutine的内存泄漏主要需要确保通道的正确关闭、避免循环引用、及时释放资源、避免无限循环或阻塞，以及适当等待其他goroutine完成。通过合理的设计和资源管理，可以避免大多数goroutine泄漏和资源泄漏问题。

### （10）go和Java有哪些区别？

Go和Java是两种不同的编程语言，它们在许多方面有着显著的区别。以下是Go和Java之间的一些主要区别：

1. 语言设计和语法：Go的语法相对较简洁和紧凑，强调代码的可读性和易于编写。它具有C风格的语法，使用显式类型声明和关键字来定义变量和函数。Java语法更为冗长，使用较多的关键字和语法结构，支持面向对象编程范式。
2. 并发和并行：Go在语言层面提供了轻量级的协程（goroutine）和通道（channel）机制，用于实现并发编程。它内置了高效的并发原语，并提供了简洁的方式来编写并发代码。相比之下，Java使用线程和锁来实现并发，需要开发人员手动管理线程和同步机制。
3. 内存管理：Go使用垃圾回收器（Garbage Collector）自动管理内存，开发人员不需要手动分配和释放内存。Java也有垃圾回收器，但它的内存管理机制更为复杂，包括堆内存、栈内存和手动的内存释放（如`finalize()`方法）。
4. 依赖管理：Go使用Go Modules进行依赖管理，允许开发人员声明和管理项目的依赖关系，实现版本控制和构建复现性。Java使用Maven或Gradle等构建工具来管理依赖项，通过配置文件（如pom.xml）指定项目的依赖关系。
5. 性能：由于Go的语言设计和运行时环境的特性，它在许多情况下表现出较高的性能和低延迟。相比之下，Java具有更广泛的生态系统和优化工具，可以在不同的场景中实现高性能。
6. 领域应用：Go主要用于构建网络服务和分布式系统，如Web服务器、微服务和容器编排。它在处理并发、高性能和可伸缩性方面表现出色。Java则广泛应用于企业级应用程序开发，包括桌面应用程序、后端服务器和大规模企业应用。

### #Reference

1. https://zhuanlan.zhihu.com/p/471490292
2. [「Chris Richardson 微服务系列」服务发现的可行方案以及实践案例](https://blog.daocloud.io/3289.html)