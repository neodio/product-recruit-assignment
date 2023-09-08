
let main = {
    init: function () {
        main.initForm();
        main.event();
    },
    initForm: function () {
        $("#widthId").text('');
        $("#heightId").text('');
        $("#fileSize").text('');
        $("#resultValue").text('');
        $("#errorMsg").text('');
    },
    event: function () {
        $('#btn').on('click', function () {
            if(main.validate()) {
                main.getImageInfo();
            }
        });
    },
    getImageInfo: function () {
        main.initForm();
        let formData = {};
        let serialize = $("#imageForm").serializeArray();
                serialize.forEach(e => {
                    formData[e.name] = e.value;
                });

        $.ajax({
            type: "POST",
            url: "/image/getImageAnalytics",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(formData),
            dataType: "json",
            async: true,
            success: function (res) {
                if(res.code === "ok"){
                    let result = res.data;
                    $("#widthId").text(result.width);
                    $("#heightId").text(result.height);
                    $("#fileSize").text(result.fileSize);
                    $("#resultValue").text(result.resultValue);
                }
            },
            error: function (resError) {
                let errorData = JSON.parse(resError.responseText);
                let errorMsg =  errorData.message;
                $("#errorMsg").text(errorMsg);
            }
        });
    },
    validate: function () {
        if ($('#imageUrl').val() == "") {
            $('#imageUrl').focus();
            alert('이미지 주소를 입력해주세요.');
            return false;
        }
        return true;
    }
};