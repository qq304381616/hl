package com.hl.knowledge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.SharedElementCallback;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.hl.utils.LogUtils;

import java.io.FileDescriptor;
import java.io.PrintWriter;

/**
 * Created by HL on 2017/9/13.
 */

public class MyFragment extends Fragment {

    private static final String TAG = MyFragment.class.getSimpleName();

    public MyFragment() {
        super();
    }

    @Override
    public String toString() {
        LogUtils.e(TAG, "Fragment 生命周期：" + "toString");
        return super.toString();
    }

    @Override
    public void setArguments(Bundle args) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "setArguments");
        super.setArguments(args);
    }

    @Override
    public void setInitialSavedState(SavedState state) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "setInitialSavedState");
        super.setInitialSavedState(state);
    }

    @Override
    public void setTargetFragment(Fragment fragment, int requestCode) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "setTargetFragment");
        super.setTargetFragment(fragment, requestCode);
    }

    @Override
    public Context getContext() {
        LogUtils.e(TAG, "Fragment 生命周期：" + "getContext");
        return super.getContext();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onHiddenChanged " + hidden);
        super.onHiddenChanged(hidden);
    }

    @Override
    public void setRetainInstance(boolean retain) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "setRetainInstance");
        super.setRetainInstance(retain);
    }

    @Override
    public void setHasOptionsMenu(boolean hasMenu) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "setHasOptionsMenu");
        super.setHasOptionsMenu(hasMenu);
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "setMenuVisibility");
        super.setMenuVisibility(menuVisible);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "setUserVisibleHint");
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public boolean getUserVisibleHint() {
        LogUtils.e(TAG, "Fragment 生命周期：" + "getUserVisibleHint");
        return super.getUserVisibleHint();
    }

    @Override
    public LoaderManager getLoaderManager() {
        LogUtils.e(TAG, "Fragment 生命周期：" + "getLoaderManager");
        return super.getLoaderManager();
    }

    @Override
    public void startActivity(Intent intent) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "startActivity");
        super.startActivity(intent);
    }

    @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "startActivity");
        super.startActivity(intent, options);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "startActivityForResult");
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "startActivityForResult");
        super.startActivityForResult(intent, requestCode, options);
    }

    @Override
    public void startIntentSenderForResult(IntentSender intent, int requestCode, @Nullable Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {
        LogUtils.e(TAG, "Fragment 生命周期：" + "startIntentSenderForResult");
        super.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onRequestPermissionsResult");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "shouldShowRequestPermissionRationale");
        return super.shouldShowRequestPermissionRationale(permission);
    }

    @Override
    public LayoutInflater getLayoutInflater(Bundle savedInstanceState) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "getLayoutInflater");
        return super.getLayoutInflater(savedInstanceState);
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onInflate");
        super.onInflate(context, attrs, savedInstanceState);
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onInflate");
        super.onInflate(activity, attrs, savedInstanceState);
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onAttachFragment");
        super.onAttachFragment(childFragment);
    }

    @Override
    public void onAttach(Context context) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onAttach");
        super.onAttach(context);
    }

    @Override
    public void onAttach(Activity activity) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onAttach");
        super.onAttach(activity);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onCreateAnimation");
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View getView() {
        LogUtils.e(TAG, "Fragment 生命周期：" + "getView");
        return super.getView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onStart() {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onResume");
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onMultiWindowModeChanged");
        super.onMultiWindowModeChanged(isInMultiWindowMode);
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onPictureInPictureModeChanged");
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onPause() {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onStop");
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onDetach");
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onPrepareOptionsMenu");
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onDestroyOptionsMenu() {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onDestroyOptionsMenu");
        super.onDestroyOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onOptionsItemSelected");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onOptionsMenuClosed");
        super.onOptionsMenuClosed(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onCreateContextMenu");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public void registerForContextMenu(View view) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "registerForContextMenu");
        super.registerForContextMenu(view);
    }

    @Override
    public void unregisterForContextMenu(View view) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "unregisterForContextMenu");
        super.unregisterForContextMenu(view);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "onContextItemSelected");
        return super.onContextItemSelected(item);
    }

    @Override
    public void setEnterSharedElementCallback(SharedElementCallback callback) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "setEnterSharedElementCallback");
        super.setEnterSharedElementCallback(callback);
    }

    @Override
    public void setExitSharedElementCallback(SharedElementCallback callback) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "setExitSharedElementCallback");
        super.setExitSharedElementCallback(callback);
    }

    @Override
    public void setEnterTransition(Object transition) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "setEnterTransition");
        super.setEnterTransition(transition);
    }

    @Override
    public Object getEnterTransition() {
        LogUtils.e(TAG, "Fragment 生命周期：" + "getEnterTransition");
        return super.getEnterTransition();
    }

    @Override
    public void setReturnTransition(Object transition) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "setReturnTransition");
        super.setReturnTransition(transition);
    }

    @Override
    public Object getReturnTransition() {
        LogUtils.e(TAG, "Fragment 生命周期：" + "getReturnTransition");
        return super.getReturnTransition();
    }

    @Override
    public void setExitTransition(Object transition) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "setExitTransition");
        super.setExitTransition(transition);
    }

    @Override
    public Object getExitTransition() {
        LogUtils.e(TAG, "Fragment 生命周期：" + "getExitTransition");
        return super.getExitTransition();
    }

    @Override
    public void setReenterTransition(Object transition) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "setReenterTransition");
        super.setReenterTransition(transition);
    }

    @Override
    public Object getReenterTransition() {
        LogUtils.e(TAG, "Fragment 生命周期：" + "getReenterTransition");
        return super.getReenterTransition();
    }

    @Override
    public void setSharedElementEnterTransition(Object transition) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "setSharedElementEnterTransition");
        super.setSharedElementEnterTransition(transition);
    }

    @Override
    public Object getSharedElementEnterTransition() {
        LogUtils.e(TAG, "Fragment 生命周期：" + "getSharedElementEnterTransition");
        return super.getSharedElementEnterTransition();
    }

    @Override
    public void setSharedElementReturnTransition(Object transition) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "setSharedElementReturnTransition");
        super.setSharedElementReturnTransition(transition);
    }

    @Override
    public Object getSharedElementReturnTransition() {
        LogUtils.e(TAG, "Fragment 生命周期：" + "getSharedElementReturnTransition");
        return super.getSharedElementReturnTransition();
    }

    @Override
    public void setAllowEnterTransitionOverlap(boolean allow) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "setAllowEnterTransitionOverlap");
        super.setAllowEnterTransitionOverlap(allow);
    }

    @Override
    public boolean getAllowEnterTransitionOverlap() {
        LogUtils.e(TAG, "Fragment 生命周期：" + "getAllowEnterTransitionOverlap");
        return super.getAllowEnterTransitionOverlap();
    }

    @Override
    public void setAllowReturnTransitionOverlap(boolean allow) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "setAllowReturnTransitionOverlap");
        super.setAllowReturnTransitionOverlap(allow);
    }

    @Override
    public boolean getAllowReturnTransitionOverlap() {
        LogUtils.e(TAG, "Fragment 生命周期：" + "getAllowReturnTransitionOverlap");
        return super.getAllowReturnTransitionOverlap();
    }

    @Override
    public void postponeEnterTransition() {
        LogUtils.e(TAG, "Fragment 生命周期：" + "postponeEnterTransition");
        super.postponeEnterTransition();
    }

    @Override
    public void startPostponedEnterTransition() {
        LogUtils.e(TAG, "Fragment 生命周期：" + "startPostponedEnterTransition");
        super.startPostponedEnterTransition();
    }

    @Override
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        LogUtils.e(TAG, "Fragment 生命周期：" + "dump");
        super.dump(prefix, fd, writer, args);
    }
}
