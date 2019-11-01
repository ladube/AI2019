/*Lauren Dube
 * c2019 Courtney Brown 
 * 
 * Class: MarkovGenerator
 * Description: This contains the procedure for Markov training
 * 
 */
import java.util.ArrayList;
public class MarkovGenerator<G> extends ProbablilityGenerator<G>{
	int lastIndex = -1;
	int tokenIndex = 0;
	//transition table
	ArrayList<ArrayList<Integer>> transitionTableCount = new ArrayList<ArrayList<Integer>>(); // = new ArrayList();
	ArrayList<ArrayList<Float>> transitionTable = new ArrayList<ArrayList<Float>>();
	
	//extend and override train
	public void train() {
		for(int i = 0; i < tokenArray.size(); i++) {
			if(alphabet.contains(tokenArray.get(i)) == false) {
				tokenIndex = alphabet.size();
				alphabet.add(tokenArray.get(i));
				expandTable();
				if(alphabet.size() > 1)
					transitionTableCount.get(alphabet.indexOf(tokenArray.get(lastIndex))).set(tokenIndex, 1);
			} else { //if token is already listed in alphabet
				int prevAlphabetIndex = alphabet.indexOf(tokenArray.get(lastIndex));
				int currAlphabetIndex = alphabet.indexOf(tokenArray.get(i));
				int tmp = transitionTableCount.get(prevAlphabetIndex).get(currAlphabetIndex);
				tmp++;
				transitionTableCount.get(prevAlphabetIndex).set(currAlphabetIndex,tmp);
			}
			lastIndex = i; 
		}
	}
	private void expandTable() {
		ArrayList<Integer> tmp = new ArrayList<Integer>();
		ArrayList<Float> tmpFloat = new ArrayList<Float>();
		for(int i = 0; i < transitionTableCount.size(); i++) {
			tmp.add(0);
			tmpFloat.add((float) 0);
		}
		transitionTableCount.add(tmp);
		transitionTable.add(tmpFloat);
		for(int i = 0; i < transitionTableCount.size(); i++) {
			transitionTableCount.get(i).add(0);
			transitionTable.get(i).add((float) 0);
		}
	}
	public void normalize() {
		for(int i = 0; i < transitionTableCount.size(); i++) {
			int sum = 0;
			for(int j = 0; j < transitionTableCount.get(i).size(); j++)
				sum += transitionTableCount.get(i).get(j);
			for(int j = 0; j < transitionTableCount.get(i).size(); j++) {
				float val = transitionTableCount.get(i).get(j);
				if(sum == 0)
					val = 0;
				else
					val /= sum;
				transitionTable.get(i).set(j,val);
			}
		}
		//System.out.println("Alphabet size: " + alphabet.size() + " TT size: " + transitionTable.size()); //check 
	}
	public void print(){ 
		for(int i = 0; i < transitionTable.size(); i++) {
			System.out.print("[");
			for(int j = 0; j < transitionTable.get(i).size(); j++) {
				System.out.print(transitionTable.get(i).get(j) + ", ");
			}
			System.out.println("]");
		}
	}
	//override the generate function
	void generate(G initToken) {
		//clear the generated array if full already
		genarray.clear();
		for(int i = 0; i < tokenArray.size(); i++) {
			tokenIndex = alphabet.indexOf(initToken);
			G genToken = generateToken(tokenIndex);
			genarray.add(genToken);
			initToken = genToken;
		}
	}
	public G generateToken(int i) {//determine the generated token based off of transition table
		float randIndex = (float)Math.random();
		boolean found = false;
		int index = 0;
		float total = 0;
	
		while(!found && index < alphabet.size()) {
			total += transitionTable.get(i).get(index);
			found = randIndex <= total;
			index++;
		}
		if(found) return (G) alphabet.get(index-1);
		else return (G) alphabet.get(alphabet.size()-1);
	}
}