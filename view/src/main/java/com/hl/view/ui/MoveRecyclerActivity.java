package com.hl.view.ui;

import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.hl.base.BaseActivity;
import com.hl.base.BaseConstant;
import com.hl.base.adapter.BaseRecyclerAdapter;
import com.hl.view.R;

/**
 * 移动位置
 */
public class MoveRecyclerActivity extends BaseActivity {

    private BaseRecyclerAdapter adapter;


    //为RecycleView绑定触摸事件
    private ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            //首先回调的方法 返回int表示是否监听该方向
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;//拖拽
            int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;//侧滑删除
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        }

        @Override
        public boolean isLongPressDragEnabled() {
            //是否可拖拽
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            // 是否可滑动删除
            return false;
        }

        /**
         * 按下时 震动 + 动画
         */
        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE); //获取系统震动服务
                if (vib != null) vib.vibrate(70);    //震动70毫秒
                ScaleAnimation animation = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(200);
                animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
                viewHolder.itemView.setAnimation(animation);
                animation.startNow();
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        /**
         * 手指松开的时候还原
         */
        @Override
        public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            ScaleAnimation animation = new ScaleAnimation(1.1f, 1.0f, 1.1f, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(200);
            animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
            viewHolder.itemView.setAnimation(animation);
            animation.startNow();
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_recycler);
        initToolbar(true);

        RecyclerView rv_base = findViewById(R.id.rv_base);

        adapter = new BaseRecyclerAdapter(this);
        rv_base.setLayoutManager(new GridLayoutManager(this, 4));
        rv_base.setAdapter(adapter);

        adapter.setData(BaseConstant.getData(30));
        adapter.notifyDataSetChanged();

        helper.attachToRecyclerView(rv_base);
    }
}
