package com.hl.utils.base;

import android.support.v4.app.Fragment;

import com.hl.utils.UmengUtils;

public class AppBaseFragment extends Fragment {

	@Override
	public void onPause() {
		super.onPause();
		UmengUtils.fragmentPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		UmengUtils.fragmentResume();
	}

}
