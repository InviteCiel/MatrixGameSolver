package org.invite_ciel.study.matrix_games.solver;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * ListUtilsTest
 *
 * @author Gleb Mayorov
 */
public class ListUtilsTest {

    @org.junit.Test
    public void intersect() throws Exception {
        List<Integer> list1 = new LinkedList<>(Arrays.asList(1, 2, 3));
        List<Integer> listA = new LinkedList<>(Arrays.asList(1, 2, 3, 5, 6, 7));
        List<Integer> listB= new LinkedList<>(Arrays.asList(1, 2, 3, 8, 9, 10));
        assertEquals(list1, ListUtils.intersect(listA, listB));
    }
}