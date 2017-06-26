package com.palmap.exhibition.repo;


import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;


/**
 * Created by 王天明 on 2017/3/27.
 */

public class Test {


    public static void main(String[] args) {
        Observable.timer(2, TimeUnit.MICROSECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) {
                System.out.println(o.toString());
            }
        });
        while (true){}
    }

}
