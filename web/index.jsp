<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/3/26
  Time: 16:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>文件上传下载</title>
    <link rel="stylesheet" type="text/css" href="jquery-easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="jquery-easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="jquery-easyui/themes/color.css">
    <script type="text/javascript" src="jquery-easyui/jquery.min.js"></script>
    <script type="text/javascript" src="jquery-easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="jquery-easyui/locale/easyui-lang-zh_CN.js"></script>
</head>
<body>

<table id="dg" title="文件管理" class="easyui-datagrid" style="width:100%; height:800px"
       url="queryAll.action"
       toolbar="#toolbar" pagination="false"
       rownumbers="true" fitColumns="true" singleSelect="true">
    <thead>
    <tr>
        <th field="fid" width="100">文件编号</th>
        <th field="foldname" width="100">原文件名</th>
        <th field="fnewname" width="100">新文件名</th>
        <th field="fconententtype" width="100">文件类型</th>
        <th field="fpath" width="100">文件路径</th>
    </tr>
    </thead>
</table>
<div id="toolbar">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newAttachment()">添加文件</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
       onclick="downloadAttachment()">下载文件</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
       onclick="deleteAttachment()">删除文件</a>
</div>

<div id="dlg" class="easyui-dialog" style="width:400px" closed="true" buttons="#dlg-buttons">
    <form id="fm" method="post" novalidate style="margin:0;padding:20px 50px" enctype="multipart/form-data">
        <div style="margin-bottom:10px">
            <input name="mf" class="easyui-filebox" required="true" data-options="prompt:'请选择文件',buttonText:'添加文件'"
                   style="width:100%" id="btn_file">
        </div>
    </form>
</div>
<div id="dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveAttachment()" style="width:90px">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#dlg').dialog('close')" style="width:90px">关闭</a>
</div>
<script type="text/javascript">
    var url;
    function newAttachment() {
        $('#dlg').dialog('open').dialog('center').dialog('setTitle', '添加文件');
        $('#fm').form('clear');
        url = 'addAttachment.action';
    }

    function downloadAttachment() {
        var row = $('#dg').datagrid('getSelected');
        if (row) {
            $.messager.confirm('Confirm', '您确定要下载该文件么', function (r) {
                if (r) {
                    window.location.href = "downloadAttachment.action?fid=" + row.fid;
                }
            });
        }
    }

    function saveAttachment() {
        $('#fm').form('submit', {
            url: url,
            onSubmit: function () {
                return $(this).form('validate');
            },
            success: function (result) {
                var result = eval('(' + result + ')');
                if (result.status == "error") {
                    $.messager.show({
                        title: '失败',
                        msg: result.msg
                    });
                } else {
                    $.messager.show({
                        title: '成功',
                        msg: result.msg
                    });
                    $('#dlg').dialog('close');        // close the dialog
                    $('#dg').datagrid('reload');    // reload the user data
                }
            }
        });
    }

    function deleteAttachment() {
        var row = $('#dg').datagrid('getSelected');
        if (row) {
            $.messager.confirm('Confirm', '您确定要删除这个文件么', function (r) {
                if (r) {
                    $.post('deleteAttachment.action', {fid: row.fid}, function (result) {
                        if (result.status == "success") {
                            $.messager.show({
                                title: '提示',
                                msg : result.msg
                            });
                            $('#dg').datagrid('reload');    // reload the user data
                        } else {
                            $.messager.show({    // show error message
                                title: '失败',
                                msg: result.msg
                            });
                        }
                    }, 'json');
                }
            });
        }
    }
</script>
</body>
</html>
