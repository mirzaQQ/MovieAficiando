package dk.easv.movieaficionado.bll;

import dk.easv.movieaficionado.dal.dao.MovieDAO;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;

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

    public void playMovie(String path) throws IOException {
        File selected = new File(path);
        if (selected == null) return;
        if(Desktop.isDesktopSupported()){
            Desktop.getDesktop().open(selected);

        }
        MovieDAO movieDAO = new MovieDAO();
        movieDAO.insertDate(path);
    }
}
