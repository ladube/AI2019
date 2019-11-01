/*Lauren Dube
 * c2019 Courtney Brown 
 * 
 * Class: Probablility Generator
 * Description: This class has generic train and generate functions
 * 
 */
import java.util.ArrayList;

//ProbabilityGenerator trains and generates melodies
public class ProbablilityGenerator<G> {
	int pitch;
	double rhythm;
	
	//array of example tokens used for training
	ArrayList<G> tokenArray = new ArrayList<G>();
	public void setTokens(ArrayList<G> input) { 
		tokenArray.clear(); //clear the token array if full already
		for(int i = 0; i < input.size(); i++)
			tokenArray.add(input.get(i));
	}
	
	//array of generated tokens 
	protected ArrayList<G> genarray = new ArrayList<G>();
	ArrayList<G> getGenArray(){
		return genarray;
	}
	//array alphabet 
	protected ArrayList<G> alphabet = new ArrayList<G>();
	
	//alphabet count
	private ArrayList<Integer> alphabetcount = new ArrayList<Integer>();
	
	//normalized count
	float probability;
	private ArrayList<Float> probabilitydistru = new ArrayList<Float>();
	
	public void train() {//compare tokenArray to alphabet Array
		for(int i = 0; i < tokenArray.size(); i++) {
			if(alphabet.contains(tokenArray.get(i)) == false) {
				alphabet.add(tokenArray.get(i)); 
				alphabetcount.add(1);
			}else{ //if token is already listed in alphabet
				//add to previous count to distinguish the number of times the token occurs
				int alpha = alphabet.indexOf(tokenArray.get(i)); //find index of token in alphabet
				int add = alphabetcount.get(alpha) + 1; //using index, get the value in alphabetcount's equivalent and increment 
				alphabetcount.set(alpha, add); //set incremented count in index
			}
		}
	}
	public void print(int iterations) {//print out the probabilities
		if(iterations <= 0)
			return; 
		for(int j = 0; j < alphabetcount.size(); j++) {
			probability = (float)alphabetcount.get(j)/(tokenArray.size()*iterations); //index through to get all probabilities
			System.out.println("Token:" + alphabet.get(j) + " | Probability:" + probability);
			probabilitydistru.add(probability);
		}
	}
	void generate() {
		//clear the generated array if full already
		genarray.clear();
		//for every note, find the generated token
		for(int i = 0; i < tokenArray.size(); i++) {
			genarray.add(generateToken());
		}
	}
	public G generateToken() {//determine the generated token based off of probability distribution
		float randIndex = (float)Math.random();
		boolean found = false;
		int index = 0;
		float total = 0;
		
		while(!found && index < alphabetcount.size()) {
			total += probabilitydistru.get(index);
			found = randIndex <= total;
			index++;
		}
		if(found) return alphabet.get(index-1);
		else return alphabet.get(alphabet.size()-1);
	}
}
