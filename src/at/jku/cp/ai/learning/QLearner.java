package at.jku.cp.ai.learning;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import at.jku.cp.ai.rau.IBoard;
import at.jku.cp.ai.rau.objects.Move;
import at.jku.cp.ai.search.datastructures.Pair;

public class QLearner
{
	public Map<Pair<IBoard, Move>, Double> qmatrix;
	@SuppressWarnings("unused")
	private int numEpisodes;
	@SuppressWarnings("unused")
	private Random random;
	@SuppressWarnings("unused")
	private double discountFactor;

	/**
	 * 
	 * @param random
	 *            the random number generator to be used
	 * @param numEpisodes
	 *            the number of episodes for learning the model
	 * @param discountFactor
	 *            this determines the importance of future rewards; in our test
	 *            cases this plays a very minor role.
	 */
	public QLearner(Random random, int numEpisodes, double discountFactor)
	{
		if (discountFactor < 0d)
		{
			throw new RuntimeException("discountFactor must be greater than 0.0!");
		}

		this.random = random;
		this.numEpisodes = numEpisodes;
		this.discountFactor = discountFactor;
		this.qmatrix = new HashMap<>();
	}

	/**
	 * This is the method that has to learn the qmatrix, which will be used later on!
	 * @param board
	 */
	public void learnQFunction(IBoard board)
	{
		// HINT: adapt algorithm for q-learning from the lecture slides
		// in the following way:
		//
		// for e in (0 .. number_of_episodes):
		//     while not (goal reached):
		//         ...

		IBoard curr, prev;
		for (int episode = 0; episode < numEpisodes; episode++)
		{
			curr = board.copy();

			while(curr.isRunning()) {
				Move next = getRandomMove(curr);
				prev = curr.copy();
				curr.executeMove(next);
				qmatrix.put(new Pair<>(prev, next), getReward(curr) + discountFactor * getMaxQValue(curr));
			}

		}
	}

	/**
	 * this method uses the (learned) qmatrix to determine best move, given the current
	 * situation!
	 * @param board
	 * @return
	 */
	public Move getMove(IBoard board)
	{
		List<Move> moves = board.getPossibleMoves();

		double bestScore = Double.NEGATIVE_INFINITY;
		Move bestMove = Move.STAY;

		for (Move move : moves)
		{
			double score = qmatrix.getOrDefault(new Pair<>(board, move), 0d);
			if (score > bestScore)
			{
				bestScore = score;
				bestMove = move;
			}
		}

		return bestMove;
	}

	private Move getRandomMove(IBoard board)
	{
		List<Move> moves = board.getPossibleMoves();

		return moves.get(random.nextInt(moves.size()));
	}
	
	public double getReward(IBoard board)
	{
		if (!board.isRunning() && board.getEndCondition().getWinner() == 0)
		{
			return 100d;
		}

		return 0d;
	}

	public double getMaxQValue(IBoard board)
	{
		if (board.isRunning())
		{
			List<Move> moves = board.getPossibleMoves();

			double maxQ = Double.NEGATIVE_INFINITY;
			for (Move move : moves)
			{
				maxQ = Math.max(maxQ, qmatrix.getOrDefault(new Pair<>(board, move), 0d));
			}

			return maxQ;
		}

		return getReward(board);
	}
}