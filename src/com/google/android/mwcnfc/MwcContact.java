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

package com.google.android.mwcnfc;

import java.lang.String;public class MwcContact {

	public String firstName;
	public String lastName;
	public String jobTitle;
	public String company;
	public String email;
	public String phone;

	public MwcContact(String firstName, String lastName, String jobTitle,
			String company, String email, String phone) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.jobTitle = jobTitle;
		this.company = company;
		this.email = email;
		this.phone = phone;
	}

}
