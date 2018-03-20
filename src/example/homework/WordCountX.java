package example.homework;
import java.io.BufferedReader;
import java.io.File;
import java.io.*;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.*;

public class WordCountX {
    public static void main(String[] args){
//        String inPath = "/Users/jason/Desktop/1.txt"; //文件名

        String outPath="result.txt"; //输出文件名,默认为result.txt
        int line=0;
        int num=0;
        int letter=0;
        int space=0;
        int word=0;
        boolean c = false;
        boolean w = false;
        boolean l = false;
        boolean o = false;
        boolean a = false;
        boolean e = false;
        boolean s =false;
      String inPath=null;
        String stopPath=null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].charAt(0) == '-' && args[i].length() < 3) {
                switch (args[i].charAt(1)) {
                    case 't':
                        c = true;
                        break;
                    case 'w':
                        w = true;
                        break;
                    case 'l':
                        l = true;
                        break;
                    case 'c':
                        c = true;
                        break;
                    case 'o':
                        o = true;
                        i++;
                        if (i < args.length) {
                            outPath = args[i];
                        } else {
                            System.out.println("lacking path of out put file");
                        }
                        break;
                    case 'a':
                        a=true;
                        break;
                    case 's':
                        s=true;
                        break;
                    case 'e':
                        e=true;
                        i++;
                        if (i<args.length){
                            stopPath = args[i];
                        } else {
                            System.out.println("lacking path of out put file");
                        }
                        break;
                    default:
                        break;
                }
            } else {
                inPath = args[i];
            }
        }
        try{
            BufferedReader br= new BufferedReader(new FileReader(inPath));
            String str = null;
            long whitLines=0,commentLines=0,path=0;
            while((str=br.readLine())!=null){
                line++;
                num += countNumber(str);
                letter += countLetter(str);
                word += countChinese(str);
                space += countSpace(str);long[] res=coutDiff(str);
                whitLines+=res[1];
                commentLines+=res[0];
                path+=res[2];
            }
            if (l) {
                printToFile(outPath,inPath+", 行数："+line);
            }
            if (c) {
                printToFile(outPath,inPath+", 字符数："+letter);
            }
            if (w) {
                printToFile(outPath,inPath+", 单词数："+word);
            }
            if (a) {

                printToFile(outPath,inPath+", 代码／空白／注释："+commentLines+"/"+whitLines+"/"+path);
            }
            if (e) {
                printToFile(outPath,inPath+", 单词数："+countStopWords(br, stopPath));//集体参数自己写了函数以后填
            }
        }catch (IOException e2){
            e2.printStackTrace();
        }


        System.out.println("数字数："+num);
        System.out.println("字母数"+letter);
        System.out.println("汉字数"+word);
        System.out.println("空格数"+space);
        System.out.println("行数"+line);
    }

    /**
     *递归处理文件目录
     *@param
     *@return File[]
     */
    public static ArrayList<File> findALLFile(File file,ArrayList<File> fileArrayList) {
        if(file.isDirectory())//判断file是否是目录
        {
            File[] lists = file.listFiles();
            if(lists!=null)
            {
                for(int i=0;i<lists.length;i++)
                {
                    findALLFile(lists[i],fileArrayList);//是目录就递归进入目录内再进行判断
                }
            }
        }
        fileArrayList.add(file);
        return fileArrayList;
    }

    /**
     * 统计数字数
     * @param str
     * @return
     */
    public static int countNumber(String str) {
        int count = 0;
        Pattern p = Pattern.compile("\\d");
        Matcher m = p.matcher(str);
        while(m.find()){
            count++;
        }
        return count;
    }

    /**
     * 统计字母数
     * @param str
     * @return
     */
    public static int countLetter(String str) {
        int count = 0;
        Pattern p = Pattern.compile("[a-zA-Z]");
        Matcher m = p.matcher(str);
        while(m.find()){
            count++;
        }
        return count;
    }

    /**
     * 统计汉字数
     * @param str
     * @return
     */
    public static int countChinese(String str) {
        int count = 0;
        Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]");
        Matcher m = p.matcher(str);
        while(m.find()){
            count++;
        }
        return count;
    }

    /**
     *统计不同类别行数
     *@return int[]//代码，空白，注释
     * @param
     */
    public static long[] coutDiff(String line) {
        long annotationLine = 0;
        // 记录空白行数
        long blankLine = 0;
        // 记录有效代码的行数
        long codeLine = 0;
        //假注释
        long notLine = 0;

        // 判断此行是否为注释行
        boolean comment = false;
        int whiteLines = 0;
        int commentLines = 0;
        int normalLines = 0;


        if (line.matches("^[//s&&[^//n]]*$") || line.equals("{") || line.equals("}")) {
            // 空行 :本行全部是空格或格式控制字符，如果包括代码，则只有不超过一个可显示的字符，例如“{”
            whiteLines++;
        }
        /* 本行不是代码行，并且本行包括注释。一个有趣的例子是有些程序员会在单字符后面加注释：
         *  }//注释
         */
        else if (line.startsWith("/*") && !line.endsWith("*/") || ((line.startsWith("{/*") || line.startsWith("}/*")) && !line.endsWith("*/"))) {
            // 判断此行为"/*"开头的注释行
            commentLines++;
            comment = true;
        } else if (comment == true && !line.endsWith("*/") && !line.startsWith("*/")) {
            // 为多行注释中的一行（不是开头和结尾）
            notLine++;
            commentLines++;
        } else if (comment == true && (line.endsWith("*/") || line.startsWith("*/"))) {
            // 为多行注释的结束行
            commentLines++;
            comment = false;
        } else if (line.startsWith("//") || line.startsWith("}//") || line.startsWith("{//") ||
                ((line.startsWith("{/*") || line.startsWith("}/*") || line.startsWith("/*")) && line.endsWith("*/"))) {
            // 单行注释行
            commentLines++;
        } else {
            // 正常代码行
            //System.out.println(line);
            normalLines++;
        }
        long[] args={normalLines+notLine,whiteLines,commentLines- notLine};
        return args;
    }
    /**
     * 统计空格数
     * @param str
     * @return
     */
    public static int countSpace(String str) {
        int count = 0;
        Pattern p = Pattern.compile("\\s");
        Matcher m = p.matcher(str);
        while(m.find()){
            count++;
        }
        return count;
    }
    /**
     *包含stopLists
     *@param
     *@return int
     */
    static int countStopWords( BufferedReader thefile,String txt){
        int stopcount=0;
        int wordcount=0;
        File stopfile=new File(txt);
      //  File file=new File(thefile);
        ArrayList<String> stop=new ArrayList<String>(3);
//     读入stopfile.txt的单词到一个动态string数组中保存
        if(stopfile.exists()){
            try{
                FileInputStream fis=new FileInputStream(stopfile);
                InputStreamReader isr=new InputStreamReader(fis,"UTF-8");
                BufferedReader br=new BufferedReader(isr);
                String line=new String("");
                StringBuffer sb=new StringBuffer();
                TreeMap<String, Integer> map = new TreeMap<>();
                String[] split =null;
                while((line=br.readLine())!=null){
                    sb.append(line);
                    split = line.split("\\s+");
                    for (int i = 0; i < split.length; i++) {
//			          获取到每一个单词
                        Integer integer = map.get(split[i]);
//			          如果这个单词在map中没有，赋值1
                        if(null==integer){
                            map.put(split[i], 1);
                        }
                    }
                }
                Set<String> keySet = map.keySet();
                for (String string : keySet) {
                    stop.add(string);
                }
                br.close();
                isr.close();
                fis.close();
            }
            catch(FileNotFoundException e){
                e.printStackTrace();
            }
            catch(UnsupportedEncodingException e){
                e.printStackTrace();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        //统计stop表的总数目

            try{

                BufferedReader br= thefile;
                String line=new String("");
                StringBuffer sb=new StringBuffer();
                TreeMap<String, Integer> map = new TreeMap<>();
                while((line=br.readLine())!=null){
                    String[] split = line.split("\\s++|\\.|,|\\;|\\(|\\)|\\[|\\]|\\<|\\>|\\=|\\-|\\+|\\*|\\/|\\{|\\}");  //去除多个空格\\s+
                    for (int i = 0; i < split.length; i++) {
//			          获取到每一个单词
                        Integer integer = map.get(split[i]);
//			          如果这个单词在map中没有，赋值1
                        if(null==integer){
                            map.put(split[i], 1);
                        }else{
//			              如果有，在原来的个数上加上一
                            map.put(split[i], ++integer);
                        }
                    }
                }
//		      遍历，根据key获取所对应的value
                Set<String> keySet = map.keySet();
                for (String string : keySet) {
                    int i=0;
                    if(!(string.equals(""))){
                        wordcount+=map.get(string);
                        while(i<stop.size()){
                            if(string.equalsIgnoreCase(stop.get(i++)))//不区分大小写判断
                            {
                                stopcount+=map.get(string);
                                System.out.println(string+":"+map.get(string));
                            }}

                    }
                }
               // System.out.println(wordcount+"  "+stopcount+"  "+(wordcount-stopcount));
                sb.append(line);
                br.close();

            }
            catch(FileNotFoundException e){
                e.printStackTrace();
            }
            catch(UnsupportedEncodingException e){
                e.printStackTrace();
            }
            catch(IOException e){
                e.printStackTrace();
            }

        return (wordcount-stopcount);
    }

    /**
     *输出到文件
     *@param str,path
     *@return
     */
    public static void printToFile(String filepath,String str){
        File file= new File(filepath);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(filepath, true);
            PrintWriter out =new PrintWriter(fw);
            out.println(str);
            fw.flush();
            out.close();
            fw.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}

