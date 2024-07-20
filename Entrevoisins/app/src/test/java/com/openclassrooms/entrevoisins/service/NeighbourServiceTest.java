package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static com.openclassrooms.entrevoisins.service.DummyNeighbourGenerator.urlFromSeed;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Unit test on Neighbour service
 */
@RunWith(JUnit4.class)
public class NeighbourServiceTest {

    private NeighbourApiService service;

    @Before
    public void setup() {
        service = DI.getNewInstanceApiService();
    }

    @Test
    public void getNeighboursWithSuccess() {
        List<Neighbour> neighbours = service.getNeighbours();
        List<Neighbour> expectedNeighbours = DummyNeighbourGenerator.DUMMY_NEIGHBOURS;
        assertThat(neighbours, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedNeighbours.toArray()));
    }

    @Test
    public void deleteNeighbourWithSuccess() {
        Neighbour neighbourToDelete = service.getNeighbours().get(0);
        service.deleteNeighbour(neighbourToDelete);
        assertFalse(service.getNeighbours().contains(neighbourToDelete));
    }

    @Test
    public void createNeighbourWithSuccess() {
        Neighbour neighbourToAdd = new Neighbour(13, "Ludovic", urlFromSeed("042581f3e39026702d"), "Saint-Pierre-du-Mont ; 5km",
                "+33 6 86 57 90 14",  "Bonjour !Je souhaiterais faire de la marche nordique. Pas initiée, je recherche une ou plusieurs personnes susceptibles de m'accompagner !J'aime les jeux de cartes tels la belote et le tarot..");
        service.createNeighbour(neighbourToAdd);
        assertTrue(service.getNeighbours().contains(neighbourToAdd));
    }

    @Test
    public void urlFromSeedWithSuccess() {
        String actual = urlFromSeed("042581f3e39026702d");
        String expected = "https://robohash.org/042581f3e39026702d?bgset=bg1";
        assertEquals(expected, actual);
    }

    @Test
    public void getFavoritesWithSuccess() {
        List<Neighbour> neighbour = service.getNeighbours();
        service.invertFavoriteState(neighbour.get(0));
        service.invertFavoriteState(neighbour.get(4));
        service.invertFavoriteState(neighbour.get(4));
        service.invertFavoriteState(neighbour.get(6));
        List<Neighbour> favoriteNeighbours = service.getFavorites();

        for (Neighbour n:favoriteNeighbours) {
            assertTrue(n.getFavorite());
        }

        assertEquals(2, favoriteNeighbours.size());
    }

    @Test
    public void invertFavoriteWithSuccess() {
        Neighbour nOld = service.getNeighbours().get(0);
        boolean oldNeighbourFavorite = nOld.getFavorite();

        service.invertFavoriteState(nOld);
        Neighbour nNew = service.getNeighbours().get(0);

        boolean newNeighbourFavorite = nNew.getFavorite();

        assertFalse(oldNeighbourFavorite == newNeighbourFavorite);
    }
}
