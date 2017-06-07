function sendEmailCode(url, email, btnDom) {
    if (!/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(email)) {
        alert('邮箱格式不正确');
        return;
    }
    btnDom.disabled = true;
    $.post(url, {
        'email' : email
    }, function(result) {
        if (result.status == 'error') {
            alert(result.data);
            btnDom.disabled = false;
        } else {
            var remainTime = 60;
            btnDom.value = remainTime + '秒后可重试';
            var intervalId = setInterval(function() {
                remainTime--;
                if (remainTime <= 0) {
                    clearInterval(intervalId);
                    btnDom.disabled = false;
                    btnDom.value = '获取邮箱验证码';
                } else {
                    btnDom.value = remainTime + '秒后可重试';
                }
            }, 1000);
        }
    }, 'json');
}
// 发送短信验证码
function sendSMSCode(url, phone, btnDom) {
    if (!/^1(\d{10})$/.test(phone)) {
        alert('手机号格式不正确');
        return;
    }
    btnDom.disabled = true;
    $.post(url, {
        'phone' : phone
    }, function(result) {
        if (result.status == 'error') {
            alert(result.data);
            btnDom.disabled = false;
        } else {
            var remainTime = 60;
            btnDom.innerHTML = remainTime + '秒后可重试';
            var intervalId = setInterval(function() {
                remainTime--;
                if (remainTime <= 0) {
                    clearInterval(intervalId);
                    btnDom.disabled = false;
                    btnDom.innerHTML = '获取短信验证码';
                } else {
                    btnDom.innerHTML = remainTime + '秒后可重试';
                }
            }, 1000);
        }
    }, 'json');
}

/**
 * 使用ajax发送一个请求
 */
function ajaxSubmit(url, params) {
    $.post(url, params, function(ajaxResult) {
        alert(ajaxResult.data);
        if (ajaxResult.status == 'success') {
            location.reload();
        }
    }, 'json');
}

function ajaxSubmitForm(formDom) {

    var params = $(formDom).serialize();
    $.post(formDom.action, params, function(result) {
        alert(result.data);
        if (result.status == "success") {
            location.reload();
        }
    }, 'json');
    event.preventDefault();
}
// 自动调节mainDiv的最小高度，使得footer区域总是处在最下方，而且尽可能不出现滚动条
$(function() {
    $("#mainDiv").css("min-height", window.innerHeight - $("#mainDiv").prop("offsetTop") - $("#footerDiv").prop("offsetHeight") + "px");
});