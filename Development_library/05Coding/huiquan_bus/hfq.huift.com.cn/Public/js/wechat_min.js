function Q(a) {
	if (!a) {
		return null
	}
	if (a.constructor == String) {
		var b = document.querySelectorAll(a);
		return b
	}
	return a
}
Q.trim = function(b) {
	var a = new RegExp("(^[\\s\\t\\xa0\\u3000]+)|([\\u3000\\xa0\\s\\t]+$)", "g");
	return String(b).replace(a, "")
};
Q.addClass = function(h, j) {
	h = Q(h);
	var b = j.split(/\s+/), a = h.className, f = " " + a + " ", g = 0, d = b.length;
	for (; g < d; g++) {
		if (f.indexOf(" " + b[g] + " ") < 0) {
			a += (a ? " " : "") + b[g]
		}
	}
	h.className = Q.trim(a);
	return h
};
Q.hasClass = function(d, f) {
	d = Q(d);
	if (!d.nodeType === 1) {
		return false
	}
	var b = Q.trim(f).split(/\s+/), a = b.length;
	className = d.className.split(/\s+/).join(" ");
	while (a--) {
		if (!(new RegExp("(^| )" + b[a] + "( |$)")).test(className)) {
			return false
		}
	}
	return true
};
Q.removeClass = function(f, h) {
	f = Q(f);
	var b = f.className.split(/\s+/);
	var g = h.split(/\s+/);
	var a = b.length;
	var j = g.length;
	for (var d = 0; d < j; d++) {
		while (a--) {
			if (b[a] == g[d]) {
				b.splice(a, 1);
				break
			}
		}
	}
	f.className = b.join(" ");
	return f
};
Q.tap = function(f, k) {
	var a, h, d, g, b, j;
	a = "1.0.1";
	d = 2 * 1000;
	j = function(l, m) {
		return function() {
			return l.apply(m, arguments)
		}
	};
	if ("ontouchstart" in document.documentElement) {
		f.addEventListener("touchstart", j(function(l) {
			g = new Date().getTime();
			h = false
		}, f));
		f.addEventListener("touchmove", j(function(l) {
			h = true
		}, f));
		f.addEventListener("touchend", j(function(l) {
			b = new Date().getTime();
			if (!h && ((b - g) < d)) {
				k(l)
			}
		}, f));
		f.addEventListener("touchcancel", j(function(l) {
			k(l)
		}, f))
	} else {
		f.addEventListener("click", j(function(l) {
			k(l)
		}, f))
	}
};
Q.parent = function(b, a) {
	a = a ? a : function() {
		return true
	};
	while ((b = b.parentNode) && b.nodeType == 1) {
		if (a(b)) {
			return b
		}
	}
	return null
};
Q.getData = function(a, b) {
	if (window.nativeStorage) {
		return window.nativeStorage.get(a)
	} else {
		if (window.localStorage) {
			return window.localStorage[a]
		} else {
			return Q.getCookie(a)
		}
	}
};
Q.setData = function(a, b) {
	if (window.nativeStorage) {
		return window.nativeStorage.set(a, b)
	} else {
		if (window.localStorage) {
			window.localStorage[a] = b
		} else {
			Q.setCookie(a, b, {
				expires : new Date("2020-12-23")
			})
		}
	}
};
Q.remove = function() {
	var f;
	for (var a = 0, d = arguments.length; a < d; a++) {
		f = Q(arguments[a]);
		try {
			f.parentNode.removeChild(f)
		} catch (b) {
		}
	}
};
Q.loadJs = function(d) {
	d.id = d.id || "LoadedJs" + new Date().getTime();
	d.isRemove = d.isRemove || true;
	var b = document.createElement("script");
	b.id = d.id;
	b.type = "text/javascript";
	b.charset = d.charset || "gb2312";
	var f = function() {
		d.callback && d.callback();
		if (d.isRemove) {
			Q.remove("#" + d.id)
		}
	};
	if (b.readyState) {
		b.onreadystatechange = function() {
			if (b.readyState == "loaded" || b.readyState == "complete") {
				b.onreadystatechange = null;
				f()
			}
		}
	} else {
		b.onload = function() {
			f()
		}
	}
	b.src = d.src;
	var a = document.getElementsByTagName("script")[0];
	a.parentNode.insertBefore(b, a)
};

Q.getQuery = function(b, d) {
	var f = new RegExp("(^|&|\\?|#)" + d + "=([^&#]*)(&|$|#)", "");
	var a = b.match(f);
	if (a) {
		return a[2]
	}
	return ""
};
Q.debug = function(b) {
	var a = Q("#DebugContainer");
	if (!a) {
		a = document.createElement("div");
		a.id = "DebugContainer";
		document.body.appendChild(a)
	}
	if (a) {
		a.innerHTML += b + "<br>";
		if (console && console.log) {
			console.log(b)
		}
	}
};
Q.delQueryValue = function(a, k) {
	if (!Q.getQuery(a, k)) {
		return a
	}
	var j = a.indexOf("?");
	var h = a.substr(0, a.indexOf("?"));
	var d = a.substr(a.indexOf("?") + 1);
	var g = d.split("&");
	for (var f = 0, b = g.length; f < b; f++) {
		var l = g[f].split("=")[0];
		if (l == k) {
			g.splice(f, 1);
			break
		}
	}
	if (g.length == 0) {
		return h
	} else {
		return h + "?" + g.join("&")
	}
};
var weChatBridgeReady = {
	init : function() {
		weChatBridgeReady.bindShareWithApp();
		weChatBridgeReady.bindShareWithTimeline();
		var f = Q("img");
		for (var b = 0, d = f.length; b < d; b++) {
			var a = f[b];
			Q.tap(a, weChatBridgeReady.clickHandler)
		}
	},
	clickHandler : function() {
		if (this.className == "download") {
			return
		}
		var d = [], g = Q("img");
		for (var a = 0, f = g.length; a < f; a++) {
			var b = g[a];
			if (b.parenNode.parenNode.id = "DownloadBar") {
				continue
			}
			if (b.className == "download") {
				continue
			}
			d.push(b.src)
		}
		WeixinJSBridge.invoke("imagePreview", {
			current : e.target.src,
			urls : d
		})
	},
	bindShareWithApp : function() {
		var a = document.location.href;
		var b = Q.delQueryValue(a, "mmuin");
		if (!(a.indexOf("isShare=1") > -1)) {
			b = b.indexOf("?") != -1 ? b + "&isShare=1" : b + "?isShare=1"
		}
		WeixinJSBridge.on("menu:share:appmessage", function(d) {
			WeixinJSBridge.invoke("sendAppMessage", {
				appid : "",
				img_url : contentModel.img_url,
				img_width : "65",
				img_height : "65",
				link : b,
				desc : contentModel.desc,
				title : contentModel.title
			}, function(f) {
				WeixinJSBridge.log(f.err_msg)
			})
		})
	},
	bindShareWithTimeline : function() {
		var a = document.location.href;
		var b = Q.delQueryValue(a, "mmuin");
		if (!(a.indexOf("isShare=1") > -1)) {
			b = b.indexOf("?") != -1 ? b + "&isShare=1" : b + "?isShare=1"
		}
		WeixinJSBridge.on("menu:share:timeline", function(d) {
			WeixinJSBridge.invoke("shareTimeline", {
				img_url : contentModel.img_url,
				img_width : "65",
				img_height : "65",
				link : b,
				desc : "www.yntv.com",
				title : contentModel.title
			}, function(f) {
				WeixinJSBridge.log(f.err_msg)
			})
		})
	}
};
if (typeof WeixinJSBridge != "undefined" && WeixinJSBridge.invoke) {
	weChatBridgeReady.init()
} else {
	document.addEventListener("WeixinJSBridgeReady", weChatBridgeReady.init)
}