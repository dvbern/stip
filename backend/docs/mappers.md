# Mappers

Our mappers are automatically generated by Mapstruct and are used to map between our DB entities and our DTOs. We also use our mappers to reset/ clear some fields if others have changed. For example, if the `PersonInAusbildung.getWohnsitz` of the `GesuchFormular` changes to anything other than `EIGENER_HAUSHALT` we need to set the field `EinnahmenKosten.getWohnkosten` to null. This is done in the method annotated with `@BeforeMapping`, in this method we use the `resetFieldIf` method of the `EntityUpdateMapper` in conjunction with the `*DiffUtil` of the entity we are mapping to see if the field has changed, and if so, to set the value on the `*UpdateDto` which then gets correctly mapped to the entity.


## Testing

Mappers that contain logic either in the `@BeforeMapping` or `@AfterMapping`, like all other code, should be tested. The general pattern that should be used for it are the 3 As (Arrange, Act, Assert).

1: Arrange data. Unless the target needs some specific info, then you should only need to setup the `UpdateDto` object and let the mapper create the initial target entity for you, something like

```java
final var updatePia = new PersonInAusbildungUpdateDto();
updatePia.setZivilstand(Zivilstand.LEDIG);

final var updateDto = new GesuchFormularUpdateDto();
updateDto.setPersonInAusbildung(updatePia);

final var target = new GesuchFormular();
mapper.partialUpdate(updateDto, target);

// 'target' should now have all the same values as 'updateDto', change the data and Act and Assert 
``` 

2: Act. Call the specific method on the mapper that resets the data. In most mappers, related behaviour is clumped together, for the preceding example you should call `mapper.resetPartner(update, target)`.

3: Assert. Assert that the correct fields have been reset/ now contain the correct data. 