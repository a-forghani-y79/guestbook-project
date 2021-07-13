package com.liferay.docs.guestbook.workflow;

import com.liferay.docs.guestbook.model.Guestbook;
import com.liferay.docs.guestbook.service.GuestbookLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.ResourceAction;
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
public class GuestbookWorkflowHandler extends BaseWorkflowHandler<Guestbook>{

    private ResourceActions resourceActions;

    @Reference(unbind = "-")
    public void setResourceActions(ResourceActions resourceActions) {
        this.resourceActions = resourceActions;
    }

    private GuestbookLocalService guestbookLocalService;

    @Reference(unbind = "-")
    public void setGuestbookLocalService(GuestbookLocalService guestbookLocalService) {
        this.guestbookLocalService = guestbookLocalService;
    }

    @Override
    public String getClassName() {
        return Guestbook.class.getName();
    }

    @Override
    public String getType(Locale locale) {
        return resourceActions.getModelResource(locale, getClassName());
    }

    @Override
    public Guestbook updateStatus(int status, Map<String, Serializable> workflowContext) throws PortalException {
        long userId = GetterUtil.getLong((String) workflowContext.get(WorkflowConstants.CONTEXT_USER_ID));
        long resourcePrimaryKey = GetterUtil.getLong((String)workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_CLASS_PK));
        ServiceContext serviceContext = (ServiceContext) workflowContext.get("serviceContext");
        return guestbookLocalService.updateStatus(userId, resourcePrimaryKey, status, serviceContext);
    }
}