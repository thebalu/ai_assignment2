package at.jku.cp.ai.search.algorithms;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiPredicate;
import java.util.function.DoubleBinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

import at.jku.cp.ai.search.AdversarialSearch;
import at.jku.cp.ai.search.Node;
import at.jku.cp.ai.search.datastructures.Pair;

import static java.lang.Double.min;

public class MinMaxSearch implements AdversarialSearch {

	@SuppressWarnings("unused")
	private BiPredicate<Integer, Node> searchLimitingPredicate;
	private Function<Node, Double> evalFunction;
	/**
	 * To limit the extent of the search, this implementation should honor a
	 * limiting predicate. The predicate returns 'true' as long as we are below the limit,
	 * and 'false', if we exceed the limit.
	 * 
	 * @param searchLimitingPredicate
	 */
	public MinMaxSearch(BiPredicate<Integer, Node> slp)
	{
		this.searchLimitingPredicate = slp;
	}

	public Pair<Node, Double> search(Node start, Function<Node, Double> evalFunction) {
		this.evalFunction = evalFunction;

		Node res = start.adjacent().stream()
										.max(Comparator.comparing(node -> minValue(1,node)))
										.orElseThrow(NoSuchElementException::new);
		return new Pair<> (res, minValue(1,res));
	}

	private double maxValue(int depth, Node state) {
		if(!searchLimitingPredicate.test(depth, state) || state.isLeaf()) return evalFunction.apply(state);
		return state.adjacent().stream().map(node -> minValue(depth+1, node))
				.max(Double::compare).orElseThrow(NoSuchElementException::new);
	}

	private double minValue(int depth, Node state) {
		if(!searchLimitingPredicate.test(depth, state) || state.isLeaf()) return evalFunction.apply(state);

		return state.adjacent().stream().map(node -> maxValue(depth+1, node))
				.min(Double::compare).orElseThrow(NoSuchElementException::new);

	}
}
