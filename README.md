# Ti.PhoneNumber

This is a Axway Titanium module  for gettingtelephone numbrer og device. It offers some methods, all needs run time permissions.

```javascript 
const Tel = require('ti.phonenumber');
```

## SIM

Needs  `android.permission.READ_PHONE_STATE`
in manifest. 

If you start without granted permissions the methode will return null;

```javascript
const res = Tel.getNumberBySIM();
console.log(res);

Tel.getNumberBySIM(function(e){
	console.log(e);
});

```

If you call with a call back function as Paramter, then the permission will requested.

## WhatsappAccount

Needs `android.permission.GET_ACCOUNTS` permission. Same pattern as above: With callback the permission request will forced.

```javascript
const res = Tel.getNumberByWhatsappAccount();
console.log(res);

```

## Contact

Needs `android.permission.READ_CONTACTS` permission.

```javascript
const res = Tel.getNumberByContactlist();
console.log(res);

Tel.getNumberByContactList(function(e){
	console.log(e);
});

```

In most devices the entry with index 0 is the number of the device owner or the user has name `me`.


### Errors

In case of an error the callback contains a property "error" (=true) and "message"