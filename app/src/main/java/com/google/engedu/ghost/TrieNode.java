package com.google.engedu.ghost;

import java.util.HashMap;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        HashMap<String, TrieNode> next = this.children;

        for(int i = 0; i < s.length(); i++ ){
            char c = s.charAt(i);

            TrieNode t ;
            if(children.containsKey(c)){
                t = children.get(c);
            }else{
                t = new TrieNode();
                children.put(String.valueOf(c), t);
            }

            children = t.children;

            if(i == s.length() - 1){
                t.isWord = true;
            }
        }
    }

    public boolean isWord(String s) {
      return false;
    }

    public String getAnyWordStartingWith(String s) {
        return null;
    }

    public String getGoodWordStartingWith(String s) {
        return null;
    }
}
