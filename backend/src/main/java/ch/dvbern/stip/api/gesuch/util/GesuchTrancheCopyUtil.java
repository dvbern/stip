package ch.dvbern.stip.api.gesuch.util;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.adresse.util.AdresseCopyUtil;
import ch.dvbern.stip.api.ausbildung.util.AusbildungCopyUtil;
import ch.dvbern.stip.api.auszahlung.util.AuszahlungCopyUtil;
import ch.dvbern.stip.api.common.exception.AppErrorException;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.einnahmen_kosten.util.EinnahmenKostenCopyUtil;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.eltern.util.ElternCopyUtil;
import ch.dvbern.stip.api.familiensituation.util.FamiliensituationCopyUtil;
import ch.dvbern.stip.api.geschwister.util.GeschwisterCopyUtil;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.kind.util.KindCopyUtil;
import ch.dvbern.stip.api.lebenslauf.util.LebenslaufItemCopyUtil;
import ch.dvbern.stip.api.partner.util.PartnerCopyUtil;
import ch.dvbern.stip.api.personinausbildung.util.PersonInAusbildungCopyUtil;
import ch.dvbern.stip.api.steuerdaten.util.SteuerdatenCopyUtil;
import ch.dvbern.stip.generated.dto.AenderungsantragCreateDto;
import lombok.experimental.UtilityClass;

@UtilityClass
// TODO KSTIP-1236: Once proper test data generation is in place, test copying
public class GesuchTrancheCopyUtil {
    public GesuchTranche createAenderungstranche(
        final GesuchTranche gesuchTranche,
        final AenderungsantragCreateDto createDto
    ) {
        final var gesuch = gesuchTranche.getGesuch();
        final var newTranche = new GesuchTranche();
        newTranche.setGesuch(gesuch);

        newTranche.setGueltigkeit(clampStartStop(gesuch.getGesuchsperiode(), createDto));
        newTranche.setComment(createDto.getComment());
        newTranche.setGesuchFormular(copy(gesuchTranche.getGesuchFormular()));
        return newTranche;
    }

    DateRange clampStartStop(final Gesuchsperiode gesuchsperiode, final AenderungsantragCreateDto createDto) {
        final var gesuchsperiodeStart = gesuchsperiode.getGesuchsperiodeStart();
        final var gesuchsperiodeStopp = gesuchsperiode.getGesuchsperiodeStopp();
        final var startDate = DateUtil.clamp(
            createDto.getStart(),
            gesuchsperiodeStart,
            gesuchsperiodeStopp
        );
        final var endDate = DateUtil.clamp(
            createDto.getEnd() != null ? createDto.getEnd() : gesuchsperiodeStopp,
            gesuchsperiodeStart,
            gesuchsperiodeStopp
        );

        if (startDate.isAfter(endDate)) {
            throw new AppErrorException("Start date for new GesuchTranche must be after end date");
        }

        return new DateRange(startDate, endDate);
    }

    GesuchFormular copy(final GesuchFormular other) {
        final var copy = new GesuchFormular();

        // PiA und PiA Adresse
        copy.setPersonInAusbildung(PersonInAusbildungCopyUtil.createCopyIgnoreReferences(other.getPersonInAusbildung()));
        final var piaAdresseCopy = AdresseCopyUtil.createCopy(other.getPersonInAusbildung().getAdresse());
        copy.getPersonInAusbildung().setAdresse(piaAdresseCopy);

        // Ausbildung
        copy.setAusbildung(AusbildungCopyUtil.createCopyIncludingStammdatenReferences(other.getAusbildung()));

        // Familiensituation
        copy.setFamiliensituation(FamiliensituationCopyUtil.createCopy(other.getFamiliensituation()));

        // Partner und Partner Adresse
        copy.setPartner(PartnerCopyUtil.createCopyIgnoreReferences(other.getPartner()));
        if (copy.getPartner() != null) {
            copy.getPartner().setAdresse(AdresseCopyUtil.createCopy(other.getPartner().getAdresse()));
        }

        // Eltern
        copy.setElterns(ElternCopyUtil.createCopyOfSetWithoutReferences(other.getElterns()));
        Adresse mutterAdresseCopy = null;
        Adresse vaterAdresseCopy = null;
        for (final var eltern : copy.getElterns()) {
            final var adresseCopy = AdresseCopyUtil.createCopy(eltern.getAdresse());
            if (eltern.getElternTyp() == ElternTyp.MUTTER) {
                mutterAdresseCopy = adresseCopy;
            } else if (eltern.getElternTyp() == ElternTyp.VATER) {
                vaterAdresseCopy = adresseCopy;
            }

            eltern.setAdresse(adresseCopy);
        }

        // Auszahlung
        copy.setAuszahlung(AuszahlungCopyUtil.createCopyIgnoreReferences(other.getAuszahlung()));
        final var auszahlungAdresseCopy = switch (copy.getAuszahlung().getKontoinhaber()) {
            case GESUCHSTELLER -> piaAdresseCopy;
            case MUTTER -> mutterAdresseCopy;
            case VATER -> vaterAdresseCopy;
            default -> AdresseCopyUtil.createCopy(other.getAuszahlung().getAdresse());
        };
        copy.getAuszahlung().setAdresse(auszahlungAdresseCopy);

        // Einnahmen Kosten
        copy.setEinnahmenKosten(EinnahmenKostenCopyUtil.createCopy(other.getEinnahmenKosten()));

        // Lebenslauf
        copy.setLebenslaufItems(LebenslaufItemCopyUtil.createCopyOfSet(other.getLebenslaufItems()));

        // Geschwister
        copy.setGeschwisters(GeschwisterCopyUtil.createCopyOfSet(other.getGeschwisters()));

        // Kinds
        copy.setKinds(KindCopyUtil.createCopySet(other.getKinds()));

        // Steuerdaten
        copy.setSteuerdaten(SteuerdatenCopyUtil.createCopySet(other.getSteuerdaten()));

        return copy;
    }
}