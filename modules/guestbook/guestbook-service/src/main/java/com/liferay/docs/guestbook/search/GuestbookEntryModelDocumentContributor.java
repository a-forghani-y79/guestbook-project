package com.liferay.docs.guestbook.search;

import com.liferay.docs.guestbook.model.Guestbook;
import com.liferay.docs.guestbook.model.GuestbookEntry;
import com.liferay.docs.guestbook.service.GuestbookLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.search.spi.model.index.contributor.ModelDocumentContributor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Locale;

@Component(immediate = true,
        property = "indexer.class.name=com.liferay.docs.guestbook.model.GuestbookEntry",
        service = ModelDocumentContributor.class)
public class GuestbookEntryModelDocumentContributor implements ModelDocumentContributor<GuestbookEntry> {

    private static final Log _log = LogFactoryUtil.getLog(GuestbookEntryModelDocumentContributor.class);

    @Reference
    private GuestbookLocalService _guestbookLocalService;

    @Override
    public void contribute(Document document, GuestbookEntry baseModel) {
        try {
            Locale defaultLocale = PortalUtil.getSiteDefaultLocale(baseModel.getGroupId());
            document.addDate(Field.MODIFIED_DATE, baseModel.getModifiedDate());
            document.addText("entryEmail", baseModel.getEmail());
            String localizedTitle = LocalizationUtil.getLocalizedName(Field.TITLE, defaultLocale.toString());
            String localizedContent = LocalizationUtil.getLocalizedName(Field.CONTENT, defaultLocale.toString());
            document.addText(localizedTitle, baseModel.getName());
            document.addText(localizedContent, baseModel.getMessage());

            long guestbookId = baseModel.getGuestbookId();
            Guestbook guestbook = _guestbookLocalService.getGuestbook(guestbookId);

            String guestbookName = guestbook.getName();
            String localizedGuestbookName = LocalizationUtil.getLocalizedName(Field.NAME, defaultLocale.toString());
            document.addText(localizedGuestbookName, guestbookName);

        } catch (PortalException pe) {
            if (_log.isWarnEnabled())
                _log.warn("unable to index entry " + baseModel.getEntryId(), pe);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
