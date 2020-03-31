function vminfo(uuid) {
    $.ajax({
        url: apiAddress + 'product/vminfo',
        type: 'post',
        dataType: 'json',
        data: {token: tk, vmuuid: uuid},
        complete: () => {
            loading.hide();
        },
        success: (data) => {
            var state = data.state;
            var datas = data.data;
            if(state == 200) {
                $('.uuid').text(datas.uuid);
                $('.vmstate').html(vmStateFilter(datas.vmstate));
                $('.serviceregion').text(serviceregion);
                $('.exampleName').text(datas.examplename);
                $('.publicip').text(publicip);
                $('.ostype').text(datas.ostype);
                $('.cpu').text(datas.cpu + "核");
                $('.memory').text(datas.memory + "MB");
                $('.payment').text(datas.payment);

                $('.time').text(getTime(datas.time));
                $('.createtime').text(getTime(datas.createtime));
                
                if(datas.base64Snapshot != null) {
                    $('.img-snapshot').attr('src', 'data:image/png;base64,' + datas.base64Snapshot);
                } else {
                    $('.img-snapshot').attr('src', '../image/zw.png');
                }

                let currcreateprogress = datas.currcreateprogress;
                if(currcreateprogress != -1) {
                    $('.createProgress-info-frame').css({
                        'display': 'block'
                    });
                    $('.start-btn').attr('disabled', '');
                    $('.stop-btn').attr('disabled', '');
                    $('#createProgress .progress-bar').css({
                        'width': currcreateprogress + '%'
                    });
                } else {
                    $('.createProgress-info-frame').css({
                        'display': 'none'
                    });
                    $('.start-btn').removeAttr('disabled', '');
                    $('.stop-btn').removeAttr('disabled', '');
                }
                
            }
        }
    });
}

function power(type) {
    $.ajax({
        url: apiAddress + 'vm/power',
        type: 'post',
        dataType: 'json',
        data: {token: tk, vmuuid: uuid, type: type},
        success: (data) => {
            var state = data.state;
            var datas = data.data;
            if(state == 200) {
                
            } else {
                window.location.href="./login.html";
            }
        },
        error: (data) => {
            //alert('无法连接至服务器');
        }
    });
}

function portMapperList() {
    $.ajax({
        url: apiAddress + 'vm/portmapperlist',
        type: 'post',
        dataType: 'json',
        data: {token: tk, vmuuid: uuid},
        complete: () => {
            // 获取资源信息
            vminfo(uuid);
        },
        success: (data) => {
            var state = data.state;
            var datas = data.data;
            if(state == 200) {
                $(".portMappingTable").bootstrapTable("removeAll");
                for(let i = 0; i < datas.length; i++) {
                    $(".portMappingTable").bootstrapTable('insertRow',{index: i, row:{
                        name: datas[i].name,
                        agreement: datas[i].agreement,
                        intranetPort: datas[i].intranetPort,
                        externalPort: datas[i].externalPort
                    }});
                }
            }
        }
    });
}

function addPortMapper(portName, agreement, intranetPort, externalPort) {
    var agreementType = 1;
    switch(agreement) {
        case 'TCP':
            agreementType = 1;
            break;
        case 'UDP':
            agreementType = 2;
            break;
    }
    $.ajax({
        url: apiAddress + 'vm/addportmapper',
        type: 'post',
        data: {token: tk, vmuuid: uuid, name: portName, agreement: agreementType, intranetPort: intranetPort, externalPort: externalPort},
        success: (data) => {
            let state = data.state;
            if(state == 200) {
                portMapperList();
            }
            if(state == 400) {
                Modal("端口操作提示", data.msg);
            }
        }
    });
}

function deletePortMapper(portName) {
    $.ajax({
        url: apiAddress + 'vm/deleteportmapper',
        type: 'post',
        data: {token: tk, vmuuid: uuid, name: portName},
        success: (data) => {
            let state = data.state;
            if(state == 200) {
                portMapperList();
            }
        },
        error: (data) => {
            Modal("端口操作提示", "无法删除该端口");
        }
    });
}

function osList() {
    $.ajax({
        url: apiAddress + 'vm/oslist',
        type: 'post',
        data: {token: tk, vmuuid: uuid},
        success: (data) => {
            let state = data.state;
            let datas = data.data;
            if(state == 200) {
                let reinstallSystem_select = $('.reinstallSystem-select');
                reinstallSystem_select.html('');
                for(let i = 0; i < datas.length; i++) {
                    reinstallSystem_select.append('<option value="' + (i + 1) + '">' + datas[i].ostype + '</option>');
                }
            }
        }
    });
}

function changeOs(osId) {
    $.ajax({
        url: apiAddress + 'vm/changeos',
        type: 'post',
        data: {token: tk, vmuuid: uuid, osid: osId},
        success: (data) => {
            let state = data.state;
            let datas = data.data;
            if(state == 200) {
                location.reload();
            }
            if(state == 400) {
                Modal("重装系统提示", data.msg);
            }
        }
    });
}