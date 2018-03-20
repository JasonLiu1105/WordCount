package com.company;

import java.io.*;
import java.util.Scanner;

/**
 * created by LTZ  2018-03-16
 * 统计单词数，行数，字符数
 */

public class Main {

    //静态全局变量
    private static String aimFile;
    public static int lineC =0;//行数
    public static int charC =0;//字符数
    public static int wordC =0;//单词数

    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        System.out.print("目标文件地址:");
        String filePath=scanner.nextLine();

        System.out.print("请输入操作(以空格隔开):");
        String[] charArr=scanner.nextLine().split(" ");

        System.out.print("请输入输出文件地址，不输出直接回车:");
        String outputFile=scanner.nextLine();

        File file=new File(outputFile);
        count(filePath);
        if (file.exists()){
            ouput(file,charArr);
        }
        for (String c:charArr){
            switch (c){
                case "c":
                    System.out.println("字符数:"+ charC);
                    break;
                case "l":
                    System.out.println("行数:"+ lineC);
                    break;
                case "w":
                    System.out.println("单词数:"+ wordC);
                    break;
                default:break;
            }
        }
    }

    //计算函数
    public static void count(String filePath){
        try {
            File file=new File(filePath);
            if (!file.exists()){
                return;
            }
            aimFile=file.getName();

            FileInputStream fis=new FileInputStream(file);
            InputStreamReader isr=new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String s="";
            while ((s=br.readLine())!=null) {
                if(s.length()>1){
                    for (int i=1;i<s.length();i++){
                        if ((s.charAt(i)==' '&&s.charAt(i-1)!=' '&&s.charAt(i-1)!=','&&s.charAt(i-1)!='，')||
                                (s.charAt(i)==','&&s.charAt(i-1)!=' '&&s.charAt(i-1)!=','&&s.charAt(i-1)!='，')
                                ||(s.charAt(i)=='，'&&s.charAt(i-1)!=' '&&s.charAt(i-1)!=','&&s.charAt(i-1)!=',')){
                            wordC++;
                        }
                        if (s.charAt(i)!=' '&&s.charAt(i)!='，'&&s.charAt(i)!=','&&i==s.length()-1) wordC++;
                    }
                }else {
                    if(s.equals(" ")||s.equals(","))continue;
                    wordC++;
                }
                lineC++;
                charC += s.length();
            }
            br.close();
            isr.close();
            fis.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //输出函数
    public static void ouput(File filePath,String[] arr){

        BufferedWriter bw=null;
        try {
            FileWriter fw = new FileWriter(filePath.getAbsoluteFile());
            bw = new BufferedWriter(fw);
        }catch (IOException e){
            e.printStackTrace();
        }
        for (String c:arr){
            switch (c){
                case "c":
                    try {
                        bw.write(aimFile+",字符数:"+ charC +'\n');
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    break;
                case "l":
                    try {
                        bw.write(aimFile+",行数:"+ lineC +'\n');
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    break;
                case "w":
                    try {
                        bw.write(aimFile+",单词数:"+ wordC +'\n');
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    break;
                default:break;
            }
        }
        try {
            bw.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}