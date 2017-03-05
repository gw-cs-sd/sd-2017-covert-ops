values = {};

// Keystroke Detection
// jQuery('.slider.keystroke_dynamics input').value();
values['keystroke_dynamics'] = {};

jQuery('.slider.keystroke_dynamics').on('moved.zf.slider',function(){
	new_kb_val = jQuery(this).find("input").first().attr("value");
	values['keystroke_dynamics']['accuracy'] = new_kb_val;
	update_config();
});


function update_config(){
	jQuery('#config').html(JSON.stringify(values));
}