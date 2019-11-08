/* Lauren Dube
 * c2017-2019 Courtney Brown 
 * 
 * Class: HelloWorldMidiMain
 * Description: Demonstration of MIDI file manipulations, etc. & 'MelodyPlayer' sequencer
 * 
 */

import processing.core.*;
import java.util.*; 

//importing the JMusic stuff
import jm.music.data.*;
import jm.JMC;
import jm.util.*;
import jm.midi.*;

import java.io.UnsupportedEncodingException;
import java.net.*;

			//make sure this class name matches your file name, if not fix.
public class HelloWorldMidiMain extends PApplet {

	MelodyPlayer player; //play a midi sequence
	MidiFileToNotes midiNotes; //read a midi file
	
	ProbablilityGenerator<Integer> p = new ProbablilityGenerator<Integer>();
	ProbablilityGenerator<Double> r = new ProbablilityGenerator<Double>();
	MarkovGenerator<Integer> p2 = new MarkovGenerator<Integer>();
	MarkovGenerator<Double> r2 = new MarkovGenerator<Double>();
	MarkovN<Integer> p3 = new MarkovN<Integer>();
	MarkovN<Double> r3 = new MarkovN<Double>();
	PredictionSuffixTree<String> t1 = new PredictionSuffixTree<String>();
	PredictionSuffixTree<Integer> pt = new PredictionSuffixTree<Integer>();
	PredictionSuffixTree<Double> rt = new PredictionSuffixTree<Double>();
	
	boolean play = false;
	int projectNum = 1;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main("HelloWorldMidiMain"); //change this to match above class & file name 
//*********Test the Markov Generator*****************
//		ArrayList<Integer> tst = new ArrayList<Integer>();
//		for(int i = 0; i < 15; i++) {
//			tst.add(i/3);
//		}
//		MarkovGenerator<Integer> m = new MarkovGenerator();
//		m.setTokens(tst);
//		m.train();
	}

	//setting the window size to 300x300
	public void settings() {
		size(300, 300);

	}

	//doing all the setup stuff
	public void setup() {
		fill(120, 50, 240);

		// returns a url
		String filePath = getPath("mid/MaryHadALittleLamb.mid");
		// playMidiFile(filePath);

		midiNotes = new MidiFileToNotes(filePath); //creates a new MidiFileToNotes -- reminder -- ALL objects in Java must 
													//be created with "new". Note how every object is a pointer or reference. Every. single. one.
//		// which line to read in --> this object only reads one line (or ie, voice or ie, one instrument)'s worth of data from the file
		midiNotes.setWhichLine(0);

		player = new MelodyPlayer(this, 100.0f);
		player.setup();
		player.setMelody(midiNotes.getPitchArray()); //instead of setting Melody, send it to ProbabilityGen?
		player.setRhythm(midiNotes.getRhythmArray());
		
		
	}

	public void draw() {
		background(255,255,255);
		fill(255,0,128);
		rect(width/4, 210, 30, 30);
		if(projectNum == 1)
			text("Project 1", 50, 50);
		fill(55,222,219);
		rect(width/2, 210, 30, 30);
		if(projectNum == 2)
			text("Project 2", 50, 50);
		fill(252,183,45);
		rect(width - width/4, 210, 30, 30);
		if(projectNum == 3)
			text("Project 3", 50, 50);
		fill(0,0,0);
		text("Click below to select the Project Number", 20, 200);
		text("1                    2                     3", width/4 + 15, 230);
		text("Press 1 for Unit Test 1", 30, 80);
		text("Press 2 for Unit Test 2", 30, 110);
		text("Press 3 for Unit Test 3", 30, 140);
		text("Press t to test Tree", 30, 170);
		if(play == true) {
			player.play(); //play each note in the sequence -- the player will determine whether is time for a note onset
		}
	}

	//this finds the absolute path of a file
	String getPath(String path) {

		String filePath = "";
		try {
			filePath = URLDecoder.decode(getClass().getResource(path).getPath(), "UTF-8");

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath;
	}

	//this function is not currently called. you may call this from setup() if you want to test
	//this just plays the midi file -- all of it via your software synth. You will not use this function in upcoming projects
	//but it could be a good debug tool.
	void playMidiFile(String filename) {
		Score theScore = new Score("Temporary score");
		Read.midi(theScore, filename);
		Play.midi(theScore);
	}

	//GUI Interactivity
	public void keyPressed() {
		if (key == ' ') {
			player.reset();
			println("Melody started!");
		}
		if(key == '1')
		{
			unitTestOne();
		}
		if(key == '2') {
			unitTestTwo();
		}
		if(key == '3') {
			unitTestThree();
		}
		if(key == 'P') {
			if(play == false) {
				play = true;
			}else {play = false;}
		}
		if(key == 't') {
			treeTest();
		}
	}
	public void mouseClicked() {
		if(mouseX >= (width/4) && mouseX <= (width/4 + 30) && mouseY >= 210 && mouseY <= 240)
			projectNum = 1;
		if(mouseX >= (width/2) && mouseX <= (width/2 + 30) && mouseY >= 210 && mouseY <= 240)
			projectNum = 2;
		if(mouseX >= (width - width/4) && mouseX <= (width - width/4 + 30) && mouseY >= 210 && mouseY <= 240)
			projectNum = 3;
	}
	//Unit Test One function 
	public void unitTestOne() {
		ArrayList<Integer> pitchInput = midiNotes.getPitchArray();
		
		ArrayList<Double> rhythmInput = midiNotes.getRhythmArray();
	
		if(projectNum == 1) {		
			p.setTokens(pitchInput);
			r.setTokens(rhythmInput);
			System.out.println("------Pitches Probability Distribution:-------");
			p.train();
			p.print(1);
			System.out.println("------------------------------");
			System.out.println("------Rhythm Probability Distribution:-------");
			r.train();
			r.print(1);
			System.out.println("------------------------------");
		}else if(projectNum == 2) {
			p2.setTokens(pitchInput);
			r2.setTokens(rhythmInput);
			System.out.println("------Pitches Transition Table:-------");
			p2.train();
			p2.normalize();
			p2.print();
			System.out.println("------------------------------");
			System.out.println("------Rhythm Transition Table:-------");
			r2.train();
			r2.normalize();
			r2.print();
			System.out.println("------------------------------");
		}else if(projectNum ==3) {
			p3.setTokens(pitchInput);
			r3.setTokens(rhythmInput);
			System.out.println("------N Pitches Transition Table:-------");
			p3.train();
			p3.normalize();
			p3.print();
			System.out.println("------------------------------");
			System.out.println("------N Rhythm Transition Table:-------");
			r3.train();
			r3.normalize();
			r3.print();
			System.out.println("------------------------------");
		}
	}
	public void unitTestTwo() {
		if(projectNum == 1) {
			p.generate();
			r.generate();
			System.out.println(p.getGenArray());
			System.out.println(r.getGenArray());
			player.setMelody(p.getGenArray());
			player.setRhythm(r.getGenArray());
		}else if(projectNum == 2) {
			//use Project 1 to generate a single starter token
			Integer ptoken = p.generateToken();
			Double rtoken = r.generateToken();
			p2.generate(ptoken);
			r2.generate(rtoken);
			System.out.println(p2.getGenArray());
			System.out.println(r2.getGenArray());
			player.setMelody(p2.getGenArray());
			player.setRhythm(r2.getGenArray());
		}else if(projectNum ==3) {
			
		}
	}
	
	public void unitTestThree() {
		int numIterations = 1000;
		int length = 20;
		for(int i = 0; i < numIterations; i++) {
			if(projectNum == 1) {
				p.generate();
				p.setTokens(p.getGenArray());
				p.train();
				r.generate();
				r.setTokens(r.getGenArray());
				r.train();
			}else if(projectNum == 2) {
				Integer ptoken = p.generateToken();
				Double rtoken = r.generateToken();
				p2.generate(ptoken);
				p2.setTokens(p2.getGenArray());
				p2.train();
				r2.generate(rtoken);
				r2.setTokens(r2.getGenArray());
				r2.train();	
			}else if(projectNum ==3) {
				
			}
		}
		if(projectNum == 1) {
			System.out.println("Pitches Probability Distribution after "+ numIterations +" iterations:");
			p.print(numIterations);
			System.out.println("------------------------------");
			System.out.println("Rhythm Probability Distribution after "+ numIterations +" iterations:");
			r.print(numIterations);
			System.out.println("------------------------------");
		}
		else if(projectNum == 2) {
			p2.normalize();
			r2.normalize();
			System.out.println("Pitches Transition Table after "+ numIterations +" iterations:");
			p2.print();
			System.out.println("------------------------------");
			System.out.println("Rhythm Transition Table after "+ numIterations +" iterations:");
			r2.print();
			System.out.println("------------------------------");
		}else if(projectNum ==3) {
			p3.normalize();
			r3.normalize();
			System.out.println("Pitches Transition Table after "+ numIterations +" iterations:");
			p3.print();
			System.out.println("------------------------------");
			System.out.println("Rhythm Transition Table after "+ numIterations +" iterations:");
			r3.print();
			System.out.println("------------------------------");
		}
	}
	public void treeTest() {
		String[] abracadabra = {"a", "b", "r", "a", "c", "a", "d", "a", "b", "r", "a"};
		ArrayList<String> treeInput1 = new ArrayList(Arrays.asList(abracadabra));
		t1.setTokens(treeInput1);
		t1.train();
		System.out.println("Abacadabra Tree:");
		t1.print();
		t1.clearTree();
		String[] acadaacbda = {"a", "c", "a", "d", "a", "a", "c", "b", "d", "a"};
		ArrayList<String> treeInput2 = new ArrayList(Arrays.asList(acadaacbda));
		t1.setTokens(treeInput2);
		t1.train();
		System.out.println("Acadaacbda Tree: ");
		t1.print();
		t1.clearTree();
		String[] abcccdaadcdaabcadad = {"a", "b", "c", "c", "c", "d", "a", "a", "d", "c", "d", "a", "a", "b", "c", "a", "d", "a", "d"};
		ArrayList<String> treeInput3 = new ArrayList(Arrays.asList(abcccdaadcdaabcadad));
		t1.setTokens(treeInput3);
		t1.train();
		System.out.println("Abcccdaadcdaabcadad Tree: ");
		t1.print();
		ArrayList<Integer> pitchInput = midiNotes.getPitchArray();
		pt.setTokens(pitchInput);
		ArrayList<Double> rhythmInput = midiNotes.getRhythmArray();
		rt.setTokens(rhythmInput);
		pt.train();
		rt.train();
		System.out.println("Pitch Tree: ");
		pt.print();
		System.out.println("Rhythm Tree: ");
		rt.print();
	}
}
