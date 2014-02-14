import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import org.sdhanbit.mobile.android.entities.FeedEntry;

public class DatabaseConfigUtil extends OrmLiteConfigUtil {
    private static final Class<?>[] classes = new Class[] {
            FeedEntry.class,
    };
    public static void main(String[] args) throws Exception {
        writeConfigFile("ormlite_config.txt", classes);
    }
}