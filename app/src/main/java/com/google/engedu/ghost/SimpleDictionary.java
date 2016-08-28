package com.google.engedu.ghost;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    Random random = new Random();

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH) {
                words.add(line.trim());
            }
        }
        Collections.sort(words);
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {

        if(prefix.isEmpty()){
            return words.get(random.nextInt(words.size()));
        }else{

            Comparator<String> comparator = new Comparator<String>() {
                @Override
                public int compare(String lhs, String rhs) {
                    lhs = lhs.substring(0,Math.min(lhs.length(),rhs.length()));
                    return lhs.compareTo(rhs);
                }
            };

            int index = Collections.binarySearch(words,prefix,comparator);
            if(index < 0){
                return null;
            }else{
                Log.d("word", words.get(index));
                return words.get(index);
            }
        }
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        return null;
    }
}
