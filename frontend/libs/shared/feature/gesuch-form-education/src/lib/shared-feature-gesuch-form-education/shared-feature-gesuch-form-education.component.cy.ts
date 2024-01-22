import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideMockStore } from '@ngrx/store/testing';
import { TranslateTestingModule } from 'ngx-translate-testing';

import { provideMaterialDefaultOptions } from '@dv/shared/pattern/angular-material-config';
import { SharedEducationPO } from '@dv/shared/util-fn/e2e-helpers';
import { Ausbildungsstaette } from '@dv/shared/model/gesuch';

import { SharedFeatureGesuchFormEducationComponent } from './shared-feature-gesuch-form-education.component';

describe(SharedFeatureGesuchFormEducationComponent.name, () => {
  describe('form validity', () => {
    it('should be invalid if begin is not a date', () => {
      mountWithGesuch();
      SharedEducationPO.getFormBeginnDerAusbildung().type('gugus').blur();
      SharedEducationPO.getFormBeginnDerAusbildung().should(
        'have.class',
        'ng-invalid',
      );
    });
    it('should be invalid if end is not a date', () => {
      mountWithGesuch();
      SharedEducationPO.getFormEndeDerAusbildung().type('gugus').blur();
      SharedEducationPO.getFormEndeDerAusbildung().should(
        'have.class',
        'ng-invalid',
      );
    });
    it('should be valid if a past date is provided for begin', () => {
      mountWithGesuch();
      SharedEducationPO.getFormBeginnDerAusbildung().type('01.2020').blur();
      SharedEducationPO.getFormBeginnDerAusbildung().should(
        'not.have.class',
        'ng-invalid',
      );
    });
    it('should be valid if the begin date is before the end date', () => {
      mountWithGesuch();
      SharedEducationPO.getFormBeginnDerAusbildung().type('01.2019').blur();
      SharedEducationPO.getFormEndeDerAusbildung().type('01.2020').blur();
      SharedEducationPO.getFormBeginnDerAusbildung().should(
        'not.have.class',
        'ng-invalid',
      );
      SharedEducationPO.getFormEndeDerAusbildung().should(
        'not.have.class',
        'ng-invalid',
      );
    });
    it('should be invalid if the begin date is after the end date', () => {
      mountWithGesuch();
      SharedEducationPO.getFormBeginnDerAusbildung().type('01.2020').blur();
      SharedEducationPO.getFormEndeDerAusbildung().type('01.2019').blur();
      SharedEducationPO.getFormBeginnDerAusbildung().should(
        'not.have.class',
        'ng-invalid',
      );
      SharedEducationPO.getFormEndeDerAusbildung().should(
        'have.class',
        'ng-invalid',
      );
      SharedEducationPO.getForm().should('have.class', 'ng-invalid');
      SharedEducationPO.getForm()
        .find('mat-error')
        .should('include.text', 'YearAfterStart');
    });
    it('should have disabled inputs depending on each previous input state', () => {
      const fields = {
        notFound: 'form-education-ausbildungNichtGefunden',
        land: 'form-education-ausbildungsland',
        staette: 'form-education-ausbildungsstaette',
        gang: 'form-education-ausbildungsgang',
        alternativ: {
          staette: 'form-education-alternativeAusbildungsstaette',
          gang: 'form-education-alternativeAusbildungsgang',
        },
        fachrichtung: 'form-education-fachrichtung',
      };
      mountWithGesuch();

      cy.getBySel(fields.staette).should('be.disabled');
      cy.getBySel(fields.land).click();
      cy.get('mat-option').eq(0).click();
      cy.getBySel(fields.staette).should('not.be.disabled');

      cy.getBySel(fields.gang).should('have.class', 'mat-mdc-select-disabled');
      cy.getBySel(fields.staette).click();
      cy.get('mat-option').eq(0).click();
      cy.getBySel(fields.gang).should(
        'not.have.class',
        'mat-mdc-select-disabled',
      );

      cy.getBySel(fields.fachrichtung).should('be.disabled');
      cy.getBySel(fields.gang).click();
      cy.get('mat-option').eq(1).click();
      cy.getBySel(fields.fachrichtung).should('not.be.disabled');
      cy.getBySel(fields.fachrichtung).type('fachrichtung1');
      cy.getBySel(fields.fachrichtung).should('have.value', 'fachrichtung1');

      cy.getBySel(fields.notFound).click();
      cy.getBySel(fields.fachrichtung).should('have.value', '');

      cy.getBySel(fields.fachrichtung).should('be.disabled');
      cy.getBySel(fields.alternativ.staette).type('staette1');
      cy.getBySel(fields.alternativ.gang).type('gang1');
      cy.getBySel(fields.fachrichtung).should('not.be.disabled');
    });
  });
});

function mountWithGesuch(): void {
  cy.mount(SharedFeatureGesuchFormEducationComponent, {
    imports: [
      TranslateTestingModule.withTranslations({}),
      NoopAnimationsModule,
    ],
    providers: [
      provideMockStore({
        initialState: {
          ausbildungsstaettes: {
            ausbildungsstaettes: <Ausbildungsstaette[]>[
              {
                nameDe: 'staette1',
                nameFr: 'staette1',
                id: '1',
                ausbildungsgaenge: [
                  {
                    ausbildungsort: 'BERN',
                    ausbildungsrichtung: 'FACHHOCHSCHULEN',
                    bezeichnungDe: 'gang1',
                    bezeichnungFr: 'gang1',
                    ausbildungsstaetteId: '1',
                    id: '1',
                  },
                ],
              },
            ],
          },
          gesuchs: {
            gesuchFormular: {},
          },
          language: { language: 'de' },
        },
      }),
      provideMaterialDefaultOptions(),
    ],
  });
}
