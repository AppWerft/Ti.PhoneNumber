/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2017 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package ti.phonenumber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.util.TiActivityResultHandler;
import org.json.JSONArray;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.telephony.TelephonyManager;

@Kroll.module(name = "Phonenumber", id = "ti.phonenumber")
public class PhonenumberModule extends KrollModule implements
		TiActivityResultHandler {

	private static final String LCAT = "☎️ TiPhoneNumber";
	private static final int REQCODE_READ_PHONE_STATE = 1;
	private static final int REQCODE_READ_CONTACTS = 2;
	private static final int REQCODE_GET_ACCOUNTS = 3;

	private Map<Integer, KrollFunction> requests;

	public PhonenumberModule() {
		super();
		if (requests == null) {
			requests = new HashMap<Integer, KrollFunction>();
		}
	}
	@Kroll.method
	public KrollDict getNumberByAccount(){
		return handleAccounts();
	}

	@Kroll.method
	public KrollDict getNumberBySIM(
			@Kroll.argument(optional = true) Object callback) {
		if (callback == null && hasPermission("READ_PHONE_STATE")) {
			return handlePhonestate();
		} else if (callback != null && callback instanceof KrollFunction) {
			Log.d(LCAT, "getNumberBySIM with callback");
			requests.put(REQCODE_READ_PHONE_STATE, (KrollFunction) callback);
			requestPermission("READ_PHONE_STATE", REQCODE_READ_PHONE_STATE);
		}
		return null;
	}
	
	

	@Kroll.method
	public KrollDict getNumberByContactlist(
			@Kroll.argument(optional = true) Object callback) {
		if (callback == null && hasPermission("READ_CONTACTS")) {
			return handleContacts();
		} else if (callback != null && callback instanceof KrollFunction) {
			requests.put(REQCODE_READ_CONTACTS, (KrollFunction) callback);
			requestPermission("READ_CONTACTS", REQCODE_READ_CONTACTS);
		}
		return null;
	}

	private KrollDict handlePhonestate() {
		KrollDict res = new KrollDict();
		// https://stackoverflow.com/questions/14517338/android-check-whether-the-phone-is-dual-sim
		Context ctx = TiApplication.getInstance().getApplicationContext();
		TelephonyManager telephonyManager = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);

		if (Build.VERSION.SDK_INT >= 23) {
			res.put("phoneCount", telephonyManager.getPhoneCount());
		}
		String sim1 = telephonyManager.getLine1Number();
		res.put("sim1", sim1);
		res.put("isSmsCapable", telephonyManager.isSmsCapable());
		res.put("isVoiceCapable", telephonyManager.isVoiceCapable());
		return res;
	}

	@Kroll.method
	public KrollDict handleAccounts() {
		ArrayList<KrollDict> accountlist = new ArrayList<KrollDict>();
		
		Context ctx = TiApplication.getInstance().getApplicationContext();
		AccountManager am = AccountManager.get(ctx);
		Account[] accounts = am.getAccounts();
		for (Account ac : accounts) {
			KrollDict a = new KrollDict();
			a.put("type", ac.type);
			a.put("name", ac.name);
			accountlist.add(a);
		}
		KrollDict result = new KrollDict();
		result.put("accounts", accountlist.toArray(new KrollDict[accountlist.size()]));
		return result;
	}

	@Kroll.method
	public KrollDict handleContacts() {
		KrollDict result = new KrollDict();
		final Context ctx = TiApplication.getInstance().getApplicationContext();
		ArrayList<KrollDict> phoneNumbers = new ArrayList<KrollDict>();
		Cursor phones = ctx.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
				null, null);
		while (phones.moveToNext()) {
			KrollDict phone = new KrollDict();
			phone.put(
					"displayName",
					phones.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
			phone.put(
					"number",
					phones.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
			/*phone.put(
					"contentType",
					phones.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE)));
			*/
			phone.put(
					"isSuperPrimary",
					phones.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.IS_SUPER_PRIMARY)));
			phone.put(
					"isPrimary",
					phones.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.IS_PRIMARY)));
			phoneNumbers.add(phone);
		}
		// https://stackoverflow.com/questions/10755994/how-to-get-all-contacts-and-all-of-their-attributes
		phones.close();
		Log.d(LCAT,"numbers found: " + phoneNumbers.size());
		result.put("contacts", phoneNumbers.toArray(new KrollDict[phoneNumbers.size()]));
		
		return result;
	}

	private boolean hasPermission(String permission) {
		if (Build.VERSION.SDK_INT >= 23) {
			Activity currentActivity = TiApplication.getInstance()
					.getCurrentActivity();
			if (currentActivity.checkSelfPermission("android.permission."
					+ permission) != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}

	private void requestPermission(String permission, int requestCode) {
		if (Build.VERSION.SDK_INT < 23 || hasPermission(permission)) {
			Log.d(LCAT, "always granted: " + permission);
			dispatchTaskAndCallback(requestCode);
		} else {
			Log.d(LCAT, "requestPermission " + permission);
			String permissions[] = new String[] { "android.permission."
					+ permission };
			Activity currentActivity = TiApplication.getInstance()
					.getCurrentActivity();
			currentActivity.requestPermissions(permissions, requestCode);
			return;
		}
	}

	@Override
	public void onError(Activity activity, int requestCode, Exception ex) {
		if (requests.containsKey(requestCode)) {
			KrollDict res = new KrollDict();
			res.put("error", true);
			res.put("message", ex.getMessage());
			requests.get(requestCode).call(getKrollObject(), res);
		}
	}

	@Override
	public void onResult(Activity activity, int requestCode, int resultCode,
			Intent data) {
		Log.d(LCAT,"onResult " + requestCode + "  " + resultCode);
		if (resultCode == Activity.RESULT_CANCELED) {
			KrollDict res = new KrollDict();
			res.put("error", true);
			res.put("message", "canceled");
			requests.get(requestCode).call(getKrollObject(), res);

		}
		if (resultCode == Activity.RESULT_OK
				&& requests.containsKey(requestCode)) {
			dispatchTaskAndCallback(requestCode);
		}
	}

	private void dispatchTaskAndCallback(int requestCode) {
		switch (requestCode) {
		case REQCODE_READ_PHONE_STATE:
			requests.get(requestCode)
					.call(getKrollObject(), handlePhonestate());
			break;
		case REQCODE_READ_CONTACTS:
			requests.get(requestCode).call(getKrollObject(), handleContacts());
			break;
		case REQCODE_GET_ACCOUNTS:
			requests.get(requestCode).call(getKrollObject(), handleAccounts());
			break;
		}
	}
}
