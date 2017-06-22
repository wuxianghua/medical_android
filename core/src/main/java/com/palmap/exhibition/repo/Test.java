package com.palmap.exhibition.repo;


import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by 王天明 on 2017/3/27.
 */

public class Test {


    public static void main(String[] args) {
        Observable.timer(2, TimeUnit.MICROSECONDS).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                System.out.println(o.toString());
            }
        });
        while (true){}
    }

}
