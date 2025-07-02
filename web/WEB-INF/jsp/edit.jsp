<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <title>Редактирование резюме</title>
</head>
<body>
<jsp:include page="/WEB-INF/views/fragments/header.jsp"/>
<section>
    <h2>${resume.uuid.isEmpty() ? 'Новый резюме' : 'Редактирование резюме'}</h2>
    <c:if test="${not empty error}">
        <div style="color:red">${error}</div>
    </c:if>
    <form method="post" action="resume">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size="50" value="${resume.fullName}" required pattern="^[A-Za-zА-Яа-яЁё\s-]{2,50}$"></dd>
        </dl>
        <c:forEach var="type" items="${resume.contacts.keySet()}">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type}" size="50" value="${resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>
        <c:forEach var="type" items="${resume.sections.keySet()}">
            <c:set var="section" value="${resume.getSection(type)}"/>
            <c:if test="${not empty section}">
                <h3>${type.title}</h3>
                <c:choose>
                    <c:when test="${type == 'PERSONAL' || type == 'OBJECTIVE'}">
                        <textarea name="${type}" rows="5" cols="50">${section.content}</textarea>
                    </c:when>
                    <c:when test="${type == 'ACHIEVEMENT' || type == 'QUALIFICATIONS'}">
                        <textarea name="${type}" rows="5" cols="50"><c:forEach var="item" items="${section.items}">${item}\n</c:forEach></textarea>
                    </c:when>
                    <c:when test="${type == 'EXPERIENCE' || type == 'EDUCATION'}">
                        <c:forEach var="org" items="${section.organizations}" varStatus="orgStatus">
                            <dl>
                                <dt>Название организации:</dt>
                                <dd><input type="text" name="${type}${orgStatus.index}.name" size="50" value="${org.homePage.name}"></dd>
                            </dl>
                            <dl>
                                <dt>URL:</dt>
                                <dd><input type="text" name="${type}${orgStatus.index}.url" size="50" value="${org.homePage.url}"></dd>
                            </dl>
                            <c:forEach var="pos" items="${org.positions}" varStatus="posStatus">
                                <div style="margin-left: 30px">
                                    <dl>
                                        <dt>Начальная дата (MM/yyyy):</dt>
                                        <dd><input type="text" name="${type}${orgStatus.index}.position${posStatus.index}.startDate" size="10" value="${pos.startDate.format('MM/yyyy')}"></dd>
                                    </dl>
                                    <dl>
                                        <dt>Конечная дата (MM/yyyy):</dt>
                                        <dd><input type="text" name="${type}${orgStatus.index}.position${posStatus.index}.endDate" size="10" value="${pos.endDate.format('MM/yyyy')}"></dd>
                                    </dl>
                                    <dl>
                                        <dt>Должность:</dt>
                                        <dd><input type="text" name="${type}${orgStatus.index}.position${posStatus.index}.title" size="50" value="${pos.title}"></dd>
                                    </dl>
                                    <dl>
                                        <dt>Описание:</dt>
                                        <dd><textarea name="${type}${orgStatus.index}.position${posStatus.index}.description" rows="3" cols="50">${pos.description}</textarea></dd>
                                    </dl>
                                </div>
                            </c:forEach>
                            <dl>
                                <dt>Добавить позицию:</dt>
                                <dd><input type="text" name="${type}${orgStatus.index}.positionnew.title" size="50" placeholder="Новая должность"></dd>
                            </dl>
                        </c:forEach>
                        <dl>
                            <dt>Добавить организацию:</dt>
                            <dd><input type="text" name="${type}new.name" size="50" placeholder="Новое название"></dd>
                        </dl>
                    </c:when>
                </c:choose>
            </c:if>
        </c:forEach>
        <button type="submit">Сохранить</button>
        <button type="button" onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
</body>
</html>