<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <title>Курс JavaSE + Web</title>
</head>
<body>
<header>Приложение вебинара <a href="http://javawebinar.ru/basejava/" target="_blank">Практика Java. Разработка Web приложения."</a></header>
<h1>Курс JavaSE + Web</h1>
<h2>${param.name == null ? 'Список резюме' : 'Привет, ' += param.name += '! Список резюме'}</h2>
<table border="1">
    <tr>
        <th>UUID</th>
        <th>Full Name</th>
    </tr>
    <c:forEach var="resume" items="${resumes}">
        <tr>
            <td>${resume.uuid}</td>
            <td>${resume.fullName}</td>
        </tr>
    </c:forEach>
</table>
<footer>Приложение вебинара <a href="http://javawebinar.ru/basejava/" target="_blank">Практика Java. Разработка Web приложения."</a></footer>
</body>
</html>