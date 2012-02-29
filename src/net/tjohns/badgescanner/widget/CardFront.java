// Copyright 2012 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package net.tjohns.badgescanner.widget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.tjohns.badgescanner.MwcContact;
import net.tjohns.badgescanner.R;

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
