package com.example.ivy.rxandroiddemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private Button btTest;
    private TextView tvShow;
    private Observable<String> observable;
    private Observer<String> observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btTest = (Button) findViewById(R.id.button);
        tvShow = (TextView) findViewById(R.id.textView);


        //1. 创建Observe或Subscriber

        observer = new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.d("main", s);
                tvShow.setText(s);
            }
        };

        // 2. 创建Observable (just,from)

        observable = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("first");
//                subscriber.onNext("second");
//                subscriber.onNext("third");
                subscriber.onCompleted();
            }
        });

        String[] words = {"a", "b", "c"};
        Observable<String> observable1 = Observable.from(words);
        Observable<String> observable2 = Observable.just("e", "f", "g");

        //3. 订阅Subscribe

        //observable.subscribe(observer);


        btTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                observable.subscribe(observer);
            }
        });


        // 其他


        //-----------基本-使用示例:---------------------
        Observable.just("Hello,Rx1").subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                //将在调用者线程执行
                Log.d("main", "Rx1--" + s + ",threadId:" + Thread.currentThread().getId());
            }
        });


        Observable.just("Hello,Rx2")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        //将在主线程执行
                        Log.d("main", "Rx2--" + s + ",threadId:" + Thread.currentThread().getId());
                    }
                });

        Observable.just("Hello,Rx3")
                .observeOn(Schedulers.newThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        //将在新线程执行
                        Log.d("main", "Rx3--" + s + ",threadId:" + Thread.currentThread().getId());
                    }
                });


        //----------前台调用-后台执行-----------

        Observable.just("Hello,Rx4")
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Log.d("main", "Rx4--threadId:" + Thread.currentThread().getId());
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        //将在新线程执行
                        Log.d("main", "Rx4--" + s + ",threadId:" + Thread.currentThread().getId());
                    }
                });


    }
}
