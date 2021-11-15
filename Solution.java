package com.javarush.task.task22.task2209;

import java.io.*;
import java.util.*;

/* 
Составить цепочку слов
*/

public class Solution {
    public static void main(String[] args) {
        //...
        ArrayList<String> wordsList = new ArrayList<>();
        try( BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)))
        {
            String filename = reader.readLine();
            Scanner sc = new Scanner(new FileReader(filename));
            while(sc.hasNext())
            {
                wordsList.add(sc.next());
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        StringBuilder result = getLine(wordsList.toArray(new String[0]));
        System.out.println(result.toString());

    }

    public static StringBuilder getLine(String... words)
    {
        StringBuilder sb = new StringBuilder();
        ArrayList<String> example = new ArrayList<String>(Arrays.asList(words));
        String letterS = "";
        String letterE = "";

        /** находим первое и последнее слово, если есть */
        String firstWord = findFirst(example);
        //System.out.println(firstWord);
        String lastWord = findLast(example);
        //System.out.println(lastWord);

        /** ищем сочетания используя граф записанный в виде списка*/

        ArrayList<word> graphList= new ArrayList<>(); // список слов(класса word) из первоначального списка example со своими стеками
        // для хранения расматриваемого сочетания. в стеке viewed лежат "использованные" возможные пути, а в стеке available еще не
        // рассмотренные возможности. Данные из одного стека перекидываются в другой , тем самым фиксируется рассматриваемый вариант.

        for (int i = 0; i < example.size(); i++) {
            graphList.add(new word());
            String tmp = example.get(i);
            letterE = tmp.substring(tmp.length()-1).toLowerCase();
            for (int j = 0; j < example.size(); j++) {
                if (i == j) continue;
                String tmp1 = example.get(j);
                letterS = tmp1.substring(0,1).toLowerCase();
                if (letterE.equals(letterS))
                {
                    graphList.get(i).addAvailable(j);
                }
            }
        }

        for (int i = 0; i < example.size(); i++) {
            if (i == example.indexOf(lastWord)) continue;
            sb = trying(example,graphList,i);
            if (sb.length() > 0) break;

        }
        return sb;
    }

    public static String findFirst(ArrayList<String> arr)
    {
        StringBuilder sb = new StringBuilder();
        String LetterS = "";
        String LetterE = "";
        boolean find = false;
        for (int i = 0; i < arr.size(); i++)
        {
            LetterS = arr.get(i).substring(0,1).toLowerCase();
            for (int j = 0; j < arr.size(); j++)
            {
                if (i == j && j != arr.size()-1) continue;
                String tmp = arr.get(j);
                LetterE = tmp.substring(tmp.length()-1).toLowerCase();
                if(LetterE.equals(LetterS)) break;
                if (j == arr.size()-1)
                {
                    find = true;
                }
            }
            if(find)
            {
                sb.append(arr.get(i));
                break;
            }
        }
        return sb.toString();
    }

    public static String findLast (ArrayList<String> arr)
    {
        StringBuilder sb = new StringBuilder();
        String LetterS = "";
        String LetterE = "";
        boolean find = false;
        for (int i = 0; i < arr.size(); i++) {
            LetterE = arr.get(i).substring(arr.get(i).length()-1).toLowerCase();
            for (int j = 0; j < arr.size(); j++) {
                if(i == j && j !=arr.size()-1) continue;
                LetterS = arr.get(j).substring(0,1).toLowerCase();
                if (LetterS.equals(LetterE)) break;
                if (j == arr.size()-1)
                {
                    find = true;
                }
            }
            if(find)
            {
                sb.append(arr.get(i));
                break;
            }
        }
        return sb.toString();
    }


    public static StringBuilder trying(ArrayList<String> origin, ArrayList<word> arr, int begin)
    {
        for(word x: arr)
        {
            x.moveToAvailable();
        }
        StringBuilder sbTry = new StringBuilder();
        // создаем список слов buffer, который отражает какие слова использовались в данной цепочке,
        // какие слова можно брать - если они еще не использовались, а какие нет. Когда список пуст - все слова были использованы
        ArrayList<String> buffer = new ArrayList<>(origin);
        int next = -1;
        for (int i = begin;buffer.size() != 0 ; ) {
                if (i == begin)
                {
                    sbTry.setLength(0);
                    sbTry.append(origin.get(begin));
                    buffer.remove(origin.get(begin));
                }
                try
                {
                   while (arr.get(i).getAvailable().size() != 0) {
                       next = arr.get(i).getAvailable().peek(); // берем номер слова в списке origin из стека available и проверяем есть ли такое в buffer
                       if (buffer.contains(origin.get(next))) // если есть, значит будем работать с ним
                       {
                           sbTry.append(" ").append(origin.get(next)); // прибавляем пробел и слово по номеру из списка origin
                           buffer.remove(origin.get(next)); // удаляем слово из buffer используя значение из origin
                           arr.get(i).addViewed(arr.get(i).getAvailable().pop()); // перемещаем слово из стека available в стек viewed
                           i = next;

                       }
                       else {
                           arr.get(i).addViewed(arr.get(i).getAvailable().pop()); // перемещаем слово из стека available в стек viewed

                       }

                   }
                   if (buffer.size() != 0) throw new EmptyStackException();

                }
                catch (EmptyStackException e)
                {

                    if (i == begin)
                    {
                        sbTry.setLength(0);
                        return sbTry;
                    }
                    int posLastWord = sbTry.lastIndexOf(" ") + 1; // выясняем индекс начала последнего слова
                    String currentWord = sbTry.substring(posLastWord); // выделяем это слово
                    sbTry.delete(posLastWord-1,sbTry.length()); // удаляем это слово из собираемой строки
                    i = origin.indexOf(currentWord); // присваеваем индекс этого последнего добавленного слова к i
                    arr.get(i).moveToAvailable(); // перемещаем обратно из viewed в available
                    buffer.add(origin.get(i)); // возвращаем в буфер последнее слово
                    posLastWord = sbTry.lastIndexOf(" ") + 1; // выясняем индекс начала нового последнего слова
                    currentWord = sbTry.substring(posLastWord); // выделяем это слово
                    i = origin.indexOf(currentWord); // присваеваем индекс этого последнего добавленного слова к i
                }
        }
        return sbTry;
    }
}
// c:/гифки/Res.txt