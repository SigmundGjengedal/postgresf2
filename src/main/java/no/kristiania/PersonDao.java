package no.kristiania;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PersonDao {

    private final DataSource dataSource;
    private Person person;

    // konst
    public PersonDao(DataSource dataSource) {
        this.dataSource=dataSource;
    }

    // skal lagre en person, når den tar inn en person.
    public void save(Person person) throws SQLException {
        try (Connection connection = dataSource.getConnection() ) { // For å connecte til db trenger vi å getConnection. Try for å close
            // preperer statement og setter variabler på det:
            try (PreparedStatement statement = connection.prepareStatement("insert into person (first_name, last_name) values (?, ?)")) {
                statement.setString(1,person.getFirstName());
                statement.setString(2,person.getLastName());

                // utfører statement:
                statement.executeUpdate();
            }
        }
        this.person = person;
    }

    // gir oss tilbake en person, når vi tar inn en id. liten long fordi id kan ikke være null, kan ikke be om noen som ikke er der.
    public Person retrieve(Long id) {
        return person;
    }
}
