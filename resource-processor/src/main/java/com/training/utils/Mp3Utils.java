package com.training.utils;

import com.training.entity.Song;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Mp3Utils {
    public static Song buildSongFromBytes(byte[] bytes, String resourceId) {
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext pcontext = new ParseContext();

        //Mp3 parser
        Mp3Parser Mp3Parser = new Mp3Parser();
        InputStream inputstream = new ByteArrayInputStream(bytes);

        try {
            Mp3Parser.parse(inputstream, handler, metadata, pcontext);
        } catch (IOException | TikaException | SAXException e) {
            throw new RuntimeException(e);
        }
        Song song = new Song();
        song.setResourceId(resourceId);
        song.setRate(Integer.parseInt(metadata.get("samplerate")));
        song.setDuration(Double.parseDouble(metadata.get("xmpDM:duration")));
        return song;
    }
}
