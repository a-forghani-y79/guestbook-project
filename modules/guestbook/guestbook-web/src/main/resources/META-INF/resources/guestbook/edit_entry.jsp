
<%@include file="../init.jsp" %>
<liferay-portlet:renderURL var="viewURL">
    <portlet:param name="mcvPath" value="/guestbook/view.jsp"/>
</liferay-portlet:renderURL>
<liferay-portlet:actionURL name="addEntry" var="addEntryURL"/>
<%
    long entryId = ParamUtil.getLong(request, "entryId");
    GuestbookEntry entry = null;
    if (entryId > 0) {
        entry = GuestbookEntryLocalServiceUtil.getGuestbookEntry(entryId);
    }
    long guestbookId = ParamUtil.getLong(request, "guestbookId");
%>

<aui:form action="<%=addEntryURL%>" name="<portlet:namespace/>fm">
    <aui:model-context bean="<%=entry%>" model="<%=GuestbookEntry.class%>"/>
    <aui:fieldset>
        <aui:input name="name"/>
        <aui:input name="email"/>
        <aui:input name="message"/>
        <aui:input name="entryId" type="hidden"/>
        <aui:input name="guestbookId" type="hidden" value="<%=entry==null ? guestbookId : entry.getGuestbookId()%>"/>
    </aui:fieldset>
    <aui:button-row>
        <aui:button type="submit"/>
        <aui:button type="cancel" onClick="<%=viewURL.toString()%>"/>
    </aui:button-row>
</aui:form>