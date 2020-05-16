var apiAddress = "http://localhost:8080/";

// 网站标题
var webtitle;
// 获取token
var tk = localStorage.getItem('token');

function logout() {
    localStorage.setItem('token', '');
    window.location.href = "../../page/login.html";
}

// 获取url参数
function getQueryVariable(variable)
{
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i=0;i<vars.length;i++) {
        var pair = vars[i].split("=");
        if(pair[0] == variable){return pair[1];}
    }
    return(false);
}

function webinfo() {
    let tk = localStorage.getItem('token');
    $.ajax({
        url: apiAddress + 'web/webinfo',
        type: 'post',
        data: {token: tk},
        complete: () => {
            // 授权验证
            authorization();
        },
        success: (data) => {
            var state = data.state;
            var datas = data.data;
            if(state == 200) {
                webtitle = datas.webtitle;
                document.title = document.title + " - " + webtitle;
            }
        }
    });
}

// 虚拟机电源状态过滤
function vmStateFilter(vmstate) {
    let state;
    switch(vmstate) {
        case 'poweroff' :
            state = '<span style="color: #e74c3c;">关机</span>';
            break;
        case 'starting' :
            state = '<span style="color: #2980b9;">正在启动</span>';
            break;
        case 'running' :
            state = '<span style="color: #2ecc71;">运行中</span>';
            break;
        case 'create' :
            state = '<span style="color: #e67e22;">正在创建</span>';
            break;
        case 'expire' :
            state = '<span style="color: #e74c3c;">服务到期</span>';
            break;
        case undefined :
            state = '<span style="color: #e74c3c;">机器不存在</span>';
            break;
        default :
        state = '<span style="color: #e74c3c;">关机</span>';
    }
    return state;
}

// 虚拟机电源状态过滤图标
function vmStateFilterIcon(vmstate) {
    let state;
    switch(vmstate) {
        case 'poweroff' :
            state = 'glyphicon glyphicon-play';
            break;
        case 'starting' :
            state = 'glyphicon glyphicon-refresh';
            break;
        case 'running' :
            state = 'glyphicon glyphicon-pause';
            break;
        case 'create' :
            state = 'glyphicon glyphicon-refresh';
            break;
        case 'expire' :
            state = 'glyphicon glyphicon-warning-sign';
            break;
        case undefined :
            state = 'glyphicon glyphicon-ban-circle';
            break;
        default :
            state = 'glyphicon glyphicon-play';
    }
    return state;
}

function Modal(title, text) {
    $('#myModalLabel').text(title);
    $('#ordinary-modal-body').html(text);
    $('.ordinary-modal').modal('show');
}

// 时间戳转 YYYY-mm-dd 日期格式 
function getTime(timestamp) {
	var day = ""
	var month = ""
	var d = new Date(timestamp * 1000)
	day = d.getDate() < 10 ? '0' + d.getDate() : d.getDate()
	month = d.getMonth() + 1 < 10 ? '0' + (d.getMonth()+1) : (d.getMonth()+1)
	var date = (d.getFullYear() + "-" + month + "-" + day)
	return date
}

function KZ_Loading(config) {
    if (this instanceof KZ_Loading) {
        const domTemplate = '<div class="modal fade kz-loading" data-kzid="@@KZ_Loadin_ID@@" backdrop="static" keyboard="false"><div style="width: 200px;height:20px; z-index: 20000; position: absolute; text-align: center; left: 50%; top: 50%;margin-left:-100px;margin-top:-10px"><div class="progress progress-striped active" style="margin-bottom: 0;"><div class="progress-bar" style="width: 100%;"></div></div><h5>@@KZ_Loading_Text@@</h5></div></div>';
        this.config = {
        content: '<span style="color: #ecf0f1;">加载中...</span>',
        time: 0,
    };
    if (config != null) {
        if (typeof config === 'string') {
            this.config = Object.assign(this.config, {
            content: config
        });
        } else if (typeof config === 'object') {
            this.config = Object.assign(this.config, config);
        }
    }
    this.id = new Date().getTime().toString();
    this.state = 'hide';
   
      /*显示 */
    this.show = function () {
        $('.kz-loading[data-kzid=' + this.id + ']').modal({
            backdrop: 'static',
            keyboard: false
        });
        this.state = 'show';
        if (this.config.time > 0) {
            var that = this;
            setTimeout(function () {
                that.hide();
            }, this.config.time);
        }
    };
      /*隐藏 */
    this.hide = function (callback) {
        $('.kz-loading[data-kzid=' + this.id + ']').modal('hide');
        this.state = 'hide';
        if (callback) {
            callback();
        }
    };
      /*销毁dom */
    this.destroy = function () {
        var that = this;
        this.hide(function () {
            var node = $('.kz-loading[data-kzid=' + that.id + ']');
            node.next().remove();
            node.remove();
            that.show = function () {
                throw new Error('对象已销毁！');
            };
            that.hide = function () {};
            that.destroy = function () {};
        });
    }
   
    var domHtml = domTemplate.replace('@@KZ_Loadin_ID@@', this.id).replace('@@KZ_Loading_Text@@', this.config.content);
    $('body').append(domHtml);
    } else {
        return new KZ_Loading(config);
    }
}