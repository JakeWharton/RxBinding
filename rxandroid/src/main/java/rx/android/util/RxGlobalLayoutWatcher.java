package rx.android.util;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.util.SparseArrayCompat;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.HandlerSchedulers;
import rx.functions.Action0;

/**
 * Created by gokhanbarisaker on 4/22/15.
 */
public class RxGlobalLayoutWatcher
{
    // == Variables ================================================================================

    public static final Objective OBJECTIVE_NONZEROAREA = new Objective() {
        @Override
        public boolean accomplished(View view) {
            int measuredWidth = view.getMeasuredWidth();
            int measuredHeight = view.getMeasuredHeight();

            return !(measuredHeight == 0 && measuredWidth == 0);
        }
    };

    private static RxGlobalLayoutWatcher sharedInstance;
    private SparseArrayCompat<OnGlobalLayoutRemovableListener> onGlobalLayoutRemovableListeners = new SparseArrayCompat<>();


    // == Constructors && Singleton instance providers =============================================

    public static RxGlobalLayoutWatcher instance()
    {
        synchronized (RxGlobalLayoutWatcher.class)
        {
            if (sharedInstance == null)
            {
                sharedInstance = new RxGlobalLayoutWatcher();
            }
        }

        return sharedInstance;
    }


    // == Tools ====================================================================================

    public Observable<View> addGlobalLayoutWatch(final View view, final Mission mission)
    {
        return Observable.create(new Observable.OnSubscribe<View>() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void call(final Subscriber<? super View> subscriber) {

                int measuredWidth = view.getMeasuredWidth();
                int measuredHeight = view.getMeasuredHeight();

                boolean laidOut = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) ? (view.isLaidOut()) : (!(measuredHeight == 0 && measuredWidth == 0));

                if (laidOut && mission.accomplished(view)) {
                    // The view is already measured, possibly laid out & accomplished given mission
                    // Perform callback asap

                    subscriber.onNext(view);
                    subscriber.onCompleted();
                } else {
                    // Add a wait request, till view measurements are done
                    OnGlobalLayoutRemovableListener imageViewOnGlobalLayoutRemovableListener = onGlobalLayoutRemovableListeners.get(view.hashCode());

                    if (imageViewOnGlobalLayoutRemovableListener != null) {
                        imageViewOnGlobalLayoutRemovableListener.remove(view.getViewTreeObserver());
                    }

                    imageViewOnGlobalLayoutRemovableListener = new OnGlobalLayoutRemovableListener() {
                        @TargetApi(Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onGlobalLayout() {
                            if (mission.accomplished(view)) {
                                remove(view.getViewTreeObserver());
                                onGlobalLayoutRemovableListeners.remove(view.hashCode());

                                if (!subscriber.isUnsubscribed()) {
                                    // Perform callback
                                    subscriber.onNext(view);
                                    subscriber.onCompleted();
                                }
                            }
                        }
                    };

                    onGlobalLayoutRemovableListeners.append(view.hashCode(), imageViewOnGlobalLayoutRemovableListener);
                    view.getViewTreeObserver().addOnGlobalLayoutListener(imageViewOnGlobalLayoutRemovableListener);
                }
            }
        }).doOnUnsubscribe(new Action0() {
            @Override
            public void call() {
                removeGlobalLayoutWatch(view);
            }
        }).observeOn(HandlerSchedulers.mainThread()).subscribeOn(HandlerSchedulers.mainThread());
    }

    private void removeGlobalLayoutWatch(View view)
    {
        OnGlobalLayoutRemovableListener onGlobalLayoutRemovableListener = onGlobalLayoutRemovableListeners.get(view.hashCode());

        if (onGlobalLayoutRemovableListener != null)
        {
            onGlobalLayoutRemovableListener.remove(view.getViewTreeObserver());
            onGlobalLayoutRemovableListeners.remove(view.hashCode());
        }
    }


    // == Minions ==================================================================================

    public static class Mission
    {
        private List<Objective> objectives = null;

        public Mission(Objective... objectives)
        {
            this.objectives = Arrays.asList(objectives);
        }

        public boolean accomplished(View view)
        {
            for (Objective objective : objectives)
            {
                if (!objective.accomplished(view))
                {
                    return false;
                }
            }

            return true;
        }
    }

    public interface Objective
    {
        boolean accomplished(View view);
    }

    public abstract class OnGlobalLayoutRemovableListener implements ViewTreeObserver.OnGlobalLayoutListener{
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public void remove(ViewTreeObserver observer)
        {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
            {
                observer.removeGlobalOnLayoutListener(this);
            }
            else
            {
                observer.removeOnGlobalLayoutListener(this);
            }
        }
    }
}