import { SharedModelLebenslauf } from '@dv/shared/model/lebenslauf';
import { lowercased } from '@dv/shared/util-fn/string-helper';

// eslint-disable-next-line @typescript-eslint/no-namespace
export namespace LebenslaufPO {
  export const getFormLebenslaufLoading = () =>
    cy.getBySel('form-lebenslauf-loading');
  export const getFirstTimelineGapBlock = () =>
    cy.getBySel('timeline-gap-block').first();
  export const getAddButton = (type: SharedModelLebenslauf['type']) =>
    cy.getBySel(`lebenslauf-add-${lowercased(type)}`);

  export const getArtInput = (type: SharedModelLebenslauf['type']) =>
    cy.get(
      `[formcontrolname=${
        type === 'AUSBILDUNG' ? 'bildungs' : 'taetigskeits'
      }art]`
    );
}
