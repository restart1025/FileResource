# FileResource
书籍资料

```

 /**
  * 增加标签页
  */
 function addTab(options) {
     //option:
     //tabMainName:tab标签页所在的容器
     //tabName:当前tab的名称
     //tabTitle:当前tab的标题
     //tabUrl:当前tab所指向的URL地址
     var exists = checkTabIsExists(options.tabMainName, options.tabName);
     if(exists){
         $("#tab_a_"+options.tabName).click();
     } else {
         $("#"+options.tabMainName).append('<li id="tab_li_'+options.tabName+'"><a href="#tab_content_'+options.tabName+'" data-toggle="tab" id="tab_a_'+options.tabName+'"><button class="close closeTab" type="button" onclick="closeTab(this);">×</button>'+options.tabTitle+'</a></li>');
          
         //固定TAB中IFRAME高度
         mainHeight = $(document.body).height() - 5;
          
         var content = '';
         if(options.content){
             content = option.content;
         } else {
             content = '<iframe src="' + options.tabUrl + '" width="100%" height="'+mainHeight+'px" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="yes" allowtransparency="yes"></iframe>';
         }
         $("#"+options.tabContentMainName).append('<div id="tab_content_'+options.tabName+'" role="tabpanel" class="tab-pane" id="'+options.tabName+'">'+content+'</div>');
         $("#tab_a_"+options.tabName).click();
     }
 }
  
  
 /**
  * 关闭标签页
  * @param button
  */
 function closeTab (button) {
      
     //通过该button找到对应li标签的id
     var li_id = $(button).parent().parent().attr('id');
     var id = li_id.replace("tab_li_","");
      
     //如果关闭的是当前激活的TAB，激活他的前一个TAB
     if ($("li.active").attr('id') == li_id) {
         $("li.active").prev().find("a").click();
     }
      
     //关闭TAB
     $("#" + li_id).remove();
     $("#tab_content_" + id).remove();
 };
  
 /**
  * 判断是否存在指定的标签页
  * @param tabMainName
  * @param tabName
  * @returns {Boolean}
  */
 function checkTabIsExists(tabMainName, tabName){
     var tab = $("#"+tabMainName+" > #tab_li_"+tabName);
     //console.log(tab.length)
     return tab.length > 0;
 }

```

## 多线程使用

```
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ManyThread {

	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		// Callable 线程会有多个返回值, 放入CompletionService中, 执行的是call方法而不是run方法 
		ExecutorService eService = Executors.newCachedThreadPool();// 创建缓存线程池
		CompletionService<Integer> completionService = 
				new ExecutorCompletionService<Integer>(eService);// 将线程池放进去
		// 循环执行5个线程，返回值分别放入completionService中
		for( int i = 1; i <= 5; i++ )
		{
			final int seq = 1;
			completionService.submit(
					new Callable<Integer>() {

						public Integer call() throws Exception {
							System.out.println("线程执行");
							Thread.sleep( new Random().nextInt(5000) );
							return seq;
						}
						
					}
			);
		}
		// 执行结束之后，取出线程中放入的值
		for( int i = 0; i < 5; i++ )
		{
			System.out.println("结果取出");
			System.out.println("取出结果" + (i + 1) + completionService.take().get());
		}
		
		
		
	}
	/**
	 * ThreadLocal 以当前线程为key存入value,
	 * 每个线程只可取出自己的value
	 */
	public void ThreadLocalTest() {
		ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>();
		threadLocal.set(1);
		threadLocal.get();
	}
	
	/**
	 * 创建线程池
	 * 1、缓存线程池
	 * 2、创建自定义数量线程池
	 * 3、创建单个线程池
	 * 4、创建线程启动定时器
	 */
	public void ExecutorsTest() {
		
		// Executors 缓存线程池(有多少个任务就生成多少个线程)
		ExecutorService executorService = Executors.newCachedThreadPool();
		// Executors 创建有3个线程的线程池
		ExecutorService executorService2 = Executors.newFixedThreadPool(3);
		executorService2.shutdown();
		// Executors 创建单个线程的线程池(如果不小心挂了，会自动再生成一个线程)
		ExecutorService executorService3 = Executors.newSingleThreadExecutor();
		executorService3.shutdown();
		
		// 线程池使用(向execute方法中放入一个Runnable即可执行)
		executorService.execute(new Runnable() {
			
			public void run() {
				System.out.println("线程池调用线程");
			}
		});
		executorService.shutdown();
		
		// Executors 创建线程启动定时器
		ScheduledExecutorService executorService4 = Executors.newScheduledThreadPool(3);
		executorService4.schedule(new Runnable() {
			public void run() {
				System.out.println("线程启动定时器执行");
			}
		}, 2, TimeUnit.SECONDS);
		executorService4.shutdown();
	}
	/**
	 * 
	 * Callable 线程调用会有返回值
	 * 
	 * 单个返回值可放入Future<T>中
	 * 
	 * 多个返回值需要用CompletionService<T>来存储
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public void CallableTest() throws InterruptedException, ExecutionException {
		// Callable 线程会有返回值, 放入Future中, 执行的是call方法而不是run方法
		ExecutorService threadPool = Executors.newSingleThreadExecutor();//创建单个线程
		Future<String> future = threadPool.submit(
			new Callable<String>() {
				public String call() throws Exception {
					Thread.sleep(2000);
					return "success";
				}
			}
		);
		System.out.println( "拿到结果:" + future.get() );
		threadPool.shutdown();
		
		// Callable 线程会有多个返回值, 放入CompletionService中, 执行的是call方法而不是run方法 
		ExecutorService eService = Executors.newCachedThreadPool();// 创建缓存线程池
		CompletionService<Integer> completionService = 
				new ExecutorCompletionService<Integer>(eService);// 将线程池放进去
		// 循环执行5个线程，返回值分别放入completionService中
		for( int i = 1; i <= 5; i++ )
		{
			final int seq = 1;
			completionService.submit(
				new Callable<Integer>() {
					public Integer call() throws Exception {
						System.out.println("线程执行");
						Thread.sleep( new Random().nextInt(5000) );
						return seq;
					}
				}
			);
		}
		// 执行结束之后，取出线程中放入的值
		for( int i = 0; i < 5; i++ )
		{
			System.out.println("结果取出");
			System.out.println("取出结果" + (i + 1) + completionService.take().get());
		}
		eService.shutdown();
	}
	
	/**
	 * 
	 * 线程锁
	 * 
	 * 1、普通锁
	 * 
	 * 2、读写锁
	 * 
	 */
	public void LockTest() {
		
		// 线程锁(普通锁)
		Lock lock = new ReentrantLock();
		lock.lock();
		try {
			System.out.println("锁上了, 即将解锁");
		}catch(Exception e) {
			lock.unlock();
		}
		// 读写锁
		ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
		
		// 写锁
		readWriteLock.writeLock().lock();
		readWriteLock.writeLock().unlock();
		// 读锁
		readWriteLock.readLock().lock();
		readWriteLock.readLock().unlock();
	}
	/**
	 * 
	 * 条件阻塞Condition
	 * 
	 * 1、阻塞的时候所拥有的锁是被释放的
	 * 
	 * 2、再次唤醒的时候，需要重新获取锁才能继续执行
	 * 
	 * 3、如果需要多个线程互相唤醒，可定义多个condition
	 * 
	 * 4、例如：condition、conditionSub、conditionMain
	 * 
	 * 5、第一个线程执行完唤醒第二个线程：conditionSub.signal();
	 * 
	 * 6、第二个线程执行完唤醒第三个线程：conditionMain.signal();
	 * 
	 * wait、await的时候锁是被释放的
	 * 
	 * sleep的时候锁不被释放
	 * 
	 */
	public void ConditionTest() {
		
		// 首先定义一个锁
		Lock lock = new ReentrantLock();
		// Condition是在具体的lock之上的
		Condition condition = lock.newCondition();
		
		lock.lock();// 开启锁
		try {
			System.out.println("线程要被阻塞啦");
			condition.await();// 当前线程阻塞
			System.out.println("线程被唤醒啦");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		condition.signal();// 唤醒某一个阻塞线程
		lock.unlock();// 释放锁
		
	}
	
	/**
	 * 
	 * 线程同步工具Semaphore
	 * 
	 * 1、可限制资源访问量，定义的时候需要设置最大的许可集
	 * 
	 * 2、操作资源前需要先获取许可
	 * 
	 * 3、操作结束之后需要释放许可
	 * 
	 */
	public void SemaphoreTest() {
		
		// 创建缓存线程池
		ExecutorService service = Executors.newCachedThreadPool();
		// 创建Semaphore的信号量，设置最大并发数为3
		final Semaphore semaphore = new Semaphore(3);
		
		// 定义线程操作方法(多个线程的话，可在线程外部增加循环)
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					
					// 获取许可
					semaphore.acquire();
					System.out.println("获取许可之后操作数据");
					
					System.out.println("线程执行");
					// 释放许可
					semaphore.release();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		service.execute(runnable);
		
		service.shutdown();
	}
	
	/**
	 * 
	 * 线程同步工具CyclicBarrier
	 * 
	 * 1、设置等待线程数量
	 * 
	 * 2、只有等所有线程都到达了等待区域，才会继续进行
	 * 
	 * 3、可用于分散查询，等所有查询都结束之后，主线程再继续执行
	 * 
	 */
	public void CyclicBarrierTest() {
		
		ExecutorService executorService = Executors.newCachedThreadPool();
		
		// 设置2个线程要等待，都执行完了才能继续执行
		final CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
		
		System.out.println("初始化：当前有" + cyclicBarrier.getNumberWaiting() + "个线程在等待");
		
		for( int i = 0; i < 2; i++ )
		{
			Runnable runnable = new Runnable() {
				public void run() {
					try {
						
						System.out.println("第一次等待集合");
						System.out.println("初始化：当前有" + (cyclicBarrier.getNumberWaiting() + 1) + "个线程在等待");
						cyclicBarrier.await();
						
						System.out.println("第二次等待集合");
						System.out.println("初始化：当前有" + (cyclicBarrier.getNumberWaiting() + 1) + "个线程在等待");
						cyclicBarrier.await();
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (BrokenBarrierException e) {
						e.printStackTrace();
					}
				}
			};
			executorService.execute(runnable);
		}
		executorService.shutdown();
	}
	
	/**
	 * 
	 * 线程同步工具Exchanger:可用于互相交换数据
	 * 
	 * 1、只能两个线程来交换数据
	 * 
	 * 2、一个线程先进入exchange()之后会等待
	 * 
	 * 3、当第二个线程进入exchange()之后
	 * 
	 * 4、两个线程放入的数据会交换
	 * 
	 */
	public void ExchangerTest() {
		// 定义一个线程
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		// 定义一个交换对象，用来交换数据
		final Exchanger<Integer> exchanger = new Exchanger<Integer>();
		
		//开启一个线程执行任务
		executorService.execute(new Runnable(){ 
            public void run() { 
                try {
                	
                	System.out.println("子线程在放入数据");
                    Thread.sleep((long)(Math.random()*10000)); 

                    //把要交换的数据传到exchange方法中，然后被阻塞，等待另一个线程与之交换。返回交换后的数据
                    int data2 = exchanger.exchange(10); 
                    System.out.println("用2交换了主线程数据：" + data2);
                    
                }catch(Exception e){ 
                	
                } finally {
                	executorService.shutdown();
                }
            }
        });
		
		// 主线程放入数据2
		try {
			System.out.println("主线程在放入数据");
			int main = exchanger.exchange(2);
			System.out.println("用1交换了子线程数据：" + main);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 阻塞队列的应用
	 * 
	 * BlockingQueue<E> 阻塞队列接口
	 * 
	 * ArrayBlockingQueue<E> 基于数组实现的： 数组阻塞队列
	 * 
	 * LinkedBlockingQueue<E> 基于链表实现的：链表阻塞队列
	 * 
	 * PriorityBlockingQueue<E> 基于优先级队列：优先级阻塞队列
	 * 
	 * 1、首先会定义阻塞队列缓存区的大小，指明之后无法更改
	 * 
	 * 2、如果试图向已满队列放入元素会导致操作受阻塞
	 * 
	 * 3、如果试图从空队列中提取元素将导致类似阻塞
	 * 
	 */
	public void BlockQueueTest() {
		
		// 定义数组阻塞队列，设置缓存区为3，代表只有可存放3个数据
		final BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(3);
		
		try {
			
			// 可开启线程放入数据
			queue.put(1);
			
			// 可另外开启线程取数据
			queue.take();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}


```

