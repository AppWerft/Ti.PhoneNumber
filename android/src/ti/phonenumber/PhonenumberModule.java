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
	// android.permission.READ_PHONE_STATE
	// Standard Debugging variables
	private static final String LCAT = "PhonenumberModule";
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

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app) {
		Log.d(LCAT, "inside onAppCreate");
		// put module init code that needs to run when the application is
		// created
	}

	@Kroll.method
	public String getSimNumbers(@Kroll.argument(optional = true) Object callback) {
		if (hasPermission("READ_PHONE_STATE")) {
			return handlePhonestate();
		} else if (callback != null && callback instanceof KrollFunction) {
			requests.put(REQCODE_READ_PHONE_STATE, (KrollFunction) callback);
			requestPermission("READ_PHONE_STATE", REQCODE_READ_PHONE_STATE);
		}
		return null;
	}

	@Kroll.method
	public KrollDict getWhatsapp(@Kroll.argument(optional = true) Object callback) {
		if (hasPermission("GET_ACCOUNTS")) {
			return handleAccounts();
		} else if (callback != null && callback instanceof KrollFunction) {
			requests.put(REQCODE_GET_ACCOUNTS, (KrollFunction) callback);
			requestPermission("GET_ACCOUNTS", REQCODE_GET_ACCOUNTS);
		}
		return null;
	}

	@Kroll.method
	public Object[] getContactlist(
			@Kroll.argument(optional = true) Object callback) {
		if (hasPermission("READ_CONTACTS")) {
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
		TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(ctx);
		String imeiSIM1 = telephonyInfo.getImsiSIM1();
        String imeiSIM2 = telephonyInfo.getImsiSIM2();

        boolean isSIM1Ready = telephonyInfo.isSIM1Ready();
        boolean isSIM2Ready = telephonyInfo.isSIM2Ready();

        boolean isDualSIM = telephonyInfo.isDualSIM();
        
		if (Build.VERSION.SDK_INT >= 23) {
			res.put("phoneCount",telephonyManager.getPhoneCount());
		}
		
		String sim1 = telephonyManager.getLine1Number();
		res.put("isDualSIM", isDualSIM);
		res.put("sim1", sim1);
		res.put("isSmsCapable", 	telephonyManager.	isSmsCapable()); 
		res.put("isVoiceCapable", 	telephonyManager.	isVoiceCapable()); 
		return res;
	}

	@Kroll.method
	public KrollDict handleAccounts() {
		KrollDict res = new KrollDict();
		Context ctx = TiApplication.getInstance().getApplicationContext();
		AccountManager am = AccountManager.get(ctx);
		Account[] accounts = am.getAccounts();
		ArrayList<String> googleAccounts = new ArrayList<String>();
		for (Account ac : accounts) {
			String acname = ac.name;
			String actype = ac.type;
			if (actype.equals("com.whatsapp")) {
				String phoneNumber = ac.name;
				res.put("com.whatsapp", ac.name);
				return res;
			}
			// Take your time to look at all available accounts
			System.out.println("Accounts : " + acname + ", " + actype);
		}
		return null;
	}

	@Kroll.method
	public Object[] handleContacts() {
		KrollDict res = new KrollDict();
		final Context ctx = TiApplication.getInstance().getApplicationContext();
		ArrayList<KrollDict> phoneNumbers = new ArrayList<KrollDict>();
		final ContentResolver cr = ctx.getContentResolver();
		String[] projection = new String[] { Contacts.AUTHORITY, Phone.NUMBER };
		final Cursor cur = cr.query(Data.CONTENT_URI, projection, null, null,
				null);
		if (cur.getCount() > 0) {// thats mean some resutl has been found
			if (cur.moveToNext()) {

				KrollDict Name = new KrollDict();
				phoneNumbers.add(Name);
				String id = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cur
						.getString(cur
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				Log.e("Names", name);
				Name.put("name", name);
				ArrayList<String> numberList = new ArrayList<String>();
				Name.put("phones", numberList);
				if (Integer
						.parseInt(cur.getString(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
					Cursor phones = ctx.getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = " + id, null, null);
					while (phones.moveToNext()) {
						String phoneNumber = phones
								.getString(phones
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						Log.e("Number", phoneNumber);
						numberList.add(phoneNumber);
					}
					phones.close();
				}
			}
		}
		cur.close();
		return phoneNumbers.toArray();
	}

	private boolean hasPermission(String permission) {
		if (Build.VERSION.SDK_INT >= 23) {
			ArrayList<String> permissions = new ArrayList<String>();
			Activity currentActivity = TiApplication.getInstance()
					.getCurrentActivity();
			if (currentActivity.checkSelfPermission("android.permission."
					+ permission) != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}

	private void requestPermission(String permission, int REQUEST_CODE) {
		if (Build.VERSION.SDK_INT >= 23) {
			String permissions[] = new String[] { "android.permission."
					+ permission };
			Activity currentActivity = TiApplication.getInstance()
					.getCurrentActivity();
			currentActivity.requestPermissions(permissions, REQUEST_CODE);
			return;
		}
	}

	@Override
	public void onError(Activity arg0, int arg1, Exception arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResult(Activity activity, int requestCode, int resultCode,
			Intent data) {
		if (resultCode == Activity.RESULT_CANCELED) {

		}
		if (resultCode == Activity.RESULT_OK
				&& requests.containsKey(requestCode)) {
			switch (requestCode) {
			case REQCODE_READ_PHONE_STATE:
				requests.get(requestCode).call(getKrollObject(),
						handlePhonestate());
				break;
			case REQCODE_READ_CONTACTS:
				requests.get(requestCode).call(getKrollObject(),
						handleContacts());
				break;
			case REQCODE_GET_ACCOUNTS:
				requests.get(requestCode).call(getKrollObject(),
						handleAccounts());
				break;
			}
		}

	}
}
