values = {};

// Keystroke Detection
// jQuery('.slider.keystroke_dynamics input').value();
values['keystroke_dynamics'] = {};
values['signature_recognition'] = {};

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
	jQuery('#config').html(JSON.stringify(values));
}

var dlAnchorElem = document.getElementById('create');
jQuery(dlAnchorElem).click(function(){
	var dataStr = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(values));
	dlAnchorElem.setAttribute("href",     dataStr     );
	dlAnchorElem.setAttribute("download", "covertops.conf");
});
