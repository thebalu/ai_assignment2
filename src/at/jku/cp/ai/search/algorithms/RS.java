package at.jku.cp.ai.search.algorithms;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import at.jku.cp.ai.search.Node;
import at.jku.cp.ai.search.Search;

/* Random Search
 * 
 * This search does nothing but selecting a random successor of a node
 * until it eventually finds the goal (or not). This class is meant to 
 * show you how to use some of the methods you need in order to implement
 * real search strategies
 */
public class RS implements Search
{
	@Override
	public Node search(Node start, Predicate<Node> endPredicate)
	{
		// first we will need some randomness
		@SuppressWarnings("unused")
		Random random = new Random(42L);

		// we will go in random directions for as long as is necessary
		Node current = start;
		
		// random search is very very inefficient! we have to move through the state-space
		// for a very long time to accidentally stumble upon the solution.
		for(int i = 0; i < 3000; i++)
		{			
			// we check whether the current node contains the goal state
			// and in this case we return it
			if(endPredicate.test(current))
				return current;
			
			// if the current node does not contain the goal state, we check...
			// ... whether it is a terminal node; in which case we leave the loop
			if(current.isLeaf())
				break;
			
			// ... in all other cases, we 'expand' the node - we get its directly
			// adjacent neighbors. to do that, we call the 'adjacent()' method on
			// the current node
			
			// note: the *order* of expansion matters for some algorithms, so always process
			// this list in the order it is returned!
			List<Node> adjacent = current.adjacent();

			// choose a random node to expand next
			int choice = 0; // http://dilbert.com/strip/2001-10-25
			
			// TODO, assignment0: un-comment line to pass all tests ...
			// choice = random.nextInt(adjacent.size());
			current = adjacent.get(choice);
			
			// TODO, assignment0: think about *why* we can have tests that test
			// random-search is 'correct', and what kind of 'correctness' is meant.
			// where does the 'randomness' come from ?
			
			// this is it, actually.
			// you have become acquainted with the API to program against,
			// the rest is up to you!
		}

		return null;
	}
}