package com.palmap.library.utils;

import rx.Subscription;

/**
 * Created by 王天明 on 2016/12/22.
 */

public class SubscriptionUtils {

    public static void unsubscribeAll(Subscription... subscriptions) {
        if (null == subscriptions) {
            return;
        }
        for (Subscription s : subscriptions) {
            try {
                if (s != null && !s.isUnsubscribed()) {
                    s.unsubscribe();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
