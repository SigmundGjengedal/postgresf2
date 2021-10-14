package no.kristiania;

import javax.sql.DataSource;
import java.sql.*;

public class PersonDao {

    private final DataSource dataSource;

    // konst
    public PersonDao(DataSource dataSource) {
        this.dataSource=dataSource;
    }

    // skal lagre en person, når den tar inn en person.
    public void save(Person person) throws SQLException {
        try (Connection connection = dataSource.getConnection() ) { // For å connecte til db trenger vi å getConnection. Try for å close
            // preperer statement og setter variabler på det:
            try (PreparedStatement statement = connection.prepareStatement("insert into person (first_name, last_name) values (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1,person.getFirstName());
                statement.setString(2,person.getLastName());
                // utfører statement:
                statement.executeUpdate();
                // må hente ut PK som ble autogenerert og også sette denne som en variabel i person objektet.
                try (ResultSet rsKeys = statement.getGeneratedKeys()) {
                    rsKeys.next(); // må lese første rad, starter utenfor av en retard grunn.
                    person.setId(rsKeys.getLong("id"));
                }
            }
        }
    }

    // gir oss tilbake en person, når vi tar inn en id. liten long fordi id kan ikke være null, kan ikke be om noen som ikke er der.
    public Person retrieve(Long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from person where id = ? ")) {
                statement.setLong(1,id);
                // henter og setter data fra et resultset fra query
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    Person coolPerson = new Person();
                    coolPerson.setId(resultSet.getLong("id")); // setter id og navn i objektet
                    coolPerson.setFirstName(resultSet.getString("first_name"));
                    coolPerson.setLastName(resultSet.getString("last_name"));
                    return coolPerson;

                }
            }
        }
    }
}
