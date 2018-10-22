# Ti.PhoneNumber

This is a Axway Titanium module  for gettingtelephone numbrer og device. It offers some methods, all needs run time permissions.

```javascript 
const Tel = require('ti.phonenumber');
const Window = Ti.UI.createWindow();


```

## By account


```javascript
if (Ti.Android.hasPermission('android.permission.READ_CONTACTS')) {
	console.log(Tel.getByAccounts());
} else Ti.Android.requestPermissions(['android.permission.READ_CONTACTS'],function(e)) {
if (e.success) console.log(Tel.getByAccounts());



```


## By SIM

Needs  `android.permission.READ_PHONE_STATE`
in manifest. 

If you start without granted permissions the methode will return null;

```javascript

if (Ti.Android.hasPermission('android.permission.READ_PHONE_STATE')) {
	console.log(Tel.getBySim());
} else Ti.Android.requestPermissions(['android.permission.READ_PHONE_STATE'],function(e)) {
if (e.success) console.log(Tel.getBySim());
```
In most cases this method will return null or '??????'. ;-)
If you call with a call back function as Paramter, then the permission will requested.


## By Contact

Needs `android.permission.READ_CONTACTS` permission.


```javascript
if (Ti.Android.hasPermission('android.permission.READ_CONTACTS')) {
	console.log(Tel.getByContacts());
} else Ti.Android.requestPermissions(['android.permission.READ_CONTACTS'],function(e)) {
if (e.success) console.log(Tel.getByContacts());


Window.open();
```

In most devices the entry with index 0 is the number of the device owner or the user has name `me`.


### Errors

In case of an error the callback contains a property "error" (=true) and "message"