## Java如何优雅的编程

#### 1 需要 Map 的主键和取值时，应该迭代 entrySet()

- 反例

  ```java
  Map<String, String> map = ...;
  for (String key : map.keySet()) {
      String value = map.get(key);
      ...
  }
  ```

- 正例

  ```Java
  Map<String, String> map = ...;
  for (Map.Entry<String, String> entry : map.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      ...
  }
  ```

#### 2 应该使用[Collection](https://so.csdn.net/so/search?q=Collection&spm=1001.2101.3001.7020).isEmpty()检测空

- 反例

  ```Java
  List<Integer> list = new ArrayList<>(10);
  if (list.size() == 0) {
      ...
  }
  ```

- 正例

  ```Java
  List<Integer> list = new ArrayList<>(10);
  if (list.isEmpty()) {
      ...
  }
  ```

- 需要判断null

  如果调用其他服务拿到一个`list`，这个`list`有可能为空，如果直接使用`list.isEmpty()`进行判断，可能会抛出空指针异常，因为其他的服务可能会返回一个`null`对象，而`null`没有`isEmpty()`方法，所以抛出空指针异常

  ```Java
  List<Integer> list = getListFromOtherService();
  if (CollectionUtils.isEmpty(list)) {
      ...
  }
  if (CollectionUtils.isNotEmpty(list)) {
      ...
  }
  ```

#### 3 不要把集合对象传给自己

- 反例

  ```Java
  List<String> list = new ArrayList<>();
  list.add("Hello");
  list.add("World");
  if (list.containsAll(list)) { // 无意义,总是返回true
      ...
  }
  list.removeAll(list); // 性能差, 直接使用clear()会更好
  ```

#### 4 集合初始化尽量指定大小

**Java集合初始化时都会指定一个默认大小，当默认大小不再满足数据需求时就会扩容，每次扩容的时间复杂度有可能是O(n)**

- 反例

  ```Java
  List<UserDO> userDOList = getListFromOtherService();
  Set<Long> userSet = new HashSet<>();//没有指定初始大小
  Map<Long, UserDO> userMap = new HashMap<>();//没有指定初始大小
  List<UserVO> userList = new ArrayList<>();//没有指定初始大小
  for (UserDO userDO : userDOList) {
      userSet.add(userDO.getId());
      userMap.put(userDO.getId(), userDO);
      userList.add(transUser(userDO));
  }
  ```

- 正例

  ```Java
  List<UserDO> userDOList = getListFromOtherService();
  int userSize = userDOList.size();
  Set<Long> userSet = new HashSet<>(userSize);
  Map<Long, UserDO> userMap = new HashMap<>((int) Math.ceil(userSize * 4.0 / 3 + 1));
  List<UserVO> userList = new ArrayList<>(userSize);
  for (UserDO userDO : userDOList) {
      userSet.add(userDO.getId());
      userMap.put(userDO.getId(), userDO);
      userList.add(transUser(userDO));
  }
  ```

  > **`HashMap`的初始大小为什么是（expectedSize * 4.0 / 3 + 1）？**
  >
  > 因为`HashMap`的默认大小是16，扩容的条件是当容器元素个数超过容器的3/4时会开始自动扩容，而且，`HashMap`就算给定了初始大小，也不是完全按照我们指定的大小创建，而是使用**第一个大于传入的值的2的幂**作为初始大小，比如，1->2, 3->4, 7->8, 9->16 ...
  >
  > 举个例子：当我们需要用Map存储7个键值对，我们如果初始化默认大小为7，此时JDK通过计算为我们创建了一个容量为8的`HashMap`，因为`HashMap`的默认装载因子为0.75，那么也就是在`HashMap`中存储的元素个数达到6时，**在添加第7个元素的时候，JDK会为我们自动扩容，这不是我们想要的**，所以我们需要将`HashMap`的大小设置的比我们需要存储的元素个数多一点。
  >
  > 所以应该设置成（7*0.75+1）=10，然后JDK通过计算之后，得到16，16\*0.75=12，这样就不会在自动扩容了。
  >
  > 结论：
  >
  > 1. 在我们能够明确清楚要往`HashMap`中存放元素的个数时，我们可以通过公式（expectedSize * 4.0 / 3 + 1）提前申请一个较大的容器提升性能，但是也会浪费一部分内存
  > 2. 该用法是空间换时间，性能是提升了，但是内存消耗增加了

  > 《阿里巴巴Java开发手册》建议`HashMap`的初始大小：initialCapacity=(需要存储的元素个数 / 负载因子) + 1。默认装载因子为：0.75。

#### 5 字符串拼接使用 StringBuilder

- 反例

  ```Java
  String s = "";
  for (int i = 0; i < 10; i++) {
      s += i;
  }
  ```

- 正例

  ```Java
  String a = "a";
  String b = "b";
  String c = "c";
  String s = a + b + c; // 没问题，java编译器会进行优化
  StringBuilder sb = new StringBuilder(50); //尽量设置初始化大小
  for (int i = 0; i < 10; i++) {
      sb.append(i);
  }
  ```

#### 6 判断链表还是数组

```Java
// 调用别人的服务获取到list
List<Integer> list = otherService.getList();
if (list instanceof RandomAccess) {
    // 内部数组实现，可以随机访问
    System.out.println(list.get(list.size() - 1));
} else {
    // 内部可能是链表实现，随机访问效率低
}
```

#### 7 频繁调用 `Collection.contains`方法建议使用 Set

- 反例

  ```Java
  ArrayList<Integer> list = otherService.getList();
  for (int i = 0; i <= Integer.MAX_VALUE; i++) {
      // 时间复杂度O(n)
      list.contains(i);
  }
  ```

- 正例

  ```Java
  ArrayList<Integer> list = otherService.getList();
  Set<Integer> set = new HashSet(list);
  for (int i = 0; i <= Integer.MAX_VALUE; i++) {
      // 时间复杂度O(1)
      set.contains(i);
  }
  ```

#### 8 常量赋值尽量不要声明新对象

**直接给常量赋值，只是创建了一个对象引用，而这个对象引用指向常量值。**

- 反例

  ```Java
  Long i = new Long(1L);//i变量指向了堆内存中的一个对象，而堆中的对象指向常量池中的这个常量
  String s = new String("abc");
  ```

- 正例

  ```Java
  Long i = 1L;
  String s = "abc";
  ```

#### 9 当成员变量值无需改变时，尽量定义为静态常量

**在类的每个对象实例中，每个成员变量都有一份副本，而成员静态常量只有一份实例**

- 反例

  ```Java
  public class HttpConnection {
      private final long timeout = 5L;//每new一个对象，内存中就会有一个该对象的拷贝
      ...
  }
  ```

- 正例

  ```Java
  public class HttpConnection {
      private static final long TIMEOUT = 5L;
      ...
  }
  ```

#### 10 controller接口接收参数，尽量用包装类

因为调用方可能会传一个空的参数过来，我们使用包装类，可以对该参数进行判空处理，如果使用基本数据类型，则不能对其进行判空处理

- 反例

  ```Java
  @RequestMapping(value = "/user")
  public ResWrapper<Info> user(
      @RequestParam(name = "start_day", required = false) int startDayId,
      @RequestParam(name = "eusernd_day", required = false) int endDayId,
      @RequestParam(name = "offset", required = false) int offset,
      @RequestParam(name = "page_size", required = false) int pageSize,
  ) {
      return WrapMapper.ok(voiceDataService.user(startDayId, endDayId, offset, pageSize));
  }
  ```

- 正例

  ```
  @RequestMapping(value = "/user")
  public ResWrapper<Info> user(
      @RequestParam(name = "start_day", required = false) Integer startDayId,
      @RequestParam(name = "eusernd_day", required = false) Integer endDayId,
      @RequestParam(name = "offset", required = false) Integer offset,
      @RequestParam(name = "page_size", required = false) Integer pageSize,
  ) {
      return WrapMapper.ok(voiceDataService.user(startDayId, endDayId, offset, pageSize));
  }
  ```

#### 11 如果变量的初值会被覆盖，就没有必要给变量赋初值

- 反例

  ```Java
  List<UserDO> userList = new ArrayList<>();
  if (isAll) {
      userList = userDAO.queryAll();
  } else {
      userList = userDAO.queryActive();
  }
  ```

- 正例

  ```Java
  List<UserDO> userList;
  if (isAll) {
      userList = userDAO.queryAll();
  } else {
      userList = userDAO.queryActive();
  }
  ```

#### 12  尽量使用函数内的基本类型的临时变量

**在函数内，基本类型的参数和临时变量都保存在栈（Stack）中，访问速度较快；对象类型的参数和临时变量的引用都保存在栈（Stack）中，内容都保存在堆（Heap）中，访问速度较慢。在类中，任何类型的成员变量都保存在堆（Heap）中，访问速度较慢**

- 反例

  ```Java
  public final class Accumulator {
      private double result = 0.0D;
      public void addAll(@NonNull double[] values) {
          for(double value : values) {
              result += value;//result变量存在堆中，访问速度慢
          }
      }
      ...
  }
  ```

- 正例

  ```Java
  public final class Accumulator {
      private double result = 0.0D;
      public void addAll(@NonNull double[] values) {
          double sum = 0.0D;
          for(double value : values) {
              sum += value;//sum变量存放在栈中，访问速度快
          }
          result += sum;
      }
      ...
  }
  ```

#### 13 尽量不要在循环体外定义变量

**在老版JDK中，建议“尽量不要在循环体内定义变量”，但是在新版的JDK中已经做了优化，根据“ 局部变量作用域最小化 ”原则，变量定义在循环体内更科学更便于维护，避免了延长大对象生命周期导致延缓回收问题**

- 反例

  ```Java
  UserVO userVO;
  List<UserDO> userDOList = ...;
  List<UserVO> userVOList = new ArrayList<>(userDOList.size());
  for (UserDO userDO : userDOList) {
      userVO = new UserVO();
      userVO.setId(userDO.getId());
      ...
      userVOList.add(userVO);
  }
  ```

- 正例

  ```Java
  List<UserDO> userDOList = ...;
  List<UserVO> userVOList = new ArrayList<>(userDOList.size());
  for (UserDO userDO : userDOList) {
      UserVO userVO = new UserVO();//直接在循环内定义，避免延迟回收
      userVO.setId(userDO.getId());
      ...
      userVOList.add(userVO);
  }
  ```

#### 14 不可变的静态常量，尽量使用非线程安全类

- 反例

  ```Java
  public static final Map<String, Class> CLASS_MAP;
  static {
      Map<String, Class> classMap = new ConcurrentHashMap<>(16);//线程安全，但是性能较低，因为加了锁
      classMap.put("VARCHAR", java.lang.String.class);
      ...
      CLASS_MAP = Collections.unmodifiableMap(classMap);
  }
  ```

- 正例

  ```Java
  public static final Map<String, Class> CLASS_MAP;
  static {
      Map<String, Class> classMap = new HashMap<>(16);//线程不安全，但是性能高
      classMap.put("VARCHAR", java.lang.String.class);
      ...
      CLASS_MAP = Collections.unmodifiableMap(classMap);
  }
  ```

#### 15 尽量避免定义不必要的子类

**多一个类就需要多一份类加载，所以尽量避免定义不必要的子类**

- 反例

  ```Java
  public static final Map<String, Class> CLASS_MAP =
      Collections.unmodifiableMap(new HashMap<String, Class>(16) {
      private static final long serialVersionUID = 1L;
      {
          put("VARCHAR", java.lang.String.class);
      }
  });
  ```

- 正例

  ```Java
  public static final Map<String, Class> CLASS_MAP;
  static {
      Map<String, Class> classMap = new HashMap<>(16);
      classMap.put("VARCHAR", java.lang.String.class);
      ...
      CLASS_MAP = Collections.unmodifiableMap(classMap);
  }
  ```

#### 16 尽量指定类的final修饰符

**为类指定final修饰符，可以让该类不可以被继承。如果指定了一个类为final，则该类所有的方法都是final的，Java编译器会寻找机会内联所有的final方法**

#### 17 把跟类成员变量无关的方法声明成静态方法

**静态方法的好处就是不用生成类的实例就可以直接调用。静态方法不再属于某个对象，而是属于它所在的类**

- 反例：

  ```Java
  public int getMonth(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(Calendar.MONTH) + 1;
  }
  ```

- 正例

  ```Java
  public static int getMonth(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(Calendar.MONTH) + 1;
  }
  ```

#### 18 尽量减少方法的重复调用

- 反例

  ```Java
  List<UserDO> userList = ...;
  for (int i = 0; i < userList.size(); i++) {
      ...
  }
  ```

- 正例

  ```Java
  List<UserDO> userList = ...;
  int userLength = userList.size();
  for (int i = 0; i < userLength; i++) {
      ...
  }
  ```

#### 19 尽量使用移位来代替正整数乘除

**用移位操作可以极大地提高性能。对于乘除2^n(n为正整数)的正整数计算，可以用移位操作来代替**

- 反例

  ```Java
  int num1 = a * 4;
  int num2 = a / 4;
  ```

- 正例

  ```Java
  int num1 = a << 2;
  int num2 = a >> 2;
  ```

#### 20 尽量不在条件表达式中使用`!`取反

**使用!取反会多一次计算，如果没有必要则优化掉**

- 反例

  ```Java
  if (!(a >= 10)) {
      ... // 条件处理1
  } else {
      ... // 条件处理2
  }
  ```

- 正例

  ```Java
  if (a < 10) {
      ... // 条件处理1
  } else {
      ... // 条件处理2
  }
  ```

#### 21 对于多选择分支，尽量使用switch语句而不是if-else语句

**if-else语句，每个if条件语句都要加装计算，直到if条件语句为true为止。switch语句进行了跳转优化，Java中采用tableswitch或lookupswitch指令实现，对于多常量选择分支处理效率更高。经过试验证明：在每个分支出现概率相同的情况下，低于5个分支时if-else语句效率更高，高于5个分支时switch语句效率更高**

- 反例

  ```Java
  if (i == 1) {
      ...; // 分支1
  } else if (i == 2) {
      ...; // 分支2
  } else if (i == ...) {
      ...; // 分支n
  } else {
      ...; // 分支n+1
  }
  ```

- 正例

  ```Java
  switch (i) {
      case 1 :
          ... // 分支1
          break;
      case 2 :
          ... // 分支2
          break;
      case ... :
          ... // 分支n
          break;
      default :
          ... // 分支n+1
          break;
  }
  ```

#### 22 尽量不要使用正则表达式匹配

**正则表达式匹配效率较低，尽量使用字符串匹配操作**

#### 23 不要使用循环拷贝数组，尽量使用System.arraycopy拷贝数组

**推荐使用System.arraycopy拷贝数组，也可以使用Arrays.copyOf拷贝数组**

- 反例

  ```Java
  int[] sources = new int[] {1, 2, 3, 4, 5};
  int[] targets = new int[sources.length];
  for (int i = 0; i < targets.length; i++) {
      targets[i] = sources[i];
  }
  ```

- 正例

  ```Java
  int[] sources = new int[] {1, 2, 3, 4, 5};
  int[] targets = new int[sources.length];
  System.arraycopy(sources, 0, targets, 0, targets.length);
  ```

#### 24 集合转化为类型T数组时，尽量传入空数组T[0]

**将集合转换为数组有2种形式：toArray(new T[n])和toArray(new T[0])。在旧的Java版本中，建议使用toArray(new T[n])，因为创建数组时所需的反射调用非常慢。在OpenJDK6后，反射调用是内在的，使得性能得以提高，toArray(new T[0])比toArray(new T[n])效率更高。此外，toArray(new T[n])比toArray(new T[0])多获取一次列表大小，如果计算列表大小耗时过长，也会导致toArray(new T[n])效率降低**

- 反例

  ```Java
  List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5, ...);
  Integer[] integers = integerList.toArray(new Integer[integerList.size()]);
  ```

- 正例

  ```Java
  List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5, ...);
  Integer[] integers = integerList.toArray(new Integer[0]); // 勿用new Integer[]{}
  ```

#### 25 不要使用循环拷贝集合，尽量使用JDK提供的方法拷贝集合

**JDK提供的方法可以一步指定集合的容量，避免多次扩容浪费时间和空间。同时，这些方法的底层也是调用System.arraycopy方法实现，进行数据的批量拷贝效率更高**

- 反例

  ```Java
  List<UserDO> user1List = ...;
  List<UserDO> user2List = ...;
  List<UserDO> userList = new ArrayList<>(user1List.size() + user2List.size());
  for (UserDO user1 : user1List) {
      userList.add(user1);
  }
  for (UserDO user2 : user2List) {
      userList.add(user2);
  }
  ```

- 正例

  ```Java
  List<UserDO> user1List = ...;
  List<UserDO> user2List = ...;
  List<UserDO> userList = new ArrayList<>(user1List.size() + user2List.size());
  userList.addAll(user1List);
  userList.addAll(user2List);
  ```

#### 26 尽量重复使用同一缓冲区

**针对缓冲区，Java虚拟机需要花时间生成对象，还要花时间进行垃圾回收处理。所以，尽量重复利用缓冲区**

- 反例

  ```Java
  StringBuilder builder1 = new StringBuilder(128);
  builder1.append("update t_user set name = '").append(userName).append("' where id = ").append(userId);
  statement.executeUpdate(builder1.toString());
  StringBuilder builder2 = new StringBuilder(128);
  builder2.append("select id, name from t_user where id = ").append(userId);
  ResultSet resultSet = statement.executeQuery(builder2.toString());
  ...
  ```

- 正例

  ```Java
  StringBuilder builder = new StringBuilder(128);
  builder.append("update t_user set name = '").append(userName).append("' where id = ").append(userId);
  statement.executeUpdate(builder.toString());
  builder.setLength(0);//这里将之前的对象清空，继续使用
  builder.append("select id, name from t_user where id = ").append(userId);
  ResultSet resultSet = statement.executeQuery(builder.toString());
  ...
  ```

#### 27 尽量使用缓冲流减少IO操作

**使用缓冲流BufferedReader、BufferedWriter、BufferedInputStream、BufferedOutputStream等，可以大幅较少IO次数并提升IO速度**

- 反例

  ```Java
  try (FileInputStream input = new FileInputStream("a");
      FileOutputStream output = new FileOutputStream("b")) {
      int size = 0;
      byte[] temp = new byte[1024];
      while ((size = input.read(temp)) != -1) {
          output.write(temp, 0, size);
      }
  } catch (IOException e) {
      log.error("复制文件异常", e);
  }
  ```

- 正例

  ```Java
  try (BufferedInputStream input = new BufferedInputStream(new FileInputStream("a"));
      BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream("b"))) {
      int size = 0;
      byte[] temp = new byte[1024];
      while ((size = input.read(temp)) != -1) {
          output.write(temp, 0, size);
      }
  } catch (IOException e) {
      log.error("复制文件异常", e);
  }
  ```

#### 28 在单线程中，尽量使用非线程安全类

**java中的StringBuffer是线程安全的，StringBuilder是非线程安全的**

- 反例

  ```Java
  StringBuffer buffer = new StringBuffer(128);
  buffer.append("select * from ").append(T_USER).append(" where id = ?");
  ```

- 正例

  ```Java
  StringBuilder buffer = new StringBuilder(128);
  buffer.append("select * from ").append(T_USER).append(" where id = ?");
  ```

#### 29 在多线程中，尽量使用线程安全类

- 反例

  ```Java
  private volatile int counter = 0;
  public void access(Long userId) {
      synchronized (this) {
          counter++;
      }
      ...
  }
  ```

- 正例

  ```Java
  private final AtomicInteger counter = new AtomicInteger(0);
  public void access(Long userId) {
      counter.incrementAndGet();
      ...
  }
  ```

#### 30 尽量减少同步代码块范围

**在一个方法中，可能只有一小部分的逻辑是需要同步控制的，如果同步控制了整个方法会影响执行效率。所以，尽量减少同步代码块的范围，只对需要进行同步的代码进行同步**

- 反例

  ```Java
  private volatile int counter = 0;
  public synchronized void access(Long userId) {
    counter++;
      ... // 非同步操作
  }
  ```

- 正例

  ```Java
  private volatile int counter = 0;
  public void access(Long userId) {
      synchronized (this) {
          counter++;
      }
      ... // 非同步操作
  }
  ```

#### 31 尽量使用线程池减少线程开销

**多线程中两个必要的开销：线程的创建和上下文切换。采用线程池，可以尽量地避免这些开销**

- 反例

  ```Java
  public void executeTask(Runnable runnable) {
      new Thread(runnable).start();
  }
  ```

- 正例

  ```Java
  private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(10);
  public void executeTask(Runnable runnable) {
      executorService.execute(runnable);
  }
  ```

#### 32 长整型常量后添加大写 L

- 反例

  ```Java
  long value = 1l;
  long max = Math.max(1L, 5);
  ```

- 正例

  ```Java
  long value = 1L;
  long max = Math.max(1L, 5L);
  ```

#### 33 不要使用魔法值

- 反例

  ```Java
  for (int i = 0; i < 100; i++){
      ...
  }
  if (a == 100) {
      ...
  }		
  ```

- 正例

  ```Java
  private static final int MAX_COUNT = 100;
  for (int i = 0; i < MAX_COUNT; i++){
      ...
  }
  if (count == MAX_COUNT) {
      ...
  }
  ```

#### 34 不要使用集合实现来赋值静态成员变量

- 反例

  ```Java
  private static Map<String, Integer> map = new HashMap<String, Integer>() {
      {
          put("a", 1);
          put("b", 2);
      }
  };
  
  private static List<String> list = new ArrayList<String>() {
      {
          add("a");
          add("b");
      }
  };
  ```

- 正例

  ```Java
  private static Map<String, Integer> map = new HashMap<>();
  static {
      map.put("a", 1);
      map.put("b", 2);
  };
  
  private static List<String> list = new ArrayList<>();
  static {
      list.add("a");
      list.add("b");
  };
  ```

#### 35 删除代码中未使用的方法、参数、变量

1. 对于不熟悉项目的同事而言，某一个**未使用的方法或变量**会给人造成一些迷惑，浪费时间
2. 删除未使用的代码，可以使项目更简洁

#### 36 公有静态常量应该通过类访问

**因为类中的静态常量是属于类的，而不是属于某个实例对象的，虽然用实例对象也能访问，但是如果用实例对象调用会使得接口不清晰**

- 反例

  ```Java
  public class User {
      public static final String CONST_NAME = "name";
      ...
  }
  
  User user = new User();
  String nameKey = user.CONST_NAME;//这里是用的某个实例对象调用
  ```

- 正例

  ```Java
  public class User {
      public static final String CONST_NAME = "name";
      ...
  }
  
  String nameKey = User.CONST_NAME;
  ```

#### 37 使用String.valueOf(value)代替""+value

**String.valueOf(value)的效率要比`""+value`的效率更高**

- 反例

  ```Java
  int i = 1;
  String s = "" + i;
  ```

- 正例

  ```Java
  int i = 1;
  String s = String.valueOf(i);
  ```

#### 38 过时代码添加 @Deprecated 注解

- 正例

  ```Java
  /**
   * @deprecated 此方法效率较低，请使用{@link newSave()}方法替换它
   */
  @Deprecated
  public void save(){
      // do something
  }
  ```

#### 39 尽量避免在循环中捕获异常

- 反例

  ```Java
  public Double sum(List<String> valueList) {
      double sum = 0.0D;
      for (String value : valueList) {
          try {
              sum += Double.parseDouble(value);
          } catch (NumberFormatException e) {
              return null;
          }
      }
      return sum;
  }
  ```

- 正例

  ```Java
  public Double sum(List<String> valueList) {
      double sum = 0.0D;
      try {
          for (String value : valueList) {
              sum += Double.parseDouble(value);
          }
      } catch (NumberFormatException e) {
          return null;
      }
      return sum;
  }
  ```

#### 40 尽量避免使用构造方法 BigDecimal(double)

- 反例

  ```Java
  BigDecimal value = new BigDecimal(0.1D); // 0.100000000000000005551115...
  ```

- 正例

  ```Java
  BigDecimal value = BigDecimal.valueOf(0.1D);; // 0.1
  ```

#### 41 方法返回空数组或空集合而不是 null

**编写方法时，如果方法的返回结果是集合或数组，应该返回一个空的集合或数组，而不是直接返回一个null，如果调用方没有检测null，则会抛出空指针异常**

- 反例

  ```Java
  public static Result[] getResults() {
      return null;
  }
  
  public static List<Result> getResultList() {
      return null;
  }
  
  public static Map<String, Result> getResultMap() {
      return null;
  }
  
  public static void main(String[] args) {
      Result[] results = getResults();
      if (results != null) {
          for (Result result : results) {
              ...
          }
      }
  
      List<Result> resultList = getResultList();
      if (resultList != null) {
          for (Result result : resultList) {
              ...
          }
      }
  
      Map<String, Result> resultMap = getResultMap();
      if (resultMap != null) {
          for (Map.Entry<String, Result> resultEntry : resultMap) {
              ...
          }
      }
  }
  ```

- 正例

  ```Java
  public static Result[] getResults() {
      return new Result[0];
  }
  
  public static List<Result> getResultList() {
      return Collections.emptyList();
  }
  
  public static Map<String, Result> getResultMap() {
      return Collections.emptyMap();
  }
  
  public static void main(String[] args) {
      Result[] results = getResults();
      for (Result result : results) {
          ...
      }
  
      List<Result> resultList = getResultList();
      for (Result result : resultList) {
          ...
      }
  
      Map<String, Result> resultMap = getResultMap();
      for (Map.Entry<String, Result> resultEntry : resultMap) {
          ...
      }
  }
  ```

#### 42 优先使用常量或确定值来调用 equals 方法

- 反例

  ```Java
  public void isFinished(OrderStatus status) {
      return status.equals(OrderStatus.FINISHED); // 可能抛空指针异常
  }
  ```

- 正例

  ```Java
  public void isFinished(OrderStatus status) {
      return OrderStatus.FINISHED.equals(status);
  }
  
  public void isFinished(OrderStatus status) {
      return Objects.equals(status, OrderStatus.FINISHED);
  }
  ```

#### 43 枚举的属性字段必须是私有不可变

- 反例

  ```Java
  public enum UserStatus {
      DISABLED(0, "禁用"),
      ENABLED(1, "启用");
  
      public int value;//公有
      private String description;
  
      private UserStatus(int value, String description) {
          this.value = value;
          this.description = description;
      }
  
      public String getDescription() {
          return description;
      }
  
      public void setDescription(String description) {//这里可以改变枚举的值
          this.description = description;
      }
  }
  ```

- 正例

  ```Java
  public enum UserStatus {
      DISABLED(0, "禁用"),
      ENABLED(1, "启用");
  
      private final int value;
      private final String description;
  
      private UserStatus(int value, String description) {
          this.value = value;
          this.description = description;
      }
  
      public int getValue() {
          return value;
      }
  
      public String getDescription() {
          return description;
      }
  }
  ```

#### 44 String.split(String regex)中的regex注意转义

- 反例

  ```Java
  "a.ab.abc".split("."); // 结果为[]
  "a|ab|abc".split("|"); // 结果为["a", "|", "a", "b", "|", "a", "b", "c"]
  ```

- 正例

  ```Java
  "a.ab.abc".split("\\."); // 结果为["a", "ab", "abc"]
  "a|ab|abc".split("\\|"); // 结果为["a", "ab", "abc"]
  ```

  

## Reference

1. https://blog.csdn.net/jichao138168/article/details/103609318
2. [关于HashMap容量的初始化，还有这么多学问。](https://mp.weixin.qq.com/s?__biz=MzI3NzE0NjcwMg==&mid=2650121359&idx=1&sn=c63d62be1a36db675c62e341044f10e0&chksm=f36bb9aec41c30b8b369428db1286d3de9bc04675057cde49632f3ba50db2d0a69451d6ec080&mpshare=1&scene=1&srcid=0529kU8fMyKTUrpKDwaLMJc6#rd)
3. [HashMap中傻傻分不清楚的那些概念](https://mp.weixin.qq.com/s?__biz=MzI3NzE0NjcwMg==&mid=2650121339&idx=1&sn=7b6bfd4b16b65972271cdb929134496b&chksm=f36bb95ac41c304cace48901fc1be5fd6a13825b6443c331182f636997a05c792a82a9cb27aa&scene=21#wechat_redirect)
4. [全网把Map中的hash()分析的最透彻的文章，别无二家。](https://mp.weixin.qq.com/s?__biz=MzI3NzE0NjcwMg==&mid=2650120877&idx=1&sn=401bb7094d41918f1a6e142b6c66aaac&chksm=f36bbf8cc41c369aa44c319942b06ca0f119758b22e410e8f705ba56b9ac6d4042fe686dbed4&scene=21#wechat_redirect)
5. [Java字符串拼接效率的比较和对String.valueOf的思考](https://blog.csdn.net/diangangqin/article/details/100996596)
6. [Java提高篇——静态代码块、构造代码块、构造函数以及Java类初始化顺序](https://www.cnblogs.com/Qian123/p/5713440.html)
7. [重构 - 为什么应该删除未使用的代码？](https://www.itranslater.com/qa/details/2325897869242401792)
8. [消灭 Java 代码的“坏味道”](https://www.infoq.cn/article/dwa5rvr96s1vta75ltwz)