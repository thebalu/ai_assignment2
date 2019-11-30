package at.jku.cp.ai.search.algorithms;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiPredicate;
import java.util.function.Function;

import at.jku.cp.ai.search.AdversarialSearch;
import at.jku.cp.ai.search.Node;
import at.jku.cp.ai.search.datastructures.Pair;

import static java.lang.Double.max;
import static java.lang.Double.min;

public class AlphaBetaSearch implements AdversarialSearch {
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
	public AlphaBetaSearch(BiPredicate<Integer, Node> searchLimitingPredicate)
	{
		this.searchLimitingPredicate = searchLimitingPredicate;
	}

	public Pair<Node, Double> search(Node start, Function<Node, Double> evalFunction) {
		// TODO: implement alpha-beta pruning here
		this.evalFunction = evalFunction;

		Node res = start.adjacent().stream()
				.max(Comparator.comparing(node -> minValue(1,node, -11111.1, 11111.1)))
				.orElseThrow(NoSuchElementException::new);
		return new Pair<> (res, minValue(1,res, -11111.1, 11111.1));

	}

	private double maxValue(int depth, Node state, double alpha, double beta) {
		if(!searchLimitingPredicate.test(depth, state) || state.isLeaf()) return evalFunction.apply(state);

		List<Node> adj = state.adjacent();
		double v = -11111.1;
		for(int i=0; i<adj.size(); i++) {
			Node curr = adj.get(i);
			v = max(v, minValue(depth+1, curr, alpha, beta));
			if(v>=beta) return v;
			alpha = max(alpha,v);
		}
		return v;
	}

	private double minValue(int depth, Node state, double alpha, double beta) {
		if(!searchLimitingPredicate.test(depth, state) || state.isLeaf()) return evalFunction.apply(state);

		List<Node> adj = state.adjacent();
		double v = 11111.1;
		for(int i=0; i<adj.size(); i++) {
			Node curr = adj.get(i);
			v = min(v, maxValue(depth+1, curr, alpha, beta));
			if(v<=alpha) return v;
			beta = min(beta,v);
		}
		return v;
	}
}
