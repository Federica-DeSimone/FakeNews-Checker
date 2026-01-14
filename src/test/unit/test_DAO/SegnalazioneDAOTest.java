package unit.test_DAO;

import static org.junit.jupiter.api.Assertions.*;
import it.unisa.storage.*;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SegnalazioneDAOTest {

    private SegnalazioneDAO segnalazioneDAO;
    private UtenteDao utenteDao;
    private GestoreDAO gestoreDAO;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        DatabaseSetupForTest.getConnection();
    }

    @BeforeEach
    void setUp() {
        segnalazioneDAO = new SegnalazioneDAO();
        utenteDao = new UtenteDao();
        gestoreDAO = new GestoreDAO();

        try (Connection conn = DatabaseSetupForTest.getConnection()) {
            conn.createStatement().execute("DELETE FROM segnalazioni;");
            conn.createStatement().execute("DELETE FROM notizie;");
            conn.createStatement().execute("DELETE FROM gestori;");
            conn.createStatement().execute("DELETE FROM utenti;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testInserisciSegnalazione() throws SQLException {
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setTitolo("Fake News Sospetta");
        segnalazione.setDescrizione("Questa notizia non è vera");
        segnalazione.setUrl("http://fake.news/articolo");
        segnalazione.setAutore("Segnalatore Anonimo");
        segnalazione.setImmaginePath("fake.png");

        boolean result = segnalazioneDAO.inserisciSegnalazione(segnalazione);

        assertTrue(result, "Inserimento segnalazione fallito");
        assertNotNull(segnalazione.getId(), "ID segnalazione non assegnato");

        System.out.println("Segnalazione inserita: Titolo=" + segnalazione.getTitolo());
    }

    @Test
    void testInserisciSegnalazioneConUtente() throws SQLException {
        Utente utente = new Utente();
        utente.setNome("Giuseppe");
        utente.setCognome("Rossi");
        utente.setEmail("giuseppe.rossi@example.com");
        utente.setTelefono("1111111111");
        utente.setPasswordHash("hashedPass");
        utenteDao.registraUtente(utente, "hashedPass");

        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setTitolo("Segnalazione con Utente");
        segnalazione.setDescrizione("Segnalata da un utente registrato");
        segnalazione.setUrl("http://example.com/fake");
        segnalazione.setAutore("Giuseppe Rossi");
        segnalazione.setImmaginePath("user_report.png");

        boolean result = segnalazioneDAO.inserisciSegnalazione(segnalazione, utente.getId());

        assertTrue(result, "Inserimento fallito");
        assertNotNull(segnalazione.getId());

        System.out.println("Segnalazione con utente inserita: Titolo=" + segnalazione.getTitolo());
    }

    @Test
    void testGetSegnalazioniInVerifica() throws SQLException {
        Segnalazione s1 = new Segnalazione();
        s1.setTitolo("Segnalazione 1");
        s1.setDescrizione("Desc 1");
        s1.setUrl("http://url1.com");
        s1.setAutore("Autore 1");
        s1.setImmaginePath("img1.png");
        segnalazioneDAO.inserisciSegnalazione(s1);

        Segnalazione s2 = new Segnalazione();
        s2.setTitolo("Segnalazione 2");
        s2.setDescrizione("Desc 2");
        s2.setUrl("http://url2.com");
        s2.setAutore("Autore 2");
        s2.setImmaginePath("img2.png");
        segnalazioneDAO.inserisciSegnalazione(s2);

        List<Segnalazione> segnalazioni = segnalazioneDAO.getSegnalazioniInVerifica();

        assertNotNull(segnalazioni);
        assertTrue(segnalazioni.size() >= 2, "Dovrebbe avere almeno 2 segnalazioni");

        System.out.println("Segnalazioni in verifica: " + segnalazioni.size());
    }

    @Test
    void testGetSegnalazioneById() throws SQLException {
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setTitolo("Test Get By ID");
        segnalazione.setDescrizione("Descrizione test");
        segnalazione.setUrl("http://test.com");
        segnalazione.setAutore("Tester");
        segnalazione.setImmaginePath("test.png");
        segnalazioneDAO.inserisciSegnalazione(segnalazione);

        Segnalazione retrieved = segnalazioneDAO.getSegnalazioneById(segnalazione.getId());

        assertNotNull(retrieved, "Segnalazione non trovata");
        assertEquals("Test Get By ID", retrieved.getTitolo());

        System.out.println("Segnalazione recuperata: Titolo=" + retrieved.getTitolo());
    }

    @Test
    void testAggiornaStatoSegnalazione() throws SQLException {
        Gestore gestore = new Gestore();
        gestore.setNome("Verificatore");
        gestore.setCognome("Test");
        gestore.setEmail("verificatore@example.com");
        gestore.setRuolo(Gestore.TipoGestore.GESTORE_VERIFICHE);
        gestoreDAO.registraGestore(gestore, "pass", null);

        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setTitolo("Da Verificare");
        segnalazione.setDescrizione("Desc");
        segnalazione.setUrl("http://url.com");
        segnalazione.setAutore("Autore");
        segnalazione.setImmaginePath("img.png");
        segnalazioneDAO.inserisciSegnalazione(segnalazione);

        boolean result = segnalazioneDAO.aggiornaStatoSegnalazione(
                segnalazione.getId(), "verificata", gestore.getId(), null
        );

        assertTrue(result, "Aggiornamento stato fallito");

        Segnalazione retrieved = segnalazioneDAO.getSegnalazioneById(segnalazione.getId());
        assertEquals("verificata", retrieved.getStato());

        System.out.println("Stato segnalazione aggiornato: " + retrieved.getStato());
    }

    @Test
    void testCountSegnalazioniByStato() throws SQLException {
        Segnalazione s = new Segnalazione();
        s.setTitolo("Test Count");
        s.setDescrizione("Desc");
        s.setUrl("http://url.com");
        s.setAutore("Autore");
        s.setImmaginePath("img.png");
        segnalazioneDAO.inserisciSegnalazione(s);

        int count = segnalazioneDAO.countSegnalazioniByStato("Verificata");

        assertTrue(count > 0, "Dovrebbe avere segnalazioni Verificata");

        System.out.println("Segnalazioni Verificate: " + count);
    }

    @Test
    void testGetSegnalazioniByUtente() throws SQLException {
        Utente utente = new Utente();
        utente.setNome("Marco");
        utente.setCognome("Bianchi");
        utente.setEmail("marco.bianchi@example.com");
        utente.setTelefono("2222222222");
        utente.setPasswordHash("hashedPass");
        utenteDao.registraUtente(utente, "hashedPass");

        Segnalazione s1 = new Segnalazione();
        s1.setTitolo("Segnalazione Utente 1");
        s1.setDescrizione("Desc 1");
        s1.setUrl("http://url1.com");
        s1.setAutore("Marco");
        s1.setImmaginePath("img1.png");
        segnalazioneDAO.inserisciSegnalazione(s1, utente.getId());

        Segnalazione s2 = new Segnalazione();
        s2.setTitolo("Segnalazione Utente 2");
        s2.setDescrizione("Desc 2");
        s2.setUrl("http://url2.com");
        s2.setAutore("Marco");
        s2.setImmaginePath("img2.png");
        segnalazioneDAO.inserisciSegnalazione(s2, utente.getId());

        List<Segnalazione> segnalazioni = segnalazioneDAO.getSegnalazioniByUtente(utente.getId());

        assertNotNull(segnalazioni);
        assertTrue(segnalazioni.size() >= 2);

        System.out.println("Segnalazioni dell'utente: " + segnalazioni.size());
    }

    @Test
    void testGetTutteSegnalazioni() throws SQLException {
        Segnalazione s1 = new Segnalazione();
        s1.setTitolo("Segnalazione A");
        s1.setDescrizione("Desc A");
        s1.setUrl("http://urlA.com");
        s1.setAutore("Autore A");
        s1.setImmaginePath("imgA.png");
        segnalazioneDAO.inserisciSegnalazione(s1);

        Segnalazione s2 = new Segnalazione();
        s2.setTitolo("Segnalazione B");
        s2.setDescrizione("Desc B");
        s2.setUrl("http://urlB.com");
        s2.setAutore("Autore B");
        s2.setImmaginePath("imgB.png");
        segnalazioneDAO.inserisciSegnalazione(s2);

        List<Segnalazione> segnalazioni = segnalazioneDAO.getTutteSegnalazioni();

        assertNotNull(segnalazioni);
        assertTrue(segnalazioni.size() >= 2);

        System.out.println("Segnalazioni totali: " + segnalazioni.size());
    }
}