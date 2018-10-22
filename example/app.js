
var win = Ti.UI.createWindow({
	backgroundColor : 'white'
});
win.addEventListener('open', function() {
	const TN = require("ti.phonenumber");
	TN.getNumberBySIM(function(e) {
		console.log(e);
		TN.getNumberByAccount(function(e) {
			console.log(e);
			TN.getNumberByContactlist(function(e) {
				console.log(e);
			});
		});
	});
});
win.open();
