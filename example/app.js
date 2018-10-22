// This is a test harness for your module
// You should do something interesting in this harness
// to test out the module and to provide instructions
// to users on how to use it by example.

// open a single window
var win = Ti.UI.createWindow({
	backgroundColor : 'white'
});
win.addEventListener('open', function() {
	const TN = require("ti.phonenumber");
	TN.getNumberBySIM(function(e) {
		console.log(e);
		TN.getNumberByWhatsappAccount(function(e) {
			console.log(e);
			TN.getNumberByContactlist(function(e) {
				console.log(e);
			});
		});
	});
});
win.open();
