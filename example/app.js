const PERMISSIONS = ['android.permission.READ_PHONE_STATE', 'android.permission.READ_CONTACTS'];
const Tel = require("ti.phonenumber");

var win = Ti.UI.createWindow({
	backgroundColor : 'white'
});

win.addEventListener('open', function() {
	if (Ti.Android.hasPermission(PERMISSIONS[0]) && Ti.Android.hasPermission(PERMISSIONS[1])) 
		handleActionWithPermissions();
	else {
		Ti.Android.requestPermissions(PERMISSIONS, function(e) {
			if (e.success)
				handleActionWithPermissions(); 
		});
	}
});
win.open();
function handleActionWithPermissions() {
	console.log(Tel.getBySim());
	console.log(Tel.getNumberByAccount());
	console.log(Tel.getNumberByContactlist());
}
