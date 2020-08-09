package project.bumar;

import org.junit.runner.RunWith;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(MockitoJUnitRunner.Silent.class)
//@SpringBootTest
//@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class BaseTest { }
