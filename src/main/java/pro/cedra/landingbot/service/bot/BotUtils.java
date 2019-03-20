package pro.cedra.landingbot.service.bot;

import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Created by TIMUR on 19.11.2017.
 */
public class BotUtils {
    public static PhotoSize getPhoto(Message message) {
        // Check that the update contains a message and the message has a photo
        if (message.hasPhoto()) {
            // When receiving a photo, you usually get different sizes of it
            List<PhotoSize> photos = message.getPhoto();

            // We fetch the bigger photo
            return photos.stream()
                .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                .findFirst()
                .orElse(null);
        }

        // Return null if not found
        return null;
    }
    public static String getFilePath(PhotoSize photo, LandingBot bot) {
        Objects.requireNonNull(photo);

        if (photo.hasFilePath()) { // If the file_path is already present, we are done!
            return photo.getFilePath();
        } else {
            GetFile getFileMethod = new GetFile();
            getFileMethod.setFileId(photo.getFileId());
            try {
                File file = bot.execute(getFileMethod);
                return file.getFilePath();
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        return null; // Just in case
    }
    public static java.io.File downloadPhotoByFilePath(String filePath, LandingBot bot) {
        try {
            // Download the file calling AbsSender::downloadFile method
            return bot.downloadFile(filePath);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        return null;
    }
}
