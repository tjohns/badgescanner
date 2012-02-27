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
