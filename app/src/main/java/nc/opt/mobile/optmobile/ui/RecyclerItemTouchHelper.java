package nc.opt.mobile.optmobile.ui;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import nc.opt.mobile.optmobile.ui.adapter.ColisAdapter;


/**
 * Created by 2761oli on 28/12/2017.
 */

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private SwipeListener listener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, SwipeListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwipe(viewHolder, direction);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            final View foregroundView = ((ColisAdapter.ViewHolderColisAdapter) viewHolder).getmConstraintDetailColisLayout();
            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        getDefaultUIUtil().onDrawOver(c, recyclerView, viewHolder.itemView, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((ColisAdapter.ViewHolderColisAdapter) viewHolder).getmConstraintDetailColisLayout();
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        final ColisAdapter.ViewHolderColisAdapter vh = (ColisAdapter.ViewHolderColisAdapter) viewHolder;
        final View foregroundView = vh.getmConstraintDetailColisLayout();
        final View deletingView = vh.getmConstraintDeletedLayout();
        final View deliveringView = vh.getmConstraintDeliveredLayout();

        if (dX > 0) {
            deletingView.setVisibility(View.GONE);
            deliveringView.setVisibility(View.VISIBLE);
        } else {
            deletingView.setVisibility(View.VISIBLE);
            deliveringView.setVisibility(View.GONE);
        }

        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
    }

    public interface SwipeListener {
        void onSwipe(RecyclerView.ViewHolder view, int direction);
    }
}