/**
 * 公共调用js
 */

//var basepath = "/";

var _filePath = document.URL;
var _pathName = document.location.pathname;
var _projectName = _pathName.substring(1, _pathName.substr(1).indexOf('/') + 1);

//运行模式判断
if(_filePath.indexOf('/www/') != -1){
	//真机或模拟器模式
	_projectName = '/www/';
} else {
	_projectName = '/'+ _projectName +'/';
}
var docURL = _filePath;
if(document.URL.indexOf('?') != -1) {
	docURL = _filePath.substring(0 , document.URL.indexOf('?'));
} 

_filePath = _filePath.substring(docURL.indexOf(_projectName) + (_projectName.length), docURL.length);
var _pathDeep = (_filePath.split('/')).length - 1;

_filePath = '';
if (_pathDeep >= 1) {
	for (var i = 0; i < _pathDeep; i++) {
		_filePath += '../';
	}
}

document.write('<link rel="stylesheet" type="text/css" href="' + _filePath + 'inspinia/css/bootstrap.min.css"/>');
document.write('<link rel="stylesheet" type="text/css" href="' + _filePath + 'inspinia/font-awesome/css/font-awesome.css"/>');
document.write('<link rel="stylesheet" type="text/css" href="' + _filePath + 'inspinia/css/plugins/iCheck/custom.css"/>');
document.write('<link rel="stylesheet" type="text/css" href="' + _filePath + 'inspinia/css//plugins/chosen/chosen.css"/>');
document.write('<link rel="stylesheet" type="text/css" href="' + _filePath + 'inspinia/css/plugins/toastr/toastr.min.css"/>');/* 搜索型下拉框 */
document.write('<link rel="stylesheet" type="text/css" href="' + _filePath + 'inspinia/js/plugins/gritter/jquery.gritter.css"/>');
document.write('<link rel="stylesheet" type="text/css" href="' + _filePath + 'inspinia/css/plugins/sweetalert/sweetalert.css"/>');/*Sweet Alert*/
document.write('<link rel="stylesheet" type="text/css" href="' + _filePath + 'inspinia/css/plugins/ladda/ladda-themeless.min.css"/>');/*Ladda style*/
document.write('<link rel="stylesheet" type="text/css" href="' + _filePath + 'inspinia/css/animate.css"/>');
document.write('<link rel="stylesheet" type="text/css" href="' + _filePath + 'inspinia/css/plugins/jQueryUI/jquery-ui-1.10.4.custom.min.css"/>');/*jqgrid*/
document.write('<link rel="stylesheet" type="text/css" href="' + _filePath + 'inspinia/css/plugins/jqGrid/ui.jqgrid.css"/>');/*jqgrid*/
document.write('<link rel="stylesheet" type="text/css" href="' + _filePath + 'inspinia/css/style.css"/>');

/*Mainly scripts*/
document.write('<script src="' + _filePath + 'js/jquery-2.1.1.js"></script>');
document.write('<script src="' + _filePath + 'inspinia/js/bootstrap.min.js"></script>');
document.write('<script src="' + _filePath + 'inspinia/js/plugins/metisMenu/jquery.metisMenu.js"></script>');
document.write('<script src="' + _filePath + 'inspinia/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>');
/*Pop scripts*/
document.write('<script src="' + _filePath + 'js/plug-in.js"></script>');
/*Flot scripts*/
document.write('<script src="' + _filePath + 'inspinia/js/plugins/flot/jquery.flot.js"></script>');
document.write('<script src="' + _filePath + 'inspinia/js/plugins/flot/jquery.flot.tooltip.min.js"></script>');
document.write('<script src="' + _filePath + 'inspinia/js/plugins/flot/jquery.flot.spline.js"></script>');
document.write('<script src="' + _filePath + 'inspinia/js/plugins/flot/jquery.flot.resize.js"></script>');
document.write('<script src="' + _filePath + 'inspinia/js/plugins/flot/jquery.flot.pie.js"></script>');
/*Peity scripts*/
document.write('<script src="' + _filePath + 'inspinia/js/plugins/peity/jquery.peity.min.js"></script>');
document.write('<script src="' + _filePath + 'inspinia/js/demo/peity-demo.js"></script>');
/*jqGrid scripts*/
document.write('<script src="' + _filePath + 'inspinia/js/plugins/jqGrid/i18n/grid.locale-en.js"></script>');
document.write('<script src="' + _filePath + 'inspinia/js/plugins/jqGrid/jquery.jqGrid.min.js"></script>');
/*Custom and plugin scripts*/
document.write('<script src="' + _filePath + 'inspinia/js/inspinia.js"></script>');
document.write('<script src="' + _filePath + 'inspinia/js/plugins/pace/pace.min.js"></script>');
/*jQuery UI scripts*/
document.write('<script src="' + _filePath + 'inspinia/js/plugins/jquery-ui/jquery-ui.min.js"></script>');
/*Chosen*/
document.write('<script src="' + _filePath + 'inspinia/js/plugins/chosen/chosen.jquery.js"></script>');
/*GRITTER scripts*/
document.write('<script src="' + _filePath + 'inspinia/js/plugins/gritter/jquery.gritter.min.js"></script>');
/*Sparkline scripts*/
document.write('<script src="' + _filePath + 'inspinia/js/plugins/sparkline/jquery.sparkline.min.js"></script>');
document.write('<script src="' + _filePath + 'inspinia/js/demo/sparkline-demo.js"></script>');
/*ChartJS scripts*/
document.write('<script src="' + _filePath + 'inspinia/js/plugins/chartJs/Chart.min.js"></script>');
/*iCheck scripts*/
document.write('<script src="' + _filePath + 'inspinia/js/plugins/iCheck/icheck.min.js"></script>');
/*Toastr scripts*/
document.write('<script src="' + _filePath + 'inspinia/js/plugins/toastr/toastr.min.js"></script>');
/*Sweet alert*/
document.write('<script src="' + _filePath + 'inspinia/js/plugins/sweetalert/sweetalert.min.js"></script>');
/*Tinycon*/
document.write('<script src="' + _filePath + 'inspinia/js/plugins/tinycon/tinycon.min.js"></script>');
/*Ladda*/
document.write('<script src="' + _filePath + 'inspinia/js/plugins/ladda/spin.min.js"></script>');
document.write('<script src="' + _filePath + 'inspinia/js/plugins/ladda/ladda.min.js"></script>');
document.write('<script src="' + _filePath + 'inspinia/js/plugins/ladda/ladda.jquery.min.js"></script>');
/*Jquery Validate*/
document.write('<script src="' + _filePath + 'inspinia/js/plugins/validate/jquery.validate.min.js"></script>');
/*conf scripts*/
document.write('<script src="' + _filePath + 'js/config.js"></script>');
document.write('<script src="' + _filePath + 'js/tool.js"></script>');