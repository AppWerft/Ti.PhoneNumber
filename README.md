# Ti.PhoneNumber

This is a Axway Titanium module  for gettingtelephone numbrer og device. It offers some methods, all needs run time permissions.

```javascript 
const Tel = require('ti.phonenumber');

const Window = Ti.UI.createWindow();

```

## By account


```javascript
const res = Tel.getNumberByAccount();
console.log(res);

```


## By SIM

Needs  `android.permission.READ_PHONE_STATE`
in manifest. 

If you start without granted permissions the methode will return null;

```javascript

const res = Tel.getNumberBySIM();
console.log(res);
Window.addEventListener('open',function() {
	Tel.getNumberBySIM(function(e){
		console.log(e);
	});
}
```
In most cases this method will return null or '??????'. ;-)
If you call with a call back function as Paramter, then the permission will requested.


## By Contact

Needs `android.permission.READ_CONTACTS` permission.

```javascript
// you can work without permissions, in this case you
// will get the result directly.
const res = Tel.getNumberByContactlist();
console.log(res);

// or yot start with callback. Tn this case a permission
// requester will start:
Window.addEventListener('open',function() {
	Tel.getNumberByContactList(function(e){
		console.log(e);
	});
});
Window.open();
```

In most devices the entry with index 0 is the number of the device owner or the user has name `me`.


### Errors

In case of an error the callback contains a property "error" (=true) and "message"