package com.liferay.docs.guestbook.workflow;

import com.liferay.docs.guestbook.model.GuestbookEntry;
import com.liferay.docs.guestbook.service.GuestbookEntryLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.ResourceActions;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.BaseWorkflowHandler;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowHandler;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

@Component(immediate = true, service = WorkflowHandler.class)
public class GuestbookEntryWorkflowHandler extends BaseWorkflowHandler<GuestbookEntry> {
    private ResourceActions resourceActions;
    private GuestbookEntryLocalService guestbookEntryLocalService;

    @Reference(unbind = "-")
    public void setResourceActions(ResourceActions resourceActions) {
        this.resourceActions = resourceActions;
    }

    @Reference(unbind = "-")
    public void setGuestbookEntryLocalService(GuestbookEntryLocalService guestbookEntryLocalService) {
        this.guestbookEntryLocalService = guestbookEntryLocalService;
    }

    @Override
    public String getClassName() {
        return GuestbookEntry.class.getName();
    }

    @Override
    public String getType(Locale locale) {
        return resourceActions.getModelResource(locale, getClassName());
    }

    @Override
    public GuestbookEntry updateStatus(int status, Map<String, Serializable> workflowContext) throws PortalException {
        long userId = GetterUtil.getLong((String) workflowContext.get(WorkflowConstants.CONTEXT_USER_ID));
        long resourcePK = GetterUtil.getLong((String) workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_CLASS_PK));
        ServiceContext serviceContext = (ServiceContext) workflowContext.get("serviceContext");
        long guestbookId = guestbookEntryLocalService.getGuestbookEntry(resourcePK).getGuestbookId();
        return guestbookEntryLocalService.updateStatus(userId, guestbookId, resourcePK, status, serviceContext);
    }
}
