package ch.dvbern.stip.api.util;

import java.util.Arrays;
import java.util.List;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.tuple.Pair;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@UtilityClass
public class RequiredDocsUtil {
    public void requiresOneAndType(final Pair<String, List<DokumentTyp>> requiredDocs, final DokumentTyp dokTyp) {
        requiresOneAndType(requiredDocs.getRight(), dokTyp);
    }

    public void requiresOneAndType(final List<DokumentTyp> requiredDocs, final DokumentTyp dokTyp) {
        assertCount(requiredDocs, 1);
        assertType(requiredDocs, dokTyp);
    }

    public void requiresOneOfManyAndType(
        final List<Pair<String, List<DokumentTyp>>> requiredList,
        final DokumentTyp dokTyp
    ) {
        assertThat(requiredList.size(), is(1));
        requiresOneAndType(requiredList.get(0), dokTyp);
    }

    public void requiresFirstCountAndTypes(
        final int count,
        final List<Pair<String, List<DokumentTyp>>> requiredList,
        final DokumentTyp... dokTypes
    ) {
        assertThat(requiredList.size(), is(1));
        final var flatTypes = requiredList.stream().flatMap(x -> x.getRight().stream()).toList();
        assertThat(flatTypes.size(), is(count));
        assertTypes(flatTypes, dokTypes);
    }

    public void assertCount(final Pair<String, List<DokumentTyp>> requiredDocs, final int count) {
        assertCount(requiredDocs.getRight(), count);
    }

    public void assertCount(final List<DokumentTyp> requiredDocs, final int count) {
        assertThat(requiredDocs.toString(), requiredDocs.size(), is(count));
    }

    public void assertType(final Pair<String, List<DokumentTyp>> requiredDocs, final DokumentTyp dokTyp) {
        assertType(requiredDocs.getRight(), dokTyp);
    }

    public void assertTypes(final Pair<String, List<DokumentTyp>> requiredDocs, final DokumentTyp... dokTyp) {
        assertTypes(requiredDocs.getRight(), dokTyp);
    }

    public void assertTypes(final List<DokumentTyp> requiredDocs, final DokumentTyp... dokTyp) {
        assertThat(requiredDocs.containsAll(Arrays.stream(dokTyp).toList()), is(true));
    }

    public void assertType(final List<DokumentTyp> requiredDocs, final DokumentTyp dokTyp) {
        assertThat(requiredDocs.toString(), requiredDocs.contains(dokTyp), is(true));
    }
}
