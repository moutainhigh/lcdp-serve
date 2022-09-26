<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign comment=model.tabComment>
<#assign system=vars.system>
<#assign domain=vars.domain>
<#assign tableName=model.tableName>
import rxAjax from '@/assets/js/ajax.js';

//${comment} api接口
export const ${class}Api = {};

${class}Api.baseUrl= '/api-${system}/${system}/${package}/${classVar}';
${class}Api.exportUrl= ${class}Api.baseUrl + '/export';

//查询列表
${class}Api.query=function (parameter) {
  var url= ${class}Api.baseUrl + '/query';
  return rxAjax.postJson(url,parameter).then (res => {
    return res.result
  })
}

/**
* 获取单记录
* @param pkId
* @returns {*}
*/
${class}Api.get =function(pkId) {
  var url= ${class}Api.baseUrl + '/get?pkId=' + pkId;
  return rxAjax.get(url);
}

//保存数据
${class}Api.save =function(parameter) {
  var url= ${class}Api.baseUrl + '/save';
  return rxAjax.postJson(url,parameter);
}

//删除数据
${class}Api.del =function(parameter) {
  var url= ${class}Api.baseUrl + '/del';
  return rxAjax.postUrl(url,parameter);
}

export  default ${class}Api;

