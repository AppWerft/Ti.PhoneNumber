Ti.PhoneNumber
===========================================

This is a Axway Titanium module  for gettingtelephone numbrer og device. It offers some methods, all needs run time permissions.

``` 
const Tel = require('ti.phonenumber');
```

## SimNumber

Needs  `android.permission.READ_PHONE_STATE`
in manifest. 

If you start without granted permissions the methode will return null;

```
console.log(Tel.getSimNumber());
```

If you call with a call back function as Paramter, then the permission will requested.

## Whatsapp

Needs `android.permission.GET_ACCOUNTS` permission. Same pattern as above: With callback the permission request will forced.

```
console.log(Tel.getWhatsapp());
```

## Contact

Needs `android.permission.READ_CONTACTS` permission.

```javascript
console.log(Tel.getContactlist());

```

In most devices the entry with index 0 is the number of the device owner or the user has name `me`.