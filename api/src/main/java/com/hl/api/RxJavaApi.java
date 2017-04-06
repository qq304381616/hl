package com.hl.api;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hl on 2017/4/6.
 * just: 获取输入数据, 直接分发, 更加简洁, 省略其他回调.
 * from: 获取输入数组, 转变单个元素分发.
 * map: 映射, 对输入数据进行转换, 如大写.
 * flatMap: 增大, 本意就是增肥, 把输入数组映射多个值, 依次分发.
 * reduce: 简化, 正好相反, 把多个数组的值, 组合成一个数据.
 */

public class RxJavaApi {

    private static final String LOG_TAG = "RxJavaTestActivity";


    private void test() {

        Observable<String> myObservable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> sub) {
                        sub.onNext("Hello, world!");
                        sub.onCompleted();
                    }
                }
        );

        Subscriber<String> mySubscriber = new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                System.out.println(s);
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }
        };

        myObservable.subscribe(mySubscriber);

        ///////////////////////////////////////////////////////////////////////////////////////////////
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onNext(String s) {
                Log.d(LOG_TAG, "Item: " + s);
            }

            @Override
            public void onCompleted() {
                Log.d(LOG_TAG, "Completed!");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(LOG_TAG, "Error!");
            }
        };
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                Log.d(LOG_TAG, "Item: " + s);
            }

            @Override
            public void onCompleted() {
                Log.d(LOG_TAG, "Completed!");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(LOG_TAG, "Error!");
            }
        };
        ///////////////////////////////////////////////////////////////////////////////////////////////

        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("Hi");
                subscriber.onNext("Aloha");
                subscriber.onCompleted();
            }
        });

        Observable observable2 = Observable.just("Hello", "Hi", "Aloha");
        // 将会依次调用：
        // onNext("Hello");
        // onNext("Hi");
        // onNext("Aloha");
        // onCompleted();

        String[] words = {"Hello", "Hi", "Aloha"};
        Observable observable3 = Observable.from(words);
        // 将会依次调用：
        // onNext("Hello");
        // onNext("Hi");
        // onNext("Aloha");
        // onCompleted();
        ///////////////////////////////////////////////////////////////////////////////////////////////

        observable.subscribe(observer);
        // 或者：
        observable.subscribe(subscriber);

        ///////////////////////////////////////////////////////////////////////////////////////////////

        Action1<String> onNextAction = new Action1<String>() {
            // onNext()
            @Override
            public void call(String s) {
                Log.d(LOG_TAG, s);
            }
        };
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            // onError()
            @Override
            public void call(Throwable throwable) {
                // Error handling
            }
        };
        Action0 onCompletedAction = new Action0() {
            // onCompleted()
            @Override
            public void call() {
                Log.d(LOG_TAG, "completed");
            }
        };

        // 自动创建 Subscriber ，并使用 onNextAction 来定义 onNext()
        observable.subscribe(onNextAction);
        // 自动创建 Subscriber ，并使用 onNextAction 和 onErrorAction 来定义 onNext() 和 onError()
        observable.subscribe(onNextAction, onErrorAction);
        // 自动创建 Subscriber ，并使用 onNextAction、 onErrorAction 和 onCompletedAction 来定义 onNext()、 onError() 和 onCompleted()
        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);

        ///////////////////////////////////////////////////////////////////////////////////////////////

        int drawableRes = 1;
        final ImageView imageView = null;
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = null;
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        }).subscribe(new Observer<Drawable>() {
            @Override
            public void onNext(Drawable drawable) {
                imageView.setImageDrawable(drawable);
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }
        });

        ///////////////////////////////////////////////////////////////////////////////////////////////

        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer number) {
                        Log.d(LOG_TAG, "number:" + number);
                    }
                });

        mapTest();
        flatMapTest();
    }

    /**
     * 变换 MAP
     */
    private void mapTest() {
        Observable.just("images/logo.png") // 输入类型 String
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String filePath) { // 参数类型 String
//                        return getBitmapFromPath(filePath); // 返回类型 Bitmap
                        return null;
                    }
                })
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) { // 参数类型 Bitmap
//                        showBitmap(bitmap);
                    }
                });
    }

    private void flatMapTest() {
//        Student[] students = ...;
//        Subscriber<Course> subscriber = new Subscriber<Course>() {
//            @Override
//            public void onNext(Course course) {
//                Log.d(tag, course.getName());
//            }
//            ...
//        };
//        Observable.from(students)
//                .flatMap(new Func1<Student, Observable<Course>>() {
//                    @Override
//                    public Observable<Course> call(Student student) {
//                        return Observable.from(student.getCourses());
//                    }
//                })
//                .subscribe(subscriber);
    }

}
