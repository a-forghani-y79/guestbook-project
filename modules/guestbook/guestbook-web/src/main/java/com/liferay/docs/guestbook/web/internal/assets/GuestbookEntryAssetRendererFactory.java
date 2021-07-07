package com.liferay.docs.guestbook.web.internal.assets;


import com.liferay.asset.kernel.model.AssetRenderer;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.model.BaseAssetRendererFactory;
import com.liferay.docs.guestbook.constants.GuestbookPortletKeys;
import com.liferay.docs.guestbook.model.GuestbookEntry;
import com.liferay.docs.guestbook.service.GuestbookEntryLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;
import javax.servlet.ServletContext;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component(immediate = true,
        property = {"javax.portlet.name="+GuestbookPortletKeys.GUESTBOOK},
        service = AssetRendererFactory.class)
public class GuestbookEntryAssetRendererFactory
        extends BaseAssetRendererFactory<GuestbookEntry> {

    private ServletContext servletContext;
    private GuestbookEntryLocalService guestbookEntryLocalService;
    private static final boolean _LINKABLE = true;
    private static final String CLASS_NAME = GuestbookEntry.class.getName();
    private static final String TYPE = "entry";
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private ModelResourcePermission<GuestbookEntry> _guestbookEntryModelResourcePermission;

    @Reference(target = "(osgi.web.symbolicname=com.liferay.docs.guestbook.portlet)",
            unbind = "-")
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Reference(unbind = "-")
    public void setGuestbookEntryLocalService(GuestbookEntryLocalService
                                                      guestbookEntryLocalService) {
        this.guestbookEntryLocalService = guestbookEntryLocalService;
    }

    public GuestbookEntryAssetRendererFactory() {
        setClassName(CLASS_NAME);
        setLinkable(_LINKABLE);
        setPortletId(GuestbookPortletKeys.GUESTBOOK);
        setSearchable(true);
        setSelectable(true);
        System.out.println("hi ali alia aliaalia");
    }


    @Override
    public AssetRenderer<GuestbookEntry> getAssetRenderer(long classPK, int type)
            throws PortalException {
        GuestbookEntry guestbookEntry = guestbookEntryLocalService.getGuestbookEntry(classPK);
        GuestbookEntryAssetRenderer guestbookEntryAssetRenderer =
                new GuestbookEntryAssetRenderer(guestbookEntry,
                        _guestbookEntryModelResourcePermission);
        guestbookEntryAssetRenderer.setAssetRendererType(type);
        guestbookEntryAssetRenderer.setServletContext(servletContext);
        return guestbookEntryAssetRenderer;
    }

    @Override
    public String getClassName() {
        return CLASS_NAME;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean hasPermission(PermissionChecker permissionChecker,
                                 long classPK, String actionId) throws Exception {
        GuestbookEntry guestbookEntry = guestbookEntryLocalService.getGuestbookEntry(classPK);
        return _guestbookEntryModelResourcePermission.contains(permissionChecker,
                guestbookEntry, actionId);
    }

    @Override
    public PortletURL getURLAdd(LiferayPortletRequest liferayPortletRequest,
                                LiferayPortletResponse liferayPortletResponse,
                                long classTypeId) throws PortalException {
        PortletURL portletURL = null;

        try {
            ThemeDisplay themeDisplay = (ThemeDisplay) liferayPortletRequest.getAttribute(WebKeys.THEME_DISPLAY);

            portletURL = liferayPortletResponse.createLiferayPortletURL(getControlPanelPlid(themeDisplay),
                    GuestbookPortletKeys.GUESTBOOK, PortletRequest.RENDER_PHASE);
            portletURL.setParameter("mvcRenderCommandName", "/guestbook/edit_entry");
            portletURL.setParameter("showback", Boolean.FALSE.toString());
        } catch (PortalException e) {
        }

        return portletURL;
    }

    @Override
    public PortletURL getURLView(LiferayPortletResponse liferayPortletResponse, WindowState windowState) throws PortalException {
        LiferayPortletURL liferayPortletURL
                = liferayPortletResponse.createLiferayPortletURL(
                GuestbookPortletKeys.GUESTBOOK, PortletRequest.RENDER_PHASE);

        try {
            liferayPortletURL.setWindowState(windowState);
        } catch (WindowStateException wse) {
            logger.log(Level.SEVERE, wse.getMessage());
        }
        return liferayPortletURL;
    }

    @Override
    public boolean isLinkable() {
        return _LINKABLE;
    }

    @Override
    public String getIconCssClass() {
        return "pencil";
    }

}
