package me.snnupai.door.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Random;

@Component
@Slf4j
public class AnonymousUtils implements InitializingBean{

    static HashMap<Integer, String> nameMap = new HashMap<>();
    static Random random = new Random();

    public static String getAnonymousNickName() {
        int size = nameMap.size();
        int index = random.nextInt(size);
        return nameMap.get(index);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {

            String path = System.getProperty("user.dir") ;
            System.out.println(path);
            File filename = new File(path + "/src/main/resources/name.txt");

            InputStreamReader inputStreamReader = new InputStreamReader(
                    new FileInputStream(filename), "gbk"); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(inputStreamReader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = "";
            while ((line = br.readLine()) != null) {
                if(line == null || line.trim().equals("")){
                    continue;
                }
                line = br.readLine(); // 一次读入一行数据

                String[] names = line.split("[ ,，.、]+");
                int nameMapSize = nameMap.size();
                for (String name:
                     names) {
                    name = name.trim();
                    if(name.equals("")){
                        continue;
                    }
                    nameMap.putIfAbsent(nameMapSize++, name);
                }
            }
            log.info("匿名名称一共有 " + nameMap.size() + "个");
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getAnonymousHeadUrl() {
        String head = String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000));
        return head;
    }
}
