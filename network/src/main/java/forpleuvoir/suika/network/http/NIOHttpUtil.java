package forpleuvoir.suika.network.http;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * 非阻塞的Http工具类
 *
 * @author forpleuvoir
 * <p>#project_name suika
 * <p>#package forpleuvoir.suika.network.http
 * <p>#class_name NIOHttpUtil
 * <p>#create_time 2021/5/31 21:48
 */
public class NIOHttpUtil {
    public static final String THREAD_NAME = "Suika-Http-Thread";
    private static final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    static {
        executor.setThreadFactory(new NamedThreadFactory(THREAD_NAME));
    }

    private static void execute(Runnable task) {
        executor.execute(task);
    }

    /**
     * 发送一个Get请求
     *
     * @param uri      链接地址
     * @param params   参数 k:v
     * @param headers  请求头 k:v
     * @param consumer 执行器
     * @see HttpUtil#get(String, Map, Map)
     */
    public static void get(String uri, Map<String, Object> params, Map<String, String> headers,
                           Consumer<Result> consumer
    ) {
        execute(() -> {
            consumer.accept(HttpUtil.get(uri, params, headers));
        });
    }


    /**
     * 发送一个DELETE请求
     *
     * @param uri     链接地址
     * @param params  参数 k:v
     * @param headers 请求头 k:v
     * @see HttpUtil#delete(String, Map, Map)
     */
    public static void delete(String uri, Map<String, Object> params, Map<String, String> headers,
                                Consumer<Result> consumer
    ) {
        execute(() -> {
            consumer.accept(HttpUtil.delete(uri, params, headers));
        });
    }


    /**
     * 发送一个Post请求
     *
     * @param uri     链接地址
     * @param params  参数 Request.Body
     * @param headers 请求头 k:v
     * @see HttpUtil#post(String, String, Map)
     */
    public static void post(String uri, @NonNull String params, Map<String, String> headers, Consumer<Result> consumer
    ) {
        execute(() -> {
            execute(() -> {
                consumer.accept(HttpUtil.post(uri, params, headers));
            });
        });
    }


    /**
     * 发送一个Put请求
     *
     * @param uri     链接地址
     * @param params  参数 Request.Body
     * @param headers 请求头 k:v
     * @see HttpUtil#put(String, String, Map)
     */
    public static void put(String uri, @NonNull String params, Map<String, String> headers, Consumer<Result> consumer) {
        execute(() -> {
            execute(() -> {
                consumer.accept(HttpUtil.put(uri, params, headers));
            });
        });
    }


    private static class NamedThreadFactory implements ThreadFactory {

        private final ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String threadPrefix;

        public NamedThreadFactory(String threadPrefix) {
            this.threadPrefix = threadPrefix;
        }

        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            Thread thread = defaultThreadFactory.newThread(runnable);
            thread.setName(threadPrefix + "-" + threadNumber);
            return thread;
        }
    }

}
