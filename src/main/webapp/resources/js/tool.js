//正确的返回状态
var STATUS_SUCCESS= '00' ;
//错误的返回状态
var STATUS_ERROR = '01' ;

/**
 * js 检验是否为空
 * @return
 */
function isEmpty(obj){
	return obj==null || obj=='' || obj==undefined ||  obj=='null' || obj=='undefined' ;
}

/**
 * 日期转str yyyymmdd
 * @param myDate
 * @returns {String}
 */
function dateToStr(myDate){
	var year =myDate.getFullYear(); 
	var mon = toTwo(myDate.getMonth()+1);  
	var day = toTwo(myDate.getDate());   
	var currentDateStr= ''+year+mon+day;
	return currentDateStr;
}
/**
 * 日期转str yyyy-mm-dd
 * @param myDate
 * @returns {String}
 */
function dateToStr2(myDate){
	var year =myDate.getFullYear(); 
	var mon = toTwo(myDate.getMonth()+1);  
	var day = toTwo(myDate.getDate());   
	var currentDateStr= ''+year+'-'+mon+'-'+day;
	return currentDateStr;
}
/**
 * 保证日期的月和日是两位数的
 * @param {Object} arg
 */
function toTwo(arg) {
	var argLength = ('' + arg).length;
	if(1 == argLength) {
		return '0' + arg;
	}
	return arg;
}

/**
 * 初始化查询条件的 日期
 * @param {Object} dateSelect
 */
function getDateInfo(dateSelect){
	document.getElementById(dateSelect).innerHTML = dateToStr2(new Date());
	
	document.getElementById(dateSelect).addEventListener('tap', function() {
		var dDate = new Date();
		var minDate = new Date();
		minDate.setFullYear(1949, 0, 1);
		var maxDate = new Date();
		maxDate.setFullYear(2100, 11, 31);
		plus.nativeUI.pickDate(function(e) {
			var d = e.date;  
			document.getElementById(dateSelect).innerHTML = (d.getFullYear() + "-" + toTwo(d.getMonth() + 1) + "-" + toTwo(d.getDate()) );
		}, function(e) {
			//alert("您没有选择日期");
		}, {
			title: "请选择日期",
			date: dDate,
			minDate: minDate,
			maxDate: maxDate
		});
	})
}

/* iCheck相关操作 start */
/*
iCheck
*/
//初始化
function iCheckInit() {
    $('.i-checks').iCheck({
        checkboxClass: 'icheckbox_square-green',
        radioClass: 'iradio_square-green',
    });
}
//选中数量
function selectedCount(name) {
    return $("input[name='" + name + "']:checked").length;
}
//全选
function selectAll(name) {
    $('input[name="' + name + '"]').iCheck('check');
}
//全不选
function selectNone(name) {
    $('input[name="' + name + '"]').iCheck('uncheck');
}
/* iCheck相关操作 end */

/**
 * 重新加载表格数据
 * @param {Object} gridTableId	表格ID
 * @param {Object} params		重新加载参数 
 * 		{
 *           url : "list.do",
 *           datatype : 'json',
 *           //发送数据  
 *           postData : {"name":name ,"age":age,"gender":gender,"posswrod":posswrod,
 *                      "seraly":seraly,"position":position,"dept":dept,"remark":remark},
 *           page : 1          
 *       }
 */
function reloadGridData(gridTableId, params){
	if(isEmpty(params)){
		params = {};
	}
	$("#"+gridTableId).jqGrid('setGridParam',params).trigger("reloadGrid");
}

/**
 * 清空表格数据
 * @param {Object} gridTableId  表格元素id
 */
function clearGridData(gridTableId){
	$("#"+gridTableId).jqGrid('clearGridData');
}

function resetForm(formId){
	 $("#"+formId)[0].reset();
}
