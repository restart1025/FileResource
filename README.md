# FileResource
书籍资料

```

 /**
  * 增加标签页
  */
 function addTab(options) {
     //option:
     //tabMainName:tab标签页所在的容器
     //tabName:当前tab的名称
     //tabTitle:当前tab的标题
     //tabUrl:当前tab所指向的URL地址
     var exists = checkTabIsExists(options.tabMainName, options.tabName);
     if(exists){
         $("#tab_a_"+options.tabName).click();
     } else {
         $("#"+options.tabMainName).append('<li id="tab_li_'+options.tabName+'"><a href="#tab_content_'+options.tabName+'" data-toggle="tab" id="tab_a_'+options.tabName+'"><button class="close closeTab" type="button" onclick="closeTab(this);">×</button>'+options.tabTitle+'</a></li>');
          
         //固定TAB中IFRAME高度
         mainHeight = $(document.body).height() - 5;
          
         var content = '';
         if(options.content){
             content = option.content;
         } else {
             content = '<iframe src="' + options.tabUrl + '" width="100%" height="'+mainHeight+'px" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="yes" allowtransparency="yes"></iframe>';
         }
         $("#"+options.tabContentMainName).append('<div id="tab_content_'+options.tabName+'" role="tabpanel" class="tab-pane" id="'+options.tabName+'">'+content+'</div>');
         $("#tab_a_"+options.tabName).click();
     }
 }
  
  
 /**
  * 关闭标签页
  * @param button
  */
 function closeTab (button) {
      
     //通过该button找到对应li标签的id
     var li_id = $(button).parent().parent().attr('id');
     var id = li_id.replace("tab_li_","");
      
     //如果关闭的是当前激活的TAB，激活他的前一个TAB
     if ($("li.active").attr('id') == li_id) {
         $("li.active").prev().find("a").click();
     }
      
     //关闭TAB
     $("#" + li_id).remove();
     $("#tab_content_" + id).remove();
 };
  
 /**
  * 判断是否存在指定的标签页
  * @param tabMainName
  * @param tabName
  * @returns {Boolean}
  */
 function checkTabIsExists(tabMainName, tabName){
     var tab = $("#"+tabMainName+" > #tab_li_"+tabName);
     //console.log(tab.length)
     return tab.length > 0;
 }

```
