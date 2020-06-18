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
            formData.append(type, this.upFile);
            $.ajax({
                type: "POST",
                url: '/fileserver/upload/single',
                data: formData,
                processData: false,
                contentType: false,
                success: function (data) {
                    console.log(data);
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
    }
});

$.ajax({
    type: "GET",
    url: '/fileserver/file/list',
    data: null,
    success: function (data) {
        fileList._data.files = data;
    }
});