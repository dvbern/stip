import { Route } from '@angular/router';

import {
  AUSBILDUNG,
  AUSZAHLUNG,
  DARLEHEN,
  DOKUMENTE,
  EINNAHMEN_KOSTEN,
  ELTERN,
  ELTERN_STEUERDATEN_STEPS,
  ELTERN_STEUERERKLAERUNG_STEPS,
  FAMILIENSITUATION,
  GESCHWISTER,
  KINDER,
  LEBENSLAUF,
  PARTNER,
  PERSON,
  TRANCHE,
} from '@dv/shared/model/gesuch-form';

export const sachbearbeitungAppFeatureGesuchFormRoutes: Route[] = [
  {
    path: TRANCHE.route,
    resolve: {
      step: () => TRANCHE,
    },
    title: TRANCHE.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/gesuch-form-tranche').then(
        (m) => m.sharedFeatureGesuchFormTrancheRoutes,
      ),
  },
  {
    path: KINDER.route,
    resolve: {
      step: () => KINDER,
    },
    title: KINDER.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/gesuch-form-kinder').then(
        (m) => m.gesuchAppFeatureGesuchFormKinderRoutes,
      ),
  },
  {
    path: LEBENSLAUF.route,
    resolve: {
      step: () => LEBENSLAUF,
    },
    title: LEBENSLAUF.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/gesuch-form-lebenslauf').then(
        (m) => m.gesuchAppFeatureGesuchFormLebenslaufRoutes,
      ),
  },
  {
    path: GESCHWISTER.route,
    resolve: {
      step: () => GESCHWISTER,
    },
    title: GESCHWISTER.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/gesuch-form-geschwister').then(
        (m) => m.gesuchAppFeatureGesuchFormGeschwisterRoutes,
      ),
  },
  {
    path: AUSZAHLUNG.route,
    resolve: {
      step: () => AUSZAHLUNG,
    },
    title: AUSZAHLUNG.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/gesuch-form-auszahlungen').then(
        (m) => m.gesuchAppFeatureGesuchFormAuszahlungenRoutes,
      ),
  },
  {
    path: FAMILIENSITUATION.route,
    resolve: {
      step: () => FAMILIENSITUATION,
    },
    title: FAMILIENSITUATION.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/gesuch-form-familiensituation').then(
        (m) => m.gesuchAppFeatureGesuchFormFamiliensituationRoutes,
      ),
  },
  {
    path: PARTNER.route,
    resolve: {
      step: () => PARTNER,
    },
    title: PARTNER.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/gesuch-form-partner').then(
        (m) => m.gesuchAppFeatureGesuchFormPartnerRoutes,
      ),
  },
  {
    path: ELTERN.route,
    resolve: {
      step: () => ELTERN,
    },
    title: ELTERN.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/gesuch-form-eltern').then(
        (m) => m.gesuchAppFeatureGesuchFormElternRoutes,
      ),
  },
  ...Object.values(ELTERN_STEUERERKLAERUNG_STEPS).map((step) => ({
    path: step.route,
    resolve: {
      step: () => step,
    },
    title: step.translationKey,
    loadChildren: () =>
      // TODO: @spse I wasn't able to look at the eslint error here yet and also not the following one
      import('@dv/shared/feature/gesuch-form-eltern-steuererklaerung').then(
        (m) => m.sharedFeatureGesuchFormElternSteuererklaerungRoutes,
      ),
  })),
  ...Object.values(ELTERN_STEUERDATEN_STEPS).map((step) => ({
    path: step.route,
    resolve: {
      step: () => step,
    },
    title: step.translationKey,
    loadChildren: () =>
      import(
        '@dv/sachbearbeitung-app/feature/gesuch-form-eltern-steuerdaten'
      ).then((m) => m.sachbearbeitungAppFeatureGesuchFormSteuerdatenRoutes),
  })),
  {
    path: PERSON.route,
    resolve: {
      step: () => PERSON,
    },
    title: PERSON.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/gesuch-form-person').then(
        (m) => m.gesuchAppFeatureGesuchFormPersonRoutes,
      ),
  },
  {
    path: AUSBILDUNG.route,
    resolve: {
      step: () => AUSBILDUNG,
    },
    title: AUSBILDUNG.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/ausbildung').then(
        (m) => m.sharedFeatureAusbildungRoutes,
      ),
  },
  {
    path: EINNAHMEN_KOSTEN.route,
    resolve: {
      step: () => EINNAHMEN_KOSTEN,
    },
    title: EINNAHMEN_KOSTEN.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/gesuch-form-einnahmenkosten').then(
        (m) => m.gesuchAppFeatureGesuchFormEinnahmenkostenRoutes,
      ),
  },
  {
    path: DARLEHEN.route,
    resolve: {
      step: () => DARLEHEN,
    },
    title: DARLEHEN.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/darlehen').then(
        (m) => m.sharedFeatureDarlehenRoutes,
      ),
  },
  {
    path: DOKUMENTE.route,
    resolve: {
      step: () => DOKUMENTE,
    },
    title: DOKUMENTE.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/gesuch-dokumente').then(
        (m) => m.sharedFeatureGesuchDokumenteRoutes,
      ),
  },
  {
    path: ':id',
    redirectTo: TRANCHE.route + '/:id',
  },
];
