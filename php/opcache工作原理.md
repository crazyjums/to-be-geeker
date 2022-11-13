# Opcache工作原理

时间：2022-09

## 1 php-fpm+nginx工作机制

一个PHP请求从浏览器发送到nginx，nginx再把请求转发给php-fpm处理，最后把处理结果返回给浏览器，一共需要经历以下6个步骤：

**（1）发起请求**

1. 客户端发起服务请求

**（2）启动服务**

1. **启动nginx**，首先会载入 ngx_http_fastcgi_module 模块，初始化FastCGI执行环境，实现FastCGI协议请求代理
2. **启动php-fpm**，php-fpm支持**tcp socket**和**unix socket**通信
   1. master进程：负责监听端口，分配任务给worker进程，管理worker进程
   2. worker进程：这个worker进程就是一个php-cgi程序，负责解释、编译、执行php脚本

**（3）请求 -> nginx**

1. nginx接收请求，并基于location配置，选择一个代理handler，
2. 此时的handler是php-fpm

**（4）nginx -> php-fpm.master**

1. nginx把请求翻译成fast-cgi协议请求
2. 通过**tcp socket**或**unix socket**协议发送给php-fpm的master进程

**（5）php-fpm.master -> php-fpm.worker**

1. master进程接收到请求，并分配给某一个空闲的worker进程，如果此时没有空闲的worker进程，此时则根据php-fpm配置决定是返回502错误，还是fork()新的子进程来处理
2. 如果有空闲的worker进程，但是处理超时，则返回504错误
3. 处理成功，则返回结果

**（6）php-fpm.worker -> php-fpm.master**

1. worker进程返回处理结果，此时根据php-fpm配置决定当前worker是否关闭，等待下一个请求
2. php-fpm.master进程通过socket通信将处理结果返回给nginx

**（7）php-fpm.master -> nginx**

1. nginx handler处理结果返回给客户端

## 2 php解释脚本的执行机制

```php
<?php
if (!empty($_POST)) {
    echo "Response Body POST: ". json_encode($_POST). "\n";
}
if (!empty($_GET)) {
    echo "Response Body GET: ". json_encode($_GET). "\n";
}
```

执行过程：

1. PHP初始化执行环节，**启动zend引擎**，加载注册的扩展模块
2. 初始化之后，读取PHP脚本文件，然后zend引擎对脚本文件进行词法分析（lex），语法分析（bison），**生成语法树**
3. zend引擎编译语法树，生成[opcode](https://blog.csdn.net/sqzxwq/article/details/47786345)
4. zend引擎执行opcode，返回执行结果

- 在PHP的命令行模式下，**每次执行一次PHP脚本，都会执行上面的4个步骤**

- 在php-fpm模式下，**步骤1**在php-fpm启动时执行一次，之后的请求中不会再执行，但是，**步骤2~步骤4每次请求都得执行**
  - 其实**步骤2~步骤3**生成的语法树和opcode对于同一个脚本而言，都是一样的，那么如果每次请求过来都要重新生成一边，就会浪费资源和时间，
  - **opcache就是为了解决在重复请求时重复执行步骤3所造成的资源浪费的问题**

## 3 opcache

## 3.1 简介

OPCache 是Zend官方出品的，开放自由的 opcode 缓存扩展，还具有代码优化功能，省去了每次加载和解析 PHP 脚本的开销。

PHP 5.5.0 及后续版本中已经绑定了 OPcache 扩展。

缓存两类内容:

- OPCode
- Interned String，如注释、变量名等
  - PHP脚本涉及到的函数
  - PHP脚本中定义的Class
  - PHP脚本文件路径
  - PHP脚本OPArray
  - PHP脚本自身结构/内容

## 3.2 工作原理

### 3.2.1 OPCache缓存的机制：

**将编译好的操作码放入共享内存，提供给其他进程访问**。

1. opcache将PHP编译产生的字节码以及数据缓存到共享内存中, 在每次请求，从缓存中直接读取编译后的opcode，进行执行。
2. 在php-fpm模式下，interned string仅能缓存在单个worker进程中，worker之间不能共享，如果多个worker有相同的interned string，那么就会缓存多次，造成资源浪费
3. 如果将interned string缓存到opcache，则worker之间可以共享这个数据，减少资源的消耗

### 3.2.2 互斥锁：

1. 同一时刻只能有一个进程在执行写操作，同一个时刻，可以有多个进程执行读操作

> 注意：新代码、大流量场景，进程排队执行缓存opcode操作；重复写入，导致资源浪费。

### 3.2.3 缓存更新策略：

1. OPCache的更新策略非常简单，过期数据被设置为Wasted，达到设定值，清空缓存，重建缓存

   ```
   opcache.max_wasted_percentage=5 //wasted数据达到内存的5%时，清空缓存
   ```

> 注意：每次发布新代码，都会重新建立新的opcache
>
> 1. 尽量不要在高峰时刻发布新代码，因为**在高流量的场景下，重建缓存是一件非常耗费资源的事儿。**

### 3.2.4 opcache配置信息：

```
zend_extension=opcache.so

; Zend Optimizer + 的开关, 关闭时代码不再优化.
opcache.enable=1

; Determines if Zend OPCache is enabled for the CLI version of PHP
opcache.enable_cli=1


; Zend Optimizer + 共享内存的大小, 总共能够存储多少预编译的 PHP 代码(单位:MB)
; 推荐 128
opcache.memory_consumption=64

; Zend Optimizer + 暂存池中字符串的占内存总量.(单位:MB)
; 推荐 8
opcache.interned_strings_buffer=4


; 最大缓存的文件数目 200  到 100000 之间
; 推荐 4000
opcache.max_accelerated_files=2000

; 内存“浪费”达到此值对应的百分比,就会发起一个重启调度.
opcache.max_wasted_percentage=5

; 开启这条指令, Zend Optimizer + 会自动将当前工作目录的名字追加到脚本键上,
; 以此消除同名文件间的键值命名冲突.关闭这条指令会提升性能,
; 但是会对已存在的应用造成破坏.
opcache.use_cwd=0


; 开启文件时间戳验证 
opcache.validate_timestamps=1


; 2s检查一次文件更新 注意:0是一直检查不是关闭
; 推荐 60
opcache.revalidate_freq=2

; 允许或禁止在 include_path 中进行文件搜索的优化
;opcache.revalidate_path=0


; 是否保存文件/函数的注释   如果apigen、Doctrine、 ZF2、 PHPUnit需要文件注释
; 推荐 0
opcache.save_comments=1

; 是否加载文件/函数的注释
;opcache.load_comments=1


; 打开快速关闭, 打开这个在PHP Request Shutdown的时候会收内存的速度会提高
; 推荐 1
opcache.fast_shutdown=1

;允许覆盖文件存在（file_exists等）的优化特性。
;opcache.enable_file_override=0


; 定义启动多少个优化过程
;opcache.optimization_level=0xffffffff


; 启用此Hack可以暂时性的解决”can’t redeclare class”错误.
;opcache.inherited_hack=1

; 启用此Hack可以暂时性的解决”can’t redeclare class”错误.
;opcache.dups_fix=0

; 设置不缓存的黑名单
; 不缓存指定目录下cache_开头的PHP文件. /png/www/example.com/public_html/cache/cache_ 
;opcache.blacklist_filename=


; 通过文件大小屏除大文件的缓存.默认情况下所有的文件都会被缓存.
;opcache.max_file_size=0

; 每 N 次请求检查一次缓存校验.默认值0表示检查被禁用了.
; 由于计算校验值有损性能,这个指令应当紧紧在开发调试的时候开启.
;opcache.consistency_checks=0

; 从缓存不被访问后,等待多久后(单位为秒)调度重启
;opcache.force_restart_timeout=180

; 错误日志文件名.留空表示使用标准错误输出(stderr).
;opcache.error_log=


; 将错误信息写入到服务器(Apache等)日志
;opcache.log_verbosity_level=1

; 内存共享的首选后台.留空则是让系统选择.
;opcache.preferred_memory_model=

; 防止共享内存在脚本执行期间被意外写入, 仅用于内部调试.
;opcache.protect_memory=0
```

# Reference

1. [OPCode详解及汇编与反汇编原理](https://blog.csdn.net/sqzxwq/article/details/47786345)
2. [https://zhuanlan.zhihu.com/p/75869838](https://zhuanlan.zhihu.com/p/75869838)