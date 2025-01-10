/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.dokument.entity;

import java.util.List;

import ch.dvbern.stip.api.dokument.service.CustomDokumentTypService;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.util.RequiredCustomDocsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

class CustomDocumentsRequiredDocumentProducerTest {
    private CustomDocumentsRequiredDocumentProducer producer;
    private CustomDokumentTypService service;
    private GesuchFormular gesuchFormular;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(CustomDokumentTypService.class);
        producer = new CustomDocumentsRequiredDocumentProducer(service);
        gesuchFormular = Mockito.mock(GesuchFormular.class);
    }

    @Test
    void noCustomDocumentsShouldBeRequiredTest(){
        when(service.getAllCustomDokumentTyps()).thenReturn(List.of());
        final var requiredCustomDocuments = producer.getRequiredDocuments(gesuchFormular);
        assertThat(requiredCustomDocuments.getValue().isEmpty(),is(true));
        assertThat(requiredCustomDocuments.getKey().equals(""),is(true));
    }

    @Test
    void customDocumentsShouldBeRequiredTest() {
        // todo: add unique constraint to name
        final var customDokumentTyp = new CustomDokumentTyp().setType("test").setDescription("description");
        when(service.getAllCustomDokumentTyps()).thenReturn(List.of(customDokumentTyp));
        assertThat(producer.getRequiredDocuments(gesuchFormular).getKey().equals("custom-documents"), is(true));
        RequiredCustomDocsUtil.requiresOneAndType(producer.getRequiredDocuments(gesuchFormular), customDokumentTyp);
    }
}
