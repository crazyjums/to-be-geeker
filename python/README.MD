# python教程

## 一、协程

## 二、多线程

## 三、asyncio | 异步编程

## 四、魔法变量

> **魔法函数和魔法变量的区别？**
>
> 在Python中，魔法函数（Magic Methods）和魔法变量（Magic Variables）是两个不同的概念。
>
> 魔法函数是指在类中以双下划线（`__`）开头和结尾的特殊函数。它们被称为魔法函数是因为它们在特定的情况下会被自动调用，而不需要显式地调用。这些魔法函数用于在类中实现特定的行为，例如初始化对象、比较对象、重载运算符等。通过定义这些魔法函数，可以自定义类的行为和操作。
>
> 例如，`__init__()`是一个常见的魔法函数，用于在创建对象时进行初始化：
>
> ```python
> class MyClass:
>     def __init__(self, arg):
>         self.arg = arg
> 
> obj = MyClass("hello")
> ```
>
> 在这个例子中，`__init__()`函数在创建`MyClass`对象时自动被调用，并将参数`"hello"`传递给它。
>
> 魔法变量是指在Python中具有特殊含义或用途的预定义变量。它们以双下划线（`__`）开头和结尾。这些变量用于表示特定的状态或属性。一些常见的魔法变量包括`__name__`、`__doc__`、`__class__`等。
>
> 例如，`__name__`是一个魔法变量，用于表示当前模块的名称：
>
> ```python
> # module.py
> 
> print(__name__)  # 打印当前模块的名称
> ```
>
> 当直接运行`module.py`时，`__name__`的值是`__main__`；当该模块被导入时，`__name__`的值是模块的名称。
>
> 总而言之，魔法函数用于定义和自定义类的行为，而魔法变量用于表示特定的状态或属性。它们都以双下划线开头和结尾，但具有不同的用途和功能。

### （1）`__all__`

在Python中，`__all__`并不是一个魔法函数，而是一个魔法变量，用于定义模块的公开接口。它可以在模块中被定义为一个包含字符串的列表，用于限制通过`from module import *`导入时，可以被导入的对象。

`__all__`的作用是控制模块的命名空间，帮助开发者明确指定哪些对象是可以被其他模块直接导入的。当其他模块使用`from module import *`时，只有在`__all__`列表中列出的对象才会被导入，其他对象将不会被导入。

以下是`__all__`的用法示例：

```python
# module.py

def public_function():
    pass

def _private_function():
    pass

# __all__列表中只列出了public_function，_private_function不会被导入
__all__ = ['public_function']
```

在另一个模块中，当使用`from module import *`导入时，只有`public_function`会被导入：

```python
# main.py

from module import *

public_function()  # 可以直接使用
_private_function()  # 会引发NameError，因为它没有被导入
```

使用`__all__`可以提供更好的模块封装和代码可读性。通过明确指定导出的接口，可以避免不必要的命名冲突和意外的导入。需要注意的是，`__all__`只在使用`from module import *`导入时起作用，当使用`import module`导入时，它并不会限制可访问的对象。

### （2）`__doc__`

`__doc__`是一个固定的魔法变量，用于表示当前对象（模块、类、函数等）的文档字符串（docstring）。

文档字符串是位于对象定义之后的字符串，用于提供对该对象的说明和文档。通常，文档字符串用来描述对象的功能、使用方法、参数说明、返回值等相关信息。文档字符串是编写高质量代码和提供良好文档的重要组成部分。

使用`__doc__`可以访问对象的文档字符串。它可以在运行时被访问，以便将对象的文档信息提供给用户或其他部分的代码。

以下是一个示例，展示了如何使用`__doc__`来访问函数的文档字符串：

```python
def add(a, b):
    """
    This function takes two arguments and returns their sum.
    """
    return a + b

print(add.__doc__)  # 打印函数add的文档字符串
```

在这个例子中，`add`函数的文档字符串包含了关于函数的说明和功能。通过访问`add.__doc__`，可以获得该文档字符串的内容。

`__doc__`的作用是帮助开发人员编写和提供清晰、准确的文档。它可以被IDE、文档生成工具、帮助系统等用来展示对象的说明和用法。同时，它也可以被其他代码使用，例如用于生成自动化测试用例、进行文档验证等。

需要注意的是，文档字符串通常应该遵循一定的文档注释规范，如使用三重引号包裹、提供参数和返回值的说明等。这样可以使文档字符串更易读、易理解，并提供给用户更有用的信息。

### （3）`__class__`

`__class__`是一个固定的魔法变量，用于表示当前对象所属的类。

在Python中，一切皆对象，每个对象都属于某个类。通过`__class__`可以访问到对象所属的类，从而可以获取类的属性和调用类的方法。

以下是一个示例，展示了如何使用`__class__`来访问对象所属的类：

```python
class MyClass:
    pass

obj = MyClass()
print(obj.__class__)  # 打印obj所属的类
```

在这个例子中，`__class__`用于获取`obj`对象所属的类。输出结果将显示类的名称和其他相关信息。

`__class__`的作用是提供了一种动态获取对象所属类的方式。这在一些情况下很有用，例如在对象上调用类的方法、通过对象获取类的属性等。

需要注意的是，`__class__`是一个只读属性，不能通过赋值操作来修改对象的类。如果需要修改对象的类，应该使用`type()`函数来创建一个新的类对象，并将对象重新设置为新类的实例。

```python
class NewClass:
    pass

obj.__class__ = NewClass  # 错误的写法，不能修改对象的类

obj = NewClass()  # 正确的写法，创建一个新的类对象，并将对象重新设置为新类的实例
```

总结起来，`__class__`提供了一种访问对象所属类的方式，可以用于获取类的属性和调用类的方法。它帮助我们在运行时动态操作和处理对象的类信息。

## 五、魔法函数

### （1）`__call__()`

`__call__()`是一个特殊的魔法函数，在Python中用于使对象可以像函数一样被调用。当调用一个对象时，Python会检查该对象是否定义了`__call__()`方法，如果定义了，则会调用该方法。

`__call__()`的作用是为对象提供可调用的行为，使其可以像函数一样被使用。它可以接受参数并执行一些操作，类似于函数调用时的行为。

以下是一个示例，展示了如何使用`__call__()`方法：

```python
class Adder:
    def __init__(self, num):
        self.num = num

    def __call__(self, x):
        return self.num + x

adder = Adder(5)
result = adder(10)
print(result)  # 输出15
```

在这个例子中，`Adder`类定义了`__call__()`方法。当`adder`对象被调用时，即写作`adder(10)`，实际上会调用`adder.__call__(10)`。`__call__()`方法接受参数`x`，将`self.num`与`x`相加并返回结果。

通过定义`__call__()`方法，我们可以使对象具备可调用的行为，使其在使用上更像一个函数。这在某些情况下很有用，例如实现可调用的类、函数装饰器等。

需要注意的是，`__call__()`方法是可选的，不是所有的对象都需要定义它。只有当你希望对象能够像函数一样被调用时，才需要定义`__call__()`方法。

总结起来，`__call__()`是一个特殊的魔法函数，用于使对象可以像函数一样被调用。通过定义`__call__()`方法，我们可以为对象提供可调用的行为，使其在使用上更灵活和方便。

> **如果一个对象没有定义`__call__()`方法，会怎样?**
>
> 如果一个对象没有定义`__call__()`方法，当尝试调用该对象时，Python会引发一个`TypeError`异常。这是因为对象没有可调用的行为，无法像函数一样被调用。
>
> 以下是一个示例，展示了当对象没有定义`__call__()`方法时的行为：
>
> ```python
> class NotCallable:
>     pass
> 
> obj = NotCallable()
> obj()  # 尝试调用对象，将引发TypeError异常
> ```
>
> 在这个例子中，`NotCallable`类没有定义`__call__()`方法。当我们尝试调用`obj()`时，Python会抛出`TypeError: 'NotCallable' object is not callable`异常。
>
> 因此，如果一个对象没有定义`__call__()`方法，它就不能被调用。如果你希望对象能够像函数一样被调用，需要在对象中定义`__call__()`方法，使其具备可调用的行为。

## 六、装饰器

> 装饰器可以有以下几种实现方式：
>
> 1. 函数装饰器：使用函数来实现装饰器。函数装饰器是最常见的装饰器实现方式。它是一个高阶函数，接收一个函数作为参数，并返回一个新的函数作为装饰后的函数。通过在被装饰的函数周围添加额外的逻辑，函数装饰器可以修改或扩展函数的行为。
>
> 2. 类装饰器：使用类来实现装饰器。类装饰器是通过创建一个装饰器类，并在类中定义`__init__()`和`__call__()`方法来实现的。类装饰器可以更灵活地处理装饰器参数和状态，并可以在装饰的函数执行前后添加额外的逻辑。
>
> 3. 带参数的装饰器：装饰器可以接受参数。这样的装饰器允许在装饰器本身上设置一些参数，以便根据需要自定义装饰器的行为。通过在装饰器外添加一层函数或类，可以实现带参数的装饰器。
>
> 4. 嵌套装饰器：可以将多个装饰器应用于同一个函数。这称为嵌套装饰器。嵌套装饰器的执行顺序是从内到外，即最内层的装饰器先执行，然后依次向外执行。这样可以在函数上应用多个装饰器，每个装饰器可以为函数添加不同的功能。
>
> 5. 类方法装饰器：除了装饰函数，装饰器还可以用于装饰类方法。类方法装饰器可以在类方法执行前后添加额外的逻辑，对类方法的行为进行修改或扩展。
>
> 这些是常见的装饰器实现方式，每种方式都有其适用的场景和特点。选择哪种实现方式取决于具体需求和个人偏好。无论选择哪种方式，装饰器都是一种有用的工具，可以用于修改、扩展或包装函数和类的行为。

### （1）函数装饰器

当使用函数装饰器时，我们需要定义一个装饰器函数，它接收一个函数作为参数，并返回一个新的函数作为装饰后的函数。装饰器函数通常使用`@`语法来应用于目标函数。

下面是一个简单的函数装饰器的写法：

```python
def decorator_func(func):
    def wrapper(*args, **kwargs):
        # 在函数执行前添加额外的逻辑
        print("Before function execution")

        # 调用被装饰的函数
        result = func(*args, **kwargs)

        # 在函数执行后添加额外的逻辑
        print("After function execution")

        # 返回结果
        return result

    # 返回装饰后的函数
    return wrapper
```

在这个例子中，`decorator_func`是一个装饰器函数，它接收一个函数作为参数`func`。在装饰器函数内部定义了一个包装函数`wrapper`，它包含了在函数执行前后添加的额外逻辑。最后，装饰器函数返回包装函数`wrapper`，取代了原始函数。

要使用这个装饰器，我们可以将其应用于目标函数，如下所示：

```python
@decorator_func
def my_function():
    print("Inside the function")

my_function()
```

在这个例子中，`decorator_func`装饰器被应用于`my_function`函数。当调用`my_function()`时，实际上是调用了装饰器返回的包装函数`wrapper`，从而在函数执行前后添加了额外的逻辑。

函数装饰器常见的使用场景包括：

1. 记录日志：可以使用装饰器来记录函数的调用信息，例如函数名称、参数值和返回值。

   ```python
   def logger(func):
       def wrapper(*args, **kwargs):
           print(f"Calling function: {func.__name__}")
           print(f"Arguments: {args}, {kwargs}")
           result = func(*args, **kwargs)
           print(f"Returned value: {result}")
           return result
   
       return wrapper
   
   @logger
   def add(a, b):
       return a + b
   
   add(2, 3)
   ```

   在这个示例中，装饰器`logger`用于记录函数的调用信息。当调用`add(2, 3)`时，装饰器将打印出函数的名称、参数和返回值。

2. 计时统计：装饰器可以用于测量函数执行的时间，以便对函数的性能进行分析和优化。

   ```python
   import time
   
   def timer(func):
       def wrapper(*args, **kwargs):
           start_time = time.time()
           result = func(*args, **kwargs)
           end_time = time.time()
           print(f"Execution time: {end_time - start_time} seconds")
           return result
   
       return wrapper
   
   @timer
   def fibonacci(n):
       if n <= 1:
           return n
       else:
           return fibonacci(n-1) + fibonacci(n-2)
   
   fibonacci(10)
   ```

   在这个示例中，装饰器`timer`用于测量`fibonacci`函数的执行时间。当调用`fibonacci(10)`时，装饰器将打印出函数的执行时间。

3. 缓存结果：装饰器可以用于缓存函数的结果，以避免重复执行相同的计算操作。

   ```python
   def cache_result(func):
       cache = {}
   
       def wrapper(*args, **kwargs):
           key = (args, tuple(sorted(kwargs.items())))
           if key not in cache:
               cache[key] = func(*args, **kwargs)
           return cache[key]
   
       return wrapper
   
   @cache_result
   def factorial(n):
       if n <= 1:
           return 1
       else:
           return n * factorial(n-1)
   
   factorial(5)
   ```

   在这个示例中，装饰器`cache_result`用于缓存`factorial`函数的结果。当调用`factorial(5)`时，装饰器将检查缓存中是否已经计算过该值，如果有则直接返回缓存中的结果，否则进行计算并存储在缓存中。

4. 权限验证：装饰器可以用于验证用户权限，以确保只有具备特定权限的用户才能访问某些函数或方法。

   ```python
   def check_permission(allowed_role):
       def decorator(func):
           def wrapper(*args, **kwargs):
               user_role = get_user_role()  # 假设该函数返回用户角色
               if user_role == allowed_role:
                   return func(*args, **kwargs)
               else:
                   print("Unauthorized access")
                   return None
   
           return wrapper
   
       return decorator
   
   @check_permission("admin")
   def delete_user(user_id):
       # 删除用户的逻辑
       print(f"Deleting user with ID: {user_id}")
   
   delete_user(123)
   ```

   在这个示例中，装饰器`check_permission`用于验证用户的权限。当调用`delete_user(123)`时，装饰器将检查用户的角色是否为"admin"，如果是则执行删除用户的逻辑，否则打印未授权访问的提示。

5. 异常处理：装饰器可以用于捕获函数中的异常，并进行适当的处理或记录。

   ```python
   def handle_exceptions(func):
       def wrapper(*args, **kwargs):
           try:
               return func(*args, **kwargs)
           except Exception as e:
               print(f"Exception occurred: {e}")
               return None
   
       return wrapper
   
   @handle_exceptions
   def divide(a, b):
       return a / b
   
   divide(10, 0)
   ```

   在这个示例中，装饰器`handle_exceptions`用于捕获`divide`函数中的异常，并打印出异常信息。当调用`divide(10, 0)`时，由于除以零会引发异常，装饰器将捕获该异常并打印出异常信息。


这些只是一些常见的使用场景，实际上函数装饰器非常灵活，可以根据具体需求进行定制。通过使用装饰器，我们可以在不修改原始函数代码的情况下，对函数的行为进行修改、扩展或包装。

### （2）类装饰器

当使用类装饰器时，我们需要定义一个装饰器类，它必须实现`__init__()`和`__call__()`方法。`__init__()`方法用于接收装饰器的参数，`__call__()`方法用于定义装饰器的行为。类装饰器通常使用`@`语法来应用于目标函数或类。

下面是一个简单的类装饰器的写法：

```python
class DecoratorClass:
    def __init__(self, func):
        self.func = func

    def __call__(self, *args, **kwargs):
        # 在函数执行前添加额外的逻辑
        print("Before function execution")

        # 调用被装饰的函数
        result = self.func(*args, **kwargs)

        # 在函数执行后添加额外的逻辑
        print("After function execution")

        # 返回结果
        return result
```

在这个例子中，`DecoratorClass`是一个装饰器类，它接收一个函数作为参数`func`。在`__call__()`方法中定义了在函数执行前后添加的额外逻辑。`DecoratorClass`实例可以像函数一样被调用，所以它本质上是一个可调用对象。

要使用这个装饰器，我们可以将其应用于目标函数，如下所示：

```python
@DecoratorClass
def my_function():
    print("Inside the function")

my_function()
```

在这个示例中，`DecoratorClass`装饰器被应用于`my_function`函数。当调用`my_function()`时，实际上是调用了装饰器实例，从而在函数执行前后添加了额外的逻辑。

类装饰器常见的使用场景包括：

1. 记录日志：类装饰器可以用于记录函数的调用信息，例如函数名称、参数值和返回值。

2. 计时统计：类装饰器可以用于测量函数执行的时间，以便对函数的性能进行分析和优化。

3. 缓存结果：类装饰器可以用于缓存函数的结果，以避免重复执行相同的计算操作。

4. 权限验证：类装饰器可以用于验证用户权限，以确保只有具备特定权限的用户才能访问某些函数或方法。

5. 异常处理：类装饰器可以用于捕获函数中的异常，并进行适当的处理或记录。

以下是每个场景的示例代码：

1. 记录日志：

```python
class Logger:
    def __init__(self, func):
        self.func = func

    def __call__(self, *args, **kwargs):
        print(f"Calling function: {self.func.__name__}")
        print(f"Arguments: {args}, {kwargs}")
        result = self.func(*args, **kwargs)
        print(f"Returned value: {result}")
        return result

@Logger
def add(a, b):
    return a + b

add(2, 3)
```

2. 计时统计：

```python
import time

class Timer:
    def __init__(self, func):
        self.func = func

    def __call__(self, *args, **kwargs):
        start_time = time.time()
        result = self.func(*args, **kwargs)
        end_time = time.time()
        print(f"Execution time: {end_time - start_time} seconds")
        return result

@Timer
def fibonacci(n):
    if n <= 1:
        return n
    else:
        return fibonacci(n-1) + fibonacci(n-2)

fibonacci(10)
```

3. 缓存结果：

```python
class CacheResult:
    def __init__(self, func):
        self.func = func
        self.cache = {}

    def __call__(self, *args, **kwargs):
        key = (args, tuple(sorted(kwargs.items())))
        if key not in self.cache:
            self.cache[key] = self.func(*args, **kwargs)
        return self.cache[key]

@CacheResult
def factorial(n):
    if n <= 1:
        return 1
    else:
        return n * factorial(n-1)

factorial(5)
```

4. 权限验证：

```python
class CheckPermission:
    def __init__(self, allowed_role):
        self.allowed_role = allowed_role

    def __call__(self, func):
        def wrapper(*args, **kwargs):
            user_role = get_user_role()  # 假设该函数返回用户角色
            if user_role == self.allowed_role:
                return func(*args, **kwargs)
            else:
                print("Unauthorized access")
                return None
        return wrapper

@CheckPermission("admin")
def delete_user(user_id):
    # 删除用户的逻辑
    print(f"Deleting user with ID: {user_id}")

delete_user(123)
```

5. 异常处理：

```python
class HandleExceptions:
    def __init__(self, func):
        self.func = func

    def __call__(self, *args, **kwargs):
        try:
            return self.func(*args, **kwargs)
        except Exception as e:
            print(f"Exception occurred: {e}")
            return None

@HandleExceptions
def divide(a, b):
    return a / b

divide(10, 0)
```

在这些示例中，类装饰器提供了更灵活的方式来定义装饰器的行为，并且可以在装饰器的实例中保存状态或共享数据。

### （3）带参数的装饰器

当使用带参数的装饰器时，我们需要编写一个装饰器工厂函数，它接收装饰器参数并返回一个装饰器函数。装饰器函数接收被装饰的函数，并根据传递的参数来定义装饰器的行为。

下面是一个带参数的装饰器的写法示例：

```python
def decorator_factory(arg1, arg2):
    def decorator(func):
        def wrapper(*args, **kwargs):
            # 在函数执行前添加额外的逻辑
            print(f"Decorator arguments: {arg1}, {arg2}")
            print("Before function execution")

            # 调用被装饰的函数
            result = func(*args, **kwargs)

            # 在函数执行后添加额外的逻辑
            print("After function execution")

            # 返回结果
            return result
        return wrapper
    return decorator
```

在这个示例中，`decorator_factory`是装饰器工厂函数，它接收参数`arg1`和`arg2`。在内部，它定义了一个装饰器函数`decorator`，它接收被装饰的函数`func`。在`wrapper`函数中，我们可以根据传递的参数来定制装饰器的行为。

要使用带参数的装饰器，我们需要在应用装饰器时传递参数，如下所示：

```python
@decorator_factory("arg1_value", "arg2_value")
def my_function():
    print("Inside the function")

my_function()
```

在这个示例中，`decorator_factory`装饰器被应用于`my_function`函数，并传递了参数`"arg1_value"`和`"arg2_value"`。当调用`my_function()`时，实际上是调用了装饰器实例，并根据传递的参数定制了装饰器的行为。

带参数的装饰器常见的使用场景和示例代码如下：

1. 配置和定制：

```python
def log_level(level):
    def decorator(func):
        def wrapper(*args, **kwargs):
            print(f"Log level: {level}")
            return func(*args, **kwargs)
        return wrapper
    return decorator

@log_level("DEBUG")
def my_function():
    print("Inside the function")

my_function()
```

在这个示例中，`log_level`装饰器工厂函数接收一个级别参数，根据传递的参数来定制装饰器的行为。当应用`@log_level("DEBUG")`装饰器时，`my_function`函数将在执行前打印出日志级别。

2. 条件装饰：

```python
def conditional_decorator(condition):
    def decorator(func):
        def wrapper(*args, **kwargs):
            if condition:
                print("Decorator applied")
                return func(*args, **kwargs)
            else:
                return func
        return wrapper
    return decorator

@conditional_decorator(True)
def my_function():
    print("Inside the function")

my_function()
```

在这个示例中，`conditional_decorator`装饰器工厂函数接收一个条件参数，根据传递的条件参数来决定是否应用装饰器。当应用`@conditional_decorator(True)`装饰器时，`my_function`函数将被装饰器应用。

3. 动态装饰：

```python
def authentication(auth_type):
    def decorator(func):
        def wrapper(*args, **kwargs):
            if auth_type == "basic":
                print("Basic authentication")
            elif auth_type == "jwt":
                print("JWT authentication")
            return func(*args, **kwargs)
        return wrapper
    return decorator

@authentication("basic")
def sensitive_operation():
    print("Performing sensitive operation")

sensitive_operation()
```

在这个示例中，`authentication`装饰器工厂函数接收一个身份验证类型参数，根据传递的参数来决定该应用哪种身份验证方式。当应用`@authentication("basic")`装饰器时，`sensitive_operation`函数将使用基本身份验证进行敏感操作。

4. 多层装饰：

```python
def log_decorator(func):
    def wrapper(*args, **kwargs):
        print("Logging before function execution")
        return func(*args, **kwargs)
    return wrapper

def timing_decorator(func):
    def wrapper(*args, **kwargs):
        start_time = time.time()
        result = func(*args, **kwargs)
        end_time = time.time()
        print(f"Execution time: {end_time - start_time} seconds")
        return result
    return wrapper

@timing_decorator
@log_decorator
def my_function():
    print("Inside the function")

my_function()
```

在这个示例中，我们定义了两个装饰器函数：`log_decorator`和`timing_decorator`。`log_decorator`用于添加日志记录，`timing_decorator`用于计算函数的执行时间。通过在`my_function`函数上应用这两个装饰器，我们可以实现先记录日志，然后计算执行时间的效果。

这些示例展示了带参数的装饰器的不同使用场景，使我们能够根据不同的需求来定制装饰器的行为。带参数的装饰器提供了更大的灵活性，使我们能够编写可重用且可定制的装饰器函数。

### （4）嵌套装饰器

嵌套装饰器是指将多个装饰器按照一定的顺序嵌套在一起，以便对被装饰的函数进行多层次的装饰。嵌套装饰器的写法相对简单，只需要在被装饰函数上依次应用装饰器即可。

下面是一个嵌套装饰器的写法示例：

```python
def decorator1(func):
    def wrapper(*args, **kwargs):
        print("Decorator 1")
        return func(*args, **kwargs)
    return wrapper

def decorator2(func):
    def wrapper(*args, **kwargs):
        print("Decorator 2")
        return func(*args, **kwargs)
    return wrapper

@decorator1
@decorator2
def my_function():
    print("Inside the function")

my_function()
```

在这个示例中，我们定义了两个装饰器函数：`decorator1`和`decorator2`。在`my_function`函数上通过`@decorator1`和`@decorator2`的方式，将这两个装饰器按照顺序应用到`my_function`函数上。当调用`my_function()`时，实际上是先应用`decorator2`装饰器，然后再应用`decorator1`装饰器。

嵌套装饰器的常见使用场景和示例代码如下：

1. 日志记录和性能分析：

```python
def log_decorator(func):
    def wrapper(*args, **kwargs):
        print("Logging before function execution")
        return func(*args, **kwargs)
    return wrapper

def timing_decorator(func):
    def wrapper(*args, **kwargs):
        start_time = time.time()
        result = func(*args, **kwargs)
        end_time = time.time()
        print(f"Execution time: {end_time - start_time} seconds")
        return result
    return wrapper

@log_decorator
@timing_decorator
def my_function():
    print("Inside the function")

my_function()
```

在这个示例中，我们定义了两个装饰器函数：`log_decorator`和`timing_decorator`。通过将这两个装饰器按照顺序嵌套在`my_function`函数上，我们可以实现先记录日志，然后计算函数的执行时间的效果。

2. 身份验证和权限检查：

```python
def authentication_decorator(func):
    def wrapper(*args, **kwargs):
        if check_authenticated():
            print("Authentication passed")
            return func(*args, **kwargs)
        else:
            print("Authentication failed")
            return None
    return wrapper

def permission_decorator(func):
    def wrapper(*args, **kwargs):
        if check_permission():
            print("Permission granted")
            return func(*args, **kwargs)
        else:
            print("Permission denied")
            return None
    return wrapper

@authentication_decorator
@permission_decorator
def sensitive_operation():
    print("Performing sensitive operation")

sensitive_operation()
```

在这个示例中，我们定义了两个装饰器函数：`authentication_decorator`和`permission_decorator`。通过将这两个装饰器按照顺序嵌套在`sensitive_operation`函数上，我们可以实现先进行身份验证，然后检查权限的效果。

3. 缓存和日志记录：

```python
def cache_decorator(func):
    cache = {}

    def wrapper(*args, **kwargs):
        key = (args, tuple(kwargs.items()))
        if key in cache:
            print("Returning cached result")
            return cache[key]
        else:
            result = func(*args, **kwargs)
            cache[key] = result
            return result

    return wrapper

def log_decorator(func):
    def wrapper(*args, **kwargs):
        print("Logging before function execution")
        return func(*args, **kwargs)
    return wrapper

@log_decorator
@cache_decorator
def expensive_operation(num):
    print("Performing expensive operation")
    time.sleep(2)
    return num * 2

result1 = expensive_operation(5)
result2 = expensive_operation(5)
```

在这个示例中，我们定义了两个装饰器函数：`cache_decorator`和`log_decorator`。通过将这两个装饰器按照顺序嵌套在`expensive_operation`函数上，我们可以实现先检查缓存，如果结果已经存在则直接返回缓存的结果，否则执行昂贵的操作，并将结果缓存起来。同时，还可以在函数执行前记录日志。

这些示例展示了嵌套装饰器的常见使用场景，可以根据实际需求灵活组合和编写自己的装饰器。

### （5）类方法装饰器

类方法装饰器是指用于装饰类方法的装饰器。类方法装饰器可以在类方法定义之前使用`@`符号将装饰器应用到类方法上，以实现对类方法的增强或扩展。类方法装饰器的写法与普通函数装饰器类似，只需将装饰器函数应用到类方法上即可。

下面是一个类方法装饰器的写法示例：

```python
def class_method_decorator(func):
    def wrapper(cls, *args, **kwargs):
        print(f"Decorator applied to class method: {func.__name__}")
        return func(cls, *args, **kwargs)
    return wrapper

class MyClass:
    @class_method_decorator
    def my_class_method(cls):
        print("Inside class method")

MyClass.my_class_method()
```

在这个示例中，我们定义了一个类方法装饰器函数`class_method_decorator`。在`MyClass`类的`my_class_method`方法上通过`@class_method_decorator`的方式将装饰器应用到类方法上。当调用`MyClass.my_class_method()`时，实际上是先应用`class_method_decorator`装饰器，然后再执行类方法。

类方法装饰器的常见使用场景和示例代码如下：

1. 计数器：

```python
def count_calls(func):
    def wrapper(cls, *args, **kwargs):
        wrapper.count += 1
        print(f"Method {func.__name__} called {wrapper.count} times")
        return func(cls, *args, **kwargs)
    wrapper.count = 0
    return wrapper

class Counter:
    @classmethod
    @count_calls
    def increment(cls):
        print("Incrementing counter")

    @classmethod
    @count_calls
    def decrement(cls):
        print("Decrementing counter")

Counter.increment()
Counter.increment()
Counter.decrement()
```

在这个示例中，我们定义了一个类方法装饰器函数`count_calls`。通过将`count_calls`装饰器应用到`increment`和`decrement`类方法上，我们可以实现对这两个方法的调用次数进行计数，并在每次调用时打印计数结果。

2. 缓存：

```python
def cache_result(func):
    cache = {}

    def wrapper(cls, *args, **kwargs):
        key = (func.__name__, args, tuple(kwargs.items()))
        if key in cache:
            print("Returning cached result")
            return cache[key]
        else:
            result = func(cls, *args, **kwargs)
            cache[key] = result
            return result

    return wrapper

class MathUtils:
    @classmethod
    @cache_result
    def factorial(cls, n):
        if n == 1:
            return 1
        else:
            return n * cls.factorial(n-1)

print(MathUtils.factorial(5))
print(MathUtils.factorial(5))
```

在这个示例中，我们定义了一个类方法装饰器函数`cache_result`。通过将`cache_result`装饰器应用到`factorial`类方法上，我们可以实现对阶乘计算结果的缓存，避免重复计算。当第一次调用`MathUtils.factorial(5)`时，会执行阶乘计算，并将结果缓存起来。后续再次调用相同的参数时，直接返回缓存的结果。

这些示例展示了类方法装饰器的常见使用场景，可以根据实际需求编写自己的类方法装饰器来增强或扩展类方法的功能。
