package no.kristiania;

public class PersonDaoTest {
    @Test
    void shouldRetrieveSavedPersonFromDatabase() {
        PersonDao dao = new PersonDao(createDatasource());

        Person person = examplePerson();
        doa.save(person);

        assertThat(dao.retrieve(person.getId()))
        // hva vi forventer
        ;
    }
}
