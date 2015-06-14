

function layout_center_addTabFun(opts) {
		var t = $('#layout_center_tabs');
		if (t.tabs('exists', opts.title)) {
			t.tabs('select', opts.title);
		} else {
			t.tabs('add', opts);
		}
}

$(function(){
	$('#navid').tree({
		onClick: function(node){
//			alert(node.text+","+node.url);  // alert node text property when clicked
			console.info("click:"+node.text);
			if(node.attributes.folder=='1'){
				return ;
			}
			console.info("open url:"+node.attributes.url)	
			var url;
			if (node.attributes.url) {
				url = node.attributes.url;
			} else {
				url = '404.jsp';
			}
			console.info("open "+url);
			layout_center_addTabFun({
				title : node.text,
				closable : true,
				iconCls : node.iconCls,
				href : url
			});
		}
	});	
	

});
