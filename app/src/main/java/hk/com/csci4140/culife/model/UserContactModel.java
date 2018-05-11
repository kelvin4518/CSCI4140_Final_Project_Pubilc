package hk.com.csci4140.culife.model;



import com.nanchen.wavesidebar.FirstLetterUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by maoyuxuan(Michael Mao) on 10/05/2018.
 */

/**
 * This class is to save the user data.
 */

public class UserContactModel {

    private String index;
    private String name;

    public UserContactModel(String name){
        this.index = FirstLetterUtil.getFirstLetter(name);
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }


    // TODO : get information from server
    public static List<UserContactModel> getContacts() {
        List<UserContactModel> contacts = new ArrayList<>();

        contacts.add(new UserContactModel("Andy"));
        contacts.add(new UserContactModel("阿姨"));
        contacts.add(new UserContactModel("爸爸"));
        contacts.add(new UserContactModel("Bear"));
        contacts.add(new UserContactModel("BiBi"));
        contacts.add(new UserContactModel("CiCi"));
        contacts.add(new UserContactModel("刺猬"));
        contacts.add(new UserContactModel("Dad"));
        contacts.add(new UserContactModel("弟弟"));
        contacts.add(new UserContactModel("妈妈"));
        contacts.add(new UserContactModel("哥哥"));
        contacts.add(new UserContactModel("姐姐"));
        contacts.add(new UserContactModel("奶奶"));
        contacts.add(new UserContactModel("爷爷"));
        contacts.add(new UserContactModel("哈哈"));
        contacts.add(new UserContactModel("测试"));
        contacts.add(new UserContactModel("自己"));
        contacts.add(new UserContactModel("You"));
        contacts.add(new UserContactModel("NearLy"));
        contacts.add(new UserContactModel("Hear"));
        contacts.add(new UserContactModel("Where"));
        contacts.add(new UserContactModel("怕"));
        contacts.add(new UserContactModel("嘻嘻"));
        contacts.add(new UserContactModel("123"));
        contacts.add(new UserContactModel("1508022"));
        contacts.add(new UserContactModel("2251"));
        contacts.add(new UserContactModel("****"));
        contacts.add(new UserContactModel("####"));
        contacts.add(new UserContactModel("w asad "));
        contacts.add(new UserContactModel("我爱你"));
        contacts.add(new UserContactModel("一百二十二"));
        contacts.add(new UserContactModel("壹"));
        contacts.add(new UserContactModel("I"));
        contacts.add(new UserContactModel("肆"));
        contacts.add(new UserContactModel("王八蛋"));
        contacts.add(new UserContactModel("zzz"));
        contacts.add(new UserContactModel("呵呵哒"));
        contacts.add(new UserContactModel("叹气"));
        contacts.add(new UserContactModel("南尘"));
        contacts.add(new UserContactModel("欢迎关注"));
        contacts.add(new UserContactModel("西西"));
        contacts.add(new UserContactModel("东南"));
        contacts.add(new UserContactModel("成都"));
        contacts.add(new UserContactModel("四川"));
        contacts.add(new UserContactModel("爱上学"));
        contacts.add(new UserContactModel("爱吖校推"));

        Collections.sort(contacts, new LetterComparator());
        return contacts;
    }
}
