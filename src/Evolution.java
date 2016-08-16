public class Evolution {
    private String familyName;
    private int amountOfEvolution;
    private int amountOfPokemon;
    private int candies;
    private int candiesToEvolve;
    private String[] pokeArr = new String[5];

    public Evolution(String familyName, int candies, int candiesToEvolve) {
        this.familyName = familyName;
        this.candies = candies;
        this.candiesToEvolve = candiesToEvolve;

        int candiesTemp = candies;
        while (candiesTemp >= candiesToEvolve) {
            candiesTemp -= candiesToEvolve;
            amountOfEvolution += 1;
            candiesTemp += 1;
        }

        increment();
    }

    public void increment() {
        amountOfPokemon += 1;
    }

    public String getFamily() {
        return familyName;
    }

    public int getPokemon() {
        return amountOfPokemon;
    }

    public int getEvolutions() {
        return amountOfEvolution;
    }

    public int getCandies() {
        return candies;
    }

    public int getEvolveCandies() {
        return candiesToEvolve;
    }

    public String[] toArray() {
        pokeArr[0] = familyName;
        pokeArr[1] = Integer.toString(candies);
        pokeArr[2] = Integer.toString(candiesToEvolve);
        pokeArr[3] = Integer.toString(amountOfEvolution);
        pokeArr[4] = Integer.toString(amountOfPokemon);

        return (pokeArr);
    }
}
