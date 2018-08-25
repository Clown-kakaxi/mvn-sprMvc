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

document.write('<script src="' + _filePath + 'js/vue/vue.js"></script>');
