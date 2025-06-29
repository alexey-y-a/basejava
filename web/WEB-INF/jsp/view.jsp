<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <title>Просмотр резюме</title>
</head>
<body>
<jsp:include page="/WEB-INF/views/fragments/header.jsp"/>
<section>
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="../img/pencil.png" alt="Edit"></a></h2>
    <c:if test="${not empty error}">
        <div style="color:red">${error}</div>
    </c:if>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry" type="java.util.Map.Entry<ru.javawebinar.basejava.model.ContactType, java.lang.String>"/>
            <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    </p>
    <c:forEach var="sectionEntry" items="${resume.sections}">
        <jsp:useBean id="sectionEntry" type="java.util.Map.Entry<ru.javawebinar.basejava.model.SectionType, ru.javawebinar.basejava.model.Section>"/>
        <c:set var="type" value="${sectionEntry.key}"/>
        <c:set var="section" value="${sectionEntry.value}"/>
        <c:if test="${not empty section}">
            <h3>${type.title}</h3>
            <c:choose>
                <c:when test="${type == 'PERSONAL' || type == 'OBJECTIVE'}">
                    <p>${section.content}</p>
                </c:when>
                <c:when test="${type == 'ACHIEVEMENT' || type == 'QUALIFICATIONS'}">
                    <ul>
                        <c:forEach var="item" items="${section.items}">
                            <li>${item}</li>
                        </c:forEach>
                    </ul>
                </c:when>
                <c:when test="${type == 'EXPERIENCE' || type == 'EDUCATION'}">
                    <c:forEach var="org" items="${section.organizations}">
                        <jsp:useBean id="org" type="ru.javawebinar.basejava.model.Organization"/>
                        <p>${org.homePage.name}<c:if test="${not empty org.homePage.url}"> (${org.homePage.url})</c:if></p>
                        <c:forEach var="pos" items="${org.positions}">
                            <jsp:useBean id="pos" type="ru.javawebinar.basejava.model.Organization.Position"/>
                            <p>${pos.startDate} - ${pos.endDate}<br>${pos.title}<c:if test="${not empty pos.description}"><br>${pos.description}</c:if></p>
                        </c:forEach>
                    </c:forEach>
                </c:when>
            </c:choose>
        </c:if>
    </c:forEach>
    <button onclick="window.history.back()">ОК</button>
</section>
<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
</body>
</html>