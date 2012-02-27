package com.google.android.mwcnfc.widget;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.mwcnfc.R;

public class CardBack extends Fragment {

	public CardBack() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.card_back, container, false);
		
		
		// by default the card back is not visible, set it up so that it's ready to animate in
		v.setRotationY(-90);
		v.setVisibility(View.GONE);
		return v;
	}

}
