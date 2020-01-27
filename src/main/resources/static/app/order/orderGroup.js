(function ($) {

    var maxBtnSize = 7;              // 검색 하단 최대 범위
    var indexBtn = [];               // 인덱스 버튼

    // 페이징 처리 데이터
    var pagination = {
        total_pages : 0,            // 전체 페이지수
        total_elements : 0,         // 전체 데이터수
        current_page :  0,          // 현재 페이지수
        current_elements : 0        // 현재 데이터수
    };


    // 페이지 정보
    var showPage = new Vue({
        el : '#showPage',
        data : {
            totalElements : {},
            currentPage:{}
        }
    });

    // 데이터 리스트
    var itemList = new Vue({
        el : '#itemList',
        data : {
            itemList : {}
        },
        methods: {
            click: function (id) {
                // 상세 정보 조회
                detailSearch(id);
            }
        }
    });


    // 페이지 버튼 리스트
    var pageBtnList = new Vue({
        el : '#pageBtn',
        data : {
            btnList : {}
        },
        methods: {
            indexClick: function (id) {
                searchStart(id-1)
            },
            previousClick:function () {
                if(pagination.current_page !== 0){
                    searchStart(pagination.current_page-1);
                }
            },
            nextClick:function () {
                if(pagination.current_page !== pagination.total_pages-1){
                    searchStart(pagination.current_page+1);
                }
            }
        },
        mounted:function () {
            // 제일 처음 랜더링 후 색상 처리
            setTimeout(function () {
                $('li[btn_id]').removeClass( "active" );
                $('li[btn_id='+(pagination.current_page+1)+']').addClass( "active" );
            },50)
        }
    });


    $('#search').click(function () {
        searchStart(0)
    });

    $(document).ready(function () {
        searchStart(0, "Y")
    });

    $('#itemRegistPopupBtn').click(function () {
        window.open("/pages/itemRegistPopup", "itemPopup","width=1300,height=685");
    });

    // 등록 모달 팝업 open
    $('#registPopupBtn').click(function () {
        // template 태그 삽입
        //$('#modalContentDiv').html($('#regist-template').html());
        // 등록 모달 팝업 show
        $('#orderRegistModal').modal('show');
    });

    // 등록 event
    $('#registBtn').click(function () {
        registOrder();
    });

    // 등록 모달 팝업 close
    $('#registCloseModalTopBtn').click(function () {
        // regist form reset
        registFormReset();
        // regist modal hide
        $('#orderRegistModal').modal('hide');
    });

    // 수정 event
    $('#modifyBtn').click(function () {
        modifyOrder();
    });

    // 삭제 event
    $('#deleteBtn').click(function () {
        if(confirm("정말 삭제 하시겠습니까?")){
            deleteOrder();
        }
    });

    // 상세 모달 팝업 hide
    function closeModifyPopup() {
        $('#orderModifyModal').modal('hide');
        /*$('#adminModifyModal').remove();*/
    }

    // 등록 모달 팝업 hide
    function closeRegistPopup() {
        $('#orderRegistModal').modal('hide');
        /*$('#adminRegistModal').remove();*/
    }

    // 수정 모달 팝업 close
    $('#modifyCloseModalBtn, #modifyCloseModalTopBtn').click(function () {
        // modify form reset
        modifyFormReset();
        // modal popup hide
        $('#orderModifyModal').modal('hide');

    });

    // 등록 from 값 초기화
    function registFormReset() {
        $('#registForm').find('input, select, checkbox, radio').val(null);
    }

    // 수정 form 값 초기화
    function modifyFormReset() {
        $('#modifyForm').find('input, select, checkbox, radio').val(null);
    }

    function searchStart(index, initialYn) {

        var pageSize = 10;
        var paramUrl = "";

        if(initialYn != "" && initialYn != null){
            paramUrl += "&initialYn=Y";
        }else{
            paramUrl += "&initialYn=''";
        }

        $.get("/api/orderGroup?page="+index+'&size='+pageSize+paramUrl, function (response) {

            /* 데이터 셋팅 */
            // 페이징 처리 데이터
            indexBtn = [];
            pagination = response.pagination;


            //전체 페이지
            showPage.totalElements = pagination.current_elements;
            showPage.currentPage = pagination.current_page+1;


            // 검색 데이터
            itemList.itemList = response.data;


            // 이전버튼
            if(pagination.current_page === 0){
                $('#previousBtn').addClass("disabled")
            }else{
                $('#previousBtn').removeClass("disabled")
            }


            // 다음버튼
            if(pagination.current_page === pagination.total_pages-1){
                $('#nextBtn').addClass("disabled")
            }else{
                $('#nextBtn').removeClass("disabled")
            }

            // 페이징 버튼 처리
            var temp = Math.floor(pagination.current_page / maxBtnSize);
            for(var i = 1; i <= maxBtnSize; i++){
                var value = i+(temp*maxBtnSize);

                if(value <= pagination.total_pages){
                    indexBtn.push(value)
                }
            }

            // 페이지 버튼 셋팅
            pageBtnList.btnList = indexBtn;


            // 색상처리
            setTimeout(function () {
                $('li[btn_id]').removeClass( "active" );
                $('li[btn_id='+(pagination.current_page+1)+']').addClass( "active" );
            },50)
        });
    }

    // 주문 등록
    function registOrder(){

        var pwdEqualCheck = $('#reg_equal_pwd').val();

        $.ajax({
            url: "/api/orderGroup",
            type: 'POST',
            contentType: 'application/json; charset=utf-8',
            /*data: JSON.stringify($('#registForm').serialize()),*/
            data: JSON.stringify(common.serializeObject("registForm")),
            dataType: 'json',
            async: true,
            beforeSend : function(xhr) {
                var token = $("meta[name='_csrf']").attr("content");
                var header = $("meta[name='_csrf_header']").attr("content");
                xhr.setRequestHeader(header, token);
            },
            success: function (response, textStatus, jqXHR) {
                var resultCode = response.result_code;
                if(resultCode == "OK"){
                    alert("등록 되었습니다.");
                    // form 초기화
                    registFormReset();
                    // 팝업 close
                    closeRegistPopup();
                    // 주문 조회
                    searchStart(0);
                }else{
                    alert(response.description);
                }
            }
        });
    }

    // 주문 수정
    function modifyOrder(){

        var pwdEqualCheck = $('#mod_equal_pwd').val();

        if(pwdEqualCheck != "success"){
            alert("비밀번호를 확인해주세요.");
            return false;
        }

        $.ajax({
            url: "/api/orderGroup",
            type: 'PUT',
            contentType: 'application/json; charset=utf-8',
            /*data: JSON.stringify($('#registForm').serialize()),*/
            data: JSON.stringify(common.serializeObject("modifyForm")),
            dataType: 'json',
            async: true,
            beforeSend : function(xhr) {
                var token = $("meta[name='_csrf']").attr("content");
                var header = $("meta[name='_csrf_header']").attr("content");
                xhr.setRequestHeader(header, token);
            },
            success: function (response, textStatus, jqXHR) {
                var resultCode = response.result_code;
                if(resultCode == "OK"){
                    alert("수정 되었습니다.");
                    // 팝업 close
                    closeModifyPopup();
                    // 주문 조회
                    searchStart(0);
                }
            }
        });
    }

    // 주문 삭제
    function deleteOrder(){
        var id = $("#mod_id").val();
        $.ajax({
            url: "/api/orderGroup/"+id,
            type: 'DELETE',
            beforeSend : function(xhr) {
                var token = $("meta[name='_csrf']").attr("content");
                var header = $("meta[name='_csrf_header']").attr("content");
                xhr.setRequestHeader(header, token);
            },
            success: function (response, textStatus, jqXHR) {
                var resultCode = response.result_code;
                if(resultCode == "OK"){
                    alert("삭제 되었습니다.");
                    // 팝업 close
                    closeModifyPopup();
                    // 주문 조회
                    searchStart(0);
                }else{
                    alert(response.description);
                }
            }
        });
    }

    // 주문 상세 정보 조회
    function detailSearch(id){
        // template 태그 삽입
        $('#modalContentDiv').html($('#modify-template').html());
        // 상세 모달 팝업 show
        $('#orderModifyModal').modal('show');
        // 상세 조회 서비스 호출
        $.ajax({
            url: "/api/orderGroup/"+id,
            success: function (response, textStatus, jqXHR) {
                var orderData = response.data;
                var $selector = $('#modifyForm');

                $selector.find("#mod_id").val(orderData.id);
                $selector.find("#mod_account").val(orderData.account);
                $selector.find("#mod_role").val(orderData.role);
                $selector.find("#mod_login_fail_count").text(orderData.login_fail_count);
                $selector.find("#mod_last_login_at").text(orderData.last_login_at);
                $selector.find("#mod_registered_at").text(orderData.registered_at);
                $selector.find("#mod_equal_pwd").val("success");

                /*new Vue({
                    el : '#adminModifyTableDiv',
                    data :adminUserData
                });*/
            }
        });
    }

})(jQuery);