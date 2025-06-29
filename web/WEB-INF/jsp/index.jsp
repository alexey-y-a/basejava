<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <title>Курс JavaSE + Web</title>
</head>
<body>
<jsp:include page="/WEB-INF/views/fragments/header.jsp"/>
<h1>Курс JavaSE + Web</h1>
<h2>${param.name == null ? 'Список резюме' : 'Привет, ' += param.name += '! Список резюме'}</h2>
<a href="resume?action=add"><img src="../img/add.png" alt="Add"></a>
<table border="1">
    <tr>
        <th>UUID</th>
        <th>Full Name</th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach var="resume" items="${resumes}">
        <tr>
            <td>${resume.uuid}</td>
            <td>${resume.fullName}</td>
            <td><a href="resume?uuid=${resume.uuid}&action=view"><img src="../img/view.png" alt="View"></a></td>
            <td><a href="resume?uuid=${resume.uuid}&action=delete"><img src="../img/delete.png" alt="Delete"></a></td>
        </tr>
    </c:forEach>
</table>
<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
</body>
</html>