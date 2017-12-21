package nc.opt.mobile.optmobile.service;

import android.support.test.runner.AndroidJUnit4;

import com.google.common.collect.Lists;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.TestScheduler;

/**
 * Created by 2761oli on 07/11/2017.
 */
@RunWith(AndroidJUnit4.class)
public class RxJavaTest {

    @Test
    public void switchMap() throws Exception {
        final List<String> items = Lists.newArrayList("a", "b", "c", "d", "e", "f");

        final TestScheduler scheduler = new TestScheduler();

        Observable.fromIterable(items)
                .switchMap(s -> {
                    final int delay = new Random().nextInt(10);
                    return Observable.just(s + "x")
                            .delay(delay, TimeUnit.SECONDS, scheduler);
                })
                .toList()
                .doOnSubscribe(System.out::println)
                .subscribe();

        scheduler.advanceTimeBy(1, TimeUnit.MINUTES);
    }
}

