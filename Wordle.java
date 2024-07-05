/*
 * File: Wordle.java
 * -----------------
 * This module is the starter file for the Wordle assignment.
 * BE SURE TO UPDATE THIS COMMENT WHEN YOU COMPLETE THE CODE.
 */

import java.util.ArrayList;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import edu.willamette.cs1.wordle.WordleDictionary;
import edu.willamette.cs1.wordle.WordleGWindow;

public class Wordle {
    private String secretWord=WordleDictionary.LEVEL1_WORDS[(int)(Math.random()*WordleDictionary.LEVEL1_WORDS.length)];
//    test bug
//    private String secretWord="glass";
    public void run() {
        gw = new WordleGWindow();
        gw.addEnterListener((s) -> enterAction(s));//EnterListener听用户的enter，按一次enter，执行一次enterAction
    }//call back 自动触发enterAction

/*
 * Called when the user hits the RETURN key or clicks the ENTER button,
 * passing in the string of characters on the current row.
 */

    public void enterAction(String s) {
      ArrayList<String> FiveLetterWordsList=new ArrayList<String>();//创建合法的单词集合
      for(int i=0;i<WordleDictionary.FIVE_LETTER_WORDS.length;i++)
        {
    	  FiveLetterWordsList.add(WordleDictionary.FIVE_LETTER_WORDS[i]);//将数组类型转换成集合,以查找s是否在其中
    	}//end of for
      //检查单词是否合法
        if(FiveLetterWordsList.contains(s.toLowerCase()))
        {      
             //gw.showMessage("in");
//        	if(gw.getCurrentRow()<WordleGWindow.N_ROWS){
                     paintSqaure(s.toLowerCase());
                     paintKey(s.toLowerCase());
                                           
        	if(s.toLowerCase().equals(secretWord))
        	    { 
        			gw.showMessage("Success");
               		return;
                }
        	else if(gw.getCurrentRow()==WordleGWindow.N_ROWS-1)//确保row始终小于等于5,避免超出边界
        	{
        		gw.showMessage("Lose, answer is "+secretWord);
        	    return;
        	}
        	   if(gw.getCurrentRow()<5)//若row小于五即可+1,去下一行接着执行
        	        gw.setCurrentRow(gw.getCurrentRow() + 1);
        }
        else 
        	gw.showMessage("Not in word list.");//不合法
        
    }
/* Startup code */
    

    public static void main(String[] args) {
        new Wordle().run();
    }

/* Private instance variables */

    private WordleGWindow gw;
    private void paintSqaure(String guess){
    	   // 将秘密单词和猜测单词转换为字符数组
        char[] secretChars = secretWord.toCharArray();
        char[] guessChars = guess.toLowerCase().toCharArray();
        
        // 记录秘密单词中每个字母的剩余出现次数
        int[] secretLetterCounts = new int[26];
        for (char c : secretChars) {
            secretLetterCounts[c - 'a']++;
        }

        // 记录猜测单词中某字母(以索引代替)是否变绿
        boolean[] isGreen = new boolean[5];
        //首先判断绿色,若为绿色,该字母所在位置不再改变颜色
        for (int i = 0; i < guessChars.length; i++) 
        {
            char guessChar = guessChars[i];      
            for (int j = 0; j < secretChars.length; j++) 
            {
                if (guessChar == secretChars[j]) 
                {
                //if(guessLetterCounts[guessChar - 'a']>secretLetterCounts[guessChar - 'a'])
                    if (i == j && !isGreen[i]) 
                    {
                        // 字母在当前位置且未被标记
                    	gw.setSquareColor(gw.getCurrentRow(), i, WordleGWindow.CORRECT_COLOR);// 设置为绿色
                    	isGreen[i] = true; // 标记为已变绿,接下来不在改变其颜色
                        secretLetterCounts[guessChar - 'a']--;//秘密单词中已经出现过一次
                    } 
                }
            }//end of inner
        }//end of outer
        
        for (int i = 0; i < guessChars.length; i++) {
            char guessChar = guessChars[i];
            // 检查当前字母是否在秘密单词中
            for (int j = 0; j < secretChars.length; j++) {
                if (guessChar == secretChars[j]) {
//                	if(guessLetterCounts[guessChar - 'a']>=secretLetterCounts[guessChar - 'a'])
                   if (i != j && secretLetterCounts[guessChar - 'a'] > 0) {
                           // 字母不在当前位置但在秘密单词中
                    	   gw.setSquareColor(gw.getCurrentRow(), i, WordleGWindow.PRESENT_COLOR);// 设置为黄色
                           secretLetterCounts[guessChar - 'a']--;//记为秘密单词中出现过一次
                            // 找到黄色字母后跳出循环
                           break;
                    } //处理重复字母:如果字母出现在秘密单词中,但出现次数超过秘密单词中所含有次数,给灰色(前提:该位置未被标为绿色)
                   else if(i!=j&&secretLetterCounts[guessChar - 'a'] <= 0&&!isGreen[i]){
                	   gw.setSquareColor(gw.getCurrentRow(), i, WordleGWindow.MISSING_COLOR);
                	   break;
                   }
                }else if(!isGreen[i])
                	gw.setSquareColor(gw.getCurrentRow(), i, WordleGWindow.MISSING_COLOR);
            }//end of inner 
        }//end of outer

           
    }//end of paintSquare
    
    public void paintKey(String guess){
    	boolean[]isGreen=new boolean[26];
    	char[] secretChars = secretWord.toCharArray();
    	char[] guessChars = guess.toLowerCase().toCharArray();
    	for(int i=0;i<guess.length();i++){
    		 char guessChar = guessChars[i];
    		 if(gw.getKeyColor((guessChar+"").toUpperCase())!=WordleGWindow.CORRECT_COLOR)//若当前字母不为绿色,首先判断是否为绿色
    		for(int j=0;j<secretWord.length();j++){
    			 if (guessChar == secretChars[j]) {
//                 	if(guessLetterCounts[guessChar - 'a']>secretLetterCounts[guessChar - 'a'])
                     if (i == j && !isGreen[guessChar-'a']) {
                         // 字母在当前位置且未被标记
                    	 gw.setKeyColor((guessChar+"").toUpperCase(),WordleGWindow.CORRECT_COLOR );// 设置为绿色
                     	isGreen[guessChar-'a'] = true; // 标记为已变绿                       
                     	break;
                     } 
                 }
    			}//end of innerfor
    		   }//end of outerfor
    	
    	for (int i = 0; i < guessChars.length; i++) {
            char guessChar = guessChars[i];
            // 检查当前字母是否为绿色,为绿色不执行,不为绿色执行
            if(gw.getKeyColor((guessChar+"").toUpperCase())!=WordleGWindow.CORRECT_COLOR)
            for (int j = 0; j < secretChars.length; j++) {
                if (guessChar == secretChars[j]&&!isGreen[guessChar-'a']) {
//                	if(guessLetterCounts[guessChar - 'a']>=secretLetterCounts[guessChar - 'a'])
                   if (i != j) {
                           // 字母不在当前位置但在秘密单词中
                	       gw.setKeyColor((guessChar+"").toUpperCase(), WordleGWindow.PRESENT_COLOR);// 设置为黄色
                            // 找到黄色字母后跳出循环
                           break;
                    } 
                         
                }else if(!isGreen[guessChar-'a']) {
             	   gw.setKeyColor((guessChar+"").toUpperCase(), WordleGWindow.MISSING_COLOR);
                }
            }//end of inner 
        }//end of outer
    }//end of paintKey
    
}//end of Wordle

