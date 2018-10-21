/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2011-2016 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

/** This is generated, do not edit by hand. **/

#include <jni.h>

#include "Proxy.h"

namespace ti {
namespace phoneneumber {

class PhonenumberModule : public titanium::Proxy
{
public:
	explicit PhonenumberModule();

	static void bindProxy(v8::Local<v8::Object>, v8::Local<v8::Context>);
	static v8::Local<v8::FunctionTemplate> getProxyTemplate(v8::Isolate*);
	static void dispose(v8::Isolate*);

	static jclass javaClass;

private:
	static v8::Persistent<v8::FunctionTemplate> proxyTemplate;

	// Methods -----------------------------------------------------------
	static void getNumberByWhatsappAccount(const v8::FunctionCallbackInfo<v8::Value>&);
	static void handleAccounts(const v8::FunctionCallbackInfo<v8::Value>&);
	static void getNumberBySIM(const v8::FunctionCallbackInfo<v8::Value>&);
	static void getNumberByContactlist(const v8::FunctionCallbackInfo<v8::Value>&);
	static void handleContacts(const v8::FunctionCallbackInfo<v8::Value>&);

	// Dynamic property accessors ----------------------------------------

};

} // phoneneumber
} // ti
