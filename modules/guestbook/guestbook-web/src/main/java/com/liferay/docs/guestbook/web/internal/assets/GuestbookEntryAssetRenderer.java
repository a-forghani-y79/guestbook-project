package com.liferay.docs.guestbook.web.internal.assets;

import com.liferay.asset.kernel.model.BaseJSPAssetRenderer;
import com.liferay.docs.guestbook.constants.GuestbookPortletKeys;
import com.liferay.docs.guestbook.model.GuestbookEntry;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.service.persistence.PortletUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GuestbookEntryAssetRenderer extends BaseJSPAssetRenderer<GuestbookEntry> {
    private GuestbookEntry _guestbookEntry;
    private final ModelResourcePermission<GuestbookEntry> _guestbookEntryModelResourcePermission;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public GuestbookEntryAssetRenderer(GuestbookEntry _guestbookEntry, ModelResourcePermission<GuestbookEntry> _guestbookEntryModelResourcePermission) {
        this._guestbookEntry = _guestbookEntry;
        this._guestbookEntryModelResourcePermission = _guestbookEntryModelResourcePermission;
    }

    @Override
    public boolean hasEditPermission(PermissionChecker permissionChecker) throws PortalException {
        try {
            return _guestbookEntryModelResourcePermission.contains(permissionChecker,
                    _guestbookEntry, ActionKeys.UPDATE);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return false;
    }

    @Override
    public boolean hasViewPermission(PermissionChecker permissionChecker) throws PortalException {
        try {
            return _guestbookEntryModelResourcePermission.contains(permissionChecker,
                    _guestbookEntry, ActionKeys.VIEW);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return false;
    }

    @Override
    public String getJspPath(HttpServletRequest httpServletRequest, String template) {
        if (template.equals(TEMPLATE_FULL_CONTENT)) {
            httpServletRequest.setAttribute("gb_entry", _guestbookEntry);
            return "/asset/entry/" + template + ".jsp";
        } else
            return null;
    }

    @Override
    public GuestbookEntry getAssetObject() {
        return _guestbookEntry;
    }

    @Override
    public long getGroupId() {
        return _guestbookEntry.getGroupId();
    }

    @Override
    public long getUserId() {
        return _guestbookEntry.getUserId();
    }

    @Override
    public String getUserName() {
        return _guestbookEntry.getUserName();
    }

    @Override
    public String getUuid() {
        return _guestbookEntry.getUuid();
    }

    @Override
    public String getClassName() {
        return GuestbookEntry.class.getName();
    }

    @Override
    public long getClassPK() {
        return _guestbookEntry.getEntryId();
    }

    @Override
    public String getSummary(PortletRequest portletRequest, PortletResponse portletResponse) {
        return "Name: " + _guestbookEntry.getName() + " Message: " + _guestbookEntry.getMessage();
    }

    @Override
    public String getTitle(Locale locale) {
        return _guestbookEntry.getName();
    }

    @Override
    public boolean include(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String template) throws Exception {
        httpServletRequest.setAttribute("ENTRY", _guestbookEntry);
        httpServletRequest.setAttribute("HtmlUtil", HtmlUtil.getHtml());
        httpServletRequest.setAttribute("StringUtil", new StringUtil());
        return super.include(httpServletRequest, httpServletResponse, template);
    }

    @Override
    public PortletURL getURLEdit(LiferayPortletRequest liferayPortletRequest, LiferayPortletResponse liferayPortletResponse) throws Exception {
        PortletURL portletURL = liferayPortletResponse.createLiferayPortletURL(
                getControlPanelPlid(liferayPortletRequest), GuestbookPortletKeys.GUESTBOOK,
                PortletRequest.RENDER_PHASE);

        portletURL.setParameter("mvcPath", "/guestbook/edit_entry.jsp");
        portletURL.setParameter("entryId", String.valueOf(_guestbookEntry.getEntryId()));
        portletURL.setParameter("showback", Boolean.FALSE.toString());

        return portletURL;
    }

    @Override
    public String getURLView(LiferayPortletResponse liferayPortletResponse, WindowState windowState) throws Exception {
        return super.getURLView(liferayPortletResponse, windowState);
    }

    @Override
    public boolean isPrintable() {
        return true;
    }

    @Override
    public String getURLViewInContext(LiferayPortletRequest liferayPortletRequest, LiferayPortletResponse liferayPortletResponse, String noSuchEntryRedirect) throws Exception {
        try {
            long plid = PortalUtil.getPlidFromPortletId(-_guestbookEntry.getGroupId(),
                    GuestbookPortletKeys.GUESTBOOK);
            PortletURL portletURL;
            if (plid == LayoutConstants.DEFAULT_PLID) {
                portletURL = liferayPortletResponse.createLiferayPortletURL(
                        getControlPanelPlid(liferayPortletRequest), GuestbookPortletKeys.GUESTBOOK,
                        PortletRequest.RENDER_PHASE);
            } else
                portletURL = PortletURLFactoryUtil.create(liferayPortletRequest,
                        GuestbookPortletKeys.GUESTBOOK, plid, PortletRequest.RENDER_PHASE);

            portletURL.setParameter("mvcPath", "/guestbook/view_entry.jsp");
            portletURL.setParameter("entryId", String.valueOf(_guestbookEntry.getEntryId()));

            String currentUrl = PortalUtil.getCurrentURL(liferayPortletRequest);

            portletURL.setParameter("redirect", currentUrl);
            return portletURL.toString();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return noSuchEntryRedirect;
    }
}
