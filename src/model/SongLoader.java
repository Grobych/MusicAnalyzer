package model;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Alex on 13.01.2017.
 */
public class SongLoader {

    private static Song parseMP3(MP3File mp3){
        Song song = new Song();
        boolean ID1 = mp3.hasID3v1Tag();
        boolean ID2 = mp3.hasID3v2Tag();
        //System.out.println("Ver 1: "+ID1+" \nVer2: "+ID2);

        if (ID1){
            song.setArtist(mp3.getID3v1Tag().getArtist());
            song.setName(mp3.getID3v1Tag().getSongTitle());
            song.setAlbum(mp3.getID3v1Tag().getAlbumTitle());
        }else if (ID2){
            song.setArtist(mp3.getID3v2Tag().getLeadArtist());
            song.setName(mp3.getID3v2Tag().getSongTitle());
            song.setAlbum(mp3.getID3v2Tag().getAlbumTitle());
        }else System.out.println("No tag!");

        return song;
    }


    public static Song loadSong(String path){
        Song song;// = new Song();
        try {
            MP3File mp3 = new MP3File(path); /// !!!! some song get StringIndexOutOfBand Ex (-4)  !!!!
            song = parseMP3(mp3);
        } catch (IOException e) {
            System.out.println("File open error!");
            return null;
            //e.printStackTrace();
        } catch (TagException e) {
            System.out.println("No tag in file!");
            return null;
            //e.printStackTrace();
        }
        catch (StringIndexOutOfBoundsException e){
            System.out.println("ERROR   \n"+path+" Len: "+path.length());
            System.out.println(e);
            //e.printStackTrace();
            return null;
        }

        return song;
    }

    public static void loadSongList(List<File> fileList){
        for (File file : fileList){
            Song song = loadSong(file.getAbsolutePath());
            SongList.add(song);
        }
//        List<Song> list = fileList.stream().map(tmp -> SongLoader.loadSong(tmp.getAbsolutePath())).collect(Collectors.toList());
//        list.forEach(SongList::add);
     //   return list;
    }
}
