
var common = {};

common.getContextPath = function getContextPath() {
    var hostIndex = location.href.indexOf( location.host ) + location.host.length;
    return location.href.substring( hostIndex, location.href.indexOf('/', hostIndex + 1) );
};

common.href = function href(url){
    location.href = common.getContextPath()+"/"+url;
}