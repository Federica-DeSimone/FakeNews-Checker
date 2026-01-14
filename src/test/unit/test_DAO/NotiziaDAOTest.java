package unit.test_DAO;

import static org.junit.jupiter.api.Assertions.*;
import it.unisa.storage.NotiziaDAO;
import it.unisa.storage.Notizia;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class NotiziaDAOTest {
    private NotiziaDAO notiziaDAO;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        DatabaseSetupForTest.getConnection();
    }

    @BeforeEach
    void setUp() {
        notiziaDAO = new NotiziaDAO();
        try (Connection conn = DatabaseSetupForTest.getConnection()) {
            conn.createStatement().execute("SET FOREIGN_KEY_CHECKS=0;");
            conn.createStatement().execute("DELETE FROM segnalazioni WHERE id_notizia > 9;");
            conn.createStatement().execute("DELETE FROM notizie WHERE id > 9;");
            conn.createStatement().execute("SET FOREIGN_KEY_CHECKS=1;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testInserisciNotizia() throws SQLException {
        Notizia notizia = new Notizia();
        notizia.setTitolo("Notizia Tecnologia");
        notizia.setDescrizione("Descrizione tech");
        notizia.setImmagine("tech.png");
        notizia.setAutore("Marco Rossi");
        notizia.setStato("verificata");

        boolean result = notiziaDAO.inserisciNotizia(notizia);
        assertTrue(result, "Inserimento notizia fallito");
        assertTrue(notizia.getId() > 0, "ID non assegnato");

        System.out.println("Notizia inserita: Titolo=" + notizia.getTitolo());
    }

    @Test
    void testGetNotiziaById() throws SQLException {
        Notizia notizia = new Notizia();
        notizia.setTitolo("Notizia Test");
        notizia.setDescrizione("Descrizione test");
        notizia.setImmagine("image.png");
        notizia.setAutore("Luigi Verdi");
        notizia.setStato("verificata");

        notiziaDAO.inserisciNotizia(notizia);
        int id = notizia.getId();

        Notizia retrieved = notiziaDAO.getNotiziaById(id);
        assertNotNull(retrieved, "Notizia non trovata");
        assertEquals("Notizia Test", retrieved.getTitolo(), "Titolo non corrispondente");
        assertEquals(id, retrieved.getId(), "ID non corrispondente");

        System.out.println("Notizia recuperata: Titolo=" + retrieved.getTitolo());
    }

    @Test
    void testAggiornaNotizia() throws SQLException {
        Notizia notizia = new Notizia();
        notizia.setTitolo("Titolo Originale");
        notizia.setDescrizione("Descrizione originale");
        notizia.setImmagine("original.png");
        notizia.setAutore("Andrea Bianchi");
        notizia.setStato("verificata");

        notiziaDAO.inserisciNotizia(notizia);
        int id = notizia.getId();

        notizia.setTitolo("Titolo Modificato");
        notizia.setDescrizione("Descrizione modificata");

        boolean updated = notiziaDAO.aggiornaNotizia(notizia);
        assertTrue(updated, "Aggiornamento fallito");

        Notizia retrieved = notiziaDAO.getNotiziaById(id);
        assertEquals("Titolo Modificato", retrieved.getTitolo(), "Titolo non aggiornato");

        System.out.println("Notizia aggiornata: Titolo=" + retrieved.getTitolo());
    }

    @Test
    void testCercaNotizie() throws SQLException {
        Notizia notizia1 = new Notizia();
        notizia1.setTitolo("Notizia Tecnologia");
        notizia1.setDescrizione("Descrizione tecnologia");
        notizia1.setImmagine("tech.png");
        notizia1.setAutore("Tech Author");
        notizia1.setStato("verificata");
        notiziaDAO.inserisciNotizia(notizia1);

        Notizia notizia2 = new Notizia();
        notizia2.setTitolo("Notizia Sport");
        notizia2.setDescrizione("Descrizione sport");
        notizia2.setImmagine("sport.png");
        notizia2.setAutore("Sport Author");
        notizia2.setStato("verificata");
        notiziaDAO.inserisciNotizia(notizia2);

        List<Notizia> risultati = notiziaDAO.cercaNotizie("Tecnologia");
        assertFalse(risultati.isEmpty(), "Dovrebbero esserci risultati");
        assertEquals("Notizia Tecnologia", risultati.get(0).getTitolo());

        System.out.println("Notizie trovate per keyword 'Tecnologia': " + risultati.size());
    }

    @Test
    void testGetTutteNotizie() throws SQLException {
        // Notizie base nel DB: 7
        List<Notizia> notizie_prima = notiziaDAO.getTutteNotizie();
        int count_prima = notizie_prima.size();

        Notizia notizia1 = new Notizia();
        notizia1.setTitolo("Prima Notizia");
        notizia1.setDescrizione("Desc 1");
        notizia1.setImmagine("img1.png");
        notizia1.setAutore("Autore 1");
        notizia1.setStato("verificata");
        notiziaDAO.inserisciNotizia(notizia1);

        Notizia notizia2 = new Notizia();
        notizia2.setTitolo("Seconda Notizia");
        notizia2.setDescrizione("Desc 2");
        notizia2.setImmagine("img2.png");
        notizia2.setAutore("Autore 2");
        notizia2.setStato("verificata");
        notiziaDAO.inserisciNotizia(notizia2);

        List<Notizia> notizie_dopo = notiziaDAO.getTutteNotizie();
        assertNotNull(notizie_dopo);
        assertTrue(notizie_dopo.size() == count_prima + 2, "Dovrebbe avere 2 notizie in più");

        System.out.println("Notizie totali: " + notizie_dopo.size());
    }

    @Test
    void testAggiornaStatoNotizia() throws SQLException {
        Notizia notizia = new Notizia();
        notizia.setTitolo("Notizia da Verificare");
        notizia.setDescrizione("Descrizione");
        notizia.setImmagine("news.png");
        notizia.setAutore("Autore News");
        notizia.setStato("verificata");
        notiziaDAO.inserisciNotizia(notizia);

        boolean result = notiziaDAO.aggiornaStatoNotizia(notizia.getId(), "verificata");
        assertTrue(result, "Aggiornamento stato fallito");

        Notizia retrieved = notiziaDAO.getNotiziaById(notizia.getId());
        assertEquals("verificata", retrieved.getStato());

        System.out.println("Stato notizia aggiornato: " + retrieved.getStato());
    }

    @Test
    void testEliminaNotizia() throws SQLException {
        List<Notizia> notizie = notiziaDAO.getTutteNotizie();
        assertNotNull(notizie);
        assertTrue(notizie.size() > 0, "Dovrebbero esserci notizie");

        int idDaEliminare = notizie.get(0).getId();

        boolean result = notiziaDAO.eliminaNotizia(idDaEliminare);
        assertTrue(result, "Eliminazione fallita");

        Notizia retrieved = notiziaDAO.getNotiziaById(idDaEliminare);
        assertNull(retrieved, "Notizia dovrebbe essere eliminata");

        System.out.println("Notizia eliminata: ID=" + idDaEliminare);
    }
}