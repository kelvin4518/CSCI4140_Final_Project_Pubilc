package hk.com.csci4140.culife.model;
import java.util.Comparator;


/**
 * Created by maoyuxuan(Michael Mao) on 10/05/2018.
 */

public class LetterComparator implements Comparator<UserContactModel>{
    @Override
    public int compare(UserContactModel UserContactModel, UserContactModel t1) {
        if (UserContactModel == null || t1 == null){
            return 0;
        }
        String lhsSortLetters = UserContactModel.getIndex().substring(0, 1).toUpperCase();
        String rhsSortLetters = t1.getIndex().substring(0, 1).toUpperCase();
        return lhsSortLetters.compareTo(rhsSortLetters);
    }
}
