# multi-thread-and-concurrency
这是一个配合理解 Java 多线程的库

# 理解多线程（并发）编程

## 什么是线程、多线程？

### 线程

#### 线程状态

#### 优先级

#### daemon


### 多线程

### 多线程一定比单线程执行快吗？

#### 上下文切换

### 多线程编程的一些挑战

#### 并发安全让人头大

#### 加锁吧

#### 死锁了

#### 机器不行啊

#### 带宽不够啊

# 并发编程基础

## JMM（Java 内存模型）

### 内存模型抽象结构

### 线程通信助手
#### volatile

#### synchronized

#### ThreadLocal

#### wait/notify(All)

#### join

### 重排序

#### 什么是重排序

#### 用 final 治愈重排序

#### volatile 和锁再度登场

### 小结

## Lock 的实现与使用

### 基础组件-队列同步器

### ReentrantLock

### ReentrantReadWriteLock

### LockSupport

### Condition

## 并发容器

## 并发工具类

## 并发框架

### 线程池

### Fork/Join

### RxJava

### 其他（Disruptor 和 Akka）
