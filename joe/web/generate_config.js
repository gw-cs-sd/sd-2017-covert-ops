

// http://stackoverflow.com/questions/1184624/convert-form-data-to-javascript-object-with-jquery
function objectifyForm(formArray) {//serialize data function

  returnArray = {};
  for (var i = 0; i < formArray.length; i++){
    returnArray[formArray[i]['name']] = formArray[i]['value'];
  }
  return returnArray;
}