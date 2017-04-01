package threads;

import data.Song;
import data.SongList;
import model.Analyze.Analyzer;

/**
 * Created by Alex on 01.04.2017.
 */
public class SongAnalyzeThread implements Runnable {

    @Override
    public void run() {
        if (SongList.getList().size()==0) return;
        Analyzer analyzer = new Analyzer();
        for (int i=0; i<SongList.getList().size(); i++) {
            Song song = analyzer.analyzeSong(SongList.getList().get(i));
            SongList.getList().set(i,song);
            System.out.println("CHECK");
        }
    }
}
