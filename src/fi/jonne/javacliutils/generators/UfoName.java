package fi.jonne.javacliutils.generators;

import java.util.Random;

public class UfoName {
	
	// Arrays containing vowels and consonants
	private final static char[] vowels = {'a','e','i','o','u','y','ä','ö','å'};
    private final static char[] consonants = {'b','c','d','f','g','h','j','k','l','m','n','p','q','r','s','t','v','w','x','z'};
    
    // Newly created name will go to this variable
    public String name = "";
    
    // Constructor for creating a new name
    public UfoName(String yourName){
    	
    	String oldName = yourName.toLowerCase(); // Convert all characters in string to lower case for comparing purposes
    	String newName = "";
    	
    	// Boolean variables for checking
    	boolean foundVowel = false;
    	boolean foundConsonant = false;
    	
    	Random r = new Random();
    	int randomVowel = 0;
    	int randomConsonant = 0;
    	
    	// Counters
    	int i = 0;
    	int j = 0;
    	int k = 0;
    	
    	// Loop for exploring every character in given name string and replacing them with random ones
    	for(i=0;i<oldName.length();i++){
    		
    		// If name string has two exactly same characters next to each other
            if(i<oldName.length()-1 && (oldName.charAt(i) == oldName.charAt(i+1))){
                
                // If vowels found then replace those two with newly generated vowels
                for(j=0;j<vowels.length;j++){
                    if(oldName.charAt(i) == vowels[j]){
                    	randomVowel = r.nextInt(vowels.length);
                    	newName += String.valueOf(vowels[randomVowel]);
                    	newName += String.valueOf(vowels[randomVowel]);
                        foundVowel = true;
                    }
                }
                
                // Same thing with consonants as vowels
                if(foundVowel == false){
                    for(k=0;k<consonants.length;k++){
                        if(oldName.charAt(i) == consonants[k]){
                        	randomConsonant = r.nextInt(consonants.length);
                        	newName += String.valueOf(consonants[randomConsonant]);
                        	newName += String.valueOf(consonants[randomConsonant]);
                            foundConsonant = true;
                        }
                    }
                }
                
                i++; // Generated two characters in one round so need to skip one round
                
            }else {
            for(j=0;j<vowels.length;j++){
                if(oldName.charAt(i) == vowels[j]){
                	randomVowel = r.nextInt(vowels.length);
                	newName += String.valueOf(vowels[randomVowel]);
                    foundVowel = true;
                }
            }
            if(foundVowel == false){
                for(k=0;k<consonants.length;k++){
                    if(oldName.charAt(i) == consonants[k]){
                    	randomConsonant = r.nextInt(consonants.length);
                    	newName += String.valueOf(consonants[randomConsonant]);
                        foundConsonant = true;
                    }
                }
            }
            }
            
            // If character is something else than recognized character
            if(foundVowel == false && foundConsonant == false){
            	newName += oldName.substring(i, i+1);
            }
            
            // Resetting checkers
            foundVowel = false;
            foundConsonant = false;
    	}
    	
    	// Convert first character of a new name to upper case character
        String[] names = newName.split(" ");
        
        String tempName = "";
        
        for(i=0;i<names.length;i++){
        	tempName += names[i].substring(0, 1).toUpperCase() + names[i].substring(1) + " ";
        }
        
        this.name = tempName;
    }
    
    // This method tries to fix "ugly" character combinations or currently unsupported characters. For future development!
    public void fixName(){
    	String fixedname;
    	
    	fixedname = name.replace("kc", "ck");
    	fixedname = name.replace("ö", "o");
    	fixedname = name.replace("ä", "a");
    	fixedname = name.replace("å", "o");
    	
    	this.name = fixedname;
    }
}
