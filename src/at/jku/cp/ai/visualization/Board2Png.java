package at.jku.cp.ai.visualization;

import at.jku.cp.ai.rau.Board;
import at.jku.cp.ai.utils.RenderUtils;

public class Board2Png
{
	public static void main(String[] args)
	{
		for (int i = 0; i < args.length; i++)
			System.out.print(i + ":" + args[i] + " ");

		if (args.length != 2)
		{
			System.out.println("usage: java at.jku.cp.ai.visualization.Board2Png <level> <pngfile>");
			System.exit(-1);
		}

		Board board = Board.fromLevelFile(args[0]);
		RenderUtils.writeBoardAsPNG(args[1], board);

		System.out.println("board.walls     " + board.getWalls().size());
		System.out.println("board.paths     " + board.getPaths().size());
		System.out.println("board.fountains " + board.getFountains().size());

		System.out.println("board.clouds    " + board.getClouds().size());
		System.out.println("board.unicorns  " + board.getUnicorns().size());
		System.out.println("board.seeds     " + board.getSeeds().size());
		System.out.println("board.rainbows  " + board.getRainbows().size());
	}
}
