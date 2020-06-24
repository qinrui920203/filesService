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
// 预览功能
function openShadow(type, url){
    var shadowDiv = $("#shadowBox");
    var displayImg = shadowDiv.find("img").hide(),
        displayVideo = shadowDiv.find("video").hide();
    if(type == "img") {
        shadowDiv.show();
        displayImg.attr("src", url);
        displayImg.show();
    }
}
function closeShadow(){
    $("#shadowBox").hide();
}

// 大文件分块上传核心方法
function bigFileUpLoad(file, formData, totalSize, blockSize, blockCount, fileIndex){
    var start = fileIndex * blockSize;
    var end = Math.min(totalSize, start+blockSize);
    var upBlock = file.slice(start, end);
    bigFileUpload.$data.rate = start/end * 100;  // 计算进度
    if(start >= end){
        return ;
    }

    formData.set('data', upBlock);
    formData.set('index', fileIndex);

    $.ajax({
        url: '/fileserver/upload/big',
        type: 'post',
        data: formData,
        processData: false,
        contentType: false,
        success: function(res){
            var newIndex = ++fileIndex;
            formData.set('data', null);
            formData.set('index', null);
            bigFileUpLoad(file, formData, totalSize, blockSize, blockCount, newIndex);
        }
    });
}
var uploadModel = new Vue({
    el: "#vueUpload",
    data: {
        labelMessage: "less-20M-upload",
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

var bigFileUpload = new Vue({
    el: "#vueBigFileUpload",
    data: {
        labelMessage: "20M-more-big-file-upload",
        rate: 0,
        upfileName: null,
        upFile: null
    },
    methods: {
        tirggerFile: function(e){
            this.upFile = e.target.files[0];
            this.upfileName = e.target.value;
        },
        bigUpload: function(e){
            var fileIndex = 0,
                bigFile = this.upFile;
            var totalSize = bigFile.size;
            var blockSize = 1024 * 1024 * 5;
            var blockCount = Math.ceil(totalSize/blockSize);

            var formData = new FormData();
            var fileCurName = this.upfileName.substring(this.upfileName.lastIndexOf('\\')+1);
            formData.append('fileName', fileCurName);
            formData.append('totalCount', blockCount);

            bigFileUpLoad(bigFile, formData, totalSize, blockSize, blockCount, fileIndex);
        }
    }
});

var fileList = new Vue({
    el: '#vueListFiles',
    data: {
        baseUrl: "download/getFileByName?fileName=",
        downloadUrl: "",
        files: [
            { name: '文件1' },
            { name: '文件2' },
            { name: '文件3' }
        ]
    },
    methods: {
        refresh: function () {
            refreshDisplayList(this);
        },
        downloadFile: function (event) {
            var fileName = event.target.getAttribute("dataValue");
            this.downloadUrl = this.baseUrl+fileName;
        },
        viewImg: function (event){
            var imgUrl = event.target.getAttribute("dataValue");
            openShadow("img", this.baseUrl+imgUrl);
        }
    }
});

// 初始化调用refreshDisplayList刷新文件列表,默认15个一页
refreshDisplayList(null, 0, 15);