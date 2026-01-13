package dk.easv.movieaficionado.bll;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileOps {
    public FileOps() {

    }

    public String openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Find a movie file");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) return file.getAbsolutePath().toString();
        return null;
    }

    public boolean checkFile(String fileName) {
        String regex = "\\\\" ;
        String[] path = fileName.split(regex);
        String ChoosenFile =  path[path.length - 1];
        if(ChoosenFile.contains(".mp4") || ChoosenFile.contains(".mpeg4")) {
            return true;
        }
        return false;
    }
}
