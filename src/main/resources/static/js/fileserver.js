function submit2(){
    debugger;
    var type = "file";
    var id = "cert";
    var formData = new FormData();
    formData.append(type, $("#"+id)[0].files[0]);
    $.ajax({
        type: "POST",
        url: '/fileserver/upload/single',
        data: formData,
        processData: false,
        contentType: false,
        success: function (data) {
            alert(data);
        }
    });
}