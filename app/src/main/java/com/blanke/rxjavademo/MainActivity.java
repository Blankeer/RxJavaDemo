package com.blanke.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.init().methodCount(5);

//        Observable.range(2, 5).subscribe(new Action1<Integer>() {
//            @Override
//            public void call(Integer integer) {
//                Logger.e("range", integer);
//            }
//        });
//        final Subscription timeSub = Observable.interval(1000, TimeUnit.MILLISECONDS).subscribe(new Action1<Long>() {
//            @Override
//            public void call(Long aLong) {
//                Logger.e("interval", aLong);
//
//            }
//        });
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                timeSub.unsubscribe();
//            }
//        }.start();
//
        Observable.timer(3000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Logger.d("test");
                    }
                });
//
//        Observable.range(0, 20).filter(new Func1<Integer, Boolean>() {
//            @Override
//            public Boolean call(Integer integer) {
//                return integer % 2 == 0;
//            }
//        }).subscribe(new Action1<Integer>() {
//            @Override
//            public void call(Integer integer) {
//                Logger.e("filter", integer);
//            }
//        });

//        Observable.range(2, 15).concatMap(new Func1<Integer, Observable<Integer>>() {
//            @Override
//            public Observable<Integer> call(final Integer integer) {
////                Logger.e(integer);
//                return Observable.just(integer * integer).subscribeOn(Schedulers.newThread())
//                        .doOnNext(new Action1<Integer>() {
//                            @Override
//                            public void call(Integer integer) {
//                                try {
//                                    Thread.sleep(new Random().nextInt(3000));
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//            }
//        }).subscribe(new Action1<Integer>() {
//            @Override
//            public void call(Integer integer) {
//                Logger.e(integer);
//            }
//        });
        Observable.concat(getLocalData(), getNetData())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String strings) {
                        Logger.e("concat " + strings);
                    }
                });
        Observable.merge(getLocalData(), getNetData())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String strings) {
                        Logger.e("merge " + strings);
                    }
                });

    }

    /**
     * local data
     *
     * @return
     */
    private Observable<String> getLocalData() {
        return Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("local " + i);
                }
                subscriber.onNext(list);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread()).flatMap(new Func1<List<String>, Observable<String>>() {
            @Override
            public Observable<String> call(List<String> strings) {
                return Observable.from(strings);
            }
        });
    }

    /**
     * net data
     *
     * @return
     */
    private Observable<String> getNetData() {
        return Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < 3; i++) {
                    list.add("network " + i);
                }
                subscriber.onNext(list);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread()).flatMap(new Func1<List<String>, Observable<String>>() {
            @Override
            public Observable<String> call(List<String> strings) {
                return Observable.from(strings);
            }
        });
    }
}