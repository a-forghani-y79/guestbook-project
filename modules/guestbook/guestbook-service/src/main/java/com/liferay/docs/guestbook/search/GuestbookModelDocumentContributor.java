package com.liferay.docs.guestbook.search;

import com.liferay.docs.guestbook.model.Guestbook;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.search.spi.model.index.contributor.ModelDocumentContributor;
import org.osgi.service.component.annotations.Component;

import java.util.Locale;

@Component(immediate = true,
        property = "indexer.class.name=com.liferay.docs.guestbook.model.Guestbook",
        service = ModelDocumentContributor.class
)
public class GuestbookModelDocumentContributor implements ModelDocumentContributor<Guestbook> {

    private static final Log _log =LogFactoryUtil.getLog(GuestbookModelDocumentContributor.class);
    @Override
    public void contribute(Document document, Guestbook baseModel) {
        try {
            document.addDate(Field.MODIFIED_DATE, baseModel.getModifiedDate());
            Locale locale = PortalUtil.getSiteDefaultLocale(baseModel.getGroupId());
            String localizedTitle = LocalizationUtil.getLocalizedName(Field.TITLE,locale.toString());
            document.addText(localizedTitle,baseModel.getName());
        } catch (PortalException pe) {
            if (_log.isWarnEnabled()){
                _log.warn("Unable to index guestbook "+ baseModel.getGuestbookId() +" name:"+ baseModel.getName(),pe);
            }
        }
    }
}
