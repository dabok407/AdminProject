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

    $(document).ready(function () {
        searchStart(0)
    });

    $('#search').click(function () {
        searchStart(0)
    });

    // 등록 모달 팝업 open
    $('#registPopupBtn').click(function () {
        // template 태그 삽입
        //$('#modalContentDiv').html($('#regist-template').html());
        // 등록 모달 팝업 show
        $('#adminRegistModal').modal('show');
    });

    // 등록 event
    $('#registBtn').click(function () {
        registAdminUser();
    });

    // 등록 모달 팝업 close
    $('#registCloseModalBtn').click(function () {
        $('#adminRegistModal').modal('hide');
    });

    // 등록 모달 팝업 비밀번호 일치
    $('#reg_passwordCheck').keyup(function () {
        var pwdVal = $('#reg_password').val();
        var pwdCheckVal = $('#reg_passwordCheck').val();
        var msg = "";
        if(pwdVal === pwdCheckVal){
            msg = "일치 합니다.";
        }else{
            msg = "비밀번호가 일지 하지 않습니다.";
        }
        $('#reg_pwdCompareText').text(msg);
        $('#reg_pwdCompareText').show();
    });

    // 수정 event
    $('#modifyBtn').click(function () {
        modifyAdminUser();
    });

    // 상세 모달 팝업 hide
    function closeModifyPopup() {
        $('#adminModifyModal').remove();
    }

    // 등록 모달 팝업 hide
    function closeRegistPopup() {
        //$('#adminRegistModal').modal('hide');
        $('#adminRegistModal').remove();
    }

    function searchStart(index) {
        $.get("/api/adminUser?page="+index, function (response) {

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

    // 사용자 등록
    function registAdminUser(){
        $.ajax({
            url: "/api/adminUser",
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
                console.log(response);
            }
        });
    }

    // 사용자 수정
    function modifyAdminUser(){
        $.ajax({
            url: "/api/adminUser",
            type: 'PUT',
            contentType: 'application/json; charset=utf-8',
            /*data: JSON.stringify($('#registForm').serialize()),*/
            data: $('#registForm').serialize(),
            dataType: 'json',
            async: true,
            beforeSend : function(xhr) {
                var token = $("meta[name='_csrf']").attr("content");
                var header = $("meta[name='_csrf_header']").attr("content");
                xhr.setRequestHeader(header, token);
            },
            success: function (response, textStatus, jqXHR) {
                console.log(response);
                // 팝업 닫기
                closeModifyPopup();
                // 상세 팝업 재호출
                detailSearch(response.data.id);
            }
        });
    }

    // 사용자 상세 정보 조회
    function detailSearch(id){
        // template 태그 삽입
        $('#modalContentDiv').html($('#modify-template').html());
        // 상세 모달 팝업 show
        $('#adminModifyModal').modal('show');
        // 상세 조회 서비스 호출
        $.ajax({
            url: "/api/adminUser/"+id,
            success: function (response, textStatus, jqXHR) {
                var adminUserData = response.data;
                var $selector = $('#modifyForm');

                $('#modifyForm').find('input, select, checkbox, radio').val(null);

                $selector.find("#mod_account").val(adminUserData.account);
                $selector.find("#mod_role").val(adminUserData.role);
                $selector.find("#mod_login_fail_count").text(adminUserData.login_fail_count);
                $selector.find("#mod_last_login_at").text(adminUserData.last_login_at);
                $selector.find("#mod_registered_at").text(adminUserData.registered_at);

                /*new Vue({
                    el : '#adminModifyTableDiv',
                    data :adminUserData
                });*/
            }
        });
    }

})(jQuery);