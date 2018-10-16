package nc.opt.mobile.optmobile;

import android.util.Log;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        Observable.zip(
                Observable.interval(2, TimeUnit.SECONDS),
                Observable.just(list).flatMapIterable(integers -> integers),
                (aLong, integer) -> integer)
                .subscribe(integer -> System.out.println("HELLO " + integer.toString()),
                        throwable -> Log.e("HELLO", throwable.getMessage(), throwable),
                        () -> System.out.println("HELLO OH PUTAIN"));

        Thread.sleep(50000);
    }
}