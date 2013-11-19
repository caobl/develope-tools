<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<title>HTML5文件上传</title>
<meta charset="utf-8">
<style></style>
</head>
<body>
    <section id="main-content">
        <h2>文件上传</h2>
        <form action="upload" method="post" enctype="multipart/form-data">
            <input id="file" type="file" multiple accept="*.xls" name="file" required="required"> <input type="submit" value="上传" />
        </form>
    </section>
</body>
</html>
