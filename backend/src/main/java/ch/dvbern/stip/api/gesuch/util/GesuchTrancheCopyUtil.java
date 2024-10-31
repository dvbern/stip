package ch.dvbern.stip.api.gesuch.util;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.adresse.util.AdresseCopyUtil;
import ch.dvbern.stip.api.auszahlung.util.AuszahlungCopyUtil;
import ch.dvbern.stip.api.common.exception.AppErrorException;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.dokument.util.GesuchDokumentCopyUtil;
import ch.dvbern.stip.api.einnahmen_kosten.util.EinnahmenKostenCopyUtil;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.eltern.util.ElternCopyUtil;
import ch.dvbern.stip.api.familiensituation.util.FamiliensituationCopyUtil;
import ch.dvbern.stip.api.geschwister.util.GeschwisterCopyUtil;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.kind.util.KindCopyUtil;
import ch.dvbern.stip.api.lebenslauf.util.LebenslaufItemCopyUtil;
import ch.dvbern.stip.api.partner.util.PartnerCopyUtil;
import ch.dvbern.stip.api.personinausbildung.util.PersonInAusbildungCopyUtil;
import ch.dvbern.stip.api.steuerdaten.util.SteuerdatenCopyUtil;
import ch.dvbern.stip.generated.dto.CreateAenderungsantragRequestDto;
import lombok.experimental.UtilityClass;

@UtilityClass
// TODO KSTIP-1236: Once proper test data generation is in place, test copying
public class GesuchTrancheCopyUtil {
    /**
     * Copies an existing {@link GesuchTranche} and sets all values, so it's a complete Aenderungstranche
     */
    public GesuchTranche createAenderungstranche(
        final GesuchTranche original,
        final CreateAenderungsantragRequestDto createDto
    ) {
        var endDate = createDto.getEnd();
        if (endDate == null) {
            endDate = original.getGesuch().getGesuchsperiode().getGesuchsperiodeStopp();
        }

        final var copy = copyTranche(
            original,
            new DateRange(createDto.getStart(), endDate),
            createDto.getComment()
        );

        copy.setStatus(GesuchTrancheStatus.IN_BEARBEITUNG_GS);
        copy.setTyp(GesuchTrancheTyp.AENDERUNG);

        return copy;
    }

    public GesuchTranche createNewTranche(final GesuchTranche gesuchTranche) {
        return createNewTranche(gesuchTranche, gesuchTranche.getGueltigkeit(), gesuchTranche.getComment());
    }

    /**
     * Copies an existing {@link GesuchTranche} and sets the new {@link GesuchTranche#status}
     * to {@link GesuchTrancheStatus#UEBERPRUEFEN}
     */
    public GesuchTranche createNewTranche(
        final GesuchTranche gesuchTranche,
        final DateRange gueltigkeit,
        final String comment
    ) {
        final var clamped = clampStartStop(
            gesuchTranche.getGesuch().getGesuchsperiode(),
            gueltigkeit
        );

        final var newTranche = copyTranche(
            gesuchTranche,
            clamped,
            comment
        );

        newTranche.setStatus(GesuchTrancheStatus.UEBERPRUEFEN);
        newTranche.setTyp(GesuchTrancheTyp.TRANCHE);

        return newTranche;
    }

    /**
     * Copies a tranche
     */
    public GesuchTranche copyTranche(
        final GesuchTranche original,
        final DateRange createDateRange,
        final String comment
    ) {
        final var newTranche = new GesuchTranche();
        newTranche.setGueltigkeit(createDateRange);
        newTranche.setComment(comment);
        newTranche.setGesuchFormular(copy(original.getGesuchFormular()));
        newTranche.setGesuchDokuments(GesuchDokumentCopyUtil.copyGesuchDokumenteWithDokumentReferences(
                newTranche,
                original.getGesuchDokuments()
            )
        );
        newTranche.getGesuchFormular().setTranche(newTranche);
        newTranche.setGesuch(original.getGesuch());
        return newTranche;
    }

    DateRange clampStartStop(final Gesuchsperiode gesuchsperiode, final DateRange createDateRange) {
        final var gesuchsperiodeStart = gesuchsperiode.getGesuchsperiodeStart();
        final var gesuchsperiodeStopp = gesuchsperiode.getGesuchsperiodeStopp();
        final var startDate = DateUtil.roundToStartOrEnd(
            DateUtil.clamp(
                createDateRange.getGueltigAb(),
                gesuchsperiodeStart,
                gesuchsperiodeStopp
            ),
            15,
            false,
            true
        );

        var endDate = createDateRange.getGueltigBis();
        if (endDate == null) {
            endDate = gesuchsperiodeStopp;
        }

        final var roundedEndDate = DateUtil.roundToStartOrEnd(
            DateUtil.clamp(
                endDate,
                gesuchsperiodeStart,
                gesuchsperiodeStopp
            ),
            14,
            true,
            false
        );

        if (startDate.isAfter(roundedEndDate)) {
            throw new AppErrorException("Start date for new GesuchTranche must be after end date");
        }

        return new DateRange(startDate, roundedEndDate);
    }

    GesuchFormular copy(final GesuchFormular other) {
        final var copy = new GesuchFormular();

        // PiA und PiA Adresse
        copy.setPersonInAusbildung(PersonInAusbildungCopyUtil.createCopyIgnoreReferences(other.getPersonInAusbildung()));
        final var piaAdresseCopy = AdresseCopyUtil.createCopy(other.getPersonInAusbildung().getAdresse());
        copy.getPersonInAusbildung().setAdresse(piaAdresseCopy);

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
