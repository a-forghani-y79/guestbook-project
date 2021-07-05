package com.liferay.docs.guestbook.search;

import com.liferay.docs.guestbook.model.GuestbookEntry;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.search.spi.model.index.contributor.ModelIndexerWriterContributor;
import com.liferay.portal.search.spi.model.registrar.ModelSearchRegistrarHelper;
import com.liferay.portal.search.spi.model.result.contributor.ModelSummaryContributor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true)
public class GuestbookEntrySearchRegistrar {

    @Reference(target = "(indexer-class.name=com.liferay.docs.guestbook.model.GuestbookEntry")
    protected ModelIndexerWriterContributor<GuestbookEntry> modelIndexerWriterContributor;

    @Reference
    protected ModelSearchRegistrarHelper modelSearchRegistrarHelper;

    @Reference(target = "(indexer-class.name=com.liferay.docs.guestbook.model.GuestbookEntry")
    protected ModelSummaryContributor modelSummaryContributor;

    private ServiceRegistration<?> _serviceRegistration;

    @Activate
    public void activate(BundleContext bundleContext) {
        _serviceRegistration = modelSearchRegistrarHelper.register(GuestbookEntry.class, bundleContext, modelSearchDefinition -> {
            modelSearchDefinition.setDefaultSelectedFieldNames(Field.COMPANY_ID, Field.ENTRY_CLASS_NAME, Field.ENTRY_CLASS_PK,
                    Field.UID, Field.SCOPE_GROUP_ID, Field.GROUP_ID);
            modelSearchDefinition.setDefaultSelectedLocalizedFieldNames(Field.TITLE, Field.CONTENT);
            modelSearchDefinition.setModelIndexWriteContributor(modelIndexerWriterContributor);
            modelSearchDefinition.setModelSummaryContributor(modelSummaryContributor);
        });
    }

    @Deactivate
    public void deactivate() {
        _serviceRegistration.unregister();
    }
}
