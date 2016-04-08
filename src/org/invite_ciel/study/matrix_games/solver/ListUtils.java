package org.invite_ciel.study.matrix_games.solver;

import java.util.LinkedList;
import java.util.List;

/**
 * ListUtils
 *
 * @author Gleb Mayorov
 */
public class ListUtils {
    private ListUtils() {}

    public static <T> List<T> intersect(List<T> list1, List<T> list2) {
        List<T> list1WithoutList2 = new LinkedList<>(list1);
        list1WithoutList2.removeAll(list2);
        List<T> intersection = new LinkedList<>(list1);
        intersection.removeAll(list1WithoutList2);
        return intersection;
    }
}
