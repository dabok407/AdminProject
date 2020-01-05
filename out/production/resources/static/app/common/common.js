
var common = {};

/*
 * context path
 * {param} url
 * {return} context path + url
 */
common.getContextPath = function getContextPath() {
    var hostIndex = location.href.indexOf( location.host ) + location.host.length;
    return location.href.substring( hostIndex, location.href.indexOf('/', hostIndex + 1) );
};

/*
 * href link
 * {param} url
 * {return} context path + url => link
 */
common.href = function href(url){
    location.href = common.getContextPath()+"/"+url;
}

/*
 * form data json parse
 * {param} form Data (serialize)
 * {return} json
 */
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

/*
 * object empty 여부 확인
 * {param} obj
 * {return} boolean true or false
 */
common.isEmpty = function(obj){
    if (obj == null) return true;
    if (obj == "null") return true;
    if (obj.length > 0)    return false;
    if (obj.length === 0)  return true;
    if (typeof obj !== "object") return true;
    for (var key in obj) {
        if (hasOwnProperty.call(obj, key)) return false;
    }
    return true;
};

/*
 * 1000단위 콤마
 * {param} str
 * {return} 콤마 문자열
 */
common.setComma = function (str) {
    str = str.toString();
    var pattern = /(-?\d+)(\d{3})/;
    while (pattern.test(str))
        str = str.replace(pattern, "$1,$2");
    return str;
};

/*
 * 현재날짜 반환 함수
 * 서버 쪽 시간을 가져오기 위해서 xmlHttp 객체 안에 있는 서버의 시간
 * 정보를 header에서 추출해서 사용
 * {return} 오늘 날짜
 */
common.getToday = function()
{
    var xmlHttp;
    var serverTime;
    if (window.XMLHttpRequest) {//분기하지 않으면 IE에서만 작동된다.
        xmlHttp = new XMLHttpRequest(); // IE 7.0 이상, 크롬, 파이어폭스 등
        xmlHttp.open('HEAD',window.location.href.toString(),false);
        xmlHttp.setRequestHeader("Content-Type", "text/html");
        xmlHttp.send('');
        serverTime = xmlHttp.getResponseHeader("Date");
    } else if (window.ActiveXObject) {
        xmlHttp = new ActiveXObject('Msxml2.XMLHTTP');
        xmlHttp.open('HEAD',window.location.href.toString(),false);
        xmlHttp.setRequestHeader("Content-Type", "text/html");
        xmlHttp.send('');
        serverTime = xmlHttp.getResponseHeader("Date");
    }

    var dateObj = new Date(serverTime);
    var year = dateObj.getFullYear();
    var month = dateObj.getMonth()+1;
    var day = dateObj.getDate();

    //1~9월 까지는 앞에 0 붙히기
    //이 부분 로직 formatter로 변경가능한지 찾아봐야함.
    if(month<10)
    {
        month="0"+month;
    }
    if(day<10)
    {
        day="0"+day;
    }

    var today = year+"-"+month+"-"+day;
    return today;
};
