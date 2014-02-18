import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import org.sdhanbit.mobile.android.entities.Category;
import org.sdhanbit.mobile.android.entities.FeedEntry;
import org.sdhanbit.mobile.android.entities.FeedEntryCategory;

public class DatabaseConfigUtil extends OrmLiteConfigUtil {
    private static final Class<?>[] classes = new Class[] {
            FeedEntry.class,
            Category.class,
            FeedEntryCategory.class
    };
    public static void main(String[] args) throws Exception {
        writeConfigFile("ormlite_config.txt", classes);
    }
}