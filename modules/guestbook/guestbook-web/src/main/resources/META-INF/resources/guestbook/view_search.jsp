<%@ page import="com.liferay.portal.kernel.search.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.liferay.portal.kernel.util.*" %>
<%@ page import="com.liferay.portal.kernel.exception.PortalException" %>
<%@ page import="com.liferay.portal.kernel.exception.SystemException" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.liferay.portal.kernel.log.Log" %>
<%@ page import="com.liferay.portal.kernel.log.LogFactoryUtil" %>
<%@ include file="../init.jsp" %>

<% String keywords = ParamUtil.getString(request, "keywords");
    long guestbookId = ParamUtil.getLong(request, "guestbookId");
%>


<portlet:renderURL var="searchURL"><portlet:param name="mvcPath"
                                                  value="/guestbook/veiw_search.jsp"/></portlet:renderURL>
<portlet:renderURL var="viewURL"><portlet:param name="mvcPath" value="/guestbook/veiw.jsp"/></portlet:renderURL>

<aui:form action="${searchURL}" name="fm">
    <liferay-ui:header title="back" backURL="${viewURL}"/>
    <div class="row">
        <div class="col-md-8">
            <aui:input name="keywords" inlineLabel="left" label="" placeholder="search-entries" size="256"/>
        </div>
        <div class="col-md-4">
            <aui:button type="submit" value="search"/>
        </div>
    </div>
</aui:form>

<%
    SearchContext searchContext = SearchContextFactory.getInstance(request);
    searchContext.setKeywords(keywords);
    searchContext.setAttribute("paginationType", "more");
    searchContext.setStart(0);
    searchContext.setEnd(10);

    Indexer<GuestbookEntry> indexer = IndexerRegistryUtil.getIndexer(GuestbookEntry.class);
    Hits hits = indexer.search(searchContext);
    List<GuestbookEntry> entries = new ArrayList<GuestbookEntry>();
    for (int i = 0; i < hits.getDocs().length; i++) {
        Document document = hits.doc(i);
        long entryId = GetterUtil.getLong(document.get(Field.ENTRY_CLASS_PK));
        GuestbookEntry guestbookEntry = null;

        try {
            guestbookEntry = GuestbookEntryLocalServiceUtil.getGuestbookEntry(entryId);
        } catch (PortalException pe) {
            _log.error(pe.getLocalizedMessage());
        } catch (SystemException se) {
            _log.error(se.getLocalizedMessage());
        }

        entries.add(guestbookEntry);
    }
    List<Guestbook> guestbooks = GuestbookLocalServiceUtil.getGuestbooks(scopeGroupId);
    Map<String, String> guestbookMap = new HashMap<>();
    for (Guestbook guestbook : guestbooks) {
        guestbookMap.put(Long.toString(guestbook.getGuestbookId()), guestbook.getName());
    }
%>

<liferay-ui:search-container delta="10"
                             emptyResultsMessage="no-entries-were-found"
                             total="<%=entries.size()%>"
>
    <liferay-ui:search-container-results results="<%=entries%>"/>
    <liferay-ui:search-container-row className="com.liferay.docs.guestbook.model.GuestbookEntry"
                                     keyProperty="entryId"
                                     modelVar="entry"
                                     escapedModel="<%=true%>">
        <liferay-ui:search-container-column-text
                name="guestbook"
                value="<%=guestbookMap.get(Long.toString(entry.getGuestbookId()))%>"/>
        <liferay-ui:search-container-column-text
                property="message"/>
        <liferay-ui:search-container-column-text
                property="name"/>
        <liferay-ui:search-container-column-jsp path="/guestbook/entry_actions.jsp" align="right"/>

    </liferay-ui:search-container-row>

    <liferay-ui:search-iterator/>

</liferay-ui:search-container>
<%!
private static Log _log = LogFactoryUtil.getLog("html.guestbook.view_search_jsp");
%>