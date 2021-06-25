package com.liferay.docs.guestbook.portlet;

import com.liferay.docs.guestbook.constants.GuestbookPortletKeys;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Component;

/**
 * @author jinos
 */
@Component(
		immediate = true,
		property = {
				"com.liferay.portlet.display-category=category.social",
				"javax.portlet.name=" + GuestbookPortletKeys.GUESTBOOK,
				"com.liferay.portlet.instanceable=false",
				"com.liferay.portlet.scopeable=true",
				"javax.portlet.display-name=Guestbook",
				"javax.portlet.expiration-cache=0",
				"javax.portlet.init-param.template-path=/",
				"javax.portlet.init-param.view-template=/guestbook/view.jsp",
				"javax.portlet.resource-bundle=content.Language",
				"javax.portlet.security-role-ref=power-user,user",
				"javax.portlet.supports.mime-type=text/html"
		},
		service = Portlet.class
)
public class GuestbookPortlet extends MVCPortlet {
}