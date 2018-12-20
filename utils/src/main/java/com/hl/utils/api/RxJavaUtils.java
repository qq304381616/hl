package com.hl.utils.api;


import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * 四种线程：
 * Schedulers.io(): I/O 操作（读写文件、数据库、网络请求等），与newThread()差不多，区别在于io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 效率比 newThread() 更高。值得注意的是，在 io() 下，不要进行大量的计算，以免产生不必要的线程；
 * Schedulers.newThread(): 开启新线程操作；
 * Schedulers.immediate(): 默认指定的线程，也就是当前线程；
 * Schedulers.computation():计算所使用的调度器。这个计算指的是 CPU 密集型计算，即不会被 I/O等操作限制性能的操作，例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。值得注意的是，不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU；
 * AndroidSchedulers.mainThread(): Rxndroid 扩展的 Android 主线程；
 * <p>
 * 对于线程还需要注意
 * create() , just() , from()   等              --  事件产生 ：默认运行在当前线程，可以由 subscribeOn() 自定义线程
 * map() , flapMap() , scan() , filter()  等    --  事件加工 ：默认跟事件产生的线程保持一致, 可由 observeOn() 自定义线程
 * subscribe()                                  --  事件消费 ：默认运行在当前线程，可以有observeOn() 自定义
 */
public class RxJavaUtils {

    public static void main(String[] args) {
        // 1 2 3  三种只在当前线程运行的例子。
//        test_1();
//        test_2();
//        test_3();
        // 4 关于线程调用的例子
//        test_4();
        // 特殊处理
//        test_map();
//        test_zip();
//        test_merge();
//        test_from();
//        test_from_filter();
//        test_from_take();
        test_from_take_onOnNext();

    }

    private static void test_1() {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                System.out.println("subscribe 3 .........");
                e.onNext("hello");
                e.onNext(error());
                e.onComplete();
                e.onNext("hello2"); // 不执行
            }
        });

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("onSubscribe 2 .........");
            }

            @Override
            public void onNext(@NonNull String s) {
                System.out.println("onNext 4 ........." + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("onError ........." + e);
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete 5 .........");
            }
        };

        System.out.println("subscribe 1 .........");
        observable.subscribe(observer);
    }

    private static void test_2() {
        Observable<String> observable = Observable.just("hello");
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println(s);
            }
        };
        Disposable subscribe = observable.subscribe(consumer);
        System.out.println(subscribe);
    }

    private static void test_3() {
        Observable<String> observable = Observable.just("hello");
        Action onCompleteAction = new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("complete");
            }
        };
        Consumer<String> onNextConsumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("accept " + s);
            }
        };
        Consumer<Throwable> onErrorConsumer = new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("error " + throwable);
            }
        };
        Disposable subscribe = observable.subscribe(onNextConsumer, onErrorConsumer, onCompleteAction);
    }

    private static void test_4() {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                System.out.println("Observable thread is : " + Thread.currentThread().getName());
                System.out.println("ObservableEmitter " + emitter);
                emitter.onNext(1);
            }
        });

        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("Observer thread is : " + Thread.currentThread().getName());
                System.out.println("onNext " + integer);
            }
        };

        Disposable subscribe = observable.subscribeOn(Schedulers.newThread())  // 子线程
                .observeOn(AndroidSchedulers.mainThread())  // 主线程
                .subscribe(consumer);
    }

    private static String error() throws Exception {
        throw new Exception();
    }

    // map 方法 转换数据
    private static void test_map() {
        Disposable subscribe = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) {
                System.out.println("map " + integer);
                return "This is result " + integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println("onNext " + s);
            }
        });
    }

    // zip() 合并数据。 onNext会合并o1和o2的数据
    private static void test_zip() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                System.out.println("emitter " + 1);
                emitter.onNext(1);
                System.out.println("emitter " + 2);
                emitter.onNext(2);
                System.out.println("emitter " + 3);
                emitter.onNext(3);
                System.out.println("emitter " + 4);
                emitter.onNext(4);
                System.out.println("emitter complete1");
                emitter.onComplete();
            }
        });

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                System.out.println("emitter A");
                emitter.onNext("A");
                System.out.println("emitter B");
                emitter.onNext("B");
                System.out.println("emitter C");
                emitter.onNext("C");
                System.out.println("emitter complete2");
                emitter.onComplete();
            }
        });

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("onSubscribe");
            }

            @Override
            public void onNext(String value) {
                System.out.println("onNext " + value);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError");
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }

    // 等待两个Observable 执行完
    private static void test_merge() {
        Observable<String> observable1 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("haha");
            }
        }).subscribeOn(Schedulers.newThread());

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("hehe");
            }
        }).subscribeOn(Schedulers.newThread());

        Observable.merge(observable1, observable2)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String value) {
                        System.out.println("value" + value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    // from：分为 fromArray, fromIterable, fromFuture // 只能走主线程。耗时操作不适用
    private static void test_from() {
        Disposable subscribe = Observable.fromArray(new Integer[]{1, 2, 3, 4, 5}).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("Integer" + integer);
            }
        });
    }

    // from filter 过滤
    private static void test_from_filter() {
        Disposable subscribe = Observable.fromArray(new Integer[]{1, 2, 3, 4, 5})
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        // 偶数返回true，则表示剔除奇数，留下偶数
                        return integer % 2 == 0;

                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("Integer" + integer);
                    }
                });
    }

    // from take 保留事件数。
    private static void test_from_take() {
        Observable.just("1", "2", "6", "3", "4", "5").take(2).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String integer) {
                System.out.println("Integer" + integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    // from doOnNext 处理下个事件前执行的操作
    private static void test_from_take_onOnNext() {
        Observable.fromArray(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}).filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                // 偶数返回true，则表示剔除奇数
                return integer % 2 == 0;
            }
        })// 最多保留三个，也就是最后剩三个偶数
                .take(3).doOnNext(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                // 在输出偶数之前输出它的hashCode
                System.out.println("hahcode = " + integer.hashCode() + "");
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer value) {
                System.out.println("number = " + value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

}

