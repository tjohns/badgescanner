package com.google.android.mwcnfc.widget;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.mwcnfc.MwcContact;
import com.google.android.mwcnfc.R;

public class CardFront extends Fragment {

	private MwcContact mContact;

	public CardFront() {

	}

	public CardFront(MwcContact contact) {
		mContact = contact;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.card_front, container, false);
		if (mContact != null) {
			((TextView) v.findViewById(R.id.name)).setText(mContact.firstName
					+ " " + mContact.lastName);
			((TextView) v.findViewById(R.id.company)).setText(mContact.company);
			((TextView) v.findViewById(R.id.phone)).setText(mContact.phone);
			((TextView) v.findViewById(R.id.email)).setText(mContact.email);
		}
		return v;
	}

}
