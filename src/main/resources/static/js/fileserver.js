// 文件列表刷新核心方法，后续分页也用这个
function refreshDisplayList (content,begin, end) {
    var url = content?"/fileserver/file/refreshList":"/fileserver/file/list";
    $.ajax({
        type: "GET",
        url: url,
        data: null,
        success: function (data) {
            if(content){
                content.files = data;
            } else {
                fileList._data.files = data;
            }
        }
    });
}

var uploadModel = new Vue({
    el: "#vueUpload",
    data: {
        labelMessage: "upload",
        upfileName: null,
        upFile: null
    },
    methods: {
        tirggerFile: function(e){
            this.upFile = e.target.files[0];
            this.upfileName = e.target.value;
        },
        dpupload: function (e) {
            var formData = new FormData();
            formData.append("file", this.upFile);
            $.ajax({
                type: "POST",
                url: '/fileserver/upload/single',
                data: formData,
                processData: false,
                contentType: false,
                success: function (data) {
                    console.log(data);
                    refreshDisplayList(fileList._data);
                }
            });
        }
    }
});

var fileList = new Vue({
    el: '#vueListFiles',
    data: {
        files: [
            { name: '文件1' },
            { name: '文件2' },
            { name: '文件3' }
        ]
    },
    methods: {
        refresh: function () {
            refreshDisplayList(this);
        }
    }
});

// 初始化调用refreshDisplayList刷新文件列表,默认15个一页
refreshDisplayList(null, 0, 15);