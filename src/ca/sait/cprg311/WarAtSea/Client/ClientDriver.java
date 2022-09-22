package ca.sait.cprg311.WarAtSea.Client;

/**
 * 
 * Class Description: A class containing the entry point for the War At Sea client application.
 * 
 * @author Jory Vardas
 * @version 1.0
 *
 */
public class ClientDriver
{

	/**
	 * The entry point for the War At Sea client application.
	 * @param args Arguments passed from the command line to the application.
	 */
	public static void main(String[] args)
	{
		Game game = new WarAtSeaGame();
		game.init();
		game.play();
		game.clean();
	}
}
