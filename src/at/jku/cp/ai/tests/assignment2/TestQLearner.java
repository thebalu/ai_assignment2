package at.jku.cp.ai.tests.assignment2;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

import at.jku.cp.ai.rau.Board;
import at.jku.cp.ai.rau.IBoard;
import at.jku.cp.ai.rau.endconditions.CloudEvaporator;
import at.jku.cp.ai.rau.objects.Move;
import at.jku.cp.ai.learning.QLearner;

public class TestQLearner
{
	private boolean verbose;
	
	public TestQLearner() {
		this.verbose = false;
	}
	
	@Test
	public void simplest()
	{	
		doLearningAndTestOnBoard("simplest.lvl");
	}

	@Test
	public void simple()
	{	
		doLearningAndTestOnBoard("simple.lvl");
	}
	
	@Test
	public void length()
	{	
		doLearningAndTestOnBoard("length.lvl");
	}
	
	@Test
	public void L0()
	{	
		doLearningAndTestOnBoard("L0.lvl");
	}
	
	@Test
	public void L1()
	{	
		doLearningAndTestOnBoard("L1.lvl");
	}
	
	@Test
	public void L2()
	{	
		doLearningAndTestOnBoard("L2.lvl");
	}
	
	private void doLearningAndTestOnBoard(String filename)
	{
		IBoard startBoard = Board.fromLevelFile("assets/assignment2/" + filename);
		// we restrict the number of seeds the unicorn can spawn, to make the
		// statespace considerably smaller!
		startBoard.getUnicorns().get(0).seeds = 1;
		
		// we need a new end-condition for the game as well
		startBoard.setEndCondition(new CloudEvaporator());

		if(verbose)
			System.out.println(startBoard);
		
		IBoard learnBoard = startBoard.copy();
		IBoard runBoard = startBoard.copy();

		/////////////////
		// 20.000 episodes is enough to learn a successful model
		QLearner learner = new QLearner(new Random(2468L), 20001, 0.9);
		
		// learning the qmatrix happens here
		learner.learnQFunction(learnBoard);
		
		// apply the learned model to the original board
		// if we go sailing, or need more than 100 ticks before
		// we reach the goal, this test will fail
		while (runBoard.isRunning() && runBoard.getTick() < 100)
		{
			// determine the next move based upon the current board
			// state and the learned model
			Move nextMove = learner.getMove(runBoard);
			runBoard.executeMove(nextMove);
			
			if(verbose)
				System.out.println(runBoard);
		}

		if(verbose)
			System.out.println(runBoard);

		// the learned model should have guided the unicorn to the cloud
		// and the cloud must have been evaporated as well
		assertEquals(0, runBoard.getEndCondition().getWinner());
	}

}
