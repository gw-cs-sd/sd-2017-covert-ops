values = {};
values['keystroke_dynamics'] = {"order": 1};
values['signature_recognition'] = {"order": 2};

jQuery('#config').html(JSON.stringify(values));

jQuery('.slider.keystroke_dynamics').on('moved.zf.slider',function(){
	new_kb_val = jQuery('.slider.keystroke_dynamics').find("input").first().attr("value");
	values['keystroke_dynamics']['accuracy'] = new_kb_val;
	update_config();
});
jQuery('.slider.signature_recognition').on('moved.zf.slider',function(){
	new_sig_val = jQuery('.slider.signature_recognition').find("input").first().attr("value");
	values['signature_recognition']['accuracy'] = new_sig_val;
	update_config();
});

function update_config(){
	console.log("update");
	jQuery('#config').html(JSON.stringify(values));
	// update order
	jQuery('.auth-factor a').each(function(a){
		order = jQuery(this).parent().index();
		console.log(this.innerText.toString().toLowerCase().replace(" ","_") + " " +order)
		current_auth_factor = this.innerText.toString().toLowerCase().replace(" ","_");
		values[current_auth_factor]["order"] = order;
	});
}

var dlAnchorElem = document.getElementById('create');
jQuery(dlAnchorElem).click(function(){
	var dataStr = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(values));
	dlAnchorElem.setAttribute("href",     dataStr     );
	dlAnchorElem.setAttribute("download", "covertops.conf");
});
