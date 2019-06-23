package es.ucm.fdi.tp.view.chessAnimation;

public enum OnePieceFigure {
	PACIFISTA_PX_5("pacifistaPX5", 6, 8, 8, 143,
			"The fifth Pacifista human weapon, built to look like Bartholomew Kuma. Capable"
			+ " of firing lasers from his mouth, among other terrifying attacks. Creating one"
			+ " Pacifista takes about as much money as building an entire battleship."
			),
	Princess_VIVI("princessVivi", 3, 9, 49, 6,
			"Princess of Alabasta Kingdom. Along with the Straw Hat Pirates, she saves her"
			+ " homeland from Baroque Works' plot. Afterwards, she returns to her kingdom"
			+ " to fulfill her duties."
			),
	Black_Clad_Division_Commander_PORTGAS_D_ACE("blackCladDivisionCommanderPortgasDAce", 10, 7, 47, 37,
			"Commander of the Whitebeard Pirates' second division. He's an easygoing free"
			+ " spirit who's capable of courtesy and common sense when the times call for it."
			+ " However, he flies into a rage whenever the name of Captain Whitebeard, his"
			+ " benefactor and savior, is insulted."
			),
	Straw_Hat_Pirates_Born_Again_FRANKY("strawHatPiratesBornAgainFranky", 8, 6, 70, 32,
			"Straw Hat Pirates shipwright. After happening across Dr. Vegapunk in Baldimore,"
			+ " he holed up there for two years, using the plans he found there to build new"
			+ " weapons like the gigantic \"General Franky\" robot."
			),
	Warlond_of_the_Sea_BOA_HANCOCK("warlondOfTheSeaBoaHancock", 11, 12, 50, 18,
			"Captain of the Kuja Pirates and one of the Seven Warlords of the Sea. Endowed"
			+ " with powers from the Love-Love Fruit, she can turn to stone those enthralled"
			+ " by her appearance. Boa is the oldest of the three Gorgon sisters. For a time,"
			+ " she was a slave to a World Noble while still a child."
			),
	Voyage_Dream_Pirate_King_MONKEY_D_LUFFY ("voyageDreamPirateKingMonkeyDLuffy", 15, 10, 70, 94,
			"Leader of the Straw Hat Pirates. He became the enemy of the World Government"
			+ " to rescue Robin. During his battle with CP9, he created a new \"Gear\" skill"
			+ " set that lets him unleash more powerful moves than ever."
			),
	Four_Emperors_WHITEBEARD("fourEmperorsWhitebeard", 7, 11, 82, 13,
			"Captain of the Whitebeard Pirates. He risked his life to fight for his \"sons\""
			+ " during the Paramount War. Despite all the wounds he sustained, he still stood"
			+ " strong, even after death. The pride he bore on his back allowed for no escape"
			+ " by anyone."
			),
	Donquixote_Pirates_PICA("donquixotePiratesPica", 8, 5, 84, 18,
			"One of the top three executives of the Donquixote Pirates. His Stone-Stone Fruit"
			+ " skills let him merge with nearby stones and use them freely in battle. This"
			+ " skill can be deployed over wide distances to change the shape of the land itself."
			);

	private String name;
	private int staticc, dinamic, animation;
	private int battleDirection;
	private String description;
	
	OnePieceFigure(String name, int staticc, int dinamic, int animation, int battleDirection, String description){
		this.name = name;
		this.staticc = staticc;
		this.dinamic = dinamic;
		this.animation = animation;
		this.battleDirection = battleDirection;
		this.description = description;
	}

	public String getName() {
		return name;
	}
	
	public int getStatic() {
		return staticc;
	}
	
	public int getDinamic() {
		return dinamic;
	}
	
	public int getAnimation(){
		return animation;
	}
	
	public int getBattleDirection(){
		return battleDirection;
	}
	
	public String getDescription(){
		return description;
	}
}
