
var common = {};

common.getContextPath = function getContextPath() {
    var hostIndex = location.href.indexOf( location.host ) + location.host.length;
    return location.href.substring( hostIndex, location.href.indexOf('/', hostIndex + 1) );
};

common.href = function href(url){
    location.href = common.getContextPath()+"/"+url;
}

common.serializeObject = function(formId) {
    var returnObj = null;
    var data = null;
    var $selector = $('#'+formId);
    try {
        if($selector[0].tagName && $selector[0].tagName.toUpperCase() == "FORM" ) {
            var arr = $selector.serializeArray();
            if(arr){
                returnObj = {};
                data = {};
                jQuery.each(arr, function() {
                    data[this.name] = this.value;
                });
            }
        }
    }catch(e) {
        alert(e.message);
    }finally {}
    returnObj["data"] = data;
    return returnObj;
}