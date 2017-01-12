function test(){
	$.ajax({
		type : 'post',
		url : '/line/linePo',
		data : '{id:1}',
		cache : false,
		dataType : 'json',
		success : function(data) {
			if(data.returnCode==1){
			}else{
				swal("系统提示", data.msg,"error");
			}
		}
	});
}