package nc.opt.mobile.optmobile.glide;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.support.annotation.DrawableRes;

import com.bumptech.glide.RequestBuilder;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by 2761oli on 27/12/2017.
 */

public class GlideRequester {

    private GlideRequester() {
    }

    public static RequestBuilder<PictureDrawable> getSvgRequester(Context context, @DrawableRes int onError, @DrawableRes int placeholder) {
        return GlideApp.with(context)
                .as(PictureDrawable.class)
                .placeholder(placeholder)
                .error(onError)
                .transition(withCrossFade())
                .listener(new SvgSoftwareLayerSetter());
    }
}
