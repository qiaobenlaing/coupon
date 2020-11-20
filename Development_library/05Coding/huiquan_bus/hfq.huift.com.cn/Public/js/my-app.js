// Initialize app
var myApp = new Framework7();

// If we need to use custom DOM library, let's save it to $$ variable:
var $$ = Framework7.$;

// Add view
var mainView = myApp.addView('.view-main', {
    // Because we want to use dynamic navbar, we need to enable it for this view:
    dynamicNavbar: true
});

// Now we need to run the code that will be executed only for About page.
// For this case we need to add event listener for "pageInit" event

// Option 1. Using one 'pageInit' event handler for all pages (recommended way):
$$(document).on('pageInit', function (e) {
    // Get page data from event data
    var page = e.detail.page;
});
$('.shop-decoration:first').addClass('active');
// 命名空间
var sz;
if(!sz) sz = {};
/** status SUCC */
sz.STATUS_SUCC = 0;
/** status FAIL */
sz.STATUS_FAIL = -1;